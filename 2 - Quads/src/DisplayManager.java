import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class DisplayManager {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;


	public long window;

	int vaoId;
	int vboId;
	int vertexCount;

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		window = glfwCreateWindow(WIDTH, HEIGHT, "Display", 0, 0);

		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}

		glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
		});

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
				window,
				(vidMode.width() - WIDTH) / 2,
				(vidMode.height() - HEIGHT) / 2
		);

		//Make the OpenGL context current
		glfwMakeContextCurrent(window);

		//enable vsync
		glfwSwapInterval(1);

		//Show the window
		glfwShowWindow(window);
	}

	public void prepareQuad() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		createCapabilities();


		glClearColor(0f,0f,0f, 0f);
		vaoId = GL30.glGenVertexArrays();

		float[] vertices = {
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f,
			-0.5f, 0.5f, 0f
		};
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		vertexCount = 6;
		glBindVertexArray(vaoId);
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);

		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		//Put the VBO in the attribute list at index 0
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0 , 0);



		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);

		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);

		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

	}

}
