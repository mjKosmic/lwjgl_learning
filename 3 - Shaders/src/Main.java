public class Main {

	LWJGLManager lwjglManager;
	public static boolean running = true;

	public static void main(String[] args) {
		Main main = new Main();
		main.init();
		main.run();
	}

	Main() {
		lwjglManager = new LWJGLManager();
	}

	void init() {
		lwjglManager.initLwjgl();
	}

	void run() {
		while (running) {
			lwjglManager.poll();
		}
	}
}
