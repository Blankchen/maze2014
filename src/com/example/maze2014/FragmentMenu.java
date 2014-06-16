package com.example.maze2014;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class FragmentMenu extends ListFragment{
	
	OnMenuListener MenuCallback;
	static String[] listData;
	static int[] listCost;
	static String[] listItem;
	static boolean[] itemChecked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		listData = getArguments().getStringArray("Data");
		listCost = getArguments().getIntArray("Cost");
		itemChecked = new boolean[listData.length];
		listItem = new String[listData.length];
				
		for(int i=0; i<listData.length; i++) listItem[i] = listData[i]+" $"+listCost[i];
		
		ArrayAdapter<String> adapter 
        = new ArrayAdapter<String>(getActivity(),
             android.R.layout.simple_list_item_multiple_choice,listItem);		
		setListAdapter(adapter);
		
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initView(inflater, container);           
    }   
    
    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);   
             
        return view;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
     
        String menu = "您點的餐點有：";        
     
        for(int i=0;i<l.getCheckedItemPositions().size();i++)
        {
        	if(l.getCheckedItemPositions().valueAt(i))        	  
        		menu += "\n"+listData[l.getCheckedItemPositions().keyAt(i)];
        }
        
        if(l.getCheckedItemCount()>0)             
        	Toast.makeText(getActivity(), menu, Toast.LENGTH_SHORT).show(); 
        else
        	Toast.makeText(getActivity(), "尚未點餐", Toast.LENGTH_SHORT).show();    
        //callback to activity
        MenuCallback.onListItemClick(l, v, position, id);
    }
    
    //fragment to activity 作為容器的 Activity 必需實作這個界面
    public interface OnMenuListener {
    	public void onListItemClick(ListView l, View v, int position, long id);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // 這裡確保容器 activity 有實作這個界面、否則丟出例外
        try {
        	MenuCallback = (OnMenuListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
