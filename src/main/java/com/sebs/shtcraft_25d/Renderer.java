package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;
import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;

import java.util.function.Function;
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
	private int windowWidthPx, windowHeightPx;
	private double cameraX, cameraY;
	
	 // TODO: make a proper Game class that holds world level objects like this
	private WorldEntityManager itemManager;
	
	public Renderer()
	{
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
		this.itemManager = new WorldEntityManager(this);
		this.register(new WeakReference<Entity>(this.itemManager));
		
		for (int i = 0; i < 256; ++i)
		{
			this.isKeyPressed.put(i, false);
		}
	}
	
	public double getCameraXWorld()
	{
		return this.cameraX;
	}
	
	public double getCameraYWorld()
	{
		return this.cameraY;
	}
	
	public WorldEntityManager getItemManager()
	{
		return this.itemManager;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		this.windowWidthPx = this.getWidth();
		this.windowHeightPx = this.getHeight();
		
		super.paintComponent(g);
		
	    DrawCallCollector callCollector = new DrawCallCollector(
	    		g,
	    		this.cameraX, this.cameraY, 10.0 * (float)this.windowWidthPx / this.windowHeightPx, 10.0, this.windowWidthPx, this.windowHeightPx);
	    
	    for (WeakReference<Entity> w : this.entities)
	    {
	    	Entity e = w.get();
	    	
	    	if (e != null)
	    	{
	    		e.draw(callCollector);
	    	}
	    	else
	    	{
	    		this.entities.remove(w);
	    	}
	    }	
	    
	    
	    callCollector.dispatch();
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
		
		// camera update | TODO: not a great place to be.
		final double cameraMoveSpeed = 5.0;
		Vec2 moveVector = new Vec2(0.0, 0.0);
		
		moveVector.x += this.isKeyPressed(KeyEvent.VK_A) ? -1.0 : 0.0;
		moveVector.x += this.isKeyPressed(KeyEvent.VK_D) ? 1.0 : 0.0;
		moveVector.y += this.isKeyPressed(KeyEvent.VK_W) ? 1.0 : 0.0;
		moveVector.y += this.isKeyPressed(KeyEvent.VK_S) ? -1.0 : 0.0;
		
		// trying to normalize the zero vector isn't a good idea.
		if (moveVector.x != 0.0 || moveVector.y != 0.0)
		{
			moveVector.normalize();
			
			this.cameraX += moveVector.x * cameraMoveSpeed * deltaTime;
			this.cameraY += moveVector.y * cameraMoveSpeed * deltaTime;
		}
		
		for (WeakReference<Entity> e : this.entities)
		{
			Entity maybeEntity = e.get();
			
			if (maybeEntity != null)
			{
				maybeEntity.tick(deltaTime);
			}
		}
	
	
		nanosPrev = now;
		
		
		this.repaint();
	}
	
	// this int represents a KeyEvent.getCode()
	public boolean isKeyPressed(int k) 
	{
		return this.isKeyPressed.get(Integer.valueOf(k));
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
		private ArrayList<Pair<Integer, Function<Graphics, Boolean>>> functions;
		
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
			
			this.functions = new ArrayList<>();
		 }
		// TODO: make *World and *ScreenSpace
		// takes in normalized coordinates 
		public void drawFilledRectangleScreen(double xScreen, double yScreen, Integer layer, double width, double height, Color color)
		{
			Function<Graphics, Boolean> f = (Graphics g) -> 
			{
				g.setColor(color);
				g.fillRect(
					(int)(xScreen * this.screenPxX),
					(int)(yScreen * this.screenPxY),
					(int)(width * this.screenPxX),
					(int)(height * this.screenPxY)
				); 
				 
				return true;
			};
			 
			this.functions.add(Pair.of(layer, f));		
		}
		
		public void drawTexturedRectangleScreen(double xScreen, double yScreen, Integer layer, double width, double height, Image img)
		{
			Function<Graphics, Boolean> f = (Graphics g) ->
			{
				g.drawImage(img, 
					(int)(xScreen * this.screenPxX),
					(int)(yScreen * this.screenPxY),
					(int)(width * this.screenPxX),
					(int)(height * this.screenPxY),
					null);
				 
				return true;
			};
			 
			this.functions.add(Pair.of(layer, f));		
		}

		 public void drawFilledRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height, Color color)
		 {
			 double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0, this.screenPxX);
			 double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0, this.screenPxY);
			 
			 double widthPx = this.screenPxX * width / this.cameraWidth;
		 	 double heightPx = this.screenPxY * height / this.cameraHeight;
			 
			 if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX ||
			    yScreenPx <= -heightPx || yScreenPx >= this.screenPxY) {
			    return;
			 }
			 
			 // thank you java very cool, I love not having ZSTs
			 Function<Graphics, Boolean> f = (Graphics g) -> 
			 {
				 g.setColor(color);
				 g.fillRect((int)xScreenPx, (int)yScreenPx, (int)widthPx, (int)heightPx); 
				 
				 return true;
			 };
			 
			 this.functions.add(Pair.of(layer, f));		 	 
	    }
		 
		 public void drawTexturedRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height, Image img)
		 {
			 double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0, this.screenPxX);
			 double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0, this.screenPxY);

		 	 double widthPx = this.screenPxX * width / this.cameraWidth;
		 	 double heightPx = this.screenPxY * height / this.cameraHeight;
		 	 
			 
			 if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX ||
			    yScreenPx <= -heightPx || yScreenPx >= this.screenPxY) {
			    return;
			 }
			 
			 Function<Graphics, Boolean> f = (Graphics g) ->
			 {
				 this.g.drawImage(img, (int)xScreenPx, (int)yScreenPx, (int)widthPx, (int)heightPx, null);
				 
				 return true;
			 };

			 this.functions.add(Pair.of(layer, f));
		 }
		 
		 public void dispatch()
		 {
			 this.functions.sort((l, r) -> l.getLeft().compareTo(r.getLeft()));
			 
			 for (Pair<Integer, Function<Graphics, Boolean>> f : this.functions)
			 {
				 Function<Graphics, Boolean> func = f.getRight();
				 
				 func.apply(this.g);
			 }
		 }
		 
	}
	
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
			synchronized (renderer.entities)
			{
				renderer.tickAndDraw();
			}
			
			// because of Java Things :tm: if we want shitcraft to stay alive, we need to use it 
			retainer(shitcraft);
		}
	}
	
	private static void retainer(Object o) {}
}


