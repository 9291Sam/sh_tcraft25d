package com.sebs.shtcraft_25d;

import java.awt.Color;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class UIManager implements Renderer.Entity{

    private Renderer renderer;
    private double timeAlive = 0.0;

    
    public UIManager(Renderer renderer) {
        this.renderer = renderer;
        
    }
    
    @Override
    public void tick(double deltaTime) {
        this.timeAlive += deltaTime;
    }

    @Override
    public void draw(DrawCallCollector d) {
        double CameraX = renderer.getCameraXWorld();
        double CameraY = renderer.getCameraYWorld();
        d.drawFilledRectangle(CameraX-4.9, CameraY-4.55, 2, 3, 0.45, Color.GRAY);
        d.drawFilledRectangle(CameraX-4.8, CameraY-4.65, 2, 2.0, 0.25, Color.RED);
    }

}
