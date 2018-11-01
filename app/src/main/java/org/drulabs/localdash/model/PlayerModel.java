package org.drulabs.localdash.model;

import java.util.List;

public class PlayerModel {
    private String name, ip;
    private int level, power, port;
    private List<CardModel> hand, itensInPlay;

    public PlayerModel() {
    }

    public PlayerModel(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.level = 1;
        this.power = 0;
    }

    public PlayerModel(String name, List<CardModel> hand) {
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

    public void print(){
        System.out.println("Name: " + this.name + ", IP: " + this.ip + ", PORT: " + this.port);
    }
}
