package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{

	
	boolean pressedEnter = false;
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER && !pressedEnter) {
			pressedEnter = true;
			Cliente.chat.sendMsg(Cliente.chat.input.getText());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER && pressedEnter) {
			pressedEnter = false;
		}
	}
	
	
	
}
