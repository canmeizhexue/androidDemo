package com.canmeizhexue.common.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;

/**蓝牙客户端,,判断顺序
 * 1.是否支持蓝牙
 * 2.是否打开蓝牙
 * 3.连接蓝牙
 * 4.通信
 * Created by silence on 2016/9/25.
 */
public class BluetoothClientActivity extends BaseActivity{
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG ="Bluetooth_activity";
    // Member object for the chat services
    private BluetoothChatService mChatService = null;



    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public ListenerThread StartListenThread;
    private EditText mEditText;
    private Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);

        mEditText = (EditText)findViewById(R.id.EditText1);
        sendBtn =(Button)findViewById(R.id.button1);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(mChatService.getState() == BluetoothChatService.STATE_CONNECTED)
                {
                    //发送数据
                    String sendContent = mEditText.getText().toString();
                    sendMessage(sendContent);
                }
            }
        });

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter==null)
        {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

        if(!mBluetoothAdapter.isEnabled())
        {

            //弹出对话框提示用户是后打开
            //Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enabler, REQUEST_ENABLE);

            //不做提示，强行打开
            mBluetoothAdapter.enable();
        }

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this,new Handler());
    }

    private void connectDevice(String address ) {
        // Get the device MAC address

        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, false);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.e(TAG, "+ ON RESUME +");

        String address = mBluetoothAdapter.getAddress();
        System.out.println(address);
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                // Start the Bluetooth chat services
                StartListenThread = new ListenerThread();
                StartListenThread.start();

            }
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            // mOutStringBuffer.setLength(0);
            //  mOutEditText.setText(mOutStringBuffer);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop the Bluetooth chat services
        if (mChatService != null)
            mChatService.stop();
        Log.e(TAG, "--- ON DESTROY ---");

    }
    //去连接蓝牙
    private class ListenerThread extends Thread {
        // The local server socket

        public void run()
        {
            // Listen to the server socket if we're not connected
            for(int i =0;i<100;i++)
            {
                //蓝牙是否开启
                if(mBluetoothAdapter.getState()== BluetoothAdapter.STATE_ON)
                {
                    //mChatService.start();
                    // 需要改为服务端蓝牙的mac地址
                    String address = "58:44:98:83:A7:78";//mBluetoothAdapter.getAddress();
                    System.out.println(address);
                    mEditText.setText(address);

                    if(mChatService.getState() != BluetoothChatService.STATE_CONNECTED)
                        connectDevice(address);
                    break;
                }
                else
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }
}
