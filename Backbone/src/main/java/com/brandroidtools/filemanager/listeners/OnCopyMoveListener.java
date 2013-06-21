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

package com.brandroidtools.filemanager.listeners;

import com.brandroidtools.filemanager.model.FileSystemObject;
import com.brandroidtools.filemanager.ui.policy.CopyMoveActionPolicy.LinkedResource;
import com.brandroidtools.filemanager.ui.policy.CopyMoveActionPolicy.COPY_MOVE_OPERATION;

import com.brandroidtools.filemanager.ui.policy.CopyMoveActionPolicy;

import java.util.List;

/**
 * A listener for handling file selections ready for paste operations.
 */
public interface OnCopyMoveListener {

    /**
     * Invoked when a selection of files must be marked for paste. item must be created.
     *
     * @param filesForPaste The data to marked
     * @param pasteOperationType The type of paste operation (Copy/Move)
     */
    void onMarkFilesForPaste(List<FileSystemObject> filesForPaste, COPY_MOVE_OPERATION pasteOperationType);

    /**
     * Invoked when the availability of files marked for paste must be checked.
     */
    boolean onAreFilesMarkedForPaste();

    /**
     * Invoked when files marked for paste must be unmarked.
     */
    void onClearFilesMarkedForPaste();

    /**
     * Invoked when files marked for paste are requested.
     */
    List<FileSystemObject> onRequestFilesMarkedForPaste();

    /**
     * Invoked when files marked for paste are requested.
     */
    COPY_MOVE_OPERATION onRequestPasteOperationType();

    /**
     * Invoked when destination directory is requested.
     */
    String onRequestDestinationDir();
}
