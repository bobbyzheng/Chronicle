package chronicle.client;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChronicleActivity extends Activity {
	String environmentOutputFile;
	Camera mCamera;
	MediaRecorder mMediaRecorder;
	private boolean recording;
	private CameraPreview mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recording = false;
		setContentView(R.layout.main); 
		if(!checkCameraHardware(this)) {
			Toast.makeText(getApplicationContext(), "No Camera", Toast.LENGTH_LONG).show();
			finish();
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		 mCamera = getCameraInstance();
	     mPreview = new CameraPreview(this, mCamera);
	     FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	     preview.addView(mPreview);
	     preview.setOnTouchListener(
	        		new View.OnTouchListener() {
	        			@Override
	        			public boolean onTouch(View v, MotionEvent event) {
	        				mCamera.stopPreview();
	        				if (recording) {
	        	                // stop recording and release camera
	        	                mMediaRecorder.stop();  // stop the recording
	        	                releaseMediaRecorder(); // release the MediaRecorder object
	        	                mCamera.lock();         // take camera access back from MediaRecorder
	        	        		Toast.makeText(getApplicationContext(), "STOPPED RECORDING", Toast.LENGTH_SHORT).show();
	        	                // inform the user that recording has stopped
	        	                recording = false;
	        	            } 
	        				else {
	        	                // initialize video camera
	        	                if (prepareVideoRecorder()) {
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
	   
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
	    mMediaRecorder.setOutputFile(environmentOutputFile+"/"+timeStamp+".mp4");
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
    
    
}
	


