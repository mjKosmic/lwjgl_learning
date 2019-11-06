import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Main {

	static DisplayManager displayManager;

	public static void main(String[] args) {
		displayManager = new DisplayManager();
		displayManager.init();
		displayManager.prepareQuad();
		loop();
	}

	public static void loop() {
		while(!glfwWindowShouldClose(glfwGetCurrentContext())) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			displayManager.draw();
			glfwSwapBuffers(displayManager.window);

			glfwPollEvents();
		}
	}

}
