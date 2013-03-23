package com.brandroidtools.filemanager.activities.preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.adapters.LicenseAdapter;
import com.brandroidtools.filemanager.model.License;
import com.brandroidtools.filemanager.model.License.LICENSE_TYPE;
import com.brandroidtools.filemanager.preferences.FileManagerSettings;
import com.brandroidtools.filemanager.ui.ThemeManager;
import com.brandroidtools.filemanager.util.ExceptionUtil;


import java.util.ArrayList;
import java.util.List;

public class LicenseActivity extends Activity {

    private static final String TAG = "LicenseActivity"; //$NON-NLS-1$

    private static boolean DEBUG = false;

    /**
     * @hide
     */
    private List<License> mLicenses;
    /**
     * @hide
     */
    private ListView mListView;
    /**
     * @hide
     */
    private ListAdapter mAdapter;
    boolean mIsEmpty;

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

        //Set in transition
        overridePendingTransition(R.anim.translate_to_right_in, R.anim.hold_out);

        //Set the main layout of the activity
        setContentView(R.layout.licenses);

        //Initialize action bars and data
        initTitleActionBar();
        initLicenses();

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
        getActionBar().setTitle(R.string.licenses);
    }

    /**
     * Method that initializes the licenses listview of the activity.
     */
    private void initLicenses() {
        this.mListView = (ListView)findViewById(R.id.licenses_listview);
        mLicenses = new ArrayList<License>();
        final LicenseAdapter mAdapter = new LicenseAdapter(this, mLicenses);
        this.mListView.setAdapter(mAdapter);

        // Retrieve the loading view
        final View waiting = findViewById(R.id.licenses_waiting);

        this.mListView = (ListView)findViewById(R.id.licenses_listview);

        // Load the history in background
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            Exception mCause;
            List<License> mLicenses;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    this.mLicenses = loadLicenses();
                    if (this.mLicenses.isEmpty()) {
                        View msg = findViewById(R.id.licenses_empty_msg);
                        msg.setVisibility(View.VISIBLE);
                        return Boolean.TRUE;
                    }
                    LicenseActivity.this.mIsEmpty = this.mLicenses.isEmpty();

                    //Show inverted history
                    final List<License> adapterList = new ArrayList<License>(this.mLicenses);
                    LicenseActivity.this.mAdapter =
                            new LicenseAdapter(LicenseActivity.this, adapterList);

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
                    if (LicenseActivity.this.mListView != null &&
                            LicenseActivity.this.mAdapter != null) {

                        LicenseActivity.this.mListView.
                                setAdapter(LicenseActivity.this.mAdapter);
                    }

                } else {
                    if (this.mCause != null) {
                        ExceptionUtil.translateException(LicenseActivity.this, this.mCause);
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

    private List<License> loadLicenses() {
        String[] mHeaderArray = getResources().getStringArray(R.array.credits_headers);
        String[] mDetailArray = getResources().getStringArray(R.array.credits_details);
        List<License> list = new ArrayList<License>();

        int len = mHeaderArray.length;
        for (int i = 0; i < len; i++) {
            License l = new License(LICENSE_TYPE.LICENSE, mHeaderArray[i], mDetailArray[i]);
            list.add(l);

        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }


    /**
     * Method that returns to previous activity and.
     */
    private void back() {
        Intent intent =  new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    /**
     * Method that applies the current theme to the activity
     * @hide
     */
    void applyTheme() {
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme(this);
        theme.setBaseTheme(this, false);

        // -View
        theme.setBackgroundDrawable(this, this.mListView, "background_drawable"); //$NON-NLS-1$
        this.mListView.setDivider(
                theme.getDrawable(this, "horizontal_divider_drawable")); //$NON-NLS-1$
        this.mListView.invalidate();
    }
}
