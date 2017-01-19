package mlistener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class MMoustAdapter extends MouseAdapter {
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
		UI_do(e);
	}

	public abstract void UI_do(MouseEvent e);
}
