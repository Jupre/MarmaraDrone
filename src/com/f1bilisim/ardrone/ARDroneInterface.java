
package com.f1bilisim.ardrone;

public interface ARDroneInterface {

	//baðlantý
	public boolean connect();
	public boolean connectVideo();
	public boolean connectNav();
	public void disconnect();

	public void start();
	
	//kamera
	public void setHorizontalCamera();//setFrontCameraStreaming()
	public void setVerticalCamera();//setBellyCameraStreaming()
	public void setHorizontalCameraWithVertical();//setFrontCameraWithSmallBellyStreaming()
	public void setVerticalCameraWithHorizontal();//setBellyCameraWithSmallFrontStreaming()
	public void toggleCamera();
	
	//kontrol 
	public void landing();
	public void takeOff();
	public void reset();
	public void forward();
	public void forward(int speed);
	public void backward();
	public void backward(int speed);
	public void spinRight();
	public void spinRight(int speed);
	public void spinLeft();
	public void spinLeft(int speed);
	public void up();
	public void up(int speed);
	public void down();
	public void down(int speed);
	public void goRight();
	public void goRight(int speed);
	public void goLeft();
	public void goLeft(int speed);
	public void stop();
	
	//getter
	public int getSpeed();
	//setter
	public void setSpeed(int speed);
	
	//max/min yükseklik ayarlama
	public void setMaxAltitude(int altitude);
	public void setMinAltitude(int altitude);
	
	
}
