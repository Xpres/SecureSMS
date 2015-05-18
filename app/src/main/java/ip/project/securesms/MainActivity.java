package ip.project.securesms;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends ActionBarActivity {
    Button sendB;
    EditText editnumber;
    EditText editmsg;
    EditText editKey;
    SmsManager smsManager;
    BluetoothAdapter BA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendB = (Button)findViewById(R.id.send_b);
        editnumber = (EditText)findViewById(R.id.sms_nmb);
        editmsg = (EditText)findViewById(R.id.sms_msg);
        editKey = (EditText)findViewById(R.id.sms_key);
        smsManager = SmsManager.getDefault();
        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA.isEnabled()) {
            Toast.makeText(this, "address : " + BA.getAddress() + " name :" + BA.getName(),
                    Toast.LENGTH_LONG).show();
            String address = BA.getAddress();
            String name = BA.getName();
        }

        sendB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager.sendTextMessage(editnumber.getText().toString()
                        , null, editmsg.getText().toString(), null, null);
//                Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
//                        Uri.parse("sms:333"));
//                smsIntent.putExtra("sms_body", "Press send to send me");
//                startActivity(smsIntent);
            }
        });



    }

    public void enableBT(View v){
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);
    }
    public void sendBT(View v) throws Exception{
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap object
        //byte[] b = baos.toByteArray();
        byte[] b = editmsg.getText().toString().getBytes("UTF-8");
        byte[] keyStart = "key".getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyStart);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();

// encrypt
        byte[] encryptedData = encrypt(key,b);
// decrypt
        byte[] decryptedData = decrypt(key,encryptedData);
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public void bTSetings(View v){
        Intent i = new Intent(this, BTSetings.class);
        i.putExtra("a", "ddddddddd");
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
