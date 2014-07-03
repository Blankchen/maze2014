package com.example.maze2014;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentFavor extends Fragment{
	 OnFavorListener MenuCallback;
	 ListView userList;
	 UserCustomAdapter userAdapter;
	 ArrayList<User> userArray = new ArrayList<User>();
	 static String[] Datalist; 
	 static int[] listCostCh;
	 static int[] DataCount; 
	 static String FavorName;
	 static String tittle;
	 private SQLiteActivity dbhelper = null; //資料庫	
	 String favorname, datalist, datacost, datacount; //資料欄位
	 
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
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		dbhelper.close();
	}
	 
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 dbhelper = new SQLiteActivity(getActivity(), "BigOrder.db", null, 0); 
		 showListView();
	
		 if(getArguments().getString("FavorName") != null){
		 	tittle =  getArguments().getString("tittle", tittle);
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
			userArray.add(new User(tittle+"："+FavorName+"", context, "總計： "+total+" 元", add()));							
		 }
	  userAdapter = new UserCustomAdapter(getActivity(), R.layout.row_listview_favor, userArray, this);
	  View rootView = inflater.inflate(R.layout.fragment_favor, container, false);
	  userList = (ListView) rootView.findViewById(R.id.listView);
	  userList.setItemsCanFocus(false);
	  userList.setAdapter(userAdapter);

	  userList.setOnItemClickListener(new OnItemClickListener() {

	   @Override
	   public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
		    Log.i("List View Clicked", "**********");
		    Toast.makeText(getActivity(), "List View Clicked:" + position, Toast.LENGTH_LONG).show();
	   }
	  });
	  return rootView;	
	 }
	
	 private void showListView(){
		 Cursor cursor = getCursor();
	      while(cursor.moveToNext()){
	            int id = cursor.getInt(0);
	            String fl = cursor.getString(1);	            
	        	String[] f2 = cursor.getString(2).split(","); 
	        	String[] f3 = cursor.getString(3).split(","); 
	        	String[] f4 = cursor.getString(4).split(","); 	   
				String context = "";
				int total = 0;
				for(int i=0; i<f2.length; i++){
					context += f2[i] + ", " + f4[i] + " 份, 共" + Integer.parseInt(f3[i])*Integer.parseInt(f4[i]) + " 元\n";
					total += Integer.parseInt(f3[i])*Integer.parseInt(f4[i]);		
				}	
				userArray.add(new User(fl, context, "總計： "+total+" 元", id));
	        }		 
	 }
	 
	 private void search(int id){
		 String[] search = new String[]{id+""};	        
		 SQLiteDatabase db = dbhelper.getReadableDatabase();
         String[] columns = {"_ID", "favorname", "datalist", "datacost", "datacount"};        
         Cursor cursor = db.query("MyFavor", columns, "_ID=?", search, null, null, null);
         
         while(cursor.moveToNext()){	            
        	Datalist =  cursor.getString(2).split(","); 
        	String[] co2 = cursor.getString(3).split(","); 
        	String[] co3 = cursor.getString(4).split(",");       
		    int[] fu1=new int[co2.length];
		    int[] fu2=new int[co2.length];
			for(int i=0;i<co2.length;i++){
				fu1[i] = Integer.parseInt(co2[i]);
				fu2[i] = Integer.parseInt(co3[i]);
			}
			listCostCh = fu1;
		 	DataCount = fu2; 	
         }
	 }
	 
	  private int add(){
		favorname = FavorName; 
		datalist = datacost = datacount="";
		for(int i=0;i<Datalist.length;i++){
			datalist += Datalist[i]+",";
			datacost += listCostCh[i]+",";
			datacount += DataCount[i]+",";
		}
	
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();        
        values.put("favorname", tittle+"："+FavorName);
        values.put("datalist", datalist);
        values.put("datacost", datacost);
        values.put("datacount", datacount);
        db.insert("MyFavor", null, values);
        
        String[] search = new String[]{favorname};	        
	    db = dbhelper.getReadableDatabase();
        String[] columns = {"_ID", "favorname", "datalist", "datacost", "datacount"};        
        Cursor cursor = db.query("MyFavor", columns, "favorname=?", search, null, null, null);
        int id = 0;
        while(cursor.moveToNext())
        	id = cursor.getInt(0);
        return id;
	 }
	 
	 public void del(int id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete("MyFavor", "_ID=" + id, null);
	 }
