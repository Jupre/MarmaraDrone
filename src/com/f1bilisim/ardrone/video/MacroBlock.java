package com.f1bilisim.ardrone.video;



public class MacroBlock {
	// /#//#b�lge 

	// private int _BlockWidth = 8;
	// private int _BlockSize = 64;

	// //#b�lge bitimi

	// //#b�lge �zellikleri

	short[][] DataBlocks;

	// //#b�lge bitimi

	// //#b�lge kar���kl�klar�

	MacroBlock() {
		DataBlocks = new short[6][];

		for (int index = 0; index < 6; index++) {
			DataBlocks[index] = new short[64];
		}
	}

	// //#b�lge bitimi
}