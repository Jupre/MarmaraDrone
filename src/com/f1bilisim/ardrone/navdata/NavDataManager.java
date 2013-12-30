
package com.f1bilisim.ardrone.navdata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import com.f1bilisim.ardrone.command.CommandManager;
import com.f1bilisim.ardrone.manager.AbstractManager;
import com.f1bilisim.ardrone.navdata.javadrone.JavadroneNavDataParser;
import com.f1bilisim.ardrone.navdata.javadrone.NavDataListener;
import com.f1bilisim.ardrone.utils.ARDroneUtils;

public class NavDataManager extends AbstractManager{

	private CommandManager manager=null;
	
	//dinleyiciler
	private AttitudeListener attitudeListener=null;
	private StateListener stateListener=null;
	private VelocityListener velocityListener=null;
	private BatteryListener batteryListener=null;
	private NavDataListener navDataListener=null;
	
	public NavDataManager(InetAddress inetaddr, CommandManager manager){
		this.inetaddr=inetaddr;
		this.manager=manager;
	}
	
	public void setAttitudeListener(AttitudeListener attitudeListener){
		this.attitudeListener=attitudeListener;
	}
	public void setBatteryListener(BatteryListener batteryListener){
		this.batteryListener=batteryListener;
	}
	public void setStateListener(StateListener stateListener){
		this.stateListener=stateListener;
	}
	public void setVelocityListener(VelocityListener velocityListener){
		this.velocityListener=velocityListener;
	}
	public void setNavDataListener(NavDataListener navDataListener){
		this.navDataListener=navDataListener;
	}

	
	@Override
	public void run(){
		ticklePort(ARDroneUtils.NAV_PORT);
		manager.enableDemoData();
		ticklePort(ARDroneUtils.NAV_PORT);
		manager.sendControlAck();
		
		
		NavDataParser parser=new NavDataParser();
		parser.setAttitudeListener(attitudeListener);
		parser.setBatteryListener(batteryListener);
		parser.setStateListener(stateListener);
		parser.setVelocityListener(velocityListener);
		
		JavadroneNavDataParser javadroneParser=new JavadroneNavDataParser();
		javadroneParser.setNavDataListener(navDataListener);
		
		while(!doStop){
			try {
				ticklePort(ARDroneUtils.NAV_PORT);
				DatagramPacket packet=new DatagramPacket(new byte[1024], 1024, inetaddr, 5554);

				socket.receive(packet);
				
				ByteBuffer buffer=ByteBuffer.wrap(packet.getData(), 0, packet.getLength());

				
				parser.parseNavData(buffer.duplicate());
				javadroneParser.parseNavData(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NavDataException e) {
				e.printStackTrace();
			}
		}
	}
}