package com.sidiq.nbs.test.sampleapppermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static String[] PERMISSIONS_LOCATIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationUtils locationUtils;
    private LocationUtils.LocationResult locationResult;

    private TextView txtUserLocation;

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUserLocation = (TextView)findViewById(R.id.txt_user_location);
        locationUtils = new LocationUtils(MainActivity.this);

        if (PermissionUtil.isMNC()){
            accessLocation();
        }else{
            getUserLocation();
        }
    }

    private void getUserLocation(){
        txtUserLocation.setText(getString(R.string.text_location));
        locationResult = new LocationUtils.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location != null){
                    txtUserLocation.setText("User Location : "+location.getLatitude()+","+location.getLongitude());
                }
            }
        };
        locationUtils.getLocation(MainActivity.this, locationResult);
    }

    private void accessLocation(){
        txtUserLocation.setText(getString(R.string.text_permission));
        if (PermissionUtil.hasSelfPermission(MainActivity.this, PERMISSIONS_LOCATIONS)){
            getUserLocation();
        }else{
            requestPermissions(PERMISSIONS_LOCATIONS, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getUserLocation();
            }else{
                txtUserLocation.setText(getString(R.string.text_nopermission));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
