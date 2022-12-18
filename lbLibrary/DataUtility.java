package lbLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import lbLibrary.DataUtility.Version1.lbSocket.lbSocketData;




public class DataUtility {
	
	public static class version0 {
		/*****************************************************
		 * Declares getOuth() which returns a DataOutputStream and uses it in send functions.
		 */
		public static interface lbSender {
			public DataOutputStream getOuth();
			public default lbSender sendData(lbDataPackage data) throws IOException { return sends(data.getlbData().data); }
			public default lbSender send(lbDataShip ship) throws IOException { ship.send(this); getOuth().flush(); return this; }
			public default lbSender send(byte data) throws IOException { getOuth().write(data); getOuth().flush(); return this; }
			public default lbSender sends(byte[] data) throws IOException {
				getOuth().writeInt(data.length); getOuth().write(data); getOuth().flush(); return this;
			}
			public default lbSender sendChars(char[] string) throws IOException {
				sends(TypeConverting.char2byteArray(string)); getOuth().flush(); return this;
			}
			public default lbSender sendShort(short data) throws IOException { getOuth().writeShort(data); getOuth().flush(); return this; }
			public default lbSender sendInt(int data) throws IOException { getOuth().writeInt(data); getOuth().flush(); return this; }
			public default lbSender sendLong(long data) throws IOException { getOuth().writeLong(data); getOuth().flush(); return this; }
			public default lbSender sendFloat(float data) throws IOException { getOuth().writeFloat(data); getOuth().flush(); return this; }
			public default lbSender sendDouble(double data) throws IOException { getOuth().writeDouble(data); getOuth().flush(); return this; }
		}
		/*****************************************************
		 * Declares getInph() which returns a DataInputStream and uses it in load functions.
		 * Additionally has build functions for building specified data objects using lbData(ShipBuilder,PackageLoader).
		 */
		public static interface lbLoader {
			public DataInputStream getInph();
			public default lbDataObject loadData() throws IOException { return new lbDataObject(this); }
			public default lbLoader load(lbDataShip ship) throws IOException { ship.load(this); return this; }
			public default lbDataShip build(lbDataShipBuilder builder) throws IOException { return builder.buildFrom(this); }
			public default lbDataPackage build(lbDataPackageLoader loader) throws IOException { return loader.loadFrom(this); }
			public default byte load() throws IOException { return (byte) getInph().read(); }
			public default byte[] loads() throws IOException {
				byte[] bytes = new byte[getInph().readInt()]; getInph().read(bytes); return bytes;
			}
			public default char[] loadChars() throws IOException {
				byte[] bytes = new byte[getInph().readInt()]; getInph().read(bytes); return TypeConverting.byte2charArray(bytes);
			}
			public default short loadShort() throws IOException { return getInph().readShort(); }
			public default int loadInt() throws IOException { return getInph().readInt();  }
			public default long loadLong() throws IOException { return getInph().readLong(); }
			public default float loadFloat() throws IOException { return getInph().readFloat(); }
			public default double loadDouble() throws IOException { return getInph().readDouble(); }
		}
		/*****************************************************
		 * Handles both the "lbLoader" and the "lbSender" interfaces.
		 * Also has a close function which closes both streams.
		 */
		public static abstract class lbDataHarbor implements lbLoader, lbSender {
			public DataOutputStream outh; public DataInputStream inph;
			@Override public DataOutputStream getOuth() { return outh; }
			@Override public DataInputStream getInph() { return inph; }
			public void lbClose() throws IOException {
				if(outh!=null) { outh.close(); outh = null; } if(inph!=null) { inph.close(); inph = null; }
			}
		}
		/*****************************************************
		 * add this to a class which to enable it's instances being used to build lbDataPackages as you want.
		 */
		public static interface lbDataPackageLoader {
			public default lbDataPackage loadFrom(lbLoader reader) throws IOException
			{ return loadFrom(reader.loadData()); }
			public lbDataPackage loadFrom(lbDataObject data);
		}
		/*****************************************************
		 * add this to a class which to enable it's instances being used to build lbDataPackages as you want.
		 */
		public static interface lbDataShipBuilder {
			public default lbDataShip buildFrom(lbLoader reader) throws IOException
			{ return buildFrom(reader.loadData()); }
			public lbDataShip buildFrom(lbDataObject data);
		}
		/*****************************************************
		 * declares getlbData() and setlbData() functions to use data package to use lbDataPackages
		 */
		public static interface lbDataPackage {
			public lbDataObject getlbData(); public lbDataPackage setlbData(lbDataObject data);
		}
		/*****************************************************
		 * declares send() and load functions to enable intances being send by lbSender and lbLoader
		 */
		public static interface lbDataShip {
			public lbDataShip send(lbSender harbor) throws IOException;
			public lbDataShip load(lbLoader harbor) throws IOException;
		}
		/*****************************************************
		 * A byte buffer with lot's of functions to contol it.
		 * Add functions don't resize the buffer but writes the data.
		 * Put functions do resize the buffer and writes the data.
		 * take functions don't resize the buffer and reades the data.
		 * The others functions documented on themself
		 */
		public static class lbDataObject implements lbDataShip, lbDataPackage {
			/** The Buffer */public byte[] data; public int used, taken=0;
			public void deleteData() { data = new byte[0]; used = 0; taken = 0; }
			public lbDataObject(lbLoader harbor) throws IOException { this.data = harbor.loads(); used = this.data.length; }
			/** Generates a new one */ public lbDataObject(byte[] data) {
				this.data = new byte[data.length]; used = data.length;
				for(int i=0;i!=data.length;i++) { this.data[i] = data[i]; }
			}
			public lbDataObject() { data = new byte[0]; used = 0; }
			
