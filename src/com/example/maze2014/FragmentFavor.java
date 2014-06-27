package com.example.maze2014;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentFavor extends Fragment{
	 ListView userList;
	 UserCustomAdapter userAdapter;
	 ArrayList<User> userArray = new ArrayList<User>();
	 static String[] Datalist; 
	 static int[] listCostCh;
	 static int[] DataCount; 
	 static String FavorName;
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
		 if(getArguments().getString("FavorName") != null){
				Datalist = getArguments().getStringArray("DataChecked"); 
				listCostCh = getArguments().getIntArray("Costed");
				DataCount = getArguments().getIntArray("DataCount");	
				FavorName = getArguments().getString("FavorName");
				String context = "";
				int total = 0;
				for(int i=0; i<Datalist.length; i++){
					context += Datalist[i] + ", " + DataCount[i] + " 份, 共" + listCostCh[i]*DataCount[i] + " 元\n";
					total += listCostCh[i]*DataCount[i];		
				}	
				userArray.add(new User(FavorName, context, "總計： "+total+" 元"));
		 }
	 /**
	   * add item in arraylist
	   */
	/*userArray.add(new User("Mumer", "Spain", "Spain"));
	  userArray.add(new User("Jon", "EW", "USA"));
	  userArray.add(new User("Broom", "Span", "SWA"));
	  userArray.add(new User("Lee", "Aus", "AUS"));
	  userArray.add(new User("Jon", "EW", "USA"));
	  userArray.add(new User("Broom", "Span", "SWA"));
	  userArray.add(new User("Lee", "Aus", "AUS"));*/
	  /**
	   * set item into adapter
	   */
	  userAdapter = new UserCustomAdapter(getActivity(), R.layout.row_listview_favor, userArray);
	  View rootView = inflater.inflate(R.layout.fragment_favor, container, false);
	  userList = (ListView) rootView.findViewById(R.id.listView);
	  userList.setItemsCanFocus(false);
	  userList.setAdapter(userAdapter);
	  /**
	   * get on item click listener
	   */
	  userList.setOnItemClickListener(new OnItemClickListener() {

	   @Override
	   public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
		    Log.i("List View Clicked", "**********");
		    Toast.makeText(getActivity(), "List View Clicked:" + position, Toast.LENGTH_LONG).show();
	   }
	  });
	
	  return rootView;	
	 }

}

class UserCustomAdapter extends ArrayAdapter<User> {
	 Context context;
	 int layoutResourceId;
	 ArrayList<User> data = new ArrayList<User>();

	 public UserCustomAdapter(Context context, int layoutResourceId,
	   ArrayList<User> data) {
	  super(context, layoutResourceId, data);
	  this.layoutResourceId = layoutResourceId;
	  this.context = context;
	  this.data = data;
	 }

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	  View row = convertView;
	  UserHolder holder = null;

	  if (row == null) {
	   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	   row = inflater.inflate(layoutResourceId, parent, false);
	   holder = new UserHolder();
	   holder.textName = (TextView) row.findViewById(R.id.textView1);
	   holder.textAddress = (TextView) row.findViewById(R.id.textView2);
	   holder.textLocation = (TextView) row.findViewById(R.id.textView3);
	   holder.btnEdit = (Button) row.findViewById(R.id.button1);
	   holder.btnDelete = (Button) row.findViewById(R.id.button2);
	   row.setTag(holder);
	  } else {
	   holder = (UserHolder) row.getTag();
	  }
	  User user = data.get(position);
	  holder.textName.setText(user.getName());
	  holder.textAddress.setText(user.getAddress());
	  holder.textLocation.setText(user.getLocation());
	  holder.btnEdit.setOnClickListener(new OnClickListener() {

	   @Override
	   public void onClick(View v) {
	    // TODO Auto-generated method stub
	    Log.i("Edit Button Clicked", "**********");
	    Toast.makeText(context, "Edit button Clicked",
	      Toast.LENGTH_LONG).show();
	   }
	  });
	  holder.btnDelete.setOnClickListener(new OnClickListener() {

	   @Override
	   public void onClick(View v) {
	    // TODO Auto-generated method stub
	    Log.i("Delete Button Clicked", "**********");
	    Toast.makeText(context, "Delete button Clicked",
	      Toast.LENGTH_LONG).show();
	   }
	  });
	  return row;

	 }

	 static class UserHolder {
	  TextView textName;
	  TextView textAddress;
	  TextView textLocation;
	  Button btnEdit;
	  Button btnDelete;
	 }
}

class User {
	 String name;
	 String address;
	 String location;

	 public String getName() {
	  return name;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 public String getAddress() {
	  return address;
	 }

	 public void setAddress(String address) {
	  this.address = address;
	 }

	 public String getLocation() {
	  return location;
	 }

	 public void setLocation(String location) {
	  this.location = location;
	 }

	 public User(String name, String address, String location) {
	  super();
	  this.name = name;
	  this.address = address;
	  this.location = location;
	 }

}

	
	

	