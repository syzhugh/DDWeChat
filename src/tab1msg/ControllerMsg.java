package tab1msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import tab1msg.Tab1Msg.OperateHandler;
import utility.HttpUtils;
import utility.HttpUtils.DefaultCallback;

import bean.Group;
import bean.Member;
import bean.Normal;

public class ControllerMsg implements OperateHandler, DefaultCallback {

	public static final int CODE_ADD = 0x10;
	public static final int CODE_SEND = 0x11;
	public static final int CODE_SUCCESS = 0x12;

	public static final String UI = "ui";
	public static final String NORMAL = "normal";
	public static final String GROUP = "group";
	public static final String INFO = "info";
	public static final String SEND = "send";

	private List<Normal> list_normal;
	private List<Group> list_group;

	private Map<String, UpdateHandler> observers;

	public ControllerMsg() {
		list_normal = new ArrayList<Normal>();
		list_group = new ArrayList<Group>();

		observers = new HashMap<String, ControllerMsg.UpdateHandler>();
	}

	public void addObservers(String tag, UpdateHandler arg0) {
		observers.put(tag, arg0);
	}

	@Override
	public void add(boolean add, int type, Member member) {
		if (member == null) {
			if (type == 0) {
				list_normal.clear();
			} else if (type == 1) {
				list_group.clear();
			}
		} else {
			if (type == 0) {
				if (add && !list_normal.contains(member)) {
					list_normal.add((Normal) member);
				} else if (!add && list_normal.contains(member)) {
					list_normal.remove((Normal) member);
				}
			} else if (type == 1) {
				if (add && !list_group.contains(member)) {
					list_group.add((Group) member);
				} else if (!add && list_group.contains(member)) {
					list_group.remove((Group) member);
				}
			}
		}
		observers.get(INFO).dispatchMsg(CODE_SEND, list_normal.size() + "," + list_group.size());
	}

	@Override
	public void send(final String text, final String filePath) {
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				List<Member> list = new ArrayList<Member>();
				list.addAll(list_group);
				list.addAll(list_normal);
				if (text != null) {
					HttpUtils.sendMsg(text, list, ControllerMsg.this);
				} else if (filePath != null) {
					HttpUtils.sendMedia(filePath, list, ControllerMsg.this);
				}
				return null;
			}

		};
		worker.execute();
	}

	@Override
	public void result(int i, boolean bool, String result) {

	}

	@Override
	public int size() {
		return list_group.size() + list_normal.size();
	}

	public interface UpdateHandler {
		void dispatchMsg(int code, Object obj);
	}

}