			/** Resizes the buffer */
			public void increasCapacity(int difference) {
				byte[] temp = new byte[data.length+difference];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			} /** learns the space in buffer and sets it to a value */
			public void setSpace(int length) {
				int request = length - (data.length - used);
				byte[] temp = new byte[data.length+request];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			} /** learns the current space and resizes the buffer if necessary */
			public void pushSpace(int minimum) {
				int request = minimum - (data.length - used); if(request<=0) { return; }
				byte[] temp = new byte[data.length+request];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			}

			public void adds(byte[] value, int requested) { pushSpace(requested+4); puts(value); }
			public void adds(byte[] value) { pushSpace(value.length+4); puts(value); }
			public void add(byte value) { pushSpace(1); put(value); }
			public void add(lbDataObject other) { adds(other.data, other.used); }
			public void addShort(short value) { pushSpace(2); byte[] r = TypeConverting.short2bytes(value); put(r[0]); put(r[1]); }
			public void addInt(int value) { pushSpace(4); byte[] r = TypeConverting.int2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void addLong(long value) { pushSpace(8); byte[] r = TypeConverting.long2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			public void addFloat(float value) { pushSpace(4); byte[] r = TypeConverting.float2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void addDouble(double value) { pushSpace(8); byte[] r = TypeConverting.double2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			
			public void puts(byte[] value, int requested)
			{ putInt(value.length); for(int i=0;i!=requested;i++) { data[i+used] = value[i]; } used += requested; }
			public void puts(byte[] value)
			{ putInt(value.length); for(int i=0;i!=value.length;i++) { data[i+used] = value[i]; } used += value.length; }
			public void put(byte value) { data[used] = value; used++; }
			public void putShort(short value) { byte[] r = TypeConverting.short2bytes(value); put(r[0]); put(r[1]); }
			public void putInt(int value) { byte[] r = TypeConverting.int2bytes(value); put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void putLong(long value) { byte[] r = TypeConverting.long2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			public void putFloat(float value) { byte[] r = TypeConverting.float2bytes(value); put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void putDouble(double value) { byte[] r = TypeConverting.double2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			
			public byte[] takes() { byte[] result = new byte[takeInt()];
			for(int i=0;i!=result.length;i++) { result[i] = data[i+used]; }
			used += result.length; return result; }
			public byte take() { taken++; return data[taken-1]; }
			public short takeShort() { return TypeConverting.bytes2short(new byte[] { take(), take()}); }
			public int takeInt() { return TypeConverting.bytes2int(new byte[] { take(), take(), take(), take()}); }
			public long takeLong()
			{ return TypeConverting.bytes2long(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
			public float takeFloat() { return TypeConverting.bytes2float(new byte[] { take(), take(), take(), take()}); }
			public double takeDouble()
			{ return TypeConverting.bytes2Double(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
			
			/** Adds the necessary part of the lbDataObject */
			public void append(lbDataPackage other) { add(other.getlbData()); }

			/** Shrinks the buffer size */
			public void remove(int length) {
				int nlength = data.length-length; byte[] temp = new byte[nlength];
				for(int i=0;i!=nlength;i++) { temp[i] = data[i]; } data = temp;
			}/** Shrinks the buffer size from the requested place */
			public void removes(int index, int length) {
				int nlength = data.length-length; byte[] temp = new byte[nlength];
				for(int i=0;i!=index;i++) { temp[i] = data[i]; }
				for(int i=index;i!=data.length;i++) { temp[i] = data[i+length]; }
				 data = temp;
			}
			@Override public lbDataObject getlbData() { return this; }
			@Override public lbDataObject setlbData(lbDataObject data) {
				this.data = new byte[data.data.length]; used = data.used; taken = data.taken;
				for(int i=0;i!=used;i++) { this.data[i] = data.data[i]; }
				return this;
			}
			@Override public lbDataObject send(lbSender harbor) throws IOException { harbor.sends(data); return null; }
			@Override public lbDataObject load(lbLoader harbor) throws IOException { data = harbor.loads(); return null; }
		}
		/** Handles the lbDataHarbor attributes of Socket */
		public static class lbSocket extends lbDataHarbor {
			public Socket self; public lbSocket() { self = null; inph = null; outh = null; }
			public lbSocket(String ip, int port) throws IOException { generate(new Socket(ip, port)); }
			public lbSocket(Socket socket) throws IOException { generate(socket); }
			public void generate(Socket socket) throws IOException {
				self = socket;
				outh = new DataOutputStream(self.getOutputStream());
				inph = new DataInputStream(self.getInputStream());
			}
			public void generate() throws IOException { generate(self); }
			public void close() throws IOException {
				if(self != null) { lbClose(); self.close(); self = null; return; }
				inph = null; outh = null;
			}
		}
		/** Handles the lbDataHarbor attributes of File */
		public static class lbFile extends lbDataHarbor {
			public File self; public lbFile() { self = null; outh = null; inph = null; }
			public lbFile(String path) throws IOException { generate(new File(path)); }
			public lbFile(File file) throws IOException { generate(file); }
			public lbFile(String path, boolean append) throws IOException { generate(new File(path), append); }
			public lbFile(File file, boolean append) throws IOException { generate(file, append); }
			public void generate(File file) throws IOException {
				self = file; self.createNewFile();
				inph = new DataInputStream(new FileInputStream(self));
				outh = new DataOutputStream(new FileOutputStream(self, true));
			}
			public void generate() throws IOException { if(!self.exists()) { self.createNewFile(); } generate(self); }
			public void generate(File file, boolean append) throws IOException {
				self = file; self.createNewFile();
				inph = new DataInputStream(new FileInputStream(self));
				outh = new DataOutputStream(new FileOutputStream(self, append));
			}
			public void generate(boolean append) throws IOException {
				if(!self.exists()) { self.createNewFile(); } generate(self, append);
			}
			public void close() throws IOException { if(self != null) { lbClose(); self = null; return; } inph = null; outh = null; }
		}
	}
	
	public static class Version1 {
		/*    TO DO:
		 * make interfaces called hasDataOutputStream, hasDataInputStream
		 * and make lbDataObject inherith lbSender and lbLoader by a way
		 * make a interface called storable
		 */
		public static interface lbSender { //todo: handle the add_Ship method
			//todo: also start to use add_ methods in send methods
			DataOutputStream getOuth();
			public default lbSender send(lbDataShip ship) throws IOException { ship.send_Ship(this); getOuth().flush(); return this; }
			public default lbSender send(byte data) throws IOException { getOuth().write(data); getOuth().flush(); return this; }
			public default lbSender sends(byte[] data) throws IOException {
				getOuth().writeInt(data.length); getOuth().write(data); getOuth().flush(); return this;
			}
			public default lbSender sends(byte[] data, int offset, int length) throws IOException {
				getOuth().write(data, offset, length); getOuth().flush(); return this;
			}
			public default lbSender sendChars(char[] string) throws IOException {
				sends(TypeConverting.char2byteArray(string)); return this;
			}
			public default lbSender sendShort(short data) throws IOException { getOuth().writeShort(data); getOuth().flush(); return this; }
			public default lbSender sendInt(int data) throws IOException { getOuth().writeInt(data); getOuth().flush(); return this; }
			public default lbSender sendLong(long data) throws IOException { getOuth().writeLong(data); getOuth().flush(); return this; }
			public default lbSender sendFloat(float data) throws IOException { getOuth().writeFloat(data); getOuth().flush(); return this; }
			public default lbSender sendDouble(double data) throws IOException { getOuth().writeDouble(data); getOuth().flush(); return this; }

			public default lbSender flush() throws IOException { getOuth().flush(); return this; }
			public default lbSender add(lbDataShip ship) throws IOException { ship.send_Ship(this); return this; }
			public default lbSender add(byte data) throws IOException { getOuth().write(data); return this; }
			public default lbSender adds(byte[] data) throws IOException {
				getOuth().writeInt(data.length); getOuth().write(data); return this;
			}
			public default lbSender adds(byte[] data, int offset, int length) throws IOException {
				getOuth().write(data, offset, length); return this;
			}
			public default lbSender addChars(char[] string) throws IOException {
				sends(TypeConverting.char2byteArray(string)); return this;
			}
			public default lbSender addShort(short data) throws IOException { getOuth().writeShort(data); return this; }
			public default lbSender addInt(int data) throws IOException { getOuth().writeInt(data); return this; }
			public default lbSender addLong(long data) throws IOException { getOuth().writeLong(data); return this; }
			public default lbSender addFloat(float data) throws IOException { getOuth().writeFloat(data); return this; }
			public default lbSender addDouble(double data) throws IOException { getOuth().writeDouble(data); return this; }
		
		}
		public static interface lbLoader {
			DataInputStream getInph();
			public default lbLoader load(lbDataShip ship) throws IOException { ship.load_Ship(this); return this; }
			public default lbDataShip load(lbDataShipBuilder builder) throws IOException { return builder.build(this); }
			public default byte load() throws IOException { return (byte) getInph().read(); }
			public default byte[] loads() throws IOException {
				byte[] bytes = new byte[getInph().readInt()]; getInph().read(bytes); return bytes;
			}
			public default char[] loadChars() throws IOException { return TypeConverting.byte2charArray(loads()); }
			public default short loadShort() throws IOException { return getInph().readShort(); }
			public default int loadInt() throws IOException { return getInph().readInt();  }
			public default long loadLong() throws IOException { return getInph().readLong(); }
			public default float loadFloat() throws IOException { return getInph().readFloat(); }
			public default double loadDouble() throws IOException { return getInph().readDouble(); }
		}
		public static abstract class lbDataHarbor implements lbLoader, lbSender {
			public DataOutputStream outh; public DataInputStream inph;
			@Override public DataOutputStream getOuth() { return outh; }
			@Override public DataInputStream getInph() { return inph; }
			public void lbClose() throws IOException {
				if(outh!=null) { outh.close(); outh = null; } if(inph!=null) { inph.close(); inph = null; }
			}
		}
		public static interface lbDataPackage { //todo: make getDataLength method by a way
			public lbDataObject getLBdata(); public lbDataObject setLBdata(lbDataObject data);
		}
		public static interface lbDataShip { //todo: handle the add_Ship method
			public lbDataShip send_Ship(lbSender sender) throws IOException;
			public lbDataShip load_Ship(lbLoader loader) throws IOException;
		}
		public static interface lbDataShipBuilder { //todo: make those buildFrom
			public lbDataShip build(lbLoader loader) throws IOException;
			public lbDataShip build(lbDataObject object) throws IOException;
		}
		public static class lbDataObject implements lbDataShip, lbDataPackage { //todo: make anoughter array copying way
			public byte[] data; public int used, taken=0;
			public void deleteData() { data = new byte[0]; used = 0; taken = 0; }
			public lbDataObject(lbLoader harbor) throws IOException { this.data = harbor.loads(); used = this.data.length; }
			public lbDataObject(byte[] data) {
				this.data = new byte[data.length]; used = data.length;
				for(int i=0;i!=data.length;i++) { this.data[i] = data[i]; }
			}
			public lbDataObject() { data = new byte[0]; used = 0; }
			public ArrayList<lbDataObjectReader> sectionReaders = new ArrayList<lbDataObjectReader>();
			public lbDataObjectReader createReader(int start) { 
				lbDataObjectReader result = new lbDataObjectReader(this);
				result.cursor = start; sectionReaders.add(result); return result;
			}
			public lbDataObjectReader createReader() { return createReader(taken); }
			public void dispose() { for(lbDataObjectReader r:sectionReaders) { r.target = null; } sectionReaders = null; }
			
			public void increasCapacity(int difference) {
				byte[] temp = new byte[data.length+difference];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			}
			public void setSpace(int length) {
				int request = length - (data.length - used);
				byte[] temp = new byte[data.length+request];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			}
			public void pushSpace(int minimum) {
				int request = minimum - (data.length - used); if(request<=0) { return; }
				byte[] temp = new byte[data.length+request];
				for(int i=0;i!=data.length;i++) { temp[i] = data[i]; } data = temp;
			}

			public void adds(byte[] value, int requested) {
				int rl = value.length > requested ? requested : value.length;
				pushSpace(rl+4); puts(value, rl); }
			public void adds(byte[] value) { pushSpace(value.length+4); puts(value); }
			public void add(byte value) { pushSpace(1); put(value); }
			public void add(lbDataObject other) { adds(other.data, other.used); }
			public void addShort(short value) { pushSpace(2); byte[] r = TypeConverting.short2bytes(value); put(r[0]); put(r[1]); }
			public void addInt(int value) { pushSpace(4); byte[] r = TypeConverting.int2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void addLong(long value) { pushSpace(8); byte[] r = TypeConverting.long2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			public void addFloat(float value) { pushSpace(4); byte[] r = TypeConverting.float2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void addDouble(double value) { pushSpace(8); byte[] r = TypeConverting.double2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			
			public void puts(byte[] value, int requested)
			{ putInt(value.length); for(int i=0;i!=requested;i++) { data[i+used] = value[i]; } used += requested; }
			public void puts(byte[] value)
			{ putInt(value.length); for(int i=0;i!=value.length;i++) { data[i+used] = value[i]; } used += value.length; }
			public void put(byte value) { data[used] = value; used++; }
			public void putShort(short value) { byte[] r = TypeConverting.short2bytes(value); put(r[0]); put(r[1]); }
			public void putInt(int value) { byte[] r = TypeConverting.int2bytes(value); put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void putLong(long value) { byte[] r = TypeConverting.long2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			public void putFloat(float value) { byte[] r = TypeConverting.float2bytes(value); put(r[0]); put(r[1]); put(r[2]); put(r[3]); }
			public void putDouble(double value) { byte[] r = TypeConverting.double2bytes(value);
			put(r[0]); put(r[1]); put(r[2]); put(r[3]); put(r[4]); put(r[5]); put(r[6]); put(r[7]); }
			
			public byte[] takes() { byte[] result = new byte[takeInt()];
			for(int i=0;i!=result.length;i++) { result[i] = data[i+taken]; }
			taken += result.length; return result; }
			public byte take() { taken++; return data[taken-1]; }
			public short takeShort() { return TypeConverting.bytes2short(new byte[] { take(), take()}); }
			public int takeInt() { return TypeConverting.bytes2int(new byte[] { take(), take(), take(), take()}); }
			public long takeLong()
			{ return TypeConverting.bytes2long(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
			public float takeFloat() { return TypeConverting.bytes2float(new byte[] { take(), take(), take(), take()}); }
			public double takeDouble()
			{ return TypeConverting.bytes2Double(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
			
			public void append(lbDataObject other) { add(other.getLBdata()); }
			
			@Override public lbDataObject getLBdata() { return this; }
			@Override public lbDataObject setLBdata(lbDataObject data) {
				this.data = new byte[data.data.length]; used = data.used; taken = data.taken;
				for(int i=0;i!=used;i++) { this.data[i] = data.data[i]; }
				return this;
			}
			@Override public lbDataObject send_Ship(lbSender harbor) throws IOException { harbor.sends(data); return null; }
			@Override public lbDataObject load_Ship(lbLoader harbor) throws IOException { data = harbor.loads(); return null; }
			
		}
		public static class lbDataObjectReader {
			public lbDataObject target; public int cursor = 0;
			public lbDataObjectReader(lbDataObject data) { target = data; cursor = data.taken; }

			public byte[] takes() { byte[] result = new byte[takeInt()];
			for(int i=0;i!=result.length;i++) { result[i] = target.data[i+cursor]; }
			cursor += result.length; return result; }
			public byte take() { cursor++; return target.data[cursor-1]; }
			public short takeShort() { return TypeConverting.bytes2short(new byte[] { take(), take()}); }
			public int takeInt() { return TypeConverting.bytes2int(new byte[] { take(), take(), take(), take()}); }
			public long takeLong()
			{ return TypeConverting.bytes2long(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
			public float takeFloat() { return TypeConverting.bytes2float(new byte[] { take(), take(), take(), take()}); }
			public double takeDouble()
			{ return TypeConverting.bytes2Double(new byte[] { take(), take(), take(), take(), take(), take(), take(), take()}); }
		}
		public static class lbSocket extends lbDataHarbor {
			public Socket self; public lbSocket() { self = null; inph = null; outh = null; }
			public lbSocket(String ip, int port) throws IOException { generate(new Socket(ip, port)); }
			public lbSocket(Socket socket) throws IOException { generate(socket); }
			public void generate(Socket socket) throws IOException {
				self = socket;
				outh = new DataOutputStream(self.getOutputStream());
				inph = new DataInputStream(self.getInputStream());
			}
			public void generate() throws IOException { generate(self); }
			public void close() throws IOException {
				if(self != null) { lbClose(); self.close(); self = null; return; }
				inph = null; outh = null;
			}
			
			public static interface lbSocketData {
				public lbSocketData send_Socket(lbSocket sender) throws IOException;
				public lbSocketData load_Socket(lbSocket loader) throws IOException;
			}
			public lbSocket sendData(lbSocketData data) throws IOException { data.send_Socket(this); return this; }
			public lbSocket loadData(lbSocketData data) throws IOException { data.load_Socket(this); return this; }
		}
		public static class lbFile extends lbDataHarbor {
			public File self; public lbFile() { self = null; outh = null; inph = null; }
			public lbFile(String path) throws IOException { generate(new File(path)); }
			public lbFile(File file) throws IOException { generate(file); }
			public lbFile(String path, boolean append) throws IOException { generate(new File(path), append); }
			public lbFile(File file, boolean append) throws IOException { generate(file, append); }
			public void generate(File file) throws IOException {
				self = file; self.createNewFile();
				inph = new DataInputStream(new FileInputStream(self));
				outh = new DataOutputStream(new FileOutputStream(self, true));
			}
			public void generate() throws IOException { if(!self.exists()) { self.createNewFile(); } generate(self); }
			public void generate(File file, boolean append) throws IOException {
				self = file; self.createNewFile();
				inph = new DataInputStream(new FileInputStream(self));
				outh = new DataOutputStream(new FileOutputStream(self, append));
			}
			public void generate(boolean append) throws IOException {
				if(!self.exists()) { self.createNewFile(); } generate(self, append);
			}
			public void close() throws IOException { if(self != null) { lbClose(); self = null; return; } inph = null; outh = null; }
			
			public static interface lbFileData {
				public lbFileData send_File(lbFile sender) throws IOException;
				public lbFileData load_File(lbFile loader) throws IOException;
			}
			public lbFile sendData(lbFileData data) throws IOException { data.send_File(this); return this; }
			public lbFile loadData(lbFileData data) throws IOException { data.load_File(this); return this; }
		}
	}
}
