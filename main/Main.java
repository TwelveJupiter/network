package main;

import javax.swing.JOptionPane;

import net.Server;

public class Main implements Runnable {

	Server ser;

	boolean working = false;
	Thread hilo;
	
	public Main(int maxClients, int DataLength) {
		ser = new Server(maxClients, DataLength);
	}

	public static void main(String[] args) {

		Main main = new Main(16, 1024);

		main.start();

		JOptionPane.showInputDialog(null, "Server currently running in port " + main.ser.serverPort + ", press 'OK' to stop it", "Server", 0, null, new String[] { "OK" }, 0);
		main.stop();
	}

	public void start() {
		working = true;
		hilo = new Thread(this);
		hilo.start();
	}

	public void stop() {
		working = false;
		hilo.interrupt();
		ser.closeServer();
	}

	@Override
	public void run() {
		while (working) {
			ser.waitUntilReceive();
			if (ser.readPacketData()[0] == -128) {
				ser.removeIP(ser.getPacket().getAddress(), ser.getPacket().getPort());
			} else {
				ser.sendPacketToAll(ser.readPacketData());
			}
		}
	}

}
