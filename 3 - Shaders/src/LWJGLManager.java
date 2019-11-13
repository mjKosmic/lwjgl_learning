import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

public class LWJGLManager {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private GLFWKeyCallback keyCallback;
	private GLFWErrorCallback errorCallback;

	long windowHandle;

	Renderer renderer;

	public LWJGLManager() {
	}

	public void initLwjgl() {
		this.setErrorCallback(null);
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize LWJGL");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "Shaders", 0, 0);
		if (windowHandle == 0) {
			throw new RuntimeException("Error creating the window in lwjgl");
		}

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
				windowHandle,
				(vidMode.width() - WIDTH) / 2,
				(vidMode.height() - HEIGHT) / 2
		);

		this.setKeyCallback(null);

		glfwMakeContextCurrent(windowHandle);
		createCapabilities();
		glfwSwapInterval(1);
		glClearColor(0.4f, 0.6f, 0.9f, 1f);
		glViewport(0, 0, WIDTH, HEIGHT);

		renderer = new Renderer();

		glfwShowWindow(windowHandle);
	}

	private void setKeyCallback(GLFWKeyCallback keyCallback) {
		if (keyCallback != null) {
			this.keyCallback = keyCallback;
		} else {
			this.keyCallback = new GLFWKeyCallback() {
				@Override
				public void invoke(final long window, final int key, final int scancode, final int action, final int mods) {
					if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
						glfwSetWindowShouldClose(window, true);
					}
				}
			};
		}
		glfwSetKeyCallback(windowHandle, this.keyCallback);
	}

	private void setErrorCallback(GLFWErrorCallback errorCallback) {
		if (errorCallback != null) {
			this.errorCallback = errorCallback;
		} else {
			this.errorCallback = GLFWErrorCallback.createPrint(System.err);
		}

		glfwSetErrorCallback(this.errorCallback);
	}

	private void destroy() {
		glfwDestroyWindow(windowHandle);
	}

	public long getWindowHandle() {
		return windowHandle;
	}

	public void poll() {
		glfwPollEvents();
		if (glfwWindowShouldClose(windowHandle)) {
			Main.running = false; //<-- not good practice but this isn't the exercise for that
			destroy();
		}
	}
}
