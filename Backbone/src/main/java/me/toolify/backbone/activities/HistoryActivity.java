/*
 * Copyright (C) 2012 The CyanogenMod Project
 * Copyright (C) 2013 BrandroidTools
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

package me.toolify.backbone.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import me.toolify.backbone.R;
import me.toolify.backbone.fragments.HistoryFragment;
import me.toolify.backbone.model.History;
import me.toolify.backbone.preferences.FileManagerSettings;
import me.toolify.backbone.ui.ThemeManager;
import me.toolify.backbone.ui.ThemeManager.Theme;

/**
 * An activity for show navigation history.
 */
public class HistoryActivity extends Activity {

    private static final String TAG = "HistoryActivity"; //$NON-NLS-1$

    private static boolean DEBUG = false;

    HistoryFragment mHistoryFragment;

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

        //Set the main layout of the activity
        setContentView(R.layout.history);

        // Load the BoookmarksFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mHistoryFragment = new HistoryFragment();
        ft.add(R.id.fragment_content, mHistoryFragment);
        ft.commit();

        //Set in transition
        overridePendingTransition(R.anim.translate_to_right_in, R.anim.hold_out);

        //Initialize action bar
        initTitleActionBar();

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
                mHistoryFragment.clearHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
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
     * Method that returns to previous activity and.
     *
     * @param cancelled Indicates if the activity was cancelled
     * @param history The selected history
     */
    public void back(final boolean cancelled, final History history) {
        Intent intent =  new Intent();
        if (cancelled) {
            if (mHistoryFragment.mIsClearHistory) {
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
     * Method that applies the current theme to the activity
     * @hide
     */
    void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme(this);
        theme.setBaseTheme(this, false);

        // -View
        theme.setBackgroundDrawable(this, getWindow().getDecorView(), "background_drawable"); //$NON-NLS-1$
    }
}
