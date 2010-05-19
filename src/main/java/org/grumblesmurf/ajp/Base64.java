package org.grumblesmurf.ajp;

class Base64 {
    static byte[] encodeData;
    static String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    
    static {
    	encodeData = new byte[64];
	for (int i = 0; i<64; i++) {
	    byte c = (byte) charSet.charAt(i);
	    encodeData[i] = c;
	}
    }

    private Base64() {
    }

    public static String encode(String s) {
        return encode(s.getBytes());
    }

    public static String encode(byte[] src) {
	return encode(src, 0, src.length);
    }

    public static String encode(byte[] src, int start, int length) {
        byte[] dst = new byte[(length+2)/3 * 4 + length/72];
        int x = 0;
        int dstIndex = 0;
        int state = 0;	// which char in pattern
        int old = 0;	// previous byte
        int len = 0;	// length decoded so far
	int max = length + start;
        for (int srcIndex = start; srcIndex<max; srcIndex++) {
	    x = src[srcIndex];
	    switch (++state) {
	    case 1:
	        dst[dstIndex++] = encodeData[(x>>2) & 0x3f];
		break;
	    case 2:
	        dst[dstIndex++] = encodeData[((old<<4)&0x30) | ((x>>4)&0xf)];
		break;
	    case 3:
	        dst[dstIndex++] = encodeData[((old<<2)&0x3C) | ((x>>6)&0x3)];
		dst[dstIndex++] = encodeData[x&0x3F];
		state = 0;
		break;
	    }
	    old = x;
	    if (++len >= 72) {
	    	dst[dstIndex++] = (byte) '\n';
	    	len = 0;
	    }
	}

	/*
	 * now clean up the end bytes
	 */

	switch (state) {
	case 1: dst[dstIndex++] = encodeData[(old<<4) & 0x30];
	   dst[dstIndex++] = (byte) '=';
	   dst[dstIndex++] = (byte) '=';
	   break;
	case 2: dst[dstIndex++] = encodeData[(old<<2) & 0x3c];
	   dst[dstIndex++] = (byte) '=';
	   break;
	}
	return new String(dst);
    }
}
