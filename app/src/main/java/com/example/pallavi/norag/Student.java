package com.example.pallavi.norag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.pallavi.norag.Manifest.*;

public class Student extends Fragment{
    String jsonresponse, token;
    JSONObject jsonObject;
    LocationManager locationManager;
    String mprovider,text;
    static ViewGroup layout ;  //define it globally
    MaterialBetterSpinner materialDesignSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.activity_student_, container, false);
        layout = (ViewGroup) view.findViewById(R.id.coordinatorlayout);
        Log.v("Check Error", "In the student activity");
        Toast.makeText(getActivity(), "Shake Here Hard", Toast.LENGTH_SHORT);
        text="";
        String[] SPINNERLIST = {"I am new MPH", "I am near ITRC", "I am near EC Department", "I am near ATM"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
         materialDesignSpinner = (MaterialBetterSpinner)view.
                findViewById(R.id.spinnerstatus);
        materialDesignSpinner.setAdapter(arrayAdapter);
        //materialDesignSpinner.setOnClickListener(new AdapterView.OnItemClickListener());

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("Student","Test");
               text=String.valueOf(materialDesignSpinner.getAdapter().getItem(position));
                Log.v("Student",text);
                Intent newintent = new Intent(getActivity(), SensorService.class);
                newintent.putExtra("helptext",text);
                getActivity().startService(newintent);

            }

        });
      /*  materialDesignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Student","Test");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      */
       // Log.v("Student",text);
        Log.v("Student",text);
        Intent newintent = new Intent(getActivity(), SensorService.class);
        newintent.putExtra("helptext",text);
        getActivity().startService(newintent);





return  view;

    }


    public void onBackPressed() {

        onBackPressed();
        Intent intent = new Intent(getActivity(), SensorService.class);
        //Start Service
        getActivity().stopService(intent);
    }





}
