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
    private Button mButtonSonic;
    private Button mButtonLow;
    private Button mButtonClear;

    private ImageView mJet;
    private int mButtonHeight;
    private MediaPlayer mMediaPlayer;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double decibel_1;
    private int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mButtonSonic = (Button) findViewById(R.id.sonic_boom);
        mButtonClear = (Button) findViewById(R.id.clear);
        mButtonLow = (Button) findViewById(R.id.low_boom);

        mJet = (ImageView) findViewById(R.id.jet);

        mButtonSonic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decibel_1 = 150;
                mColor = getResources().getColor(R.color.colorPrimary);
                mJet.setImageDrawable(getResources().getDrawable(R.drawable.fighter_jet));
                animatePlane();
            }
        });

        mButtonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decibel_1 = 140;
                mColor = getResources().getColor(R.color.color1);
                mJet.setImageDrawable(getResources().getDrawable(R.drawable.low_jet));
                animatePlane();
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMap();
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
        double inaudible = 90;
        //double decibel_2 = Math.pow((d_1/d_2), 2)* decibel_1; // what the decibel at d_2 is

        double inaudible_distance = d_1/(Math.pow(10,(inaudible-decibel_1)/20));

        if(mMap != null) {
            int d = (int)(inaudible_distance); // diameter
            int radiusM = 200;

            Bitmap bm4 = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c4 = new Canvas(bm4);
            Paint p4 = new Paint();
            p4.setColor(mColor);
            c4.drawCircle(d / 2, d / 2, d / 2, p4);


            // generate BitmapDescriptor from circle Bitmap
            BitmapDescriptor bmD4 = BitmapDescriptorFactory.fromBitmap(bm4);

            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD4).
                    position(mLatLng, (float)inaudible_distance, (float)inaudible_distance).
                    transparency(0.4f));

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mButtonHeight = mButtonSonic.getHeight();
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

    private void clearMap(){
        if(mMap != null){
            mMap.clear();
        }
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
