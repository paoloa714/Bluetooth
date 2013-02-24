package cs117.project.bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.Toast;

public class Connect_device extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, "Connecting device", Toast.LENGTH_SHORT).show();
		
		 // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter("Stuff");
        this.registerReceiver(mReceiver, filter);
		
		//setup broadcast reciever
		setContentView(R.layout.activity_connect_device);
		
         // Set result and finish this Activity
         setResult(Activity.RESULT_OK);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connect_device, menu);
		return true;
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			//when discovery finds a device add to adapter
			if(action.equals("Stuff")){
				Toast.makeText(context, "device connected", Toast.LENGTH_LONG).show();
				
				Intent it = new Intent();

		        // Set result and finish this Activity
		        finish();
			}
			return;
		}
	};

}
