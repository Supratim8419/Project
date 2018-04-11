package com.example.pallavi.norag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class signinas extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button enter;
    private RadioButton radioButton;
    private int roleid,role,roleasid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinas);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(signinas.this);
        roleasid = sharedPref.getInt("roleas", -1);
        if (roleasid==-1)
        {
            radioGroup = (RadioGroup) findViewById(R.id.radio);
            enter = (Button) findViewById(R.id.enter);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    String troleas=radioButton.getText().toString();
                    if (troleas.equalsIgnoreCase("proctor"))
                        roleasid=1;
                    else if (troleas.equalsIgnoreCase("faculty"))
                        roleasid=2;
                    else if (troleas.equalsIgnoreCase("studentmember"))
                        roleasid=3;
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(signinas.this);
                    SharedPreferences.Editor ed = sharedPrefs.edit();
                    ed.putInt("roleas", roleasid);
                    ed.commit();
                    Intent authoritylogin = new Intent(signinas.this, AuthorityLogin.class);
                    startActivity(authoritylogin);
                    signinas.this.finish();
                }
            });
        }
        else if (roleasid!=-1)
        {
            //  Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
            //  Log.v("Error",""+radioButton.getText());
                 Intent authoritylogin = new Intent(signinas.this, AuthorityLogin.class);
                startActivity(authoritylogin);
                signinas.this.finish();


        }
    }
}
