package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.InputMismatchException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.Entity;

import glm.vec._2.Vec2;

public class Shitcraft {
	private Renderer renderer;
	private TestSquare testSquare;
	private WorldManager worldManager;
	private PlayerManager playerManager;
	private UIManager UIManager;
	private Zombie zombie;

	public Shitcraft(Renderer renderer_) {
		this.renderer = renderer_;

		this.testSquare = new TestSquare();
		try {
			this.playerManager = new PlayerManager(this.renderer);
		} catch (InputMismatchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Player Manager sprite file not found or Input Mismatch");
		}

		this.worldManager = new WorldManager(this.renderer);
		this.UIManager = new UIManager(this.renderer);
		this.zombie = new Zombie(new Vec2(0.0, 0.0));
		renderer.register(new WeakReference<Entity>(this.zombie));

		renderer.register(new WeakReference<Entity>(this.worldManager));
		renderer.register(new WeakReference<Entity>(this.testSquare));
		renderer.register(new WeakReference<Entity>(this.playerManager));
		renderer.register(new WeakReference<Entity>(this.UIManager));
	}

	private static class TestSquare implements Renderer.Entity {
		private double timeAlive = 0.0;
		private Image demo;

		TestSquare() {
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
		public void draw(DrawCallCollector d) {
			d.drawFilledRectangleWorld(Math.cos(this.timeAlive), Math.sin(this.timeAlive), 1, 1.0, 1.0, Color.CYAN);
			d.drawTexturedRectangleWorld(0.0, 0.0, 1, 1.0, 1.0, this.demo);
		}
	}

	/*
	 * private static class TestStatus implements Renderer.Entity { private double
	 * timeAlive = 0.0;
	 * 
	 * TestStatus(){}
	 * 
	 * @Override public void tick(double deltaTime) { this.timeAlive += deltaTime; }
	 * 
	 * @Override public void draw(DrawCallCollector d) { d.drawFilledRectangle(-60,
	 * Math.cos(this.timeAlive), 2, 1.0, 1.0, Color.RED); }
	 * 
	 * }
	 */

}
