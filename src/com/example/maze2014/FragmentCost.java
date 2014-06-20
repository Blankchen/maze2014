package com.example.maze2014;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentCost extends Fragment{
	static String[] test; //= {"1號 大麥克", "2號 雙層牛肉吉士堡", "3號 四盎司牛肉堡", "4號 雙層四盎司牛肉堡", "5號 麥香魚", "6號 麥香雞", "7號 六塊麥克雞塊", "8號 勁辣雞腿堡", "9號 二塊麥脆雞", "10號 板烤雞腿堡"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		test= getArguments().getStringArray("DataChecked");
		
		View rootView = inflater.inflate(R.layout.fragment_cost, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView1); 
        lv.setAdapter(new CheckListAdapter(getActivity(), test));
		
		return rootView;
	}
	
}


class CheckListAdapter extends BaseAdapter {

	private Context mContext;
	private String[] checkListName;
	Context context;

	// 存储以名值对。存放Spinner的Prompt和用户选中的值
	private Map<String, Integer> allValues;

	public CheckListAdapter(Context mContext, String[] checkListName) {
		context = mContext;
		this.mContext = mContext;
		this.checkListName = checkListName;
		allValues = new HashMap<String, Integer>();
		putAllValues();
	}

	private void putAllValues() {
		for (String str : checkListName) {
			allValues.put(str, 0);
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
			         text.setText(""+num[position]);
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
			         text.setText("     "+num[position]);
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

		holder.checkinfo_item_name.setText(checkedName);
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
