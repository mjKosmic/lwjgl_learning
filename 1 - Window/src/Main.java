
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;

    private long window;

    private float sp = 0.0f;
    private boolean swapColor = false;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();

    }

    public void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        int WIDTH = 800;
        int HEIGHT = 600;

        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL", 0, 0);

        if (window < 1) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

    }

    private void update() {
        sp += 0.001f;

        if (sp > 1.0f) {
            sp = 0.0f;
            swapColor = !swapColor;
        }
    }

    private void render() {
        drawQuad();
    }

    private void drawQuad() {
        if (!swapColor) {
            glColor3f(0.0f, 1.0f, 0.0f);
        } else {
            glColor3f(0.0f, 0.0f, 1.0f);
        }

        glBegin(GL_QUADS);
        {
            glVertex3f( -sp, -sp, 0.0f);
            glVertex3f( sp, -sp, 0.0f);
            glVertex3f( sp, sp, 0.0f);
            glVertex3f( -sp, sp, 0.0f);
        }
        glEnd();
    }

    private void loop() {
        GL.createCapabilities();
        System.out.println("----------------------------");
        System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
        System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
        System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));
        System.out.println("OpenGL Renderer : " + glGetString(GL_RENDERER));
        System.out.println("OpenGL Extensions supported by your card : ");
        String extensions = glGetString(GL_EXTENSIONS);
        String[] extArr = extensions.split("\\ ");
        for (int i = 0; i < extArr.length; i++) {
            System.out.println(extArr[i]);
        }
        System.out.println("----------------------------");

        while (!glfwWindowShouldClose(window)) {
            if (!swapColor) {
                glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
            } else {
                glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            update();
            render();
            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }
    public void run() {
        System.out.println("Hello LWJGL3 " + Version.getVersion() + "!");

        try {
            init();
            loop();
            glfwDestroyWindow(window);
//            keyCallback.release();
        } finally {
            glfwTerminate();
//            errorCallback.release();
        }
    }

}
