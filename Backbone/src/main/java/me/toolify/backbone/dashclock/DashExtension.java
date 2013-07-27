package me.toolify.backbone.dashclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import me.toolify.backbone.R;
import me.toolify.backbone.activities.NavigationActivity;
import me.toolify.backbone.model.DiskUsage;
import me.toolify.backbone.model.MountPoint;
import me.toolify.backbone.util.CommandHelper;
import me.toolify.backbone.util.FileHelper;
import me.toolify.backbone.util.MountPointHelper;
import me.toolify.backbone.util.StorageHelper;

/**
 * Created by Brandon on 7/26/13.
 */
public class DashExtension extends DashClockExtension {
    private final static String TAG = "DashExtension";

    @Override
    protected void onUpdateData(int reason) {
        final SharedPreferences prefs = getSharedPreferences("dashclock", MODE_PRIVATE);
        final boolean showFree = prefs.getBoolean("usage", true);
        int icon = prefs.getInt("icon", R.drawable.ic_holo_dark_sdcard);
        try {
            StringBuilder sbBody = new StringBuilder();
            long free = 0, total = 0;
            for(Object storage : StorageHelper.getStorageVolumes(this))
            {
                String desc = StorageHelper.getStorageVolumeDescription(this, storage);
                String path = StorageHelper.getStoragePath(storage);
                boolean useMe = true;
                MountPoint mp = MountPointHelper.getMountPointFromDirectory(path);
                if(mp.getOptions().indexOf("mode=0")>-1) useMe = false; // Cyanogenmod USB workaround
                if(!MountPointHelper.isReadWrite(mp)) useMe = false;
                if(!prefs.getBoolean("storage_" + path, useMe)) continue;
                DiskUsage du = CommandHelper.getDiskUsage(this, path, null);
                long usage = showFree ? du.getFree() : du.getUsed();
                free += usage;
                total += du.getTotal();
                sbBody.append(desc + ": " +
                        FileHelper.getHumanReadableSize(usage) + "/" +
                        FileHelper.getHumanReadableSize(du.getTotal()) + "\n");
            }
            if(sbBody.length() == 0) {
                publishUpdate(new ExtensionData().visible(false));
                return;
            }
            sbBody.setLength(sbBody.length() - 1);
            publishUpdate(new ExtensionData()
                    .icon(icon)
                    .visible(true)
                    .status(FileHelper.getHumanReadableSize(free))
                    .expandedTitle("Total: " +
                            FileHelper.getHumanReadableSize(free) + "/" +
                            FileHelper.getHumanReadableSize(total))
                    .expandedBody(sbBody.toString())
                    .clickIntent(new Intent(this, NavigationActivity.class))
            );
        } catch(Exception e) {
            Log.e(TAG, "Unable to get disk usage", e);
        }
    }
}
