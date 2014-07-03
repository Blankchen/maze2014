package com.example.maze2014;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends Fragment{
	public static View view;
	private static GoogleMap mMap;
	private static Double latitude, longitude;
	private static String tittle, context;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		if(menu != null){
			menu.findItem(R.id.action_refresh).setVisible(false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    if (container == null) {
	        return null;
	    }  
	 
	   // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = getArguments().getDouble("x");
        longitude = getArguments().getDouble("y");
        tittle = getArguments().getString("tittle");
        context = getArguments().getString("context");
        
        view = inflater.inflate(R.layout.fragment_map, container, false);

        setUpMapIfNeeded(); // For setting up the MapFragment
        
	    return view;     
       
	}

	/***** Sets up the map if it is possible to do so *****/
	public static void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	        mMap = ((SupportMapFragment) MainActivity.fragmentManager
	        		.findFragmentById(R.id.location_map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap();
	    }
	}

	private static void setUpMap() {
	    // For showing a move to my loction button
	    mMap.setMyLocationEnabled(true);
	    // For dropping a marker at a point on the Map
	    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(tittle).snippet(context));
	    // For zooming automatically to the Dropped PIN Location
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
	            longitude), 17.0f));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    if (mMap != null)
	        setUpMap();

	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	        mMap = ((SupportMapFragment) MainActivity.fragmentManager
	                .findFragmentById(R.id.location_map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap();
	    }
	}

	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (mMap != null) {
	        MainActivity.fragmentManager.beginTransaction()
	            .remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
	        mMap = null;
	    }
	}

}
