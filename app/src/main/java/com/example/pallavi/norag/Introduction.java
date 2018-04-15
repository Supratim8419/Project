package com.example.pallavi.norag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.firebase.messaging.FirebaseMessaging;

public class Introduction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    Button login_signup1;
    SharedPreferences sp;
    int roleid, sessionid,code;
    LocationManager locationManager;
    String mprovider;
    String baseurl;
    Location location;
    float sourcelatitude, sourcelongitude;
    NavigationView navigationView, navigationView1;
    MaterialDialog md, mdl;
    CoordinatorLayout cl;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        code = getIntent().getExtras().getInt("code");



        if (code==1) {
            Fragment fragment = new Home();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"You have logged in successfully..", Snackbar.LENGTH_SHORT);
            sn.setActionTextColor(Color.MAGENTA);
            View sbView = sn.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.myblue));
            sn.show();
        }
        else if (code==2) {
            Fragment fragment = new complain();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        sp = PreferenceManager.getDefaultSharedPreferences(Introduction.this);
        roleid = sp.getInt("role", -1);
      //  Toast.makeText(this, "sdas " + roleid, Toast.LENGTH_SHORT).show();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Log.v("Error Location Manager", locationManager.toString());
        // mprovider = locationManager.getBestProvider(criteria, false);
        //mprovider=locationManager.NETWORK_PROVIDER;
        mprovider = locationManager.PASSIVE_PROVIDER;

        Log.v("Error Mprovider", mprovider.toString());

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 5);

            } else {

                //   location = locationManager.getLastKnownLocation(mprovider);
                locationManager.requestLocationUpdates(mprovider, 1500000, 1, this);

                location = locationManager.getLastKnownLocation(mprovider);

                Log.v("Error Location", "" + location);


                if (location != null) {
                    onLocationChanged(location);
                    sourcelatitude = Float.parseFloat(String.valueOf(location.getLatitude()));
                    sourcelongitude = Float.parseFloat(String.valueOf(location.getLongitude()));

                } else if (location == null)
                // Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                {
                    Log.v("Error ", "No location Found");
                    md=new MaterialDialog.Builder(this)
                            .title("Turn On GPS Location")
                            .content("To Turn on GPS location press Yes or if you already had turned on then please wait for sometime")
                            .positiveText("YES")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    // TODO
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);

                                }
                            }).negativeText("NO").onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    md.dismiss();
                                }
                            }).show();
                    md.setCanceledOnTouchOutside(false);

                }
            }

        }


        Log.v("Error latitude", "" + sourcelatitude);
        Log.v("Error longitude", "" + sourcelongitude);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Introduction.this);
        SharedPreferences.Editor ed = sharedPrefs.edit();
        ed.putFloat("sourcelatitude", sourcelatitude);
        ed.putFloat("sourcelongitude", sourcelongitude);
        ed.commit();

        if (roleid == 1) {
            hideAuthorityMenuItem();
            sessionid = sp.getInt("studentsessionid", -1);
            //Toast.makeText(this,"In the student role",Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, "sdas", Toast.LENGTH_SHORT).show();
           // FirebaseMessaging.getInstance().subscribeToTopic("authority");

        } else if (roleid == 2) {
            hideStudentMenuItem();
            //Toast.makeText(this, "sdasaa", Toast.LENGTH_SHORT).show();
            sessionid = sp.getInt("authoritysessionid", -1);
            FirebaseMessaging.getInstance().subscribeToTopic("authority");


        }



        // hideItem();
        View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.pallavi.norag.Login");
                startActivity(intent);
            }
        });
        OnClickButtonListener();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // login_signup1=(Button)findViewById(R.id.login);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void hideStudentMenuItem() {
        navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView1.getMenu();
        nav_Menu.findItem(R.id.shake).setVisible(false);
        nav_Menu.findItem(R.id.feedback).setVisible(false);

    }

    private void hideAuthorityMenuItem() {
        navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView1.getMenu();
        //nav_Menu.findItem(R.id.committee_members).setVisible(false);
        nav_Menu.findItem(R.id.feedback).setVisible(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(mprovider, 1500000, 1, this);
                location = locationManager.getLastKnownLocation(mprovider);

                Log.v("Error Location", "" + location);


                if (location != null) {
                    onLocationChanged(location);
                    sourcelatitude = Float.parseFloat(String.valueOf(location.getLatitude()));
                    sourcelongitude = Float.parseFloat(String.valueOf(location.getLongitude()));

                } else if (location == null)
                // Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                {
                    Log.v("Error ", "No location Found");
                    new MaterialDialog.Builder(this)
                            .title("Turn On GPS Location")
                            .content("Turn on GPS location press Yes")
                            .positiveText("YES")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    // TODO
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);

                                }
                            }).show();

                }

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //For Token Refresh of Firebase
    /*
    @Override
public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    sendRegistrationToServer(refreshedToken);
}
     */

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.introduction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.committee_members) {
           // Intent memberpage = new Intent(Introduction.this, members.class);
            //startActivity(memberpage);
            Fragment fragment=new members();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // Handle the camera action
        } else if (id == R.id.feedback) {

        } else if (id == R.id.home) {
            Fragment fragment=new Home();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.complaints) {
           // Intent complainpage = new Intent(Introduction.this, complain.class);
           // startActivity(complainpage);
            Fragment fragment=new complain();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.shake) {
            //Intent studentpage = new Intent(Introduction.this, Student.class);
            //startActivity(studentpage);
            Fragment fragment=new Student();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.settings) {
            //Intent aboutpage = new Intent(Introduction.this, About.class);
            //startActivity(aboutpage);
            Fragment fragment=new About();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.notifications) {
           // Intent notificationpage = new Intent(Introduction.this, notification.class);
            //startActivity(notificationpage);
            Fragment fragment=new notification();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.introduction, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id==R.id.logout){
            if (roleid==2)
            {
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(Introduction.this);
                SharedPreferences.Editor ed=sp.edit();
                ed.remove("authoritysessionid");
                ed.commit();
                Intent loginpage=new Intent(Introduction.this,AuthorityLogin.class);
                startActivity(loginpage);
                Introduction.this.finish();
            }
            else if (roleid==1)
            {

                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(Introduction.this);
                SharedPreferences.Editor ed=sp.edit();
                ed.remove("studentsessionid");
                ed.commit();
                Intent loginpage=new Intent(Introduction.this,Login.class);
                startActivity(loginpage);
                Introduction.this.finish();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnClickButtonListener() {
/*     login_signup1.setOnClickListener(new View.OnClickListener(){
        public void  onClick(View v){
            Intent intent=new Intent("com.example.pallavi.norag.Login");
            startActivity(intent);
    }}

    );*/
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
