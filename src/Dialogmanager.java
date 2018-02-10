import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Dialogmanager {
	static int chatcounter=0;
	
	static Datenbank db = new Datenbank();
	static GUI gui  = new GUI();;
	
	static String zustand ="begrüßung"; //begrüßung, bücherliste_erweitern, bücherliste_einschränken, bücherlist_ausgeben,
	static String usr_input ="";
	static String chatbot_output="";
	static boolean beende=false;
	static ArrayList<Buch> ergebnisse= new ArrayList<Buch>();
	
	//suchvariablen
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
    
   	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException, ExecutionException {
   		//db.load_Database();
   		gui.frame.init_frame();
   		gui.frame.setOutput(primitiveNLU.begrüßung());
   		
   		while(!beende) {
   			Thread.sleep(20);
   			//prüfe auf nutzereingabe
   			usr_input = GUI.getInput();
   			if(usr_input.equals("")) {
   				continue;
   			}
   			else {
   	   			beende = primitiveNLU.Intent_Ciao(usr_input);
   				chatcounter++;
   				chatbot_output=usr_input;
				String intend_mode = primitiveNLU.intend_search_mode(usr_input);

   				if(zustand.contains("begrüßung")) {
   	   				System.out.println("Zustand: begrüßung");

   					chatbot_output = primitiveNLU.begrüßung();
   					zustand = "bücherliste_erweitern";
   	   			}
   				if(zustand.contains("bücherliste_erweitern") || intend_mode.equals("erweitern")) {
   	   				System.out.println("Zustand: bücherliste_erweitern");
	
   						String intend_search = primitiveNLU.intend_search(usr_input);

   						if(intend_search.equals("unbekannt"))chatbot_output="Entschuldigung, das habe ich nicht verstanden. Bitte Nutzen Sie Wörter wie Titel, Autor ... um mir das Wesen des Merkmals zu nennen.";
   						else {
   							if(usr_input.contains("\"")) {
   		 	 	   				if(intend_search.equals("titel")) {
   		 	 	   					String titel = primitiveNLU.getTitle(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_title(titel));
   		   							if(ergebnisse.size() == 0) ergebnisse = Datenbank.searchBook_online_titel(titel);
   		   						}
   		 	   					if(intend_search.equals("autor")) {
   			 	   					String autor = primitiveNLU.getAuthor(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_author(autor));
   		   							if(ergebnisse.size() == 0) ergebnisse = Datenbank.searchBook_online_autor(autor);
   		 	   					}
   		 	   					if(intend_search.equals("charakter")) {
   			 	   					String charakter = primitiveNLU.getCharacter(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_character(charakter));
   		 	   					}
   		 	 	   				if(intend_search.equals("thema")) {
   			 	 	   				String thema = primitiveNLU.getTheme(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_thema(thema));
   		 	 	   				}
   		 	 	   				if(intend_search.equals("award")) {
   			 	 	   				String award = primitiveNLU.getTheme(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_award(award));
   		 	 	   				}
   		 	   					if(intend_search.equals("reihe")) {
//   			 	   					String reihe = primitiveNLU.getReihe(usr_input, true);
//   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.sear(reihe));
   		 	   					}
   		 	   					if(intend_search.equals("jahr")) {
   			 	   					String jahr = primitiveNLU.getYear(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_year(Integer.valueOf(jahr), 1)); //FIXME: davor, danach und genau erkennen
   		 	   					}
   		 	 	   				if(intend_search.equals("rating"))	{
   			 	 	   				String rating = primitiveNLU.getRating(usr_input, true);
   		   							ergebnisse = Datenbank.Vereinigung(ergebnisse, Datenbank.searchBook_rating_höher(Integer.valueOf(rating)));
   		 	 	   				}
   		 	 	   				chatbot_output += " Anzahl Ergebnisse: "+ergebnisse.size();
   	 	 	   				}
   	 	 	   				else {
   	 	 	   					chatbot_output = "Entschuldigung, das habe ich nicht verstanden. Bitte geben Sie Buchmerkmal mit \" ein.";
   							}
   						}
 	 	   		}
   				if(zustand.contains("bücherliste_einschränken") ||intend_mode.equals("einschränken")) {
   					String intend_search = primitiveNLU.intend_search(usr_input);

						if(intend_search.equals("unbekannt"))chatbot_output="Entschuldigung, das habe ich nicht verstanden. Bitte Nutzen Sie Wörter wie Titel, Autor ... um mir das Wesen des Merkmals zu nennen.";
						else {
							if(usr_input.contains("\"")) {
		 	 	   				if(intend_search.equals("titel")) {
		 	 	   					String titel = primitiveNLU.getTitle(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_title(titel));
		   							if(ergebnisse.size() == 0) ergebnisse = Datenbank.searchBook_online_titel(titel);
		   						}
		 	   					if(intend_search.equals("autor")) {
			 	   					String autor = primitiveNLU.getAuthor(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_author(autor));
		   							if(ergebnisse.size() == 0) ergebnisse = Datenbank.searchBook_online_autor(autor);
		 	   					}
		 	   					if(intend_search.equals("charakter")) {
			 	   					String charakter = primitiveNLU.getCharacter(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_character(charakter));
		 	   					}
		 	 	   				if(intend_search.equals("thema")) {
			 	 	   				String thema = primitiveNLU.getTheme(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_thema(thema));
		 	 	   				}
		 	 	   				if(intend_search.equals("award")) {
			 	 	   				String award = primitiveNLU.getTheme(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_award(award));
		 	 	   				}
		 	   					if(intend_search.equals("reihe")) {
//			 	   					String reihe = primitiveNLU.getReihe(usr_input, true);
//		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.sear(reihe));
		 	   					}
		 	   					if(intend_search.equals("jahr")) {
			 	   					String jahr = primitiveNLU.getYear(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_year(Integer.valueOf(jahr), 1)); //FIXME: davor, danach und genau erkennen
		 	   					}
		 	 	   				if(intend_search.equals("rating"))	{
			 	 	   				String rating = primitiveNLU.getRating(usr_input, true);
		   							ergebnisse = Datenbank.Schnitt(ergebnisse, Datenbank.searchBook_rating_höher(Integer.valueOf(rating)));
		 	 	   				}
		 	 	   				chatbot_output += " Anzahl Ergebnisse: "+ergebnisse.size();
	 	 	   				}
	 	 	   				else {
	 	 	   					chatbot_output = "Entschuldigung, das habe ich nicht verstanden. Bitte geben Sie Buchmerkmal mit \" ein.";
							}
						}
	 	   				
   				}
   				if(zustand.contains("bücherlist_ausgeben") || intend_mode.equals("ausgeben")) {
   				System.out.println("Zustand: ausgeben");
   					chatbot_output = Datenbank.printBooklist(ergebnisse);
   					
   				}
   	   			//beende = primitiveNLU.
   			}
   			if(!chatbot_output.equals(""))GUI.setOutput(chatcounter+": "+chatbot_output);
   			
   		}
   		System.exit(0);
	}
	
	//TODO durch DFA der erkennt was eingeben werden soll ...
	
	//TODO ruft primititveNLU: get... auf
	
	//TODO: generiert ausgabe

}
