package org.drulabs.localdash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.drulabs.localdash.db.DBAdapter;
import org.drulabs.localdash.db.DBHelper;
import org.drulabs.localdash.notification.NotificationToast;
import org.drulabs.localdash.transfer.TransferConstants;
import org.drulabs.localdash.utils.ConnectionUtils;
import org.drulabs.localdash.utils.Utility;

public class ConnectActivity extends AppCompatActivity {
    public static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_PERM_REQ_CODE = 19;

    private Button start;
    private EditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etUsername = findViewById(R.id.player_name);
        String userNameHint = getString(R.string.enter_name_hint) + "(default = " + Build
                .MANUFACTURER + ")";
        etUsername.setHint(userNameHint);

        start = findViewById(R.id.btn_match);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startNSD(v); }
        });

        checkWritePermission();
    }

    public void startNSD(View v) {
        if (Utility.isWifiConnected(ConnectActivity.this)) {
            saveUsername();
            Intent nsdIntent = new Intent(ConnectActivity.this, LocalDashNSD.class);
            startActivity(nsdIntent);
            finish();
        } else {
            NotificationToast.showToast(ConnectActivity.this, getString(R.string
                    .wifi_not_connected_error));
        }
    }

    private void saveUsername() {
        String userName = etUsername.getText().toString();
        if (userName != null && userName.trim().length() > 0) {
            Utility.saveString(ConnectActivity.this, TransferConstants.KEY_USER_NAME, userName);
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(WRITE_PERMISSION, WRITE_PERM_REQ_CODE, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            NotificationToast.showToast(ConnectActivity.this, "This permission is needed for " +
                    "file sharing. But Whatever, if that's what you want...!!!");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBAdapter.getInstance(ConnectActivity.this).clearDatabase();
    }
}
