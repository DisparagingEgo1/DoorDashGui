package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class CustomActionListener implements ActionListener {

	private final JFrame parent;
	
	public CustomActionListener(final JFrame parent) {
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.parent.dispatchEvent(new WindowEvent(this.parent,WindowEvent.WINDOW_CLOSING));
		
	}

}
