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

## Activity一像素保活

原理：手机锁屏时，启动一个一像素Activity让应用成为前台进程，解锁时，关闭该Activity。

## 启动前台服务保活

## 通过AccountManager添加账户并启用同步拉活

## 通过JobScheduler来拉活

## 通过WorkManager拉活

## 启动双进程服务保活