package com.example.refreshlibrary;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * getMeasuredWidth()： 只要一执行完 setMeasuredDimension() 方法，就有值了，并且不再改变,getMeasuredWidth() 是实际测量的宽度（真实） 。
 * <p>
 * getWidth()：必须执行完 onMeasure() 才有值，可能发生改变,getWidth() 是实际显示的宽度。
 * <p>
 * 如果 onLayout 没有对子 View 实际显示的宽高进行修改，那么 getWidth() 的值 == getMeasuredWidth() 的值。
 */
public class RefreshView extends ViewGroup{

    private View mHeader;
    private View mFooter;
    private int mLayoutContentHeight;
    private int mLastMoveY;
    private TextView tvHeader;
    private TextView tvFooter;
    private WeakReference<Context> weakReference;

    // 用于平滑滑动的Scroller对象
    private Scroller mLayoutScroller;
    // 最小有效滑动距离(滑动超过该距离才视作一次有效的滑动刷新/加载操作)
    private static int mEffectiveScroll;
    private View rlPull;
    private ContentLoadingProgressBar pbRefreshing;
    // 监听事件
    private RefreshListener listener;

    public void setListener(RefreshListener listener) {
        this.listener = listener;
    }

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        weakReference = new WeakReference<>(context);
        initViewGroup(context);
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    }

    private void initViewGroup(Context context) {
        mLayoutScroller = new Scroller(context);
        mEffectiveScroll = DpSpPxUtils.dp2px(100, context);

        mHeader = LayoutInflater.from(context).inflate(R.layout.header, null);
        mFooter = LayoutInflater.from(context).inflate(R.layout.footer, null);

        tvHeader = mHeader.findViewById(R.id.tv_down);
        tvFooter = mFooter.findViewById(R.id.tv_up);

        rlPull = mHeader.findViewById(R.id.rl_pull);
        pbRefreshing = mHeader.findViewById(R.id.pb_refreshing);

    }

    /**
     * 当布局时调用
     * <p>
     * 当View分配所有的子元素的大小和位置时触发
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.e("Refresh", "onLayout");
        // 这里置为0，防止重复叠加
        mLayoutContentHeight = 0;

        View view;
        for (int i = 0; i < getChildCount(); i++) {
            view = getChildAt(i);
            // 如果是头部视图
            if (view.equals(mHeader)) {
                // 头部视图放在顶部
                view.layout(0, 0 - view.getMeasuredHeight(), view.getMeasuredWidth(), 0);
            }
            // 如果是底部视图
            else if (view.equals(mFooter)) {
                // 底部视图放在尾部
                view.layout(0,
                        mLayoutContentHeight,
                        view.getMeasuredWidth(),
                        mLayoutContentHeight + view.getMeasuredHeight());
            }
            // 内容视图根据定义(插入)顺序,按由上到下的顺序在垂直方向进行排列
            else {
                view.layout(0,
                        mLayoutContentHeight,
                        view.getMeasuredWidth(),
                        mLayoutContentHeight + view.getMeasuredHeight());
                mLayoutContentHeight += view.getMeasuredHeight();
            }
        }
    }

    /**
     * 当布局加载完成调用
     * <p>
     * 只会在从 xml 中加载时调用，并且只调用一次。当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        /**
         * 添加控件
         * */
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        params.height = DpSpPxUtils.dp2px(72, weakReference.get());
        mHeader.setLayoutParams(params);
        mFooter.setLayoutParams(params);
        addView(mHeader);
        addView(mFooter);
    }

    /**
     * 当尺寸改变调用
     * <p>
     * 当调用 onMeasure 测量后，发现尺寸和原来的不一致就调用该方法
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 当测量时调用,确定所有子元素的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastMoveY - y;
                Log.e("Refresh", getScrollY() + "");
                // dy小于0，表示下拉
                if (dy < 0) {
                    if (Math.abs(getScrollY()) >= mHeader.getMeasuredHeight() >> 2) {
                        tvHeader.setText("松开刷新...");
                    } else {
                        tvHeader.setText("下拉刷新...");
                    }
                } else {

                }
                scrollBy(0, dy);
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(getScrollY()) >= mEffectiveScroll){
                    mLayoutScroller.startScroll(0,
                            getScrollY() ,
                            0,
                            -(getScrollY() + mEffectiveScroll));
                    refreshBegin();
                    listener.onRefresh();
                } else {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
                postInvalidate();
                break;
        }

        mLastMoveY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mLayoutScroller.computeScrollOffset()) {
            scrollTo(0, mLayoutScroller.getCurrY());
        }
        postInvalidate();
    }

    /**
     * 刷新结束，改变界面数据
     * */
    public void refreshOver(){
        mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
        pbRefreshing.setVisibility(View.GONE);
        tvHeader.setText("下拉刷新...");
        rlPull.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新开始，改变界面数据
     * */
    public void refreshBegin(){
        rlPull.setVisibility(GONE);
        pbRefreshing.setVisibility(VISIBLE);
    }
}
