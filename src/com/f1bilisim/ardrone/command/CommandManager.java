
package com.f1bilisim.ardrone.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.f1bilisim.ardrone.manager.AbstractManager;

public class CommandManager extends AbstractManager{

	private static final String CR="\r";

	private static final String SEQ = "$SEQ$";

	private static int seq=1;

	private FloatBuffer fb=null;
	private IntBuffer ib=null;

	private boolean landing=true;
	private boolean continuance=false;
	private String command=null;
	
	/** hız */
	private float speed=0.05f;//0.01f - 1.0f
		
	public CommandManager(InetAddress inetaddr){
		this.inetaddr=inetaddr;
		initialize();
	}
	//Komutlar
	//Kamera komutu
	
	public void setHorizontalCamera() {
		//command="AT*ZAP="+SEQ+",0";
		command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"0\"";
		continuance=false;
		//setCommand("AT*ZAP="+SEQ+",0", false);
	}

	//Kamera komutu
	public void setVerticalCamera() {
		//command="AT*ZAP="+SEQ+",1";
		command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"1\"";
		continuance=false;
		//setCommand("AT*ZAP="+SEQ+",1", false);
	}

	//Kamera komutu
	public void setHorizontalCameraWithVertical() {
		//command="AT*ZAP="+SEQ+",2";
		command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"2\"";
		continuance=false;
		//setCommand("AT*ZAP="+SEQ+",2", false);
	}

	//Kamera komutu
	public void setVerticalCameraWithHorizontal() {
		//command="AT*ZAP="+SEQ+",3";
		command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"3\"";
		continuance=false;
		//setCommand("AT*ZAP="+SEQ+",3", false);
	}

	//Kamera komutu
	public void toggleCamera() {
		//command="AT*ZAP="+SEQ+",4";
		command="AT*CONFIG="+SEQ+",\"video:video_channel\",\"4\"";
		continuance=false;
		//setCommand("AT*ZAP="+SEQ+",4", false);
	}

	//iniş
	public void landing() {
		System.out.println("*** Landing");
		command="AT*REF=" + SEQ + ",290717696";
		continuance=false;
		//setCommand("AT*REF=" + SEQ + ",290717696", false);
		landing=true;		
	}

	//Kalkış
	public void takeOff() {
		System.out.println("*** Take off");
		sendCommand("AT*FTRIM="+SEQ);
		command="AT*REF=" + SEQ + ",290718208";
		continuance=false;
		//setCommand("AT*REF=" + SEQ + ",290718208", false);
		landing=false;		
	}

	//Sıfırla
	public void reset() {
		System.out.println("*** Sıfırla");
		command="AT*REF="+SEQ+",290717952";
		continuance=true;
		//setCommand("AT*REF="+SEQ+",290717952", true);
		landing=true;		
	}

