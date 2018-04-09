package com.example.pallavi.norag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.security.AuthProvider;

public class Signin extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button signinbtn;
    private RadioButton radioButton;
    private int roleid,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Signin.this);
        role = sharedPref.getInt("role", -1);
         if (role==-1)
        {
            radioGroup = (RadioGroup) findViewById(R.id.radio);
            signinbtn = (Button) findViewById(R.id.signin);

            signinbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    String trole=radioButton.getText().toString();
                    if (trole.equalsIgnoreCase("student"))
                        roleid=1;
                    else if (trole.equalsIgnoreCase("authority"))
                        roleid=2;


                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Signin.this);
                    SharedPreferences.Editor ed = sharedPrefs.edit();
                    ed.putInt("role", roleid);
                    ed.commit();
                    Log.v("Error",""+radioButton.getText());
                    if (roleid == 1) {
                        Intent studentlogin = new Intent(Signin.this, Login.class);
                        startActivity(studentlogin);
                    } else if (roleid == 2) {
                        Intent authoritylogin = new Intent(Signin.this, signinas.class);
                        startActivity(authoritylogin);
                    }
                    //Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }



            });
        }
         else if (role!=-1)
         {
           //  Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
           //  Log.v("Error",""+radioButton.getText());
             if (role == 1) {
                 Intent studentlogin = new Intent(Signin.this, Login.class);
                 startActivity(studentlogin);
                 Signin.this.finish();
             } else if (role == 2) {
                 Intent authoritylogin = new Intent(Signin.this, signinas.class);
                 startActivity(authoritylogin);
                 Signin.this.finish();
             }

         }

    }
}
