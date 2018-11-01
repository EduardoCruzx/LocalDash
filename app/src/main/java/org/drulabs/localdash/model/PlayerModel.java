package org.drulabs.localdash.model;

import android.os.Parcelable;

import org.drulabs.localdash.SectionModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerModel implements Serializable {
    private String name, ip;
    private int level, power, port;
    private ArrayList<CardModel> hand, itensInPlay;

    public PlayerModel() {
    }

    public PlayerModel(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.level = 1;
        this.power = 0;
        this.itensInPlay = new ArrayList<>();
        this.hand = new ArrayList<>();
    }

    public PlayerModel(String name, ArrayList<CardModel> hand) {
        this.name = name;
        this.hand = hand;
        this.level = 1;
        this.power = 0;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public ArrayList<CardModel> getHand() {
        return hand;
    }

    public void setHand(ArrayList<CardModel> hand) {
        this.hand = hand;
    }

    public void setHand(CardModel card) {
        ArrayList<CardModel> hand = new ArrayList<>();
        hand.add(card);
        this.hand = hand;
    }

    public ArrayList<CardModel> getItensInPlay() {
        return itensInPlay;
    }

    public void setItensInPlay(ArrayList<CardModel> itensInPlay) {
        this.itensInPlay = itensInPlay;
    }

    public void addItemsToHand(List<CardModel> rewards){
        this.hand.addAll(rewards);
    }

    public void addItemToHand(CardModel reward){
        this.hand.add(reward);
    }

    public int addItemToTable(CardModel item){
        int pos = hand.indexOf(item);
        this.itensInPlay.add(hand.remove(pos));
        return pos;
    }

    public void levelChange(int level){
        this.level = this.level + level;
        if(this.level < 1)
            this.level = 1;
    }

    public void print(){
        System.out.println("Name: " + this.name + ", Power: " + (this.getPower() + this.getLevel()) + ", Level: " + this.getLevel());
    }
}
