precision mediump float;

uniform vec3 u_v3Color;
uniform vec3 u_v3LightDirection;
uniform vec3 u_v3LightColor;
varying vec2 v2Position;

/* We suppose eye position is on Z axis,
 * caller responsible to orient face properly.
 */

void main()
{
    if (length(v2Position) > 1.0)
        discard;

    float x = v2Position.x;
    float y = v2Position.y;
    vec3 v3n = vec3(x, y, sqrt(1.0 - x*x - y*y));
    vec3 v3tl = (u_v3LightDirection - v3n);
    vec3 v3h = (normalize(v3tl) + vec3(0.0, 0.0, 1.0));
    vec3 c = vec3(0.0, 0.0, 0.0);
    c += (u_v3Color * 0.4);
    c += (u_v3Color * 0.3 * dot(v3tl, v3n) / length(v3tl));
    c += (u_v3LightColor * 0.3 * pow(clamp(dot(v3n, v3h)/length(v3h), 0.0, 1.0), 40.0));

    gl_FragColor = vec4(c, 1.0);
}
