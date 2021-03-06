uniform mat4 u_m4Matrix;
attribute vec4 a_v4Position;
varying vec4 v_v4Position;

void main()
{
    v_v4Position = a_v4Position;
    gl_Position = u_m4Matrix * a_v4Position;
}
