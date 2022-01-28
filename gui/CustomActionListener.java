package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class CustomActionListener implements ActionListener {

	private final JFrame parent;
	private final ArrayList<String>fields;
	
	public CustomActionListener(final JFrame parent, ArrayList<String>fields) {
		this.parent = parent;
		this.fields = fields;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.checkFields())this.parent.dispatchEvent(new WindowEvent(this.parent,WindowEvent.WINDOW_CLOSING));
		
	}
	private boolean checkFields() {
		for(String s: this.fields) {
			if(s.equals("") || s.isEmpty())return false;
		}
		return true;
	}
}
