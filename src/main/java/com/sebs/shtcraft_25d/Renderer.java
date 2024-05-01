package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import glm.vec._2.Vec2;

public class Renderer extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;

	public interface Entity {
		public void tick(double deltaTime);

		public void draw(DrawCallCollector d);
	} 

	private Set<WeakReference<Entity>> entities;
	private Map<Integer, Boolean> isKeyPressed;
	private boolean shouldClose;
	private int windowWidthPx, windowHeightPx;
	private double cameraX, cameraY;
	private final double desiredWidthUnits = 12.5;

	public Renderer() {
		final int defaultWidth = 600;
		final int defaultHeight = 600;

		setPreferredSize(new Dimension(defaultWidth, defaultHeight));
		this.windowHeightPx = defaultHeight;
		this.windowWidthPx = defaultWidth;

		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);

		this.entities = ConcurrentHashMap.newKeySet();
		this.isKeyPressed = new HashMap<>();
		this.nanosPrev = System.nanoTime();
		this.cameraX = 0.0;
		this.cameraY = 0.0;

		for (int i = 0; i < 256; ++i) {
			this.isKeyPressed.put(i, false);
		}
	}

	public double getCameraXWorld() {
		return this.cameraX;
	}

	public double getCameraYWorld() {
		return this.cameraY;
	}

	@Override
	public void paintComponent(Graphics g) {
		this.windowWidthPx = this.getWidth();
		this.windowHeightPx = this.getHeight();

		super.paintComponent(g);

		DrawCallCollector callCollector = new DrawCallCollector(g, this.cameraX, this.cameraY,
				this.desiredWidthUnits * this.windowWidthPx / this.windowHeightPx, this.desiredWidthUnits, this.windowWidthPx, this.windowHeightPx);

		for (WeakReference<Entity> w : this.entities) {
			Entity e = w.get();

			if (e != null) {
				e.draw(callCollector);
			} else {
				this.entities.remove(w);
			}
		}

		callCollector.dispatch();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.isKeyPressed.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.isKeyPressed.put(e.getKeyCode(), false);
	}

	private long nanosPrev;

	public void tickAndDraw() {
		long now = System.nanoTime();

		double deltaTime = (now - this.nanosPrev) / 1e9;

		// camera update | TODO: not a great place to be.
		final double cameraMoveSpeed = 5.0;
		Vec2 moveVector = new Vec2(0.0, 0.0);

		moveVector.x += this.isKeyPressed(KeyEvent.VK_A) ? -1.0 : 0.0;
		moveVector.x += this.isKeyPressed(KeyEvent.VK_D) ? 1.0 : 0.0;
		moveVector.y += this.isKeyPressed(KeyEvent.VK_W) ? 1.0 : 0.0;
		moveVector.y += this.isKeyPressed(KeyEvent.VK_S) ? -1.0 : 0.0;

		// trying to normalize the zero vector isn't a good idea.
		if (moveVector.x != 0.0 || moveVector.y != 0.0) {
			moveVector.normalize();
			if(cameraX > 50) {
			    this.cameraX -= 0.001;
			} else if(cameraX < -50) {
                this.cameraX += 0.001;
            } else if(cameraY > 50) {
                this.cameraY -= 0.001;
            } else if(cameraY < -50) {
                this.cameraY += 0.001;
            } else { 
    			this.cameraX += moveVector.x * cameraMoveSpeed * deltaTime;
    			this.cameraY += moveVector.y * cameraMoveSpeed * deltaTime;
            }
		}
		//System.out.printf("Active Renderer Entity %d\n", this.entities.size());
		for (WeakReference<Entity> e : this.entities) {
			Entity maybeEntity = e.get();

			if (maybeEntity != null) {
				maybeEntity.tick(deltaTime);
			}
		}

		nanosPrev = now;


		this.repaint();
	}

	// this int represents a KeyEvent.getCode()
	public boolean isKeyPressed(int k) {
		return this.isKeyPressed.get(Integer.valueOf(k));
	}

	public void register(WeakReference<Entity> e)
	{
		this.entities.add(e);
	}

	public boolean shouldClose() {
		return this.shouldClose;
	}
	
	public void _Kill()
	{		
		this.killScreen = new KillScreen();
		this.register(new WeakReference<Entity>(this.killScreen));
	}
	
	KillScreen killScreen = null;

	

	public static void main(String[] args) {
		JFrame frame = new JFrame("Shitcraft");

		Renderer renderer = new Renderer();
		Shitcraft shitcraft = new Shitcraft(renderer);

		frame.add(renderer);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

		
		while (!renderer.shouldClose()) {
			synchronized (renderer.entities) {
				renderer.tickAndDraw();
			}

			// because of Java Things :tm: if we want shitcraft to stay alive, we need to
			// use it
			retainer(shitcraft);
		}
	}

	private static void retainer(Object o) {}
	
	private static class KillScreen implements Renderer.Entity
	{
		Image gameOver;
		
		public KillScreen()
		{
			this.gameOver = Utils.loadImage("game_over.jpg");
		}

		@Override
		public void tick(double deltaTime) {}

		@Override
		public void draw(DrawCallCollector d)
		{
			
			d.drawFilledRectangleScreen(0.0, 0.0, 100, 1.0, 1.0, Color.black);
			d.drawTexturedRectangleScreen(0.0, 0.15, 101, 1.0, 7 / 10.0, this.gameOver);
		}
		
	}
}
