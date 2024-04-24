package com.sebs.shtcraft_25d;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.Entity;

public class ItemManager implements Renderer.Entity
{
	private Set<Item> activeItems;
	private Renderer renderer;
	
	public ItemManager(Renderer renderer_)
	{
		this.activeItems = new HashSet<Item>();
		this.renderer = renderer_;
	}
	
	
	public void registerItem(Item i)
	{
		this.activeItems.add(i);
		
		this.renderer.register(new WeakReference<Entity>(i));
	}

	@Override
	public void tick(double deltaTime)
	{
		System.out.printf("Items: %d\n", this.activeItems.size());
		
		this.activeItems = 
			this.activeItems.stream()
				.filter(i -> i.isAlive())
				.collect(Collectors.toSet());
	}

	@Override
	public void draw(DrawCallCollector d) {}
	
	
	

}
