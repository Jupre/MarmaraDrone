
package sample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.f1bilisim.ardrone.ARDrone;
import com.f1bilisim.ardrone.navdata.AttitudeListener;
import com.f1bilisim.ardrone.navdata.BatteryListener;
import com.f1bilisim.ardrone.navdata.DroneState;
import com.f1bilisim.ardrone.navdata.StateListener;
import com.f1bilisim.ardrone.navdata.VelocityListener;
import com.f1bilisim.ardrone.video.ImageListener;



public class ARDroneTest extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private ARDrone ardrone=null;
	private boolean shiftflag=false;
	
	private MyPanel myPanel;
	
	public ARDroneTest(){
		initialize();
	}
	
	private void initialize(){
		ardrone=new ARDrone("192.168.1.1");
		System.out.println("connect drone controller");
		ardrone.connect();
		System.out.println("connect drone navdata");
		ardrone.connectNav();
		System.out.println("connect drone video");
		ardrone.connectVideo();
		System.out.println("start drone");
		ardrone.start();
		
		
		ardrone.addImageUpdateListener(new ImageListener(){
			@Override
			public void imageUpdated(BufferedImage image) {
				if(myPanel!=null){
					myPanel.setImage(image);
					myPanel.repaint();
				}
			}
		});
		
		ardrone.addAttitudeUpdateListener(new AttitudeListener() {
			@Override
			public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
//				System.out.println("pitch: "+pitch+", roll: "+roll+", yaw: "+yaw+", altitude: "+altitude);
			}
		});
		
		ardrone.addBatteryUpdateListener(new BatteryListener() {
			@Override
			public void batteryLevelChanged(int percentage) {
				System.out.println("battery: "+percentage+" %");
			}
		});
				
		ardrone.addStateUpdateListener(new StateListener() {
			@Override
			public void stateChanged(DroneState state) {
//				System.out.println("state: "+state);
			}
		});

		ardrone.addVelocityUpdateListener(new VelocityListener() {
			@Override
			public void velocityChanged(float vx, float vy, float vz) {
//				System.out.println("vx: "+vx+", vy: "+vy+", vz: "+vz);
			}   
		});
		
		addKeyListener(new KeyAdapter(){
			int speed = 45;
			
			public void keyReleased(KeyEvent e){
				ardrone.stop();
			}
			public void keyPressed(KeyEvent e){
				int key=e.getKeyCode();
				int mod=e.getModifiersEx();
				
				System.out.println("Key pressed: " + key + " (Enter=" + KeyEvent.VK_ENTER + " Space=" + KeyEvent.VK_SPACE + " S=" + KeyEvent.VK_S + " E=" + KeyEvent.VK_E + ")");
				 
				shiftflag=false;
				if((mod & InputEvent.SHIFT_DOWN_MASK)!=0){
					shiftflag=true;
		       		}

				switch(key){
				case KeyEvent.VK_ENTER:
					ardrone.takeOff();
					break;
				case KeyEvent.VK_SPACE:
					ardrone.landing();
					break;
				case KeyEvent.VK_S:
					ardrone.stop();            
					break;
				case KeyEvent.VK_LEFT:
					if(shiftflag){
						 ardrone.spinLeft();
						shiftflag=false;
					}else
						ardrone.goLeft(speed);
					break;
				case KeyEvent.VK_RIGHT:
					if(shiftflag){
						ardrone.spinRight();
						shiftflag=false;
					}else
						ardrone.goRight(speed);
					break;
				case KeyEvent.VK_UP:
					if(shiftflag){
						ardrone.up(speed);
						shiftflag=false;
					}else
						ardrone.forward(speed);
					break;
				case KeyEvent.VK_DOWN:
					if(shiftflag){
						ardrone.down(speed);
						shiftflag=false;
					}else
						ardrone.backward(speed);
					break;
				case KeyEvent.VK_1:
					ardrone.setHorizontalCamera();
					//System.out.println("1");
					break;
				case KeyEvent.VK_2:
					ardrone.setHorizontalCameraWithVertical();
					//System.out.println("2");
					break;
				case KeyEvent.VK_3:
					ardrone.setVerticalCamera();
					//System.out.println("3");
					break;
				case KeyEvent.VK_4:
					ardrone.setVerticalCameraWithHorizontal();
					//System.out.println("4");
					break;
				case KeyEvent.VK_5:
					ardrone.toggleCamera();
					//System.out.println("5");
					break;
				case KeyEvent.VK_R:
					ardrone.spinRight();
					break;
				case KeyEvent.VK_L:
					ardrone.spinLeft();
					break;
				case KeyEvent.VK_U:
					ardrone.up();
					break;
				case KeyEvent.VK_D:
					ardrone.down();
					break;
				case KeyEvent.VK_E:
					ardrone.reset();
					break;
				}	
			}
		});
		
		this.setTitle("ardrone");
		this.setSize(400, 400);
		this.add(getMyPanel());
	}
	
	
	private JPanel getMyPanel(){
		if(myPanel==null){
			myPanel=new MyPanel();
		}
		return myPanel;
	}
	

	private class MyPanel extends JPanel{
		private static final long serialVersionUID = -7635284252404123776L;

		private BufferedImage image=null;
		
		public void setImage(BufferedImage image){
			this.image=image;
		}
		
		public void paint(Graphics g){
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			if(image!=null)
				g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		}
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				final ARDroneTest thisClass=new ARDroneTest();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.addWindowListener(new WindowAdapter(){
					@Override
					public void windowOpened(WindowEvent e) {
						System.out.println("WindowOpened");
					}
					@Override
					public void windowClosing(WindowEvent e) {
						thisClass.dispose();
					}
				});
				thisClass.setVisible(true);
			}
		});
	}
}