package org.drulabs.localdash.model;

import java.io.Serializable;

public class CardModel implements Serializable {
    public static int ITEM = 0;
    public static int ENEMY = 1;

    private String name, description;
    private int id, bonus, available, power, badstuff, reward, type;

    public CardModel() {
    }

    public CardModel(int id, String name, int bonus) {
        this.id = id;
        this.name = name;
        this.bonus = bonus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBadstuff() {
        return badstuff;
    }

    public void setBadstuff(int badstuff) {
        this.badstuff = badstuff;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getAvailable() { return available; }

    public void setAvailable(int available) { this.available = available; }

    public int getPower() { return power; }

    public void setPower(int power) { this.power = power; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) { this.bonus = bonus; }
}
