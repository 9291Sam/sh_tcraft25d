package com.sebs.shtcraft_25d;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * 
 */
public class SpriteAnimation {
	/**
	 * each frame in sprite sheet is an 64 x 64 px size. 8 rows, 8 cols of
	 * animations for each sheet. total size of sheet for 512 x 512 px
	 */
	private Map<AnimationType, BufferedImage[]> animations;

	public SpriteAnimation() {
		animations = new HashMap<>();
	}

	public void standingStill(String file, String lastKey) throws IOException, InputMismatchException {

		SpriteSheetLoader loader = new SpriteSheetLoader(file, 64, 64);
		BufferedImage[][] frames = loader.getFrames();
		// frames loads all subimages based on their columns and rows respectively.
		// input frames[col][row] to pull subimage and organize into enums.
		final int MAX_SIZE = 6;
		BufferedImage[] arr = new BufferedImage[MAX_SIZE];

		if (lastKey == "S") {
			arr[0] = frames[0][1]; // forwards
			animations.put(AnimationType.stand_B, arr);
		} else if (lastKey == "W") {
			arr[0] = frames[0][1]; // forwards
			animations.put(AnimationType.stand_F, arr);
		} else if (lastKey == "D") {
			arr[0] = frames[0][1]; // forwards
			animations.put(AnimationType.stand_R, arr);
		} else if (lastKey == "A") {
			arr[0] = frames[0][1]; // forwards
			animations.put(AnimationType.stand_L, arr);
		}

	}

	/**
	 * Organize subimages into map
	 */
	public void loadAnimations(String file, String key) throws IOException, InputMismatchException {
		SpriteSheetLoader loader = new SpriteSheetLoader(file, 64, 64);
		BufferedImage[][] frames = loader.getFrames();
		// frames loads all subimages based on their columns and rows respectively.
		// input frames[col][row] to pull subimage and organize into enums.
		final int MAX_SIZE = 6;
		BufferedImage[] arr = new BufferedImage[MAX_SIZE];

		/**************** STAND ANIMATIONS *************************/
//		arr[0] = frames[0][1]; // forwards
//		animations.put(AnimationType.stand_F, arr);
//		
//		arr[0] = frames[0][1]; // forwards
//		animations.put(AnimationType.stand_F, arr);
//
//		arr[0] = frames[0][2]; // right
//		animations.put(AnimationType.stand_R, arr);
//
//		arr[0] = frames[0][3]; // left
//		animations.put(AnimationType.stand_L, arr);

		/**************** WALK ANIMATIONS *************************/
		// walk_F
		if (key == "W") {
			for (int i = 0; i < 6; i++) {
				arr[0] = frames[5][i];
				animations.put(AnimationType.walk_F, arr);
				// wait for the frame
			}
//			arr[0] = frames[5][0];
//			arr[1] = frames[5][1];
//			arr[2] = frames[5][2];
//			arr[3] = frames[5][3];
//			arr[4] = frames[5][4];
//			arr[5] = frames[5][5];
//			animations.put(AnimationType.walk_F, arr);
		}

		// walk_B
		if (key == "S") {
			for (int i = 0; i < 6; i++) {
				arr[0] = frames[4][i];
				animations.put(AnimationType.walk_B, arr);
				// wait for the frame
			}
//			arr[0] = frames[4][0];
//			arr[1] = frames[4][1];
//			arr[2] = frames[4][2];
//			arr[3] = frames[4][3];
//			arr[4] = frames[4][4];
//			arr[5] = frames[4][5];
//			animations.put(AnimationType.walk_B, arr);
		}

		// walk_L
		if (key == "A") {
			for (int i = 0; i < 6; i++) {
				arr[0] = frames[7][i];
				animations.put(AnimationType.walk_L, arr);
				// wait for the frame
			}
//			arr[0] = frames[7][0];
//			arr[1] = frames[7][1];
//			arr[2] = frames[7][2];
//			arr[3] = frames[7][3];
//			arr[4] = frames[7][4];
//			arr[5] = frames[7][5];
//			animations.put(AnimationType.walk_L, arr);
		}

		// walk_R
		if (key == "D") {
			for (int i = 0; i < 6; i++) {
				arr[0] = frames[6][i];
				animations.put(AnimationType.walk_R, arr);
				// wait for the frame
			}
//			arr[0] = frames[6][0];
//			arr[1] = frames[6][1];
//			arr[2] = frames[6][2];
//			arr[3] = frames[6][3];
//			arr[4] = frames[6][4];
//			arr[5] = frames[6][5];
//			animations.put(AnimationType.walk_R, arr);
		}

		/**************** RUN ANIMATIONS *************************/
		// run_F
//		arr[0] = frames[6][0];
//		arr[1] = frames[6][1];
//		arr[2] = frames[6][2];
//		arr[3] = frames[6][3];
//		arr[4] = frames[6][4];
//		arr[5] = frames[6][5];
//		animations.put(AnimationType.walk_F, arr);

		// run_B
//		arr[0] = frames[6][0];
//		arr[1] = frames[6][1];
//		arr[2] = frames[6][2];
//		arr[3] = frames[6][3];
//		arr[4] = frames[6][4];
//		arr[5] = frames[6][5];
//		animations.put(AnimationType.walk_F, arr);

		// run_L

//		animations.put(AnimationType.walk_F, arr);

		// run_R

//		animations.put(AnimationType.walk_F, arr);
	}

	public BufferedImage[] getAnimations(AnimationType e) {
		return animations.get(e);
	}

	public enum AnimationType {
		walk_F, walk_B, walk_L, walk_R, run_F, run_B, run_L, run_R, stand_F, stand_B, stand_L, stand_R, jump_F, jump_B,
		jump_L, jump_R,
	}
}
