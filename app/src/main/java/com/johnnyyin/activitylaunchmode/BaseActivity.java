package com.johnnyyin.activitylaunchmode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity
        extends Activity
        implements Constants {
    private final int DISPLAY_STACK_DELAY = 500;
    private BaseApplication app;
    private int chosenFlags;
    private Handler handler = new Handler();
    private int[] intentFlags = {Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, Intent.FLAG_ACTIVITY_FORWARD_RESULT, Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
            Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_NO_HISTORY, Intent.FLAG_ACTIVITY_NO_USER_ACTION, Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, Intent.FLAG_ACTIVITY_SINGLE_TOP};
    private String[] intentFlagsText = {"CLEAR_TOP", "CLEAR_WHEN_TASK_RESET", "EXCLUDE_FROM_RECENTS", "FORWARD_RESULT", "MULTIPLE_TASK", "NEW_TASK", "NO_HISTORY", "NO_USER_ACTION", "PREVIOUS_IS_TOP", "REORDER_TO_FRONT", "RESET_TASK_IF_NEEDED", "SINGLE_TOP"};
    private TextView lifecycle;
    private StringBuilder lifecycleFlow = new StringBuilder();

    private void checkIfReorderToFront() {
        if (shouldReorderToFront(getIntent())) {
            this.app.removeFromStack(this);
            this.app.pushToStack(this);
        }
    }

    private String getLaunchMode() {
        return "[" + hashCode() + "] " + getClass().getSimpleName();
    }

    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    private Intent getNextIntent(View v) {
        Class<? extends BaseActivity> nextActivity = null;
        switch (v.getId()) {
            case R.id.btn_standard:
                nextActivity = Standard.class;
                break;
            case R.id.btn_singletop:
                nextActivity = SingleTop.class;
                break;
            case R.id.btn_singletask:
                nextActivity = SingleTask.class;
                break;
            case R.id.btn_singleInstance:
                nextActivity = SingleInstance.class;
                break;
        }
        return new Intent(this, nextActivity);
    }

    private void logMethodName() {
        String str = getMethodName();
        Log.v("ActivitesLaunchDemo", getLaunchMode() + ": " + str);
        this.lifecycleFlow.append(str).append("\n");
        if (this.lifecycle != null) {
            this.lifecycle.setText(this.lifecycleFlow.toString());
        }
    }

    private void prepareIntentFiltedDialog(AlertDialog.Builder paramBuilder) {
    }

    private void setupView() {
        findViewById(R.id.main_layout).setBackgroundResource(getBackgroundColour());
        ((TextView) findViewById(R.id.header)).setText(getLaunchMode());
        this.lifecycle = (TextView) findViewById(R.id.lifecycle_content);
        this.lifecycle.setMovementMethod(new ScrollingMovementMethod());
    }

    private boolean shouldReorderToFront(Intent paramIntent) {
        return (0x20000 & paramIntent.getFlags()) > 0;
    }

    private void showIntentFilterDialog(final View nextActivityBtn) {
        this.chosenFlags = 0;
        Builder builder = new Builder(this);
        prepareIntentFiltedDialog(builder);
        builder.setTitle("List selection");
        builder.setCancelable(true);
        builder.setMultiChoiceItems(this.intentFlagsText, null, new OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                BaseActivity baseActivity = BaseActivity.this;
                baseActivity.chosenFlags = baseActivity.chosenFlags | BaseActivity.this.intentFlags[which];
            }
        });
        builder.setPositiveButton("Done", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = BaseActivity.this.getNextIntent(nextActivityBtn);
                intent.setFlags(BaseActivity.this.chosenFlags);
                BaseActivity.this.startActivity(intent);
            }
        });
        builder.show();
    }


    public void generalOnClick(View paramView) {
        if (this.app.isIntentFilterMode()) {
            showIntentFilterDialog(paramView);
            return;
        }
        startActivity(getNextIntent(paramView));
    }

    public abstract int getBackgroundColour();

    public void onContentChanged() {
        logMethodName();
        super.onContentChanged();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        logMethodName();
        setContentView(R.layout.activity_main);
        setupView();
        this.app = ((BaseApplication) getApplication());
        this.app.pushToStack(this);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        logMethodName();
        getMenuInflater().inflate(R.menu.base_activity, paramMenu);
        return true;
    }

    protected void onDestroy() {
        logMethodName();
        this.app.removeFromStack(this);
        super.onDestroy();
    }

    protected void onNewIntent(Intent paramIntent) {
        logMethodName();
        super.onNewIntent(paramIntent);
        setIntent(paramIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        logMethodName();
        switch (item.getItemId()) {
            case R.id.menuitem_intentfilter_mode:
                this.app.toggleIntentFilterMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        logMethodName();
        super.onPause();
    }

    protected void onPostCreate(Bundle paramBundle) {
        logMethodName();
        super.onPostCreate(paramBundle);
    }

    protected void onPostResume() {
        logMethodName();
        super.onPostResume();
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        logMethodName();
        MenuItem localMenuItem = paramMenu.findItem(R.id.menuitem_intentfilter_mode);
        StringBuilder localStringBuilder = new StringBuilder("Turn IntentFilter mode ");
        if (this.app.isIntentFilterMode()) {
        }
        for (String str = "OFF"; ; str = "ON") {
            localMenuItem.setTitle(str);
            return super.onPrepareOptionsMenu(paramMenu);
        }
    }

    protected void onRestart() {
        logMethodName();
        super.onRestart();
    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        logMethodName();
        super.onRestoreInstanceState(paramBundle);
    }

    protected void onResume() {
        logMethodName();
        checkIfReorderToFront();
        TaskInfoDisplayer localTaskInfoDisplayer = new TaskInfoDisplayer(this);
        this.handler.postDelayed(localTaskInfoDisplayer, 500L);
        super.onResume();
    }

    public Object onRetainNonConfigurationInstance() {
        logMethodName();
        return super.onRetainNonConfigurationInstance();
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        logMethodName();
        super.onSaveInstanceState(paramBundle);
    }

    protected void onStart() {
        logMethodName();
        super.onStart();
    }

    protected void onStop() {
        logMethodName();
        super.onStop();
    }
}