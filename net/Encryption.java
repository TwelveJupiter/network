package net;

import java.util.Arrays;

import javax.swing.JOptionPane;

public class Encryption {
	
	
	
	
	public static String getServerName(String data) {
		JOptionPane.showMessageDialog(null, data.substring(0, 7));
		return data.substring(0, 7);
	}
	
	public static byte[] getServerIP(String data) {
		
		byte[] ip = new byte[4];
		ip[0] = Byte.valueOf(data.substring(7, 11));
		ip[1] = Byte.valueOf(data.substring(11, 15));
		ip[2] = Byte.valueOf(data.substring(15, 19));
		ip[3] = Byte.valueOf(data.substring(19, 23));
		
		JOptionPane.showMessageDialog(null, Arrays.toString(ip));
		
		return ip;
	}
	
	public static int getServerPort(String data) {
		JOptionPane.showMessageDialog(null, Integer.valueOf(data.substring(23)));
		return  Integer.valueOf(data.substring(23));
	}
	
	
	public static byte[] longToBytes(long l) {
		byte[] b = new byte[8];
		for(int i=0;i<8;i++) {
			b[i] = (byte)((l >> ((7 - i) * 8)) & 0xff);
		}
		return b;
	}
	
	public static long bytesToLong(byte[] bytes) {
		
		long l = 0;
		for(int i=0;i<8;i++) {
			l += ((long)bytes[i]&0xff)<<(8*(7 - i));
		}
		
		return l;
	}
	
	public static byte[] encryptBytes(byte[] data) {
		return longToBytes(Long.reverse(Long.reverseBytes(bytesToLong(data))));
	}
	
	public static byte[] deCryptBytes(byte[] data) {
		return longToBytes(Long.reverseBytes(Long.reverse(bytesToLong(data))));
	}
	
}
