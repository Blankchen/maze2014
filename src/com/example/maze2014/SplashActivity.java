package com.example.maze2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.start_logo);
	        new Thread(new Runnable(){

	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	                try {
	                    Thread.sleep(3000);//�o��i�H���A�Q�w�����J�����
	                    //���U�������app���D�e��
	                    startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
	                } catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	            
	        }).start();
	    }

}
