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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ChronicleActivity extends Activity {
	String tags;
	String environmentOutputFile;
	String timeStamp;
	Camera mCamera;
	MediaRecorder mMediaRecorder;
	private boolean recording;
	FileWriter tagFile;
	private CameraPreview mPreview;
	public CalendarAdapter adapter;
	PowerManager pm;
	PowerManager.WakeLock wl;
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

		Button button = (Button) findViewById(R.id.recordButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startPreview();
				// Perform action on click
			}
		});
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
				if (prepareVideoRecorder()) {
					wl.acquire();
					Log.d("PREPARE", "SUCCESS");
					Toast.makeText(getApplicationContext(), "RECORDING", Toast.LENGTH_SHORT).show();
					mMediaRecorder.start();
					recording = true;
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
				dialog.cancel();
			}
		});
		final AlertDialog.Builder builderStop = new AlertDialog.Builder(ChronicleActivity.this);
		builderStop.setMessage("Stop recording?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				wl.release();
				// stop recording and release camera
				//writeTagFile(tags);
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
				dialog.cancel();
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

		//	     PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		//     final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		preview.setOnTouchListener(
				new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int action = event.getActionMasked();
						if (action == MotionEvent.ACTION_DOWN){
							registerForContextMenu(v);
							//mCamera.stopPreview();builder = new AlertDialog.Builder(this);
							

							if (recording){
								builderStop.create();
								builderStop.create();
							}
							else{
								builderStart.create();
								builderStart.show();
							}
						}
						return false;
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
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
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
			FileWriter tagFile = new FileWriter(Environment.getExternalStorageDirectory()+"/recordedMedia/"+timeStamp+".txt");
			tagFile.write(tags);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
}
	


