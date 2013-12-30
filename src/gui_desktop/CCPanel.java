package gui_desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.f1bilisim.ardrone.ARDrone;
import com.f1bilisim.ardrone.navdata.javadrone.NavData;
import com.f1bilisim.ardrone.navdata.javadrone.NavDataListener;
import com.f1bilisim.ardrone.video.ImageListener;

@SuppressWarnings("unused")
public class CCPanel extends JPanel implements ImageListener, NavDataListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VideoPanel video;
	private BatteryPanel battery;
	private StatePanel state;
	private AttitudePanel attitude;
	
	public CCPanel(final ARDrone ardrone)
	{
		super(new GridBagLayout());
		
		setBackground(Color.BLUE);
		video = new VideoPanel(ardrone);
		battery = new BatteryPanel();
		state = new StatePanel();
		attitude = new AttitudePanel();
		
		add(video, new GridBagConstraints(0, 0, 1, 1, 0.7, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		add(new JScrollPane(state), new GridBagConstraints(1, 0, 1, 2, 0.3, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));
		
		add(attitude.getPane(), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
//		add(battery, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		
		
		
		// Drone i�in CommandManager'da kollar� (klavye) giri� ve olaylar g�nderir		
		System.out.println("CCFrame Klavye ayarlar� y�klendi ...");
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
        	
        	private KeyboardCommandManager cmdManager = new KeyboardCommandManager(ardrone, 45);
        	
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
	            return true;
			}
		});
		
		ardrone.addImageUpdateListener(this);
		ardrone.addNavDataListener(this);
	}

	public void imageUpdated(BufferedImage image)
	{
		video.setImage(image);
	}

	public void navDataUpdated(NavData navData)
	{
		battery.setBatteryLevel(navData.getBattery());
		attitude.setAttitude(navData.getPitch(), navData.getRoll(), navData.getYaw(), (int)navData.getAltitude());
		state.setState(navData.toDetailString());
	}
}
