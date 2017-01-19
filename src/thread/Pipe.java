package thread;

import java.util.ArrayList;
import java.util.List;

public class Pipe {

	private List<Messenger> threads;

	public Pipe() {
		threads = new ArrayList<Pipe.Messenger>();
	}

	public void toAll() {
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).toObervers();
		}
	}

	public void add(Messenger messenger) {
		threads.add(messenger);
	}

	public interface Messenger {
		void send();

		void back();

		void fromSubject();

		void toObervers();
	}
}
