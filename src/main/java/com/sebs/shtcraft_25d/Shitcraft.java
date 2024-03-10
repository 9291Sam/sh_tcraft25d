package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.Entity;

public class Shitcraft
{
	private Renderer renderer;
	private TestSquare testSquare;
	private WorldManager worldManager;
	private PlayerManager playerManager;
	// EntityManager
	
	
	public Shitcraft(Renderer renderer_)
	{
		this.renderer = renderer_;
		
		this.testSquare = new TestSquare();
		
		renderer.register(new WeakReference<Entity>(this.testSquare));
	}
	
	private static class PlayerManager implements Renderer.Entity
	{
		private Image playerImage;
		private double x;
		private double y;
		
		public static Image loadImage(String filePath) 
		{
			File file = new File(filePath);
			try {
				return ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
				return(null);
			}
		}
		public PlayerManager(Image playerImage, double x, double y) {
			this.playerImage = playerImage;
			playerImage = loadImage("PathToFile");
			this.x = x;
			this.y = y;
		}

		@Override
		public void tick(double deltaTime) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(DrawCallCollector d) {
			// TODO Auto-generated method stub
			if (playerImage != null) {
				
			}
		}
		
	}
	
	private static class TestSquare implements Renderer.Entity
	{
		private double timeAlive = 0.0;
		private Image demo;
		
		TestSquare()
		{
			try {
				this.demo = ImageIO.read(new File("demo.jpeg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void tick(double deltaTime) {
			this.timeAlive += deltaTime;
		}

		@Override
		public void draw(DrawCallCollector d)
		{					
			d.drawFilledRectangle(Math.cos(this.timeAlive), Math.sin(this.timeAlive), 1.0, 1.0, Color.CYAN);
			d.drawTexturedRectangle(0.0, 0.0, 1.0, 1.0, this.demo);
		}
		
	}
	

}

