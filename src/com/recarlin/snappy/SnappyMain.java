/*
 * project		Snappy
 * package		com.recarlin.snappy
 * author		Russell Carlin
 * date			Jun 13, 2013
 */
package com.recarlin.snappy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SnappyMain extends Activity {
	
	private File image;
	private static String FILE_TYPE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snappy_main);
		
		Button newPic = (Button) findViewById(R.id.newPic);
		newPic.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/Snappy");
				if (! storageDir.exists()){
			        if (! storageDir.mkdirs()){
			            Log.d("DIRECTORY MAKE", "Failed to make directory.");
			        }
			    }
				String timeStamp = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new Date());
				String imageFileName =  "pic_" + timeStamp;
				try {
					image = File.createTempFile(imageFileName, FILE_TYPE, storageDir);
				} catch (IOException e) {
					Log.e("FILE CREATION", "There was an issue creating the file: " + imageFileName + "  " + storageDir);
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
				startActivityForResult(intent, 100);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snappy_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			MediaStore.Images.Media.insertImage(getContentResolver(), image.getAbsolutePath(), image.getName(), image.getName());
		} catch (FileNotFoundException e) {
			Log.e("GALLERY", "Could not find file to save to gallery.");
		}
	    
	    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    Notification noti = new Notification();
	    noti.defaults = Notification.DEFAULT_SOUND;
	    manager.notify(1, noti);
	}
}
