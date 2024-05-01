package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils
{
	public static Image loadImage(String filePath) 
	{
		Image i = null; 
		
		try
		{
			i = ImageIO.read(new File(filePath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if (i == null)
		{
			System.out.printf("Failed to load %s\n", filePath);
			
			throw new RuntimeException("Failed to load image" + filePath);
		}
		
		return i;
	}
	
	public static double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public static double clamp(double x, double min, double max)
	{
		if (x < min)
		{
			return min;
		}
		
		if (x > max)
		{
			return max;
		}
		
		return x;
	}

}
