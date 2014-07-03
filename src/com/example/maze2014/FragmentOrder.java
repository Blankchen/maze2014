package com.example.maze2014;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentOrder extends Fragment{
	ListView OrderList;
	EditText etphone;
	OrderCustomAdapter OrderAdapter;
	ArrayList<Order> OrderArray = new ArrayList<Order>();
	static String tittle;
	static String phone;
	static String search;
	static String where;
	static String[] Datalist; 
	static int[] listCostCh;
	static int[] DataCount; 
	//httpPost
	private String uriAPI = "http://192.168.11.3/httpPostWrite.php";
	/** 「要更新版面」的訊息代碼 */
	protected static final int REFRESH_DATA = 0x00000001;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_order, container, false);
		OrderList = (ListView) rootView.findViewById(R.id.listView1);
		etphone = (EditText) rootView.findViewById(R.id.etphone);
		
		if(getArguments().getString("phone")!=null){		
			tittle =  getArguments().getString("tittle", tittle);
			phone = getArguments().getString("phone");
			where = getArguments().getString("where");
			Datalist = getArguments().getStringArray("datalist"); 
			listCostCh = getArguments().getIntArray("datacost");
			DataCount = getArguments().getIntArray("datacount");	
			String datalist = "";
			String datacost = "";
			String datacount = "";
			for(int i=0; i<Datalist.length; i++){
				datalist+=Datalist[i]+",";
				datacost+=listCostCh[i]+",";
				datacount+=DataCount[i]+",";
			}
			etphone.setText(phone);
			Thread test = new Thread(new sendDataRunnable(phone, where, datalist, datacost, datacount, tittle, "準備中"));
			test.start();
			
		}else{		
			etphone.setText(search);
			search = getArguments().getString("search");		
			//httpPost
			Thread t = new Thread(new sendDataRunnable(search, null, null, null, null, null, null));
			t.start();
		}
		
		OrderAdapter = new OrderCustomAdapter(getActivity(), R.layout.row_listview_order, OrderArray);	
		OrderList.setAdapter(OrderAdapter);	
				
		return rootView;
	}
	
	/** 建立UI Thread使用的Handler，來接收其他Thread來的訊息 */
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 顯示網路上抓取的資料
			case REFRESH_DATA:
				String result = null;
				if (msg.obj instanceof String)
					result = (String) msg.obj;
				if (result != null)
					// 印出網路回傳的文字
					renewListView(result);
				break;
			}
		}
	};
	
	class sendDataRunnable implements Runnable
	{
		String phone = null;
		String where = null;
		String datalist = null;
		String datacost = null;
		String datacount = null;
		String tittle = null;
		String state = null;

		// 建構子，設定要傳的字串
		public sendDataRunnable(String phone, String where, String datalist, String datacost, String datacount, String tittle, String state)
		{
			this.phone = phone;
			this.where = where;
			this.datalist = datalist;
			this.datacost = datacost;
			this.datacount = datacount;
			this.tittle = tittle;
			this.state = state;
		}

		@Override
		public void run()
		{
			String result = sendDataToInternet(phone, where, datalist, datacost, datacount, tittle, state);
			mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
		}

	}
	

	private String sendDataToInternet(String phone, String where, String datalist, String datacost, String datacount, String tittle, String state)
	{

		/* 建立HTTP Post連線 */
		HttpPost httpRequest = new HttpPost(uriAPI);
		/*
		 * Post運作傳送變數必須用NameValuePair[]陣列儲存
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("where", where));
		params.add(new BasicNameValuePair("datalist", datalist));
		params.add(new BasicNameValuePair("datacost", datacost));
		params.add(new BasicNameValuePair("datacount", datacount));
		params.add(new BasicNameValuePair("tittle", tittle));
		params.add(new BasicNameValuePair("state", state));

		try

		{
			/* 發出HTTP request */

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 取得HTTP response */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若狀態碼為200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				/* 取出回應字串 */
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				// 回傳回應字串
				return strResult;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
		
	public final void renewListView(String input)
    {
	/*
	 * SQL 結果有多筆資料時使用JSONArray
	 * 只有一筆資料時直接建立JSONObject物件
	 * JSONObject jsonData = new JSONObject(result);
	 */
	try
	{
	    JSONArray jsonArray = new JSONArray(input);
	    for (int i = 0; i < jsonArray.length(); i++)
	    {
			JSONObject jsonData = jsonArray.getJSONObject(i);

			
			String no = jsonData.getString("no");
			phone = jsonData.getString("phone");
			where = jsonData.getString("where");
			Datalist = jsonData.getString("datalist").split(",");
			String[] scost = jsonData.getString("datacost").split(",");
			String[] scount = jsonData.getString("datacount").split(",");
			int[] fu1=new int[scost.length];
		    int[] fu2=new int[scost.length];
			for(int j=0;j<scost.length;j++){
				fu1[j] = Integer.parseInt(scost[j]);
				fu2[j] = Integer.parseInt(scount[j]);
			}
			listCostCh = fu1;
			DataCount = fu2;
			tittle = jsonData.getString("tittle");
			String state = jsonData.getString("state");
			
			String context = "";
			int total = 0;
			for(int j=0; j<Datalist.length; j++){
				context += Datalist[j] + ", " + DataCount[j] + " 份, 共" + listCostCh[j]*DataCount[j] + " 元\n";
				total += listCostCh[j]*DataCount[j];	
			}
			OrderArray.add(new Order(tittle+"("+where+")",context,"總計： "+total+" 元 ("+phone+")",no,state));
			
			OrderAdapter = new OrderCustomAdapter(getActivity(), R.layout.row_listview_order, OrderArray);
			OrderList.setAdapter(OrderAdapter);	
	    }
	}
	catch (JSONException e)
	{
	    // TODO 自動產生的 catch 區塊
	    e.printStackTrace();
	}
    }
}

