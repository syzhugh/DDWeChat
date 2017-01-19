package thread;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main._Controller;
import mlistener.MActionListener;

import tab1msg.MsgAreaFriend;
import tab1msg.MsgAreaGroup;
import tab1msg.MsgAreaInfo;
import tab1msg.MsgAreaSend;
import tab1msg.Tab1Msg;
import utility.Constants;

public class UIThread extends BaseTHread implements _Controller.cCallback {

	private _Controller controller;

	public UIThread(Pipe pipe, String name) {
		super(pipe, name);
		controller = new _Controller(this);
	}

	// ui main
	private JFrame jFrame;
	private JRootPane rootpanel;

	private JTabbedPane pane;
	private JPanel tab1, tab2, tab3;

	private JMenu menutab1, menutab2, menutab3;
	private JMenuItem login, quit;

	// tools
	private JDialog toast_qrcode;
	private JOptionPane toast_errormsg;

	@Override
	public void run() {
		initTheme();
		initRoot();
		initToolBar();
		initTab();
		initTools();

		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
	}

	private void initTools() {
		toast_qrcode = new JDialog(jFrame, "二维码");
		toast_qrcode.setSize(500, 500);
		toast_qrcode.setLocationRelativeTo(null);

		toast_errormsg = new JOptionPane("提示", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void result(int i, boolean bool, String result) {
		switch (i) {
		case 0:// uuid
			if (!bool) {
				toast_errormsg.showMessageDialog(jFrame, result);
			}
			break;
		case 1:// img
			if (bool) {
				File pic = new File(Constants.qrcodeImg);
				if (pic.exists()) {
					toast_qrcode.getContentPane().add(new JLabel(new ImageIcon(pic.getAbsolutePath())));
					toast_qrcode.setVisible(true);
					controller.waitForLogin();
					// pic.delete();
				} else {
					toast_errormsg.showMessageDialog(jFrame, result);
				}
			} else {
				toast_errormsg.showMessageDialog(jFrame, result);
			}
			break;
		case 2:// login
			break;
		case 5:
			toast_qrcode.setVisible(false);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					initData();
				}
			});
			break;
		}
	}

	private void initData() {
		((Tab1Msg) tab1).refresh(controller.getUser());

	}

	private void initTheme() {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initRoot() {
		jFrame = new JFrame();
		jFrame.setSize(1120, 630);
		jFrame.setResizable(false);

		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		rootpanel = new JRootPane();
		jFrame.setContentPane(rootpanel);
	}

	private void initToolBar() {

		JMenuBar menubar = new JMenuBar();
		menubar.add(menutab1 = new JMenu("登陆退出"));
		menubar.add(menutab2 = new JMenu("tool2"));
		menubar.add(menutab3 = new JMenu("tool3"));
		rootpanel.setJMenuBar(menubar);

		menutab1.add(login = new JMenuItem("登陆"));
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker worker = new SwingWorker() {
					@Override
					protected Object doInBackground() throws Exception {
						controller.getuuid();
						return null;
					}
				};
				worker.execute();
			}
		});

		menutab1.add(quit = new JMenuItem("退出"));
		quit.addActionListener(new MActionListener() {

			@Override
			public void UI_do() {

			}
		});
	}

	private void initTab() {
		Container contentPane = rootpanel.getContentPane();

		contentPane.add(pane = new JTabbedPane());
		pane.add(tab1 = new Tab1Msg());
		pane.add(tab2 = new JPanel(), "好友管理");
		pane.add(tab3 = new JPanel(), "群组管理");

	}

	@Override
	public void send() {

	}

	@Override
	public void back() {

	}

	@Override
	public void fromSubject() {

	}

	@Override
	public void toObervers() {

	}

}
