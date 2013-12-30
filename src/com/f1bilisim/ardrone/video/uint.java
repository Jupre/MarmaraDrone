package com.f1bilisim.ardrone.video;

import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.nio.ByteBuffer;


public class uint {


	public String toString() {
		return Integer.toString(base2, 2);
	}

	public uint(int base) {
		this.base2 = base;

	}

	public uint(uint that) {
		this.base2 = that.base2;
	}

	public uint(byte[] bp, int start) {
		try {
			byte[] b = new byte[4];
			b[0] = bp[start + 3];
			b[1] = bp[start + 2];
			b[2] = bp[start + 1];
			b[3] = bp[start + 0];
			
			ByteArrayInputStream bas = new ByteArrayInputStream(b);
			DataInputStream din = new DataInputStream(bas);

			this.base2 = din.readInt();
		} catch (Exception e) {
			throw new RuntimeException("error creating uint", e);
		}
	}
	
	
	public uint(ByteBuffer bp, int start) {
		try {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.put(bp.array()[start + 3]);
			bb.put(bp.array()[start + 2]);
			bb.put(bp.array()[start + 1]);
			bb.put(bp.array()[start + 0]);
			bb.flip();
			this.base2 = bb.getInt();
		} catch (Exception e) {
			throw new RuntimeException("error creating uint", e);
		}
	}

	private int base2;

	public short times(short i) {
		return (short) (intValue() * i);
	}

	public uint shiftRight(int i) {
		// System.out.println("shiftRight[0] " + base2 + " " + i);

		// String str = Integer.toBinaryString(base);
		int base = base2;
		// System.out.println("shiftRight[n][1] " + uint.toBinaryString(base));

		base = base >>> i;

		// System.out.println("shiftRight[n][2] " + uint.toBinaryString(base));

		return new uint(base);
	}

	public uint shiftLeft(int i) {
		int base = base2;
		base <<= i;

		return new uint(base);
		// return Integer.parseInt(base, 2);
	}

	public int flipBits() {
		int base = ~base2;

		return base;
	}

	public int intValue() {
		return base2;

	}

	public uint and(int andval) {
		int retval = base2 & andval;
		return new uint(retval);
	}

	public void shiftLeftEquals(int i) {
		int base = base2;

		base <<= i;

		base2 = base;
	}

	public void shiftRightEquals(int i) {
		int base = base2;

		base >>>= i;

		base2 = base;
	}

	public uint or(uint orval) {
		int retval = base2 | orval.base2;
		return new uint(retval);
	}

	
}
