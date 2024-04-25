package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.InputMismatchException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.Entity;

import glm.vec._2.Vec2;

public class Shitcraft {
	private Renderer renderer;
	private ShitHut testSquare;
	private WorldManager worldManager;
	private PlayerManager playerManager;
	private UIManager UIManager;
	private Inventory inventory;

	public Shitcraft(Renderer renderer_) {
		this.renderer = renderer_;

		this.testSquare = new ShitHut();
		try {
			this.playerManager = new PlayerManager(this.renderer);
		} catch (InputMismatchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Player Manager sprite file not found or Input Mismatch");
		}

		this.worldManager = new WorldManager(this.renderer);
		this.UIManager = new UIManager(this.renderer);
		this.inventory = new Inventory(this.renderer);

		renderer.register(new WeakReference<Entity>(this.worldManager));
		renderer.register(new WeakReference<Entity>(this.testSquare));
		renderer.register(new WeakReference<Entity>(this.playerManager));
		renderer.register(new WeakReference<Entity>(this.UIManager));
		renderer.register(new WeakReference<Entity>(this.inventory));
		
		this.renderer.getWorldManager().registerWorldEntity(new Zombie(new Vec2(0.0, 0.0)));
	}

	private static class ShitHut implements Renderer.Entity {
		private double timeAlive = 0.0;
		private Image demo;

		ShitHut() {
			try {
				this.demo = ImageIO.read(new File("ShitHut.png"));
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
			d.drawFilledRectangleWorld(12.5, 3.2, 5, 0.1, 0.1, Color.CYAN);
			d.drawTexturedRectangleWorld(-2.5, 2.8, 1, 4.0, 4.0, this.demo);
		}
	}

}
