
package com.f1bilisim.ardrone.navdata;

public interface AttitudeListener {
	void attitudeUpdated(float pitch, float roll, float yaw, int altitude);
}
