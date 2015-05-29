package com.johnnyyin.activitylaunchmode;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;

import java.util.HashMap;
import java.util.Stack;

public class BaseApplication extends Application {
    private boolean intentFilterMode;
    private ActivityManager manager;
    private HashMap<Integer, Stack<BaseActivity>> tasks;

    @SuppressWarnings("ResourceType")
    public void onCreate() {
        super.onCreate();
        this.manager = (ActivityManager) getSystemService("activity");
        this.tasks = new HashMap();
    }

    public void pushToStack(BaseActivity activity) {
        int currentTaskId = getCurrentTaskId();
        if (!this.tasks.containsKey(Integer.valueOf(currentTaskId))) {
            this.tasks.put(Integer.valueOf(currentTaskId), new Stack());
        }
        ((Stack) this.tasks.get(Integer.valueOf(currentTaskId))).add(activity);
    }

    public int getCurrentTaskId() {
        return ((RunningTaskInfo) this.manager.getRunningTasks(1).get(0)).id;
    }

    public void removeFromStack(BaseActivity activity) {
        Stack<BaseActivity> stack = (Stack) this.tasks.get(Integer.valueOf(getCurrentTaskId()));
        if (stack != null) {
            stack.remove(activity);
        }
    }

    public Stack<BaseActivity> getCurrentTask() {
        return (Stack) this.tasks.get(Integer.valueOf(getCurrentTaskId()));
    }

    public void toggleIntentFilterMode() {
        this.intentFilterMode = !this.intentFilterMode;
    }

    public boolean isIntentFilterMode() {
        return this.intentFilterMode;
    }
}