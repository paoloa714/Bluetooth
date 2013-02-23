package cs117.project.bluetooth;

import java.util.Set;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class Find_Devices extends Activity {
	
	public static String EXTRA_DEVICE_ADDRESS = "device_addresses";
	private ArrayAdapter<String> mDevices;
	private BluetoothAdapter mBTadapter;

	@SuppressLint("NewApi")
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_devices);
		
		//set result to canceled in case user backs out of finding device
		setResult(Activity.RESULT_CANCELED);
		
		//initialize stuff
		mBTadapter = BluetoothAdapter.getDefaultAdapter();
		mDevices = new ArrayAdapter<String>(this , R.layout.device_name);
		
		//setup listview
		ListView devicenames = (ListView) findViewById(R.id.listView1);
		devicenames.setAdapter(mDevices);
		devicenames.setOnItemClickListener(mDeviceClickListener);
		
		// make sure device can be discovered for 2 mins
   		if (mBTadapter.getScanMode() !=
   	            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
   	            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
   	            // discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION , 300); //uncomment if you want to set discoverable time window
   	            startActivity(discoverableIntent);
   	        }
		
		
		//find bluetooth devices when you press the button
		Button scan = (Button) findViewById(R.id.scan_button);
		scan.setText("Scan Devices");
		scan.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				setTitle(R.string.scanning);
				
				// turns off discovery if already on
				if(mBTadapter.isDiscovering()){
					mBTadapter.cancelDiscovery();
				}
				
				//starts discovery again (is on for 12 secs, asynchronous)
				mBTadapter.startDiscovery();
			}
		});
		
		//get already paired devices
		Set<BluetoothDevice> pairedDevices = mBTadapter.getBondedDevices();
		if(pairedDevices.size() > 0 ){
			for(BluetoothDevice device : pairedDevices){
				mDevices.add(device.getName() + "\n" + device.getAddress());
			}
		}
		
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
		
		
		// Show the Up button in the action bar.
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		getActionBar().setDisplayHomeAsUpEnabled(true);}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBTadapter != null) {
            mBTadapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			//when discovery finds a device add to adapter
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mDevices.add(device.getName() + "\n" + device.getAddress());
				
			}
			return;
		}
	};
	
	//when a device is clicked
	private final OnItemClickListener mDeviceClickListener = new OnItemClickListener(){
		 public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
	            // Cancel discovery because it's costly and we're about to connect
	            mBTadapter.cancelDiscovery();

	            // Get the device MAC address, which is the last 17 chars in the View
	            String info = ((TextView) v).getText().toString();
	            String address = info.substring(info.length() - 17);

	            // Create the result Intent and include the MAC address
	            Intent intent = new Intent();
	            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

	            // Set result and finish this Activity
	            setResult(Activity.RESULT_OK, intent);
	            finish();
	        }
		
	};

}
