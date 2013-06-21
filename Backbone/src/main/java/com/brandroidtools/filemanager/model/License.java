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

package com.brandroidtools.filemanager.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.brandroidtools.filemanager.providers.BookmarksContentProvider;

import java.io.File;
import java.io.Serializable;

/**
 * A class that represent a bookmark.
 */
public class License {

    /**
     * Enumeration for types of bookmarks.
     */
    public enum LICENSE_TYPE {
        /**
         * An USB mount point.
         */
        LICENSE,
        /**
         * A bookmark added by the user.
         */
        CREDIT
    }

    /** @hide **/
    public int mId;
    /** @hide **/
    public LICENSE_TYPE mType;
    /** @hide **/
    public String mHeader;
    /** @hide **/
    public String mDetail;

    /**
     * Constructor of <code>License</code>.
     *
     * @param type The type of the bookmark
     * @param header The header text of the license
     * @param detail The text of the license details
     * @hide
     */
    public License(LICENSE_TYPE type, String header, String detail) {
        super();
        this.mType = type;
        this.mHeader = header;
        this.mDetail = detail;
    }
}
