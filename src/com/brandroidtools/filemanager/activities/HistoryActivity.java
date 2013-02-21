/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brandroidtools.filemanager.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.adapters.HighlightedSimpleMenuListAdapter;
import com.brandroidtools.filemanager.adapters.HistoryAdapter;
import com.brandroidtools.filemanager.adapters.SimpleMenuListAdapter;
import com.brandroidtools.filemanager.model.History;
import com.brandroidtools.filemanager.preferences.FileManagerSettings;
import com.brandroidtools.filemanager.ui.ThemeManager;
import com.brandroidtools.filemanager.ui.ThemeManager.Theme;
import com.brandroidtools.filemanager.ui.widgets.ButtonItem;
import com.brandroidtools.filemanager.util.AndroidHelper;
import com.brandroidtools.filemanager.util.DialogHelper;
import com.brandroidtools.filemanager.util.ExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An activity for show navigation history.
 */
public class HistoryActivity extends Activity implements OnItemClickListener {

    private static final String TAG = "HistoryActivity"; //$NON-NLS-1$

    private static boolean DEBUG = false;

    private final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().compareTo(FileManagerSettings.INTENT_THEME_CHANGED) == 0) {
                    applyTheme();
                }
            }
        }
    };

    /**
     * @hide
     */
    ListView mListView;
    /**
     * @hide
     */
    HistoryAdapter mAdapter;
    /**
     * @hide
     */
    boolean mIsEmpty;
    private boolean mIsClearHistory;

    private View mOptionsAnchorView;

    /**
     * Intent extra parameter for the history data.
     */
    public static final String EXTRA_HISTORY_LIST = "extra_history_list";  //$NON-NLS-1$

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle state) {
        if (DEBUG) {
            Log.d(TAG, "HistoryActivity.onCreate"); //$NON-NLS-1$
        }

        // Register the broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(FileManagerSettings.INTENT_THEME_CHANGED);
        registerReceiver(this.mNotificationReceiver, filter);

        this.mIsEmpty = false;
        this.mIsClearHistory = false;

        //Set in transition
        overridePendingTransition(R.anim.translate_to_right_in, R.anim.hold_out);

        //Set the main layout of the activity
        setContentView(R.layout.history);

        //Initialize action bars and data
        initTitleActionBar();
        initHistory();

        // Apply the theme
        applyTheme();

        //Save state
        super.onCreate(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        if (DEBUG) {
            Log.d(TAG, "HistoryActivity.onDestroy"); //$NON-NLS-1$
        }

        // Unregister the receiver
        try {
            unregisterReceiver(this.mNotificationReceiver);
        } catch (Throwable ex) {
            /**NON BLOCK**/
        }

        //All destroy. Continue
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        //Set out transition
        overridePendingTransition(R.anim.hold_in, R.anim.translate_to_left_out);
        super.onPause();
    }

    /**
     * Method that initializes the titlebar of the activity.
     */
    private void initTitleActionBar() {
        //Configure the action bar options
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.history);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back(true, null);
                break;
            case R.id.mnu_clear_history:
                clearHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that initializes the titlebar of the activity.
     */
    @SuppressWarnings("unchecked")
    private void initHistory() {
        // Retrieve the loading view
        final View waiting = findViewById(R.id.history_waiting);

        this.mListView = (ListView)findViewById(R.id.history_listview);

        // Load the history in background
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            Exception mCause;
            List<History> mHistory;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    this.mHistory =
                            (List<History>)getIntent().getSerializableExtra(EXTRA_HISTORY_LIST);
                    if (this.mHistory.isEmpty()) {
                        View msg = findViewById(R.id.history_empty_msg);
                        msg.setVisibility(View.VISIBLE);
                        return Boolean.TRUE;
                    }
                    HistoryActivity.this.mIsEmpty = this.mHistory.isEmpty();

                    //Show inverted history
                    final List<History> adapterList = new ArrayList<History>(this.mHistory);
                    Collections.reverse(adapterList);
                    HistoryActivity.this.mAdapter =
                            new HistoryAdapter(HistoryActivity.this, adapterList);

                    return Boolean.TRUE;

                } catch (Exception e) {
                    this.mCause = e;
                    return Boolean.FALSE;
                }
            }

            @Override
            protected void onPreExecute() {
                waiting.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                waiting.setVisibility(View.GONE);
                if (result.booleanValue()) {
                    if (HistoryActivity.this.mListView != null &&
                        HistoryActivity.this.mAdapter != null) {

                        HistoryActivity.this.mListView.
                            setAdapter(HistoryActivity.this.mAdapter);
                        HistoryActivity.this.mListView.
                            setOnItemClickListener(HistoryActivity.this);
                    }

                } else {
                    if (this.mCause != null) {
                        ExceptionUtil.translateException(HistoryActivity.this, this.mCause);
                    }
                }
            }

            @Override
            protected void onCancelled() {
                waiting.setVisibility(View.GONE);
            }
        };
        task.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back(true, null);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History history = ((HistoryAdapter)parent.getAdapter()).getItem(position);
        back(false, history);
    }

    /**
     * Method that returns to previous activity and.
     *
     * @param cancelled Indicates if the activity was cancelled
     * @param history The selected history
     */
    private void back(final boolean cancelled, final History history) {
        Intent intent =  new Intent();
        if (cancelled) {
            if (this.mIsClearHistory) {
                intent.putExtra(NavigationActivity.EXTRA_HISTORY_CLEAR, true);
            }
            setResult(RESULT_CANCELED, intent);
        } else {
            intent.putExtra(NavigationActivity.EXTRA_HISTORY_ENTRY_SELECTION, history);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * Method that clean the history and return back to navigation view
     *  @hide
     */
    void clearHistory() {
        if (this.mAdapter != null) {
            this.mAdapter.clear();
            this.mAdapter.notifyDataSetChanged();
            View msg = findViewById(R.id.history_empty_msg);
            msg.setVisibility(View.VISIBLE);
            this.mIsClearHistory = true;
        }
    }

    /**
     * Method that applies the current theme to the activity
     * @hide
     */
    void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme(this);
        theme.setBaseTheme(this, false);

        // -View
        theme.setBackgroundDrawable(this, this.mListView, "background_drawable"); //$NON-NLS-1$
        if (this.mAdapter != null) {
            this.mAdapter.notifyThemeChanged();
            this.mAdapter.notifyDataSetChanged();
        }
        this.mListView.setDivider(
                theme.getDrawable(this, "horizontal_divider_drawable")); //$NON-NLS-1$
        this.mListView.invalidate();
    }
}
