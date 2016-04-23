package waterloo.spaceapps.com.lowboom;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LatLng mLatLng;
    private Button mButton;
    private ImageView mJet;

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        centerMapOnMyLocation();
    }

    private void drawBoomLocation(){
        if(mMap != null) {
            int d = 500; // diameter
            Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.colorPrimary));
            c.drawCircle(d / 2, d / 2, d / 2, p);

            Bitmap bm2 = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c2 = new Canvas(bm2);
            Paint p2 = new Paint();
            p.setColor(getResources().getColor(R.color.colorAccent));
            c2.drawCircle(d / 2, d / 2, d / 2, p2);
            int radiusM = 1000;

            // generate BitmapDescriptor from circle Bitmap
            BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
            BitmapDescriptor bmD2 = BitmapDescriptorFactory.fromBitmap(bm2);

            // mapView is the GoogleMap
            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD).
                    position(mLatLng, radiusM * 2, radiusM * 2).
                    transparency(0.4f));

            mMap.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD2).
                    position(mLatLng, (radiusM + 500) * 2, (radiusM + 500) * 2).
                    transparency(0.4f));

        }
    }

    private void animatePlane(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        Animation animation = new TranslateAnimation(0.0f, width, (height/2), (height/2));
        animation.setDuration(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mJet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mJet.setVisibility(View.GONE);
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


    private void centerMapOnMyLocation() {

        mMap.setMyLocationEnabled(true);

        mLatLng = new LatLng(43.451096, -80.498792);
        mMap.addMarker(new MarkerOptions().position(mLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));

    }
}
