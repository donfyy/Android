# 事件分发

```java
// 1
class Activity {
    // Activity会在该方法收到触摸事件的通知
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // ...
        // Activity在此处将事件分发给Window
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
}
// 2
class PhoneWindow {
    @Override
    public boolean superDispatchTouchEvent(MotionEvent event) {
        // PhoneWindow又将事件分发给View树的根结点DecorView
        return mDecor.superDispatchTouchEvent(event);
    }
}
// 3
class DecorView extends FrameLayout {
    public boolean superDispatchTouchEvent(MotionEvent event) {
        // DecorView调用父类的方法分发事件
        // DecorView继承自FrameLayout，而FrameLayout并未重写dispatchTouchEvent
        // 最终将调用ViewGroup的dispatchTouchEvent方法
        return super.dispatchTouchEvent(event);
    }
}
// 4
class ViewGroup {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // ...
        // 该变量用来记录事件是否被消费
        boolean handled = false;
        // 整个事件的处理逻辑在这个if语句块中
        if (onFilterTouchEventForSecurity(ev)) {
            final int action = ev.getAction();
            final int actionMasked = action & MotionEvent.ACTION_MASK;
            // 当前事件如果是down事件，则清理状态
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // 给所有的事件消费者发送一个取消事件，然后清空所有事件消费者
                cancelAndClearTouchTargets(ev);
                // 重置处理一个事件序列所用到的状态，如FLAG_DISALLOW_INTERCEPT，事件消费者等
                resetTouchState();
            }

            // 检查是否拦截当前的事件
            final boolean intercepted;
            // 如果当前事件是down事件，或者不是down事件但是有子View在消费该事件序列，则执行如下if语句块
            // 这里有两层意思
            // 1.如果是down事件，该ViewGroup一定会执行检查是否拦截的逻辑
            // 2.如果不是down事件并且也没有子View要消费事件，则ViewGroup不会执行是否拦截的逻辑，
            // 换句话说，事件一定会被拦截
            // 重点1：该语句块用来判断是否拦截
            if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                // FLAG_DISALLOW_INTERCEPT 用来判断是否执行该ViewGroup自身的onInterceptTouchEvent()方法
                // 该标记是通过requestDisallowInterceptTouchEvent()方法设置的，
                // 因此子View可以通过设置该标记告诉父View不要执行onInterceptTouchEvent()方法
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    // 这里调用了该ViewGroup自身的onInterceptTouchEvent()方法，
                    // 也就是自定义ViewGroup以及处理事件冲突会重写的方法
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
            } else {
                intercepted = true;
            }
            // ...
            // resetCancelNextUpFlag 是用来判断该View是否从父View中移除，或者是否是鼠标右键已经按下过
            // 这里不用关心此标记，因此canceled在此处可以简单的理解为当前是否是取消事件
            final boolean canceled = resetCancelNextUpFlag(this)
                    || actionMasked == MotionEvent.ACTION_CANCEL;

            // Update list of touch targets for pointer down, if needed.
            final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;
            TouchTarget newTouchTarget = null;
            boolean alreadyDispatchedToNewTouchTarget = false;
            // 如果不是取消并且也不拦截，则执行如下语句块
            // 重点2：该语句块用来寻找消费事件的子View
            if (!canceled && !intercepted) {
                // ...
                // ACTION_HOVER_MOVE不是触摸事件，这里就不分析了
                // 当如下条件之一满足则寻找消费事件的子View
                // 1.当前事件是第一个down事件
                // 2.当前事件不是第一个down事件
                // 总之只有down事件才会去寻找消费事件的子View
                if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                    // ...
                    final int childrenCount = mChildrenCount;
                    if (newTouchTarget == null && childrenCount != 0) {
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);
                        // 生成一个按照优先级排序的子View列表，该列表从后向前遍历，因此子View的索引越大优先级越高
                        final ArrayList<View> preorderedList = buildTouchDispatchChildList();
                        // 这里有点绕。。。 这个判断考虑到了标记A和标记B的实现
                        final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                        final View[] children = mChildren;
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            // 标记A
                            final int childIndex = getAndVerifyPreorderedIndex(
                                    childrenCount, i, customOrder);
                            // 标记B
                            final View child = getAndVerifyPreorderedView(
                                    preorderedList, children, childIndex);
                            // ... 
                            // 如果子View不能接收触摸事件或者触摸点不在子View的范围内，则寻找下一个View
                            if (!child.canReceivePointerEvents()
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                                ev.setTargetAccessibilityFocus(false);
                                continue;
                            }

                            // 子View可以处理触摸事件并且触摸点落在子View的可视范围内。
                            // 判断子View已经在事件消费者列表中
                            newTouchTarget = getTouchTarget(child);
                            if (newTouchTarget != null) {
                                // ...
                                break;
                            }

                            resetCancelNextUpFlag(child);
                            // 子View不在事件消费者列表中，则把事件分发给子View，询问子View是否消费事件。
                            // 该方法会调用子View的dispatchTouchEvent方法，由此可见事件分发是一个dfs算法
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // ...
                                // 将消费Down事件的子view加入到事件消费者链表 ，然后跳出循环。
                                // 至此，找到并记录了要消费事件的子View
                                // A
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }
                            // ...
                       }
                        // ...
                    }
                    // ...
                }
            }

            // Dispatch to touch targets.
            // 重点3：分发事件
            if (mFirstTouchTarget == null) {
                // 如果没有事件消费者， 则将事件分发给ViewGroup自身
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
            } else {
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        // 在分发down事件时，该事件已经被消费了。
                        handled = true;
                    } else {
                        // 重点4：判断是继续给事件消费者发送事件，还是给事件消费者发送一个取消事件
                        // 当ViewGroup自身在当前事件决定要拦截时，ViewGroup会给所有的事件消费者发送一个取消事件
                        // 也就说，在ViewGroup在事件A到来时决定要拦截随后的事件，则ViewGroup不会消费事件A，
                        // 而是将当前事件的类型设置为取消事件，然后发送给所有的事件消费者
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                || intercepted;
                        if (dispatchTransformedTouchEvent(ev, cancelChild,
                                target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        if (cancelChild) {
                            // 移除事件消费者。
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }
            // ...
       }
        // ...
       return handled;
    }
}
```

