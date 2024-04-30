package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.InputMismatchException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sebs.shtcraft_25d.Renderer.Entity;

import glm.vec._2.Vec2;

public class Shitcraft {
	private Renderer renderer;
	private ShitHut testSquare;
	private WorldManager worldManager;
	private WorldEntityManager worldEntityManager;
	private PlayerColissionTracker colissionTracker;
	private PlayerManager playerManager;
	private UIManager UIManager;
	private Inventory inventory;

	public Shitcraft(Renderer renderer_) {
		this.renderer = renderer_;
		
		this.worldEntityManager = new WorldEntityManager(this.renderer);

		this.testSquare = new ShitHut();
		try {
			this.playerManager = new PlayerManager(this.renderer, this.worldEntityManager);
		} catch (InputMismatchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Player Manager sprite file not found or Input Mismatch");
		}
		
		File f = new File("328857_NG_Rival_remix.wav");
	    AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			clip.open(audioIn);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    clip.start();

		this.worldManager = new WorldManager(this.renderer);
		this.UIManager = new UIManager(this.renderer);
		this.inventory = new Inventory(this.renderer);

		renderer.register(new WeakReference<Entity>(this.worldManager));
		renderer.register(new WeakReference<Entity>(this.testSquare));
		renderer.register(new WeakReference<Entity>(this.playerManager));
		renderer.register(new WeakReference<Entity>(this.UIManager));
		renderer.register(new WeakReference<Entity>(this.inventory));
		renderer.register(new WeakReference<Entity>(this.worldEntityManager));
		
		this.worldEntityManager.registerWorldEntity(new ZombieSpawner(this.worldEntityManager, new Vec2(5.0, 0.0), (i) -> {this.inventory.addCoins(i); return true;}));
		this.worldEntityManager.registerWorldEntity(new PlayerColissionTracker(this.playerManager, this.UIManager));
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
			d.drawTexturedRectangleWorld(-2.5, 2.8, 1, 4.0, 4.0, this.demo);
		}
	}

}
