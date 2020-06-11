precision highp float;

uniform vec3 iResolution;
uniform sampler2D iChannel0;

// 40.0 - 100.0
uniform float maxTileSize;

// 1.0 - 5.0
uniform float borderSize;

varying vec2 texCoord;

const float textureSamplesCount = 3.0;
const float textureEdgeOffset = 0.005;
const float size = 0.5;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
	float tileSize = floor(size * maxTileSize);
	tileSize += mod(tileSize, 2.0);
	vec2 tileNumber = floor(fragCoord / tileSize);

	vec4 accumulator = vec4(0.0);
	for (float y = 0.0; y < textureSamplesCount; ++y) {
		for (float x = 0.0; x < textureSamplesCount; ++x) {
			vec2 textureCoordinates = (tileNumber + vec2((x + 0.5)/textureSamplesCount, (y + 0.5)/textureSamplesCount)) * tileSize / iResolution.xy;
			textureCoordinates = clamp(textureCoordinates, 0.0 + textureEdgeOffset, 1.0 - textureEdgeOffset);
			accumulator += texture2D(iChannel0, textureCoordinates);
	   }
	}

	fragColor = accumulator / vec4(textureSamplesCount * textureSamplesCount);

	vec2 pixelNumber = floor(fragCoord - (tileNumber * tileSize));
	pixelNumber = mod(pixelNumber + borderSize, tileSize);

    float pixelBorder = step(min(pixelNumber.x, pixelNumber.y), borderSize) * step(borderSize * 2.0 + 1.0, tileSize);

	fragColor *= pow(fragColor, vec4(pixelBorder));
}

void main() {
    mainImage(gl_FragColor, texCoord.xy*iResolution.xy);
}