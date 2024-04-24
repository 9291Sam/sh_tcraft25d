package com.sebs.shtcraft_25d;

import java.awt.Image;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;


public abstract class WorldEntity implements Renderer.Entity
{
	final double edgeLength = 0.25;
	
	boolean isAlive;
	Image image;
	Vec2 position;
	
	public WorldEntity(Image image_, Vec2 position_)
	{
		this.image = image_;
		this.position = position_;
		this.isAlive = true;
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}

	@Override
	public void draw(DrawCallCollector d)
	{
		// I love non-deterministic lifetimes!!!!!
		// (I only know how to program with them, so this feels like a hack)
		if (this.isAlive())
		{
			d.drawTexturedRectangleWorld(
				this.position.x - 0.5 * this.edgeLength, 
				this.position.y - 0.5 * this.edgeLength,
				4,
				this.edgeLength,
				this.edgeLength,
				this.image
			);
		}
	}	
}

