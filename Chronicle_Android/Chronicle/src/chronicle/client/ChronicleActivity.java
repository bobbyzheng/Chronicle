package chronicle.client;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.Window;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import chronicle.client.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChronicleActivity extends Activity {
	String tags;
	String environmentOutputFile;
	SQLiteDatabase fileDataBase;
	String timeStamp;
	Camera mCamera;
	MediaRecorder mMediaRecorder;
	private boolean recording;
	FileWriter tagFile;
	private CameraPreview mPreview;
	int startTime;
	public CalendarAdapter adapter;
	PowerManager pm;
	PowerManager.WakeLock wl;
	ListView filesList;
	private Object tagList;
	
	//AlertDialog.Builder builder;
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//setContentView(R.layout.main);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		recording = false;
		tags="";
		if(!checkCameraHardware(this)) {
			Toast.makeText(getApplicationContext(), "No Camera", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main); 
		
		filesList = (ListView) findViewById(R.id.savedFiles);
		//filesList.setOnClickListener(new View.OnClickListener() {
		filesList.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String test = arg0.getItemAtPosition(arg2).toString();
				viewEventTags(test);
			}			
		});
		
		this.fileDataBase = this.openOrCreateDatabase("fileDB", MODE_PRIVATE, null);
	    this.fileDataBase.execSQL("CREATE TABLE IF NOT EXISTS table_of_dics(value TEXT)");
		this.update_list();
		Button button = (Button) findViewById(R.id.recordButton);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startPreview();
			}
		});	
	    
	}
	private void viewEventTags(String eventName){
		setContentView(R.layout.event_info); 
		
		Log.d("TESTARG", eventName);
		ListView tagList = (ListView) findViewById(R.id.taglist);
		ArrayList<String> tagArray = new ArrayList<String>();

		//FIGURE out how to put these tags into an array adapter, using something like 
		//		this.filesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db_results));
		//then populate the listview with the tags.
		//Then maybe make the leave a tag button work
		
		
		try{             
    			File tagFile = new File(Environment.getExternalStorageDirectory()+"/recordedMedia/"+eventName+".txt");

	           //File f = new File(Environment.getExternalStorageDirectory()+"test.txt");             
	           FileInputStream fileIS = new FileInputStream(tagFile);          
	           BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));           
	           String readString = new String();                

	           while ((readString = buf.readLine()) != null) {
					tagArray.add(readString);

	               Log.d("line: ", readString);   
	           }
	           
	           }
		catch (FileNotFoundException e) {          
	           e.printStackTrace();          
	        }
		catch (IOException e){             
	           e.printStackTrace();          
	    }    
		tagList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tagArray));

	}
	
	private void startPreview(){
		setContentView(R.layout.camera_preview); 
		mCamera = getCameraInstance();
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{   
			mCamera.setDisplayOrientation(90);
		}
		mCamera.setDisplayOrientation(90);
		mPreview = new CameraPreview(getApplicationContext(), mCamera);
		// mPreview.setDisplayOrientation(90);
		FrameLayout preview;
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		registerForContextMenu(preview);
		preview.addView(mPreview);
		
		final AlertDialog.Builder builderStart = new AlertDialog.Builder(ChronicleActivity.this);
		builderStart.setMessage("Start recording?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mCamera.stopPreview();

				if (prepareVideoRecorder()) {

					wl.acquire();
					Log.d("PREPARE", "SUCCESS");
					Toast.makeText(getApplicationContext(), "RECORDING", Toast.LENGTH_SHORT).show();
					mMediaRecorder.start();
					Calendar c = Calendar.getInstance(); 
					startTime = c.get(Calendar.HOUR)*3600 + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND);
					recording = true;
					Toast.makeText(getApplicationContext(), "RECORDING2", Toast.LENGTH_SHORT).show();

				} 
				else {
					Log.d("PREPARE", "FAILED");
					Toast.makeText(getApplicationContext(), "PREPARE FAILED", Toast.LENGTH_SHORT).show();
					// prepare didn't work, release the camera
					releaseMediaRecorder();
				}
			}
		})
		.setNegativeButton("Leave a tag", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AlertDialog.Builder tagAlert = new AlertDialog.Builder(ChronicleActivity.this);
				tagAlert.setTitle("Leave a tag");
				//tagAlert.setMessage("Message");
				// Set an EditText view to get user input 
				final EditText input = new EditText(ChronicleActivity.this);
				tagAlert.setView(input);
				tagAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						Calendar c = Calendar.getInstance(); 
						int currentTime = c.get(Calendar.HOUR)*3600 + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND);;
						tags=tags+value+":::"+"0000000000"+"\r\n";
					}
				});
				tagAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
				tagAlert.show();
				//tags=tags+"TESTING SAVING A TAG";
				//dialog.cancel();
			}
		});
		final AlertDialog.Builder builderStop = new AlertDialog.Builder(ChronicleActivity.this);
		builderStop.setMessage("Stop recording?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				wl.release();
				// stop recording and release camera
				writeTagFile(tags);
				mMediaRecorder.stop();  // stop the recording
				releaseMediaRecorder(); // release the MediaRecorder object
				mCamera.lock();         // take camera access back from MediaRecorder
				Toast.makeText(getApplicationContext(), "STOPPED RECORDING", Toast.LENGTH_SHORT).show();
				// inform the user that recording has stopped
				recording = false;
			}
		})
		.setNegativeButton("Leave a tag", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AlertDialog.Builder tagAlert = new AlertDialog.Builder(ChronicleActivity.this);
				tagAlert.setTitle("Leave a tag");
				Calendar c = Calendar.getInstance(); 
				final int currentTime = c.get(Calendar.HOUR)*3600 + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND);;

				//tagAlert.setMessage("Message");
				// Set an EditText view to get user input 
				final EditText input = new EditText(ChronicleActivity.this);
				tagAlert.setView(input);
				tagAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						tags=tags+value+":::"+Integer.toString(currentTime-startTime)+"\r\n";
					}
				});
				tagAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
				tagAlert.show();
				//dialog.cancel();
			}
		});

		/*
	     if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
	        {   
    	     preview = (FrameLayout) findViewById(R.id.camera_preview);
	        }
	     else{
    	     preview = (FrameLayout) findViewById(R.id.camera_preview2);

	     }
		 */

		preview.setOnTouchListener(
				new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int action = event.getActionMasked();
						if (action == MotionEvent.ACTION_DOWN){
							//registerForContextMenu(v);
							//mCamera.stopPreview();
							if (recording){
								//builderStop.create();
								builderStop.show();
							}
							else{
								//builderStart.create();
								builderStart.show();
							}
						}
						return true;
					}
				}
				);  
		
		//preview.addView(mPreview);
		Toast.makeText(getApplicationContext(), "BLAST PAST", Toast.LENGTH_LONG).show();


	}

	private boolean checkCameraHardware(Context context) {
		if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private void update_list() {
		//fileDataBase.execSQL("INSERT INTO table_of_dics(value) VALUES('TEST')");
		//fileDataBase.execSQL("INSERT INTO table_of_dics(value) VALUES('TEST3')");
		//fileDataBase.execSQL("INSERT INTO table_of_dics(value) VALUES('TES2T')");
		
		ArrayList<String> db_results = new ArrayList<String>();
		Cursor cursor = fileDataBase.rawQuery("SELECT value FROM table_of_dics ORDER BY value", null);
		while(cursor.moveToNext()) {
			db_results.add(String.valueOf(cursor.getString(cursor.getColumnIndex("value"))));
			//db_results.add(cursor.getColumnIndex("value"));
			//db_results.add("TEST");
		}
		cursor.close();
		this.filesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db_results));
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {			
			c = Camera.open();
		}
		catch(Exception e) {
			Log.e("Camera.open()", "Error getting camera with Camera.open(): "+e.getMessage());
		}
		return c;
	}

	private boolean prepareVideoRecorder(){
		environmentOutputFile = Environment.getExternalStorageDirectory().toString();
		File file = new File(Environment.getExternalStorageDirectory(), "/recordedMedia/");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating Image folder");
			}
		}
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
		/*
		try {
			FileWriter tagFile = new FileWriter(Environment.getExternalStorageDirectory()+"/recordedMedia/"+timeStamp+".txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 */
		// mCamera = getCameraInstance();
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.reset();
		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);
		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mMediaRecorder.setOutputFile(environmentOutputFile+"/recordedMedia/"+timeStamp+".mp4");
		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	protected void onPause() {
		super.onPause();
		releaseMediaRecorder();       // if you are using MediaRecorder, release it first
		releaseCamera();              // release the camera immediately on pause event
	}

	private void releaseMediaRecorder(){
		if (mMediaRecorder != null) {
			mMediaRecorder.reset();   // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock();           // lock camera for later use
		}
	}

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    private void writeTagFile(String tags){
    	try {
			Log.d("WRITETAG", "Attempting to write to tag file "+tags);
			

    		File tagFile = new File(Environment.getExternalStorageDirectory()+"/recordedMedia/"+timeStamp+".txt");
			FileWriter tagWriter = new FileWriter(tagFile);
    		//FileWriter tagFile = new FileWriter(Environment.getExternalStorageDirectory()+"/recordedMedia/"+timeStamp+".txt");
			tagWriter.write(tags);
			tagWriter.write(";IS THIS WRITING?");
			tagWriter.flush();
			tagWriter.close();
			Log.d("WRITETAG", "Supposedly successful write to tag file");
			fileDataBase.execSQL("INSERT INTO table_of_dics(value) VALUES('"+timeStamp+"')");

		} catch (IOException e1) {
			Log.d("WRITETAG", "IOException writing to tag file: " + e1.getMessage());

			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
}
	