	//ileri
	public void forward() {
		command="AT*PCMD="+SEQ+",1,0,"+intOfFloat(-speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
		//setCommand("AT*PCMD="+SEQ+",1,0,"+intOfFloat(-speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208", true);
	}

	//ileri hızı
	public void forward(int speed) {
		setSpeed(speed);
		forward();
	}

	//Geri
	public void backward() {
		command="AT*PCMD="+SEQ+",1,0,"+intOfFloat(speed)+",0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	//geri hızı
	public void backward(int speed) {
		setSpeed(speed);
		backward();
	}

	//Sağa dön
	public void spinRight() {
		command="AT*PCMD=" + SEQ + ",1,0,0,0," + intOfFloat(speed)+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	//Sağa dönme hızı
	public void spinRight(int speed) {
		setSpeed(speed);
		spinRight();
	}

	//Sola dön
	public void spinLeft() {
		command="AT*PCMD=" + SEQ + ",1,0,0,0," + intOfFloat(-speed)+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	//Sola dönme hızı
	public void spinLeft(int speed) {
		setSpeed(speed);
		spinLeft();
	}

	//Yukarı
	public void up() {
		System.out.println("*** Up");
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(0)+","+intOfFloat(0)+","+intOfFloat(speed)+","+intOfFloat(0)+"\r"+"AT*REF="+SEQ+",290718208";
		continuance=true;
	}

	//Yukarı çıkış hızı
	public void up(int speed) {
		setSpeed(speed);
		up();
	}

	//Aşağı
	public void down() {
		System.out.println("*** Down");
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(0)+","+intOfFloat(0)+","+intOfFloat(-speed)+","+intOfFloat(0)+"\r"+"AT*REF="+SEQ+",290718208";
		continuance=true;
	}

	//Aşağı iniş hızı
	public void down(int speed) {
		setSpeed(speed);
		down();
	}

	//sağa git
	public void goRight() {
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(speed)+",0,0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	//sağa gitme hızı
	public void goRight(int speed) {
		setSpeed(speed);
		goRight();
	}

	//sola git
	public void goLeft() {
		command="AT*PCMD="+SEQ+",1,"+intOfFloat(-speed)+",0,0,0"+"\r"+"AT*REF=" + SEQ + ",290718208";
		continuance=true;
	}

	//sola gitme hızı
	public void goLeft(int speed) {
		setSpeed(speed);
		goLeft();
	}

	
	//Dur
	public void stop() {
		System.out.println("*** Dur (Hover)");
		command="AT*PCMD="+SEQ+",1,0,0,0,0";
		continuance=true;
	}

	//hız ayarla
	public void setSpeed(int speed) {
		if(speed>100)
			speed=100;
		else if(speed<1)
			speed=1;

		this.speed=(float) (speed/100.0);
	}
	
//videoyu başlat
	public void enableVideoData(){
		command="AT*CONFIG="+SEQ+",\"general:video_enable\",\"TRUE\""+CR+"AT*FTRIM="+SEQ;
		continuance=false;
		//setCommand("AT*CONFIG="+SEQ+",\"general:video_enable\",\"TRUE\""+CR+"AT*FTRIM="+SEQ, false);
	}
	
	public void enableDemoData(){
		command="AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"TRUE\""+CR+"AT*FTRIM="+SEQ;
		continuance=false;
		//setCommand("AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"TRUE\""+CR+"AT*FTRIM="+SEQ, false);
	}

	public void sendControlAck(){
		command="AT*CTRL="+SEQ+",0";
		continuance=false;
		//setCommand("AT*CTRL="+SEQ+",0", false);
	}
	
	public int getSpeed(){
		return (int) (speed*100);
	}
	
	public void disableAutomaticVideoBitrate(){
		command="AT*CONFIG="+SEQ+",\"video:bitrate_ctrl_mode\",\"0\"";
		continuance=false;
	}
//maksimum yükseklik
	public void setMaxAltitude(int altitude){
		command="AT*CONFIG="+SEQ+",\"control:altitude_max\",\""+altitude+"\"";
		continuance=false;
	}
	//min yükseklik
	public void setMinAltitude(int altitude){
		command="AT*CONFIG="+SEQ+",\"control:altitude_min\",\""+altitude+"\"";
		continuance=false;
	}

	

	
	@Override
	public void run() {
		initARDrone();
		while(!doStop){
			if(this.command!=null){
				sendCommand();
				if(!continuance){
					command=null;
				}
			}else{
				if(landing){
					sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696");
				}else{
					sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290718208");
				}
			}
			if(seq%5==0){//<2000ms
				sendCommand("AT*COMWDG="+SEQ);
			}
		}
	}
	

	private void initialize(){
		ByteBuffer bb=ByteBuffer.allocate(4);
		fb=bb.asFloatBuffer();
		ib=bb.asIntBuffer();
	}
	
	private void initARDrone(){
		sendCommand("AT*CONFIG="+SEQ+",\"general:navdata_demo\",\"TRUE\""+CR+"AT*FTRIM="+SEQ);//1
		sendCommand("AT*PMODE="+SEQ+",2"+CR+"AT*MISC="+SEQ+",2,20,2000,3000"+CR+"AT*FTRIM="+SEQ+CR+"AT*REF="+SEQ+",290717696");//2-5
		sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696"+CR+"AT*COMWDG="+SEQ);//6-8
		sendCommand("AT*PCMD="+SEQ+",1,0,0,0,0"+CR+"AT*REF="+SEQ+",290717696"+CR+"AT*COMWDG="+SEQ);//6-8
		sendCommand("AT*FTRIM="+SEQ);
		System.out.println("Initialize completed!");
	}
	
	/*private void setCommand(String command, boolean continuance){
		this.command=command;
		this.continuance=continuance;
	}*/

	
	private void sendCommand(){
		sendCommand(this.command);
	}
	
	private synchronized void sendCommand(String command){
	
		int seqIndex = -1;
		while ((seqIndex = command.indexOf(SEQ)) != -1)
		{
			command = command.substring(0, seqIndex) + (seq++) + command.substring(seqIndex + SEQ.length());
		} 
		
		byte[] buffer=(command+CR).getBytes();
//		System.out.println(command);
		DatagramPacket packet=new DatagramPacket(buffer, buffer.length, inetaddr, 5556);
		try {
			socket.send(packet);
			Thread.sleep(20);//<50ms			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int intOfFloat(float f) {
		fb.put(0, f);
		return ib.get(0);
	}
}