import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javafx.scene.shape.Path;

//klasse für hintergrundbild
class BackgroundPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image img;

    public BackgroundPanel() {
        try {
            img = ImageIO.read(getClass().getResource("bibliothecarius.jpg"));
            System.out.println("Picture loaded.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Picture was not found.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    public Image getBackgroundImage() {
        return img;
    }

}


public class GUI extends JFrame implements MouseMotionListener, MouseListener, KeyListener{
	private static final long serialVersionUID = 1L;
	static int pressedX; static int pressedY; static int posX; static int posY; 
	static int deltaX; static int deltaY;
	char keyTyped;
	
	static GUI frame = new GUI();
	
	static JTextField editTextArea_input = new JTextField();
	static JTextArea   editTextArea_output = new JTextArea();
	static JScrollPane scroller = new JScrollPane(editTextArea_output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	static String input ="";
	static String output ="";
	static boolean  eingabebereit=true;
	
	private Image img;


	public static void main(String[] args) throws InterruptedException {
		frame = new GUI();
		frame.init_frame();
		mirrormode();
	}
		
	private static void mirrormode() throws InterruptedException {
		while(true) {
			Thread.sleep(20);
			String input = getInput();
			if(!input.equals(""))setOutput(input);
		}
	}
	
<<<<<<< HEAD
	public void paint( Graphics g ) { 
	    super.paint(g);
	    g.drawImage(img, 0, 0, null);
	  }
	
	public void init_frame() {		
=======
	public void init_frame2() {		
>>>>>>> b433ebd42e852e9b38c59ae30f53351e608f4f0d
		try {
            img = ImageIO.read(getClass().getResource("bibliothecarius.jpg"));
            System.out.println("Picture loaded.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Picture was not found.");
        }

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
		
<<<<<<< HEAD
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, FlowLayout.CENTER));
		//BIld
//		 setVisible(true);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         BackgroundPanel backgroundPanel = new BackgroundPanel();
//         cp.add(backgroundPanel);
//         setSize(backgroundPanel.getBackgroundImage().getWidth(backgroundPanel), backgroundPanel.getBackgroundImage().getHeight(backgroundPanel));
		
		//INPUT TEXT AREA
		JLabel label_input = new JLabel("Nutzer: ");
		label_input.setSize(100, 100);
		label_input.setOpaque(false);
		label_input.setBackground(new Color(0,0,0,0));

		BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
		cp.setLayout(layout);
		this.getContentPane().add(label_input);
		 
		editTextArea_input = new JTextField(100);
		editTextArea_input.setHorizontalAlignment(SwingConstants.LEFT);
		editTextArea_input.setOpaque(false);
		editTextArea_input.setBackground(new Color(0,0,0,0));
	    
		cp.add(editTextArea_input);
		
		JLabel label_output= new JLabel("Chatbot: ");
		label_output.setSize(100, 100);
		label_output.setOpaque(false);
		label_output.setBackground(new Color(0,0,0,0));

		this.getContentPane().add(label_output);
=======
		
		//INPUT TEXT AREA
		JLabel label_input = new JLabel("Nutzer: ");
		label_input.setSize(100,5);
		 
		editTextArea_input = new JTextField(100);
		editTextArea_input.setHorizontalAlignment(SwingConstants.LEFT);
		frame.add(editTextArea_input);
		
		JLabel label_output= new JLabel("Chatbot: ");
		label_input.setSize(100, 100);
>>>>>>> b433ebd42e852e9b38c59ae30f53351e608f4f0d
	
		editTextArea_output = new JTextArea("");
		editTextArea_output.setSize(100, 6);
		editTextArea_output.setAlignmentX(0);
<<<<<<< HEAD
		editTextArea_output.setOpaque(false);
		editTextArea_output.setBackground(new Color(0,0,0,0));
		editTextArea_output.setEditable(false);
		cp.add(editTextArea_output);
		
		scroller =new JScrollPane(editTextArea_output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setOpaque(false);
		scroller.setBackground(new Color(0,0,0,0));
		cp.add(scroller);
=======
		frame.add(editTextArea_output);
		
		scroller =new JScrollPane(editTextArea_output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scroller);
>>>>>>> b433ebd42e852e9b38c59ae30f53351e608f4f0d
		
		frame.pack();
		
		frame.setVisible(true);

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
	
	public void init_frame() {		
		try {
            img = ImageIO.read(getClass().getResource("bibliothecarius.jpg"));
            System.out.println("Picture loaded.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Picture was not found.");
        }

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
		cp.setLayout(new BoxLayout(cp, FlowLayout.CENTER));
		
//TODO: BIld
//		 setVisible(true);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         BackgroundPanel backgroundPanel = new BackgroundPanel();
//         cp.add(backgroundPanel);
//         setSize(backgroundPanel.getBackgroundImage().getWidth(backgroundPanel), backgroundPanel.getBackgroundImage().getHeight(backgroundPanel));
		
		//Layout
			BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
			cp.setLayout(layout);
	
		//Input Label
			JLabel label_input = new JLabel("Nutzer: ");
			label_input.setSize(100,5);
			this.getContentPane().add(label_input);
	
		//Input Area
			editTextArea_input = new JTextField(100);
			editTextArea_input.setHorizontalAlignment(SwingConstants.LEFT);
			cp.add(editTextArea_input);
			frame.pack();	
			
		//Output Label
			JLabel label_output= new JLabel("Chatbot: ");
			this.getContentPane().add(label_output);
	
		//Output Area
			editTextArea_output = new JTextArea("");
			editTextArea_output.setSize(100, 6);
			editTextArea_output.setAlignmentX(0);
			cp.add(editTextArea_output);
			scroller =new JScrollPane(editTextArea_output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cp.add(scroller);
		
	
		
			frame.setSize(400,400);
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
	
	private static String output_formatieren2(String output) {
		int counter_last_ZU=0;
		a: for(int pos=0; pos< output.length(); pos++) {
				if(output.charAt(pos) == '\n') {
					counter_last_ZU = 0;
					continue;
				}
				if(counter_last_ZU >= 100) {
					counter_last_ZU = 0;
					while(pos < output.length()-1 && output.charAt(pos) != ' ') {
						++pos;
						if(pos == output.length()-1) break a;
					}
					if(pos == output.length()-1) break a;
					output = output.substring(0,pos)+'\n'+output.substring(pos+1,output.length());
				}
				++counter_last_ZU;
		}
	return output;
	}
	
	private static String output_formatieren1(String output) {
		output = output.replaceAll("\n", " ");
		output = output.replaceAll("°", "\n");

		a: for(int pos=0; pos< output.length(); pos++) {
			if(pos%100 == 0 && pos > 0) {
				while(pos < output.length()-1 && output.charAt(pos) != ' ') {
					++pos;
					if(pos == output.length()-1) break a;
				}
				if(pos == output.length()-1) break a;
				output = output.substring(0,pos)+'\n'+output.substring(pos+1,output.length());
			}
		}
	return output;
	}
	
	public static void setOutput(String output) {
		//System.out.println("setOutput aufgerufen");
		GUI.output = output;
			//umbrüche setzen
		
		output = output_formatieren1(output);
		
		//System.out.println(output);
		editTextArea_output.setText(output);


		editTextArea_input.setText("");
		eingabebereit = true;
		
		editTextArea_output.revalidate();
		editTextArea_output.setSize(100,5);
		
		frame.setSize(400,400);

		
		Dimension d = new Dimension(400, 400);
		frame.setPreferredSize(d);
		frame.pack();

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
