package com.johnnyyin.activitylaunchmode;

import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Stack;

public class TaskInfoDisplayer implements Runnable, Constants {
    private final BaseApplication app;
    private final TextView taskIdField;
    private final LinearLayout taskView;

    public TaskInfoDisplayer(BaseActivity baseActivity) {
        this.app = (BaseApplication) baseActivity.getApplication();
        this.taskIdField = (TextView) baseActivity.findViewById(R.id.task_id_field);
        this.taskView = (LinearLayout) baseActivity.findViewById(R.id.task_view);
        this.taskView.removeAllViews();
    }

    public void run() {
        Log.v(Constants.LOG_TAG, "===============================");
        showCurrentTaskId();
        showCurrentTaskActivites();
        Log.v(Constants.LOG_TAG, "===============================");
    }

    private void showCurrentTaskId() {
        int taskId = this.app.getCurrentTaskId();
        String taskMessage = "Activities in current task (id: " + taskId + ")";
        this.taskIdField.setText("Task id: " + taskId);
        Log.v(Constants.LOG_TAG, taskMessage);
    }

    private void showCurrentTaskActivites() {
        Stack<BaseActivity> task = this.app.getCurrentTask();
        if (task == null)
            return;
        for (int location = task.size() - 1; location >= 0; location--) {
            showActivityDetails(task.get(location));
        }
    }

    private void showActivityDetails(BaseActivity activity) {
        Log.v(Constants.LOG_TAG, activity.getClass().getSimpleName());
        this.taskView.addView(getActivityRepresentation(activity));
    }

    private ImageView getActivityRepresentation(BaseActivity activity) {
        ImageView image = new ImageView(activity);
        image.setBackgroundResource(activity.getBackgroundColour());
        LayoutParams params = new LayoutParams(-1, 10);
        params.setMargins(2, 2, 2, 2);
        image.setLayoutParams(params);
        return image;
    }
}