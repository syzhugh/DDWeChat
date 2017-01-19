package main;

import thread.BackServiceThread;
import thread.Pipe;
import thread.UIThread;

public class _Main {

	public static void main(String[] args) {

		System.setProperty("jsse.enableSNIExtension", "false");

		Pipe pipe = new Pipe();
		UIThread ui = new UIThread(pipe, "ui_thread");
		BackServiceThread back = new BackServiceThread(pipe, "back_thread");

		ui.start();
		back.start();
	}

}
