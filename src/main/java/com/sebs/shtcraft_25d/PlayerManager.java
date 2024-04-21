package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class PlayerManager implements Renderer.Entity
{
	Renderer renderer;
	Image playerImage;
	
	double playerX;
	double playerY;
	
	public static Image loadImage(String filePath) 
	{
		File file = new File(filePath);
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return(null);
		}
	}
	
	public PlayerManager(Renderer renderer_)
	{
		this.playerImage = loadImage("PixelArtTutorial.png");
		this.renderer = renderer_;
		
		this.playerX = 0.0;
		this.playerY = 0.0;
	}

	@Override
	public void tick(double deltaTime)
	{
		this.playerX = this.renderer.getCameraXWorld();
		this.playerY = this.renderer.getCameraYWorld();
	}

	@Override
	public void draw(DrawCallCollector d)
	{
		if (playerImage != null)
		{
			final double playerWidth = 1.0;
			final double playerHeight = 1.0;
			
			d.drawTexturedRectangleWorld(
				this.playerX - 0.5 * playerWidth,
				this.playerY - 0.5 * playerHeight,
				0,
				playerWidth,
				playerHeight,
				playerImage);
		}
	}
}