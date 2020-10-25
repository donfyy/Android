# LiveData

LiveData的实现基于Lifecycle。同时为数据提供了版本号。

一个观察者从头到尾只能绑定到一个Lifecycle。

观察者只有满足如下两个条件才会收到通知

- Lifecycle处于激活态
- 观察者的数据版本小于LiveData的数据版本

观察者收到通知的时间点如下

- Lifecycle激活时，具体来说就是Activity/Fragment的onStart()方法调用后
- 数据更新时

```java
    // 注册观察者，需要将观察者及生命周期组件(Activity/Fragment)一起传入
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        assertMainThread("observe");
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // 组件已经被销毁了，则忽略
            return;
        }
        // 绑定观察者和生命周期组件
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
        ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        // 不允许同一个观察者先后绑定到不同的生命周期组件
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        // 观察该生命周期组件
        owner.getLifecycle().addObserver(wrapper);
    }

    // 在主线程中设置数据
    @MainThread
    protected void setValue(T value) {
        assertMainThread("setValue");
        // 每设置一次数据，版本号加一
        mVersion++;
        // 将数据保存起来
        mData = value;
        // 分发数据
        dispatchingValue(null);
    }
    // 该方法是分发数据的入口。当数据发生变更时以及某个观察者的组件生命周期激活时调用
    void dispatchingValue(@Nullable ObserverWrapper initiator) {
        if (mDispatchingValue) {
            mDispatchInvalidated = true;
            return;
        }
        mDispatchingValue = true;
        do {
            mDispatchInvalidated = false;
            if (initiator != null) {
                // 某个观察者的组件生命周期激活了，为该观察者分发数据
                considerNotify(initiator);
                initiator = null;
            } else {
                // 数据发生变更了，则尝试将数据分发给每个观察者
                for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
                        mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
                    // 分发数据
                    considerNotify(iterator.next().getValue());
                    if (mDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (mDispatchInvalidated);
        mDispatchingValue = false;
    }

    @SuppressWarnings("unchecked")
    private void considerNotify(ObserverWrapper observer) {
        // 如果观察者没有激活，则不通知，组件生命周期激活时会通知更新
        if (!observer.mActive) {
            return;
        }
        // Check latest state b4 dispatch. Maybe it changed state but we didn't get the event yet.
        //
        // we still first check observer.active to keep it as the entrance for events. So even if
        // the observer moved to an active state, if we've not received that event, we better not
        // notify for a more predictable notification order.
        // 观察者是处于激活态的，但是生命周期组件是未激活的，此时LiveData还没有收到事件通知，
        // 则先将反激活观察者
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
        // 观察者是激活的，组件是激活的，但是观察者的数据版本是最新的，此时不用再通知了
        if (observer.mLastVersion >= mVersion) {
            return;
        }
        // 通知观察者更新数据，更新观察者的数据版本
        observer.mLastVersion = mVersion;
        observer.mObserver.onChanged((T) mData);
    }

    // 将观察者和生命周期组件绑定在一起的包装类
    class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
        @NonNull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
            super(observer);
            mOwner = owner;
        }

        @Override
        boolean shouldBeActive() {
            // 当生命周期组件处于Started/Resumed的状态时，认为该组件处于激活状态
            return mOwner.getLifecycle().getCurrentState().isAtLeast(STARTED);
        }

        // 组件生命周期发生变化时，在此处收到通知。
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source,
                @NonNull Lifecycle.Event event) {
            if (mOwner.getLifecycle().getCurrentState() == DESTROYED) {
                // 如果组件被销毁了，将观察者从LiveData中移除
                removeObserver(mObserver);
                return;
            }
            // 组件的生命周期发生变化时，通知观察者
            activeStateChanged(shouldBeActive());
        }

        @Override
        boolean isAttachedTo(LifecycleOwner owner) {
            return mOwner == owner;
        }

        // 组件被销毁时，自动调用该方法取消观察生命周期组件。
        @Override
        void detachObserver() {
            mOwner.getLifecycle().removeObserver(this);
        }
    }

    private abstract class ObserverWrapper {
        final Observer<? super T> mObserver;
        boolean mActive;
        int mLastVersion = START_VERSION;

        ObserverWrapper(Observer<? super T> observer) {
            mObserver = observer;
        }

        abstract boolean shouldBeActive();

        boolean isAttachedTo(LifecycleOwner owner) {
            return false;
        }

        void detachObserver() {
        }
        // 激活或者反激活观察者，组件生命周期发生变化时调用
        void activeStateChanged(boolean newActive) {
            // 观察者的状态和新状态一致，则不用处理
            if (newActive == mActive) {
                return;
            }
            // immediately set active state, so we'd never dispatch anything to inactive
            // owner
            // 立即保存状态
            mActive = newActive;
            // mActiveCount表示LiveData中处于激活态的观察者数量
            // wasInactive表示LiveData中是否存在激活的观察者
            boolean wasInactive = LiveData.this.mActiveCount == 0;
            LiveData.this.mActiveCount += mActive ? 1 : -1;
            if (wasInactive && mActive) {
                // 当前观察者是第一个激活的，则激活LiveData
                onActive();
            }
            if (LiveData.this.mActiveCount == 0 && !mActive) {
                // 当前观察者是最后一个反激活的，反激活LiveData
                onInactive();
            }
            if (mActive) {
                // 分发数据
                dispatchingValue(this);
            }
        }
    }

```

## 参考链接

[Android消息总线的演进之路：用LiveDataBus替代RxBus、EventBus](https://tech.meituan.com/2018/07/26/android-livedatabus.html)

