package me.toolify.backbone.dashclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import me.toolify.backbone.R;
import me.toolify.backbone.model.MountPoint;
import me.toolify.backbone.util.MountPointHelper;
import me.toolify.backbone.util.StorageHelper;

/**
 * Created by brandon on 7/26/13.
 */
public class DashSettings extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.ic_launcher_settings);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupPreferences();
    }

    private String getUsageType(boolean free)
    {
        String ret = getString(free ?
                R.string.filesystem_info_dialog_free_disk_usage :
                R.string.filesystem_info_dialog_used_disk_usage);
        ret = ret.substring(0, ret.length() - 1); // remove colon
        return ret;
    }

    private void setupPreferences()
    {
        getPreferenceManager().setSharedPreferencesName("dashclock");
        addPreferencesFromResource(R.xml.preferences_dashclock);
        final SharedPreferences prefs = getSharedPreferences("dashclock", MODE_PRIVATE);
        final PreferenceScreen mIcon = (PreferenceScreen)findPreference("pref_icon");
        final Preference mUsage = findPreference("pref_usage");
        mIcon.setIcon(prefs.getInt("icon", R.drawable.ic_holo_dark_sdcard));
        boolean showFree = prefs.getBoolean("usage", true);
        mUsage.setSummary(getUsageType(showFree));
        mUsage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean free = !prefs.getBoolean("usage", true);
                prefs.edit().putBoolean("usage", free).apply();
                mUsage.setSummary(getUsageType(free));
                return false;
            }
        });
        mIcon.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Context context = DashSettings.this;
                final int iconSel = prefs.getInt("icon", R.drawable.ic_holo_dark_sdcard);
                final LayoutInflater inflater = getLayoutInflater();
                LinearLayout mIconParent = new LinearLayout(context);
                mIconParent.setBackground(getResources().getDrawable(R.drawable.dark_background));
                mIconParent.setOrientation(LinearLayout.HORIZONTAL);
                TypedArray ar = getResources().obtainTypedArray(R.array.dash_icons);
                final AlertDialog dialog = new AlertDialog.Builder(DashSettings.this)
                        .setView(mIconParent)
                        .create();
                for(int i = 0; i < ar.length(); i++)
                {
                    final int icon = ar.getResourceId(i, 0);
                    ImageView imageView = (ImageView)inflater
                            .inflate(R.layout.simple_icon, mIconParent, false);
                    imageView.setImageResource(icon);
                    if(icon == iconSel)
                        imageView.setSelected(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prefs.edit()
                                    .putInt("icon", icon)
                                    .apply();
                            if (mIcon != null)
                                mIcon.setIcon(icon);
                            dialog.dismiss();
                        }
                    });
                    mIconParent.addView(imageView);
                }
                ar.recycle();
                dialog.show();
                return false;
            }
        });
        PreferenceCategory mStores = (PreferenceCategory)findPreference("pref_storage");
        if(mStores == null)
            return;
        for(Object storage : StorageHelper.getStorageVolumes(this))
        {
            final String path = StorageHelper.getStoragePath(storage);
            final String desc = StorageHelper.getStorageVolumeDescription(this, storage);
            final CheckBoxPreference p = new CheckBoxPreference(this);
            boolean useMe = true;
            MountPoint mp = MountPointHelper.getMountPointFromDirectory(path);
            if(mp.getOptions().indexOf("mode=0")>-1) useMe = false; // Cyanogenmod USB workaround
            if(!MountPointHelper.isReadWrite(mp)) useMe = false;
            p.setTitle(desc);
            p.setChecked(prefs.getBoolean("storage_" + path, useMe));
            p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    prefs.edit()
                         .putBoolean("storage_" + path, p.isChecked())
                         .apply();
                    return false;
                }
            });
            mStores.addPreference(p);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
