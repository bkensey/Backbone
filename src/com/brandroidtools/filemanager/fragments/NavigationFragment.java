package com.brandroidtools.filemanager.fragments;

/**
 * Created with IntelliJ IDEA.
 * User: Brent
 * Date: 1/16/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.activities.NavigationActivity;
import com.brandroidtools.filemanager.console.Console;
import com.brandroidtools.filemanager.console.ConsoleAllocException;
import com.brandroidtools.filemanager.console.ConsoleBuilder;
import com.brandroidtools.filemanager.model.FileSystemStorageVolume;
import com.brandroidtools.filemanager.preferences.FileManagerSettings;
import com.brandroidtools.filemanager.preferences.Preferences;
import com.brandroidtools.filemanager.ui.widgets.NavigationView;
import com.brandroidtools.filemanager.util.DialogHelper;
import com.brandroidtools.filemanager.util.FileHelper;
import com.brandroidtools.filemanager.util.StorageHelper;

import java.io.File;

public class NavigationFragment extends Fragment {

    private static final String TAG = "NavigationFragment"; //$NON-NLS-1$

    NavigationView[] mNavigationViews;
    private int mCurrentNavigationView;
    private NavigationActivity mActivity;

    /**
     * @hide
     */
    Handler mHandler;

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (NavigationActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation, container, false);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        //Navigation views
        initNavigationViews();

        // Apply the theme
        applyTheme();

        this.mHandler = new Handler();
        this.mHandler.post(new Runnable() {

            @Override
            public void run() {
                //Initialize navigation
                int cc = NavigationFragment.this.mNavigationViews.length;
                for (int i = 0; i < cc; i++) {
                    initNavigation(i, false);
                }

                //Check the intent action
                //checkIntent(getIntent());
            }
        });

    }

    /**
     * Create a new instance of FileListFragment, providing "num" as an
     * argument.
     */
    public static NavigationFragment newInstance(int num) {
        NavigationFragment f = new NavigationFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    /**
     * Method that initializes the navigation views of the activity
     */
    private void initNavigationViews() {
        this.mNavigationViews = new NavigationView[1];
        this.mCurrentNavigationView = 0;
        //- 0
        this.mNavigationViews[0] = (NavigationView)getView().findViewById(R.id.navigation_view);
        this.mNavigationViews[0].setId(0);
    }

    /**
     * Method that initializes the navigation.
     *
     * @param viewId The navigation view identifier where apply the navigation
     * @param restore Initialize from a restore info
     * @hide
     */
    void initNavigation(final int viewId, final boolean restore) {
        final NavigationView navigationView = getNavigationView(viewId);
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                //Create the default console (from the preferences)
                try {
                    Console console = ConsoleBuilder.getConsole(mActivity);
                    if (console == null) {
                        throw new ConsoleAllocException("console == null"); //$NON-NLS-1$
                    }
                } catch (Throwable ex) {
                    if (!mActivity.isChRooted()) {
                        //Show exception and exists
                        Log.e(TAG, getString(R.string.msgs_cant_create_console), ex);
                        // We don't have any console
                        // Show exception and exists
                        DialogHelper.showToast(
                                mActivity,
                                R.string.msgs_cant_create_console, Toast.LENGTH_LONG);
                        mActivity.exit();
                        return;
                    }

                    // We are in a trouble (something is not allowing creating the console)
                    // Ask the user to return to prompt or root access mode mode with a
                    // non-privileged console, prior to make crash the application
                    mActivity.askOrExit();
                    return;
                }

                //Is necessary navigate?
                if (!restore) {
                    //Load the preference initial directory
                    String initialDir =
                            Preferences.getSharedPreferences().getString(
                                    FileManagerSettings.SETTINGS_INITIAL_DIR.getId(),
                                    (String)FileManagerSettings.
                                            SETTINGS_INITIAL_DIR.getDefaultValue());
                    if (mActivity.isChRooted()) {
                        // Initial directory is the first external sdcard (sdcard, emmc, usb, ...)
                        FileSystemStorageVolume[] volumes =
                                StorageHelper.getStorageVolumes(mActivity);
                        if (volumes != null && volumes.length > 0) {
                            initialDir = volumes[0].getPath();
                        }
                    }

                    //Ensure initial is an absolute directory
                    try {
                        initialDir = new File(initialDir).getAbsolutePath();
                    } catch (Throwable e) {
                        Log.e(TAG, "Resolve of initital directory fails", e); //$NON-NLS-1$
                        String msg =
                                getString(
                                        R.string.msgs_settings_invalid_initial_directory,
                                        initialDir);
                        DialogHelper.showToast(mActivity, msg, Toast.LENGTH_SHORT);
                        initialDir = FileHelper.ROOT_DIRECTORY;
                    }

                    // Change the current directory to the preference initial directory or the
                    // request if exists
                    String navigateTo = mActivity.getIntent().getStringExtra(mActivity.EXTRA_NAVIGATE_TO);
                    if (navigateTo != null && navigateTo.length() > 0) {
                        navigationView.changeCurrentDir(navigateTo);
                    } else {
                        navigationView.changeCurrentDir(initialDir);
                    }
                }
            }
        });
    }

    /**
     * Method that returns the current navigation view.
     *
     * @return NavigationView The current navigation view
     */
    public NavigationView getCurrentNavigationView() {
        return getNavigationView(this.mCurrentNavigationView);
    }

    /**
     * Method that returns the requested navigation view.
     *
     * @param viewId The view to return
     * @return NavigationView The requested navigation view
     */
    public NavigationView getNavigationView(int viewId) {
        if (this.mNavigationViews == null) return null;
        return this.mNavigationViews[viewId];
    }

    void applyTheme() {
        //- NavigationView
        int cc = this.mNavigationViews.length;
        for (int i = 0; i < cc; i++) {
            getNavigationView(i).applyTheme();
        }
    }
}
