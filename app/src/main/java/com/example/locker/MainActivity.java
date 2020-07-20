package com.example.locker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static android.content.ContentValues.TAG;

import com.example.locker.Database.Password_Database;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String string;
    Button btn_pass, btn_app;
    List<String>pass;
    Cursor cursor;
    Password_Database db;
    Context cnt;
    private BackgroundService backgroundService = new BackgroundService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_app = findViewById(R.id.select_app);
        btn_pass = findViewById(R.id.update_pass);

        pass = new ArrayList<>();
        db  = new Password_Database(this);
        cnt  = this;
        cursor = db.getData();

        Log.i(TAG, "onCreate: MainActivity Created");

        while(cursor.moveToNext())
            pass.add(cursor.getString(0));


        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
            Intent intent2 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS,Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent2,0);
            startService(new Intent(this,backgroundService.getClass()));
        }

        btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass.isEmpty())
                    Toast.makeText(MainActivity.this,"First Set Password",Toast.LENGTH_LONG).show();
                else
                {
                    startActivity(new Intent(MainActivity.this,SelectAppsActivity.class));
                }
            }
        });
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
                builder.setTitle("Set Password");

                final EditText input = new EditText(cnt);

                input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        string = input.getText().toString();
                        if(string.isEmpty())
                            Toast.makeText(MainActivity.this,"password can't be empty",Toast.LENGTH_LONG).show();
                        else
                        {
                            boolean val = db.update_password(string);
                            if(val)
                            {
                                Toast.makeText(MainActivity.this,"password updated Successfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Password not updated",Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

//    private boolean permissionCheck() throws PackageManager.NameNotFoundException {
//        PackageManager packageManager = getPackageManager();
//        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(),0);
//        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(this.APP_OPS_SERVICE);
//        int mode = 0;
//
//
//    }
}