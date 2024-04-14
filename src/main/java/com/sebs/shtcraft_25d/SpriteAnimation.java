package com.sebs.shtcraft_25d;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteAnimation {
	// each frame in sprite sheet is an 64 x 64 px size.
	// 8 rows, 8 cols of animations for each sheet. total size of sheet for 512 x 512 px
	private Map<AnimationType, BufferedImage[][]> animations;

	public SpriteAnimation() {
		animations = new HashMap<>();

	}

	public void loadAnimations() {
		SpriteSheetLoader loader = new 
				SpriteSheetLoader("Player_Char_Set/char_a_p1/char_a_p10bas_humn_v01.png", 64, 64 );
		BufferedImage[][] frames = loader.getFrames();
		//frames loads all subimages based on their columns and rows respectively.
		//input frames[col][row] to pull subimage and organize into enums.
		
		/*
		 * Organize subimages into map
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
	}

	private static enum AnimationType {
		walk_F, walk_B, walk_L, walk_R, run_F, run_B, run_L, run_R, stand_F, stand_B, stand_L, stand_R, jump_F, jump_B,
		jump_L, jump_R,
	}
}
