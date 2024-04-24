package com.sebs.shtcraft_25d;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.Entity;

public class WorldEntityManager implements Renderer.Entity
{
	private Set<WorldEntity> activeItems;
	private Renderer renderer;
	
	public WorldEntityManager(Renderer renderer_)
	{
		this.activeItems = ConcurrentHashMap.newKeySet();
		this.renderer = renderer_;
	}
	
	
	public void registerWorldEntity(WorldEntity i)
	{
		this.activeItems.add(i);
		
		this.renderer.register(new WeakReference<Entity>(i));
	}

	@Override
	public void tick(double deltaTime)
	{
		
		for (WorldEntity e : this.activeItems)
		{
			if (!e.isAlive())
			{
				this.activeItems.remove(e);
			}
		}
		
		// TODO: test for colissions
	}

	@Override
	public void draw(DrawCallCollector d) {}
	
	
	

}
