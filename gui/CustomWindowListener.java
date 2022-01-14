package gui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


public class CustomWindowListener implements WindowListener {
	
	private final JFrame parent;
	private final JFrame child;
	
	public CustomWindowListener(final JFrame parent, final JFrame child ) {
		this.parent = parent;
		this.child = child;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		

	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.parent.setVisible(true);

	}

	@Override
	public void windowClosed(WindowEvent e) {
		

	}

	@Override
	public void windowIconified(WindowEvent e) {
		

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		

	}

	@Override
	public void windowActivated(WindowEvent e) {
		

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		

	}

}
