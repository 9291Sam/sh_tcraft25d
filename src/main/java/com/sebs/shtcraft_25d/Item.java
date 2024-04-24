package com.sebs.shtcraft_25d;

import java.awt.Image;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;


public class Item implements Renderer.Entity
{
	final double edgeLength = 0.25;
	boolean isAlive;
	Image image;
	Vec2 position;
	
	public Item(Type t, Vec2 position_)
	{
		switch (t)
		{
		case Grass:
			this.image = Utils.loadImage("grassTexure.png");
			break;
		
		}
		
		this.position = position_;
		this.isAlive = true;
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}
	
	@Override
	public void tick(double deltaTime) {}

	@Override
	public void draw(DrawCallCollector d)
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

	public enum Type
	{
		Grass,
	}

	
}

