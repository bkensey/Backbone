package com.brandroidtools.filemanager.ui.actionmode;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.*;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.brandroidtools.filemanager.FileManagerApplication;
import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.listeners.OnCopyMoveListener;
import com.brandroidtools.filemanager.listeners.OnRequestRefreshListener;
import com.brandroidtools.filemanager.listeners.OnSelectionListener;
import com.brandroidtools.filemanager.model.FileSystemObject;
import com.brandroidtools.filemanager.model.SystemFile;
import com.brandroidtools.filemanager.preferences.AccessMode;
import com.brandroidtools.filemanager.ui.dialogs.InputNameDialog;
import com.brandroidtools.filemanager.ui.policy.*;
import com.brandroidtools.filemanager.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectionModeCallback implements ActionMode.Callback {
    private MenuItem mActionRefresh;
    private MenuItem mActionNewDirectory;
    private MenuItem mActionNewFile;
    private MenuItem mActionPasteSelection;
    private MenuItem mActionMoveSelection;
    private MenuItem mActionDeleteSelection;
    private MenuItem mActionCompressSelection;
    private MenuItem mActionCreateLinkGlobal;
    private MenuItem mActionSendSelection;
    private MenuItem mActionProperties;
    private MenuItem mActionOpen;
    private MenuItem mActionOpenWith;
    private MenuItem mActionDelete;
    private MenuItem mActionRename;
    private MenuItem mActionCompress;
    private MenuItem mActionExtract;
    private MenuItem mActionCreateCopy;
    private MenuItem mActionCreateLink;
    private MenuItem mActionExecute;
    private MenuItem mActionSend;
    private MenuItem mActionAddBookmark;
    private MenuItem mActionAddShortcut;
    private MenuItem mActionOpenParentFolder;
    private ShareActionProvider mShareActionProvider;

    private TextView mFileCount;
    private TextView mFolderCount;

    private boolean pasteReady = false;

    boolean mClosedByUser = true;
    private Activity mActivity;
    private ActionMode mSelectionMode;
    private Boolean mGlobal;
    private Boolean mMultiSelection;
    private final Boolean mSearch;
    private final Boolean mChRooted;

    /**
     * @hide
     */
    OnRequestRefreshListener mOnRequestRefreshListener;
    /**
     * @hide
     */
    OnSelectionListener mOnSelectionListener;
    /**
     * @hide
     */
    OnCopyMoveListener onCopyMoveListener;

    private FileSystemObject mFso;

    /**
     * Constructor for <code>SelectionModeCallback</code>.
     *
     * @param activity The current Activity context
     * @param search If the call is from search activity
     */
    public SelectionModeCallback (Activity activity, Boolean search) {
        this.mActivity = activity;
        this.mSearch = search;
        this.mChRooted = FileManagerApplication.getAccessMode().compareTo(AccessMode.SAFE) == 0;
    }

    /**
     * Method that sets the listener for communicate a refresh request.
     *
     * @param onRequestRefreshListener The request refresh listener
     */
    public void setOnRequestRefreshListener(OnRequestRefreshListener onRequestRefreshListener) {
        this.mOnRequestRefreshListener = onRequestRefreshListener;
    }

    /**
     * Method that sets the listener for requesting selection data
     *
     * @param onSelectionListener The request selection data listener
     */
    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.mOnSelectionListener = onSelectionListener;
    }

    /**
     * Method that sets the listener for marking file selections for paste
     *
     * @param onCopyMoveListener The request selection data listener
     */
    public void setOnCopyMoveListener(OnCopyMoveListener onCopyMoveListener) {
        this.onCopyMoveListener = onCopyMoveListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mSelectionMode = mode;

        MenuInflater inflater = mActivity.getMenuInflater();
        inflater.inflate(R.menu.actionmode, menu);

        View customTitle = mActivity.getLayoutInflater().inflate(R.layout.navigation_action_mode_test, null, false);
        mFileCount = (TextView)customTitle.findViewById(R.id.file_count);
        mFolderCount = (TextView)customTitle.findViewById(R.id.folder_count);
        mode.setCustomView(customTitle);

        mActionRefresh = menu.findItem(R.id.mnu_actions_refresh);
        mActionNewDirectory = menu.findItem(R.id.mnu_actions_new_directory);
        mActionNewFile = menu.findItem(R.id.mnu_actions_new_file);
        mActionCreateCopy = menu.findItem(R.id.mnu_actions_copy);
        mActionMoveSelection = menu.findItem(R.id.mnu_actions_move);
        mActionDeleteSelection = menu.findItem(R.id.mnu_actions_delete_selection);
        mActionCompressSelection = menu.findItem(R.id.mnu_actions_compress_selection);
        mActionCreateLinkGlobal = menu.findItem(R.id.mnu_actions_create_link_global);
        mActionSendSelection = menu.findItem(R.id.mnu_actions_send_selection);
        mActionProperties = menu.findItem(R.id.mnu_actions_properties);
        mActionOpen = menu.findItem(R.id.mnu_actions_open);
        mActionOpenWith = menu.findItem(R.id.mnu_actions_open_with);
        mActionDelete = menu.findItem(R.id.mnu_actions_delete);
        mActionRename = menu.findItem(R.id.mnu_actions_rename);
        mActionCompress = menu.findItem(R.id.mnu_actions_compress);
        mActionExtract = menu.findItem(R.id.mnu_actions_extract);
        mActionCreateLink = menu.findItem(R.id.mnu_actions_create_link);
        mActionExecute = menu.findItem(R.id.mnu_actions_execute);
        mActionSend = menu.findItem(R.id.mnu_actions_send);
        mActionAddBookmark = menu.findItem(R.id.mnu_actions_add_to_bookmarks);
        mActionAddShortcut = menu.findItem(R.id.mnu_actions_add_shortcut);

        // Set file with share history to the provider and set the share intent.
//        mShareActionProvider = (ShareActionProvider) mShare.getActionProvider();
//        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        int folders = 0;
        int files = 0;
        String folderCount;
        String fileCount;

        // Get selection
        List<FileSystemObject> selection = null;
        if (this.mOnSelectionListener != null) {
            selection = this.mOnSelectionListener.onRequestSelectedFiles();
        }

        // Count selection
        for (FileSystemObject fso : selection) {
            if (FileHelper.isDirectory(fso)) {
                folders++;
            } else {
                files++;
            }
        }

        // Display selection counts in action mode
        folderCount = Integer.toString(folders);
        fileCount = Integer.toString(files);
        mFolderCount.setText(folderCount);
        mFileCount.setText(fileCount);

        //TODO: Figure out how to change/fix the mActionCreateLinkGlobal flag
        this.mGlobal = false;

        // Reset action item visibility
        mActionMoveSelection.setVisible(true);
        mActionDeleteSelection.setVisible(true);
        mActionCompressSelection.setVisible(true);
        mActionCreateLinkGlobal.setVisible(true);
        mActionSendSelection.setVisible(true);
        mActionProperties.setVisible(true);
        mActionOpen.setVisible(true);
        mActionOpenWith.setVisible(true);
        mActionDelete.setVisible(true);
        mActionRename.setVisible(true);
        mActionCompress.setVisible(true);
        mActionExtract.setVisible(true);
        mActionCreateCopy.setVisible(true);
        mActionCreateLink.setVisible(true);
        mActionExecute.setVisible(true);
        mActionSend.setVisible(true);
        mActionAddBookmark.setVisible(true);
        mActionAddShortcut.setVisible(true);

        // Determine the need for single file (not global) and multiple selection (global) operations
        if (selection.size() == 1) {
            this.mFso = selection.get(0);
            this.mMultiSelection = false;

            // Hide multi target actions when only one item is selected
            mActionDeleteSelection.setVisible(false);
            mActionCompressSelection.setVisible(false);
            mActionSendSelection.setVisible(false);
        } else {
            this.mMultiSelection = true;

            // Hide single target actions when multiple items are selected
            mActionProperties.setVisible(false);
            mActionOpen.setVisible(false);
            mActionOpenWith.setVisible(false);
            mActionDelete.setVisible(false);
            mActionRename.setVisible(false);
            mActionCompress.setVisible(false);
            mActionExtract.setVisible(false);
            mActionCreateLink.setVisible(false);
            mActionExecute.setVisible(false);
            mActionSend.setVisible(false);
            mActionAddBookmark.setVisible(false);
            mActionAddShortcut.setVisible(false);
            mActionCreateLinkGlobal.setVisible(false);
        }

        /*
         * SINGLE FILE ACTIONS
         */

        //- Check actions that needs a valid reference
        if (!mMultiSelection && this.mFso != null) {

            //- Open/Open with -> Only when the fso is not a folder and is not a system file
            if (FileHelper.isDirectory(this.mFso) || FileHelper.isSystemFile(this.mFso)) {
                mActionOpen.setVisible(false);
                mActionOpenWith.setVisible(false);
                mActionSend.setVisible(false);
            }

            // Create link (not allow in storage volume)
            if (StorageHelper.isPathInStorageVolume(this.mFso.getFullPath())) {
                mActionCreateLink.setVisible(false);
            }

            //Execute only if mime/type category is EXEC
            MimeTypeHelper.MimeTypeCategory category = MimeTypeHelper.getCategory(this.mActivity, this.mFso);
            if (category.compareTo(MimeTypeHelper.MimeTypeCategory.EXEC) != 0) {
                mActionExecute.setVisible(false);
            }
        }

        //- Add to bookmarks -> Only directories
        if (this.mFso != null && FileHelper.isRootDirectory(this.mFso)) {
            mActionAddBookmark.setVisible(false);
        }

        /*
         * MULTI FILE ACTIONS
         */

        //- Create link
        if (this.mGlobal  && selection != null) {
            // Create link (not allow in storage volume)
            FileSystemObject fso = selection.get(0);
            if (StorageHelper.isPathInStorageVolume(fso.getFullPath())) {
                mActionCreateLink.setVisible(false);
            }
        } else if (!this.mGlobal) {
            // Create link (not allow in storage volume)
            if (StorageHelper.isPathInStorageVolume(this.mFso.getFullPath())) {
                mActionCreateLink.setVisible(false);
            }
        }

        //- Compress/Uncompress (only when selection is available)

        //Uncompress (Only for single supported files)
        if (!this.mMultiSelection && !FileHelper.isSupportedUncompressedFile(this.mFso)) {
            mActionExtract.setVisible(false);
        }

        // Send multiple (only regular files)
        boolean areAllFiles = true;
        for (FileSystemObject fso : selection) {
            if (FileHelper.isDirectory(fso)) {
                areAllFiles = false;
                break;
            }
        }
        if (!areAllFiles) {
            mActionSendSelection.setVisible(false);
        }

        // Remove not-ChRooted actions (actions that can't be present when running in
        // unprivileged mode)
        if (this.mChRooted) {
            mActionCreateLink.setVisible(false);
            mActionCreateLinkGlobal.setVisible(false);
            mActionExecute.setVisible(false);

            // NOTE: This actions are not implemented in chrooted environments. The reason is
            // that the main target of this application is CyanogenMod (a rooted environment).
            // Adding this actions requires the use of commons-compress, an external Apache
            // library that will add more size to the ending apk.
            // For now, will maintain without implementation. Maybe, in the future.
            mActionCompress.setVisible(false);
            mActionCompressSelection.setVisible(false);
            mActionExtract.setVisible(false);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            //- Rename
            case R.id.mnu_actions_rename:
                if (this.mOnSelectionListener != null) {
                    showFsoInputNameDialog(menuItem, this.mFso, false);
                    finish();
                    return true;
                }
                break;

            //- Create link
            case R.id.mnu_actions_create_link:
                if (this.mOnSelectionListener != null) {
                    showFsoInputNameDialog(menuItem, this.mFso, true);
                    finish();
                    return true;
                }
                break;
            case R.id.mnu_actions_create_link_global:
                if (this.mOnSelectionListener != null) {
                    // The selection must be only 1 item
                    List<FileSystemObject> selection =
                            this.mOnSelectionListener.onRequestSelectedFiles();
                    if (selection != null && selection.size() == 1) {
                        showFsoInputNameDialog(menuItem, selection.get(0), true);
                    }
                    finish();
                    return true;
                }
                break;

            //- Delete
            case R.id.mnu_actions_delete:
                DeleteActionPolicy.removeFileSystemObject(
                        this.mActivity,
                        this.mFso,
                        this.mOnSelectionListener,
                        this.mOnRequestRefreshListener,
                        null);
                finish();
                break;

            //- Refresh
            case R.id.mnu_actions_refresh:
                if (this.mOnRequestRefreshListener != null) {
                    this.mOnRequestRefreshListener.onRequestRefresh(null, false); //Refresh all
                }
                break;

            //- Open
            case R.id.mnu_actions_open:
                IntentsActionPolicy.openFileSystemObject(
                        this.mActivity, this.mFso, false, null, null);
                finish();
                break;
            //- Open with
            case R.id.mnu_actions_open_with:
                IntentsActionPolicy.openFileSystemObject(
                        this.mActivity, this.mFso, true, null, null);
                finish();
                break;

            //- Execute
            case R.id.mnu_actions_execute:
                ExecutionActionPolicy.execute(this.mActivity, this.mFso);
                finish();
                break;

            //- Send
            case R.id.mnu_actions_send:
                IntentsActionPolicy.sendFileSystemObject(
                        this.mActivity, this.mFso, null, null);
                finish();
                break;
            case R.id.mnu_actions_send_selection:
                if (this.mOnSelectionListener != null) {
                    List<FileSystemObject> selection =
                            this.mOnSelectionListener.onRequestSelectedFiles();
                    if (selection.size() == 1) {
                        IntentsActionPolicy.sendFileSystemObject(
                                this.mActivity, selection.get(0), null, null);
                    } else {
                        IntentsActionPolicy.sendMultipleFileSystemObject(
                                this.mActivity, selection, null, null);
                    }
                    finish();
                }
                break;

            //- Create copy
            case R.id.mnu_actions_copy:
                // Create a copy of the fso
                if (this.mOnSelectionListener != null) {
                    List<FileSystemObject> selection =
                            this.mOnSelectionListener.onRequestSelectedFiles();
                    CopyMoveActionPolicy.createCopyFileSystemObject(
                            selection,
                            this.onCopyMoveListener);
                    finish();
                }
                break;

            // Move selection
            case R.id.mnu_actions_move:
                if (this.mOnSelectionListener != null) {
                    List<FileSystemObject> selection =
                            this.mOnSelectionListener.onRequestSelectedFiles();
                    CopyMoveActionPolicy.createMoveFileSystemObject(
                            selection,
                            this.onCopyMoveListener);
                    finish();
                }
                break;

            // Delete selection
            case R.id.mnu_actions_delete_selection:
                if (this.mOnSelectionListener != null) {
                    List<FileSystemObject> selection =
                            this.mOnSelectionListener.onRequestSelectedFiles();
                    DeleteActionPolicy.removeFileSystemObjects(
                            this.mActivity,
                            selection,
                            this.mOnSelectionListener,
                            this.mOnRequestRefreshListener,
                            null);
                    finish();
                }
                break;

            //- Uncompress
            case R.id.mnu_actions_extract:
                CompressActionPolicy.uncompress(
                        this.mActivity,
                        this.mFso,
                        this.mOnRequestRefreshListener);
                break;
            //- Checksum
            case R.id.mnu_actions_compute_checksum:
                InfoActionPolicy.showComputeChecksumDialog(
                        this.mActivity,
                        this.mFso);
                break;
            //- Compress
            case R.id.mnu_actions_compress:
                if (this.mOnSelectionListener != null) {
                    CompressActionPolicy.compress(
                            this.mActivity,
                            this.mFso,
                            this.mOnSelectionListener,
                            this.mOnRequestRefreshListener);
                    finish();
                }
                break;
            case R.id.mnu_actions_compress_selection:
                if (this.mOnSelectionListener != null) {
                    CompressActionPolicy.compress(
                            this.mActivity,
                            this.mOnSelectionListener,
                            this.mOnRequestRefreshListener);
                    finish();
                }
                break;

            //- Add to bookmarks
            case R.id.mnu_actions_add_to_bookmarks:
                BookmarksActionPolicy.addToBookmarks(this.mActivity, this.mFso);
                break;

            //- Add shortcut
            case R.id.mnu_actions_add_shortcut:
                IntentsActionPolicy.createShortcut(this.mActivity, this.mFso);
                break;

            //- Properties
            case R.id.mnu_actions_properties:
            case R.id.mnu_actions_properties_current_folder:
                InfoActionPolicy.showPropertiesDialog(
                        this.mActivity, this.mFso, this.mOnRequestRefreshListener);
                break;

            //- Navigate to parent
            case R.id.mnu_actions_open_parent_folder:
                NavigationActionPolicy.openParentFolder(
                        this.mActivity, this.mFso, this.mOnRequestRefreshListener);
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Clear this before onDeselectAll() to prevent onDeselectAll() from
        // trying to close the
        // contextual mode again.
        mSelectionMode = null;
        mOnSelectionListener.onDeselectAll();
    }

    public void finish() {
        mSelectionMode.finish();
    }

    public void setClosedByUser(boolean closedByUser) {
        this.mClosedByUser = closedByUser;
    }

    public boolean inSelectionMode() {
        return mSelectionMode != null;
    }

    public void refresh() {
        mSelectionMode.invalidate();
    }

    /**
     * Method that show a new dialog for input a name for an existing fso.
     *
     * @param menuItem The item menu associated
     * @param fso The file system object
     * @param allowFsoName If allow that the name of the fso will be returned
     */
    private void showFsoInputNameDialog(
            final MenuItem menuItem, final FileSystemObject fso, final boolean allowFsoName) {

        //Show the input name dialog
        final InputNameDialog inputNameDialog =
                new InputNameDialog(
                        this.mActivity,
                        this.mOnSelectionListener.onRequestCurrentItems(),
                        fso,
                        allowFsoName,
                        menuItem.getTitle().toString());
        inputNameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Retrieve the name an execute the action
                try {
                    String name = inputNameDialog.getName();
                    switch (menuItem.getItemId()) {
                        case R.id.mnu_actions_rename:
                            // Rename the fso
                            if (SelectionModeCallback.this.mOnSelectionListener != null) {
                                CopyMoveActionPolicy.renameFileSystemObject(
                                        SelectionModeCallback.this.mActivity,
                                        inputNameDialog.mFso,
                                        name,
                                        SelectionModeCallback.this.mOnSelectionListener,
                                        SelectionModeCallback.this.mOnRequestRefreshListener);
                            }
                            break;

                        case R.id.mnu_actions_create_link:
                        case R.id.mnu_actions_create_link_global:
                            // Create a link to the fso
                            if (SelectionModeCallback.this.mOnSelectionListener != null) {
                                NewActionPolicy.createSymlink(
                                        SelectionModeCallback.this.mActivity,
                                        inputNameDialog.mFso,
                                        name,
                                        SelectionModeCallback.this.mOnSelectionListener,
                                        SelectionModeCallback.this.mOnRequestRefreshListener);
                            }
                            break;

                        default:
                            break;
                    }

                } catch (InflateException e) {
                    //TODO: Catch this exception properly
                }
            }
        });
        inputNameDialog.show();
    }
}
