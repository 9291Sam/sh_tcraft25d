package com.sebs.shtcraft_25d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils
{
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

}