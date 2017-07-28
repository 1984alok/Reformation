package com.reformation.home.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reformation.home.ExhibitorDetailActivity;
import com.reformation.home.R;
import com.reformation.home.ReformationApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import adapter.PlaceCatAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Exhibitor;
import model.GateModel;
import model.MapModel;
import model.MapResponse;
import model.EventPlaceCatg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
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
    ArrayList<Exhibitor> filteredexhibitors = new ArrayList<>();
    Set<String> filterbaleCatlist = new HashSet<>();
    private CoordinatorLayout rlMapLayout;
    HashMap<Marker,Exhibitor> hashMapMarker = new HashMap<Marker,Exhibitor>();
    private ArrayList<LatLng>listLatLng;
    BottomSheetBehavior behavior;
    Exhibitor exhibitorData;
    View rootView;
    TextView title,desc,titleDist,selectALL;
    Button more;
    Marker clickMarker;
    ReformationApplication reformationApplication ;
    ArrayList<EventPlaceCatg.ResponseDatum> catList;

    RecyclerView catRecyclerView;
    Button buttonTakeOver;
    LinearLayoutManager  linearLayoutManager;
    PlaceCatAdapter placeCatAdapter;
    ImageView fillter,fillterClose,moveToMylocation;
    View popUpViiew;
    public MapFragment(){}
    AlertDialog dilg;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        title = (TextView) rootView.findViewById(R.id.title);
        titleDist= (TextView) rootView.findViewById(R.id.titleDist);
        desc = (TextView) rootView.findViewById(R.id.titleDesc);
        more = (Button) rootView.findViewById(R.id.buttonMore);
        rlMapLayout = (CoordinatorLayout) rootView.findViewById(R.id.rlMapLayout);
        mMapView.onCreate(savedInstanceState);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        dlg = CustomProgresDialog.getInstance(getActivity());
        bottomSheet = rootView.findViewById(R.id.design_bottom_sheet);
        reformationApplication = (ReformationApplication) getActivity().getApplicationContext();
        catList = reformationApplication.getPalceCatList();
        fillter = (ImageView) rootView.findViewById(R.id.fillter);
        moveToMylocation= (ImageView) rootView.findViewById(R.id.direction);

        fillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadCatPopup();

            }
        });


        moveToMylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( Constant.appLoc!=null) {
                   LatLng sydney = new LatLng(Constant.appLoc.getLatitude(), Constant.appLoc.getLongitude());
                   googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                   // For zooming automatically to the location of the marker
                   CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                   googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

               }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(exhibitorData!=null)
                    startActivity(new Intent(getActivity(), ExhibitorDetailActivity.class).putExtra("DATA",exhibitorData));
            }
        });
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
                            Location loc= new Location("GPS");
                            loc.setLatitude(exhibitorData.getLatitude()!=null?Double.parseDouble(exhibitorData.getLatitude()):0.0);
                            loc.setLongitude(exhibitorData.getLongitude()!=null?Double.parseDouble(exhibitorData.getLongitude()):0.0);
                            titleDist.setText(Utils.getDistance(Constant.appLoc,loc)+" km");
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
                            clickMarker.hideInfoWindow();
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
        if(catList==null||catList.size()==0){
            getPlaceCatgListFromServer();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMarkerClickListener(MapFragment.this);
            }
        });



        return rootView;
    }

    private void loadCatPopup() {
        linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        popUpViiew =  LayoutInflater.from(getActivity()).inflate(R.layout.filter,null);
        catRecyclerView = (RecyclerView)popUpViiew.findViewById(R.id.ll);
        fillterClose = (ImageView) popUpViiew.findViewById(R.id.buttonClose);
        selectALL = (TextView) popUpViiew.findViewById(R.id.textViewSelectAll);
        buttonTakeOver = (Button) popUpViiew.findViewById(R.id.buttontakeover);
        catRecyclerView.setLayoutManager(linearLayoutManager);
        catList = reformationApplication.getPalceCatList();
        if(catList!=null && catList.size()>0){
            placeCatAdapter = new PlaceCatAdapter(getActivity(),catList);
            catRecyclerView.setAdapter(placeCatAdapter);
            placeCatAdapter.setOnItemClickListener(fillterItemClickListner);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(popUpViiew);
        dilg =  builder.create();
        dilg.show();

        fillterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilg.dismiss();
            }
        });

        selectALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllCatg();
            }
        });

        buttonTakeOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFillterTakeOverButton();
                dilg.dismiss();
            }
        });



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




    private void getPlaceCatgListFromServer() {

        dlg.showDialog();
        Call<EventPlaceCatg> call = mApiInterface.getPlaceCatg(Constant.SELECTED_LANG);
        call.enqueue(new Callback<EventPlaceCatg>() {
            @Override
            public void onResponse(Call<EventPlaceCatg> call, Response<EventPlaceCatg> response) {

                if (response.isSuccessful()) {
                    if(response.body()!=null) {
                        EventPlaceCatg model = response.body();
                        if(model!=null&& model.getStatus()){
                            ArrayList< EventPlaceCatg.ResponseDatum> dataModel = model.getResponseData();
                            if(dataModel!=null&&dataModel.size()>0) {
                                for (int i = 0; i < dataModel.size(); i++) {
                                    dataModel.get(i).setChecked(true);
                                    filterbaleCatlist.add(dataModel.get(i).getTitle());
                                }
                                reformationApplication.setPalceCatList(dataModel);
                            }
                            else
                                Utils.showToast(getActivity(),getResources().getString(R.string.no_record_found));
                        }else{
                            Utils.showToast(getActivity(),getResources().getString(R.string.no_record_found));
                        }
                    }

                }
                dlg.hideDialog();

            }

            @Override
            public void onFailure(Call<EventPlaceCatg> call, Throwable t) {
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
            //   googleMap.setMyLocationEnabled(true);
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
                                .icon(Utils.getMarkerIcon(model.getCategory().trim())));
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
                // marker.setIcon(getMarkerIcon());

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }


            }
        });

        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            clickMarker.showInfoWindow();
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clickMarker.hideInfoWindow();
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
                //   marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.exhibitors_point_small));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }


            }
        });


    }


    PlaceCatAdapter.OnItemClickListener fillterItemClickListner = new PlaceCatAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View clickView, View view, int position) {
            if(catList.get(position).isChecked())
                deSelect(catList.get(position));
            else {
                catList.get(position).setChecked(true);
                filterbaleCatlist.add(catList.get(position).getTitle());
            }
            placeCatAdapter.notifyDataSetChanged();

        }
    };


    public void selectAllCatg(){
        if (catList!=null&catList.size()>0){
            selectALL.setText(getResources().getString(R.string.all_selected));
            for (int i = 0; i < catList.size(); i++) {
                if(!catList.get(i).isChecked())
                    catList.get(i).setChecked(true);
                filterbaleCatlist.add(catList.get(i).getTitle());

            }
            placeCatAdapter.notifyDataSetChanged();
        }
    }

    public void deSelect(EventPlaceCatg.ResponseDatum responseDatum){
        selectALL.setText(getResources().getString(R.string.selectall));
        responseDatum.setChecked(false);
        filterbaleCatlist.remove(responseDatum.getTitle());
    }


    public void getFillteredExibitors(String category){
        LogUtil.createLog("filtered category ::",category);

        for (int i = 0; i < exhibitors.size(); i++) {
        if(category.equalsIgnoreCase("Church")||category.equalsIgnoreCase("Kirche")) {
            doFilter(i,"Church","Kirche");
        }else if(category.equalsIgnoreCase("Museum")||category.equalsIgnoreCase("Museum")) {
            doFilter(i,"Museum","Museum");
        }else if(category.equalsIgnoreCase("Exhibitor")||category.equalsIgnoreCase("Aussteller")) {
            doFilter(i,"Exhibitor","Aussteller");
        }else if(category.equalsIgnoreCase("Info-Point")||category.equalsIgnoreCase("Info point")) {
            doFilter(i,"Info-Point","Info point");
        }else if(category.equalsIgnoreCase("Bathroom")||category.equalsIgnoreCase("WC")) {
            doFilter(i,"Bathroom","WC");
        }else if(category.equalsIgnoreCase("First aid")||category.equalsIgnoreCase("Erste Hilfe")) {
            doFilter(i,"First aid","Erste Hilfe");
        }else if(category.equalsIgnoreCase("Parking space")||category.equalsIgnoreCase("Parkplatz")) {
            doFilter(i,"Parking space","Parkplatz");
        }else if(category.equalsIgnoreCase("Other event space")||category.equalsIgnoreCase("Sonstiger Veranstaltungsort")) {
            doFilter(i,"Other event space","Sonstiger Veranstaltungsort");
        }else if(category.equalsIgnoreCase("Reformation lunch")||category.equalsIgnoreCase("Reformationsteller")) {
            doFilter(i,"Reformation lunch","Reformationsteller");
        }else if(category.equalsIgnoreCase("Installation")||category.equalsIgnoreCase("Installation")) {
            doFilter(i,"Installation","Installation");
        }else if(category.equalsIgnoreCase("Place for children")||category.equalsIgnoreCase("Ort für Kinder")) {
            doFilter(i,"Place for children","Ort für Kinder");
        }else if(category.equalsIgnoreCase("Shop")||category.equalsIgnoreCase("Shop")) {
            doFilter(i,"Shop","Shop");
        }else{
            doFilter(i,category,category);
        }



        }

        if(filteredexhibitors.size()>0){
            addPlaceMarker(filteredexhibitors,googleMap);
        }else{
            Utils.showToast(getActivity(),getString(R.string.no_record_found));
           // addPlaceMarker(exhibitors,googleMap);
        }

    }

    private void doFilter(int i,String catE,String catG){
        if(exhibitors.get(i).getCategory().toLowerCase().indexOf(catE.toLowerCase().trim())!=-1  ||
                exhibitors.get(i).getCategory().toLowerCase().indexOf(catG.toLowerCase().trim())!=-1){
            LogUtil.createLog("filtered string exhibitors.get(i).getCategory().toLowerCase()::",exhibitors.get(i).getCategory().toLowerCase());
            filteredexhibitors.add(exhibitors.get(i));
        }
    }




    public void handleFillterTakeOverButton(){

        if(filterbaleCatlist.size()>0) {
            String fString = "";
            filteredexhibitors.clear();
            for (String s : filterbaleCatlist) {
            /*if(!fString.equals(""))
            fString = fString+","+s;
            else
                fString=s;*/
                getFillteredExibitors(s.trim());
            }
        }else{
            addPlaceMarker(exhibitors,googleMap);
        }



    }

}
