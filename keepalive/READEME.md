# 保活拉活
源码版本（29）
- 系统出于体验和性能上的考虑，app在退到后台时系统并不会真正的kill掉这个进程，而是将其缓存起来
- 打开的应用越多，系统缓存的进程也越多
- 在内存不足的情况下，系统会依据一套进程回收机制来判断要kill掉哪些进程，以腾出来内存供给需要的app，
这套杀进程回收内存的机制就叫Low Memory Killer。

## 进程优先级

- 前台进程（关键优先级）
- 可见进程（高优先级）
- 服务进程
- 后台进程（低优先级）
- 空进程

## 杀死进程的时机

不同优先级的进程对应的内存阈值不同，一旦内存低于某一内存阈值，系统就会杀死对应优先级的进程。
例如：
```bash
generic_x86:/ $ su
generic_x86:/ # set -o vi
generic_x86:/ # cat /sys/module/lowmemorykiller/parameters/minfree
18432,23040,27648,32256,36864,46080
```
单位是4KB。

前台进程到空进程的内存阈值从左到右依次为72MB，90MB，108MB，126MB，180MB。

进程的优先级依据oom_adj值来进行判断，不同手机的oom_adj值可能不同
```bash
generic_x86:/ # cat proc/31714/oom_adj
0 # 前台
generic_x86:/ # cat proc/31714/oom_adj
11 # 后台
```
进程管理是在AMS中进行的，如下的ProcessList列出了部分oom_adj值的含义。
```java
class ProcessList {
    // 前台进程的adj值
    static final int FOREGROUND_APP_ADJ = 0;
    // 可见进程的adj值
    static final int VISIBLE_APP_ADJ = 100;
    // 服务进程的adj值
    static final int SERVICE_ADJ = 500;
    // adj值位置，进程将要被缓存
    static final int UNKNOWN_ADJ = 1001;
    // ...
}
```
adj算法涉及到AMS中三个方法
- updateOomAdjLocked()：更新adj，当目标进程为空，或者被杀则返回false，否则返回true
- computeOomAdjLocked()：计算adj，返回计算后的RawAdj值
- applyOomAdjLocked()：使用adj，当需要杀掉目标进程则返回false，否则返回true。
```java
class ActivityManagerService {
    // 更新指定app的OomAdj
    final boolean updateOomAdjLocked(ProcessRecord app, boolean oomAdjAll,
            String oomAdjReason) {
        return mOomAdjuster.updateOomAdjLocked(app, oomAdjAll, oomAdjReason);
    }
    // 更新所有进程的OomAdj
    final void updateOomAdjLocked(String oomAdjReason) {
        mOomAdjuster.updateOomAdjLocked(oomAdjReason);
    }
}
class OomAdjuster {
    // 更新指定app的OomAdj
    boolean updateOomAdjLocked(ProcessRecord app, boolean oomAdjAll,
            String oomAdjReason) {
        final ProcessRecord TOP_APP = mService.getTopAppLocked();
        final boolean wasCached = app.cached;

        mAdjSeq++;

        // This is the desired cached adjusment we want to tell it to use.
        // If our app is currently cached, we know it, and that is it.  Otherwise,
        // we don't know it yet, and it needs to now be cached we will then
        // need to do a complete oom adj.
        final int cachedAdj = app.getCurRawAdj() >= ProcessList.CACHED_APP_MIN_ADJ
                ? app.getCurRawAdj() : ProcessList.UNKNOWN_ADJ;
        // 更新指定app的OomAdj
        boolean success = updateOomAdjLocked(app, cachedAdj, TOP_APP, false,
                SystemClock.uptimeMillis());
        if (oomAdjAll
                && (wasCached != app.cached || app.getCurRawAdj() == ProcessList.UNKNOWN_ADJ)) {
            // Changed to/from cached state, so apps after it in the LRU
            // list may also be changed.
            // 如果app是切换到缓存态或者从缓存态切换出，那么在lru列表中app后的其他进程OomAdj的值可能会发生改变，
            // 所以要更新所有进程的OomAdj
            updateOomAdjLocked(oomAdjReason);
        }
        return success;
    }

    private final boolean updateOomAdjLocked(ProcessRecord app, int cachedAdj,
            ProcessRecord TOP_APP, boolean doingAll, long now) {
        if (app.thread == null) {
            return false;
        }
        // 计算app的OomAdj
        computeOomAdjLocked(app, cachedAdj, TOP_APP, doingAll, now, false);
        // 应用app的OomAdj
        return applyOomAdjLocked(app, doingAll, now, SystemClock.elapsedRealtime());
    }

    void updateOomAdjLocked(String oomAdjReason) {
        // ...
    }
}
```
## updateOomAdjLocked

过程比较复杂，主要分为更新adj(满足条件则杀进程)和根据memFactor来调度执行TrimMemory操作； 

第一部分：更新adj(满足条件则杀进程) 

