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

       // bleCheck(bluetoothAdapter); // 스캔하기전에 ble를 지원하는 기기인지 확인을 해야 한다.



        // Ble 연결
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
                Toast.makeText(MainActivity.this, "BLE 연결되었습니다. onCreate", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_on));

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                Log.e("on", "onDisConnected onCreate");
                Toast.makeText(MainActivity.this, "BLE 연결이 끊어졌습니다. onCreate", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));

                Log.e("onDisConnected","main a 값 상태 : " +a);
                a = 1;
               reconnect();
              // show();

            }
        });

    }


    private void bleCheck(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            //블루투스를 지원하지 않으면 장치를 끈다
            Toast.makeText(this, "블루투스를 지원하지 않는 장치입니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //연결 안되었을 때
            if (!bluetoothAdapter.isEnabled()) {
                //블루투스 연결
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
                Log.e("bleStatus","다시 연결 " +a);
                Toast.makeText(MainActivity.this, "다시 연결!!", Toast.LENGTH_SHORT).show();
                a = 0;
                bleRe();
            }
             else {
                Log.e("bleStatus","연결 됨 " +a);
                Toast.makeText(MainActivity.this, "연결 되있음!!", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MainActivity.this, "연결 되있음!!", Toast.LENGTH_SHORT).show();
                Log.e("bleStatus","연결 됨 " +a);
            }
        });


    }





    // 데이터 전송
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
                Toast.makeText(MainActivity.this, "BLE 연결되었습니다. 2차", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_on));

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                Toast.makeText(MainActivity.this, "BLE 연결이 끊겼습니다. 2차", Toast.LENGTH_SHORT).show();
                bleStatus.setImageDrawable(getResources().getDrawable(R.drawable.blue_off));

                a = 1;

                Log.e("onDisConnected","ReCon A 값 상태 : " +a);
                reconnect();
               // show();

            }
        });
    }


    void show(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루 투스 연결 끊김");
        builder.setMessage("어플 재실행!!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // reconnect();
                Toast.makeText(MainActivity.this, "확인 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }



    void bleRe(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BLE Reconnect");
        builder.setMessage("BLE 연결 합니다.!!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reconnect();
                Toast.makeText(MainActivity.this, "확인 되었습니다.", Toast.LENGTH_SHORT).show();
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



    // 전체화면 사용 설정창
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