precision highp float;

uniform sampler2D iChannel0;

// 30x60; 40x80; 50x100
uniform vec2 tileNum;

varying vec2 texCoord;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
	vec2 uv = fragCoord.xy;
	vec2 uv2 = floor(uv*tileNum)/tileNum;
    uv -= uv2;
    uv *= tileNum;
	fragColor = texture2D(iChannel0, uv2 + vec2(step(1.0-uv.y,uv.x)/(2.0*tileNum.x), step(uv.x,uv.y)/(2.0*tileNum.y)));
}

void main() {
	mainImage(gl_FragColor, texCoord);
}