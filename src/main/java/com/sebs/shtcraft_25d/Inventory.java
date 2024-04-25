package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

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
	private double lv1Multi;
	private double lv2Multi;
	private int count;
	private double currentMulti;

    public Inventory(Renderer renderer)
    {
        this.renderer = renderer;
        this.blaster1 = Utils.loadImage("SBB.png");
        this.blaster2 = Utils.loadImage("blaster.png");
        this.blaster3 = Utils.loadImage("rayGun.png");
        this.level1 = Utils.loadImage("Level-1.png");
        this.level2 = Utils.loadImage("level-2.png");
        this.level3 = Utils.loadImage("level-3.png");
        this.gold1 = Utils.loadImage("goldSmall.png");
        this.gold2 = Utils.loadImage("GM.jpg");
        this.gold3 = Utils.loadImage("goldBig.png");
        this.lv1Multi = 0.1;
        this.lv2Multi = 0.01;
        this.currentMulti = 0;
    }
	@Override
	public void tick(double deltaTime) {
		count ++;
	}

	@Override
	public void draw(DrawCallCollector d) {
		//background squares
		d.drawFilledRectangleScreen(0.90, 0.90, 1, 0.1, 0.1, Color.GRAY);
		d.drawFilledRectangleScreen(0.9, 0.8, 1, 0.1, 0.1, Color.WHITE);
		if (count < 100000) {
			d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster1);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level1);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold1);
		} else if (count < 1000000) {
			d.drawTexturedRectangleScreen(0.9, 0.9, 2, 0.1, 0.1, this.blaster2);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level2);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold2);
		} else {
			d.drawTexturedRectangleScreen(0.77, 0.77, 2, 0.35, 0.35, this.blaster3);
			d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level3);
			d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold3); 
		}
		//porgress bar
		double multi = getMulti(count);
		currentMulti += multi;
		System.out.println(currentMulti);
		d.drawFilledRectangleScreen(0.8, 0.75, 1, 0.2, 0.05, Color.BLACK);
	    d.drawFilledRectangleScreen(0.8125, 0.763, 2, 0.175 * multi, 0.025, Color.BLUE);
		
	}
	/*
	public int setGold(int num) {
		 return num++;
	}
	public int getGold(int num) {
		return num;
	}*/
	private double getMulti (int num) {
		if ((num/10000) <=10) {
			return this.lv1Multi;
		} else if ((num/10000) <=100) {
			return this.lv2Multi;
		} else {
			return 1;
		}
	}

}
