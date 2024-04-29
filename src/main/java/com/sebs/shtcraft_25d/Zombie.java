package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;


import glm.vec._2.Vec2;

public class Zombie extends WorldEntity
{
	private static Image zombieImage;
	private static Image getZombieImage()
	{
		if (Zombie.zombieImage == null)
		{
			Zombie.zombieImage = Utils.loadImage("zombie.jpeg");
		}
		
		return Zombie.zombieImage;
	}
	
	private Vec2 prev;
	private Vec2 dir;
	private final double tTravelGoal = 3.0;
	private double t = 0;
	private State state;
	private Function<Integer, Boolean> addCoinsFunc;
	
	Zombie(Vec2 start, Function<Integer, Boolean> addCoinsFunc_)
	{			
		super(Zombie.getZombieImage(), new Vec2(start), 1.0);
		
		this.prev = new Vec2(start);
		this.dir = new Vec2(0.0);
		this.t = this.tTravelGoal - 0.01;
		this.state = State.Travel;
		this.addCoinsFunc = addCoinsFunc_;
		
	}
	
	private Vec2 calculateCurrentPos()
	{
		Vec2 workingPos = new Vec2(this.prev);
		
		workingPos.add(new Vec2(this.dir).mul((float)this.t));
		
		return workingPos;
	}
	
	@Override public String toString()
	{		
		return String.format("Zombie @ %s", this.calculateCurrentPos().toString());
	}
	
	@Override
	public void tick(double deltaTime)
	{
		switch (this.state)
		{
		case Randomize:
			this.prev = this.calculateCurrentPos();
			
			this.dir = new Vec2((Math.random() - 0.5), (Math.random() - 0.5)).normalize();

			this.t = 0.0;
			break;
		case Travel:
			this.t += deltaTime;
		}
		
		
		
		switch (this.state)
		{
		case Randomize:
			this.state = State.Travel;
			break;
			
		case Travel:
			if (this.t > tTravelGoal)
			{
				this.state = State.Randomize;
			}
			else
			{
				this.state = State.Travel;
			}
			break;
		}
		
		
		this.position = this.calculateCurrentPos();
		
	}
	
	private enum State
	{
		Randomize,
		Travel
	}

	@Override
	protected ColissionType getColissionType()
	{
		return ColissionType.Thick;
	}

	@Override
	protected void collissionWith(WorldEntity e)
	{
		if (e instanceof Zombie)
		{
			return;
		}
		
		if (this.isAlive)
		{
			this.addCoinsFunc.apply((int)Utils.map(Math.random(), 0.0, 1.0,  2.0, 6.0));
		}
		this.isAlive = false;
	}
	

}

