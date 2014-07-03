package com.example.maze2014;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class FragmentCost extends Fragment{
	OnCostListener MenuCallback;
	static String[] Datalist; 
	static int[] listCostCh;
	static int[] DataCount; 
	static CheckListAdapter cla;
	static String FavorName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_cost, container, false);
		ListView lv = (ListView) rootView.findViewById(R.id.listView1); 
		final TextView tv = (TextView) rootView.findViewById(R.id.NoOrder);
		Button favor = (Button) rootView.findViewById(R.id.button1);
		Button order = (Button) rootView.findViewById(R.id.button2);
              
		if(getArguments().getStringArray("DataChecked") != null){
			Datalist = getArguments().getStringArray("DataChecked"); 
			listCostCh = getArguments().getIntArray("Costed");
			DataCount = getArguments().getIntArray("DataCount");	
			
			cla = new CheckListAdapter(getActivity(), Datalist, listCostCh, DataCount);	
	        lv.setAdapter(cla);
	        if(Datalist.length == 0) tv.setVisibility(0);
		}else{
			tv.setVisibility(0);
		}
		
		favor.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tv.getVisibility()==4){
					Map<String, Integer> select = cla.getSelectValues();
					for(int i=0;i<Datalist.length;i++)
						DataCount[i] = select.get(Datalist[i])+1;						
					
		            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());	             
		            alert.setTitle(R.string.myfavor); //Set Alert dialog title here
		            alert.setMessage("請輸入我的最愛名稱"); //Message here
		 
		            final EditText input = new EditText(getActivity());
		            input.setInputType(InputType.TYPE_CLASS_TEXT);
		            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		            alert.setView(input);
		 
		            alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {

				             String srt = input.getEditableText().toString();
				             FavorName = srt;
				             Toast.makeText(getActivity(),srt,Toast.LENGTH_LONG).show(); 
				             getDataCount(DataCount, FavorName);
			            } // End of onClick(DialogInterface dialog, int whichButton)
			        }); //End of alert.setPositiveButton
		            
			        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface dialog, int whichButton) {
			                  dialog.cancel();
			              }
			        }); //End of alert.setNegativeButton
			            
			        AlertDialog alertDialog = alert.create();
			        alertDialog.show();
				}					
			}});
				
		order.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tv.getVisibility()==4){
		            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());	             
		            alert.setTitle("發送訂單"); //Set Alert dialog title here
		            alert.setMessage("請輸入電話並選擇內用/外帶"); //Message here
		            
		            final EditText input = new EditText(getActivity());
		            input.setInputType(InputType.TYPE_CLASS_PHONE);
		            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		            input.setKeyListener(DigitsKeyListener.getInstance());
		            alert.setView(input);
		            			 
		            alert.setPositiveButton("內用", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			            	String srt = input.getEditableText().toString();
			            	sentOrder(srt, "內用", Datalist, listCostCh, DataCount);			                        					                     
			            } // End of onClick(DialogInterface dialog, int whichButton)
			        }); //End of alert.setPositiveButton
		            
			        alert.setNegativeButton("外帶", new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface dialog, int whichButton) {
			            	  String srt = input.getEditableText().toString();
			            	  sentOrder(srt, "外帶", Datalist, listCostCh, DataCount);				                
			              }
			        }); //End of alert.setNegativeButton
			            
		            AlertDialog alertDialog = alert.create();
		            alertDialog.show();		
				}
			}});
		return rootView;
	}
	
	
	public void getDataCount(int[] DataCount, String FavorName){
		MenuCallback.getDataCount(DataCount, FavorName);		
	}
	
	public void sentOrder(String phone, String state, String[] datalist, int[] datacost, int[] datacount){
		MenuCallback.sentOrder(phone,state,datalist,datacost,datacount);
	}
	
	//fragment to activity 作為容器的 Activity 必需實作這個界面
    public interface OnCostListener {
    	public void getDataCount(int[] DataCount, String favorName);
    	public void sentOrder(String phone, String state, String[] datalist, int[] datacost, int[] datacount);	
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);        
        // 這裡確保容器 activity 有實作這個界面、否則丟出例外
        try {
        	MenuCallback = (OnCostListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCostListener");
        }
    }
   
}





class CheckListAdapter extends BaseAdapter {

	private Context mContext;
	private String[] checkListName;
	private int [] ListCost;
	private int [] ListCount;
	Context context;
	
	// 存储以名值对。存放Spinner的Prompt和用户选中的值
	private Map<String, Integer> allValues;

	public CheckListAdapter(Context mContext, String[] checkListName, int[] ListCost, int[] ListCount) {
		context = mContext;
		this.mContext = mContext;
		this.checkListName = checkListName;
		this.ListCount = ListCount;
		this.ListCost = ListCost;
		allValues = new HashMap<String, Integer>();
		putAllValues();
	}

	private void putAllValues() {
		for (int i = 0; i < checkListName.length; i++) {
			allValues.put(checkListName[i], ListCount[i]-1);
		}
	}
	
	public void setAllValues(Map<String, Integer> allValues){
		this.allValues = allValues;
	}

	@Override
	public int getCount() {
		return checkListName.length;
	}

	@Override
	public Object getItem(int position) {
		return checkListName[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		TextView checkinfo_item_name;
		Spinner checkinfo_item_value;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_listview_spinner, null);
			
			holder = new ViewHolder();
			holder.checkinfo_item_name = (TextView) convertView
					.findViewById(R.id.checkinfo_item_name);
			holder.checkinfo_item_value = (Spinner) convertView
					.findViewById(R.id.checkinfo_item_value);
			// 设置其adapter
			SpinnerAdapter adapter = new SpinnerAdapter() {
				int[] num = {1,2,3,4,5};
				
				@Override
				public void unregisterDataSetObserver(DataSetObserver observer) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void registerDataSetObserver(DataSetObserver observer) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isEmpty() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean hasStableIds() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public int getViewTypeCount() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					 TextView text = new TextView(mContext);					 
			         text.setText(num[position]+" 份");			        
			         return text;
				}
				
				@Override
				public int getItemViewType(int position) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return num[position];
				}
				
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return num.length;
				}
				
				@Override
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					 TextView text = new TextView(mContext);
			         text.setText(num[position]+"");
			         text.setWidth(100);
			         text.setPaddingRelative(50, 10, 0, 10);
			         return text;
				}
			};
			
			holder.checkinfo_item_value.setAdapter(adapter);
			holder.checkinfo_item_value
					.setOnItemSelectedListener(new ItemClickSelectListener(
							holder.checkinfo_item_value));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String checkedName = checkListName[position];
		int Cost = (allValues.get(checkedName)+1)*ListCost[position];

		holder.checkinfo_item_name.setText(checkedName + " $" + Cost);
		//关键代码，配合下面的相应事件使用。
		holder.checkinfo_item_value.setPrompt(checkedName);
				
		int spinnerOptionPosition = allValues.get(checkedName);
		Log.d("CheckList", checkedName + " = = " + spinnerOptionPosition);

		holder.checkinfo_item_value.setSelection(spinnerOptionPosition);
		return convertView;
	}

	private class ItemClickSelectListener implements OnItemSelectedListener {
		Spinner checkinfo_item_value ;

		public ItemClickSelectListener(Spinner checkinfo_item_value) {
			this.checkinfo_item_value = checkinfo_item_value;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {		
			//关键代码
			allValues.put(checkinfo_item_value.getPrompt().toString(), position);	
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
		
	}
        //返回用于选中的所有值
	public Map<String,Integer> getSelectValues() {
		return allValues;
	}	
	
}
