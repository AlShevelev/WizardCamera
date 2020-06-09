precision highp float;

uniform vec3 iResolution;
uniform sampler2D iChannel0;
varying vec2 texCoord;

// 0.0075 - small; 0.01 - medium; 0.015 - large
uniform float blockSize;

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    float TY = 0.866025;
    float TX = 1.5;

    float x = texCoord.x;
    float y = texCoord.y;

    int wx = int(x / TX / blockSize);
    int wy = int(y / TY / blockSize);
    vec2 v1, v2, vn;

    if (wx/2 * 2 == wx) {
        if (wy/2 * 2 == wy) {
            //(0,0),(1,1)
            v1 = vec2(blockSize * TX * float(wx), blockSize * TY * float(wy));
            v2 = vec2(blockSize * TX * float(wx + 1), blockSize * TY * float(wy + 1));
        } else {
            //(0,1),(1,0)
            v1 = vec2(blockSize * TX * float(wx), blockSize * TY * float(wy + 1));
            v2 = vec2(blockSize * TX * float(wx + 1), blockSize * TY * float(wy));
        }
    }else {
        if (wy/2 * 2 == wy) {
            //(0,1),(1,0)
            v1 = vec2(blockSize * TX * float(wx), blockSize * TY * float(wy + 1));
            v2 = vec2(blockSize * TX * float(wx + 1), blockSize * TY * float(wy));
        } else {
            //(0,0),(1,1)
            v1 = vec2(blockSize * TX * float(wx), blockSize * TY * float(wy));
            v2 = vec2(blockSize * TX * float(wx + 1), blockSize * TY * float(wy + 1));
        }
    }

    float s1 = sqrt(pow(v1.x - x, 2.0) + pow(v1.y - y, 2.0));
    float s2 = sqrt(pow(v2.x - x, 2.0) + pow(v2.y - y, 2.0));
    if (s1 < s2) {
        vn = v1;
    } else {
        vn = v2;
    }
    vec4 color = texture2D(iChannel0, vn);

    fragColor = color;
   }

void main () {
    mainImage(gl_FragColor, texCoord);
}