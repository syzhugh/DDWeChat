package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class IconList extends JList {
	public IconList(DefaultListModel model)// 使用列表模板创建列表
	{
		super(model);// 调用父类构造方方
		setCellRenderer(new IconCellRenderer());// 设置单元格设置
		setBackground(Color.white);// 设置列表背景色
		setForeground(Color.blue);// 设置列表前景色
	}

	class IconCellRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object obj,
				int index, boolean isSelected, boolean cellHasFocus) {
			Object[] cell = (Object[]) obj;
			setIcon((Icon) cell[0]);// 设置图片
			setText((cell[1].toString()));// 设置文本
			setToolTipText(cell[2].toString());// 设置提示文本
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框

			if (isSelected)// 如果选中
			{
				setBackground(Color.cyan);// 设置背景色
				setForeground(Color.blue);
			} else// 没有选中
			{
				setBackground(Color.white); // 设置背景色
				setForeground(Color.blue);
			}
			setEnabled(list.isEnabled());
			setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
			setOpaque(true);
			return this;
		}
	}

	public static void text() {

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		DefaultListModel listmodel = new DefaultListModel();
		JList list = new IconList(listmodel);

		try {
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception e) {
		}

		frame.setSize(120, 300);

		listmodel
				.addElement(new Object[] {
						new ImageIcon("C:/Users/ZS27/Desktop/20k.jpg"), "好友1",
						"提示信息" });// 添加选项
		listmodel
				.addElement(new Object[] {
						new ImageIcon("C:/Users/ZS27/Desktop/20k.jpg"), "好友2",
						"提示信息" });// 添加选项
		listmodel
				.addElement(new Object[] {
						new ImageIcon("C:/Users/ZS27/Desktop/20k.jpg"), "好友3",
						"提示信息" });// 添加选项
		listmodel
				.addElement(new Object[] {
						new ImageIcon("C:/Users/ZS27/Desktop/20k.jpg"), "好友4",
						"提示信息" });// 添加选项

		frame.add(list, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
