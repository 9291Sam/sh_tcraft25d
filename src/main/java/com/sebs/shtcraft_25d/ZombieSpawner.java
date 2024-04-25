package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.util.function.Function;

import glm.vec._2.Vec2;

public class ZombieSpawner extends WorldEntity
{
	private double timeBetweenSpawns;
	private double timeSinceLastSpawn;
	private double spawnRadius;
	private WorldEntityManager worldEntityManager;
	private Function<Integer, Boolean> addCoinsFunc;
	
	
	private static Image maybeZombieSpawnerImage;
	private static Image getZombieSpawnerImage()
	{
		if (ZombieSpawner.maybeZombieSpawnerImage == null)
		{
			ZombieSpawner.maybeZombieSpawnerImage = Utils.loadImage("spawner.png");
		}
		
		return ZombieSpawner.maybeZombieSpawnerImage;
	}
	
	public ZombieSpawner(WorldEntityManager worldEntityManager_, Vec2 spawnPoint, Function<Integer, Boolean> addCoinsFunc_)
	{
		super(ZombieSpawner.getZombieSpawnerImage(), new Vec2(spawnPoint), 1.0);
		
		this.worldEntityManager = worldEntityManager_;
		this.timeSinceLastSpawn = 0.0;
		this.timeBetweenSpawns = 1.8;
		this.spawnRadius = 3.0;
		this.addCoinsFunc = addCoinsFunc_;
	}

	@Override
	public void tick(double deltaTime)
	{
		this.timeSinceLastSpawn += deltaTime;
		
		if (this.timeSinceLastSpawn > this.timeBetweenSpawns)
		{
			Vec2 newSpawnPos = new Vec2(this.position)
				.add(
					(float)Utils.map(Math.random(), 0.0, 1.0, -this.spawnRadius, this.spawnRadius),
					(float)Utils.map(Math.random(), 0.0, 1.0, -this.spawnRadius, this.spawnRadius));
			
			this.worldEntityManager.registerWorldEntity(new Zombie(newSpawnPos, this.addCoinsFunc));
			
			this.timeSinceLastSpawn = 0.0;
		}
		
		
	}
	
	

	@Override
	protected void collissionWith(WorldEntity e)
	{
		throw new IllegalArgumentException("Should never happen");
	}

	@Override
	protected ColissionType getColissionType()
	{
		return ColissionType.NoClollide;
	}
}
