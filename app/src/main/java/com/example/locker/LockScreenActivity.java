package com.example.locker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static android.content.ContentValues.TAG;

public class LockScreenActivity extends AppCompatActivity {

    EditText pass_entered;
    Button btn;
    String pass,package_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        Log.i(TAG, "onCreate: LockScreen Entered");
        btn = findViewById(R.id.button);
        pass_entered = findViewById(R.id.editTextTextPassword5);

        Bundle strings = getIntent().getExtras();

        assert strings != null;
        pass = strings.getString("password");
        package_name = strings.getString("package_name");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.equals(pass_entered.getText().toString()))
                {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(package_name);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LockScreenActivity.this,"Correct Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(LockScreenActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
                }
            }
        });


        
    }
}