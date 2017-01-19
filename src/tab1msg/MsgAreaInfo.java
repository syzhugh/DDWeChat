package tab1msg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tab1msg.ControllerMsg.UpdateHandler;
import tab1msg.Tab1Msg.OperateHandler;

public class MsgAreaInfo extends JPanel implements UpdateHandler {
	private OperateHandler opHandler;

	private JLabel count_normal, count_group;

	public void setOpHandler(OperateHandler opHandler) {
		this.opHandler = opHandler;
	}

	public MsgAreaInfo() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		TitledBorder titledBorder1 = BorderFactory.createTitledBorder("信息");
		titledBorder1.setTitleJustification(TitledBorder.CENTER);
		setBorder(titledBorder1);

		addComponent();
		addEvent();
	}

	private void addEvent() {
	}

	private void addComponent() {
		count_normal = new JLabel();
		count_group = new JLabel();
		add(count_normal);
		add(count_group);
	}

	@Override
	public void dispatchMsg(int code, Object obj) {
		String[] counts = ((String) obj).split(",");
		count_normal.setText("好友：" + counts[0]);
		count_group.setText("群组：" + counts[1]);
	}
}
