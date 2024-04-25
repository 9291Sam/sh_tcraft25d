package com.sebs.shtcraft_25d;


import java.awt.Image;
import java.util.Random;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WorldManager implements Renderer.Entity
{
    Renderer renderer;
    int widthTiles = 64;
    int heightTiles = 64;
    int centerX;
    int centerY;
    int width;
    int height;
    private Rectangle[] rectangles = new Rectangle[50];
    
    Tile[][] tileMap = new Tile[this.widthTiles][this.heightTiles];
    
    private Image stone;
    private Image grass;
    private Random random = new Random();
    private int modX;
    private int modY;
    
    public WorldManager(Renderer renderer_)
    {
        modX = 0;
        modY = 0;
        this.renderer = renderer_;  

        this.stone = Utils.loadImage("stone.png");

        this.grass = Utils.loadImage("grassTexure.png");
        
        for(int i = 0; i < rectangles.length; i++) {
            centerX = modX;
            centerY = modY;
            width = randomGen();
            height = randomGen();
            rectangles[i] = new Rectangle(centerX, centerY, width, height);
            modX = random.nextInt(83) - 43;
            modY = random.nextInt(83) - 43;
        }
    }

	
	private enum Tile
	{
		Stone,
		Grass,
	}
     
    @Override
    public void tick(double deltaTime) {}

    @Override
    public void draw(DrawCallCollector d) 
    {
        for (int i = -50; i < 50; ++i)
        {
            for (int j = -50; j < 50; ++j)
            {
                d.drawTexturedRectangleWorld((double)i, (double)j, -1, 1.01, 1.01, this.stone);
            }
        }
        
        for (Rectangle rect : rectangles) {
            drawRect(d, rect.centerX, rect.centerY, rect.width, rect.height);
        }
    }
    
    private int randomGen() {
        int rand = random.nextInt(7) + 10;
        return rand;
    }
    
    private void drawRect(DrawCallCollector d, int centerX, int centerY, int width, int height) {
        for (int i = centerX - width / 2; i < centerX + width / 2; ++i) {
            for (int k = centerY - height / 2; k < centerY + height / 2; ++k ) {
                d.drawTexturedRectangleWorld((double)i + 1, (double)k, 0, 1.01, 1.01, this.grass);
            }
        }
    }
    

    public static double clamp(double min, double max, double val) {
        return Math.max(min, Math.min(max, val));
    }
    
    private class Rectangle{ 
        int centerX;
        int centerY;
        int width;
        int height;
        
        public Rectangle(int x, int y, int w, int h) {
            centerX = x;
            centerY = y;
            width = w;
            height = h;
        }
    }
}
