package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.uottawa.benjaminmacdonald.quicktings.Classes.OrdersCart;
import com.uottawa.benjaminmacdonald.quicktings.Manifest;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by BenjaminMacDonald on 2017-07-12.
 */

public class DeliveryFragment extends Fragment implements Step, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private static final int RESULT_OK = -1;
    private MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public DeliveryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_delivery, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mMapView.getMapAsync(this);



        Button searchButton = (Button) rootView.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .setCountry("CA")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(typeFilter)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (Build.VERSION.SDK_INT > 22 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MAPS_RECEIVE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.MAPS_RECEIVE}, 123);
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(current).title("Current Location"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                LatLng latLng = place.getLatLng();
                map.clear();
                Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(place.getAddress().toString()));
                marker.showInfoWindow();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                OrdersCart.getInstance().getCurrentOrder().setLocation(latLng);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                String bad = status.getStatusMessage();
                System.out.println(bad);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                String bad = status.getStatusMessage();
                System.out.println(bad);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public VerificationError verifyStep() {
        if (!OrdersCart.getInstance().getCurrentOrder().hasLocation()) {
            Toast.makeText(getContext(), "You must enter a location", Toast.LENGTH_SHORT).show();
            return new VerificationError("You must enter a location");
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
