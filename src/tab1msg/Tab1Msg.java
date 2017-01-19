package tab1msg;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import tab1msg.ControllerMsg.UpdateHandler;

import bean.Member;
import bean.User;

public class Tab1Msg extends JPanel implements UpdateHandler {
	private ControllerMsg controllerMsg;

	private MsgAreaFriend area1;
	private MsgAreaGroup area2;
	private MsgAreaInfo area3;
	private MsgAreaSend area4;

	public Tab1Msg() {
		setLayout(new GridBagLayout());
		setName("消息群发");
		controllerMsg = new ControllerMsg();

		initArea();
		initNotify();
	}

	private void initNotify() {
		controllerMsg.addObservers(ControllerMsg.UI, this);
		controllerMsg.addObservers(ControllerMsg.NORMAL, area1);
		controllerMsg.addObservers(ControllerMsg.GROUP, area2);
		controllerMsg.addObservers(ControllerMsg.INFO, area3);
		controllerMsg.addObservers(ControllerMsg.SEND, area4);

		area1.setOpHandler(controllerMsg);
		area2.setOpHandler(controllerMsg);
		area3.setOpHandler(controllerMsg);
		area4.setOpHandler(controllerMsg);
	}

	private void initArea() {
		// gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill,
		// insets, ipadx, ipady
		area1 = new MsgAreaFriend();
		GridBagConstraints carea1 = new GridBagConstraints(0, 0, 3, 4, 30, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);

		area2 = new MsgAreaGroup();
		GridBagConstraints carea2 = new GridBagConstraints(3, 0, 3, 4, 30, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);

		area3 = new MsgAreaInfo();
		GridBagConstraints carea3 = new GridBagConstraints(6, 0, 2, 1, 20, 30, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0);

		area4 = new MsgAreaSend();
		GridBagConstraints carea4 = new GridBagConstraints(6, 1, 2, 3, 0, 70, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
				0);

		add(area1, carea1);
		add(area2, carea2);
		add(area3, carea3);
		add(area4, carea4);

	}

	public void refresh(User user) {
		area1.setListModel(user.getNormalList());
		area2.setListModel(user.getGrouplList());
	}

	@Override
	public void dispatchMsg(int code, Object obj) {

	}

	public interface OperateHandler {
		void add(boolean add, int type, Member member);

		void send();
	}
}
