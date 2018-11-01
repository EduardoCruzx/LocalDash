package org.drulabs.localdash.model;

import java.util.List;

public class PlayerModel {
    private String name;
    private int level, power;
    private List<CardModel> hand, itensInPlay;

    public PlayerModel() {
    }

    public PlayerModel(String name, List<CardModel> hand) {
        this.name = name;
        this.hand = hand;
        this.level = 1;
        this.power = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public List<CardModel> getHand() {
        return hand;
    }

    public void setHand(List<CardModel> hand) {
        this.hand = hand;
    }

    public List<CardModel> getItensInPlay() {
        return itensInPlay;
    }

    public void setItensInPlay(List<CardModel> itensInPlay) {
        this.itensInPlay = itensInPlay;
    }

    public void addItemsToHand(List<CardModel> rewards){
        this.hand.addAll(rewards);
    }

    public void levelChange(int level){
        this.level = this.level + level;
    }
}
