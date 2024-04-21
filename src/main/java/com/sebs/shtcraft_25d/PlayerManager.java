package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.Renderer.keyPressed;

public class PlayerManager implements Renderer.Entity
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
			d.drawTexturedRectangleWorld(x, y, 1, width, height, playerImage);;
		}
	}
	
	public static double getX() {
		return x;
	}
	
	public static double getY() {
		return y;
	}
}