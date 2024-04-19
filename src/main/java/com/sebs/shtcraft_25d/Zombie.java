package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;

public class Zombie implements Renderer.Entity
{
	private Image demo;
	private Vec2 prev;
	private Vec2 goal;
	private final double tTravelGoal = 3.0;
	private double t = 0;
	private State state;
	
	Zombie(Vec2 start)
	{	
		this.prev = new Vec2(0.0, 0.0);
		this.goal = new Vec2(0.0, 0.0);
		this.t = 2.999;
		this.state = State.Travel;
		
		try {
			this.demo = ImageIO.read(new File("demo.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public String toString()
	{
		return String.format("Zombie | prev: %s | goal: %s | t: %f", this.prev.toString(), this.goal.toString(), this.t);
	}
	
	@Override
	public void tick(double deltaTime)
	{
		switch (this.state)
		{
		case Randomize:
			this.prev = this.goal;
			
			this.goal = new Vec2((Math.random() - 0.5) * 20, (Math.random() - 0.5) * 20);

			this.t = 0.0;
			break;
		case Travel:
			this.t += deltaTime;
		}
		
		
		
		switch (this.state)
		{
		case Randomize:
			this.state = State.Travel;
			System.out.println("zombie tick " + this.toString());
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
		

		
	}

	@Override
	public void draw(DrawCallCollector d)
	{					
		Vec2 toGoal = this.prev.sub(this.goal);
		toGoal = toGoal.normalize();
		
		d.drawFilledRectangle(
				this.prev.x + toGoal.x * this.t,
				this.prev.y + toGoal.y * this.t,
				1, 1.0, 1.0, Color.RED);
	}
	
	private enum State
	{
		Randomize,
		Travel
	}
}

