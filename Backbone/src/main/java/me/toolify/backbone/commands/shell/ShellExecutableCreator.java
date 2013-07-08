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

package me.toolify.backbone.commands.shell;

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
import me.toolify.backbone.console.InsufficientPermissionsException;
import me.toolify.backbone.console.NoSuchFileOrDirectory;
import me.toolify.backbone.console.shell.ShellConsole;
import me.toolify.backbone.model.Group;
import me.toolify.backbone.model.MountPoint;
import me.toolify.backbone.model.Permissions;
import me.toolify.backbone.model.Query;
import me.toolify.backbone.model.User;
import me.toolify.backbone.preferences.CompressionMode;

/**
 * A class for create shell {@link "Executable"} objects.
 */
public class ShellExecutableCreator implements ExecutableCreator {

    private final ShellConsole mConsole;

    /**
     * Constructor of <code>ShellExecutableCreator</code>.
     *
     * @param console A shell console that use for create objects
     */
    ShellExecutableCreator(ShellConsole console) {
        super();
        this.mConsole = console;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeOwnerExecutable createChangeOwnerExecutable(
            String fso, User newUser, Group newGroup) throws CommandNotFoundException {
        try {
            return new ChangeOwnerCommand(fso, newUser, newGroup);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ChangeOwnerCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangePermissionsExecutable createChangePermissionsExecutable(
            String fso, Permissions newPermissions) throws CommandNotFoundException {
        try {
            return new ChangePermissionsCommand(fso, newPermissions);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ChangePermissionsCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CopyExecutable createCopyExecutable(String src, String dst)
            throws CommandNotFoundException {
        try {
            return new CopyCommand(src, dst);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CopyCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateDirExecutable createCreateDirectoryExecutable(String dir)
            throws CommandNotFoundException {
        try {
            return new CreateDirCommand(dir);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CreateDirCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateFileExecutable createCreateFileExecutable(String file)
            throws CommandNotFoundException {
        try {
            return new CreateFileCommand(file);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CreateFileCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteDirExecutable createDeleteDirExecutable(String dir)
            throws CommandNotFoundException {
        try {
            return new DeleteDirCommand(dir);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("DeleteDirCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteFileExecutable createDeleteFileExecutable(String file)
            throws CommandNotFoundException {
        try {
            return new DeleteFileCommand(file);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("DeleteFileCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskUsageExecutable createDiskUsageExecutable() throws CommandNotFoundException {
        try {
            return new DiskUsageCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("DiskUsageCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskUsageExecutable createDiskUsageExecutable(String dir)
            throws CommandNotFoundException {
        try {
            return new DiskUsageCommand(dir);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("DiskUsageCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EchoExecutable createEchoExecutable(String msg) throws CommandNotFoundException {
        try {
            return new EchoCommand(msg);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("EchoCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecExecutable createExecExecutable(
            String cmd, AsyncResultListener asyncResultListener) throws CommandNotFoundException {
        try {
            return new ExecCommand(cmd, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ExecCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FindExecutable createFindExecutable(
            String directory, Query query, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new FindCommand(directory, query, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("FindCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FolderUsageExecutable createFolderUsageExecutable(
            String directory, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new FolderUsageCommand(directory, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("FolderUsageCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupsExecutable createGroupsExecutable() throws CommandNotFoundException {
        try {
            return new GroupsCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("GroupsCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityExecutable createIdentityExecutable() throws CommandNotFoundException {
        try {
            return new IdentityCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("IdentityCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkExecutable createLinkExecutable(String src, String link)
            throws CommandNotFoundException {
        try {
            return new LinkCommand(src, link);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("LinkCommand", icdEx); //$NON-NLS-1$
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ListExecutable createListExecutable(String src) throws CommandNotFoundException {
        try {
            return new ListCommand(src, this.mConsole);
        } catch (Throwable throwEx) {
            throw new CommandNotFoundException("ListCommand (DIRECTORY)", throwEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListExecutable createFileInfoExecutable(String src, boolean followSymlinks)
            throws CommandNotFoundException {
        try {
            return new ListCommand(src, followSymlinks, this.mConsole);
        } catch (Throwable throwEx) {
            throw new CommandNotFoundException("ListCommand (FILEINFO)", throwEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountExecutable createMountExecutable(MountPoint mp, boolean rw)
            throws CommandNotFoundException {
        try {
            return new MountCommand(mp, rw);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("MountCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountPointInfoExecutable createMountPointInfoExecutable()
            throws CommandNotFoundException {
        try {
            return new MountPointInfoCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("MountPointInfoCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MoveExecutable createMoveExecutable(String src, String dst)
            throws CommandNotFoundException {
        try {
            return new MoveCommand(src, dst);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("MoveCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParentDirExecutable createParentDirExecutable(String fso)
            throws CommandNotFoundException {
        try {
            return new ParentDirCommand(fso);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ParentDirCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessIdExecutable createShellProcessIdExecutable() throws CommandNotFoundException {
        try {
            return new ProcessIdCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ProcessIdCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessIdExecutable createProcessIdExecutable(int pid)
            throws CommandNotFoundException {
        try {
            return new ProcessIdCommand(pid);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ProcessIdCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessIdExecutable createProcessIdExecutable(int pid, String processName)
            throws CommandNotFoundException {
        try {
            return new ProcessIdCommand(pid, processName);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ProcessIdCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuickFolderSearchExecutable createQuickFolderSearchExecutable(String regexp)
            throws CommandNotFoundException {
        try {
            return new QuickFolderSearchCommand(regexp);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("QuickFolderSearchCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadExecutable createReadExecutable(
            String file, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new ReadCommand(file, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ReadCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolveLinkExecutable createResolveLinkExecutable(String fso)
            throws CommandNotFoundException {
        try {
            return new ResolveLinkCommand(fso);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ResolveLinkCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SendSignalExecutable createSendSignalExecutable(int process, SIGNAL signal)
            throws CommandNotFoundException {
        try {
            return new SendSignalCommand(process, signal);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("SendSignalCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SendSignalExecutable createKillExecutable(int process)
            throws CommandNotFoundException {
        try {
            return new SendSignalCommand(process);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("SendSignalCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WriteExecutable createWriteExecutable(
            String file, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new WriteCommand(file, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("WriteCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompressExecutable createCompressExecutable(
            CompressionMode mode, String dst, String[] src,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new CompressCommand(mode, dst, src, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CompressCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompressExecutable createCompressExecutable(
            CompressionMode mode, String src,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new CompressCommand(mode, src, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CompressCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UncompressExecutable createUncompressExecutable(
            String src, String dst,
            AsyncResultListener asyncResultListener)
            throws CommandNotFoundException {
        try {
            return new UncompressCommand(src, dst, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("UncompressCommand", icdEx); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChecksumExecutable createChecksumExecutable(
            String src, AsyncResultListener asyncResultListener)
            throws CommandNotFoundException, NoSuchFileOrDirectory,
            InsufficientPermissionsException {
        try {
            return new ChecksumCommand(src, asyncResultListener);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ChecksumCommand", icdEx); //$NON-NLS-1$
        }
    }

}
