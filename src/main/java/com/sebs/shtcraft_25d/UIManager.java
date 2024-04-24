package com.sebs.shtcraft_25d;

import java.awt.Color;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class UIManager implements Renderer.Entity
{
    private Renderer renderer;
    private double hp;
    
    public UIManager(Renderer renderer)
    {
        this.renderer = renderer;
        hp = 1.0;
    }
    
    @Override
    public void tick(double deltaTime) {}

    @Override
    public void draw(DrawCallCollector d)
    {
    	// TODO: do dynamic things
    	d.drawFilledRectangleScreen(0.0, 0.95, 20, 0.25, 0.1, Color.GRAY);
    	d.drawFilledRectangleScreen(0.01, 0.96, 21, (0.23 * hp), 0.035, Color.RED);
    }
    
    public double hit() {
        return hp-= 0.25;
    }
    
    public double regen() {
         return hp += 0.001;
    }

}
