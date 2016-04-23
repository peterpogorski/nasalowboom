package waterloo.spaceapps.com.lowboom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.FloatMath;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mButton = (Button) findViewById(R.id.sonic_boom);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoomLocation();
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
        double d_1 = 1;
        double d_2 = 1; // what distance away (Radius)
        double inaudible = 10;
        double decibel_1 = 150;
        //double decibel_2 = Math.pow((d_1/d_2), 2)* decibel_1; // what the decibel at d_2 is
        double inaudible_distance = d_1 / Math.sqrt(inaudible/decibel_1);

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


    private void centerMapOnMyLocation() {

        mMap.setMyLocationEnabled(true);

        mLatLng = new LatLng(43.451096, -80.498792);
        mMap.addMarker(new MarkerOptions().position(mLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));

    }
}
