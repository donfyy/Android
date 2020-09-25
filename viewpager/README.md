# ViewPager

## 源码分析
```java
class ViewPager {
    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (mAdapter != null) {
            // 清空已有的Adapter
            mAdapter.setViewPagerObserver(null);
            // 这里的代码流程比较固定，
            // 先调用Adapter.startUpdate 
            // 中间有0个或多个Adapter.instantiateItem或者Adapter.destroyItem
            // 然后再调用Adapter.finishUpdate
            mAdapter.startUpdate(this);
            for (int i = 0; i < mItems.size(); i++) {
                final ItemInfo ii = mItems.get(i);
                mAdapter.destroyItem(this, ii.position, ii.object);
            }
            mAdapter.finishUpdate(this);
            mItems.clear();
            removeNonDecorViews();
            mCurItem = 0;
            scrollTo(0, 0);
        }

        final PagerAdapter oldAdapter = mAdapter;
        mAdapter = adapter;
        mExpectedAdapterCount = 0;

        if (mAdapter != null) {
            if (mObserver == null) {
                mObserver = new PagerObserver();
            }
            // 设置观察者用来获得数据集变更的通知
            mAdapter.setViewPagerObserver(mObserver);
            mPopulatePending = false;
            final boolean wasFirstLayout = mFirstLayout;
            mFirstLayout = true;
            mExpectedAdapterCount = mAdapter.getCount();
            // 恢复当前ViewPager的状态
            if (mRestoredCurItem >= 0) {
                mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
                setCurrentItemInternal(mRestoredCurItem, false, true);
                mRestoredCurItem = -1;
                mRestoredAdapterState = null;
                mRestoredClassLoader = null;
            } else if (!wasFirstLayout) {
                // 如果不是第一次摆放，就去填充页面。
                populate();
            } else {
                requestLayout();
            }
        }

        // Dispatch the change to any listeners
        // 通知观察者Adapter发生了改变
        if (mAdapterChangeListeners != null && !mAdapterChangeListeners.isEmpty()) {
            for (int i = 0, count = mAdapterChangeListeners.size(); i < count; i++) {
                mAdapterChangeListeners.get(i).onAdapterChanged(this, oldAdapter, adapter);
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 在设计时，ViewPager的宽高不应该依赖于其子View，
        // 下面这条语句使得wrap_content的效果和match_parent对于ViewPager是一样的
        // 具体原因大家可以参考ViewGroup.getChildMeasureSpec()和getDefaultSize()的实现
        // 先测量出自身的宽高，不考虑子View
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        final int measuredWidth = getMeasuredWidth();
        final int maxGutterSize = measuredWidth / 10;
        // 默认间隙大小16dp
        mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);

        // 求出子View的宽高，在设计时子View的宽高和ViewPager就是一样的
        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        // 先测量装饰，比如tab
        int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    final int hgrav = lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int vgrav = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;
                    // 测量模式默认为AT_MOST
                    int widthMode = MeasureSpec.AT_MOST;
                    int heightMode = MeasureSpec.AT_MOST;
                    boolean consumeVertical = vgrav == Gravity.TOP || vgrav == Gravity.BOTTOM;
                    boolean consumeHorizontal = hgrav == Gravity.LEFT || hgrav == Gravity.RIGHT;

                    if (consumeVertical) {
                        widthMode = MeasureSpec.EXACTLY;
                    } else if (consumeHorizontal) {
                        heightMode = MeasureSpec.EXACTLY;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != LayoutParams.WRAP_CONTENT) {
                        // 如果子View的宽度布局参数不是WRAP_CONTENT，则将子View的测量模式设置为EXACTLY
                        widthMode = MeasureSpec.EXACTLY;
                        if (lp.width != LayoutParams.MATCH_PARENT) {
                            // 如果子View指定了具体的宽度，则使用该值
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != LayoutParams.WRAP_CONTENT) {
                        // 如果子View的高度布局参数不是WRAP_CONTENT，则将子View的测量模式设置为EXACTLY
                        heightMode = MeasureSpec.EXACTLY;
                        if (lp.height != LayoutParams.MATCH_PARENT) {
                            // 如果子View指定了具体的宽度，则使用该值
                            heightSize = lp.height;
                        }
                    }
                    final int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    final int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);

                    // 减去装饰使用的宽度和高度
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }
        // 生成子View的测量规则，并保存
        mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        // Make sure we have created all fragments that we need to have shown.
        // 填充页面View
        mInLayout = true;
        populate();
        mInLayout = false;

        // Page views next.
        // 测量每一页
        size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    final int widthSpec = MeasureSpec.makeMeasureSpec(
                            (int) (childWidthSize * lp.widthFactor), MeasureSpec.EXACTLY);
                    child.measure(widthSpec, mChildHeightMeasureSpec);
                }
            }
        }
    }

    void populate() {
        populate(mCurItem);
    }

    // newCurrentItem 表示新页的索引
    void populate(int newCurrentItem) {
        ItemInfo oldCurInfo = null;
        if (mCurItem != newCurrentItem) {
            // 切换到新的页，则获取原来显示的那一页的信息
            oldCurInfo = infoForPosition(mCurItem);
            mCurItem = newCurrentItem;
        }

        if (mAdapter == null) {
            // 没有设置adater就停止填充
            sortChildDrawingOrder();
            return;
        }

        // 等待填充，此时正在滑动
        if (mPopulatePending) {
            sortChildDrawingOrder();
            return;
        }

        // ViewPager没有添加到窗口上，也返回。
        if (getWindowToken() == null) {
            return;
        }
        
        // 调用mAdapter.startUpdate()方法
        mAdapter.startUpdate(this);

        // 根据缓存页数计算出要填充的起始页位置和结束页位置
        final int pageLimit = mOffscreenPageLimit;
        final int startPos = Math.max(0, mCurItem - pageLimit);
        final int N = mAdapter.getCount();
        final int endPos = Math.min(N - 1, mCurItem + pageLimit);

        // 检查不变式
        if (N != mExpectedAdapterCount) {
            // ...
        }

        // Locate the currently focused item or add it if needed.
        // 找到当前页在mItems中的索引
        int curIndex = -1;
        ItemInfo curItem = null;
        for (curIndex = 0; curIndex < mItems.size(); curIndex++) {
            final ItemInfo ii = mItems.get(curIndex);
            if (ii.position >= mCurItem) {
                if (ii.position == mCurItem) curItem = ii;
                break;
            }
        }

        // 当前页尚未被填充过，则生成当前页的信息
        if (curItem == null && N > 0) {
            curItem = addNewItem(mCurItem, curIndex);
        }

        // Fill 3x the available width or up to the number of offscreen
        // pages requested to either side, whichever is larger.
        // If we have no current item we have no work to do.
        // 填充左边和右边缓存的页，至少填满3倍的页面宽度，如果有padding要比缓存多填一页
        if (curItem != null) {
            // 左边已经排放的宽度
            float extraWidthLeft = 0.f;
            // 得到在mItems中的索引
            int itemIndex = curIndex - 1;
            ItemInfo ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
            final int clientWidth = getClientWidth();
            // 左边剩余的宽度
            final float leftWidthNeeded = clientWidth <= 0 ? 0 :
                    2.f - curItem.widthFactor + (float) getPaddingLeft() / (float) clientWidth;
            for (int pos = mCurItem - 1; pos >= 0; pos--) {
                // 左边剩余的宽度已经摆完了并且当前的位置的页不需要缓存
                if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                    // 没有为当前位置生成过Item，表示左边已经处理完了
                    if (ii == null) {
                        break;
                    }
                    // 只有ii是pos位置页的信息并且该页没有正在滑动
                    if (pos == ii.position && !ii.scrolling) {
                        // 移除左边不用缓存的页
                        mItems.remove(itemIndex);
                        // 调用mAdapter.destroyItem方法
                        mAdapter.destroyItem(this, pos, ii.object);
                        itemIndex--;
                        curIndex--;
                        ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
                    }
                } else if (ii != null && pos == ii.position) {
                    // 当前位置的页需要摆放在左边
                    extraWidthLeft += ii.widthFactor;
                    itemIndex--;
                    ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
                } else {
                    // 左边还有剩余的宽度或者需要创建pos处的页
                    ii = addNewItem(pos, itemIndex + 1);
                    extraWidthLeft += ii.widthFactor;
                    curIndex++;
                    ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
                }
            }

            // 右边页已经摆放的宽度系数
            float extraWidthRight = curItem.widthFactor;
            // 得到右边第一页在mItems中的索引
            itemIndex = curIndex + 1;
            // 如果右边可以摆放
            if (extraWidthRight < 2.f) {
                ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                // 右边可以摆放的宽度系数
                final float rightWidthNeeded = clientWidth <= 0 ? 0 :
                        (float) getPaddingRight() / (float) clientWidth + 2.f;
                for (int pos = mCurItem + 1; pos < N; pos++) {
                    // 右边的宽度无剩余了，并且pos处的页不需要缓存
                    if (extraWidthRight >= rightWidthNeeded && pos > endPos) {
                        if (ii == null) {
                            break;
                        }
                        // 如果ii是pos处页的信息并且该页没有在滑动
                        if (pos == ii.position && !ii.scrolling) {
                            // 删除该页，通知mAdapter
                            mItems.remove(itemIndex);
                            mAdapter.destroyItem(this, pos, ii.object);
                            ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                        }
                    } else if (ii != null && pos == ii.position) {
                        // 右边的宽度还有剩余，并且ii就是pos处的页，更新消费的宽度
                        extraWidthRight += ii.widthFactor;
                        itemIndex++;
                        ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                    } else {
                        // 右边的宽度有剩余，或者pos处的页需要缓存，并且pos处的页还没有被创建，则创建pos处的页
                        ii = addNewItem(pos, itemIndex);
                        itemIndex++;
                        extraWidthRight += ii.widthFactor;
                        ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
                    }
                }
            }

            // 计算每一页的偏移量
            calculatePageOffsets(curItem, curIndex, oldCurInfo);
            // 通知mAdapter当前页的位置
            mAdapter.setPrimaryItem(this, mCurItem, curItem.object);
        }
        // ...
        // 通知mAdapter完成更新
        mAdapter.finishUpdate(this);

        // Check width measurement of current pages and drawing sort order.
        // Update LayoutParams as needed.
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.childIndex = i;
            if (!lp.isDecor && lp.widthFactor == 0.f) {
                // 0 means requery the adapter for this, it doesn't have a valid width.
                final ItemInfo ii = infoForChild(child);
                if (ii != null) {
                    lp.widthFactor = ii.widthFactor;
                    lp.position = ii.position;
                }
            }
        }
        sortChildDrawingOrder();

        // 处理焦点
        if (hasFocus()) {
            View currentFocused = findFocus();
            ItemInfo ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
            if (ii == null || ii.position != mCurItem) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    ii = infoForChild(child);
                    if (ii != null && ii.position == mCurItem) {
                        if (child.requestFocus(View.FOCUS_FORWARD)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    // position 页面的索引
    // index 在mItems中的在position后面的已经填充的页的索引
    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        // 调用mAdapter的instantiateItem方法得到一页
        ii.object = mAdapter.instantiateItem(this, position);
        // 页的宽度因子，表示每一页View在ViewPager的内容宽度中占据多少
        // 1表示每一页的宽度和ViewPager的内容宽度相同
        ii.widthFactor = mAdapter.getPageWidth(position);
        if (index < 0 || index >= mItems.size()) {
            mItems.add(ii);
        } else {
            mItems.add(index, ii);
        }
        return ii;
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        final int N = mAdapter.getCount();
        final int width = getClientWidth();
        final float marginOffset = width > 0 ? (float) mPageMargin / width : 0;
        // Fix up offsets for later layout.
        if (oldCurInfo != null) {
            final int oldCurPosition = oldCurInfo.position;
            // Base offsets off of oldCurInfo.
            // 新的中心页在老的中心页右侧
            if (oldCurPosition < curItem.position) {
                int itemIndex = 0;
                ItemInfo ii = null;
                float offset = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;
                // 以老的中心页的偏移量为基础，计算新加入的页的偏移量
                for (int pos = oldCurPosition + 1;
                        pos <= curItem.position && itemIndex < mItems.size(); pos++) {
                    ii = mItems.get(itemIndex);
                    // 找到pos页对应的ItemInfo
                    while (pos > ii.position && itemIndex < mItems.size() - 1) {
                        itemIndex++;
                        ii = mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        // We don't have an item populated for this,
                        // ask the adapter for an offset.
                        // 找不到pos页对应的ItemInfo，从Adapter处获取宽度因子
                        offset += mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                }
            } else if (oldCurPosition > curItem.position) {
                int itemIndex = mItems.size() - 1;
                ItemInfo ii = null;
                float offset = oldCurInfo.offset;
                for (int pos = oldCurPosition - 1;
                        pos >= curItem.position && itemIndex >= 0; pos--) {
                    ii = mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        itemIndex--;
                        ii = mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        // We don't have an item populated for this,
                        // ask the adapter for an offset.
                        offset -= mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                }
            }
        }

        // Base all offsets off of curItem.
        // 基于中心页，设置偏移量
        final int itemCount = mItems.size();
        // 当前页左边界的偏移量
        float offset = curItem.offset;
        int pos = curItem.position - 1;
        mFirstOffset = curItem.position == 0 ? curItem.offset : -Float.MAX_VALUE;
        // 感觉这里 -1 不太严谨
        mLastOffset = curItem.position == N - 1
                ? curItem.offset + curItem.widthFactor - 1 : Float.MAX_VALUE;
        // Previous pages
        // 计算左边页的偏移量
        for (int i = curIndex - 1; i >= 0; i--, pos--) {
            final ItemInfo ii = mItems.get(i);
            while (pos > ii.position) {
                offset -= mAdapter.getPageWidth(pos--) + marginOffset;
            }
            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) mFirstOffset = offset;
        }
        offset = curItem.offset + curItem.widthFactor + marginOffset;
        pos = curItem.position + 1;
        // Next pages
        // 计算右边页的偏移量
        for (int i = curIndex + 1; i < itemCount; i++, pos++) {
            final ItemInfo ii = mItems.get(i);
            while (pos < ii.position) {
                // 从这些算法中可以看出，将mPageMargin和View的宽度之和作为逻辑上的一页
                offset += mAdapter.getPageWidth(pos++) + marginOffset;
            }
            if (ii.position == N - 1) {
                // 最后一页的起始位置的偏移
                mLastOffset = offset + ii.widthFactor - 1;
            }
            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;
        }

        mNeedCalculatePageOffsets = false;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        final int scrollX = getScrollX();

        int decorCount = 0;

        // First pass - decor views. We need to do this in two passes so that
        // we have the proper offsets for non-decor views later.
        // 摆放装饰View
        // ...

        // 整个ViewPager可以显示一页的宽度，这一页的宽度由PageMargin与View组成
        final int childWidth = width - paddingLeft - paddingRight;
        // Page views. Do this once we have the right padding offsets from above.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ItemInfo ii;
                if (!lp.isDecor && (ii = infoForChild(child)) != null) {
                    // 整个摆放逻辑，最重要的是这个ItemInfo.offset的计算
                    // 这个offset是以页的宽度和mPageMargin作为一个逻辑单位
                    int loff = (int) (childWidth * ii.offset);
                    int childLeft = paddingLeft + loff;
                    int childTop = paddingTop;
                    if (lp.needsMeasure) {
                        // This was added during layout and needs measurement.
                        // Do it now that we know what we're working with.
                        // 如果需要测量，在这里测量子View
                        lp.needsMeasure = false;
                        final int widthSpec = MeasureSpec.makeMeasureSpec(
                                (int) (childWidth * lp.widthFactor),
                                MeasureSpec.EXACTLY);
                        final int heightSpec = MeasureSpec.makeMeasureSpec(
                                (int) (height - paddingTop - paddingBottom),
                                MeasureSpec.EXACTLY);
                        child.measure(widthSpec, heightSpec);
                    }
                    // 摆放子View
                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                }
            }
        }
        mTopPageBounds = paddingTop;
        mBottomPageBounds = height - paddingBottom;
        mDecorChildCount = decorCount;

        if (mFirstLayout) {
            // 这里待会再来分析
            // 这是ViewPager滑动的核心代码了，我们从onTouchEvent看起
            scrollToItem(mCurItem, false, 0, false);
        }
        mFirstLayout = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the margin drawable between pages if needed.
        if (mPageMargin > 0 && mMarginDrawable != null && mItems.size() > 0 && mAdapter != null) {
            final int scrollX = getScrollX();
            final int width = getWidth();

            final float marginOffset = (float) mPageMargin / width;
            int itemIndex = 0;
            ItemInfo ii = mItems.get(0);
            float offset = ii.offset;
            final int itemCount = mItems.size();
            final int firstPos = ii.position;
            final int lastPos = mItems.get(itemCount - 1).position;
            for (int pos = firstPos; pos < lastPos; pos++) {
                while (pos > ii.position && itemIndex < itemCount) {
                    ii = mItems.get(++itemIndex);
                }

                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * width;
                    offset += widthFactor + marginOffset;
                }

                if (drawAt + mPageMargin > scrollX) {
                    // 只绘制可显示的页边距
                    mMarginDrawable.setBounds(Math.round(drawAt), mTopPageBounds,
                            Math.round(drawAt + mPageMargin), mBottomPageBounds);
                    mMarginDrawable.draw(canvas);
                }

                if (drawAt > scrollX + width) {
                    break; // No more visible, no sense in continuing
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // ... 
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                // ...
                break;
            }
            case MotionEvent.ACTION_MOVE:
                // ...
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    // 得到手指抬起时的速度
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                    mPopulatePending = true;
                    final int width = getClientWidth();
                    final int scrollX = getScrollX();
                    final ItemInfo ii = infoForCurrentScrollPosition();
                    final float marginOffset = (float) mPageMargin / width;
                    final int currentPage = ii.position;
                    final float pageOffset = (((float) scrollX / width) - ii.offset)
                            / (ii.widthFactor + marginOffset);
                    final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(activePointerIndex);
                    final int totalDelta = (int) (x - mInitialMotionX);
                    // 决定当前应该滑到哪一页
                    int nextPage = determineTargetPage(currentPage, pageOffset, initialVelocity,
                            totalDelta);
                    // 手指抬起时滑动到目标页
                    setCurrentItemInternal(nextPage, true, true, initialVelocity);

                    needsInvalidate = resetTouch();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    scrollToItem(mCurItem, true, 0, false);
                    needsInvalidate = resetTouch();
                }
                break;
            // ...
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        // ...        

        final int pageLimit = mOffscreenPageLimit;
        if (item > (mCurItem + pageLimit) || item < (mCurItem - pageLimit)) {
            // We are doing a jump by more than one page.  To avoid
            // glitches, we want to keep all current pages in the view
            // until the scroll ends.
            // 当要滑动到非缓存的页面时，设置scrolling标记，通知populate不要删除这些页
            for (int i = 0; i < mItems.size(); i++) {
                mItems.get(i).scrolling = true;
            }
        }
        final boolean dispatchSelected = mCurItem != item;

        if (mFirstLayout) {
            // We don't have any idea how big we are yet and shouldn't have any pages either.
            // Just set things up and let the pending layout handle things.
            // 第一次布局还没发生，保留当前页，通知用户中心页发生改变就行了，后面会在onMeasure里去填充页面
            mCurItem = item;
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
            }
            requestLayout();
        } else {
            // 填充页面，然后滑动到指定页
            populate(item);
            scrollToItem(item, smoothScroll, velocity, dispatchSelected);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity,
            boolean dispatchSelected) {
        final ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            final int width = getClientWidth();
            // 计算出在x轴上的目标位置
            destX = (int) (width * Math.max(mFirstOffset,
                    Math.min(curInfo.offset, mLastOffset)));
        }
        if (smoothScroll) {
            // 平滑滑动到目标位置
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
            }
        } else {
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
            }
            completeScroll(false);
            scrollTo(destX, 0);
            // 直接滑动到目标位置
            pageScrolled(destX);
        }
    }
    void smoothScrollTo(int x, int y, int velocity) {
        // ...
        mIsScrollStarted = false;
        // 开始惯性滑动
        mScroller.startScroll(sx, sy, dx, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        mIsScrollStarted = true;
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                // 滑动到此时应该滑动到的位置
                scrollTo(x, y);
                // 调用pageScrolled进行滑动
                if (!pageScrolled(x)) {
                    mScroller.abortAnimation();
                    scrollTo(0, y);
                }
            }

            // Keep on drawing until the animation has finished.
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }

        // Done with scroll, clean up state.
        completeScroll(true);
    }

    private boolean pageScrolled(int xpos) {
        // ...
        final ItemInfo ii = infoForCurrentScrollPosition();
        final int width = getClientWidth();
        final int widthWithMargin = width + mPageMargin;
        final float marginOffset = (float) mPageMargin / width;
        final int currentPage = ii.position;
        // 页面偏移量
        final float pageOffset = (((float) xpos / width) - ii.offset)
                / (ii.widthFactor + marginOffset);
        // 页面偏移像素
        final int offsetPixels = (int) (pageOffset * widthWithMargin);
        mCalledSuper = false;
        onPageScrolled(currentPage, pageOffset, offsetPixels);
        return true;
    }
  
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        // Offset any decor views if needed - keep them on-screen at all times.
        // 处理装饰的滑动
        // ...

        // 分发给onPageScrolled的监听者
        dispatchOnPageScrolled(position, offset, offsetPixels);

        // 应用在页面上的变换效果
        if (mPageTransformer != null) {
            final int scrollX = getScrollX();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (lp.isDecor) continue;
                // 求出child的偏移，transformPos是负值表示向左滑动，正值表示向右滑动
                final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
                mPageTransformer.transformPage(child, transformPos);
            }
        }

        mCalledSuper = true;
    }
}
```


## 解决ViewPager无法包裹内容的问题

```kotlin
class MyViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 先去测量下每一个子View
            child.measure(widthMeasureSpec, getChildMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),
                    paddingLeft + paddingRight, child.layoutParams.height))
            val h = child.measuredHeight
            // 记下最大高度
            if (h > height) {
                height = h
            }
        }
        if (height == 0) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)))
    }
}
```