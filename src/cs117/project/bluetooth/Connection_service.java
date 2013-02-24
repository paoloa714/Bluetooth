package cs117.project.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Connection_service {

	private BluetoothAdapter mBluetoothAdapter;
	private static final String NAME = "BluetoothConnectionService";
	private static final UUID MY_UUID =
	        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private Context context;
	private manageThread mManageThread;
	private AcceptThread mAcceptThread;
	
	public Connection_service(){
		mAcceptThread = new AcceptThread();
	}
	
	public void start(){
		mAcceptThread.start();
	}
	
	private class AcceptThread extends Thread {
	    private final BluetoothServerSocket mmServerSocket;
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                mManageThread = new manageThread();
	                try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                break;
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class manageThread extends Thread {
		private static final String BROADCAST_ACTION = "Stuff";

		manageThread(){
			Intent it = new Intent();
			it.setAction(BROADCAST_ACTION);
			context.sendBroadcast(it);
			return;
		}
	}
}
