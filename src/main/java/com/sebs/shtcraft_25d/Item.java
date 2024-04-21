package com.sebs.shtcraft_25d;

import java.awt.Image;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;


public class Item implements Renderer.Entity
{
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
	}
	
	@Override
	public void tick(double deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(DrawCallCollector d) {
		// TODO Auto-generated method stub
		
	}

	public enum Type
	{
		Grass,
	}

	
}

