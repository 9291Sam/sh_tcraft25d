package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

public class Renderer extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1L;
	
	public interface Entity
	{		
		public void tick(double deltaTime);
		public void draw(DrawCallCollector d);
	}
	
	private Set<WeakReference<Entity>> entities;
	private Map<Integer, Boolean> isKeyPressed;
	private boolean shouldClose;
	private final int windowWidthPx, windowHeightPx;
	private double cameraX, cameraY;
	
	public Renderer()
	{
		setPreferredSize(new Dimension(600, 600));
		this.windowHeightPx = 600;
		this.windowWidthPx = 600;
		
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
		
		this.entities = new HashSet<>();
		this.isKeyPressed = new HashMap<>();
		this.nanosPrev = System.nanoTime();
		this.cameraX = 0.0;
		this.cameraY = 0.0;
		
		for (int i = 0; i < 256; ++i)
		{
			this.isKeyPressed.put(i, false);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
	    DrawCallCollector callCollector = new DrawCallCollector(
	    		g,  // TODO: sync with screen size!
	    		this.cameraX, this.cameraY, 10.0, 10.0, this.windowWidthPx, this.windowHeightPx);

	    this.entities = 
	        this.entities.stream()
	            .map(WeakReference::get)
	            .filter(Objects::nonNull)
	            .peek(e -> e.draw(callCollector))
	            .map(WeakReference::new)
	            .collect(Collectors.toSet());
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		this.isKeyPressed.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		this.isKeyPressed.put(e.getKeyCode(), false);
	}
	
	private long nanosPrev;
	
	public void tickAndDraw()
	{
		long now = System.nanoTime();
		
		double deltaTime = (double)(now - this.nanosPrev) / 1e9;
		
		double cameraMoveSpeed = 5.0;
		
		if (this.isKeyPressed.get(KeyEvent.VK_W)) this.cameraY += (cameraMoveSpeed * deltaTime);
		if (this.isKeyPressed.get(KeyEvent.VK_S)) this.cameraY -= (cameraMoveSpeed * deltaTime);
		if (this.isKeyPressed.get(KeyEvent.VK_A)) this.cameraX -= (cameraMoveSpeed * deltaTime);
		if (this.isKeyPressed.get(KeyEvent.VK_D)) this.cameraX += (cameraMoveSpeed * deltaTime);
		
		for (WeakReference<Entity> e : this.entities)
		{
			Entity maybeEntity = e.get();
			
			if (maybeEntity != null)
			{
				maybeEntity.tick(deltaTime);
			}
		}
		
		nanosPrev = now;
		
		long nanosAfter = System.nanoTime();
		
		
		this.repaint();
	}
	
	public void register(WeakReference<Entity> e)
	{
		this.entities.add(e);
	}
	
	public boolean shouldClose()
	{
		return this.shouldClose;
	}
	
	private static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public class DrawCallCollector
	{
		private Graphics g;
		private double cameraX, cameraY;
		private double cameraWidth, cameraHeight;
		private double screenPxX, screenPxY;
		
		public DrawCallCollector(
				Graphics newG,
				double newCameraX,
				double newCameraY,
				double newCameraWidth,
				double newCameraHeight,
				double newScreenPxX,
				double newScreenPxY)
		{
			this.g = newG;
			this.cameraX = newCameraX;
			this.cameraY = newCameraY;
			this.cameraWidth = newCameraWidth;
			this.cameraHeight = newCameraHeight;
			this.screenPxX = newScreenPxX;
			this.screenPxY = newScreenPxY;
		}

		 public void drawFilledRectangle(double xWorld, double yWorld, double width, double height, Color color)
		 {
			 double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0, this.screenPxX);
			 double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0, this.screenPxY);
			 
			 double widthPx = this.screenPxX * width / this.cameraWidth;
		 	 double heightPx = this.screenPxY * height / this.cameraHeight;
			 
			 if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX ||
			    yScreenPx <= -heightPx || yScreenPx >= this.screenPxY) {
			    return;
			 }
			 
		 	 this.g.setColor(color);
		 	 this.g.fillRect((int)xScreenPx, (int)yScreenPx, (int)widthPx, (int)heightPx); 
	    }
		 
		 public void drawTexturedRectangle(double xWorld, double yWorld, double width, double height, Image img)
		 {
			 double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0, this.screenPxX);
			 double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0, this.screenPxY);

		 	 double widthPx = this.screenPxX * width / this.cameraWidth;
		 	 double heightPx = this.screenPxY * height / this.cameraHeight;
			 
			 if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX ||
			    yScreenPx <= -heightPx || yScreenPx >= this.screenPxY) {
			    return;
			 }
			 
		 	 
			 this.g.drawImage(img, (int)xScreenPx, (int)yScreenPx, (int)widthPx, (int)heightPx, null);
	    }
	}
	
	public Set<Entity> strongEntities = new HashSet<>();
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Shitcraft");
		
		Renderer renderer = new Renderer();
		Shitcraft shitcraft = new Shitcraft(renderer);
		
		frame.add(renderer);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		while (!renderer.shouldClose())
		{
			renderer.tickAndDraw();
			
			// because of Java Things :tm: if we want shitcraft to stay alive, we need to use it 
			retainer(shitcraft);
		}
	}
	
	private static void retainer(Object o) {}
}


