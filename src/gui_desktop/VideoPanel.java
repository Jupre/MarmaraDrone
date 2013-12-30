package gui_desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.f1bilisim.ardrone.ARDrone;

public class VideoPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image = null;

	public VideoPanel(final ARDrone drone)
	{
		setBackground(Color.BLACK);
		
		Dimension dim = new Dimension(650, 360);
		
		setMinimumSize(dim);
		setMaximumSize(dim);
		setSize(dim);
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				// dikey yatay geçiþ kamera görüntüsü
				drone.toggleCamera();
			}
		});
	}
	
	public void setImage(final BufferedImage image)
	{
		this.image = image;
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				repaint();
			}
		});
	}

	public void paint(Graphics g)
	{
		if (image != null)
			g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		
		else
		{
			g.setColor(Color.WHITE);
			g.drawString("Video bilgileri bekleniyor ...", 10, 20);
		}
	}
}
