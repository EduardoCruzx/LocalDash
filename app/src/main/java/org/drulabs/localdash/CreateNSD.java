package org.drulabs.localdash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.drulabs.localdash.db.DBAdapter;
import org.drulabs.localdash.model.DeviceDTO;
import org.drulabs.localdash.notification.NotificationToast;
import org.drulabs.localdash.nsddiscovery.NsdHelper;
import org.drulabs.localdash.transfer.DataHandler;
import org.drulabs.localdash.transfer.DataSender;
import org.drulabs.localdash.transfer.TransferConstants;
import org.drulabs.localdash.utils.ConnectionUtils;
import org.drulabs.localdash.utils.DialogUtils;
import org.drulabs.localdash.utils.Utility;

import java.util.ArrayList;

public class CreateNSD extends AppCompatActivity implements PeerListFragment.OnListFragmentInteractionListener {

    PeerListFragment deviceListFragment;
    NsdHelper mNsdHelper;
    View progressBarLocalDash;
    AppController appController = null;

    private DeviceDTO selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_dash_nsd);

        Button startMatch = findViewById(R.id.startMatch);
        startMatch.setVisibility(View.GONE);

        startMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muda para acitivity main
                // Envia mensagem para ir para activity main
            }
        });

        initialize();

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialize() {

        progressBarLocalDash = findViewById(R.id.progressBarLocalDash);

        String myIP = Utility.getWiFiIPAddress(CreateNSD.this);
        Utility.saveString(CreateNSD.this, TransferConstants.KEY_MY_IP, myIP);

//        connListener = new ConnectionListener(LocalDashNSD.this, ConnectionUtils.getPort
//                (LocalDashNSD.this));
//        connListener.start();
        appController = (AppController) getApplicationContext();
        appController.startConnectionListener();

        checkWritePermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_dash, menu);
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

    @Override
    protected void onPause() {
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localDashReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NsdHelper.BROADCAST_TAG);
        filter.addAction(DataHandler.DEVICE_LIST_CHANGED);
        filter.addAction(DataHandler.CHAT_REQUEST_RECEIVED);
        filter.addAction(DataHandler.CHAT_RESPONSE_RECEIVED);
        LocalBroadcastManager.getInstance(CreateNSD.this).registerReceiver(localDashReceiver,
                filter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DataHandler
                .DEVICE_LIST_CHANGED));

//        appController.startConnectionListener();
//        mNsdHelper.registerService(ConnectionUtils.getPort(LocalDashNSD.this));
    }

    @Override
    protected void onDestroy() {
        //mNsdHelper.tearDown();
        Utility.clearPreferences(CreateNSD.this);
        appController.stopConnectionListener();
        mNsdHelper.tearDown();
        mNsdHelper = null;
        DBAdapter.getInstance(CreateNSD.this).clearDatabase();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("DXDX", Build.MANUFACTURER + ": local dash NSD Stopped");
        super.onStop();
    }

    private BroadcastReceiver localDashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case NsdHelper.BROADCAST_TAG:
//                    NsdServiceInfo serviceInfo = intent.getParcelableExtra(NsdHelper
//                            .KEY_SERVICE_INFO);
//                    String[] serviceSplit = serviceInfo.getServiceName().split(":");
//                    String ip = serviceSplit[1];
//                    int port = Integer.parseInt(serviceSplit[2]);
//                    DataSender.sendCurrentDeviceData(LocalDashNSD.this, ip, port, true);
                    NsdServiceInfo serviceInfo = mNsdHelper.getChosenServiceInfo();
                    String ipAddress = serviceInfo.getHost().getHostAddress();
                    int port = serviceInfo.getPort();
                    DataSender.sendCurrentDeviceData(CreateNSD.this, ipAddress, port, true);
                    break;
                case DataHandler.DEVICE_LIST_CHANGED:
                    ArrayList<DeviceDTO> devices = DBAdapter.getInstance(CreateNSD.this)
                            .getDeviceList();
                    int peerCount = (devices == null) ? 0 : devices.size();
                    if (peerCount > 0) {
                        progressBarLocalDash.setVisibility(View.GONE);
                        deviceListFragment = new PeerListFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(PeerListFragment.ARG_DEVICE_LIST, devices);
                        deviceListFragment.setArguments(args);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.deviceListHolder, deviceListFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                        ft.commit();
                    }
                    break;
                case DataHandler.CHAT_REQUEST_RECEIVED:
                    DeviceDTO chatRequesterDevice = (DeviceDTO) intent.getSerializableExtra(DataHandler
                            .KEY_CHAT_REQUEST);
                    //showChatRequestedDialog(chatRequesterDevice);
                    DialogUtils.getChatRequestDialog(CreateNSD.this, chatRequesterDevice).show();
                    break;
                    // Não recebe response, só envia
//                case DataHandler.CHAT_RESPONSE_RECEIVED:
//                    boolean isChatRequestAccepted = intent.getBooleanExtra(DataHandler
//                            .KEY_IS_CHAT_REQUEST_ACCEPTED, false);
//                    if (!isChatRequestAccepted) {
//                        NotificationToast.showToast(CreateNSD.this, "Chat request " +
//                                "rejected");
//                    } else {
//                        DeviceDTO chatDevice = (DeviceDTO) intent.getSerializableExtra(DataHandler
//                                .KEY_CHAT_REQUEST);
//                        DialogUtils.openChatActivity(CreateNSD.this, chatDevice);
//                        NotificationToast.showToast(CreateNSD.this, chatDevice
//                                .getPlayerName() + "Accepted Chat request");
//                    }
//                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DialogUtils.CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    DataSender.sendFile(CreateNSD.this, selectedDevice.getIp(),
                            selectedDevice.getPort(), imageUri);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(ConnectActivity.WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(ConnectActivity.WRITE_PERMISSION, ConnectActivity
                    .WRITE_PERM_REQ_CODE, this);
        }
    }

    @Override
    public void onListFragmentInteraction(DeviceDTO deviceDTO) {
        NotificationToast.showToast(CreateNSD.this, deviceDTO.getDeviceName() + " clicked");
        selectedDevice = deviceDTO;
//        showServiceSelectionDialog();
        DialogUtils.getServiceSelectionDialog(CreateNSD.this, deviceDTO);
    }
}