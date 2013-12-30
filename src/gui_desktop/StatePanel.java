package gui_desktop;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class StatePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	
	public StatePanel()
	{
		super();
		
		setBackground(Color.white);
		
		text = new JTextArea("Konum bilgisi bekleniyor ...");
		text.setEditable(false);
		text.setFont(new Font("Helvetica", Font.PLAIN, 10));
		
		add(text);
	}
	
	public void setState(String state)
	{
		text.setText(state);
	}
	
}
