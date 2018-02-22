package com.afpsoft.mymap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.clear();
                LatLng userlocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userlocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,10));

                System.out.println("ID: 1 / OnMapReady Kodu Çalıştı");

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());



                try {

                    List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList!= null && addressList.size()>0) {
                        System.out.println("address bilgisi:" + addressList.get(0).toString());
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }



                System.out.println("Aktif Konum: " + location.toString());


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        mMap.setOnMapLongClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastlocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            LatLng userLastlocation = new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLastlocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastlocation,15));
            System.out.println("ID: 2 / setOnMapLongClickListener Kodu Çalıştı");


        }
        // Add a marker in Sydney and move the camera
        //LatLng buyaka = new LatLng(41.0269233,29.1248478);
        //mMap.addMarker(new MarkerOptions().position(buyaka).title("Buyaka"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buyaka,15));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if (grantResults.length > 0) {

           if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

               locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

           }

       }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        String address= "";

        mMap.clear();

        try {
            List<Address> addressList=  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if (addressList !=null && addressList.size()>0) {
                if (addressList.get(0).getThoroughfare()!=null) {

                    address += addressList.get(0).getThoroughfare();

                    if (addressList.get(0).getSubThoroughfare()!= null) {

                        address += addressList.get(0).getSubThoroughfare();

                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address==""){

            address="No Address!!";
        }

        System.out.println(" Long Click Adres Bilgisi : " + address);

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

    }
}
