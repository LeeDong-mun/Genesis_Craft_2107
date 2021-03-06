package com.ideasolution.genesis_craft_2107;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.bluetooth.BleBluetooth;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.exception.OtherException;




public class MainActivity extends AppCompatActivity {

    private static String BLE_MAC_ADDRESS = "D4:36:39:94:29:9F";

    private ImageButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10 ,bleStatus, bleTouch;
    private Button btnReconnect, btnDisconnect;

    private BleDevice connectedBleDevice;

    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    int a = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        Log.e("on", "onCreate");
        initView();

       // bleCheck(bluetoothAdapter); // ?????????????????? ble??? ???????????? ???????????? ????????? ?????? ??????.



        // Ble ??????
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount( 2,5000)
                .setSplitWriteNum(20).connect(BLE_MAC_ADDRESS, new BleGattCallback()

            {
            @Override
            public void onStartConnect() {

                Log.e("GENESIS", "CONNECTED STARTED!  onCreate");

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

                Log.e("GENESIS", "connect fail onCreate: " + exception.toString());
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

                connectedBleDevice = bleDevice;
                Log.e("GENESIS", "CONNECTED! onCreate:: " + bleDevice.getName());
                Toast.makeText(MainActivity.this, "BLE ?????????????????????. onCreate", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_on));

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                Log.e("on", "onDisConnected onCreate");
                Toast.makeText(MainActivity.this, "BLE ????????? ??????????????????. onCreate", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));

                Log.e("onDisConnected","main a ??? ?????? : " +a);
                a = 1;
               reconnect();
              // show();

            }
        });

    }


    private void bleCheck(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            //??????????????? ???????????? ????????? ????????? ??????
            Toast.makeText(this, "??????????????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //?????? ???????????? ???
            if (!bluetoothAdapter.isEnabled()) {
                //???????????? ??????
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(i);
            }
        }
    }




    private void initView() {

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn10 = findViewById(R.id.btn10);

        bleStatus = findViewById(R.id.img_bleStatus);
        bleTouch = findViewById(R.id.img_touch);
        btnReconnect =findViewById(R.id.btnReconnect);
        //btnDisconnect = findViewById(R.id.btnDisconnect);


        bleTouch.setOnTouchListener((view, event) ->{

            if( a == 1){
                Log.e("bleStatus","?????? ?????? " +a);
                Toast.makeText(MainActivity.this, "?????? ??????!!", Toast.LENGTH_SHORT).show();
                a = 0;
                bleRe();
            }
             else {
                Log.e("bleStatus","?????? ??? " +a);
                Toast.makeText(MainActivity.this, "?????? ?????????!!", Toast.LENGTH_SHORT).show();

            }

            return false;

        });


        btn1.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x01);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x0b);
            }
            return false;
        });

        btn2.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x02);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x0c);
            }
            return false;
        });

        btn3.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x03);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x0d);
            }
            return false;
        });

        btn4.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x04);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x0e);
            }
            return false;
        });

        btn5.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x05);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x0f);
            }
            return false;
        });

        btn6.setOnTouchListener((view, event) ->{

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Log.e("GENESIS","BTN DOWN");
                send((byte)0x06);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("GENESIS","BTN UP");
                send((byte)0x10);
            }
            return false;
        });



        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reconnect();
                Toast.makeText(MainActivity.this, "?????? ?????????!!", Toast.LENGTH_SHORT).show();
                Log.e("bleStatus","?????? ??? " +a);
            }
        });


    }





    // ????????? ??????
    private void send(byte data) {
        BleManager.getInstance().write(
                connectedBleDevice,
                "0000ffe0-0000-1000-8000-00805f9b34fb",
                "0000ffe1-0000-1000-8000-00805f9b34fb",
                new byte[]{data},
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.e("GENESIS", "write Success : !");
                        //tvBleStatus.setText("Connect");
                    }


                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.e("GENESIS", "write Fail : !" + exception.getDescription());
                        //tvBleStatus.setText("Disconnect");
                    }
                }
        );
    }


    private void reconnect(){

        Log.e("on", "ReConnect Main");

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(2,5000)
                .setSplitWriteNum(20).connect(BLE_MAC_ADDRESS, new BleGattCallback()
        {
            @Override
            public void onStartConnect() {

                Log.e("GENESIS", "CONNECTED STARTED!");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

                Log.e("GENESIS", "connect fail : " + exception.toString());
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Log.e("GENESIS", "CONNECTED! :: " + bleDevice.getName());
                Toast.makeText(MainActivity.this, "BLE ?????????????????????. 2???", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_on));

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                Toast.makeText(MainActivity.this, "BLE ????????? ???????????????. 2???", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));

                a = 1;

                Log.e("onDisConnected","ReCon A ??? ?????? : " +a);
                reconnect();
               // show();

            }
        });
    }


    void show(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????? ?????? ?????? ??????");
        builder.setMessage("?????? ?????????!!");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // reconnect();
                Toast.makeText(MainActivity.this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }



    void bleRe(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BLE Reconnect");
        builder.setMessage("BLE ?????? ?????????.!!");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reconnect();
                Toast.makeText(MainActivity.this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    @Override
    protected void onDestroy() {

        Log.e("on", "onDestroy");
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        connectedBleDevice = null;

        Log.e("GENESIS","null");
    }



    // ???????????? ?????? ?????????
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}