package com.sebs.shtcraft_25d;

import java.awt.Image;

import glm.vec._2.Vec2;

public class BlasterBullet extends WorldEntity
{
	private double timeAlive = 0.0;
	private final Vec2 origin;
	private final Vec2 dir;
	
	static Image maybeBlasterImage;
	static Image getBlasterImage()
	{
		if (maybeBlasterImage == null)
		{
			BlasterBullet.maybeBlasterImage = Utils.loadImage("plasma_bullet.png");
		}
		
		return BlasterBullet.maybeBlasterImage;
	}

	public BlasterBullet(Vec2 position_, Vec2 dir_)
	{
		super(BlasterBullet.getBlasterImage(), position_);
		
		this.origin = new Vec2(this.position);
		this.dir = new Vec2(dir_.x, dir_.y).normalize();
		
		if (Math.abs(this.dir.length()) < 0.01)
		{
			throw new IllegalArgumentException("Created Blaster Bullet with 0 dir");
		}
		
	}

	@Override
	public void tick(double deltaTime)
	{
		this.timeAlive += deltaTime;

		if (this.timeAlive > 5.0)
		{
			this.isAlive = false;
		}
		
		Vec2 dirIntercalc = new Vec2(this.dir);
		dirIntercalc.mul((float)this.timeAlive);
		
		Vec2 originInterCalc = new Vec2(this.origin);
		originInterCalc.add(dirIntercalc);
		
		this.position = originInterCalc;
		
		// I love not having operator over loading
		// I love having a reference oriented language
		// this is so much more readable than
		// this.position = this.origin + this.dir * this.timeAlive;
	}

}
