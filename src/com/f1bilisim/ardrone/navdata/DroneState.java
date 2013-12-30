
package com.f1bilisim.ardrone.navdata;

public class DroneState {
	private final int bits;
	
	public DroneState(int bits){
		this.bits=bits;
	}
	
	public String toString(){
		return "DroneState("+Integer.toHexString(bits)+")";
	}
	
	public boolean equals(Object o){
		if(o==null || o.getClass()!=getClass())
			return false;
		return bits==((DroneState)o).bits;
	}
	
	public int hashCode(){
		return 31*bits;
	}
}