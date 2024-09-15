package entity;

import battle.Ability;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import entity.Player;


public class Pokemon {
    private String name;
    private int maxHitpoints;
    private int currentHitpoints;
    private String imagePath;
    private Image image;
    private List<Ability> abilities;

    public Pokemon(String name, int maxHitpoints, String imagePath) {
        this.name = name;
        this.maxHitpoints = maxHitpoints;
        this.currentHitpoints = maxHitpoints;
        this.imagePath = imagePath;
        this.abilities = new ArrayList<>();
        loadImage();
    }

    private void loadImage() {
        java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);
        if (imgURL != null) {
            image = new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find image: " + imagePath);
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public int getCurrentHitpoints() {
        return currentHitpoints;
    }

    public Image getImage() {
        return image;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public void applyDamage(int damage) {
        currentHitpoints -= damage;
        if (currentHitpoints < 0) {
            currentHitpoints = 0;
        }
    }

    public boolean isFainted() {
        return currentHitpoints <= 0;
    }
}
