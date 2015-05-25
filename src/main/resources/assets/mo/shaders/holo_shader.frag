uniform vec4 holo_color;
uniform sampler2D tex;

varying vec4 gl_Color;

void main()
{
    vec4 color = gl_Color * texture2D(tex,gl_TexCoord[0].st);
    gl_FragColor = (holo_color * color) * color.w;
}