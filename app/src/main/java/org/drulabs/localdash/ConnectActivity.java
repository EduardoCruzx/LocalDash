package org.drulabs.localdash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConnectActivity extends AppCompatActivity {

    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start = (Button) findViewById(R.id.btn_match);
        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }
}
