package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.Entity;
import com.sebs.shtcraft_25d.Renderer.keyPressed;

import glm.vec._2.Vec2;

public class Shitcraft
{
	private Renderer renderer;
	private TestSquare testSquare;
	private WorldManager worldManager;
	private PlayerManager playerManager;
	private TestStatus testStatus;
	private UIManager UIManager;
	private Zombie zombie;
	
	public Shitcraft(Renderer renderer_)
	{
		this.renderer = renderer_;
		
		this.testSquare = new TestSquare();
		this.playerManager = new PlayerManager(0.0, 0.0, 2.0);
		this.testStatus = new TestStatus();
		
		this.worldManager = new WorldManager(this.renderer);
		this.UIManager =new UIManager(this.renderer);
		this.zombie = new Zombie(new Vec2(0.0, 0.0));
		renderer.register(new WeakReference<Entity>(this.zombie));
		
		renderer.register(new WeakReference<Entity>(this.worldManager));
		renderer.register(new WeakReference<Entity>(this.testSquare));
		renderer.register(new WeakReference<Entity>(this.playerManager));
		renderer.register(new WeakReference<Entity>(this.UIManager));
	}
	
	public static class PlayerManager implements Renderer.Entity
	{
		private Image playerImage;
		private double timeAlive = 0.0;
		private double centerX;
		private double centerY;
		private double radius;
		private double angle;
		private static double x;
		private static double y;
		
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
		public PlayerManager(double x, double y, double radius) {
			this.playerImage = loadImage("PixelArtTutorial.png");
			this.centerX = x;
			this.centerY = y;
			this.angle = 0.0;
			this.radius = radius;
		}

		@Override
		public void tick(double deltaTime) {
			// TODO Auto-generated method stub
			this.timeAlive += deltaTime;
			this.angle += 1.01 * deltaTime;
			this.x = keyPressed.getX();
			this.y = keyPressed.getY();
		}

		@Override
		public void draw(DrawCallCollector d) {
			// TODO Auto-generated method stub
			if (playerImage != null) {
				double width = 1;
				double height = 1;
				d.drawTexturedRectangle(x, y, 1, width, height, playerImage);;
			}
		}
		
		public static double getX() {
			return x;
		}
		
		public static double getY() {
			return y;
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
			d.drawFilledRectangle(Math.cos(this.timeAlive), Math.sin(this.timeAlive), 1, 1.0, 1.0, Color.CYAN);
			d.drawTexturedRectangle(0.0, 0.0, 1, 1.0, 1.0, this.demo);
		}
	}
	
    private static class TestStatus implements Renderer.Entity
    {
        private double timeAlive = 0.0;
        
        TestStatus(){}
        
        @Override
        public void tick(double deltaTime) {
            this.timeAlive += deltaTime;
        }

        @Override
        public void draw(DrawCallCollector d)
        {                   
            d.drawFilledRectangle(-60, Math.cos(this.timeAlive), 2, 1.0, 1.0, Color.RED);
        }
        
    }
	

}