/*
    public void update(){
        String id = "0";

        ContentValues values = new ContentValues();
        values.put("favorname", favorname);
        values.put("datalist", datalist);
        values.put("datacost", datacost);
        values.put("datacount", datacount);

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.update("MyFavor", values, "_ID=" + id, null);
    }*/
    
    private Cursor getCursor(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] columns = {"_ID", "favorname", "datalist", "datacost", "datacount"};

        Cursor cursor = db.query("MyFavor", columns, null, null, null, null, null);
        return cursor;
    }
    
    
    /** 彈出對話框 
     * @param state */
    public void myDialog(final int id, final String tittle, String state, final int position) {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(tittle);
        
        if(state.equals("Order")){
        	builder.setMessage("請輸入電話並選擇內用/外帶");
        	
        	final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            input.setKeyListener(DigitsKeyListener.getInstance());
            builder.setView(input);
	            
        	builder.setPositiveButton("內用", new DialogInterface.OnClickListener() {     
                    @Override
                    public void onClick(DialogInterface dialog, int which) {                    	
                    	String srt = input.getEditableText().toString();
                    	search(id);
		            	sentOrder(srt, "內用", Datalist, listCostCh, DataCount);		
                    }
                })
                .setNegativeButton("外帶", new DialogInterface.OnClickListener() {     
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	String srt = input.getEditableText().toString();
                    	search(id);
		            	sentOrder(srt, "外帶", Datalist, listCostCh, DataCount);		
                    }
                });
        }else{
        	builder.setPositiveButton("是", new DialogInterface.OnClickListener() {        	     
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	userAdapter.RemoveData(position);
                	del(id);                  	
                }
            })
            .setNegativeButton("否", new DialogInterface.OnClickListener() { 
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	dialog.cancel();                   	
                }
            });        	
        }     
        AlertDialog ad = builder.create();
        ad.show();
    }
    
	public void sentOrder(String phone, String state, String[] datalist, int[] datacost, int[] datacount){
		MenuCallback.sentOrder(phone,state,datalist,datacost,datacount);
	}
	
    public interface OnFavorListener {
     	public void sentOrder(String phone, String state, String[] datalist, int[] datacost, int[] datacount);	
    }
  
    @Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);        
       // 這裡確保容器 activity 有實作這個界面、否則丟出例外
    try {
    	MenuCallback = (OnFavorListener) activity;
    } catch (ClassCastException e) {
        throw new ClassCastException(activity.toString()
                + " must implement OnFavorListener");
        }
    }
}

class UserCustomAdapter extends ArrayAdapter<User> {
	 Context context;
	 FragmentFavor fragmentFavor;
	 int layoutResourceId;
	 ArrayList<User> data = new ArrayList<User>();

	 public UserCustomAdapter(Context context, int layoutResourceId,
	   ArrayList<User> data, FragmentFavor fragmentFavor) {
	  super(context, layoutResourceId, data);
	  this.layoutResourceId = layoutResourceId;
	  this.context = context;
	  this.data = data;
	  this.fragmentFavor = fragmentFavor;
	 }

	 @Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
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
	  final User user = data.get(position);
	  holder.textName.setText(user.getName());
	  holder.textAddress.setText(user.getAddress());
	  holder.textLocation.setText(user.getLocation());
	  
	  holder.btnEdit.setOnClickListener(new OnClickListener() {
	   @Override
	   public void onClick(View v) {
	    // TODO Auto-generated method stub     
		    fragmentFavor.myDialog(user.getId(),"確定訂購?","Order", position);
	   }
	  });
	  
	  holder.btnDelete.setOnClickListener(new OnClickListener() {
	   @Override
	   public void onClick(View v) {
	    // TODO Auto-generated method stub
		   fragmentFavor.myDialog(user.getId(),"確定刪除?","Delete", position);
	   }
	  });
	  
	  return row;
	 }
	 
	 public void RemoveData(int position){
		 data.remove(position);
		 notifyDataSetChanged();
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
	 int id;

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
	 
	 public int getId(){
		 return id;
	 }

	 public User(String name, String address, String location, int id) {
	  super();
	  this.name = name;
	  this.address = address;
	  this.location = location;
	  this.id = id;
	 }

}

	
	

	