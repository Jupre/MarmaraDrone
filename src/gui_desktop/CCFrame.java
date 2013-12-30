package gui_desktop;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.f1bilisim.ardrone.ARDrone;
import com.f1bilisim.ardrone.navdata.AttitudeListener;
import com.f1bilisim.ardrone.navdata.BatteryListener;
import com.f1bilisim.ardrone.navdata.DroneState;
import com.f1bilisim.ardrone.navdata.StateListener;
import com.f1bilisim.ardrone.navdata.VelocityListener;
import com.f1bilisim.ardrone.navdata.javadrone.NavData;
import com.f1bilisim.ardrone.navdata.javadrone.NavDataListener;
import com.f1bilisim.ardrone.video.ImageListener;

public class CCFrame extends JFrame implements NavDataListener, ImageListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VideoPanel video;
	private BatteryPanel battery;
	private StatePanel state;
	private AttitudePanel attitude;
	private AttitudeChartPanel attitudeChart;
	private StatisticsPanel statistics;
	private SpeedPanel speed;
	private KeyboardLayoutPanel keyboard;
	private ConsolePanel console;
	
	private ARDrone drone;
	
	private boolean shiftflag=false;
	
	public CCFrame(ARDrone ardrone)
	{
		super("MarmaraDrone Java Application - Mehmet Sabancýoðlu & Fýrat Biþkin ");
		
		this.drone = ardrone;
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}

		int defaultSpeed = 20;
		final KeyboardCommandManager cmdManager = new KeyboardCommandManager(drone, defaultSpeed);
		
		// CommandManager handles (keyboard) input and dispatches events to the drone		
		System.out.println("CCFrame Klavye ayarlarý yüklendi ...");
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
        	
        	public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (e.getID() == KeyEvent.KEY_PRESSED) 
				{
	                cmdManager.keyPressed(e);
	            } 
				else if (e.getID() == KeyEvent.KEY_RELEASED) 
	            {
	                cmdManager.keyReleased(e);
	            }
	            return false;
			}
		});
		
		video = new VideoPanel(ardrone);
		battery = new BatteryPanel();
		state = new StatePanel();
		attitude = new AttitudePanel();
		attitudeChart = new AttitudeChartPanel();
		statistics = new StatisticsPanel();
		speed = new SpeedPanel(cmdManager, defaultSpeed);
		keyboard = new KeyboardLayoutPanel(cmdManager);
		console = new ConsolePanel();
		
		JDesktopPane desktop = new JDesktopPane() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private Image originalImage;
			private Image scaledImage;
			private int width = 0;
		    private int height = 0;
		    
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                if (originalImage == null)
                { 
	                ImageIcon icon = new ImageIcon(KeyboardLayoutPanel.class.getResource("img/desktop.jpg"));
	        		originalImage = icon.getImage();
	        		scaledImage = originalImage;
                }
        		
                if ((width != getWidth()) || (height != getHeight()))
                { 
                	width = getWidth();
                	height = getHeight();
                	scaledImage = originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_AREA_AVERAGING);
                }
                
                g.drawImage(scaledImage, 0, 0, this);
            }
        };
        
        
	    desktop.add(createVideoFrame());
	    desktop.add(createStateFrame());
	    desktop.add(createBatteryFrame());
	    desktop.add(createStatisticsFrame());
	    desktop.add(createAttitudeFrame());
	    desktop.add(createAttitudeChartFrame());
	    desktop.add(createSpeedFrame());
	    desktop.add(createKeyboardLayoutFrame());
	    JInternalFrame frame = createConsoleFrame();
	    desktop.add(frame);
	    
	    setContentPane(desktop);
	    
		setSize(1920, 1035);
		setVisible(true);
		
		
		setAndKeepFocus(console);
		ardrone.addAttitudeUpdateListener(new AttitudeListener() {
			@Override
			public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
//				System.out.println("pitch: "+pitch+", roll: "+roll+", yaw: "+yaw+", altitude: "+altitude);
			}
		});
		
		ardrone.addBatteryUpdateListener(new BatteryListener() {
			@Override
			public void batteryLevelChanged(int percentage) {
				System.out.println("Batarya: "+percentage+" %");
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
				drone.stop();
			}
			public void keyPressed(KeyEvent e){
				int key=e.getKeyCode();
				int mod=e.getModifiersEx();
				
				System.out.println("Basýlan Tuþ: " + key + " (Enter=" + KeyEvent.VK_ENTER + " Space=" + KeyEvent.VK_SPACE + " S=" + KeyEvent.VK_S + " E=" + KeyEvent.VK_E + ")");
				 
				shiftflag=false;
				if((mod & InputEvent.SHIFT_DOWN_MASK)!=0){
					shiftflag=true;
		       		}

				switch(key){
				case KeyEvent.VK_ENTER:
					drone.takeOff();
					break;
				case KeyEvent.VK_SPACE:
					drone.landing();
					break;
				case KeyEvent.VK_S:
					drone.stop();            
					break;
				case KeyEvent.VK_LEFT:
					if(shiftflag){
						 drone.spinLeft();
						shiftflag=false;
					}else
						drone.goLeft(speed);
					break;
				case KeyEvent.VK_RIGHT:
					if(shiftflag){
						drone.spinRight();
						shiftflag=false;
					}else
						drone.goRight(speed);
					break;
				case KeyEvent.VK_UP:
					if(shiftflag){
						drone.up(speed);
						shiftflag=false;
					}else
						drone.forward(speed);
					break;
				case KeyEvent.VK_DOWN:
					if(shiftflag){
						drone.down(speed);
						shiftflag=false;
					}else
						drone.backward(speed);
					break;
				case KeyEvent.VK_1:
					drone.setHorizontalCamera();
					//System.out.println("1");
					break;
				case KeyEvent.VK_2:
					drone.setHorizontalCameraWithVertical();
					//System.out.println("2");
					break;
				case KeyEvent.VK_3:
					drone.setVerticalCamera();
					//System.out.println("3");
					break;
				case KeyEvent.VK_4:
					drone.setVerticalCameraWithHorizontal();
					//System.out.println("4");
					break;
				case KeyEvent.VK_5:
					drone.toggleCamera();
					//System.out.println("5");
					break;
				case KeyEvent.VK_R:
					drone.spinRight();
					break;
				case KeyEvent.VK_L:
					drone.spinLeft();
					break;
				case KeyEvent.VK_U:
					drone.up();
					break;
				case KeyEvent.VK_D:
					drone.down();
					break;
				case KeyEvent.VK_E:
					drone.reset();
					break;
				}	
			}
		});
		
		addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowDeiconified(WindowEvent e) { }
			public void windowActivated(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
			public void windowClosing(WindowEvent e) {
				drone.disconnect();
				attitude.stop();
				System.exit(0);
			}
			public void windowClosed(WindowEvent e) { }
		});
		
		drone.addImageUpdateListener(this);
		drone.addNavDataListener(this);
	}

	private void setAndKeepFocus(ConsolePanel console2)
	{
		new Thread(new Runnable() {
			
			public void run()
			{
				while (true)
				{
					console.focus();
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private JInternalFrame createVideoFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Video Kamera", true, false, false, true);
	    frame.setSize(650, 390);
	    frame.setLocation(330, 0);
	    frame.setContentPane(video);
	    frame.setVisible(true);
	    return frame;
	}

	private JInternalFrame createStateFrame()
	{
	    JInternalFrame frame = new JInternalFrame("NavData", true, false, false, true);
	    frame.setSize(260, 500);
	    frame.setLocation(970, 0);
	    frame.setContentPane(new JScrollPane(state));
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createBatteryFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Batarya", false, false, false, true);
	    frame.setSize(60, 120);
	    frame.setLocation(1250, 0);
	    frame.setContentPane(battery);
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createStatisticsFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Ýstatistik", true, false, false, true);
	    frame.setSize(200, 60);
	    frame.setLocation(330, 430);
	    frame.setContentPane(statistics);
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createAttitudeFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Davranýþ Modeli", true, false, false, true);
	    frame.setSize(330, 260);
	    frame.setLocation(0, 0);
	    JPanel panel = new JPanel();
	    panel.add(attitude.getPane());
	    frame.setContentPane(panel);
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createAttitudeChartFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Davranýþ Tablosu", true, false, false, true);
	    frame.setSize(330, 250);
	    frame.setLocation(0, 250);
	    frame.setContentPane(attitudeChart);
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createSpeedFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Hýz", true, false, false, true);
	    frame.setSize(60, 200);
	    frame.setLocation(1250, 120);
	    frame.setContentPane(speed);
	    frame.setVisible(true);
	    return frame;
	}
	
	private JInternalFrame createKeyboardLayoutFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Klavye Kýsayolu", true, false, false, true);
	    frame.setSize(1400, 285);
	    frame.setLocation(0, 715);
	    frame.setContentPane(keyboard);
	    frame.setVisible(false);
	    return frame;
	}
	
	private JInternalFrame createConsoleFrame()
	{
	    JInternalFrame frame = new JInternalFrame("Konsol", true, false, false, true);
	    frame.setSize(1400, 200);
	    frame.setLocation(0, 500);
	    frame.setContentPane(console);
	    frame.setVisible(true);
	    return frame;
	}
	
	public void imageUpdated(BufferedImage image)
	{
		if (statistics != null)
			statistics.imageArrived();
		if (video != null)
			video.setImage(image);
	}
	
	public void navDataUpdated(NavData navData)
	{
		if (statistics != null)
			statistics.navDataArrived();
		if (battery != null)
			battery.setBatteryLevel(navData.getBattery());
		if (attitude != null)
			attitude.setAttitude(navData.getPitch(), navData.getRoll(), navData.getYaw(), (int)navData.getAltitude());
		if (attitudeChart != null)
			attitudeChart.setAttitude(navData.getPitch(), navData.getRoll(), navData.getYaw());
		if (state != null)
			state.setState(navData.toDetailString());
	}
}
