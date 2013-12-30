package com.f1bilisim.ardrone.video;


public class ImageSlice {
	MacroBlock[] MacroBlocks;

	ImageSlice(int macroBlockCount) {
		MacroBlocks = new MacroBlock[macroBlockCount];

		for (int index = 0; index < macroBlockCount; index++) {
			MacroBlocks[index] = new MacroBlock();
		}
	}
}