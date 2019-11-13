import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	static final String VERTEX_SHADER_FILEPATH = "shaders/screen.vert";
	static final String FRAGMENT_SHADER_FILEPATH = "shaders/screen.frag";

	List<Shader> shaders;
	int program;
	int vaoId;

	public ShaderProgram(int vaoId) {
		shaders = List.of(
				new Shader(VERTEX_SHADER_FILEPATH, GL_VERTEX_SHADER),
				new Shader(FRAGMENT_SHADER_FILEPATH, GL_FRAGMENT_SHADER)
		);

		program = glCreateProgram();

		if (program == 0) {
			return;
		}

		for(Shader shader : shaders) {
			glAttachShader(program, shader.shader);
		}

		glBindAttribLocation(program, 0, "position");

		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Error creating program: " + glGetProgramInfoLog(program));
			return;
		}

		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Error validating program: " + glGetProgramInfoLog(program));
			return;
		}

		this.vaoId = vaoId;
	}
}
