package tab1msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab1msg.Tab1Msg.OperateHandler;

import bean.Group;
import bean.Member;
import bean.Normal;

public class ControllerMsg implements OperateHandler {

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

	public interface UpdateHandler {
		void dispatchMsg(int code, Object obj);
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
	public void send() {

	}

}
