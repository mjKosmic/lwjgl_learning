import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

public class Renderer {

	ShaderProgram shaderProgram;

	int quadVbo;
	int quadVao;
	int vertexCount;

	public Renderer() {
		defineQuad();
	}



	private void defineQuad() {
		float[] vertices = {
				// Left bottom triangle
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				// Right top triangle
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};

		this.vertexCount = 6;

		try (MemoryStack stack = stackPush()) {
			FloatBuffer floatBuffer = stack.callocFloat(vertices.length);
			floatBuffer.put(vertices);
			floatBuffer.flip();

			quadVao = glGenVertexArrays();
			quadVbo = glGenBuffers();

			glBindVertexArray(quadVao);

			glBindBuffer(GL_ARRAY_BUFFER, quadVbo);
			glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);

			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			shaderProgram = new ShaderProgram(quadVao);

			glBindVertexArray(0);
		}
	}

	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT);

		glBindVertexArray(quadVao);
		glEnableVertexAttribArray(0);

		glUseProgram(shaderProgram.program);
//		glDrawElements(GL_TRIANGLES, this.vertexCount, GL_UNSIGNED_BYTE, 0);
		glDrawArrays(GL_TRIANGLES, 0, this.vertexCount);

		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}
