package cs117.project.bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1,
							 REQUEST_DEVICE_ADDRESS = 2,
							 CONNECT_DEVICE = 3;
	
	private BluetoothAdapter mBTadapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
   		mBTadapter = BluetoothAdapter.getDefaultAdapter();

   		// 	on creation of activity initialize bluetooth on.
   		if(mBTadapter != null){
       		if(!mBTadapter.isEnabled()){
       			Intent enableBT_it = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       			startActivityForResult(enableBT_it , REQUEST_ENABLE_BT);
       		}}
   		else{
   			//device does not support bluetooth;
   			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
   		}
   		
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void find_devices(View view){ // "connect" button is pressed, this is assigned in activity_main.xml
    	Intent fd_intent = new Intent(this, Find_Devices.class);
    	startActivityForResult(fd_intent , REQUEST_DEVICE_ADDRESS);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        	case REQUEST_DEVICE_ADDRESS:
        		String address = data.getExtras().getString(Find_Devices.EXTRA_DEVICE_ADDRESS);
        		Intent connect_it = new Intent(this, Connect_device.class);
        		startActivityForResult(connect_it, CONNECT_DEVICE);
        		break;
        	case CONNECT_DEVICE:
        		Toast.makeText(this, "Connected to device", Toast.LENGTH_LONG).show();
        		break;
        		
        }
    }
}
