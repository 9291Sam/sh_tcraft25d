package com.sebs.shtcraft_25d;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheetLoader {
	private BufferedImage[][] frames;

	public SpriteSheetLoader(String spriteSheetPath, int frameWidth, int frameHeight)
			throws IOException, FileNotFoundException {
		try {
			// Load the sprite sheet image
			BufferedImage spriteSheet = ImageIO.read(new File(spriteSheetPath));

			// Calculate the number of frames in each dimension
			int numCols = spriteSheet.getWidth() / frameWidth;
			int numRows = spriteSheet.getHeight() / frameHeight;

			// Initialize the frames array
			frames = new BufferedImage[numCols][numRows];

			// Extract each frame from the sprite sheet
			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < numCols; col++) {
					frames[row][col] = spriteSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth,
							frameHeight);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * frames retrieved based on its respective col and row read in by Sprite sheet
	 * loader
	 * 
	 * @return frames
	 */
	public BufferedImage[][] getFrames() {
		return frames;
	}

	public BufferedImage getFrame(int col, int row) {
		return frames[col][row];
	}
}