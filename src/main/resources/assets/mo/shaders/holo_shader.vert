attribute vec4 gl_Color;
attribute vec4 gl_MultiTexCoord0;

varying vec4 gl_FrontColor;

void main()
{
    gl_Position = ftransform();
    gl_FrontColor = gl_Color;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}