总结一下，```ViewGroup.dispatchTouchEvent(MotionEvent)``` 的方法体主要有三个处理阶段

1. 判断是否拦截

    1.如果当前事件不是down事件并且没有消费事件的子View，则拦截。否则执行下步骤2
    
    2.判断 FLAG_DISALLOW_INTERCEPT 是否设置，如果设置，不拦截。否则执行步骤3
    
    3.调用自身 onInterceptTouchEvent(MotionEvent) 方法，该方法的返回值表示是否拦截。
    
2. 寻找消费事件的子View

    1.如果当前事件是取消事件或者自身拦截事件，则结束执行。
    
    2.如果当前事件不是Down事件，则结束执行。
    
    3.如果没有子View，则结束执行。
    
    4.取出一个子View。
    
    5.如果该子View不能接收触摸事件或者触摸点不在该子View的范围内，执行步骤3。
    
    6.询问该子View是否消费事件，如果不消费，执行步骤3。
    
    7.将该子View添加至事件消费者链表，结束执行。
    
3. 分发事件

    1.如果没有事件消费者，则将事件分发给ViewGroup自身，结束执行。
    
    2.如果没有事件消费者则结束执行。
    
    3.取出一个事件消费者。
    
    4.如果该事件消费者是在第二大步骤中（寻找消费事件的子View）新添加的，则执行步骤2。
    
    5.如果当前不拦截，则将该事件分发给该事件消费者，执行步骤2。
    
    6.将一个取消事件分发给事件消费者。
    
    7.从链表中移除事件消费者。执行步骤2