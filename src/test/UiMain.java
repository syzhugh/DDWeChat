package test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.MCheckList;

public class UiMain {

	public static final int window_width = 960;
	public static final int window_height = 540;

	public static void main(String[] args) {

		JFrame jFrame = new JFrame("丁丁科技");
		jFrame.setSize(window_width, window_height);
		initTheme();

		// step1 ui
		initToolBar(jFrame);

		// initLayout(jFrame);

		// initList(jFrame);

		// initText(jFrame);

		// initButton(jFrame);

		// step2 event

		// step3 process/thread

		// step4 ipc
	}

	private static void initLayout(JFrame jFrame) {
		// TODO Auto-generated method stub

		JRootPane rootpanel = (JRootPane) jFrame.getContentPane();

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(200, 200);
		panel.add(new JButton("l"), BorderLayout.WEST);
		panel.add(new JButton("r"), BorderLayout.EAST);
		panel.add(new JButton("up"), BorderLayout.NORTH);
		panel.add(new JButton("down"), BorderLayout.SOUTH);
		panel.add(new JButton("center"), BorderLayout.CENTER);

		rootpanel.add(panel);

		jFrame.setVisible(true);

	}

	private static void initButton(JFrame jFrame) {
		// TODO Auto-generated method stub

	}

	private static void initText(JFrame jFrame) {
		// TODO Auto-generated method stub
		JRootPane pane = (JRootPane) jFrame.getContentPane();

	}

	private static void initList(JFrame jFrame) {
		// TODO Auto-generated method stub

	}

	private static void initToolBar(JFrame jFrame) {
		JRootPane rootpanel = new JRootPane();
		jFrame.setContentPane(rootpanel);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu mmain = new JMenu("Main");
		JMenu tool = new JMenu("常用工具");
		JMenu settings = new JMenu("设置");

		menuBar.add(mmain);
		menuBar.add(tool);
		menuBar.add(settings);

		rootpanel.setJMenuBar(menuBar);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.add("通讯录", new Panel());
		tabbedPane.add("群聊", new Panel());

//		DefaultListModel listModel = new DefaultListModel();
//
//		listModel.addElement(new MCheckList.CheckableItem("hehe",
//				"C:/Users/ZS27/Desktop/20k.jpg"));
//
//		listModel.addElement(new MCheckList.CheckableItem("xixi",
//				"C:/Users/ZS27/Desktop/20k.jpg"));
//
//		listModel.addElement(new MCheckList.CheckableItem("haha",
//				"C:/Users/ZS27/Desktop/20k.jpg"));
//
//		final MCheckList list = new MCheckList(listModel);
//		// list.setEnabled(true);
//		list.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				int index = list.locationToIndex(e.getPoint());
//				MCheckList.CheckableItem item = (ui.MCheckList.CheckableItem) list
//						.getModel().getElementAt(index);
//				item.setSelected(!item.isSelected());
//				Rectangle rect = list.getCellBounds(index, index);
//				list.repaint(rect);
//			}
//		});
//		list.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("focusLost");
//			}
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("focusGained");
//			}
//		});
//
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//				tabbedPane, list);
//
//		rootpanel.getContentPane().add(splitPane);
//
//		jFrame.setVisible(true);
//		splitPane.setDividerLocation(0.5);
//		splitPane.setEnabled(false);

	}

	private static void initTheme() {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
