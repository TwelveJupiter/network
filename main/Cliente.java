package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.Client;

public class Cliente implements Runnable {

	public JFrame frame;
	public JPanel jp;
	public JTextArea area;
	public JScrollPane scroll;
	public JTextField input;
	public JButton button;

	String data = "";

	String serverName = "";
	byte[] serverIP = new byte[4];
	int serverPort = 0;
	Client cl;

	String clientName = "";
	String msg = "";

	Thread hilo;
	boolean listening = false;
	
	static Cliente chat;

	public static void main(String[] args) {

		chat = new Cliente();

		chat.initClient(JOptionPane.showInputDialog(null, "Ingrese el código aquí: ", "Ventana",JOptionPane.PLAIN_MESSAGE), 1024);
		chat.startListen();
		chat.displayGUI();
		chat.joinChat(JOptionPane.showInputDialog(null, "Ingresa nombre: ", "Chat", JOptionPane.PLAIN_MESSAGE));

		// U8H9FSL+026-123+068-02059670
		// 0-6
		// 7 - 10
		// 11 - 14
		//

		
	}
	
	/*
	private void test() {
		
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			area.append("\n Hola ");
			area.setCaretPosition(area.getDocument().getLength() - 1);
		}
	}
	*/
	void initClient(String data, int packetLength) {
		/*
		serverName = data.substring(0, 7);
		serverIP[0] = Byte.valueOf(data.substring(7, 11));
		serverIP[1] = Byte.valueOf(data.substring(11, 15));
		serverIP[2] = Byte.valueOf(data.substring(15, 19));
		serverIP[3] = Byte.valueOf(data.substring(19, 23));
		serverPort = Integer.valueOf(data.substring(23));
		*/
		
		serverName = "U8H9FSL";
		serverIP[0] = 26;
		serverIP[1] = -123;
		serverIP[2] = 68;
		serverIP[3] = -20;
		
		serverPort = Integer.valueOf(data);
		
		cl = new Client(packetLength, serverName, serverIP, serverPort);
	}

	synchronized void startListen() {
		hilo = new Thread(this);
		listening = true;
		hilo.start();
	}

	private void displayGUI() {
		initGUI();
		jp.repaint();

	}

	public void initGUI() {

		frame = new JFrame("Cliente");

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				//leave();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				//leave();
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		jp = new JPanel();
		jp.setPreferredSize(new Dimension(500, 500));
		jp.setLayout(null);
		Keyboard teclas = new Keyboard();
		//frame.addKeyListener(teclas);
		frame.add(jp);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		area = new JTextArea("Chat \n", 16, 128);
		scroll = new JScrollPane(area);
		area.setBounds(20, 20, 460, 400);
		scroll.setBounds(20, 20, 460, 400);
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		
		area.setBackground(Color.white);
		jp.add(scroll);
		
		

		input = new JTextField();
		input.setBounds(20, 440, 380, 20);
		input.addKeyListener(teclas);
		jp.add(input);
		
		button = new JButton("Enviar");
		button.setBounds(410, 440, 80, 20);
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (input.getText().trim() == "q") {
					leave();
				} else {
					sendMsg(input.getText());
				}
			}

		});
		jp.add(button);

	}
	
	public void sendMsg(String text) {
		msg = clientName + ": " + text;
		cl.sendPacket(msg.getBytes());
		input.setText("");
	}

	private void joinChat(String name) {
		this.clientName = name;
		msg = clientName.concat(" acaba de entrar al chat");
		cl.sendPacket(msg.trim().getBytes());
	}
	
	private synchronized void leave() {
		cl.sendPacket((clientName + "ha salido del chat").trim().getBytes());
		cl.sendPacket(new byte[] {-128});
		listening = false;
		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cl.endConnection();
		System.exit(0);
	}
	
	
	
	@Override
	public void run() {
		
		String newText = "";
		int i = 0;
		while (listening) {
			cl.waitUntilReceive();
			
			newText = new String(cl.readPacketData());
			i = newText.length()-1;
			
			while(newText.charAt(i)==' ') {
				i--;
			}
			i++;
			
			newText = newText.substring(0, i);
			
			area.append("\n" + newText);
			area.setCaretPosition(area.getDocument().getLength() - 1);
		}
	}

}
