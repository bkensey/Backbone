package me.toolify.backbone.dashclock;

import android.content.Intent;
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
        publishUpdate(new ExtensionData()
                .status("Loading")
                .visible(true)
                .icon(R.drawable.ic_launcher)
                .expandedTitle("Backbone loading...")
                .clickIntent(new Intent(this, NavigationActivity.class))
        );
        try {
            StringBuilder sbBody = new StringBuilder();
            long free = 0, total = 0;
            for(Object storage : StorageHelper.getStorageVolumes(this))
            {
                String desc = StorageHelper.getStorageVolumeDescription(this, storage);
                String path = StorageHelper.getStoragePath(storage);
                MountPoint mp = MountPointHelper.getMountPointFromDirectory(path);
                if(!MountPointHelper.isReadWrite(mp)) continue;
                DiskUsage du = CommandHelper.getDiskUsage(this, path, null);
                if(mp.getOptions().indexOf("mode=0")>-1) continue; // Cyanogenmod USB workaround
                if(du.getTotal() == 0) continue;
                free += du.getFree();
                total += du.getTotal();
                sbBody.append(desc + ": " + FileHelper.getHumanReadableSize(du.getFree()) + "/" + FileHelper.getHumanReadableSize(du.getTotal()) + "\n");
            }
            if(sbBody.length() == 0) {
                publishUpdate(new ExtensionData().visible(false));
                return;
            }
            sbBody.setLength(sbBody.length() - 1);
            publishUpdate(new ExtensionData()
                    .icon(R.drawable.ic_sdcard_drawable)
                    .visible(true)
                    .status(FileHelper.getHumanReadableSize(total))
                    .expandedTitle("Total: " + FileHelper.getHumanReadableSize(free) + "/" + FileHelper.getHumanReadableSize(total))
                    .expandedBody(sbBody.toString())
                    .clickIntent(new Intent(this, NavigationActivity.class))
            );
        } catch(Exception e) {
            Log.e(TAG, "Unable to get disk usage", e);
        }
    }
}
