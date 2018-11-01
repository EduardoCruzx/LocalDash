package org.drulabs.localdash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.drulabs.localdash.model.CardModel;
import org.drulabs.localdash.model.DealerModel;
import org.drulabs.localdash.model.PlayerModel;

public class BattleActivity extends AppCompatActivity {

    private TextView enemyPower, playerPower, name, description, power, badstuff;
    private int reward;
    private Button kill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        enemyPower = findViewById(R.id.enemyPower);
        playerPower = findViewById(R.id.playerPower);
        kill = findViewById(R.id.btn_kill);
        name = findViewById(R.id.cardName);
        description = findViewById(R.id.cardDesc);
        power = findViewById(R.id.cardPower);
        badstuff = findViewById(R.id.cardBadStuff);


        Bundle extras = getIntent().getExtras();
        final CardModel enemy = (CardModel) extras.getSerializable("enemy");
        final PlayerModel player = (PlayerModel) extras.getSerializable("player");

        power.setText("Power: " + enemy.getPower());
        power.setTextSize(16);
        name.setText(enemy.getName());
        name.setTextSize(22);
        description.setText(enemy.getDescription());
        description.setTextSize(10);
        badstuff.setText("Bad stuff: " + enemy.getBadstuff() + " Rewards: " + enemy.getReward());

        enemyPower.setText("Enemy power: " + enemy.getPower());
        playerPower.setText("Player power: " + (player.getPower() + player.getLevel()));

        kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("player", player);
                intent.putExtra("enemy", enemy);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
