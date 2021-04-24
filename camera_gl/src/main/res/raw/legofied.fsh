precision highp float;

// Screen size in pixels
uniform vec3 iResolution;

uniform sampler2D iChannel0;

// 0.04 - small; 0.03 - medium; 0.02 - large (amout of blocks = blockSize*iResolution.x)
uniform float blockSize;

varying vec2 texCoord;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    //blocked pixel coordinate
    vec2 middle = floor(fragCoord*blockSize+.5)/blockSize;

    vec3 color = texture2D(iChannel0, middle/iResolution.xy).rgb;

    //lego block effects
        //stud
    float dis = distance(fragCoord,middle)*blockSize*2.;
    if(dis<.65&&dis>.55){
        color *= dot(vec2(0.707),normalize(fragCoord-middle))*.5+1.;
    }

    //side shadow
    vec2 delta = abs(fragCoord-middle)*blockSize*2.;
    float sdis = max(delta.x,delta.y);
    if(sdis>.9){
        color *= .8;
    }

	fragColor = vec4(color,1.0);
}

void main() {
	mainImage(gl_FragColor, texCoord*iResolution.xy);
}