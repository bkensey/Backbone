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

package com.brandroidtools.filemanager.commands.shell;

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
import com.brandroidtools.filemanager.console.shell.ShellConsole;
import com.brandroidtools.filemanager.model.Group;
import com.brandroidtools.filemanager.model.MountPoint;
import com.brandroidtools.filemanager.model.Permissions;
import com.brandroidtools.filemanager.model.Query;
import com.brandroidtools.filemanager.model.User;
import com.brandroidtools.filemanager.preferences.CompressionMode;

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
    public ChangeCurrentDirExecutable createChangeCurrentDirExecutable(String dir)
            throws CommandNotFoundException {
        try {
            return new ChangeCurrentDirCommand(dir);
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("ChangeCurrentDirCommand", icdEx); //$NON-NLS-1$
        }
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
    public CurrentDirExecutable createCurrentDirExecutable() throws CommandNotFoundException {
        try {
            return new CurrentDirCommand();
        } catch (InvalidCommandDefinitionException icdEx) {
            throw new CommandNotFoundException("CurrentDirCommand", icdEx); //$NON-NLS-1$
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

}
