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

	//�o�ӬO��� Navigation Drawer ������A�]�N�O�̥~�h�Q�ڭ̳]�w id �� @+id/drw_layout �� DrawerLayout
	private DrawerLayout mDrawerLayout;
	//��b Action Bar �W�� Drawer Icon �Q�I���ɭn�����ʧ@
	private ActionBarDrawerToggle mDrawerToggle;	
	private LinearLayout mLlvDrawerContent;
	private ListView mLsvDrawerMenu;
	// �O���Q��ܪ������Х�
	private int mCurrentMenuItemPosition = -1;
	// ��涵��
	public static final String[] MENU_ITEMS = new String[]{
	    "���ʸ�ƭ���", "�|��������T", "�ӤH���ʲM�� ", "�j�M�ɤޭ���", "NFC��ƹ��"
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActionBar();
		initDrawer();
		setDrawerMenu();
		
		//�w�]�ؿ�
		selectMenuItem(0);
	}
	
	// Navigation Drawer ��@ ==============================================================
	// ���ƺ�(Mosil)�⥾ http://blog.mosil.biz/2013/12/android-navigation-drawer/
	private void initActionBar(){		  
		//��� Up Button (��b Logo �����䪺���s�ϥ�)
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//���} Up Button ���I���\�� 
		getActionBar().setHomeButtonEnabled(true);			  
	}
	
	private void initDrawer(){
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drw_layout);
		
	    // �]�w Drawer ���v�l
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	 
	    mDrawerToggle = new ActionBarDrawerToggle(
	            this, 
	            mDrawerLayout,    // �� Drawer Toggle ���D���餶���O��
	            R.drawable.ic_drawer, // Drawer �� Icon
	            R.string.open_left_drawer, // Drawer �Q���}�ɪ��y�z
	            R.string.close_left_drawer // Drawer �Q�����ɪ��y�z
	            ) {
	                //�Q���}��n�����Ʊ�
	                @Override
	                public void onDrawerOpened(View drawerView) {
	                    // �N Title �]�w���۩w�q����r
	                	getActionBar().setTitle(R.string.open_left_drawer);	                
	                }
	 
	                //�Q���W��n�����Ʊ�
	                @Override
	                public void onDrawerClosed(View drawerView) {
	                	if (mCurrentMenuItemPosition > -1) {
	                        getActionBar().setTitle(
	                                MENU_ITEMS[mCurrentMenuItemPosition]);
	                    } else {
	                        // �N Title �]�w�^ APP ���W��
	                        getActionBar().setTitle(R.string.app_name);
	                    }
	                }
	    };
	 
	    mDrawerLayout.setDrawerListener(mDrawerToggle);	        	
	}
	
	private void setDrawerMenu() {
	    // �w�q�s�ŧi����Ӫ���G�ﶵ�M�檺 ListView �H�� Drawer���e�� LinearLayou
	    mLsvDrawerMenu = (ListView) findViewById(R.id.lsv_drawer_menu);
	    mLlvDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
	 
	    // ��M��ﶵ���l����Q�I���ɭn�����ʧ@
	    mLsvDrawerMenu.setOnItemClickListener(new OnItemClickListener() {	 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	//���� Fragment
	            selectMenuItem(position);	        
	        }
	    });
	    // �]�w�M�檺 Adapter�A�o�̪����ϥ� ArrayAdapter<String>
	    mLsvDrawerMenu.setAdapter(new ArrayAdapter<String>(
	            this,
	            R.layout.drawer_menu_item,  // ��檫�󪺤��� 
	            MENU_ITEMS                  // ��椺�e
	    ));
	}
	
	//onPostCreate �h�P�B DrawerToggle �����A�A�o�O�T�O Activity �b�Q���ҥͩR�g����ADrawer �i�H�����Ӧ������ۡA�p�� icon
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    mDrawerToggle.syncState();
	}
	
	//onConfigurationChanged�A�o�D�n����|�O�b�e�����ܤƮɭn�T�O Drawer �Ӧ����ʧ@
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	//�ĤT�Ӧb Drawer �Q�I�U�ɡA�]�N�O�b onOptionsItemSelected �ӭn�����ʧ@�C 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	    	//Drawer �I��
	    	return true;
	    }else{
	    	// ActionBar �U�ϥܪ� click �ƥ�B�z
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
		

	//selectMenuItem(int position)	��M�檫��Q�I����n���檺�ʧ@
	private void selectMenuItem(int position) {
	    mCurrentMenuItemPosition = position;	 
	    // �N��檺�l����]�w���Q��ܪ����A
	    mLsvDrawerMenu.setItemChecked(position, true);	 
	    // ���� Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	    
		//[Android]�ϥ� Navigation Drawer �s�@�����
		// http://blog.tonycube.com/2014/02/android-navigation-drawer-2.html		
	    Fragment fragment = null;

	    switch (position) {
	    case 0:
	        fragment = new FragmentApple();
	        break;
	    default:
	        //�٨S�s�@���ﶵ�Afragment �O null�A������^
	        return;
	    }

	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();	
	    
	}

	// Navigation Drawer ��@ ==============================================================
	
//#################################################################################################
	
	// �ҥ� Support Library �� ActionBar ====================================================
	// �Ѧ��n�����O�� http://oldgrayduck.blogspot.tw/2013/10/android-support-library-actionbar.html
	
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
	    Toast.makeText(this, "���F �M�� �s", Toast.LENGTH_LONG).show();
	}
	  
	public void openEdit()
	{
	    Toast.makeText(this, "���F ��s  �s", Toast.LENGTH_LONG).show();
	}
	
	// �ҥ� Support Library �� ActionBar ====================================================

	
}
