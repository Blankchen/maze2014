package com.example.maze2014;






import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements FragmentMenu.OnMenuListener, FragmentCost.OnCostListener, FragmentFavor.OnFavorListener{

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
		"地圖導引", "菜單選擇",  "決定數量 ", "我的最愛", "訂單狀態"
	};
	//menu
	String[] listData = {"1號 大麥克", "2號 雙層牛肉吉士堡", "3號 四盎司牛肉堡", "4號 雙層四盎司牛肉堡", "5號 麥香魚", "6號 麥香雞", "7號 六塊麥克雞塊", "8號 勁辣雞腿堡", "9號 二塊麥脆雞", "10號 板烤雞腿堡"};
	int[] listCost = {125, 115, 132, 152, 112, 105, 119, 125, 139, 125};
	
	//map
	public static android.support.v4.app.FragmentManager fragmentManager;
	public static Double latitude = 25.082963, longitude = 121.549091;
	private static String tittle = "McDonald麥當勞", context = "104台灣北安路626號";	
	//cost
	boolean[] itemChecked = new boolean[listData.length]; //勾選項目
	String[] listChecked; //勾選清單
	int[] listCostCh; //勾選價錢
	int TrueCount = 0; //勾選總數
	int[] DataCount; //各選擇數量
	//favor
	String FavorName; //我的最愛名稱
	//order
	String phone;
	String search;
	String where;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		initActionBar();
		initDrawer();
		setDrawerMenu();		
		//預設目錄
		selectMenuItem(0);
				
		//map- initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
	    fragmentManager = getSupportFragmentManager();
		
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
	            case R.id.action_refresh:
	                openRefresh();
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    	
	    } 
	    //return super.onOptionsItemSelected(item);
	}
		

	//selectMenuItem(int position)	當清單物件被點擊後要執行的動作
	private void selectMenuItem(int position) {
		if(mCurrentMenuItemPosition == position && position ==0){
			Toast.makeText(this, "請選擇其他功能", Toast.LENGTH_LONG).show();
			return;
		}
		
	    mCurrentMenuItemPosition = position;	 
	    getActionBar().setTitle(
                MENU_ITEMS[mCurrentMenuItemPosition]);
	    // 將選單的子物件設定為被選擇的狀態
	    mLsvDrawerMenu.setItemChecked(position, true);	 
	    // 關掉 Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	   	 	    
		//[Android]使用 Navigation Drawer 製作側選單
		// http://blog.tonycube.com/2014/02/android-navigation-drawer-2.html		
	    Fragment fragment = null;
	    Bundle b=new Bundle();
	    
	    switch (position) {	    
	    case 0:	
	    	fragment = new FragmentMap();	        
	        b.putDouble("x", latitude);
	        b.putDouble("y", longitude);
	        b.putString("tittle", tittle);
	        b.putString("context", context);
	        fragment.setArguments(b);
	        break;  
	    case 1:	  
	        fragment = new FragmentMenu();	        
	        b.putStringArray("Data", listData);
	        b.putIntArray("Cost", listCost);
	        b.putBooleanArray("itemChecked", itemChecked);
	        fragment.setArguments(b);
	        break; 
	    case 2:
	        fragment = new FragmentCost(); 	      
	        b.putStringArray("DataChecked", listChecked);
	        b.putIntArray("Costed", listCostCh);
	        b.putIntArray("DataCount", DataCount);
	        fragment.setArguments(b);
	        break;  
	    case 3:
	        fragment = new FragmentFavor();
	        b.putString("tittle", tittle);
	        b.putStringArray("DataChecked", listChecked);
	        b.putIntArray("Costed", listCostCh);
	        b.putIntArray("DataCount", DataCount);
	        b.putString("FavorName", FavorName);
	        fragment.setArguments(b);
	        break;  
	    case 4:
	    	fragment = new FragmentOrder();
	    	b.putString("tittle", tittle);
	    	b.putString("phone", phone);
	    	b.putString("search", search);
	    	b.putString("where", where);
	    	b.putStringArray("datalist", listChecked);
	        b.putIntArray("datacost", listCostCh);
	        b.putIntArray("datacount", DataCount);
	        fragment.setArguments(b);
	    	break;  
	    default:
	        //還沒製作的選項，fragment 是 null，直接返回
	        return;
	    }
	    

	    FragmentManager fragmentManager = getFragmentManager(); 
	    fragmentManager.beginTransaction().  //开始Fragment的事务Transaction
	    	replace(R.id.content_frame, fragment). //替换容器(container)原来的Fragment 
	    	setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE). //设置转换效果 
	    	addToBackStack(null). //将事务添加到Back栈.即按下Back键时回到替换Fragment之前的状态.类似于Activity的返回
	    	commit();	//提交事务     	    	    
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
	
	public void openRefresh()
	{
		switch (mCurrentMenuItemPosition) {
		case 1:
			for(int i=0;i<itemChecked.length;i++) itemChecked[i]=false;
			TrueCount = 0;
			selectMenuItem(1);
			break;
		case 2:
			for(int i=0;i<DataCount.length;i++) DataCount[i]=1;
			selectMenuItem(2);
			break;
		case 4:			
			search = ((EditText)findViewById(R.id.etphone)).getText().toString();
			Log.d("FFFFFFFFFFF",search);
			selectMenuItem(4);
			break;			
		default:		        
			return;
		
		}
	}
	// 啟用 Support Library 的 ActionBar ====================================================
	
	//fragmentMenu callback
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//索取FragmentMenu資料
		itemChecked[position] = !itemChecked[position];	
		
		if(itemChecked[position]) TrueCount++;
		else TrueCount--;
		
		listChecked = new String[TrueCount];
		listCostCh = new int [TrueCount];
		DataCount = new int[TrueCount];
		
		for(int i = 0, j = 0; i < itemChecked.length; i++) 
			if(itemChecked[i]){
				listChecked[j] = listData[i];	
				listCostCh[j] = listCost[i];
				DataCount[j++] = 1;
			}
		
	}
	
	//fragmentCost callback-getDataCount sentOrder
	@Override
	public void getDataCount(int[] DataCount, String FavorName) {
		// TODO Auto-generated method stub	
		this.DataCount = DataCount;
		this.FavorName = FavorName;
		
		selectMenuItem(3);
		this.FavorName = null;

	}

	@Override
	public void sentOrder(String phone, String where, String[] datalist,
			int[] datacost, int[] datacount) {
		// TODO Auto-generated method stub
		this.phone = phone;
		this.search = phone;
		this.where = where;
		this.listChecked = datalist;
		this.listCostCh = datacost;
		this.DataCount = datacount;		
		selectMenuItem(4);
		this.phone = null;
		
	}
	
}
