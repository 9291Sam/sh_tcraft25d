package com.sebs.shtcraft_25d;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class WorldManager implements Renderer.Entity
{
	int widthTiles = 64;
	int heightTiles = 64;
	
	@Override
	public void tick(double deltaTime) {}

	@Override
	public void draw(DrawCallCollector d) 
	{
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	private enum Tile
	{
		Stone,
		Grass,
	}






	

}
