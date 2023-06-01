package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

	InetAddress serverIP;
	public String serverName = "";
	public byte[] serverDecimalIP = new byte[4];
	public int serverPort = 0;

	DatagramSocket socket;
	DatagramPacket reqPacket;
	DatagramPacket ansPacket;

	byte[] reqData;
	byte[] ansData;
	int packetLength;

	int maxClients = 0;
	ArrayList<InetAddress> clientIP;
	ArrayList<Integer> clientPort;

	InetAddress clientAddr;
	int clientPor;

	public Server(int maxClients, int packetLength) {
		
		reqData = new byte[packetLength]; //Request data
		ansData = new byte[packetLength]; //Answer data
		this.packetLength = packetLength;

		//Save client info
		this.maxClients = maxClients;
		clientIP = new ArrayList<>();
		clientPort = new ArrayList<>();

		try {
			socket = new DatagramSocket(0); //Create a server in a random port
			ansPacket = new DatagramPacket(ansData, packetLength); //Initialize the packet of answers
			
			//Initialize server info
			serverIP = InetAddress.getLocalHost();
			serverName = serverIP.getHostName();
			serverDecimalIP = serverIP.getAddress();
			serverPort = socket.getLocalPort();

			System.out.println("Servidor creado exitosamente.");
			System.out.println("Nombre: " + serverName);
			System.out.println("IP: " + Arrays.toString(serverDecimalIP));
			System.out.println("Puerto: " + serverPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void sendPacketTo(InetAddress IP, int port, byte[] data) {

		Arrays.fill(reqData, (byte) 32);
		
		if (data.length < reqData.length) {
			System.arraycopy(data, 0, reqData, 0, data.length);
		} else {
			System.arraycopy(data, 0, reqData, 0, reqData.length);
		}

		try {
			reqPacket = new DatagramPacket(reqData, packetLength, IP, port);
			socket.send(reqPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPacketToAll(byte[] data) {
		Arrays.fill(reqData, (byte) 32);
		
		
		if (data.length < reqData.length) {
			System.arraycopy(data, 0, reqData, 0, data.length);
		} else {
			System.arraycopy(data, 0, reqData, 0, reqData.length);
		}
		
		
		for (int i = 0; i < clientIP.size(); i++) {
			reqPacket = new DatagramPacket(reqData, packetLength, clientIP.get(i), clientPort.get(i));
			try {
				socket.send(reqPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DatagramPacket getPacket() {
		return ansPacket;
	}
	
	public byte[] readPacketData() {
		return ansPacket.getData();
	}

	public void waitUntilReceive() {

		ansPacket = new DatagramPacket(ansData, packetLength);

		try {
			socket.receive(ansPacket);
			
			if(clientIP.size()==0) {
				clientIP.add(ansPacket.getAddress());
				clientPort.add(ansPacket.getPort());
			} else {
				for (int i = 0; i < clientIP.size(); i++) {
					if (!clientIP.contains(ansPacket.getAddress()) && clientIP.size()<maxClients) {
						clientIP.add(ansPacket.getAddress());
						clientPort.add(ansPacket.getPort());
						break;
					}
				}
			}
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeIP(InetAddress ip, int port) {
		clientIP.remove(ip);
		clientPort.remove(new Integer(port));
	}

	public void closeServer() {
		socket.close();
	}
}