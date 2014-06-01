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
	// �O���Q��ܪ������Х�
	private int mCurrentMenuItemPosition = -1;
	// ��涵��
	public static final String[] MENU_ITEMS = new String[]{
	    "Item1", "Item2", "Item3", "Item4", "Item5"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	    //��� Up Button (��b Logo �����䪺���s�ϥ�)
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    //���} Up Button ���I���\�� 
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
	    // �w�q�s�ŧi����Ӫ���G�ﶵ�M�檺 ListView �H�� Drawer���e�� LinearLayou
	    mLsvDrawerMenu = (ListView) findViewById(R.id.lsv_drawer_menu);
	    mLlvDrawerContent = (LinearLayout) findViewById(R.id.llv_left_drawer);
	 
	    // ��M��ﶵ���l����Q�I���ɭn�����ʧ@
	    mLsvDrawerMenu.setOnItemClickListener(new OnItemClickListener() {
	 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
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
	
	private void selectMenuItem(int position) {
	    mCurrentMenuItemPosition = position;
	 
	    // �N��檺�l����]�w���Q��ܪ����A
	    mLsvDrawerMenu.setItemChecked(position, true);
	 
	    // ���� Drawer
	    mDrawerLayout.closeDrawer(mLlvDrawerContent);
	}

	

}
