package com.sebs.shtcraft_25d;

import java.awt.Image;

import glm.vec._2.Vec2;

public class BlasterBullet extends WorldEntity
{
	private double timeAlive = 0.0;
	private final Vec2 origin;
	private final Vec2 dir;
	private static final double duration = 5.0;
	private static final double speed = 8.0;
	
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
		super(BlasterBullet.getBlasterImage(), position_, 0.25 * Inventory.getLevel());
		
		this.origin = new Vec2(this.position);
		this.dir = new Vec2(dir_.x, dir_.y).normalize();
		
		if (Math.abs(this.dir.length()) < 0.01)
		{
			throw new IllegalArgumentException("Created Blaster Bullet with 0 dir");
		}
		
	}

    // Method to update bullet's state
    @Override
    public void tick(double deltaTime) {
        this.timeAlive += deltaTime; // Increment time alive

        // Check if bullet has exceeded its duration
        if (this.timeAlive > BlasterBullet.duration) {
            this.isAlive = false; // Mark bullet as dead
        }

        // Calculate new position based on time and direction
        Vec2 dirIntercalc = new Vec2(this.dir).mul((float) BlasterBullet.speed * (float) this.timeAlive);
        Vec2 originInterCalc = new Vec2(this.origin).add(dirIntercalc);
        this.position = originInterCalc;
    }

    // Method to get collision type of the bullet
    @Override
    protected ColissionType getColissionType() {
        return ColissionType.Thin; // Thin collision type
    }

    // Method to handle collision with other entities
    @Override
    protected void collissionWith(WorldEntity e) {
        if (!(e instanceof PlayerColissionTracker)) {
            this.isAlive = false; // Mark bullet as dead if collided with entity other than PlayerColissionTracker
        }
    }

    // Method to get string representation of BlasterBullet
    @Override
    public String toString() {
        return String.format("Blaster Bullet @ %s", this.position);
    }
}
