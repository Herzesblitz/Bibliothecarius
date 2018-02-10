import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;




public class GUI extends JFrame implements MouseMotionListener, MouseListener, KeyListener{
	private static final long serialVersionUID = 1L;
	static int pressedX; static int pressedY; static int posX; static int posY; 
	static int deltaX; static int deltaY;
	char keyTyped;
	
	static GUI frame = new GUI();
	
	static JTextField editTextArea_input = new JTextField();
	static JTextField editTextArea_output = new JTextField();
	
	static String input ="";
	static String output ="";
	static boolean  eingabebereit=true;


	public static void main(String[] args) {
		frame.init_frame();
		//setOutput("blbu");
	}
	
	
	public GUI() {

	}
	
	
	public void init_frame() {		
		Dimension aufloesung= Toolkit.getDefaultToolkit().getScreenSize();

		//System.out.println(aufloesung.width+" "+aufloesung.height);
		frame.setTitle("Bibliothecarius");
		frame.setResizable(true);
		frame.setLocation(0, 0);
		frame.setVisible(true);	 frame.pack();	
		frame.setSize(700,700);
		
		frame.addMouseMotionListener(this);
		frame.addMouseListener(this);
		frame.addKeyListener(this);
		
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		
		//INPUT TEXT AREA
		JLabel label_input = new JLabel("Nutzer: ");
		label_input.setSize(100, 100);

		BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
		cp.setLayout(layout);
		this.getContentPane().add(label_input);
		 
		editTextArea_input = new JTextField(100);
		editTextArea_input.setHorizontalAlignment(SwingConstants.LEFT);
		cp.add(editTextArea_input);
		
		JLabel label_output= new JLabel("Chatbot: ");
		label_input.setSize(100, 100);
		
		this.getContentPane().add(label_output);
	
		editTextArea_output = new JTextField(100);
		editTextArea_output.setHorizontalAlignment(SwingConstants.LEFT);
		cp.add(editTextArea_output);
		 
		this.setVisible(true);

		//Listener
		editTextArea_input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(eingabebereit) {
					input = editTextArea_input.getText();
					if(!input.equals("")) {
						eingabebereit = false;
						editTextArea_input.setText("Bitte keine Eingabe tätigen!");
						editTextArea_output.setText("Chatbot arbeitet, bitte warten.");
					}	
				}
				else editTextArea_input.setText("Bitte keine Eingabe tätigen!");
			}
		});	
	}
		
	public static void eingabebereitschaft_setzen(boolean a) {
		eingabebereit = a;
		editTextArea_input.setText("Bitte keine Eingabe tätigen!");
	}
	
	public static String getInput() {
		if(!input.equals("")) editTextArea_output.setText("Chatbot arbeitet, bitte warten.");
		String kopie = input;
		input = "";
		return kopie;
	}
	
	public static void setOutput(String output) {
		System.out.println("setOutput aufgerufen");
		GUI.output = output;
			//umbrüche setzen
			for(int pos=0; pos<GUI.output.length(); pos++) {
				if(pos%100 ==0 && pos > 0)GUI.output = GUI.output.substring(0,pos-1)+'\n'+GUI.output.substring(pos,GUI.output.length());
			}
		editTextArea_output.setText(GUI.output);
		editTextArea_input.setText("");
		eingabebereit = true;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
