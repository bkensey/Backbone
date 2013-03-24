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

package com.brandroidtools.filemanager.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.console.NoSuchFileOrDirectory;
import com.brandroidtools.filemanager.fragments.BookmarksFragment;
import com.brandroidtools.filemanager.fragments.NavigationFragment;
import com.brandroidtools.filemanager.model.Bookmark;
import com.brandroidtools.filemanager.model.FileSystemObject;
import com.brandroidtools.filemanager.preferences.Bookmarks;
import com.brandroidtools.filemanager.preferences.FileManagerSettings;
import com.brandroidtools.filemanager.ui.ThemeManager;
import com.brandroidtools.filemanager.ui.ThemeManager.Theme;
import com.brandroidtools.filemanager.util.*;

import java.io.FileNotFoundException;

/**
 * An activity for show bookmarks and links.
 */
public class BookmarksActivity extends FragmentActivity{

    private static final String TAG = "BookmarksActivity"; //$NON-NLS-1$

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

    // Bookmark list XML tags
    private static final String TAG_BOOKMARKS = "Bookmarks"; //$NON-NLS-1$
    private static final String TAG_BOOKMARK = "bookmark"; //$NON-NLS-1$

    /**
     * @hide
     */
    ListView mBookmarksListView;
    BookmarksFragment mBookmarksFragment;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle state) {
        if (DEBUG) {
            Log.d(TAG, "BookmarksActivity.onCreate"); //$NON-NLS-1$
        }

        Boolean test = this instanceof Activity;
        Boolean test2 = this instanceof FragmentActivity;
        //Set the main layout of the activity
        setContentView(R.layout.bookmarks);

        FragmentManager fm       = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_content); // You can find Fragments just like you would with a
        // View by using FragmentManager.

        // If we are using activity_fragment_xml.xml then this the fragment will not be
        // null, otherwise it will be.
        if (fragment == null) {

            // We alter the state of Fragments in the FragmentManager using a FragmentTransaction.
            // FragmentTransaction's have access to a Fragment back stack that is very similar to the Activity
            // back stack in your app's task. If you add a FragmentTransaction to the back stack, a user
            // can use the back button to undo a transaction. We will cover that topic in more depth in
            // the second part of the tutorial.
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content, new NavigationFragment());
            ft.commit(); // Make sure you call commit or your Fragment will not be added.
            // This is very common mistake when working with Fragments!
        }



        //this.mBookmarksFragment =
        //        (BookmarksFragment)getSupportFragmentManager().findFragmentById (R.id.navigation_fragment);

        //Initialize action bar
        initTitleActionBar();

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
            Log.d(TAG, "BookmarksActivity.onDestroy"); //$NON-NLS-1$
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
        getActionBar().setTitle(R.string.bookmarks);
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
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case android.R.id.home:
              back(true, null);
              return true;
          default:
             return super.onOptionsItemSelected(item);
       }
    }

    /**
     * Method that returns to previous activity and.
     *
     * @param cancelled Indicates if the activity was cancelled
     * @param path The path of the selected bookmark
     */
    public void back(final boolean cancelled, final String path) {
        Intent intent =  new Intent();
        if (cancelled) {
            setResult(RESULT_CANCELED, intent);
        } else {
            // Check that the bookmark exists
            try {
                FileSystemObject fso = CommandHelper.getFileInfo(this, path, null);
                if (fso != null) {
                    intent.putExtra(NavigationActivity.EXTRA_BOOKMARK_SELECTION, fso);
                    setResult(RESULT_OK, intent);
                } else {
                    // The bookmark not exists, delete the user-defined bookmark
                    try {
                        Bookmark b = Bookmarks.getBookmark(getContentResolver(), path);
                        Bookmarks.removeBookmark(this, b);
                        mBookmarksFragment.refresh();
                    } catch (Exception ex) {/**NON BLOCK**/}
                }
            } catch (Exception e) {
                // Capture the exception
                ExceptionUtil.translateException(this, e);
                if (e instanceof NoSuchFileOrDirectory || e instanceof FileNotFoundException) {
                    // The bookmark not exists, delete the user-defined bookmark
                    try {
                        Bookmark b = Bookmarks.getBookmark(getContentResolver(), path);
                        Bookmarks.removeBookmark(this, b);
                        mBookmarksFragment.refresh();
                    } catch (Exception ex) {/**NON BLOCK**/}
                }
                return;
            }
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
        theme.setBackgroundDrawable(
                this, this.mBookmarksListView, "background_drawable"); //$NON-NLS-1$
    }
}
