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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.console.NoSuchFileOrDirectory;
import com.brandroidtools.filemanager.fragments.BookmarksFragment;
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
public class BookmarksActivity extends Activity{

    private static final String TAG = "BookmarksActivity"; //$NON-NLS-1$

    private static boolean DEBUG = false;

    // Bookmark list XML tags
    private static final String TAG_BOOKMARKS = "Bookmarks"; //$NON-NLS-1$
    private static final String TAG_BOOKMARK = "bookmark"; //$NON-NLS-1$

    /**
     * @hide
     */
    BookmarksFragment mBookmarksFragment;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle state) {
        if (DEBUG) {
            Log.d(TAG, "BookmarksActivity.onCreate"); //$NON-NLS-1$
        }

        //Set the main layout of the activity
        setContentView(R.layout.bookmarks);

        // Load the BoookmarksFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mBookmarksFragment = new BookmarksFragment();
        ft.add(R.id.fragment_content, mBookmarksFragment);
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
            Log.d(TAG, "BookmarksActivity.onDestroy"); //$NON-NLS-1$
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
}
