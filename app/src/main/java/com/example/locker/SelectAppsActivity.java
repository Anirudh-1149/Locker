package com.example.locker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;

import com.example.locker.Adapters.AppsAdapter;
import com.example.locker.Database.LockedApps;
import com.example.locker.Models.App;

import java.util.ArrayList;
import java.util.List;

public class SelectAppsActivity extends AppCompatActivity {
    ArrayList<App>apps;
    ArrayList<String>locked_apps;
    LockedApps db = new LockedApps(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        apps = new ArrayList<>();
        AppsAdapter appsAdapter = new AppsAdapter(apps,this);
        apps.clear();

        locked_apps = new ArrayList<>();
        Cursor cursor = db.getData();
        while(cursor.moveToNext())
            locked_apps.add(cursor.getString(0));
        cursor.close();

        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList)
        {
            int val = 0;
            String packageName = resolveInfo.activityInfo.packageName;
            if(locked_apps.contains(packageName))val = 1;
            apps.add(new App(resolveInfo.loadLabel(packageManager).toString(),resolveInfo.loadIcon(packageManager),val,packageName));
        }

        appsAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appsAdapter);
    }
}