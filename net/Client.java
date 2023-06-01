package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class Client {

	byte[] reqData;
	byte[] ansData;
	int packetLength = 1;

	InetAddress serverIP;
	int serverPort;

	DatagramSocket socket;
	DatagramPacket reqPacket;
	DatagramPacket ansPacket;

	public Client(int packetLength, String name, byte[] decimalIP, int port) {
		this.packetLength = packetLength;

		reqData = new byte[packetLength];
		ansData = new byte[packetLength];

		try {
			serverIP = InetAddress.getByAddress(name, decimalIP);
			socket = new DatagramSocket();
			reqPacket = new DatagramPacket(reqData, packetLength, serverIP, port);
			ansPacket = new DatagramPacket(ansData, packetLength);
			this.serverPort = port;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (SocketException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void sendPacket(byte[] data) {
		Arrays.fill(reqData, (byte) 32);

		if (data.length < reqData.length) {
			System.arraycopy(data, 0, reqData, 0, data.length);
		} else {
			System.arraycopy(data, 0, reqData, 0, reqData.length);
		}

		try {
			reqPacket = new DatagramPacket(reqData, packetLength, serverIP, serverPort);
			socket.send(reqPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] readPacketData() {
		return ansPacket.getData();
	}

	public void waitUntilReceive() {
		try {
			socket.receive(ansPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void endConnection() {
		socket.close();
	}

}
