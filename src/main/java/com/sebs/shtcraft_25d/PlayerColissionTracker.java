package com.sebs.shtcraft_25d;

public class PlayerColissionTracker extends WorldEntity
{
	PlayerManager manager;
	UIManager ui;
	
	public PlayerColissionTracker(PlayerManager manager_, UIManager ui_)
	{
		super(null, manager_.getPosition(), manager_.getDimensions().x);
		
		this.manager = manager_;
		this.ui = ui_;
	}

	@Override
	public void tick(double deltaTime)
	{
		this.position = this.manager.getPosition().add((float)0.0, (float)this.edgeLength * 1.75f / 4.0f);
		this.edgeLength = this.manager.getDimensions().x / 3;
	}

	@Override
	protected void collissionWith(WorldEntity e)
	{
		if (e instanceof Zombie)
		{
			this.ui.increaseByPercent(-0.03);
		}
	}

	@Override
	protected ColissionType getColissionType()
	{
		return ColissionType.Thick;
	}
	
	
	

}
