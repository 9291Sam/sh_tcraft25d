package com.sebs.shtcraft_25d;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.sebs.shtcraft_25d.Renderer.Entity;
import com.sebs.shtcraft_25d.WorldEntity.ColissionType;

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
		Set<WorldEntity> thickEntities = this.activeItems
			.stream()
			.filter(e -> e.getColissionType().equals(ColissionType.Thick))
			.collect(Collectors.toSet());
		
		for (WorldEntity e : this.activeItems)
		{
			if (!e.isAlive())
			{
				this.activeItems.remove(e);
			}
		}
		
		for (WorldEntity all : this.activeItems)
		{
			if (all.getColissionType().equals(ColissionType.NoClollide))
			{
				continue;
			}
			
			for (WorldEntity thick : thickEntities)
			{
				if (all != thick)
				{
					if (thick.getBoundingBox().collidesWith(all.getBoundingBox()))
					{
						all.collissionWith(thick);
						thick.collissionWith(all);
					}
				}
				
			}
		}
		
		
		// TODO: test for colissions
	}

	@Override
	public void draw(DrawCallCollector d) {}
	
	
	

}
