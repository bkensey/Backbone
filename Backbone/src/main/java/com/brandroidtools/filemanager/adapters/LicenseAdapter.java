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

package com.brandroidtools.filemanager.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.model.History;
import com.brandroidtools.filemanager.model.License;
import com.brandroidtools.filemanager.parcelables.NavigationViewInfoParcelable;
import com.brandroidtools.filemanager.parcelables.SearchInfoParcelable;
import com.brandroidtools.filemanager.ui.IconHolder;
import com.brandroidtools.filemanager.ui.ThemeManager;
import com.brandroidtools.filemanager.ui.ThemeManager.Theme;

import java.util.List;

/**
 * An implementation of {@link android.widget.ArrayAdapter} for display history.
 */
public class LicenseAdapter extends ArrayAdapter<License> {

    /**
     * A class that conforms with the ViewHolder pattern to performance
     * the list view rendering.
     */
    private static class ViewHolder {
        /**
         * @hide
         */
        public ViewHolder() {
            super();
        }
        TextView mTvHeader;
        TextView mTvDetail;
    }

    /**
     * A class that holds the full data information.
     */
    private static class DataHolder {
        /**
         * @hide
         */
        public DataHolder() {
            super();
        }
        String mHeader;
        String mDetail;
    }

    private DataHolder[] mData;

    //The resource item layout
    private static final int RESOURCE_LAYOUT = R.layout.license_item;

    //The resource of the item name
    private static final int RESOURCE_ITEM_HEADER = R.id.license_header;
    //The resource of the item directory
    private static final int RESOURCE_ITEM_DETAIL = R.id.license_details;

    /**
     * Constructor of <code>HistoryAdapter</code>.
     *
     * @param context The current context
     * @param licenses The license list reference
     */
    public LicenseAdapter(Context context, List<License> licenses) {
        super(context, RESOURCE_ITEM_HEADER, licenses);

        //Do cache of the data for better performance
        processData(licenses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        processData(null);
        super.notifyDataSetChanged();
    }

    /**
     * Method that dispose the elements of the adapter.
     */
    public void dispose() {
        clear();
        this.mData = null;
    }

    /**
     * Method that process the data before use {@link #getView} method.
     *
     * @param licenseData The list of licenses (to better performance) or null.
     */
    private void processData(List<License> licenseData) {
        this.mData = new DataHolder[getCount()];
        int cc = (licenseData == null) ? getCount() : licenseData.size();
        for (int i = 0; i < cc; i++) {
            //History info
            License license = (licenseData == null) ? getItem(i) : licenseData.get(i);

            //Build the data holder
            this.mData[i] = new LicenseAdapter.DataHolder();
            this.mData[i].mHeader = license.mHeader;
            this.mData[i].mDetail = license.mDetail;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Check to reuse view
        View v = convertView;
        if (v == null) {
            //Create the view holder
            LayoutInflater li =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(RESOURCE_LAYOUT, parent, false);
            ViewHolder viewHolder = new LicenseAdapter.ViewHolder();
            viewHolder.mTvHeader = (TextView)v.findViewById(RESOURCE_ITEM_HEADER);
            viewHolder.mTvDetail = (TextView)v.findViewById(RESOURCE_ITEM_DETAIL);
            v.setTag(viewHolder);

            // Apply the current theme
            Theme theme = ThemeManager.getCurrentTheme(getContext());
            theme.setBackgroundDrawable(
                    getContext(), v, "selectors_deselected_drawable"); //$NON-NLS-1$
            theme.setTextColor(
                    getContext(), viewHolder.mTvHeader, "text_color"); //$NON-NLS-1$
            theme.setTextColor(
                    getContext(), viewHolder.mTvDetail, "text_color"); //$NON-NLS-1$
        }

        //Retrieve data holder
        final DataHolder dataHolder = this.mData[position];

        //Retrieve the view holder
        ViewHolder viewHolder = (ViewHolder)v.getTag();

        //Set the data
        viewHolder.mTvHeader.setText(Html.fromHtml(dataHolder.mHeader));
        viewHolder.mTvHeader.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.mTvDetail.setText(Html.fromHtml(dataHolder.mDetail));
        viewHolder.mTvDetail.setMovementMethod(LinkMovementMethod.getInstance());

        //Return the view
        return v;
    }

    /**
     * Method that should be invoked when the theme of the app was changed
     */
    public void notifyThemeChanged() {

    }

}
