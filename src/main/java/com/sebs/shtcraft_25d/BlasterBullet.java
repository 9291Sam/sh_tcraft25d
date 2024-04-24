package com.sebs.shtcraft_25d;

import java.awt.Image;

import glm.vec._2.Vec2;

public class BlasterBullet extends WorldEntity
{
	double timeAlive = 0.0;
	Vec2 travelVector;
	
	static Image maybeBlasterImage;
	static Image getBlasterImage()
	{
		if (maybeBlasterImage == null)
		{
			BlasterBullet.maybeBlasterImage = Utils.loadImage("plasma_bullet.png");
		}
		
		return BlasterBullet.maybeBlasterImage;
	}

	public BlasterBullet(Vec2 position_)
	{
		super(BlasterBullet.getBlasterImage(), position_);
	}

	@Override
	public void tick(double deltaTime)
	{
		this.timeAlive += deltaTime;

		if (this.timeAlive > 5.0)
		{
			this.isAlive = false;
		}
	}

}
