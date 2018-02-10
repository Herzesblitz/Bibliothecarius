import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Dialogmanager {
	static Datenbank db = new Datenbank();
	static GUI gui = new GUI();
	
	static String zustand ="begrüßung"; //begrüßung, bücherliste_erweitern, bücherliste_einschränken, bücherlist_ausgeben,
	String usr_input ="";
	String chatbot_output="";
	static boolean beende=false;
	ArrayList<Buch> ergebnisse= new ArrayList<Buch>();
	
	public int year = Integer.MIN_VALUE;
	public String url="";
	public String sprache = "";
	public String isbn="";
	public String buecherreihe ="";
    public String title="";
    public String publisher="";
    public double rating=Integer.MIN_VALUE;
    public List<String> awards =  new ArrayList<>();
    public List<String> shelves =  new ArrayList<>();
    public ArrayList<String> similar_Books =  new ArrayList<>();
    public List<String> Characters =  new ArrayList<>();
    public List<String> Author =  new ArrayList<>();	
    
  
    //Zustand 0: Begrüßung
    	//(usr_input="", zustand = Begrüßung, ergebnisse=0) = C: Begrüßung, C: "Es gibt keine Empfehlungen, Geben Sie title, autor etc. ein", Zustand = bücherliste_erweitern
   
    //Zustand 1: bücherliste_erweitern
    	//(usr_input e {title, ...}, zustand = 1, ergebnisse=0) C: "#ergebnisse, möchten sie noch mehr Kriterien hinzufügen?
    
   	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
   		db.load_Database();
   		gui.init_frame();
   		
   		
   		while(!beende) {
   			//prüfe auf nutzereingabe
   			
   			if(zustand.contains("begrüßung")) {
   				
   			}
   			//beende = primitiveNLU.
   		}
	}
	
	//TODO durch DFA der erkennt was eingeben werden soll ...
	
	//TODO ruft primititveNLU: get... auf
	
	//TODO: generiert ausgabe

}
