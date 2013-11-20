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

package me.toolify.backbone.activities.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.toolify.backbone.FileManagerApplication;
import me.toolify.backbone.R;
import me.toolify.backbone.console.ConsoleBuilder;
import me.toolify.backbone.preferences.AccessMode;
import me.toolify.backbone.preferences.FileManagerSettings;
import me.toolify.backbone.preferences.ObjectStringIdentifier;
import me.toolify.backbone.preferences.Preferences;
import me.toolify.backbone.ui.preferences.CommandPreference;

/**
 * A class that manages the commons options of the application
 */
public class RootPreferenceFragment extends TitlePreferenceFragment {

    private static final String TAG = "RootPreferenceFragment"; //$NON-NLS-1$

    private static final boolean DEBUG = false;

    private ListPreference mAccessMode;
    private CommandPreference mInstalledCommands;
    private CommandPreference mMissingCommands;

    private HashMap<String, Boolean> mInstalledCommandsList;
    private HashMap<String, Boolean> mMissingCommandsList;

    /**
     * @hide
     */
    boolean mLoaded = false;

    private final OnPreferenceChangeListener mOnChangeListener =
            new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, Object newValue) {
            boolean ret = true;

            String key = preference.getKey();
            if (DEBUG) {
                Log.d(TAG,
                    String.format("New value for %s: %s",  //$NON-NLS-1$
                            key,
                            String.valueOf(newValue)));
            }

            // Access mode
            if (FileManagerSettings.SETTINGS_ACCESS_MODE.getId().compareTo(key) == 0) {
                Activity activity = RootPreferenceFragment.this.getActivity();

                String value = (String)newValue;
                AccessMode oldMode = FileManagerApplication.getAccessMode();
                AccessMode newMode = AccessMode.fromId(value);
                if (oldMode.compareTo(newMode) != 0) {
                    // The mode was changes. Change the console
                    if (newMode.compareTo(AccessMode.ROOT) == 0) {
                        if (!ConsoleBuilder.changeToPrivilegedConsole(
                                activity.getApplicationContext())) {
                            value = String.valueOf(oldMode.ordinal());
                            ret = false;
                        }
                    } else {
                        if (!ConsoleBuilder.changeToNonPrivilegedConsole(
                                activity.getApplicationContext())) {
                            value = String.valueOf(oldMode.ordinal());
                            ret = false;
                        }
                    }
                }

                int valueId = Integer.valueOf(value).intValue();
                String[] label = getResources().getStringArray(
                        R.array.access_mode_labels);
                String[] summary = getResources().getStringArray(
                        R.array.access_mode_summaries);
                preference.setTitle(label[valueId]);
                preference.setSummary(summary[valueId]);
            }

            // Notify the change (only if fragment is loaded. Default values are loaded
            // while not in loaded mode)
            if (RootPreferenceFragment.this.mLoaded && ret) {
                Intent intent = new Intent(FileManagerSettings.INTENT_SETTING_CHANGED);
                intent.putExtra(
                        FileManagerSettings.EXTRA_SETTING_CHANGED_KEY, preference.getKey());
                getActivity().sendBroadcast(intent);
            }

            return ret;
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change the preference manager
        getPreferenceManager().setSharedPreferencesName(Preferences.SETTINGS_FILENAME);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        // Add the preferences
        addPreferencesFromResource(R.xml.preferences_root);

        // Access mode
        this.mAccessMode =
                (ListPreference)findPreference(
                        FileManagerSettings.SETTINGS_ACCESS_MODE.getId());
        this.mAccessMode.setOnPreferenceChangeListener(this.mOnChangeListener);
        String defaultValue = ((ObjectStringIdentifier)FileManagerSettings.
                            SETTINGS_ACCESS_MODE.getDefaultValue()).getId();
        String value = Preferences.getSharedPreferences().getString(
                            FileManagerSettings.SETTINGS_ACCESS_MODE.getId(),
                            defaultValue);
        this.mOnChangeListener.onPreferenceChange(this.mAccessMode, value);
        // If device is not rooted, this setting cannot be changed
        this.mAccessMode.setEnabled(FileManagerApplication.isDeviceRooted());

        mInstalledCommandsList = new HashMap<String, Boolean>();
        mMissingCommandsList = new HashMap<String, Boolean>();

        loadCommands();
        showInstalledCommands();
        showMissingCommands();

        // Loaded
        this.mLoaded = true;
    }

    /**
     * Method that gathers a list of installed and missing commands, as well as whether each
     * command is required for shell operation
     *
     * @hide
     */
    void loadCommands() {

        // Read required shell commands and denote which ones are missing.
        try {
            mInstalledCommandsList.clear();
            String shellCommands = getString(R.string.shell_required_commands);
            String[] commands = shellCommands.split(","); //$NON-NLS-1$
            int cc = commands.length;
            if (cc == 0) {
                //???
                Log.w(TAG, "No shell commands."); //$NON-NLS-1$
            }
            for (int i = 0; i < cc; i++) {
                String c = commands[i].trim();
                if (c.length() == 0) continue;
                File cmd = new File(c);
                if (!cmd.exists() || !cmd.isFile()) {
                    mMissingCommandsList.put(c, true);
                } else {
                    mInstalledCommandsList.put(c, true);
                }
            }
            // All commands are present
        } catch (Exception e) {
            Log.e(TAG,
                    "Failed to read shell commands.", e); //$NON-NLS-1$
        }

        mMissingCommandsList.clear();
        // Read optional shell commands and denote which ones are missing
        try {
            String shellCommands = getString(R.string.shell_optional_commands);
            String[] commands = shellCommands.split(","); //$NON-NLS-1$
            int cc = commands.length;
            if (cc == 0) {
                Log.w(TAG, "No optional commands."); //$NON-NLS-1$
                return;
            }
            for (int i = 0; i < cc; i++) {
                String c = commands[i].trim();
                if (c.length() == 0) continue;
                File cmd = new File(c);
                if (!cmd.exists() || !cmd.isFile()) {
                    mMissingCommandsList.put(c, false);

                } else {
                    mInstalledCommandsList.put(c, false);
                }
            }
        } catch (Exception e) {
            Log.e(TAG,
                    "Failed to read optional shell commands.", e); //$NON-NLS-1$
        }
    }

    /**
     * Method that loads and displays the list of installed commands
     *
     * @hide
     */
    void showInstalledCommands() {
        this.mInstalledCommands =
                (CommandPreference)findPreference(
                        FileManagerSettings.SETTINGS_INSTALLED_COMMANDS.getId());

        String ret = "";
        String sep = "\n";
        for (Map.Entry<String, Boolean> entry : mInstalledCommandsList.entrySet()) {
            ret += entry.getKey() + sep;
        }

        mInstalledCommands.setCommandList(ret);

    }

    /**
     * Method that loads and displays the list of missing commands
     *
     * @hide
     */
    void showMissingCommands() {
        this.mMissingCommands =
                (CommandPreference)findPreference(
                        FileManagerSettings.SETTINGS_MISSING_COMMANDS.getId());

        String ret = "";
        String sep = "\n";
        for (Map.Entry<String, Boolean> entry : mMissingCommandsList.entrySet()) {
            ret += entry.getKey() + sep;
        }

        mMissingCommands.setCommandList(ret);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence getTitle() {
        return getString(R.string.pref_root);
    }
}
