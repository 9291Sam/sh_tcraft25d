package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;


public class Inventory implements Renderer.Entity{
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
	private static double lvMulti;
	private int currentGold;
	private double currentMulti;


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
		//background squares
		d.drawFilledRectangleScreen(0.90, 0.90, 1, 0.1, 0.1, Color.GRAY);
		d.drawFilledRectangleScreen(0.9, 0.8, 1, 0.1, 0.1, Color.WHITE);
		if (currentGold < 10) {
			lvMulti = 0.1;
			d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster1);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level1);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold1);
		} else if (currentGold < 100) {
			lvMulti = 0.01;
			d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster2);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level2);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold2);
		} else {
			lvMulti = 1;
			d.drawTexturedRectangleScreen(0.77, 0.77, 2, 0.35, 0.35, this.blaster3);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level3);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold3); 
		}
		//porgress bar
		currentMulti = getCurrentMulti(lvMulti);
		d.drawFilledRectangleScreen(0.8, 0.75, 1, 0.2, 0.05, Color.BLACK);
	    d.drawFilledRectangleScreen(0.8125, 0.763, 2, 0.175 * currentMulti, 0.025, Color.BLUE);
		
	}
	
	public void addCoins(int num) {
		this.currentGold += num;
	}
	
	public static double getLevel() {
		try {
			if (lvMulti == 0.1) {
				return 1.0;
			} else if (lvMulti == 0.01) {
				return 3.0;
			} else if (lvMulti == 1){
				return 5.0;
			} 
			return 1.0;
		} catch (Exception e) {
			return 0.0;
		}
	}
	
	private double getCurrentMulti(double currentMulti) {
		double num = 0;
		if (currentMulti == 0.1) {
			num = Utils.map(currentGold, 0.0, 25.0, 0.0, 1.0);
		} else if (currentMulti == 0.01) {
			num = Utils.map(currentGold, 10.0, 100.0, 0.0, 1.0);
		} else {
			num = 1;
		}
		return num;
	}
	

}
