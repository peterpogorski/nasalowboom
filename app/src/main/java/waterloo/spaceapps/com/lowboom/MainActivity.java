package waterloo.spaceapps.com.lowboom;

import android.animation.Animator;
import android.content.ComponentCallbacks;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LatLng mLatLng;
    private Button mButton;
    private ImageView mJet;
    private int mButtonHeight;
    private MediaPlayer mMediaPlayer;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mButton = (Button) findViewById(R.id.sonic_boom);
        mJet = (ImageView) findViewById(R.id.jet);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatePlane();
            }
        });

        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        //centerMapOnMyLocation();
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            centerMapOnMyLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    private void drawBoomLocation(){
        double d_1 = 1;
        //double d_2 = 1; // what distance away (Radius)
        double inaudible = 10;
        double decibel_1 = 150;
        //double decibel_2 = Math.pow((d_1/d_2), 2)* decibel_1; // what the decibel at d_2 is

        double inaudible_distance = d_1/(Math.pow(10,(inaudible-decibel_1)/20));

        if(mMap != null) {
            int d = (int)inaudible_distance; // diameter
            int radiusM = 200;
            Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.color1));
            c.drawCircle(d / 2, d / 2, d / 2, p);

            Bitmap bm2 = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c2 = new Canvas(bm2);
            Paint p2 = new Paint();
            p2.setColor(getResources().getColor(R.color.color2));
            c2.drawCircle(d / 2, d / 2, d / 2, p2);

            Bitmap bm3 = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c3 = new Canvas(bm3);
            Paint p3 = new Paint();
            p3.setColor(getResources().getColor(R.color.color3));
            c3.drawCircle(d / 2, d / 2, d / 2, p3);

            Bitmap bm4 = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c4 = new Canvas(bm4);
            Paint p4 = new Paint();
            p4.setColor(getResources().getColor(R.color.color4));
            c4.drawCircle(d / 2, d / 2, d / 2, p4);


            // generate BitmapDescriptor from circle Bitmap
            BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
            BitmapDescriptor bmD2 = BitmapDescriptorFactory.fromBitmap(bm2);
            BitmapDescriptor bmD3 = BitmapDescriptorFactory.fromBitmap(bm3);
            BitmapDescriptor bmD4 = BitmapDescriptorFactory.fromBitmap(bm4);

            // mapView is the GoogleMap
            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD).
                    position(mLatLng, (float)inaudible_distance * (1/4), (float)inaudible_distance * (1/4)).
                    transparency(0.4f));

            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD2).
                    position(mLatLng, (float)inaudible_distance * (2/4), (float)inaudible_distance * (2/4)).
                    transparency(0.4f));
            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD3).
                    position(mLatLng, (float)inaudible_distance * (3/4), (float)inaudible_distance * (3/4)).
                    transparency(0.4f));
            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD4).
                    position(mLatLng, (float)inaudible_distance, (float)inaudible_distance).
                    transparency(0.4f));

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mButtonHeight = mButton.getHeight();
    }


    private void animatePlane(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels - (mButtonHeight);
        int width = displaymetrics.widthPixels;

        Animation animation = new TranslateAnimation(0.0f, width, (height/2), (height/2));
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.d1);
                mMediaPlayer.start();
                mJet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mJet.setVisibility(View.GONE);
                mMediaPlayer.stop();
                drawBoomLocation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mJet.startAnimation(animation);
    }

    private double calculateRadius(){
        float d_1 = 1;
        float d_2 = 1; // what distance away (Radius)
        float decibel_1 = 150;
        double decibel_2 = Math.pow((d_1/d_2), 2)*decibel_1; // what the decibel at d_2 is
        return decibel_2;
    }


    private void centerMapOnMyLocation(double userLatitude ,double userLongitude) {


        mLatLng = new LatLng(userLatitude, userLongitude);
        mMap.addMarker(new MarkerOptions().position(mLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
