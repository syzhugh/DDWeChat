package tab1msg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import mlistener.MActionListener;
import mlistener.MMoustAdapter;

import tab1msg.ControllerMsg.UpdateHandler;
import tab1msg.Tab1Msg.OperateHandler;
import ui.CheckableItem;
import ui.MCheckList;
import bean.Group;
import bean.Member;
import bean.Normal;

public class MsgAreaFriend extends JPanel implements UpdateHandler {

	private DefaultListModel<CheckableItem> listModel;

	private MCheckList mlist;
	private JButton bt_cancel, bt_selectAll;

	private OperateHandler opHandler;

	public void setOpHandler(OperateHandler opHandler) {
		this.opHandler = opHandler;
	}

	public MsgAreaFriend() {
		setLayout(new BorderLayout());

		TitledBorder titledBorder1 = BorderFactory.createTitledBorder("好友");
		titledBorder1.setTitleJustification(TitledBorder.CENTER);
		setBorder(titledBorder1);

		listModel = new DefaultListModel();

		for (int i = 0; i < 3; i++) {
			listModel.addElement(new CheckableItem(new Normal("1", "hehe" + i)));
		}

		addComponent();
		addEvent();
	}

	private void addEvent() {
		System.out.println("bt_cancel==null" + (bt_cancel == null));
		System.out.println("bt_selectAll==null" + (bt_selectAll == null));

		bt_cancel.addActionListener(new MActionListener() {
			@Override
			public void UI_do() {
				for (int i = 0; i < listModel.size(); i++) {
					CheckableItem item = listModel.getElementAt(i);
					item.setSelected(false);
				}
				opHandler.add(false, 0, null);
				mlist.repaint();
			}
		});

		bt_selectAll.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {
				for (int i = 0; i < listModel.size(); i++) {
					CheckableItem item = listModel.getElementAt(i);
					item.setSelected(true);
					opHandler.add(item.isSelected(), 0, item.getUser());
				}
				mlist.repaint();
			}
		});

		mlist.addMouseListener(new MMoustAdapter() {

			@Override
			public void UI_do(MouseEvent e) {
				int index = mlist.locationToIndex(e.getPoint());
				CheckableItem item = (CheckableItem) mlist.getModel().getElementAt(index);
				item.setSelected(!item.isSelected());
				Rectangle rect = mlist.getCellBounds(index, index);
				mlist.repaint(rect);
				opHandler.add(item.isSelected(), 0, item.getUser());
			}

		});
	}

	private void addComponent() {
		bt_cancel = new JButton("撤销");
		bt_selectAll = new JButton("全选");
		mlist = new MCheckList(listModel);

		JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		top.add(bt_cancel);
		top.add(bt_selectAll);

		JScrollPane scrollPane = new JScrollPane(mlist);
		scrollPane.setBounds(5, 5, 100, 100);
		add(top, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void setListModel(List<Normal> list) {
		listModel.clear();
		for (int i = 0; i < list.size(); i++) {
			listModel.addElement(new CheckableItem(list.get(i)));
		}
		mlist.setModel(listModel);
	}

	@Override
	public void dispatchMsg(int code, Object obj) {
		// TODO Auto-generated method stub

	}

}
