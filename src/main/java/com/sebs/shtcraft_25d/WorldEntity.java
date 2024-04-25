package com.sebs.shtcraft_25d;

import java.awt.Image;

import com.sebs.shtcraft_25d.WorldEntity.BoundingSphere;

import glm.vec._2.Vec2;


public abstract class WorldEntity implements Renderer.Entity
{
	boolean isAlive;
	Image image;
	Vec2 position;
	double edgeLength;
	
	public WorldEntity(Image image_, Vec2 position_, double edgeLength_)
	{
		this.image = image_;
		this.position = position_;
		this.isAlive = true;
		this.edgeLength = edgeLength_;
	}
	
	public boolean isAlive()
	{
		return this.isAlive;
	}

	@Override
	public void draw(DrawCallCollector d)
	{
		// I love having to hack in deterministic lifetimes
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
	
	protected BoundingSphere getBoundingBox()
	{
		return new BoundingSphere(
			new Vec2(this.position).add(new Vec2(0.0, -(float)this.edgeLength)),
			this.edgeLength / 2
		);
		
	}
	protected abstract void collissionWith(WorldEntity e);
	protected abstract ColissionType getColissionType();
	
	public class BoundingSphere
	{
	    private Vec2 center;
	    private double radius;

    	@Override
    	public String toString()
    	{
    		return String.format("Bounding Sphere %s %f", this.center.toString(), this.radius);
    	}
	    
	    public BoundingSphere(Vec2 center_, double radius_)
	    {
	    	this.center = center_;
	    	this.radius = radius_;
	    }

	    public boolean collidesWith(BoundingSphere other)
	    {
	    	return new Vec2(this.center).sub(other.center).length() < (this.radius + other.radius);
    	}
	}
	
	// Thin <=> Thin collisions are assumed to not be possible
	public static enum ColissionType
	{
		/// Use this for large entities (zombie)
		Thick,
		/// Use this for small things (bullet)
		Thin,
		
		NoClollide
	}
}



