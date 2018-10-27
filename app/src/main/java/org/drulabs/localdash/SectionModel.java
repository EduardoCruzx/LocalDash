package org.drulabs.localdash;

import java.util.ArrayList;

public class SectionModel {
    private String name;
    private ArrayList<CardModel> allCardsInSection;

    public SectionModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CardModel> getAllCardsInSection() {
        return allCardsInSection;
    }

    public void setAllCardsInSection(ArrayList<CardModel> allCardsInSection) {
        this.allCardsInSection = allCardsInSection;
    }
}
