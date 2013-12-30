package com.f1bilisim.ardrone.video;



public class MacroBlock {
	// /#//#bölge 

	// private int _BlockWidth = 8;
	// private int _BlockSize = 64;

	// //#bölge bitimi

	// //#bölge özellikleri

	short[][] DataBlocks;

	// //#bölge bitimi

	// //#bölge karýþýklýklarý

	MacroBlock() {
		DataBlocks = new short[6][];

		for (int index = 0; index < 6; index++) {
			DataBlocks[index] = new short[64];
		}
	}

	// //#bölge bitimi
}