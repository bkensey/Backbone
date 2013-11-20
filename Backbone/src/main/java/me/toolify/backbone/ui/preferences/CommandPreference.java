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

package me.toolify.backbone.ui.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afzkl.development.mColorPicker.views.ColorDialogView;
import com.afzkl.development.mColorPicker.views.ColorPanelView;

import java.util.HashMap;
import java.util.Map;

import me.toolify.backbone.R;

/**
 * A {@link android.preference.Preference} that allow to select/pick a color in a new window dialog.
 */
public class CommandPreference extends DialogPreference {

    private TextView mText;
    private String mCommandsList;

    private ColorDialogView mColorDlg;

    /**
     * Constructor of <code>ColorPickerPreference</code>
     *
     * @param context The current context
     */
    public CommandPreference(Context context) {
        this(context, null);
    }

    /**
     * Constructor of <code>ColorPickerPreference</code>
     *
     * @param context The current context
     * @param attrs The attributes of the XML tag that is inflating the preference.
     */
    public CommandPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.command_preference);
    }

    /**
     * Returns the color of the picker.
     *
     * @return The color of the picker.
     */
    public String getCommandList() {
        return this.mCommandsList;
    }

    /**
     * Sets the color of the picker and saves it to the {@link android.content.SharedPreferences}.
     *
     * @param commandList a map of commands and whether or not each is required.
     */
    public void setCommandList(String commandList) {
        mCommandsList = commandList;
        // when called from onSetInitialValue the view is still not set
        if (this.mText != null) {
            mText.setText(mCommandsList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setCommandList(restoreValue ? getPersistedString((String) defaultValue) :
                ((String) defaultValue).toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        View v = view.findViewById(R.id.command_list);
        if (v != null && v instanceof TextView) {
            this.mText = (TextView)v;
            this.mText.setText(this.mCommandsList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.commandList = getCommandList();
        return myState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setCommandList(myState.commandList);
    }

    /**
     * A class for managing the instance state of a {@link me.toolify.backbone.ui.preferences.CommandPreference}.
     */
    static class SavedState extends BaseSavedState {
        String commandList;

        /**
         * Constructor of <code>SavedState</code>
         *
         * @param source The source
         */
        public SavedState(Parcel source) {
            super(source);
            this.commandList = source.readString();
        }

        /**
         * Constructor of <code>SavedState</code>
         *
         * @param superState The parcelable state
         */
        public SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.commandList);
        }

        /**
         * A class that generates instances of the <code>SavedState</code> class from a Parcel.
         */
        @SuppressWarnings("hiding")
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
