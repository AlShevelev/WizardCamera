precision highp float;

uniform vec3 iResolution;
uniform float iGlobalTime;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

// from 0.5(included) to 2.0(included)
uniform float mixFactor;

varying vec2 texCoord;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
	vec2 texPoint = vec2(fragCoord.xy / iResolution.xy);

	vec4 sourceColor = texture2D(iChannel0, texCoord);
	vec4 texColor = texture2D(iChannel1, texPoint);

	vec4 colorMix = (sourceColor * mixFactor + texColor / mixFactor) / 2.0;

	fragColor = colorMix;

}

void main() {
	mainImage(gl_FragColor, texCoord * iResolution.xy);
}