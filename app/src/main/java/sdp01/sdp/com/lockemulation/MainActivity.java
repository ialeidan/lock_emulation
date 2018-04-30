package sdp01.sdp.com.lockemulation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private MapView mapView;
    private GoogleMap googleMap;


    ScheduledExecutorService scheduler;
    Handler handler;
    Thread thread;

    private ImageView bigImg;
    private ImageView statusImg;
    private ImageView sendingImg;

    private EditText deviceET;
    private Button setBtn;

    private Button lockBtn;
    private Button unlockBtn;


    private String device = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bigImg = (ImageView) findViewById(R.id.imageView);
        statusImg = (ImageView) findViewById(R.id.imageView2);
        sendingImg = (ImageView) findViewById(R.id.imageView3);
        deviceET = (EditText) findViewById(R.id.editText);
        setBtn = (Button) findViewById(R.id.button3);
        lockBtn = (Button) findViewById(R.id.button);
        unlockBtn = (Button) findViewById(R.id.button2);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment



        this.unlockBtn.setClickable(false);
        this.statusImg.setImageResource(R.drawable.ic_grey_status);
        this.sendingImg.setImageResource(R.drawable.ic_grey_status);


        String deviceID = AuthInfo.getDevice();
        boolean isService = AuthInfo.getService();

        if (isService) {
            deviceET.setText(deviceID);
            this.device = deviceID;
            this.lockBtn.setClickable(false);
            this.unlockBtn.setClickable(true);
            this.statusImg.setImageResource(R.drawable.ic_green_status);
            startSendingLocation();
        }else{
            deviceET.setText(deviceID);
            AuthInfo.setService(false);
            this.device = deviceID;
        }

    }


    public void setDevice(View view) {
        this.device = deviceET.getText().toString();
        AuthInfo.setDevice(this.device);
    }

    public void lock(View view) {
        if (this.device.isEmpty()){
            Log.e("ERROR", "Must put device ID");
            return;
        }
        Log.e("lock", "hi");
        this.lockBtn.setClickable(false);

        Networking.sendLock("locked", new DataSourceRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String request = response.getString("request");
                    if (request.equals("error")){
                        lockBtn.setClickable(true);
                        Log.e("ERROR", "Must put device ID");
                        return;
                    }

                    Log.e("SUCCESS", "GOOD");
                    statusImg.setImageResource(R.drawable.ic_green_status);
                    unlockBtn.setClickable(true);

                    AuthInfo.setService(true);

                    startSendingLocation();

                } catch (JSONException e) {
                    // Error handling.
                    e.printStackTrace();
                    Log.e("lock", "err");
                }

            }

            @Override
            public void onError(ErrorCode anError) {
                lockBtn.setClickable(true);
                Log.e("ERROR", "ERROR ///" + anError.comment);
            }
        });

    }

    public void startSendingLocation() {
//        handler = new Handler();
//
//        thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while(true) {
//                        // send mLastKnownLocation
//
//                        sendingImg.setImageResource(R.drawable.ic_green_status);
//                        Networking.sendLocation("locked", mLastKnownLocation, new DataSourceRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.e("LOCATION", "SENT /// " + response.toString());
//                                sendingImg.setImageResource(R.drawable.ic_grey_status);
//                            }
//
//                            @Override
//                            public void onError(ErrorCode anError) {
//                                Log.e("LOCATION", "ERROR /// " + anError.info);
//                            }
//                        });
//
//
//                        sleep(1000);
//                        handler.post(this);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread.start();

        scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call service
                        sendingImg.setImageResource(R.drawable.ic_green_status);
                        Networking.sendLocation("locked", mLastKnownLocation, new DataSourceRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("LOCATION", "SENT /// " + response.toString());
                                sendingImg.setImageResource(R.drawable.ic_grey_status);
                            }

                            @Override
                            public void onError(ErrorCode anError) {
                                Log.e("LOCATION", "ERROR /// " + anError.comment);
                            }
                        });
                    }
                }, 0, 5, TimeUnit.SECONDS);
    }


    public void unlock(View view) {
        if (this.device.isEmpty()){
            Log.e("ERROR", "Must put device ID");
            return;
        }

        this.unlockBtn.setClickable(false);

        Networking.endService("unlocked", new DataSourceRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String request = response.getString("request");
                    if (request.equals("error")){
                        unlockBtn.setClickable(true);
                        Log.e("ERROR", "Must put device ID");
                        return;
                    }

//                    thread.stop();
                    scheduler.shutdown();


                    statusImg.setImageResource(R.drawable.ic_grey_status);
                    lockBtn.setClickable(true);
                    AuthInfo.setService(false);

                } catch (JSONException e) {
                    // Error handling.
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ErrorCode anError) {
                unlockBtn.setClickable(true);
                Log.e("ERROR", "Must put device ID");
            }
        });

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            // Return null here, so that getInfoContents() is called next.
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                // Inflate the layouts for the info window, title and snippet.
//                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
//                        (FrameLayout) findViewById(R.id.map), false);
//
//                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
//                title.setText(marker.getTitle());
//
//                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
//                snippet.setText(marker.getSnippet());
//
//                return infoWindow;
//            }
//        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
//        getService();
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Log.e("MAP", task.getResult().toString());
                            mLastKnownLocation = task.getResult();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("Maps", "Current location is null. Using defaults.");
                            Log.e("Maps", "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    // Location Permission:
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
