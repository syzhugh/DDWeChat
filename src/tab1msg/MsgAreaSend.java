package tab1msg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mlistener.MActionListener;

import tab1msg.ControllerMsg.UpdateHandler;
import tab1msg.Tab1Msg.OperateHandler;

public class MsgAreaSend extends JPanel implements UpdateHandler {

	private JTextArea text;
	private JButton bt_top_pic, bt_top_file;
	private JButton bt_txt_del, bt_txt_send;

	private JFileChooser fileChooser;
	private OperateHandler opHandler;

	public void setOpHandler(OperateHandler opHandler) {
		this.opHandler = opHandler;
	}

	public MsgAreaSend() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEtchedBorder());

		initFileChooser();
		addComponent();
		addEvent();

	}

	private void initFileChooser() {
		fileChooser = new JFileChooser();
	}

	private void addEvent() {
		bt_top_pic.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {

			}
		});

		bt_top_file.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {
				int showDialog = fileChooser.showDialog(new JLabel(), "选择");
				if (showDialog == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					text.setText(file.getAbsolutePath());
				}
			}
		});

		bt_txt_del.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {
				text.setText("");
			}
		});

		bt_txt_send.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {

			}
		});
	}

	private void addComponent() {
		// top
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bt_top_pic = new JButton("表情");
		bt_top_file = new JButton("文件");
		top.add(bt_top_pic);
		top.add(bt_top_file);

		// text
		JPanel center = new JPanel(null);
		text = new JTextArea();
		text.setLineWrap(true);
		text.setBounds(2, 2, 300, 250);

		center.add(text);
		// button
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bt_txt_del = new JButton("撤销");
		bt_txt_send = new JButton("发送");
		bottom.add(bt_txt_del);
		bottom.add(bt_txt_send);

		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);

	}

	@Override
	public void dispatchMsg(int code, Object obj) {

	}

}
