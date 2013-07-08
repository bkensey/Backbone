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

import android.util.Log;

import me.toolify.backbone.commands.CreateFileExecutable;
import me.toolify.backbone.console.ExecutionException;
import me.toolify.backbone.console.InsufficientPermissionsException;
import me.toolify.backbone.console.NoSuchFileOrDirectory;
import me.toolify.backbone.model.MountPoint;
import me.toolify.backbone.util.MountPointHelper;

import java.io.File;
import java.io.IOException;


/**
 * A class for create a file.
 */
public class CreateFileCommand extends Program implements CreateFileExecutable {

    private static final String TAG = "CreateFileCommand"; //$NON-NLS-1$


    private final String mPath;

    /**
     * Constructor of <code>CreateFileCommand</code>.
     *
     * @param path The name of the new file
     */
    public CreateFileCommand(String path) {
        super();
        this.mPath = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getResult() {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
            throws InsufficientPermissionsException, NoSuchFileOrDirectory, ExecutionException {
        if (isTrace()) {
            Log.v(TAG,
                    String.format("Creating file: %s", this.mPath)); //$NON-NLS-1$
        }

        File f = new File(this.mPath);
        try {
            // Check that if the path exist, it need to be a file. Otherwise something is
            // wrong
            if (f.exists() && !f.isFile()) {
                if (isTrace()) {
                    Log.v(TAG, "Result: FAIL. ExecutionException"); //$NON-NLS-1$
                }
                throw new ExecutionException("the path exists but is not a file"); //$NON-NLS-1$
            }

            // Only create the file if the file not exists. Otherwise createNewFile
            // will return false
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    if (isTrace()) {
                        Log.v(TAG, "Result: FAIL. InsufficientPermissionsException"); //$NON-NLS-1$
                    }
                    throw new InsufficientPermissionsException();
                }
            }
        } catch (IOException ioe) {
            if (isTrace()) {
                Log.v(TAG, "Result: FAIL. InsufficientPermissionsException"); //$NON-NLS-1$
            }
            throw new InsufficientPermissionsException();
        }

        if (isTrace()) {
            Log.v(TAG, "Result: OK"); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountPoint getSrcWritableMountPoint() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MountPoint getDstWritableMountPoint() {
        return MountPointHelper.getMountPointFromDirectory(this.mPath);
    }
}
