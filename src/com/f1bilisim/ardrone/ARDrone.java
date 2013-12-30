
package com.f1bilisim.ardrone;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import com.f1bilisim.ardrone.command.CommandManager;
import com.f1bilisim.ardrone.navdata.AttitudeListener;
import com.f1bilisim.ardrone.navdata.BatteryListener;
import com.f1bilisim.ardrone.navdata.DroneState;
import com.f1bilisim.ardrone.navdata.NavDataManager;
import com.f1bilisim.ardrone.navdata.StateListener;
import com.f1bilisim.ardrone.navdata.VelocityListener;
import com.f1bilisim.ardrone.navdata.javadrone.NavData;
import com.f1bilisim.ardrone.navdata.javadrone.NavDataListener;
import com.f1bilisim.ardrone.utils.ARDroneUtils;
import com.f1bilisim.ardrone.video.ImageListener;
import com.f1bilisim.ardrone.video.VideoManager;

public class ARDrone implements ARDroneInterface{

	/** default ip address */
	private static final String IP_ADDRESS="192.168.1.1";

	private String ipaddr=null; 
	private InetAddress inetaddr=null;

	//managerlar
	private CommandManager manager=null;
	private VideoManager videoManager=null;
	private NavDataManager navdataManager=null;

	//dinleyiciler
	private ImageListener imageListener=null;
	private AttitudeListener attitudeListener=null;
	private BatteryListener batteryListener=null;
	private StateListener stateListener=null;
	private VelocityListener velocityListener=null;
	private NavDataListener navDataListener=null;

	/** Yap�c� */
	public ARDrone(){
		this(IP_ADDRESS);
	}
	/**
	 * yap�c�
	 * @param ipaddr
	 */
	public ARDrone(String ipaddr){
		this.ipaddr=ipaddr;
	}

	/**  AR.Drone ba�lant�s� */
	@Override
	public boolean connect() {
		if(inetaddr==null){
			inetaddr=getInetAddress(ipaddr);
		}
		manager=new CommandManager(inetaddr);
		return manager.connect(ARDroneUtils.PORT);
	}

	/**  video ba�lant�s� */
	@Override
	public boolean connectVideo() {
		if(inetaddr==null){
			inetaddr=getInetAddress(ipaddr);
		}
		videoManager=new VideoManager(inetaddr, manager);
		videoManager.setImageListener(new ImageListener() {
			@Override
			public void imageUpdated(BufferedImage image) {
				if(imageListener!=null){
					imageListener.imageUpdated(image);
				}
			}
		});
		return videoManager.connect(ARDroneUtils.VIDEO_PORT);
	}

	/** navdata ba�lant�s� */
	@Override
	public boolean connectNav() {
		if(inetaddr==null){
			inetaddr=getInetAddress(ipaddr);
		}
		navdataManager=new NavDataManager(inetaddr, manager);
		navdataManager.setAttitudeListener(new AttitudeListener() {
			@Override
			public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
				if(attitudeListener!=null){
					attitudeListener.attitudeUpdated(pitch, roll, yaw, altitude);
				}
			}
		});
		navdataManager.setBatteryListener(new BatteryListener() {
			@Override
			public void batteryLevelChanged(int percentage) {
				if(batteryListener!=null){
					batteryListener.batteryLevelChanged(percentage);
				}
			}
		});
		navdataManager.setStateListener(new StateListener() {
			@Override
			public void stateChanged(DroneState state) {
				if(stateListener!=null){
					stateListener.stateChanged(state);
				}
			}
		});

		navdataManager.setVelocityListener(new VelocityListener() {
			@Override
			public void velocityChanged(float vx, float vy, float vz) {
				if(velocityListener!=null){
					velocityListener.velocityChanged(vx, vy, vz);
				}
			}
		});
		navdataManager.setNavDataListener(new NavDataListener() {
			@Override
			public void navDataUpdated(NavData navData) {
				if(navDataListener!=null){
					navDataListener.navDataUpdated(navData);
				}
			}
		});

