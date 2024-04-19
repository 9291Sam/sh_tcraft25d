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
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;
import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Shitcraft.PlayerManager;

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
	private final int windowWidthPx, windowHeightPx;
	private double cameraX, cameraY;
	
	public Renderer()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setPreferredSize(new Dimension(width, height));
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
	
	public double getCameraXWorld()
	{
		return this.cameraX;
	}
	
	public double getCameraYWorld()
	{
		return this.cameraY;
	}
	
	public int getWindowWidthPx() {
	    return this.windowWidthPx;
	}
	
	public int getWindowHeightPx() {
	    return this.windowHeightPx;
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
		
		double cameraMoveSpeed = 5.0;
		
		
		for (WeakReference<Entity> e : this.entities)
		{
			Entity maybeEntity = e.get();
			
			if (maybeEntity != null)
				keyPressed(deltaTime, cameraMoveSpeed);
				this.cameraX = PlayerManager.getX();
				this.cameraY = PlayerManager.getY();
				
			{
				maybeEntity.tick(deltaTime);
			}
		}
		
		nanosPrev = now;
		
		long nanosAfter = System.nanoTime();
		
		
		this.repaint();
	}
	
	final static class keyPressed {
		private static double x;
		private static double y;
		
		public keyPressed(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public static double getX() {
			return x;
		}
		
		public static double getY() {
			return y;
		}
	}
	
	public keyPressed keyPressed (double deltaTime, double cameraMoveSpeed) 
	{
		double deltaX = 0;
		double deltaY = 0;
		if ((this.isKeyPressed.get(KeyEvent.VK_W))) {
			deltaY += (cameraMoveSpeed * deltaTime);
		}
		if (this.isKeyPressed.get(KeyEvent.VK_S)) {
			deltaY -= (cameraMoveSpeed * deltaTime);
		}
		if (this.isKeyPressed.get(KeyEvent.VK_A)) {
			deltaX -= (cameraMoveSpeed * deltaTime);
		}
		if (this.isKeyPressed.get(KeyEvent.VK_D)) {
			deltaX += (cameraMoveSpeed * deltaTime);
		} 
		
		this.cameraX += deltaX;
		this.cameraY += deltaY;
		
		return new keyPressed(cameraX, cameraY);
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

		 public void drawFilledRectangle(double xWorld, double yWorld, Integer layer, double width, double height, Color color)
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
		 
		 public void drawTexturedRectangle(double xWorld, double yWorld, Integer layer, double width, double height, Image img)
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
	
	private void run()
	{
		
	}
	
	private static void retainer(Object o) {}
}


