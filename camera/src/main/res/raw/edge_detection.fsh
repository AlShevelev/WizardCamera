#extension GL_OES_standard_derivatives : enable
precision mediump float;

//uniform vec3 iResolution;
uniform sampler2D iChannel0;
uniform int inverted;
varying vec2 texCoord;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uv = fragCoord.xy;
    vec4 color =  texture2D(iChannel0, fragCoord);
    float gray = length(color.rgb);

    // Color inversion
    // fragColor = 1.0 - vec4(vec3(step(0.06, length(vec2(dFdx(gray), dFdy(gray))))), 1.0);

    fragColor = vec4(vec3(step(0.06, length(vec2(dFdx(gray), dFdy(gray))))), 1.0);

    if(inverted == 1) {
        fragColor = 1.0 - fragColor;
    }
}

void main() {
    mainImage(gl_FragColor, texCoord);
}