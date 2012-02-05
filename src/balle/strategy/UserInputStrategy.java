package balle.strategy;
 
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
 
import balle.controller.Controller;
import balle.world.AbstractWorld;
 
public class UserInputStrategy extends AbstractStrategy {
 
        public UserInputStrategy(Controller controller, AbstractWorld world) {
                super(controller, world);
                Listener l = new Listener();
                
                JFrame frame = new JFrame("Controller");
                frame.setSize(500,500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
                JTextField textField = new JTextField();
                frame.getContentPane().add(BorderLayout.CENTER, textField);
                
                textField.addKeyListener(l);
        }
        
        @Override
        protected void aiStep(){
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
 
        private char cmd = 's';
        
        @Override
        protected void aiMove(Controller controller) {
        	
        	System.out.println(cmd);
                
                switch (cmd) {
                case 'w':       controller.setWheelSpeeds( 99,  99); break;
                case 'a':       controller.setWheelSpeeds(-99,  99); break;
                case 's':       controller.setWheelSpeeds(  0,   0); break;
                case 'd':       controller.setWheelSpeeds( 99, -99); break;
                case 'x':       controller.setWheelSpeeds(-99, -99); break;
                case ' ':		controller.kick();
                }
                
        }
        
        class Listener implements KeyListener {
 
                @Override
                public void keyPressed(KeyEvent arg0) {
                        cmd = arg0.getKeyChar();
                }
 
                @Override
                public void keyReleased(KeyEvent arg0) {
                }
 
                @Override
                public void keyTyped(KeyEvent arg0) {                   
                }
                
        }
 
}
