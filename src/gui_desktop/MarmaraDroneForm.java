
package gui_desktop;

import com.f1bilisim.ardrone.ARDrone;



public class MarmaraDroneForm
{	
	private ARDrone ardrone=null;
	
	public MarmaraDroneForm(){
		initialize();
	}
	
	private void initialize(){
		try
		{
			ardrone=new ARDrone("192.168.1.1");
			System.out.println("Kontroller Baðlandý");
			ardrone.connect();
			System.out.println("NavData Baðlandý");
			ardrone.connectNav();
			System.out.println("Video Baðlandý");
			ardrone.connectVideo();
			System.out.println("Drone Baþlatýldý. ");
			ardrone.start();
			
			ardrone.setMaxAltitude(5000); // max 5 metre
			new CCFrame(ardrone);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			
			if (ardrone != null)
				ardrone.disconnect();
			System.exit(-1);
		}
	}
		
	public static void main(String args[]){
		new MarmaraDroneForm();
	}
}