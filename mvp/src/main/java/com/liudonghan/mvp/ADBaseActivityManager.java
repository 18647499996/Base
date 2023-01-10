package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Stack;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/6/23
 */
public class ADBaseActivityManager {

    protected Stack<Activity> activityStack;

    private static volatile ADBaseActivityManager instance = null;

    private ADBaseActivityManager(){}

    public static ADBaseActivityManager getInstance(){
     //single chcekout
     if(null == instance){
        synchronized (ADBaseActivityManager.class){
            // double checkout
            if(null == instance){
                instance = new ADBaseActivityManager();
            }
        }
     }
     return instance;
    }


    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(@Nullable Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public FragmentActivity currentFragmentActivity(){
        FragmentActivity activity = (FragmentActivity) activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(@Nullable Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();

        }
        if (activityStack != null && activityStack.size() > 0 && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(@Nullable Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity != null && activity.getClass().equals(cls)) {
                activityStack.remove(activity);
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if (!activityStack.get(i).isFinishing()) {
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 遍历所有Activity并finish
     *
     * @param isExit true关闭所有activity false关闭部分activity(一般被挤下线后传false)
     */
    @SuppressLint("MissingPermission")
    public void exit(boolean isExit, Context context) {
        if (isExit) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(context.getPackageName());

            finishAllActivity();

            System.exit(0);
        } else {
            finishAllActivity();
        }
    }

    /**
     * 移除指定页面上面数据
     *
     * @param c
     */
    public void goBackTo(Class c) {
        while (!activityStack.isEmpty() && !(activityStack.peek().getClass() == c)) {
            activityStack.pop().finish();
        }
    }

    /**
     * 是否包含当前页面
     *
     * @param c class类
     * @return true 包含 false 不包含
     */
    public boolean isGoActivity(Class c) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass() == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含当前页面
     *
     * @param className class路径
     * @return true 包含 false 不包含
     */
    public boolean isGoActivity(String className) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getLocalClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
