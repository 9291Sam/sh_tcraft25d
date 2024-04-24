package com.sebs.shtcraft_25d;

import java.awt.Image;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.WorldEntity.BoundingBox;

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
	
	protected BoundingBox getBoundingBox()
	{
		return new BoundingBox(
			new Vec2(this.position),
			new Vec2(this.position).add((float)this.edgeLength)
		);
		
	}
	protected abstract ColissionType getColissionType();
	
	public class BoundingBox
	{
	    private Vec2 topLeft;
	    private Vec2 bottomRight;

	    public BoundingBox(Vec2 topLeft_, Vec2 botRight_)
	    {
	    	this.topLeft = topLeft_;
	    	this.bottomRight = botRight_;
	    }

	    public boolean collidesWith(BoundingBox other) {
	    	 Vec2 delta = new Vec2(
		        Math.abs(topLeft.x - other.topLeft.x) - (bottomRight.x - topLeft.x) - (other.bottomRight.x - other.topLeft.x),
		        Math.abs(topLeft.y - other.topLeft.y) - (bottomRight.y - topLeft.y) - (other.bottomRight.y - other.topLeft.y)
		    );

		    return (delta.x < 0) && (delta.y < 0);
	    }
	}
	
	// Thin <=> Thin collisions are assumed to not be possible
	public static enum ColissionType
	{
		/// Use this for large entities (zombie)
		Thick,
		/// Use this for small things (bullet)
		Thin,
	}
}