- 遍历mLruProcesses进程 
  - 当进程未分配adj的情况
    - 当进程procState=14或15，则设置`adj=curCachedAdj(初始化=9)`; 
      - 当curCachedAdj != nextCachedAdj，且stepCached大于cachedFactor时 则`curCachedAdj = nextCachedAdj`，（nextCachedAdj加2，nextCachedAdj上限为15）；
    - 否则，则设置`adj=curEmptyAdj(初始化=9)`; 
      - 当curEmptyAdj != nextEmptyAdj，且stepEmpty大于EmptyFactor时 则`curEmptyAdj = nextEmptyAdj`，（nextEmptyAdj加2，nextEmptyAdj上限为15）；
  - 根据当前进程procState状态来决策： 
    - 当curProcState=14或15，且cached进程超过上限(cachedProcessLimit=16)，则杀掉该进程
    - 当curProcState=16的前提下： 
      - 当空进程超过上限(TRIM_EMPTY_APPS=8)，且空闲时间超过30分钟，则杀掉该进程
      - 否则，当空进程超过上限(emptyProcessLimit=16)，则杀掉该进程
  - 没有services运行的孤立进程，则杀掉该进程；

第二部分：根据memFactor来调度执行TrimMemory操作； 

- 根据CachedAndEmpty个数来调整内存因子memFactor(值越大，级别越高)： 
  - 当CachedAndEmpty < 3，则memFactor=3；
  - 当CachedAndEmpty < 5，则memFactor=2；
  - 当CachedAndEmpty >=5，且numCached<=5,numEmpty<=8，则memFactor=1；
  - 当numCached>5 或numEmpty>8，则memFactor=0；
- 当内存因子不是普通0级别的情况下，根据memFactor来调整前台trim级别(fgTrimLevel): 
  - 当memFactor=3，则fgTrimLevel=TRIM_MEMORY_RUNNING_CRITICAL；
  - 当memFactor=2，则fgTrimLevel=TRIM_MEMORY_RUNNING_LOW；
  - 否则(其实就是memFactor=1)，则fgTrimLevel=TRIM_MEMORY_RUNNING_MODERATE 
  - 再遍历mLruProcesses队列进程： 
    - 当curProcState > 12且没有被am杀掉，则执行TrimMemory操作；
    - 否则，当curProcState = 9 且trimMemoryLevel<TRIM_MEMORY_BACKGROUND，则执行TrimMemory操作；
    - 否则，当curProcState > 7， 且pendingUiClean =true时 
      - 当trimMemoryLevel<TRIM_MEMORY_UI_HIDDEN，则执行TrimMemory操作；
      - 当trimMemoryLevel<fgTrimLevel，则执行TrimMemory操作；
- 当内存因子等于0的情况下,遍历mLruProcesses队列进程： 
  - 当curProcState >=7, 且pendingUiClean =true时, 
    - 当trimMemoryLevel< TRIM_MEMORY_UI_HIDDEN，则执行TrimMemory操作；

## computeOomAdjLock()

### Service情况

当adj>0 或 schedGroup为后台线程组 或procState>2时： 

- 当service已启动，则procState<=10； 
  - 当service在30分钟内活动过，则adj=5,cached=false;
- 获取service所绑定的connections 
  - 当client与当前app同一个进程，则continue;
  - 当client进程的ProcState >=ActivityManager.PROCESS_STATE_CACHED_ACTIVITY，则设置为空进程
  - 当进程存在显示的ui，则将当前进程的adj和ProcState值赋予给client进程
  - 当不存在显示的ui，且service上次活动时间距离现在超过30分钟，则只将当前进程的adj值赋予给client进程
  - 当前进程adj > client进程adj的情况 
    - 当service进程比较重要时，则设置adj >= -11
    - 当client进程adj<2,且当前进程adj>2时，则设置adj=2;
    - 当client进程adj>1时，则设置adj = clientAdj
    - 否则，设置adj <= 1；
    - 若client进程不是cache进程，则当前进程也设置为非cache进程
  - 当绑定的是前台进程的情况 
    - 当client进程状态为前台时，则设置mayBeTop=true，并设置client进程procState=16
    - 当client进程状态 < 2的前提下：若绑定前台service，则clientProcState=3；否则clientProcState=6
  - 当connections并没有绑定前台service时，则clientProcState >= 7
  - 保证当前进程procState不会比client进程的procState大
- 当进程adj >0，且activity可见 或者resumed 或 正在暂停，则设置adj = 0

## applyOomAdjLocked

- curRawAdj != setRawAdj

- 进程当前OOM的校准 != 进程最后的OOM校准(app.curAdj != app.setAdj)
  - 将adj值 发送给lmkd守护进程
- 最后设置的调度组 != 当前所需的调度组(app.setSchedGroup != app.curSchedGroup)
  - 等待被杀
    - 杀进程，并设置success = false
  - else
    - 设置进程组信息
    - 调整进程的swappiness值
-  最近的前台Activity != 正在运行的前台活动(app.repForegroundActivities != app.foregroundActivities)
- 最近的进程状态 != 当前的进程状态（app.repProcState != app.curProcState）
  - 设置进程状态
- 当setProcState = -1或者curProcState与setProcState值不同时
  - 计算pss下次时间
- else
  - 当前时间超过pss下次时间，则请求统计pss,并计算pss下次时间
- 进程跟踪器最后的状态 != 当前的状态

## Activity一像素保活

原理：手机锁屏时，启动一个一像素Activity让应用成为前台进程，解锁时，关闭该Activity。

## 启动前台服务保活

## 通过AccountManager添加账户并启用同步拉活

## 通过JobScheduler来拉活

## 通过WorkManager拉活

## 启动双进程服务保活



## 参考链接

- [Android进程保活招数概览](https://www.jianshu.com/p/c1a9e3e86666)
- [Android 进程保活招式大全](https://segmentfault.com/a/1190000006251859)