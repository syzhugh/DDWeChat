package thread;

public abstract class BaseTHread extends Thread implements Pipe.Messenger {

	private Pipe pipe;

	public BaseTHread(Pipe pipe, String name) {
		super(name);
		this.pipe = pipe;
		pipe.add(this);
	}

	public Pipe getPipe() {
		return pipe;
	}
}
