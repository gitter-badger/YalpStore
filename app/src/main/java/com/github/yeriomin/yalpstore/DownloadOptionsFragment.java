package com.github.yeriomin.yalpstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.yeriomin.yalpstore.model.App;

public class DownloadOptionsFragment extends DetailsFragment {

    public DownloadOptionsFragment(DetailsActivity activity, App app) {
        super(activity, app);
    }

    @Override
    public void draw() {
        final ImageButton more = (ImageButton) activity.findViewById(R.id.more);
        if (null == more) {
            return;
        }
        activity.registerForContextMenu(more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.showContextMenu();
            }
        });

    }

    public void inflate(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_download, menu);
        if (app.isInstalled()) {
            menu.findItem(R.id.action_get_local_apk).setVisible(true);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_manual:
                activity.startActivity(new Intent(activity, ManualDownloadActivity.class));
                return true;
            case R.id.action_get_local_apk:
                copyLocalApk();
                return true;
            default:
                return activity.onContextItemSelected(item);
        }
    }

    private void copyLocalApk() {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPostExecute(Boolean result) {
                String message = activity.getString(InstalledApkCopier.copy(app)
                    ? R.string.details_saved_in_downloads
                    : R.string.details_could_not_copy_apk
                );
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return InstalledApkCopier.copy(app);
            }
        };
        task.execute();
    }
}