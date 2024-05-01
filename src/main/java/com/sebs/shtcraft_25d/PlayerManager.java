package com.sebs.shtcraft_25d;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.InputMismatchException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sebs.shtcraft_25d.SpriteAnimation.AnimationType;

import glm.vec._2.Vec2;

public class PlayerManager implements Renderer.Entity {
	Renderer renderer;
	private SpriteAnimation animations;
	private String spriteSheet;
	private AudioManager audioManager;

	double playerX;
	double playerY;

	// variable for sum of delta time
	double timeAlive;

	// may be the zero vector
	Vec2 playerTravelDirection;

	boolean wasFireKeyPressed = false;
	int lastKey;

	final double playerWidth = 3.0;
	final double playerHeight = 3.0;
	private final double FRAME_DURATION = 0.1;
	BufferedImage[] sprites;

	WorldEntityManager worldEntityManager;
	
	public Vec2 getPosition()
	{
		return new Vec2(this.playerX, this.playerY);
	}
	
	public Vec2 getDimensions()
	{
		return new Vec2(this.playerWidth, this.playerHeight);
		
	}

	public PlayerManager(Renderer renderer_, WorldEntityManager worldEntityManager_, AudioManager audioManager_)
			throws InputMismatchException, IOException, FileNotFoundException {
		this.renderer = renderer_;
		// start reading in animations
		this.animations = new SpriteAnimation();
//		this.animations.loadAnimations("char_a_p1_0bas_humn_v01.png");
		this.spriteSheet = "char_a_p1_0bas_humn_v01.png";

		this.playerX = 0.0;
		this.playerY = 0.0;
		this.playerTravelDirection = new Vec2(1.0, 0.0);
		this.lastKey = 0;
		this.worldEntityManager = worldEntityManager_;
		this.audioManager = audioManager_;
	}

	@Override
	public void tick(double deltaTime) {
//		Calculate time alive
		this.timeAlive += deltaTime;

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
				
				this.worldEntityManager
						.registerWorldEntity(new BlasterBullet(new Vec2(playerX, playerY), this.playerTravelDirection));
				
				this.audioManager.playSound("pk-fire-sound-effect-made-with-Voicemod.wav");
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

		// TODO: use move dir vectors
		if (renderer.isKeyPressed(KeyEvent.VK_A)) {// player moves left
			lastKey = KeyEvent.VK_A;
			this.animations.loadAnimations(this.spriteSheet, "A");
			return animations.getAnimations(AnimationType.walk_L);

		} else if (renderer.isKeyPressed(KeyEvent.VK_D)) {// player moves right
			this.animations.loadAnimations(this.spriteSheet, "D");
			lastKey = KeyEvent.VK_D;
			return animations.getAnimations(AnimationType.walk_R);

		} else if (renderer.isKeyPressed(KeyEvent.VK_W)) {// player moves fowards
			this.animations.loadAnimations(this.spriteSheet, "W");
			lastKey = KeyEvent.VK_W;
			return animations.getAnimations(AnimationType.walk_F);

		} else if (renderer.isKeyPressed(KeyEvent.VK_S)) {// player moves backwards
			this.animations.loadAnimations(this.spriteSheet, "S");
			lastKey = KeyEvent.VK_S;
			return animations.getAnimations(AnimationType.walk_B);
		} else {
			this.animations.standingStill(spriteSheet, lastKey);
			return animations.getAnimations(AnimationType.stand);
		}
	}

	@Override
	public void draw(DrawCallCollector d) {

		try {
			BufferedImage[] maybeNewSprites = spriteImageDirection();
			if (maybeNewSprites != null) {
				this.sprites = maybeNewSprites;
			}

			if (this.sprites != null && this.sprites.length > 0) {

				int frameIndex = (int) ((this.timeAlive / FRAME_DURATION) % this.sprites.length);

//				// set frame
//				// pp should always be long
//				// i.e. no null frame data
//				System.out.println(frameIndex);
//				String pp = "";
//				if (sprites[frameIndex] == null) {
//					pp = "PP_small";
//
//				} else {
//					pp = "pp_LONG";
//				}
//				System.out.println("\n" + pp);

				// set frame
				BufferedImage frame = this.sprites[frameIndex];

				d.drawTexturedRectangleWorld(this.playerX - 0.475 * playerWidth, this.playerY + 0.25 * playerHeight, 3,
						playerWidth, playerHeight, frame);

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