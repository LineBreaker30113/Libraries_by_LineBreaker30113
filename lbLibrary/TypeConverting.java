package lbLibrary;

public class TypeConverting {
	public static byte[] short2bytes(short value) { return new byte[] { (byte) (value >> 8), (byte) (value) }; }
	public static short bytes2short(byte[] bytes) { return (short) ((bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF)); }
	public static byte[] int2bytes(int value) {
	    return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value) };
	}
	public static int bytes2int(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	public static byte[] long2bytes(long value) {
	    return new byte[] {
	    		(byte) (value >> 56), (byte) (value >> 48), (byte) (value >> 40), (byte) (value >> 32),
	    		(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value) };
	}
	public static long bytes2long(byte[] bytes) {
		long lb = ((long)bytes[0]) << 56 | (((long)bytes[1]) & 0xFF) << 48 | (((long)bytes[2]) & 0xFF) << 40;
		lb += (((long)bytes[3]) & 0xFF) << 32 | (((long)bytes[4]) & 0xFF) << 24 | (((long)bytes[5]) & 0xFF) << 16;
		lb += (((long)bytes[3]) & 0xFF) << 8 | (((long)bytes[4]) & 0xFF);
		return lb;
	}
	public static byte[] float2bytes(float value) {
		int d = Float.floatToIntBits(value);
	    return new byte[] { (byte) (d >> 24), (byte) (d >> 16), (byte) (d >> 8), (byte) (d) };
	}
	public static float bytes2float(byte[] bytes) {
		int intBits = bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
	public static byte[] double2bytes(double value) {
		long d = Double.doubleToLongBits(value);
	    return new byte[] {
	    		(byte) (d >> 56), (byte) (d >> 48), (byte) (d >> 40), (byte) (d >> 32),
	    		(byte) (d >> 24), (byte) (d >> 16), (byte) (d >> 8), (byte) (d) };
	}
	public static double bytes2Double(byte[] bytes) {
		long lb = ((long)bytes[0]) << 56 | (((long)bytes[1]) & 0xFF) << 48 | (((long)bytes[2]) & 0xFF) << 40;
		lb += (((long)bytes[3]) & 0xFF) << 32 | (((long)bytes[4]) & 0xFF) << 24 | (((long)bytes[5]) & 0xFF) << 16;
		lb += (((long)bytes[3]) & 0xFF) << 8 | (((long)bytes[4]) & 0xFF);
		return Double.longBitsToDouble(lb);
	}
	
	public static byte[] char2byteArray(char[] chars) {
		byte[] result = new byte[chars.length*2];
		for(int i=0;i!=chars.length;i++) { int c = chars[i]; boolean isneg = false;
			if(c<0) { isneg = true; c*=-1; }
			result[i*2] = (byte) ((c>>8)%(1<<8)); result[i*2+1] = (byte) ((c>>0)%(1<<8));
			if(isneg) { result[i*2] += 128; }
		} return result;
	}
	public static char[] byte2charArray(byte[] bytes) {
		char[] result = new char[bytes.length/2];
		for(int i=0;i!=result.length;i++) {
			int h = bytes[i*2], l = bytes[i*2+1]; boolean isneg = false;
			if(h>127) { isneg = true; h -= 128; }
			if(h<0) { h=(1<<8)+h; } if(l<0) { l=(1<<8)+l; }
			int res = ((h<<8) + (l<<0)); if(isneg) { res*=-1; } result[i] = (char) res;
		} return result;
	}
	/*public static byte[] short2byteArray(short[] shorts) {
		byte[] result = new byte[shorts.length*2];
		for(int i=0;i!=shorts.length;i++) { int c = shorts[i]; boolean isneg = false;
			if(c<0) { isneg = true; c*=-1; }
			result[i*2] = (byte) ((c>>8)%(1<<8)); result[i*2+1] = (byte) ((c>>0)%(1<<8));
			if(isneg) { result[i*2] += 128; }
		} return result;
	}
	public static short[] byte2shortArray(byte[] bytes) {
		short[] result = new short[bytes.length/2];
		for(int i=0;i!=result.length;i++) {
			int h = bytes[i*2], l = bytes[i*2+1];
			if(h<0) { h=(1<<8)+h; } if(l<0) { l=(1<<8)+l; }
			result[i] = (short) ((h<<8) + (l<<0));
		} return result;
	}
	public static byte[] int2byteArray(int[] ints) {
		byte[] result = new byte[ints.length*4];
		for(int i=0;i!=ints.length;i++) { int c = ints[i]; boolean isneg = false;
			if(c<0) { isneg = true; c*=-1; }
			result[i*4+0] = (byte) ((c>>24)%(1<<8)); result[i*4+1] = (byte) ((c>>16)%(1<<8));
			result[i*4+2] = (byte) ((c>> 8)%(1<<8)); result[i*4+3] = (byte) ((c>> 0)%(1<<8));
			if(isneg) { result[i*4] += 128; }
		} return result;
	}
	public static int[] byte2intArray(byte[] bytes) {
		int[] result = new int[bytes.length/4];
		for(int i=0;i!=result.length;i++) {
			int h = bytes[i*4], i1 = bytes[i*4+1], i2 = bytes[i*4], l = bytes[i*4+1];
			if(h<0) { h=(1<<8)+h; } if(l<0) { l=(1<<8)+l; }
			result[i] = (h<<24) + (i1<<16) + (i2<<8) + (l<<0);
		} return result;
	}*/
	
}
