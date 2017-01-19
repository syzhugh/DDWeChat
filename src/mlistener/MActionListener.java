package mlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

public abstract class MActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				UI_do();
			}
		});
	}

	public abstract void UI_do();
}
