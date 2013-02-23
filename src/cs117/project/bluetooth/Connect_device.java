package cs117.project.bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class Connect_device extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, "Connecting device", Toast.LENGTH_SHORT).show();
		
		//setup broadcast reciever
		setContentView(R.layout.activity_connect_device);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connect_device, menu);
		return true;
	}

}
