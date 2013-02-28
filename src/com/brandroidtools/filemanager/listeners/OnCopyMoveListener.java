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
