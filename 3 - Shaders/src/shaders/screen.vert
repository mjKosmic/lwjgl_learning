#version 410
in vec3 position;
out vec3 vertColor;
void main() {
    gl_Position = vec4(position, 1);
    vertColor = vec3(1,1,1);
}