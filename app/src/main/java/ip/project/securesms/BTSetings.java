package ip.project.securesms;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BTSetings extends ActionBarActivity {
    EditText bTName;
    TextView discoverable;
    BluetoothAdapter ba;
    BroadcastReceiver discoveryMonitor;
    private ArrayList<BluetoothDevice> deviceList =
            new ArrayList<BluetoothDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btsetings);

        discoveryMonitor = new BroadcastReceiver() {
            public String dStarted = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
            public String dFinished = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
            @Override
            public void onReceive(Context context, Intent intent) {
                if (dStarted.equals(intent.getAction())) {
// Discovery has started.
                    Toast.makeText(BTSetings.this, "Discovery Started...", Toast.LENGTH_LONG).show();
                }
                else if (dFinished.equals(intent.getAction())) {
// Discovery has completed.
                    Toast.makeText(BTSetings.this, "Discovery Complete.", Toast.LENGTH_LONG).show();
                }
            }
        };
        BroadcastReceiver discoveryResult = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                BluetoothDevice remoteDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(remoteDevice);
                Toast.makeText(BTSetings.this, "Discovered " + remoteDeviceName, Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(discoveryMonitor,
                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(discoveryMonitor,
                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        registerReceiver(discoveryResult,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
        ba = BluetoothAdapter.getDefaultAdapter();
        bTName = (EditText)findViewById(R.id.bTName);
        bTName.setText(ba.getName());
        Toast.makeText(this, getIntent().getStringExtra("a"), Toast.LENGTH_LONG).show();
        discoverable = (TextView)findViewById(R.id.discoverable);
        if(ba.getScanMode() == ba.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            discoverable.setText("Yes");
        }else{
            discoverable.setText("No");
        }
    }
    public void enable(View v){
        BluetoothAdapter.getDefaultAdapter().enable();
    }
    public void disable(View v){
        BluetoothAdapter.getDefaultAdapter().disable();
    }

    public void saveSetings(View v){
        BluetoothAdapter.getDefaultAdapter().setName(bTName.getText().toString());
    }
    public void discoverable(View v){
        startActivityForResult(
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),
                1);
    }

    public void startDiscovery(View v){
            ba.startDiscovery();
    }

    public void discoverable2(View v){

    }

    public void discoverable3(View v){

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_btsetings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
