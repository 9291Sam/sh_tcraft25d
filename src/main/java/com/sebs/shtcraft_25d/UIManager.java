package com.sebs.shtcraft_25d;

import java.awt.Color;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class UIManager implements Renderer.Entity
{
    private Renderer renderer;
    
    public UIManager(Renderer renderer)
    {
        this.renderer = renderer;   
    }
    
    @Override
    public void tick(double deltaTime) {}

    @Override
    public void draw(DrawCallCollector d)
    {
    	// TODO: do dynamic things
    	d.drawFilledRectangleScreen(0.0, 0.95, 0, 0.25, 0.1, Color.GRAY);
    	d.drawFilledRectangleScreen(0.025, 0.9675, 1, 0.2, 0.025, Color.RED);
    }

}
