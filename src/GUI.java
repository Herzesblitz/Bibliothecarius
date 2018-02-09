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
	
	JTextField editTextArea_input = new JTextField();
	JTextField editTextArea_output = new JTextField();

	String input ="";
	String output ="";
	boolean eingabebereit=true;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		frame.init_frame();
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
				if(eingabebereit) input = editTextArea_input.getText();
				else editTextArea_input.setText("Bitte keine Eingabe tätigen!");
			}
		});
		
		setOutput("test");
		
				
		 
}
	
	public void eingabebereitschaft_setzen(boolean a) {
		eingabebereit = a;
		editTextArea_input.setText("Bitte keine Eingabe tätigen!");
	}
	
	public String getInput() {
		return input;
	}
	
	public void setOutput(String output) {
		this.output = output;
		editTextArea_output.setText(output);
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
