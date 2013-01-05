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

package com.brandroidtools.filemanager.commands.java;

import com.brandroidtools.filemanager.R;
import com.brandroidtools.filemanager.commands.AsyncResultListener;
import com.brandroidtools.filemanager.commands.ChangeCurrentDirExecutable;
import com.brandroidtools.filemanager.commands.ChangeOwnerExecutable;
import com.brandroidtools.filemanager.commands.ChangePermissionsExecutable;
import com.brandroidtools.filemanager.commands.CompressExecutable;
import com.brandroidtools.filemanager.commands.CopyExecutable;
import com.brandroidtools.filemanager.commands.CreateDirExecutable;
import com.brandroidtools.filemanager.commands.CreateFileExecutable;
import com.brandroidtools.filemanager.commands.CurrentDirExecutable;
import com.brandroidtools.filemanager.commands.DeleteDirExecutable;
import com.brandroidtools.filemanager.commands.DeleteFileExecutable;
import com.brandroidtools.filemanager.commands.DiskUsageExecutable;
import com.brandroidtools.filemanager.commands.EchoExecutable;
import com.brandroidtools.filemanager.commands.ExecExecutable;
import com.brandroidtools.filemanager.commands.ExecutableCreator;
import com.brandroidtools.filemanager.commands.FindExecutable;
import com.brandroidtools.filemanager.commands.FolderUsageExecutable;
import com.brandroidtools.filemanager.commands.GroupsExecutable;
import com.brandroidtools.filemanager.commands.IdentityExecutable;
import com.brandroidtools.filemanager.commands.LinkExecutable;
import com.brandroidtools.filemanager.commands.ListExecutable;
import com.brandroidtools.filemanager.commands.ListExecutable.LIST_MODE;
import com.brandroidtools.filemanager.commands.MountExecutable;
import com.brandroidtools.filemanager.commands.MountPointInfoExecutable;
import com.brandroidtools.filemanager.commands.MoveExecutable;
import com.brandroidtools.filemanager.commands.ParentDirExecutable;
import com.brandroidtools.filemanager.commands.ProcessIdExecutable;
import com.brandroidtools.filemanager.commands.QuickFolderSearchExecutable;
import com.brandroidtools.filemanager.commands.ReadExecutable;
import com.brandroidtools.filemanager.commands.ResolveLinkExecutable;
import com.brandroidtools.filemanager.commands.SIGNAL;
import com.brandroidtools.filemanager.commands.SendSignalExecutable;
import com.brandroidtools.filemanager.commands.UncompressExecutable;
import com.brandroidtools.filemanager.commands.WriteExecutable;
import com.brandroidtools.filemanager.console.CommandNotFoundException;
import com.brandroidtools.filemanager.console.java.JavaConsole;
import com.brandroidtools.filemanager.model.Group;
import com.brandroidtools.filemanager.model.MountPoint;
import com.brandroidtools.filemanager.model.Permissions;
import com.brandroidtools.filemanager.model.Query;
import com.brandroidtools.filemanager.model.User;
import com.brandroidtools.filemanager.preferences.CompressionMode;

/**
 * A class for create shell {@link "Executable"} objects.
 */
public class JavaExecutableCreator implements ExecutableCreator {

    private final JavaConsole mConsole;

    /**
     * Constructor of <code>JavaExecutableCreator</code>.
     *
     * @param console A shell console that use for create objects
     */
    JavaExecutableCreator(JavaConsole console) {
        super();
        this.mConsole = console;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeCurrentDirExecutable createChangeCurrentDirExecutable(String dir)
            throws CommandNotFoundException {
        return new ChangeCurrentDirCommand(this.mConsole, dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeOwnerExecutable createChangeOwnerExecutable(
            String fso, User newUser, Group newGroup) throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangePermissionsExecutable createChangePermissionsExecutable(
            String fso, Permissions newPermissions) throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CopyExecutable createCopyExecutable(String src, String dst)
            throws CommandNotFoundException {
        return new CopyCommand(src, dst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateDirExecutable createCreateDirectoryExecutable(String dir)
            throws CommandNotFoundException {
        return new CreateDirCommand(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateFileExecutable createCreateFileExecutable(String file)
            throws CommandNotFoundException {
        return new CreateFileCommand(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentDirExecutable createCurrentDirExecutable() throws CommandNotFoundException {
        return new CurrentDirCommand(this.mConsole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteDirExecutable createDeleteDirExecutable(String dir)
            throws CommandNotFoundException {
        return new DeleteDirCommand(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteFileExecutable createDeleteFileExecutable(String file)
            throws CommandNotFoundException {
        return new DeleteFileCommand(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskUsageExecutable createDiskUsageExecutable() throws CommandNotFoundException {
        String mountsFile = this.mConsole.getCtx().getString(R.string.mounts_file);
        return new DiskUsageCommand(mountsFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskUsageExecutable createDiskUsageExecutable(String dir)
            throws CommandNotFoundException {
        String mountsFile = this.mConsole.getCtx().getString(R.string.mounts_file);
        return new DiskUsageCommand(mountsFile, dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EchoExecutable createEchoExecutable(String msg) throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecExecutable createExecExecutable(
            String cmd, AsyncResultListener asyncResultListener) throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FindExecutable createFindExecutable(
            String directory, Query query, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        return new FindCommand(this.mConsole.getCtx(), directory, query, asyncResultListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FolderUsageExecutable createFolderUsageExecutable(
            String directory, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        return new FolderUsageCommand(directory, asyncResultListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupsExecutable createGroupsExecutable() throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityExecutable createIdentityExecutable() throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkExecutable createLinkExecutable(String src, String link)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ListExecutable createListExecutable(String src)
            throws CommandNotFoundException {
        return new ListCommand(this.mConsole.getCtx(), src, LIST_MODE.DIRECTORY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListExecutable createFileInfoExecutable(String src, boolean followSymlinks)
            throws CommandNotFoundException {
        return new ListCommand(this.mConsole.getCtx(), src, LIST_MODE.FILEINFO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountExecutable createMountExecutable(MountPoint mp, boolean rw)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountPointInfoExecutable createMountPointInfoExecutable()
            throws CommandNotFoundException {
        String mountsFile = this.mConsole.getCtx().getString(R.string.mounts_file);
        return new MountPointInfoCommand(mountsFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MoveExecutable createMoveExecutable(String src, String dst)
            throws CommandNotFoundException {
        return new MoveCommand(src, dst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParentDirExecutable createParentDirExecutable(String fso)
            throws CommandNotFoundException {
        return new ParentDirCommand(fso);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessIdExecutable createShellProcessIdExecutable() throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessIdExecutable createProcessIdExecutable(int pid, String processName)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuickFolderSearchExecutable createQuickFolderSearchExecutable(String regexp)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadExecutable createReadExecutable(
            String file, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        return new ReadCommand(file, asyncResultListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolveLinkExecutable createResolveLinkExecutable(String fso)
            throws CommandNotFoundException {
        return new ResolveLinkCommand(this.mConsole.getCtx(), fso);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SendSignalExecutable createSendSignalExecutable(int process, SIGNAL signal)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SendSignalExecutable createKillExecutable(int process)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WriteExecutable createWriteExecutable(
            String file, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        return new WriteCommand(file, asyncResultListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompressExecutable createCompressExecutable(
            CompressionMode mode, String dst, String[] src,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompressExecutable createCompressExecutable(
            CompressionMode mode, String src,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UncompressExecutable createUncompressExecutable(
            String src, String dst,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        throw new CommandNotFoundException("Not implemented"); //$NON-NLS-1$
    }

}
