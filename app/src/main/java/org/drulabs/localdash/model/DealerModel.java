package org.drulabs.localdash.model;

import org.drulabs.localdash.db.DBAdapter;

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
        List<CardModel> rewards = new ArrayList<>();
        for (int i = 0; i < enemyDefeated.getReward(); i++){
            rewards.add(itensDeck.remove(rand.nextInt(itensDeck.size())));
        }
        return rewards;
    }

    public CardModel equipItem(CardModel item){
        return item;
    }

    public void kill(CardModel enemy, PlayerModel player){
        if (player.getPower() + player.getLevel() > enemy.getPower()){
            player.addItemsToHand(getReward(enemy));
            player.levelChange(1);
        }else{
            player.levelChange(enemy.getBadstuff());
        }
    }

    public void startGame(){
        itensDeck = new ArrayList<>();
        enemyDeck = new ArrayList<>();
        itensDeck.addAll(dbAdapter.GET_ITEMS());
        enemyDeck.addAll(dbAdapter.GET_ENEMIES());
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
}
