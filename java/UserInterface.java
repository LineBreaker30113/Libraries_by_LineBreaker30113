package lbLibrary;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class UserInterface {
	public static class lbTextHandle_v0 {
		public char[] typed = new char[0];
		public void append(String string) { if(typed == null) { typed = new char[0]; }
			char[] strb = string.toCharArray();
			char[] temp = new char[typed.length+strb.length];
			for(int i=0;i!=typed.length;i++) { temp[i] = typed[i]; }
			for(int i=0;i!=strb.length;i++) { temp[i +typed.length] = strb[i]; }
			typed = temp;
		}
		public void append(char charachter) { if(typed == null) { typed = new char[0]; }
			char[] temp = new char[typed.length+1];
			for(int i=0;i!=typed.length;i++) { temp[i] = typed[i]; }
			temp[typed.length] = charachter; typed = temp;
		}
		public void aplyKey(char charachter) { if(typed == null) { typed = new char[0]; }
			if(charachter=='\b') { if(typed.length==0) { return; }
				char[] temp = new char[typed.length-1];
				for(int i=0;i!=temp.length;i++) { temp[i] = typed[i]; }
				typed = temp; return;
			}
			char[] temp = new char[typed.length+1];
			for(int i=0;i!=typed.length;i++) { temp[i] = typed[i]; }
			temp[temp.length-1] = charachter; typed = temp;
		}
		public void aplyKeyPress(KeyEvent e) { if(e.isActionKey()) { return; } aplyKey(e.getKeyChar()); }
		public void draw2(Graphics2D target, int ls, int x, int y) {
			ArrayList<Integer> nlines = new ArrayList<Integer>();
			int nl = 0, pl = 0, cl = 0; ls += 15;
			while(nl<typed.length) {
				for(;nl!=typed.length;nl++) { if(typed[nl]=='\n') { break; } }
				if(nl!=0) { if(typed[nl-1]=='\n') { cl++; nl++; continue; } }
				target.drawChars(typed, pl, nl-pl, x, y+(cl*ls)); nl++; pl = nl; cl++;
			}
		}
	}
	public static class lbTextHandle_v1 {
		public char[][] typed = new char[1][0]; public int cx = 0, cy = 0;
		public void clear() { typed = new char[1][0]; cx=0; cy=0; }
		public static String fname = "Monospaced"; public static Font mfont = new Font(Font.MONOSPACED, 0, 20);
		public Font generateFont(int size) { mfont = new Font(Font.MONOSPACED, 0, size); return mfont; }
		public int getCX() { return cx < typed[cy].length ? cx : typed[cy].length; }
		public String getString() {
			String result = ""; for(int i=0;i!=typed.length;i++) { result += new String(typed[i]); }
			return result;
		}
		public void drawText(Graphics2D target, int ls, int x, int y) {
			ls += mfont.getSize(); Font pf = target.getFont(); target.setFont(mfont);
			for(int i=0;i!=typed.length;i++) { target.drawChars(typed[i], 0, typed[i].length, x, y+(i*ls)); }
			target.setFont(pf);
		}
		public void drawCarrot(Graphics2D target, int ls, int x, int y) {
			ls += mfont.getSize(); int sx = x+ getCX()*mfont.getSize()*3/5, sy = y+ (cy-1)*ls;
			target.fillRect(sx, sy, mfont.getSize()/5, ls);
		}
		
		public void aplyKeyEvent(KeyEvent e) {
			if(e.isActionKey()) { aplyKey(e.getKeyCode()); return; }
			aplyChar(e.getKeyChar());
		}
		public void aplyKey(int kc) {
			switch(kc) {
			case KeyEvent.VK_UP: zb_moveCY1(-1); break; case KeyEvent.VK_DOWN: zb_moveCY1(1); break;
			case KeyEvent.VK_LEFT: zb_moveCX1(-1); break; case KeyEvent.VK_RIGHT: zb_moveCX1(1); break;
			default: break;
			}
		}
		public void zb_moveCY1(int d) { if(d==-1) { if(cy==0) { return; } cy--; return; } if(cy==typed.length-1) { return; } cy++; }
		public void zb_moveCX1(int d) {
			if(d==-1) { if(cx == 0) { if(cy == 0) { return; } zb_moveCY1(-1); cx = typed[cy].length; return; } cx--; return; }
			if(cx==typed[cy].length) { if(cy==typed.length-1) { return; } cx = 0; zb_moveCY1(1); return; }
			cx++;
		}
		/*
		 * public void aplyCursorMove(byte direction, int distance) {
			if(direction % 2 == 1) { direction -= 1; cy += direction * distance; cy %= typed.length; return; }
			direction -=2;
		}
		public void moveCX(int d) {
			if(cx==0) {  }
		}*/
		public void aplyChars(char[] chars) { for(char c : chars) { aplyChar(c); } }
		public void aplyChar(char c) {
			int rx = getCX(), ry = cy % typed.length;
			if(c == '\b') { typed = zb_removeChar(typed, ry, rx); return; }
			typed = zb_insertChar(typed, c, ry, rx);
		}
		public char[][] zb_removeChar(char[][] text, int y, int x) {
			if(x==0) { if(y==0) { return text; }
				char[][] rtext = zb_removeNL(text, y); cy--; cx = text[cy].length; return rtext;
			}
			char[] current = text[y], changed = new char[current.length-1];
			int rx = current.length < x ? current.length : x; rx--;
			for(int i=0;i!=rx;i++) { changed[i] = current[i]; }
			for(int i=rx;i!=changed.length;i++) { changed[i] = current[i+1]; }
			cx = rx; text[y] = changed; return text;
		}
		public char[][] zb_removeLine(char[][] text, int y) {
			char[][] ntext = new char[text.length-1][];
			for(int i=0;i!=y;i++) { ntext[i] = text[i]; } for(int i=y;i!=ntext.length;i++) { ntext[i] = text[i+1]; }
			return ntext;
		}
		public char[][] zb_removeNL(char[][] text, int y) {
			char[][] ntext = zb_removeLine(text, y); char[] fl = text[y-1], sl = text[y], nl;
			for(int i=0;i!=y;i++) { ntext[i] = text[i]; } for(int i=y;i!=ntext.length;i++) { ntext[i] = text[i+1]; }
			nl = new char[fl.length + sl.length]; for(int i=0;i!=fl.length;i++) { nl[i] = fl[i]; }
			for(int i=0;i!=sl.length;i++) { nl[i +fl.length] = sl[i]; }
			ntext[y-1] = nl; return ntext;
		}
		/*public char[][] removeChars(char[][] text, int cnt, int y, int x) {
			char[] current = text[y], changed = new char[current.length-cnt];
			int rx = current.length < x ? current.length : x;
			for(int i=0;i!=rx;i++) { changed[i] = current[i]; }
			for(int i=rx;i!=changed.length;i++) { changed[i] = current[i+cnt]; }
			return text;
		}*/
		public char[][] zb_insertChar(char[][] text, char c, int y, int x) {
			if(c == '\n') {
				char[][] seperated = zb_seperate(text[y], x); text[y] = seperated[0];
				cy++; cx=0; return zb_insertLine(text, seperated[1], y+1);
			}
			char[] nline = new char[text[y].length+1];
			for(int i=0;i!=x;i++) { nline[i] = text[y][i]; }
			for(int i=x;i!=text[y].length;i++) { nline[i+1] = text[y][i]; }
			nline[x] = c; cx = x+1; text[y] = nline; return text;
		}
		public char[][] zb_seperate(char[] val, int index) {
			char[] first = new char[index], second = new char[val.length-index];
			for(int i=0;i!=index;i++) { first[i] = val[i]; }
			for(int i=0;i!=val.length-index;i++) { second[i] = val[i+index]; }
			return new char[][] { first, second };
		}
		public char[][] zb_insertLine(char[][] text, char[] line, int index) {
			char[][] ntext = new char[text.length+1][];
			for(int i=0;i!=index;i++) { ntext[i] = text[i]; }
			ntext[index] = line;
			for(int i=index;i!=text.length;i++) { ntext[i+1] = text[i]; }
			return ntext;
		}
	}
	public static class lbKeyBoardHandle {
		public boolean a=false,b=false,c=false,d=false,e=false,f=false,g=false,h=false,i=false,
				j=false,k=false,l=false,m=false,n=false,o=false,p=false,r=false,s=false,t=false,
				u=false,v=false,y=false,z=false,q=false,w=false;
		public boolean up=false,down=false,left=false,right=false,control=false,alt=false, delete=false,
				altgraph=false,shift=false,enter=false,capslock=false,tab=false,comma=false,period=false,escape=false;
		public boolean quote=false,_0=false,_1=false,_2=false,_3=false,_4=false,_5=false,_6=false,_7=false,_8=false,_9=false;
		public boolean insert=false,np1=false,np2=false,np3=false,np4=false,np5=false,np6=false,np7=false,np8=false,np9=false;
		public boolean starN=false,minusN=false,backspace=false,numlock=false,nslash=false,star=false,substract=false;
		public boolean ALT() { return alt || altgraph; } public boolean Star() { return star || starN; }
		public void onKeyPress(int keyCode) { switch(keyCode) {
		case KeyEvent.VK_COMMA:comma=true;break;case KeyEvent.VK_PERIOD:period=true;break;
		case KeyEvent.VK_DELETE:delete=true;break;case KeyEvent.VK_BACK_SPACE:backspace=true;break;
		case KeyEvent.VK_MINUS:minusN=true;break;case KeyEvent.VK_SUBTRACT:substract=true;break;
		case KeyEvent.VK_ESCAPE:escape=true;break;case KeyEvent.VK_NUM_LOCK:numlock=true;break;
		case KeyEvent.VK_INSERT:insert=true;break;case KeyEvent.VK_NUMPAD1:np1=true;break;
		case KeyEvent.VK_NUMPAD2:np2=true;break;case KeyEvent.VK_NUMPAD3:np3=true;break;
		case KeyEvent.VK_NUMPAD4:np4=true;break;case KeyEvent.VK_NUMPAD5:np5=true;break;
		case KeyEvent.VK_NUMPAD6:np6=true;break;case KeyEvent.VK_NUMPAD7:np7=true;break;
		case KeyEvent.VK_NUMPAD8:np8=true;break;case KeyEvent.VK_NUMPAD9:np9=true;break;
		case KeyEvent.VK_A:a=true;break;case KeyEvent.VK_B:b=true;break;
		case KeyEvent.VK_C:c=true;break;case KeyEvent.VK_D:d=true;break;
		case KeyEvent.VK_E:e=true;break;case KeyEvent.VK_F:f=true;break;
		case KeyEvent.VK_G:g=true;break;case KeyEvent.VK_H:h=true;break;
		case KeyEvent.VK_I:i=true;break;case KeyEvent.VK_J:j=true;break;
		case KeyEvent.VK_K:k=true;break;case KeyEvent.VK_L:l=true;break;
		case KeyEvent.VK_M:m=true;break;case KeyEvent.VK_N:n=true;break;
		case KeyEvent.VK_O:o=true;break;case KeyEvent.VK_P:p=true;break;
		case KeyEvent.VK_R:r=true;break;case KeyEvent.VK_S:s=true;break;
		case KeyEvent.VK_T:t=true;break;case KeyEvent.VK_U:u=true;break;
		case KeyEvent.VK_V:v=true;break;case KeyEvent.VK_Y:y=true;break;
		case KeyEvent.VK_Z:z=true;break;case KeyEvent.VK_Q:q=true;break;
		case KeyEvent.VK_W:w=true;break;case KeyEvent.VK_UP:up=true;break;
		case KeyEvent.VK_DOWN:down=true;break;case KeyEvent.VK_LEFT:left=true;break;
		case KeyEvent.VK_RIGHT:right=true;break;case KeyEvent.VK_CONTROL:control=true;break;
		case KeyEvent.VK_ALT:alt=true;break;case KeyEvent.VK_ALT_GRAPH:altgraph=true;break;
		case KeyEvent.VK_SHIFT:shift=true;break;case KeyEvent.VK_ENTER:enter=true;break;
		case KeyEvent.VK_CAPS_LOCK:capslock=true;break;case KeyEvent.VK_TAB:tab=true;break;
		case KeyEvent.VK_QUOTEDBL:quote=true;break;
		case KeyEvent.VK_1:_1=true;break;case KeyEvent.VK_2:_2=true;break;
		case KeyEvent.VK_3:_3=true;break;case KeyEvent.VK_4:_4=true;break;
		case KeyEvent.VK_5:_5=true;break;case KeyEvent.VK_6:_6=true;break;
		case KeyEvent.VK_7:_7=true;break;case KeyEvent.VK_8:_8=true;break;
		case KeyEvent.VK_9:_9=true;break;case KeyEvent.VK_0:_0=true;break;
		} }
		public void onKeyRelease(int keyCode) { switch(keyCode) {
		case KeyEvent.VK_COMMA:comma=false;break;case KeyEvent.VK_PERIOD:period=false;break;
		case KeyEvent.VK_DELETE:delete=false;break;case KeyEvent.VK_BACK_SPACE:backspace=false;break;
		case KeyEvent.VK_MINUS:minusN=false;break;case KeyEvent.VK_SUBTRACT:substract=false;break;
		case KeyEvent.VK_ESCAPE:escape=false;break;case KeyEvent.VK_NUM_LOCK:numlock=false;break;
		case KeyEvent.VK_INSERT:insert=false;break;case KeyEvent.VK_NUMPAD1:np1=false;break;
		case KeyEvent.VK_NUMPAD2:np2=false;break;case KeyEvent.VK_NUMPAD3:np3=false;break;
		case KeyEvent.VK_NUMPAD4:np4=false;break;case KeyEvent.VK_NUMPAD5:np5=false;break;
		case KeyEvent.VK_NUMPAD6:np6=false;break;case KeyEvent.VK_NUMPAD7:np7=false;break;
		case KeyEvent.VK_NUMPAD8:np8=false;break;case KeyEvent.VK_NUMPAD9:np9=false;break;
		case KeyEvent.VK_A:a=false;break;case KeyEvent.VK_B:b=false;break;
		case KeyEvent.VK_C:c=false;break;case KeyEvent.VK_D:d=false;break;
		case KeyEvent.VK_E:e=false;break;case KeyEvent.VK_F:f=false;break;
		case KeyEvent.VK_G:g=false;break;case KeyEvent.VK_H:h=false;break;
		case KeyEvent.VK_I:i=false;break;case KeyEvent.VK_J:j=false;break;
		case KeyEvent.VK_K:k=false;break;case KeyEvent.VK_L:l=false;break;
		case KeyEvent.VK_M:m=false;break;case KeyEvent.VK_N:n=false;break;
		case KeyEvent.VK_O:o=false;break;case KeyEvent.VK_P:p=false;break;
		case KeyEvent.VK_R:r=false;break;case KeyEvent.VK_S:s=false;break;
		case KeyEvent.VK_T:t=false;break;case KeyEvent.VK_U:u=false;break;
		case KeyEvent.VK_V:v=false;break;case KeyEvent.VK_Y:y=false;break;
		case KeyEvent.VK_Z:z=false;break;case KeyEvent.VK_Q:q=false;break;
		case KeyEvent.VK_W:w=false;break;case KeyEvent.VK_UP:up=false;break;
		case KeyEvent.VK_DOWN:down=false;break;case KeyEvent.VK_LEFT:left=false;break;
		case KeyEvent.VK_RIGHT:right=false;break;case KeyEvent.VK_CONTROL:control=false;break;
		case KeyEvent.VK_ALT:alt=false;break;case KeyEvent.VK_ALT_GRAPH:altgraph=false;break;
		case KeyEvent.VK_SHIFT:shift=false;break;case KeyEvent.VK_ENTER:enter=false;break;
		case KeyEvent.VK_CAPS_LOCK:capslock=false;break;case KeyEvent.VK_TAB:tab=false;break;
		case KeyEvent.VK_QUOTEDBL:quote=false;break;
		case KeyEvent.VK_1:_1=false;break;case KeyEvent.VK_2:_2=false;break;
		case KeyEvent.VK_3:_3=false;break;case KeyEvent.VK_4:_4=false;break;
		case KeyEvent.VK_5:_5=false;break;case KeyEvent.VK_6:_6=false;break;
		case KeyEvent.VK_7:_7=false;break;case KeyEvent.VK_8:_8=false;break;
		case KeyEvent.VK_9:_9=false;break;case KeyEvent.VK_0:_0=false;break;
		} }
	}
}