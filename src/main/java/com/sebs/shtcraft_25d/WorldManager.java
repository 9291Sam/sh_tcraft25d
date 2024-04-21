package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class WorldManager implements Renderer.Entity
{
	Renderer renderer;
	int widthTiles = 64;
	int heightTiles = 64;
	
	Tile[][] tileMap = new Tile[this.widthTiles][this.heightTiles];
	
	private Image stone;
	
	public WorldManager(Renderer renderer_)
	{
		this.renderer = renderer_;	
		
		try {
			this.stone = ImageIO.read(new File("stone.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tick(double deltaTime) {}

	@Override
	public void draw(DrawCallCollector d) 
	{
		for (int i = -10; i < 10; ++i)
		{
			for (int j = -10; j < 10; ++j)
			{
				d.drawTexturedRectangle((double)i, (double)j, -1, 1.01, 1.01, this.stone);
			}
		}
	}
	
	private enum Tile
	{
		Stone,
		Grass,
	}

	public static double clamp(double min, double max, double val) {
	    return Math.max(min, Math.min(max, val));
	}
}
