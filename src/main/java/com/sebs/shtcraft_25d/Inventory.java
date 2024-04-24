package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class Inventory implements Renderer.Entity{
	private static boolean rectangleVisible = true;
	private Renderer renderer;
    
    public Inventory(Renderer renderer)
    {
        this.renderer = renderer;   
    }
	@Override
	public void tick(double deltaTime) {
		
	}

	@Override
	public void draw(DrawCallCollector d) {
		d.drawFilledRectangleScreen(0.25, 0.95, 1, 0.05, 0.075, Color.BLACK);
		d.drawFilledRectangleScreen(0.3, 0.95, 0, 0.05, 0.075, Color.GRAY);
		d.drawFilledRectangleScreen(0.35, 0.95, 0, 0.05, 0.075, Color.BLACK);
		d.drawFilledRectangleScreen(0.4, 0.95, 0, 0.05, 0.075, Color.GRAY);
		d.drawFilledRectangleScreen(0.45, 0.95, 0, 0.05, 0.075, Color.BLACK);
		d.drawFilledRectangleScreen(0.5, 0.95, 0, 0.05, 0.075, Color.GRAY);
	}

}
