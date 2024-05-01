package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Inventory implements Renderer.Entity {
    private Renderer renderer;
    private Image blaster1;
    private Image blaster2;
    private Image blaster3;
    private Image level1;
    private Image level2;
    private Image level3;
    private Image gold1;
    private Image gold2;
    private Image gold3;
    private double lvMulti; // Level multiplier
    private int currentGold; // Current amount of gold
    private double currentMulti; // Current multiplier

    // Constructor
    public Inventory(Renderer renderer) {
        this.renderer = renderer;
        // Load images for inventory items
        this.blaster1 = Utils.loadImage("SBB.png");
        this.blaster2 = Utils.loadImage("blaster.png");
        this.blaster3 = Utils.loadImage("rayGun.png");
        this.level1 = Utils.loadImage("Level-1.png");
        this.level2 = Utils.loadImage("level-2.png");
        this.level3 = Utils.loadImage("level-3.png");
        this.gold1 = Utils.loadImage("goldSmall.png");
        this.gold2 = Utils.loadImage("GM.jpg");
        this.gold3 = Utils.loadImage("goldBig.png");
        this.lvMulti = 0.1; // Initialize level multiplier
        this.currentMulti = 0; // Initialize current multiplier
    }

    @Override
    public void tick(double deltaTime) {
        // Inventory doesn't update over time, so tick method is empty
    }

    @Override
    public void draw(DrawCallCollector d) {
        // Draw inventory elements
        
        // Background squares
        d.drawFilledRectangleScreen(0.90, 0.90, 1, 0.1, 0.1, Color.GRAY);
        d.drawFilledRectangleScreen(0.9, 0.8, 1, 0.1, 0.1, Color.WHITE);
        
        // Determine which images to draw based on current gold amount
        if (currentGold < 10) {
            lvMulti = 0.1; // Set level multiplier
            // Draw images for inventory items
            d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster1);
            d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level1);
            d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold1);
        } else if (currentGold < 100) {
            lvMulti = 0.01; // Set level multiplier
            // Draw images for inventory items
            d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster2);
            d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level2);
            d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold2);
        } else {
            // Draw images for inventory items
            d.drawTexturedRectangleScreen(0.77, 0.77, 2, 0.35, 0.35, this.blaster3);
            d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level3);
            d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold3); 
        }
        
        // Progress bar
        currentMulti = getCurrentMulti(lvMulti); // Calculate current multiplier
        d.drawFilledRectangleScreen(0.8, 0.75, 1, 0.2, 0.05, Color.BLACK); // Background of progress bar
        d.drawFilledRectangleScreen(0.8125, 0.763, 2, 0.175 * currentMulti, 0.025, Color.BLUE); // Actual progress bar
    }
    
    // Method to add coins to inventory
    public void addCoins(int num) {
        this.currentGold += num; // Increase current gold amount
    }
    
    // Method to calculate current multiplier based on current gold amount
    private double getCurrentMulti(double currentMulti) {
        double num = 0;
        if (currentMulti == 0.1) {
            num = Utils.map(currentGold, 0.0, 10.0, 0.0, 1.0);
        } else if (currentMulti == 0.01) {
            num = Utils.map(currentGold, 10.0, 100.0, 0.0, 1.0);
        } else {
            num = 1; // Maximum multiplier
        }
        return num;
    }
}
