package com.sebs.shtcraft_25d;

import java.util.Set;
import java.util.stream.Collectors;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class ItemManager implements Renderer.Entity
{
	private Set<Item> activeItems;
	
	public ItemManager() {}
	
	public void registerItem(Item i)
	{
		this.activeItems.add(i);
	}

	@Override
	public void tick(double deltaTime)
	{
		this.activeItems = 
			this.activeItems.stream()
				.filter(i -> i.isAlive())
				.collect(Collectors.toSet());
	}

	@Override
	public void draw(DrawCallCollector d) {}
	
	
	

}
