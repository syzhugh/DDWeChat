package ui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class MCheckList extends JList {

	public MCheckList(DefaultListModel model)// 使用列表模板创建列表
	{
		super(model);// 调用父类构造方方
		setCellRenderer(new CheckRender());// 设置单元格设置
		setBackground(Color.white);// 设置列表背景色
		setForeground(Color.blue);// 设置列表前景色
	}

	public static class CheckRender extends JPanel implements ListCellRenderer {
		private JLabel label;
		private JCheckBox checkBox;

		public CheckRender() {
			setLayout(new BorderLayout());
			add(label = new JLabel(), BorderLayout.CENTER);
			add(checkBox = new JCheckBox(), BorderLayout.EAST);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			CheckableItem item = (CheckableItem) value;

			label.setIcon(new ImageIcon(item.getHeadImgPath()));
			label.setText(item.getUser().getNickName());
			checkBox.setSelected(item.isSelected());

			setFocusable(cellHasFocus);

			setEnabled(true);
			setFont(new Font("sdf", Font.ROMAN_BASELINE, 18));
			setOpaque(true);

			return this;
		}
	}

	// public static class CheckableItem {
	// private String headImgPath;
	// private String nickName;
	// private boolean isSelected;
	//
	// public CheckableItem(String nickName, String headImgPath) {
	// super();
	// this.nickName = nickName;
	// this.headImgPath = headImgPath;
	// this.isSelected = false;
	// }
	//
	// public String getHeadImgPath() {
	// return headImgPath;
	// }
	//
	// public void setHeadImgPath(String headImgPath) {
	// this.headImgPath = headImgPath;
	// }
	//
	// public String getNickName() {
	// return nickName;
	// }
	//
	// public void setNickName(String nickName) {
	// this.nickName = nickName;
	// }
	//
	// public boolean isSelected() {
	// return isSelected;
	//
	// }
	//
	// public void setSelected(boolean isSelected) {
	// this.isSelected = isSelected;
	// }
	//
	// }
}
