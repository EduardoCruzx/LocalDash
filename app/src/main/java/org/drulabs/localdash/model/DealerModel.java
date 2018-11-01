package org.drulabs.localdash.model;

import android.os.Bundle;

import org.drulabs.localdash.MainActivity;
import org.drulabs.localdash.db.DBAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DealerModel {
    private List<CardModel> itensDeck, enemyDeck;
    private List<PlayerModel> turnOrder;
    private PlayerModel turn;
    private Random rand = new Random();
    private DBAdapter dbAdapter = null;

    public DealerModel() {
    }

    public DealerModel(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    public CardModel kickTheDoor(){
        return enemyDeck.remove(rand.nextInt(enemyDeck.size()));
    }

    public List<CardModel> getReward(CardModel enemyDefeated){
        System.out.println("REWARD" + enemyDefeated.getReward());
        List<CardModel> rewards = new ArrayList<>();
        for (int i = 0; i < enemyDefeated.getReward(); i++){
            rewards.add(itensDeck.remove(rand.nextInt(itensDeck.size())));
        }
        return rewards;
    }

    public int equipItem(CardModel item, PlayerModel player){
        int pos = player.addItemToTable(item);
        updatePlayerPower(player);
        player.print();
        return pos;
    }

    public boolean kill(CardModel enemy, PlayerModel player){
        if (player.getPower() + player.getLevel() > enemy.getPower()){
            player.addItemsToHand(getReward(enemy));
            player.levelChange(1);
            System.out.println("WIN");
            return true;
        }else{
            System.out.println("BADSTUFF");
            player.levelChange(enemy.getBadstuff());
            return false;
        }
    }

    public void startGame(Bundle extras, PlayerModel me){
        itensDeck = new ArrayList<>();
        enemyDeck = new ArrayList<>();
        turnOrder = new ArrayList<>();
        itensDeck.addAll(dbAdapter.GET_ITEMS());
        enemyDeck.addAll(dbAdapter.GET_ENEMIES());
        //PlayerModel p = new PlayerModel(extras.getString(MainActivity.KEY_CHATTING_WITH), extras.getString(MainActivity.KEY_CHAT_IP), extras.getInt(MainActivity.KEY_CHAT_PORT));
        PlayerModel p = new PlayerModel("Dudu", "10.1.1.103", 42416);
        p.print();
        me.print();
        me.addItemToHand(itensDeck.remove(rand.nextInt(itensDeck.size())));
        me.addItemToHand(itensDeck.remove(rand.nextInt(itensDeck.size())));
        p.addItemToHand(itensDeck.remove(rand.nextInt(itensDeck.size())));
        turnOrder.add(me);
        turnOrder.add(p);
//        turn = turnOrder.get(rand.nextInt(turnOrder.size()));
        turn = me;
    }

    private PlayerModel changeTurn(PlayerModel current){
        int index = turnOrder.indexOf(current) + 1;
        turn = turnOrder.get(index % turnOrder.size());
        return turn;
    }

    private boolean checkWinner(PlayerModel player){
        if (player.getLevel() == 10)
            return true;
        else
            return false;
    }

    public void updatePlayerPower(PlayerModel player){
        int power = 0;
        for (CardModel item : player.getItensInPlay()) {
            power += item.getBonus();
        }
        player.setPower(power);
    }

    public List<PlayerModel> getTurnOrder() {
        return turnOrder;
    }

    public PlayerModel getTurn() {
        return turn;
    }
}
