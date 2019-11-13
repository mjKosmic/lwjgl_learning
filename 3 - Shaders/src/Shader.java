import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL20.*;


public class Shader {
	int shader = 0;

	public Shader(String filename, int shaderType) {
		try {
			shader = createShader(filename, shaderType);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (shader == 0) {
				return;
			}
		}
	}

	private int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;

		try {

			shader = glCreateShader(shaderType);

			if (shader == 0) {
				return 0;
			}

			glShaderSource(shader, readFileAsString(filename));
			glCompileShader(shader);

			if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
				throw new RuntimeException("Error creating shader: " + glGetShaderInfoLog(shader));
			}

			return shader;
		} catch (Exception e) {
			glDeleteShader(shader);
			throw e;
		}
	}

	private String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();

		Exception exception = null;

		BufferedReader reader;
		try{
			InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch(Exception exc) {
				exception = exc;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		}
		catch(Exception exc) {
			exception = exc;
		}
		finally {
			try {
//				in.close();

			}
			catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}
}
