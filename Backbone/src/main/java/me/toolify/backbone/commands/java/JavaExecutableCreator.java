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

package me.toolify.backbone.commands.java;

import me.toolify.backbone.R;
import me.toolify.backbone.commands.AsyncResultListener;
import me.toolify.backbone.commands.ChangeOwnerExecutable;
import me.toolify.backbone.commands.ChangePermissionsExecutable;
import me.toolify.backbone.commands.ChecksumExecutable;
import me.toolify.backbone.commands.CompressExecutable;
import me.toolify.backbone.commands.CopyExecutable;
import me.toolify.backbone.commands.CreateDirExecutable;
import me.toolify.backbone.commands.CreateFileExecutable;
import me.toolify.backbone.commands.DeleteDirExecutable;
import me.toolify.backbone.commands.DeleteFileExecutable;
import me.toolify.backbone.commands.DiskUsageExecutable;
import me.toolify.backbone.commands.EchoExecutable;
import me.toolify.backbone.commands.ExecExecutable;
import me.toolify.backbone.commands.ExecutableCreator;
import me.toolify.backbone.commands.FindExecutable;
import me.toolify.backbone.commands.FolderUsageExecutable;
import me.toolify.backbone.commands.GroupsExecutable;
import me.toolify.backbone.commands.IdentityExecutable;
import me.toolify.backbone.commands.LinkExecutable;
import me.toolify.backbone.commands.ListExecutable;
import me.toolify.backbone.commands.ListExecutable.LIST_MODE;
import me.toolify.backbone.commands.MountExecutable;
import me.toolify.backbone.commands.MountPointInfoExecutable;
import me.toolify.backbone.commands.MoveExecutable;
import me.toolify.backbone.commands.ParentDirExecutable;
import me.toolify.backbone.commands.ProcessIdExecutable;
import me.toolify.backbone.commands.QuickFolderSearchExecutable;
import me.toolify.backbone.commands.ReadExecutable;
import me.toolify.backbone.commands.ResolveLinkExecutable;
import me.toolify.backbone.commands.SIGNAL;
import me.toolify.backbone.commands.SendSignalExecutable;
import me.toolify.backbone.commands.UncompressExecutable;
import me.toolify.backbone.commands.WriteExecutable;
import me.toolify.backbone.console.CommandNotFoundException;
import me.toolify.backbone.console.java.JavaConsole;
import me.toolify.backbone.model.Group;
import me.toolify.backbone.model.MountPoint;
import me.toolify.backbone.model.Permissions;
import me.toolify.backbone.model.Query;
import me.toolify.backbone.model.User;
import me.toolify.backbone.preferences.CompressionMode;

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
        return new FindCommand(directory, query, asyncResultListener);
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
        return new ListCommand(src, LIST_MODE.DIRECTORY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListExecutable createFileInfoExecutable(String src, boolean followSymlinks)
            throws CommandNotFoundException {
        return new ListCommand(src, LIST_MODE.FILEINFO);
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
    public ProcessIdExecutable createProcessIdExecutable(int pid)
            throws CommandNotFoundException {
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
        return new ResolveLinkCommand(fso);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ChecksumExecutable createChecksumExecutable(
            String src, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        return new ChecksumCommand(src, asyncResultListener);
    }

}
