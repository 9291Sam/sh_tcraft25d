package com.sebs.shtcraft_25d;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.SpriteAnimation.AnimationType;

public class PlayerManager implements Renderer.Entity {
	Renderer renderer;
	private SpriteAnimation animations;

	double playerX;
	double playerY;

	public PlayerManager(Renderer renderer_) throws InputMismatchException, IOException, FileNotFoundException {
		this.renderer = renderer_;
		// start reading in animations
		this.animations = new SpriteAnimation();
//		this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png");

		this.playerX = 0.0;
		this.playerY = 0.0;
	}

	@Override
	public void tick(double deltaTime) {
		this.playerX = this.renderer.getCameraXWorld();
		this.playerY = this.renderer.getCameraYWorld();
	}

	/**
	 * Checks and manages the direction type of the animations in accordance to how
	 * the player moves.
	 * 
	 * @return Array of buffered image animations based on direction
	 */
	public BufferedImage[] spriteImageDirection() throws IOException, InputMismatchException {
		int lastKey = 0;
		if (renderer.isKeyPressed(KeyEvent.VK_A)) {// player moves left
			lastKey = KeyEvent.VK_A;
			this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png", "A");
			return animations.getAnimations(AnimationType.walk_L);

		} else if (renderer.isKeyPressed(KeyEvent.VK_D)) {// player moves right
			this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png", "D");
			lastKey = KeyEvent.VK_R;
			return animations.getAnimations(AnimationType.walk_R);

		} else if (renderer.isKeyPressed(KeyEvent.VK_W)) {// player moves fowards
			this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png", "W");
			lastKey = KeyEvent.VK_F;
			return animations.getAnimations(AnimationType.walk_F);

		} else if (renderer.isKeyPressed(KeyEvent.VK_S)) {// player moves backwards
			this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png", "S");
			lastKey = KeyEvent.VK_B;
			return animations.getAnimations(AnimationType.walk_B);

		} else { // stand images

			if (lastKey == KeyEvent.VK_W) { // front stand
				return animations.getAnimations(AnimationType.stand_F);

			} else if (lastKey == KeyEvent.VK_A) { // left stand
				return animations.getAnimations(AnimationType.stand_L);

			} else if (lastKey == KeyEvent.VK_S) { // back stand
				return animations.getAnimations(AnimationType.stand_B);

			} else if (lastKey == KeyEvent.VK_D) { // right stand
				return animations.getAnimations(AnimationType.stand_R);
			}
		}
		return null;
	}

	@Override
	public void draw(DrawCallCollector d) {
		BufferedImage[] playerFrames;
		try {
			playerFrames = spriteImageDirection();
			if (playerFrames != null && playerFrames.length > 0) {
				final double playerWidth = 1.0;
				final double playerHeight = 1.0;

				for (BufferedImage frame : playerFrames) {
					d.drawTexturedRectangleWorld(this.playerX - 0.5 * playerWidth, this.playerY - 0.5 * playerHeight, 0,
							playerWidth, playerHeight, frame);
				}

			}

		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}