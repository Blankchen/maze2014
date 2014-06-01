package com.example.maze2014;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private LinearLayout mLlvDrawerContent;
	private ListView mLsvDrawerMenu;
	// 記錄被選擇的選單指標用
	private int mCurrentMenuItemPosition = -1;
	// 選單項目
	public static final String[] MENU_ITEMS = new String[]{
	    "Item1", "Item2", "Item3", "Item4", "Item5"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drw_layout);
	    // 設定 Drawer 的影子
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	 
	    mDrawerToggle = new ActionBarDrawerToggle(
	            this, 
	            mDrawerLayout,    // 讓 Drawer Toggle 知道母體介面是誰
	            R.drawable.ic_drawer, // Drawer 的 Icon
	            R.string.open_left_drawer, // Drawer 被打開時的描述
	            R.string.close_left_drawer // Drawer 被關閉時的描述
	            ) {
	                //被打開後要做的事情
	                @Override
	                public void onDrawerOpened(View drawerView) {
	                    // 將 Title 設定為自定義的文字
	                	getActionBar().setTitle(R.string.open_left_drawer);
	                }
	 
	                //被關上後要做的事情
	                @Override
	                public void onDrawerClosed(View drawerView) {
	                	if (mCurrentMenuItemPosition > -1) {
	                        getActionBar().setTitle(
	                                MENU_ITEMS[mCurrentMenuItemPosition]);
	                    } else {
	                        // 將 Title 設定回 APP 的名稱
	                        getActionBar().setTitle(R.string.app_name);
	                    }
	                }
	    };
	 
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    //顯示 Up Button (位在 Logo 左手邊的按鈕圖示)
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    //打開 Up Button 的點擊功能 
	    getActionBar().setHomeButtonEnabled(true);
	    
	    setDrawerMenu();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    mDrawerToggle.syncState();
	}
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	      return true;
	    }
	 
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setDrawerMenu() {
	    // 定義新宣告的兩個物件：選項清單的 ListView 以及 Drawer內容的 LinearLayou
	    mLsvDrawerMenu = (ListView) findViewById(R.id.lsv_drawer_menu);
	    mLlvDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
	 
	    // 當清單選項的子物件被點擊時要做的動作
	    mLsvDrawerMenu.setOnItemClickListener(new OnItemClickListener() {
	 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            selectMenuItem(position);
	        }
	    });
	    // 設定清單的 Adapter，這裡直接使用 ArrayAdapter<String>
	    mLsvDrawerMenu.setAdapter(new ArrayAdapter<String>(
	            this,
	            R.layout.drawer_menu_item,  // 選單物件的介面 
	            MENU_ITEMS                  // 選單內容
	    ));
	}
	
	private void selectMenuItem(int position) {
	    mCurrentMenuItemPosition = position;
	 
	    // 將選單的子物件設定為被選擇的狀態
	    mLsvDrawerMenu.setItemChecked(position, true);
	 
	    // 關掉 Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	}

	

}
