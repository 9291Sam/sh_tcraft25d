package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Inventory implements Renderer.Entity{
	private static boolean rectangleVisible = true;
	private Image blaster1;
	private Image blaster2;
	private Image blaster3;
	private Image level1;
	private Image level2;
	private Image level3;
	private Image gold1;
	private Image gold2;
	private Image gold3;
	private Renderer renderer;
    
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
    }
	@Override
	public void tick(double deltaTime) {
		
	}

	@Override
	public void draw(DrawCallCollector d) {
		d.drawFilledRectangleScreen(0.90, 0.90, 1, 0.1, 0.1, Color.GRAY);
		d.drawFilledRectangleScreen(0.9, 0.8, 1, 0.1, 0.1, Color.WHITE);
		d.drawTexturedRectangleScreen(0.77, 0.77, 2, 0.35, 0.35, this.blaster3);
		d.drawTexturedRectangleScreen(0.8, 0.9, 2, 0.1, 0.1, this.level2);
		d.drawTexturedRectangleScreen(0.9, 0.8, 2, 0.1, 0.1, this.gold1);

	}

}
