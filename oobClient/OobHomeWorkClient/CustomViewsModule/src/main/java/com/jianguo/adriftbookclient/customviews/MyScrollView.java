package com.jianguo.adriftbookclient.customviews;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Created by Administrator on 2016/4/25.
 */
public class MyScrollView extends ScrollView
{

    ArrayList<AbsListView> listViews;
    private boolean childrenStateChanged;//add or remove child
    private float downX, downY, moveX, moveY, upX, upY;
    private int slop = 0;
    private static final float VERTICAL_GRADIENT = 2;
    @Override public void addView(View child, int width, int height)
    {
        childrenStateChanged = true;
        super.addView(child, width, height);
    }
    @Override public void removeView(View view)
    {
        childrenStateChanged = true;
        super.removeView(view);
    }
    public MyScrollView(Context context)
    {
        super(context);
    }
    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    @Override public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (slop == 0)
            slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.e("dispatchTouchEvent", ev.getActionMasked() + "");
        return super.dispatchTouchEvent(ev);
    }
    private void resetCoordination()
    {
        downX = downY = moveX = moveY = upX = upY = 0;
    }
    private boolean isLastItemVisible(AbsListView lv)
    {
        final ListAdapter adapter = lv.getAdapter();
        if (null == adapter || adapter.isEmpty())
        {
            return true;
        }
        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = lv.getLastVisiblePosition();

        if (lastVisiblePosition >= lastItemPosition)
        {
           /* final int childIndex =
                    lastVisiblePosition - lv.getFirstVisiblePosition();
            final int index = Math.min(childIndex, childCount - 1);*/
            final int childCount = lv.getChildCount();
            final View lastVisibleChild = lv.getChildAt(childCount-1);
            if (lastVisibleChild != null)
            {
                return lastVisibleChild.getBottom() <= lv.getBottom();
            }
        }
        return false;
//        int location[]=new int[2];
//        lv.getLocationInWindow();
    }
    @Override public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean interceted = true;
        int action = ev.getActionMasked();
        int i = 0;
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                resetCoordination();
                if (listViews == null || childrenStateChanged)
                    listViews = getDecendantsListView(this);
                childrenStateChanged = false;
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                moveX = ev.getRawX();
                moveY = ev.getRawY();
                float distanceX = moveX - downX;
                float distanceY = moveY - downY;
                float gradient = Math.abs(distanceY / distanceX);
                /*
                tan（y/x）<2不认为是垂直滑动
                 */
                if (gradient <= VERTICAL_GRADIENT)
                    break;
                if (listViews != null && listViews.size() > 0)
                {
                    for (AbsListView lv : listViews)
                    {
                        /*//下拉刷新listview的时候不要拦截
                        if (distanceY >= slop && checkArea(ev, lv))
                            interceted = false;
                        *//*
                        上拉listview的时候，如果没有到达listview的底部，不拦截，如果到达，进行拦截
                         *//*
                        if (distanceY <= -slop && checkArea(ev, lv))
                            interceted = isLastItemVisible(lv) ? true : false;*/
                        if (checkArea(ev, lv))
                        {
                            interceted = false;
                            if (distanceY <= -slop)
                            /**
                             * 当前的item已经滑动到listview的底部，并且还在向下滑动
                             * 拦截该MotionEvent,同时ScrollView滚动
                             */
                                if (isLastItemVisible(lv))
                                {
                                    interceted = true;
                                    smoothScrollBy((int) distanceX, (int) distanceY);
                                } else
                                    interceted = false;
                        }
                    }
                }
                break;
        }
        return interceted == true ? super.onInterceptTouchEvent(ev) : false;
    }
    public static ArrayList<AbsListView> getDecendantsListView(ViewGroup container)
    {
        ArrayList<AbsListView> decendants = new ArrayList<AbsListView>();
        int firstChildCount = container.getChildCount();
        if (firstChildCount == 0)
            return decendants;
        MyQueue<View> myQueue = new MyQueue<View>();
        myQueue.enQueqe(container);
        while (!myQueue.isEmpty())
        {
            View view = myQueue.deQueqe();
            // 该组件是ViewGroup
            if (view instanceof AbsListView)
                decendants.add((AbsListView) (view));
            else if (view instanceof ViewGroup)
            {
                ViewGroup viewGroup = (ViewGroup) view;
                // myQueue.enQueqe(viewGroup);
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    if (viewGroup.getChildAt(i) instanceof ViewGroup)
                        myQueue.enQueqe(viewGroup.getChildAt(i));
            }
        }
        return decendants;
    }
    private boolean checkArea(MotionEvent ev, AbsListView lv)
    {
        int[] location = new int[2];
        lv.getLocationInWindow(location);
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();
        /**
         * be lenient about the range
         */
        if (rawX >= location[0] - slop &&
                rawX <= location[0] + lv.getWidth() + slop &&
                rawY >= location[1] - slop &&
                rawY <= location[1] + lv.getHeight() + slop)
            return true;
        return false;
    }
}

class MyQueue<T>
{

    // T element;
    LinkedList<T> linkedList;
    public boolean isEmpty()
    {
        return linkedList.isEmpty();
    }
    public MyQueue()
    {
        linkedList = new LinkedList<T>();
    }
    public void enQueqe(T t)
    {
        linkedList.addLast(t);
    }
    public T deQueqe()
    {
        if (linkedList.isEmpty())
            return null;
        else
        {
            return linkedList.pollFirst();
        }
    }
}
