package com.sebs.shtcraft_25d;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;
import com.sebs.shtcraft_25d.SpriteAnimation.AnimationType;

import glm.vec._2.Vec2;

public class PlayerManager implements Renderer.Entity {
	Renderer renderer;
	private SpriteAnimation animations;

	double playerX;
	double playerY;

	// may be the zero vector
	Vec2 playerTravelDirection;

	boolean wasFireKeyPressed = false;

	final double playerWidth = 3.0;
	final double playerHeight = 3.0;

	BufferedImage[] sprites;

	public PlayerManager(Renderer renderer_) throws InputMismatchException, IOException, FileNotFoundException {
		this.renderer = renderer_;
		// start reading in animations
		this.animations = new SpriteAnimation();
//		this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png");

		this.playerX = 0.0;
		this.playerY = 0.0;
		this.playerTravelDirection = new Vec2(1.0, 0.0);
	}

	@Override
	public void tick(double deltaTime) {

		double oldPlayerX = this.playerX;
		double oldPlayerY = this.playerY;

		this.playerX = this.renderer.getCameraXWorld();
		this.playerY = this.renderer.getCameraYWorld();

		Vec2 maybeNewDir = new Vec2(this.playerX - oldPlayerX, this.playerY - oldPlayerY).normalize();

		if (maybeNewDir.length() != 0.0 && !Float.isNaN(maybeNewDir.x) && !Float.isNaN(maybeNewDir.y)) {
			this.playerTravelDirection = maybeNewDir;
		}

		if (renderer.isKeyPressed(KeyEvent.VK_F)) {
			if (!wasFireKeyPressed) {
				renderer.getItemManager()
						.registerWorldEntity(new BlasterBullet(new Vec2(playerX, playerY), this.playerTravelDirection));
			}
			wasFireKeyPressed = true;

		} else {
			wasFireKeyPressed = false;
		}

	}

	/**
	 * Checks and manages the direction type of the animations in accordance to how
	 * the player moves.
	 * 
	 * @return Array of buffered image animations based on direction
	 */
	public BufferedImage[] spriteImageDirection() throws IOException, InputMismatchException {
		int lastKey = 0;

		// TODO: use move dir vectors
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
		}
		return null;
	}

	@Override
	public void draw(DrawCallCollector d) {

		try {
			BufferedImage[] maybeNewSprites = spriteImageDirection();
			if (maybeNewSprites != null) {
				this.sprites = maybeNewSprites;
			}

			if (this.sprites != null && this.sprites.length > 0) {

				for (BufferedImage frame : this.sprites) {
					d.drawTexturedRectangleWorld(this.playerX - 0.475 * playerWidth, this.playerY + 0.25 * playerHeight,
							3, playerWidth, playerHeight, frame);
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