class OrderCustomAdapter extends ArrayAdapter<Order> {
	 Context context;
	 int layoutResourceId;
	 ArrayList<Order> data = new ArrayList<Order>();

	 public OrderCustomAdapter(Context context, int layoutResourceId,
	   ArrayList<Order> data) {
	  super(context, layoutResourceId, data);
	  this.layoutResourceId = layoutResourceId;
	  this.context = context;
	  this.data = data;
	 }

	 @Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
	  View row = convertView;
	  UserHolder holder = null;

	  if (row == null) {
	   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	   row = inflater.inflate(layoutResourceId, parent, false);
	   holder = new UserHolder();
	   holder.textId = (TextView) row.findViewById(R.id.txtName);
	   holder.textContext = (TextView) row.findViewById(R.id.txtContext);
	   holder.textMoeny = (TextView) row.findViewById(R.id.txtMoney);
	   holder.textNo = (TextView) row.findViewById(R.id.txtNo);
	   holder.textState = (TextView) row.findViewById(R.id.txtState);
	   row.setTag(holder);
	  } else {
	   holder = (UserHolder) row.getTag();
	  }
	  
	  final Order user = data.get(position);
	  holder.textId.setText(user.getName());
	  holder.textContext.setText(user.getAddress());
	  holder.textMoeny.setText(user.getLocation());
	  holder.textNo.setText(user.getNo());
	  holder.textState.setText(user.getState());
	   
	  return row;
	 }
	 
	 static class UserHolder {
	  TextView textId;
	  TextView textContext;
	  TextView textMoeny;
	  TextView textNo;
	  TextView textState;
	 }
}

class Order {
	 String name;
	 String address;
	 String location;
	 String no;
	 String State;

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
	 
	 public String getNo(){
		 return no;
	 }
	 
	 public void setNo(String no){
		 this.no = no;
	 }
	 
	 public String getState(){
		 return State;
	 }
	 
	 public void setState(String state){
		 this.State = state;
	 }

	 public Order(String name, String address, String location, String no, String state) {
	  super();
	  this.name = name;
	  this.address = address;
	  this.location = location;
	  this.no = no;
	  this.State = state;
	 }

}

