package com.example.maze2014;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	//這個是整個 Navigation Drawer 的物件，也就是最外層被我們設定 id 為 @+id/drw_layout 的 DrawerLayout
	private DrawerLayout mDrawerLayout;
	//當在 Action Bar 上的 Drawer Icon 被點擊時要做的動作
	private ActionBarDrawerToggle mDrawerToggle;	
	private LinearLayout mLlvDrawerContent;
	private ListView mLsvDrawerMenu;
	// 記錄被選擇的選單指標用
	private int mCurrentMenuItemPosition = -1;
	// 選單項目
	public static final String[] MENU_ITEMS = new String[]{
	    "活動資料頁面", "會場相關資訊", "個人活動清單 ", "搜尋導引頁面", "NFC資料對傳"
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActionBar();
		initDrawer();
		setDrawerMenu();
		
		//預設目錄
		selectMenuItem(0);
	}
	
	// Navigation Drawer 實作 ==============================================================
	// 莫希爾(Mosil)手札 http://blog.mosil.biz/2013/12/android-navigation-drawer/
	private void initActionBar(){		  
		//顯示 Up Button (位在 Logo 左手邊的按鈕圖示)
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//打開 Up Button 的點擊功能 
		getActionBar().setHomeButtonEnabled(true);			  
	}
	
	private void initDrawer(){
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
	        	//切換 Fragment
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
	
	//onPostCreate 去同步 DrawerToggle 的狀態，這是確保 Activity 在被重啟生命週期後，Drawer 可以維持該有的長相，如其 icon
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    mDrawerToggle.syncState();
	}
	
	//onConfigurationChanged，這主要比較會是在畫面有變化時要確保 Drawer 該有的動作
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	//第三個在 Drawer 被點下時，也就是在 onOptionsItemSelected 該要做的動作。 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	    	//Drawer 點選
	    	return true;
	    }else{
	    	// ActionBar 各圖示的 click 事件處理
	    	switch (item.getItemId()) 
	        {
	            case R.id.action_search:
	                openSearch();
	                return true;
	            case R.id.action_refresh:
	                openEdit();
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    	
	    } 
	    //return super.onOptionsItemSelected(item);
	}
		

	//selectMenuItem(int position)	當清單物件被點擊後要執行的動作
	private void selectMenuItem(int position) {
	    mCurrentMenuItemPosition = position;	 
	    // 將選單的子物件設定為被選擇的狀態
	    mLsvDrawerMenu.setItemChecked(position, true);	 
	    // 關掉 Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	    
		//[Android]使用 Navigation Drawer 製作側選單
		// http://blog.tonycube.com/2014/02/android-navigation-drawer-2.html		
	    Fragment fragment = null;

	    switch (position) {
	    case 0:
	        fragment = new FragmentApple();
	        break;
	    default:
	        //還沒製作的選項，fragment 是 null，直接返回
	        return;
	    }

	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();	
	    
	}

	// Navigation Drawer 實作 ==============================================================
	
//#################################################################################################
	
	// 啟用 Support Library 的 ActionBar ====================================================
	// 老灰鴨的筆記本 http://oldgrayduck.blogspot.tw/2013/10/android-support-library-actionbar.html
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		
		//MenuItem searchItem = menu.findItem(R.id.action_search);	    
		//SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);	
		
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void openSearch()
	{
	    Toast.makeText(this, "按了 尋找 鈕", Toast.LENGTH_LONG).show();
	}
	  
	public void openEdit()
	{
	    Toast.makeText(this, "按了 更新  鈕", Toast.LENGTH_LONG).show();
	}
	
	// 啟用 Support Library 的 ActionBar ====================================================

	
}