		return navdataManager.connect(ARDroneUtils.NAV_PORT);
	}

	@Override
	public void disconnect() {
		stop();
		landing();
		manager.close();
		if(videoManager!=null)
			videoManager.close();
		if(navdataManager!=null)
			navdataManager.close();
	}

	@Override
	public void start() {
		if(manager!=null)
			new Thread(manager).start();
		if(videoManager!=null)
			new Thread(videoManager).start();
		if(navdataManager!=null)
			new Thread(navdataManager).start();
	}

	@Override
	public void setHorizontalCamera() {
		if(manager!=null)
			manager.setHorizontalCamera();
	}

	@Override
	public void setVerticalCamera() {
		if(manager!=null)
			manager.setVerticalCamera();
	}

	@Override
	public void setHorizontalCameraWithVertical() {
		if(manager!=null)
			manager.setHorizontalCameraWithVertical();
	}

	@Override
	public void setVerticalCameraWithHorizontal() {
		if(manager!=null)
			manager.setVerticalCameraWithHorizontal();
	}

	@Override
	public void toggleCamera() {
		if(manager!=null)
			manager.toggleCamera();
	}

	@Override
	public void landing() {
		if(manager!=null)
			manager.landing();
	}

	@Override
	public void takeOff() {
		if(manager!=null)
			manager.takeOff();
	}

	@Override
	public void reset() {
		if(manager!=null)
			manager.reset();
	}

	@Override
	public void forward() {
		if(manager!=null)
			manager.forward();
	}

	@Override
	public void forward(int speed) {
		if(manager!=null)
			manager.forward(speed);
	}

	@Override
	public void backward() {
		if(manager!=null)
			manager.backward();
	}

	@Override
	public void backward(int speed) {
		if(manager!=null)
			manager.backward(speed);
	}

	@Override
	public void spinRight() {
		if(manager!=null)
			manager.spinRight();
	}

	@Override
	public void spinRight(int speed) {
		if(manager!=null)
			manager.spinRight(speed);
	}

	@Override
	public void spinLeft() {
		if(manager!=null)
			manager.spinLeft();
	}

	@Override
	public void spinLeft(int speed) {
		if(manager!=null)
			manager.spinLeft(speed);
	}

	@Override
	public void up() {
		if(manager!=null)
			manager.up();
	}

	@Override
	public void up(int speed) {
		if(manager!=null)
			manager.up(speed);
	}

	@Override
	public void down() {
		if(manager!=null)
			manager.down();
	}

	@Override
	public void down(int speed) {
		if(manager!=null)
			manager.down(speed);
	}

	@Override
	public void goRight() {
		if(manager!=null)
			manager.goRight();
	}

	@Override
	public void goRight(int speed) {
		if(manager!=null)
			manager.goRight(speed);
	}

	@Override
	public void goLeft() {
		if(manager!=null)
			manager.goLeft();
	}

	@Override
	public void goLeft(int speed) {
		if(manager!=null)
			manager.goLeft(speed);
	}

	@Override
	public void setSpeed(int speed) {
		if(manager!=null)
			manager.setSpeed(speed);
	}

	@Override
	public void stop() {
		if(manager!=null)
			manager.stop();
	}

	/**
	 * 0.01-1.0 -> 1-100%
	 * @return 1-100%
	 */
	@Override
	public int getSpeed() {
		if(manager==null)
			return -1;
		return manager.getSpeed();
	}

	@Override
	public void setMaxAltitude(int altitude){
		if(manager!=null)
			manager.setMaxAltitude(altitude);
	}

	@Override
	public void setMinAltitude(int altitude){
		if(manager!=null)
			manager.setMinAltitude(altitude);
	}

	
	

	//update listeners
	public void addImageUpdateListener(ImageListener imageListener){
		this.imageListener=imageListener;
	}
	public void addAttitudeUpdateListener(AttitudeListener attitudeListener){
		this.attitudeListener=attitudeListener;
	}
	public void addBatteryUpdateListener(BatteryListener batteryListener){
		this.batteryListener=batteryListener;
	}
	public void addStateUpdateListener(StateListener stateListener){
		this.stateListener=stateListener;
	}
	public void addVelocityUpdateListener(VelocityListener velocityListener){
		this.velocityListener=velocityListener;
	}
	public void addNavDataListener(NavDataListener navDataListener)	{
		this.navDataListener = navDataListener;
	}
	//dinleyicileri kald�rma
	public void removeImageUpdateListener(){
		imageListener=null;
	}
	public void removeAttitudeUpdateListener(){
		attitudeListener=null;
	}
	public void removeBatteryUpdateListener(){
		batteryListener=null;
	}
	public void removeStateUpdateListener(){
		stateListener=null;
	}
	public void removeVelocityUpdateListener(){
		velocityListener=null;
	}
	public void removeNavDataListener(){
		navDataListener=null;
	}

	/**
	 * print error message
	 * @param message
	 * @param obj
	 */
	public static void error(String message, Object obj){
		System.err.println("["+obj.getClass()+"] "+message);
	}

	private InetAddress getInetAddress(String ipaddr){
		InetAddress inetaddr=null;
		StringTokenizer st=new StringTokenizer(ipaddr, ".");
		byte[] ipBytes=new byte[4];
		if(st.countTokens()==4){
			for(int i=0; i<4; i++){
				ipBytes[i]=(byte) Integer.parseInt(st.nextToken());
			}
		}else{
			error("Yanl�� IP adresi bi�imi: "+ipaddr, this);
			return null;
		}
		try {
			inetaddr=InetAddress.getByAddress(ipBytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inetaddr;
	}
}