package com.reformation.home.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reformation.home.R;

import java.util.ArrayList;
import java.util.HashMap;

import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Exhibitor;
import model.GateModel;
import model.MapModel;
import model.MapResponse;
import model.MapResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.Utils;

/**
 * Created by Alok on 26-03-2017.
 */
public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener{
    MapView mMapView;
    private GoogleMap googleMap;
    View bottomSheet;
    ApiInterface mApiInterface;
    private CustomProgresDialog dlg;
    ArrayList<Exhibitor> exhibitors;
    private CoordinatorLayout rlMapLayout;
    HashMap<Marker,Exhibitor> hashMapMarker = new HashMap<Marker,Exhibitor>();
    private ArrayList<LatLng>listLatLng;
    BottomSheetBehavior behavior;
    Exhibitor exhibitorData;
    View rootView;
    TextView title,desc;
    Button more;
    Marker clickMarker;
    public MapFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        title = (TextView) rootView.findViewById(R.id.title);
        desc = (TextView) rootView.findViewById(R.id.titleDesc);
        more = (Button) rootView.findViewById(R.id.buttonMore);
        rlMapLayout = (CoordinatorLayout) rootView.findViewById(R.id.rlMapLayout);
        mMapView.onCreate(savedInstanceState);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        dlg = CustomProgresDialog.getInstance(getActivity());
        bottomSheet = rootView.findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        if(exhibitorData!=null){
                            title.setText(exhibitorData.getPlaceName());
                            desc.setText(exhibitorData.getDescp());
                        }
                        if(googleMap!=null){
                            googleMap.setOnMarkerClickListener(null);
                        }

                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        if(googleMap!=null){
                            googleMap.setOnMarkerClickListener(MapFragment.this);
                        }
                        if(clickMarker!=null){
                            markerAnimationInCollapseBottomSheet(clickMarker);
                        }
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMapList();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMarkerClickListener(MapFragment.this);
            }
        });



        return rootView;
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


    private void getMapList() {

        dlg.showDialog();
        Call<MapResponse> call = mApiInterface.getMapDetail(Constant.SELECTED_LANG);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    if(response.body()!=null) {
                        MapResponse model = response.body();
                        if(model!=null&& model.getStatus()){
                            MapModel dataMapModel = model.getResponseData();
                            if(dataMapModel!=null)
                                loadMap(dataMapModel);
                            else
                                Utils.showToast(getActivity(),getResources().getString(R.string.no_record_found));
                        }else{
                            Utils.showToast(getActivity(),getResources().getString(R.string.no_record_found));
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {
               // Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadMap(MapModel model) {
        exhibitors = model.getPlaces();
        ArrayList<GateModel> gateModels = model.getGates();
        addPlaceMarker(exhibitors,googleMap);

    }


    private void addPlaceMarker(ArrayList<Exhibitor> dataList,GoogleMap googleMap) {

        if (googleMap != null) {
            googleMap.clear();
            // For showing a move to my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            if (dataList != null && dataList.size() > 0) {
                listLatLng = new ArrayList<LatLng>();
                for (int i = 0; i < dataList.size(); i++) {
                    try {

                        Exhibitor model = dataList.get(i);
                        if (model.getLatitude() != null && !model.getLatitude().equals("null") && !model.getLatitude().equals("") &&
                                model.getLongitude() != null && !model.getLongitude().equals("null") && !model.getLongitude().equals("")) {

                        }
                        LatLng location = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(model.getPlaceName()).snippet(model.getDescp())
                                .icon(getMarkerIcon(model.getCategory())));
                        hashMapMarker.put(marker, model);
                        listLatLng.add(location);

                        // For zooming automatically to the location of the marker
                        // CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                        // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                SetZoomlevel(listLatLng);
            } else {
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else
        {
            Utils.showToast(getActivity(),getResources().getString(R.string.unable_to_map_create));
        }
    }

    private BitmapDescriptor getMarkerIcon(String category) {
        int icon = R.drawable.exhibitors_point_small;
        // if(category.equals("")){}
        return BitmapDescriptorFactory.fromResource(R.drawable.exhibitors_point_small);
    }


    public void  SetZoomlevel(ArrayList<LatLng> listLatLng)
    {
        if (listLatLng != null && listLatLng.size() == 1)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 10));
        }
        else if (listLatLng != null && listLatLng.size() > 1)
        {
            final LatLngBounds.Builder builder = LatLngBounds.builder();
            for (int i = 0; i < listLatLng.size(); i++)
            {
                builder.include(listLatLng.get(i));
            }

            final ViewTreeObserver treeObserver = rlMapLayout.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout()
                {
                    if(googleMap != null){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), rootView.findViewById(R.id.mapView)
                                .getWidth(), rootView.findViewById(R.id.mapView).getHeight(), 80));
                        rlMapLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });

        }
    }



    @Override
    public boolean onMarkerClick(final Marker marker) {

        clickMarker = marker;
        //Make the marker bounce
        final Handler handler = new Handler();

        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;

        Projection proj = googleMap.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, -50);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();
        exhibitorData = hashMapMarker.get(marker);

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.exhibitors_point));

                if (t < 1.0) {
                    // Post again 16ms later.
                     handler.postDelayed(this, 16);
                }


            }
        });

        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }


        //return false; //have not consumed the event
        return true; //have consumed the event
    }



    private void markerAnimationInCollapseBottomSheet(final Marker marker){
        final Handler handler = new Handler();

        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;

        Projection proj = googleMap.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, -50);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();
        exhibitorData = hashMapMarker.get(marker);

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.exhibitors_point_small));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }


            }
        });


    }

}
