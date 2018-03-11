import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME; 
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;
import source.Eingabe;
import source.POS_Tagger_de;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class primitiveNLU {
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		//System.out.println("return: "+searchForTitles_online("Der Name des Buchs ist Herr der Ringe"));



		System.out.println(getTitle("\"der kleine Prinz\"", true));
		
//		buecherliste_zu_TitelListe();
//		buecherliste_zu_awardListe();
//		buecherliste_zu_charaktereListe();
//		buecherliste_zu_genreListe();
		//test();
	}
	
	public static void test() throws ClassNotFoundException, IOException {
		//System.out.println(getAwards("Das Buch hat den Preis gewonnen: caldecott honor", false));
		System.out.println(getCharacter("Das Buch hat snooki", false));
		System.out.println(getTheme("Das Buch hat fantasy", false));
	}
	
	public static String intend_search_mode(String eingabe) {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		if(Intent_begruessung(eingabe))return "begrüßung";
		if(eingabe.contains("erweitern") || eingabe.contains("erweitere") ||
		eingabe.contains("suche") || eingabe.contains("empfehlung"))return "erweitern";
		if(eingabe.contains("eingrenzen") || eingabe.contains("grenze ein"))return "einschränken";
		if(eingabe.contains("ausgeben") || (eingabe.contains("gebe") && eingabe.contains("aus")
		|| eingabe.contains("anzeigen") || (eingabe.contains("zeige") && eingabe.contains("an"))))return "ausgeben";
	return "";	
	}
	
	public static String intend_ablehnung(String eingabe) {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		eingabe = satzzeichen_löschen(eingabe);
		if(eingabe.contains("das reicht")) return "nein";
		if(eingabe.contains("nein")) return "nein";
		if(eingabe.contains("mehr nicht")) return "nein";
		if(eingabe.contains("es reicht")) return "nein";
		if(eingabe.contains("es ist genug")) return "nein";
		if(eingabe.contains("will  nicht")) return "nein";
		if(eingabe.contains("das lehne ich ab") || eingabe.contains("ablehn")) return "nein";
		if(eingabe.contains("keine angaben")  || eingabe.contains("keine einschränkungen")) return "nein";
	return "";
	}
	
	/** Sucht nach Variable
	 * ausgabe: String aus {"titel", "autor", "charakter", "thema", "award", "reihe", "jahr", "rating"}
	 * @param eingabe
	 * @return
	 * @throws IOException
	 */
	public static String intend_search(String eingabe ) throws IOException {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		//am genausten
			if(eingabe.contains("autor") || eingabe.contains("von"))return "autor";
			else if(eingabe.contains("charakter"))return "charakter";
			else if(eingabe.contains("thema"))return "thema";
			else if(eingabe.contains("award"))return "award";
			else if(eingabe.contains("reihe"))return "reihe";
			else if(eingabe.contains("jahr"))return "jahr";
			else if(eingabe.contains("rating") || eingabe.contains("bewertung"))return "rating";
			else if(eingabe.contains("titel") || eingabe.matches(".* buch .* namen .*") || eingabe.contains("bezeichnung"))return "titel";
		
		//semigenau
			else if(getISBN(eingabe) != "") return "isbn";		
			else if(eingabe.matches(".*\\d\\d\\d\\d.*")) return "jahr";
			else if(eingabe.contains("suche") && eingabe.contains("buch") && namen_finden_NER(eingabe).length() == 0) return "titel";
			else if(eingabe.contains("von") && namen_finden_NER(eingabe).length() > 0 || 
					eingabe.contains("verfasser") || eingabe.contains("verfasst") || eingabe.contains("schriftsteller") ||
					eingabe.contains("schöpfer") || eingabe.contains("schrieben")) return "autor";
			else if(eingabe.contains("mit") && namen_finden_NER(eingabe).length() > 0 ||
					eingabe.contains("figur")) return "charakter";
			else if(eingabe.contains("bewertung") || eingabe.matches(".+\\d.\\d.+]") || eingabe.matches(".+[0-9].+")) return "rating";
		return "unbekannt";
	}
	
	public static String getAwards(String eingabe,  boolean anführungsstriche) throws ClassNotFoundException, IOException {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/data/awards"));
			 Object o = ois.readObject();
			 ArrayList<String> awards = (ArrayList<String>) o;// cast is needed.
			 ois.close();
			 System.out.println(awards.size());
			 String kandidat = "";
			 for(int i=0; i<awards.size(); i++) {
				String a = Datenbank.toleranzEinbauen(awards.get(i));
				//System.out.println(a);
				if((eingabe.contains(" "+a) || eingabe.contains(a+" ")) && a.length()>kandidat.length() ) {					
					kandidat = a;
				}
			 }
			return kandidat;
		}
	}
	
	public static String getAuthor(String eingabe,  boolean anführungsstriche) throws IOException {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		return namen_finden_NER(eingabe);
	}
	
	public static String getYear(String eingabe,  boolean anführungsstriche) {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			if(eingabe.matches(".+ [0-9]+ .+")) return eingabe.replaceAll("\\D+","");
		}
		return "";
	}
	
	public static String getRating(String eingabe,  boolean anführungsstriche) {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			if(eingabe.matches(".+ [0-9]+ .+")) return eingabe.replaceAll("\\D+","");
			if(eingabe.matches(".+ [0-9]+ .+")) return eingabe.replaceAll("\\D+","");
		}
		return "";
	}
	
	public static String getCharacter(String eingabe, boolean anführungsstriche) throws IOException, ClassNotFoundException {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			String versuch1 = namen_finden_NER(eingabe);
			if(!versuch1.equals(""))return versuch1;
			else {
				 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/data/charaktere"));
				 Object o = ois.readObject();
				 ArrayList<String> charaktere = (ArrayList<String>) o;// cast is needed.
				 ois.close();
				 String kandidat = "";
				 for(int i=0; i<charaktere.size(); i++) {
					String a = Datenbank.toleranzEinbauen(charaktere.get(i));
					//System.out.println(a);
					if((eingabe.contains(" "+a) || eingabe.contains(a+" ")) && a.length()>kandidat.length()) {					
						kandidat = a;
					}
				 }
				return kandidat;
			}
		}
	}
	
	public static String getTheme(String eingabe,  boolean anführungsstriche) throws FileNotFoundException, IOException, ClassNotFoundException {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/data/genres"));
			 Object o = ois.readObject();
			 ArrayList<String> genres = (ArrayList<String>) o;// cast is needed.
			 ois.close();
			 String kandidat = "";
			 for(int i=0; i<genres.size(); i++) {
				String g = Datenbank.toleranzEinbauen(genres.get(i));
				//System.out.println(a);
				if((eingabe.contains(" "+g) || eingabe.contains(g+" ")) && g.length()>kandidat.length()) {					
					kandidat = g;
				}
			 }
			return kandidat;
		}
	}
	
	public static String getReihe(String eingabe, boolean anführungsstriche) {
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		return "";
	}
	
	public static String getTitle(String eingabe,  boolean anführungsstriche) throws FileNotFoundException, IOException, ClassNotFoundException {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		if(anführungsstriche) return eingabe.substring(eingabe.indexOf("\"")+1, eingabe.lastIndexOf("\""));
		else {
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/data/titel"));
			 Object o = ois.readObject();
			 ArrayList<String> titel = (ArrayList<String>) o;// cast is needed.
			 ois.close();
			 System.out.println(titel.size());
			 String kandidat = "";
			 for(int i=0; i<titel.size(); i++) {
				String t = Datenbank.toleranzEinbauen(titel.get(i));
				if((eingabe.contains(" "+t) || eingabe.contains(t+" ")) && t.length()>kandidat.length() ) {					
					kandidat = t;
				}
			 }
			return kandidat;
		}
	
	}
	
	public static String getISBN(String eingabe) {
		String isbn = "";
		Pattern pattern = Pattern.compile("(\\d-?){13}");
		Matcher matcher = pattern.matcher(eingabe);

		if(matcher.find())isbn = (matcher.group());
		return isbn;
	}
	
	public static String begrüßung() {
		return "Hallo, bitte geben Sie Merkmale des Buches in Anführungszeichen \"\" ein. \n"
				+ "Das kann der Titel, Autor, Charakter, Sprache (Englisch oder Deutsch), ISBN, Buchreihe, Verleger, Mindestbewertung von 1-5 (von Internetnutzern), ein Thema sein";
	}
	
	public static boolean Intent_begruessung(String eingabe) {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		eingabe = satzzeichen_löschen(eingabe);
		if(eingabe.contains("hallo")) return true;
		if(eingabe.contains("servus")) return true;
		if(eingabe.contains("guten tag")) return true;
		if(eingabe.contains("hi")) return true;
		if(eingabe.contains("hey")) return true;
		if(eingabe.contains("na")) return true;
		if(eingabe.contains("heiße")) return true;
		return false;
	}
	
	 public static String satzzeichen_löschen(String eingabe) {
		 eingabe = eingabe.replaceAll(".", "").replaceAll(",", "").replaceAll("-", "").replaceAll(";", "");
		 return eingabe;
	 }
	
	public static boolean Intent_Ciao(String eingabe) {
		eingabe = Datenbank.toleranzEinbauen(eingabe);
		eingabe = satzzeichen_löschen(eingabe);
		if(eingabe.contains("tschuess")) return true;
		if(eingabe.contains("auf Wiedersehen")) return true;
		if(eingabe.contains("ade, adieu")) return true;
		if(eingabe.contains("bis bald")) return true;
		if(eingabe.contains("bis später")) return true;
		if(eingabe.contains("bis dann")) return true;
		if(eingabe.contains("bye")) return true;
		if(eingabe.contains("arrivederci")) return true;
		if(eingabe.contains("ciao")) return true;
		if(eingabe.contains("mach's gut")) return true;
		if(eingabe.contains("tschau")) return true;
		if(eingabe.contains("cheerio")) return true;
		if(eingabe.contains("tschau")) return true;
		if(eingabe.contains("auf bald")) return true;
		if(eingabe.contains("bis bald")) return true;
		if(eingabe.contains("servus")) return true;
		if(eingabe.contains("wir sehen uns")) return true;
		if(eingabe.contains("man sieht sich")) return true;
		if(eingabe.contains("pass gut auf dich auf")) return true;
		if(eingabe.contains("bis morgen ")) return true; 
		if(eingabe.contains("schönen tag")) return true;
		if(eingabe.contains("schönen abend")) return true;
		if(eingabe.contains("schönes wochenende")) return true;
		if(eingabe.contains("schönen feierabend")) return true;
		return false;
	}
	
	private static String checkForSearchTerm(String url) throws IOException {
		System.out.println("suche: "+url);
		Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").referrer("http://www.google.com").timeout(20000).get();
    	if(doc.html().contains("No results"))return "";
    	else{
    		
    		return url+": "+doc.select("title").html();
    	}
	}
	
	private static class CheckURL implements Callable {
		 private String url;
		 public CheckURL(String url) throws UnsupportedEncodingException, IOException {
			 this.url = url; 
		 }
		 public String call() throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
			 return checkForSearchTerm(url);
		 }
	 }
	
	private static ArrayList<String> searchThreading(ArrayList<String> urls, int threads) throws InterruptedException, ExecutionException, UnsupportedEncodingException, IOException {
	    ExecutorService pool = Executors.newFixedThreadPool(threads);
	    Set<Future<String>> set = new HashSet<Future<String>>();    	    
	    for (String url: urls) {
	    	Callable<String> b = new CheckURL(url);
	        Future<String> future = pool.submit(b);
	        set.add(future);
	    }
	    ArrayList<String> liste = new ArrayList<>();
	    for (Future<String> future : set) {
	    	liste.add(future.get());
	    }
	    pool.shutdownNow();
	    return liste;
	}
	
	public static String namen_finden_POS_Tagger(String eingabe) throws IOException{
		String author="";
		
		//Loading Parts of speech-maxent model       
		InputStream inputStream = new FileInputStream("./src/source/de-pos-maxent.bin");
	    POSModel model = new POSModel(inputStream); 
	     
	    //Instantiating POSTaggerME class 
	    POSTaggerME tagger = new POSTaggerME(model); 
	     
	    String sentence = eingabe; 
	     
	    //Tokenizing the sentence using WhitespaceTokenizer class  
	    WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
	    String[] tokens = whitespaceTokenizer.tokenize(sentence); 
	    
	    //Generating tags 
	    String[] tags = tagger.tag(tokens);
	    	    
	    //Instantiating the POSSample class 
	    POSSample sample = new POSSample(tokens, tags); 
	    
	    for(String satzteile: sample.toString().split(" ")) {
	    	if(satzteile.endsWith("_NE")) {
	    		author+= satzteile.replaceAll("_NE", "").replace(".", "")+" ";
	    	}
	    }
	    
	     return author;  

	}

	
	
	public static String namen_finden_NER(String eingabe) throws IOException{
		   InputStream inputStream = new FileInputStream("./src/source/en-ner-person.bin"); 
		      TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
		      
		      //Instantiating the NameFinder class 
		      NameFinderME nameFinder = new NameFinderME(model); 
		    
		      //Getting the sentence in the form of String array  
		      String [] sentence = eingabe.split(" ");     
		       
//		      sentence= new String[]{ 
//		         "Mike", 
//		         "and", 
//		         "Smith", 
//		         "are", 
//		         "good", 
//		         "friends" 
//		      }; 
		      
		      //Finding the names in the sentence 
		      Span nameSpans[] = nameFinder.find(sentence); 
		      if(nameSpans.length == 0) {
		    	  return "";
		      }
		       
		      //Printing the spans of the names in the sentence 
		      String[] nameLocs = new String[nameSpans.length];
		      
		      int ret[] = new int[nameLocs.length];
		      
		      int f=0;
		      for(Span s: nameSpans){ 
		    	  String k = s.toString();
		    	  Matcher matcher = Pattern.compile("\\d+").matcher(k);
				  matcher.find();
				  ret[f] = Integer.valueOf(matcher.group()); 
		    	  f++;
		      }
		      
		      String ausgabe = "";
		      for(int pos: ret)ausgabe+=sentence[pos]+" ";
		      return ausgabe;
	   }
	
	public static String errateTitel(String input) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
		//System.out.println("verzweifelte Suche nach title: "+input);
		ArrayList<Buch> ergebnisse = new ArrayList<Buch>();
		String[] wörter = input.split(" ");
		String kandidat = "";
		for(int laenge = wörter.length-2; laenge >= 1; laenge--) {
			for(int startpos=0; startpos+laenge <= wörter.length-1; startpos++) {
				String suche = "";
				for(int pos=startpos; pos<=startpos+laenge; pos++) {
					suche += wörter[pos];
					if(pos < startpos+laenge)suche+=" ";
				}
				ergebnisse = Datenbank.searchBook_title(suche);
				//System.out.println("Suche nach:" +suche+" "+ergebnisse.size());
				if(ergebnisse.size() > 0)return suche;
			}
		}
		return "";
	}
	
	//titel, award, charaktere, genre, 
	
	private static void buecherliste_zu_TitelListe() throws FileNotFoundException, ClassNotFoundException, IOException {
		Datenbank.load_Database();
		ArrayList<String> titel = new ArrayList<String>();
		for(Buch b: Datenbank.buecherliste) {
			titel.add(b.title);
		}
		Datenbank.removeDoubles(titel);
		 System.out.println("sichere Titelliste ... "+titel.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/data/titel"));
		 oos.writeObject(titel);
		 oos.close();
	}
	
	private static void buecherliste_zu_awardListe() throws FileNotFoundException, ClassNotFoundException, IOException {
		Datenbank.load_Database();
		ArrayList<String> awards= new ArrayList<String>();
		for(Buch b: Datenbank.buecherliste) {
			for(String aw: b.awards) {
				if(aw.contains("("))awards.add(aw.substring(0,aw.indexOf("(")-1));
				else awards.add(aw);
			}
		}
		Datenbank.removeDoubles(awards);
		 System.out.println("sichere Awardliste ... "+awards.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/data/awards"));
		 oos.writeObject(awards);
		 oos.close();
	}
	
	private static void buecherliste_zu_charaktereListe() throws FileNotFoundException, ClassNotFoundException, IOException {
		Datenbank.load_Database();
		ArrayList<String> charaktere= new ArrayList<String>();
		for(Buch b: Datenbank.buecherliste) {
			for(String aw: b.Characters) {
				 charaktere.add(aw);
			}
		}
		Datenbank.removeDoubles(charaktere);
		 System.out.println("sichere charaktereliste ... "+charaktere.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/data/charaktere"));
		 oos.writeObject(charaktere);
		 oos.close();
	}
	
	private static void buecherliste_zu_genreListe() throws FileNotFoundException, ClassNotFoundException, IOException {
		Datenbank.load_Database();
		ArrayList<String> genres= new ArrayList<String>();
		for(Buch b: Datenbank.buecherliste) {
			for(String aw: b.shelves) {
				aw = Datenbank.toleranzEinbauen(aw).replaceAll(" ", "");
				genres.add(aw);
			}
		}
		genres = Datenbank.removeDoubles(genres);
		 System.out.println("sichere genreliste ... "+genres.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/data/genres"));
		 oos.writeObject(genres);
		 oos.close();
	}
}
