import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.h2.util.NetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//FIXME: Relevanzsortierung bei searchBook

//TODO: Performancebremsen:
	/* - toleranzEinbauen: O(|buecherliste|) -> O(|buecherliste|)*15
	 * -  
	 */

class Benutzer{
	String name;
	ArrayList<Buch> favoriten;
}

public class Datenbank {
	
	static ArrayList<Buch> buecherliste = new ArrayList<>();
	static ArrayList<String> dieBücherFehlen = new ArrayList<>();


	 public static void main(String args[]) throws Exception{  
		 //repariere_database();
		//printBooklist(searchBook_title("Crimson Shell"));
		// buecher_similarBerechnen();
		 //printAllTitles();
		 
		 
		// printBooklist(searchBook_title("Lord"));		 
		// System.out.println("\n\nSORTIERT\n\n");
		ArrayList<Buch> a = (relevanz_thema(searchBook_thema(new ArrayList<String>(Arrays.asList("Humor", "Science Fiction"))), new ArrayList<String>(Arrays.asList("Humor", "Science Fiction"))));
		for(Buch b: a)System.out.println(b.shelves);
		
		 
		 //save_Database();
		 //datenbankErweitern("https://www.goodreads.com/list/show/1.Best_Books_Ever");
		// buecher_similarBerechnen();
//		 ArrayList<String> a = new ArrayList<String>(); a.add("Graphic Novels");
//		 printBooklist(searchBook_thema(a));
		 //test();

	 }
	 
	 private static void test() throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
		 //printAllSimilar();
		 //printAllTitles();
		//printBooklist(searchBook_title("The Dante Club"));
		 
		 printBooklist(searchBook_online_titel("Lord of the rings"));
		 //printBooklist(searchBook_thema("Humor"));
		 //printBooklist(searchBook_author("Funky Chicken"));
		 
		//ArrayList<String> genre = new ArrayList<String>(); genre.add("Humor");
		 //printBooklist(Schnitt(searchBook_rating_höher(3), searchBook_thema(genre)));
				
		 //printBooklist(Schnitt(searchBook_title("A"), searchBook_title("The Road")));
	 }
	 
	 
	 /**
	  * Benutze Listen aus:https://www.goodreads.com/list/popular_lists
	  * @param url_liste
	  * @throws ClassNotFoundException
	  * @throws IOException
	  * @throws InterruptedException
	  * @throws ExecutionException
	  */
	 private static void datenbankErweitern(String url_liste) throws ClassNotFoundException, IOException, InterruptedException, ExecutionException {
			load_Database();
			refresh_Database_threading(20, url_liste,-1);
	 }
	 
	 private static void datenbankErweitern() throws ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		load_Database();
		while(true) {
			refresh_Database_threading(20, 20);
			repariere_database();
			sort_database();
			save_Database(); 
		}
	 }
	 
	 public static class BookCallable implements Callable {
		 private String url;
		 public BookCallable(String url) throws UnsupportedEncodingException, IOException {
			 this.url = url; 
		 }
		 public Buch call() throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
			 return Buch.buchToinfosBuecher("", "", url, 10);
		 }
	 }
	 
	 //Kann durch RASA aufgerufen werden: 
	 	//..._LS benutzen die LevensteinDistanz zur Suche (werden immer die Buecher mit dem ähnlichsten String zurueckgeben)
	 
	 /**
	  * Nimmt folgende Änderungen an String vor, damit Matching verbessert wird 
	  * @return
	  */
	 public static String toleranzEinbauen(String eingabe) {	
		 eingabe = eingabe.toLowerCase().replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll("ë", "e");
		 eingabe = eingabe.replaceAll("ú", "u").replaceAll("é", "e").replaceAll("ó", "o").replaceAll("í", "i").replaceAll("á", "a");
		 eingabe = eingabe.replaceAll("ù", "u").replaceAll("è", "e").replaceAll("ò", "o").replaceAll("ì", "i").replaceAll("à", "a");
	  return eingabe;
	 }
	 
	 	/**
	 	 * füge jedes buch hinzu was mind. einen awards
	 	 * @param awards
	 	 * @return
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
	 	//TODO: klapptext, publisher, blurb 	
	 	public static ArrayList<String> searchBook_ähnlich(Buch b){
	 		if(b.similar_Books != null) return b.similar_Books;
	 		else return new ArrayList<String>();
	 	}
	
	 	
	 	
	 	/**
	 	 * @param results
	 	 * @param title
	 	 * @return
	 	 */
	 	private static ArrayList<Buch> relevanz_title(ArrayList<Buch>results, String title){
	 		HashMap<Buch, Integer> dif = new HashMap<Buch, Integer>();
	 		//levenshteinDistance berechnen
	 		for(Buch b: results) {
	 			dif.put(b, levenshteinDistance(b.title, title));
	 		}
	 		return sort_aufsteigend(dif);
	 	}
	 	
	 	private static ArrayList<Buch> relevanz_sprache(ArrayList<Buch>results, String sprache){
	 		HashMap<Buch, Integer> dif = new HashMap<Buch, Integer>();
	 		//levenshteinDistance berechnen
	 		for(Buch b: results) {
	 			dif.put(b, levenshteinDistance(b.sprache, sprache));
	 		}
	 		return sort_aufsteigend(dif);
	 	}
	 	
	 	private static ArrayList<Buch> relevanz_year(ArrayList<Buch>results, int year){
	 		HashMap<Buch, Integer> dif = new HashMap<Buch, Integer>();
	 		//levenshteinDistance berechnen
	 		for(Buch b: results) {
	 			dif.put(b, Math.abs(b.year - year));
	 		}
	 		return sort_aufsteigend(dif);
	 	}
	 	
	 	private static ArrayList<Buch> relevanz_thema(ArrayList<Buch>results, ArrayList<String> themen){
	 		HashMap<Buch, Integer> dif = new HashMap<Buch, Integer>();
	 		//levenshteinDistance berechnen
	 		for(Buch b: results) {
	 			int autor_dif=0;
	 			for(String tB: b.shelves) {
	 				for(String tS: themen) {
		 				autor_dif += levenshteinDistance(tB, tS);
	 				}
	 			}
	 			dif.put(b, autor_dif);
	 		}
	 		return sort_aufsteigend(dif);
	 	}
	 	
	 	/**
	 	 * Großschreibung! gibt alle Bücher mit angegebener Sprache zurück
	 	 * @param sprache
	 	 * @return
	 	 * @throws IOException 
	 	 * @throws ClassNotFoundException 
	 	 * @throws FileNotFoundException 
	 	 */
	 	public static ArrayList<Buch> searchBook_sprache(String sprache) throws FileNotFoundException, ClassNotFoundException, IOException{
	 		if(buecherliste.size() == 0)load_Database();
			ArrayList<Buch> results = new ArrayList<Buch>();
	 		switch(sprache) {
	 			case "Deutsch":{
	 				for(Buch b: buecherliste) {
						 if(b.sprache.equals("German")) {
							 results.add(b);
						 }
					}
	 			}break;
	 			case "Englisch":{
	 				for(Buch b: buecherliste) {
						 if(b.sprache.equals("English")) {
							 results.add(b);
						 }
					}
	 			}break;
	 			default: {
	 				System.err.println("keine anerkannte Sprache angegeben!");
	 				return null;
	 			}
	 		}
	 		return results;
	 	}
	 
	 	/**
	 	 * modus = 0: exaktes Jahr, modus = -1: davor, modus = 1: dannach
	 	 * @param year
	 	 * @param zeit
	 	 * @return
	 	 * @throws IOException 
	 	 * @throws ClassNotFoundException 
	 	 * @throws FileNotFoundException 
	 	 */
	 	 public static ArrayList<Buch> searchBook_year(int year, int modus) throws FileNotFoundException, ClassNotFoundException, IOException{
	 		 if(buecherliste.size() == 0)load_Database();
	 		 if(modus < -1 || modus > 1) {
	 			 System.err.println("falscher Wert für Parameter "+modus);
		 		 return null;
	 		 }
	 		 else {
	 			 ArrayList<Buch> results = new ArrayList<Buch>();
		 		 //suche nach exaktem Titel
	 			 switch(modus) {
	 			 	case -1: {
	 			 		 for(Buch b: buecherliste) {
	 						 if(b.year < year) {
	 							 results.add(b);
	 						 }
	 					 }
	 			 	}break;
	 			 	case 0: {
	 			 		 for(Buch b: buecherliste) {
	 						 if(b.year == year) {
	 							 results.add(b);
	 						 }
	 					 }
	 			 	}break;
	 			 	case 1: {
	 			 		 for(Buch b: buecherliste) {
	 						 if(b.year > year) {
	 							 results.add(b);
	 						 }
	 					 }
	 			 	}break;
	 			 }
				
				 return results;
	 		 }
	 	 }
	 
		 public static ArrayList<Buch> searchBook_awards(ArrayList<String> awardliste) throws FileNotFoundException, ClassNotFoundException, IOException {
			 load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String award_buch: b.awards) {
					for(String award_suche: awardliste) {
						if(toleranzEinbauen(award_buch).contains(toleranzEinbauen(award_suche))) {
							results.add(b);
							break;
						}
					}
				}
			 } 
	 		return results;
		 }
	 	
		 /**
		  * wird von mir benutzt ...
		  * @param isbn
		  * @return
		  * @throws FileNotFoundException
		  * @throws ClassNotFoundException
		  * @throws IOException
		  */
		 public static Buch searchBook_URL(String url) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 if(buecherliste == null) {
	 			 load_Database();
	 		 }
	 		 for(Buch b: buecherliste) {
	 			 return (b);
	 		 }
	 		 return null;
		}
		 
	 	/**
	 	 * gibt genau die bücher 
	 	 * @param thema
	 	 * @return
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
		public static ArrayList<Buch> searchBook_ISBN(String isbn) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Buch> returnlist = new ArrayList<Buch>();
	 		 for(Buch b: buecherliste) {
	 			 if(b.isbn == isbn)returnlist.add(b);
	 		 }
	 		 return returnlist;
		}
	 
	    /**
	  	* sucht nach ALLEN Buechern die dieses Thema haben
	    * @param character
	    * @return
	 	* @throws FileNotFoundException
	 	* @throws ClassNotFoundException
	    * @throws IOException
	 	*/
		public static ArrayList<Buch> searchBook_thema(ArrayList<String> themenliste) throws FileNotFoundException, ClassNotFoundException, IOException {
			load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String thema_buch: b.shelves) {
					for(String thema_suche: themenliste) {
						//kaiserin -> auf kindliche kaiserin matchen
						if(toleranzEinbauen(thema_buch).contains(toleranzEinbauen(thema_suche))) {
							results.add(b);
							break;
						}
					}
				}
			 } 
	 		return results;
	 	 }
	 
	 	/**
        * sucht nach ALLEN Buechern die diesen Charakter haben
	    * @param character
	    * @return
	 	* @throws FileNotFoundException
	 	* @throws ClassNotFoundException
	    * @throws IOException
	 	*/
	    public static ArrayList<Buch> searchBook_characters(ArrayList<String> charakterliste) throws FileNotFoundException, ClassNotFoundException, IOException {
	    	load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String charakter_buch: b.Characters) {
					for(String charakter_suche: charakterliste) {
						//kaiserin -> auf kindliche kaiserin matchen
						if(toleranzEinbauen(charakter_buch).toLowerCase().contains(toleranzEinbauen(charakter_suche))) {
							results.add(b);
							break;
						}
					}
				}
			 } 
	 		return results;
	 	 }
	 	 
	 	/**
 	 	 * sucht nach ALLEN Buechern die diesen Autor haben
	 	 * @param author
	 	 * @return
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
	 	public static ArrayList<Buch> searchBook_author(ArrayList<String> authorliste) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String author_buch: b.Author) {
					for(String author_suche: authorliste) {
						//kaiserin -> auf kindliche kaiserin matchen
						if(toleranzEinbauen(author_buch).contains(toleranzEinbauen(author_suche))) {
							results.add(b);
							break;
						}
					}
				}
			 } 
	 		return results;
	 	}
	 	
	 	public static ArrayList<Buch> searchBook_rating_höher(double rating) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Titel
			 for(Buch b: buecherliste) {
				 if(b.rating >= rating) {
					 results.add(b);
				 }
			 }
			 return results;
	 	}
	 	
	 	/**
	 	 * sucht nach ALLEN Buechern die diesen Titel tragen
	 	 * @param title
	 	 * @return
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
	 	public static ArrayList<Buch> searchBook_title(String title) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 if(buecherliste.size() == 0)load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Titel
			 for(Buch b: buecherliste) {
				 if(toleranzEinbauen(b.title).contains(toleranzEinbauen(title))) {
					 results.add(b);
				 }
			 }
	 		 //wenn das fehlschlägt die besten n bücher
	 		 ///if(results.size()==0) {
//	 			 Buch min_b = buecherliste.get(0); 
//	 			 for(Buch b: buecherliste) {
//		 				 int levenshteinDistance_buch = levenshteinDistance(b.title,title);
//						 int levenshteinDistance_min_b = levenshteinDistance(min_b.title,title);
//						 if(levenshteinDistance_buch > levenshteinDistance_min_b) {
//							 min_b = b;
//						 }		
//	 			 }
//	 			 results.add(min_b);
//	 		 }
	 		 
//	 			a: while(results.size() < 5) {
//		 			 Buch min_b = buecherliste.get(0);
//		 			 for(Buch b: buecherliste) {
//		 				 int levenshteinDistance_buch = levenshteinDistance(b.title,title);
//						 int levenshteinDistance_min_b = levenshteinDistance(min_b.title,title);
//		 				 //System.out.println(b.title+" "+levenshteinDistance_buch+" "+levenshteinDistance_min_b);
//						 if(levenshteinDistance_buch <=5) {
//							 if(levenshteinDistance_buch < levenshteinDistance_min_b) {
//								 results.add(b);
//								 min_b = b;
//							 }
//						 }
//						 //nach letztem buch: zufuegen?
//						 if(buecherliste.indexOf(b) == buecherliste.size()-1) {
//							 if(!results.contains(min_b))results.add(min_b);
//						 }
//		 			 }	
//					 if(min_b == buecherliste.get(0))break a;
//
//				 }
//	 		 }
//	 		 ArrayList<Buch> r = new ArrayList<Buch>();
//	 		 r.addAll(results);
	 		 return results ;
	 	}
	 	
	 	/**
	 	 * sucht nach EINEM Buch, welches diesen Parametern am MEISTEN entspricht
	 	 * @param title
	 	 * @param author
	 	 * @param ISBN
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
	 	public static void searchBook(String title, String author, String ISBN) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
			 System.out.println("datenbank geladen ...");
			 if(ISBN!="") {
				 Buch min_b = buecherliste.get(0);
				 for(Buch b: buecherliste) {
					 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
					 if(levenshteinDistance(b.isbn,ISBN) < levenshteinDistance(min_b.isbn,ISBN) ) {
						 min_b = b;
					 }
				 } 
			 }
			 else {
				 if(ISBN!="") {
					 Buch min_b = buecherliste.get(0);
					 double min_distance = Integer.MAX_VALUE;
					 for(Buch b: buecherliste) {
						 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
						 for(String author_: b.Author) {
							 if((levenshteinDistance(author_,author) + levenshteinDistance(b.title,title)) < min_distance) {
								 min_distance = (levenshteinDistance(author_,author) + levenshteinDistance(b.title,title));
								 min_b = b;
							 } 
						 }
						 
					 } 
				 }
			 }
	 	 }
	 	
	 	//TODO: titel von ersten Ergebnisseite https://www.goodreads.com/search+autor ausgeben
	 	public static ArrayList<String> searchBook_online_autor(String autor) {
	 		return null;	 		
	 	}
	 	
	 	public static ArrayList<Buch> searchBook_online_titel(String titel) throws UnsupportedEncodingException, ClassNotFoundException, IOException, InterruptedException {
	 		System.out.println("Buch in der Datenbank nicht gefunden. Suche Online danach ...");
	 		ArrayList<Buch> results = new ArrayList<Buch>();
	 		results.add(Buch.buchToinfosBuecher(titel, "", "", 10));
	 		return results;
	 	}
	 	 
	 	public static void printBook(Buch b) {
	 		 Buch.ausgebenBuch(b);
	 	}
	 	
	 	/**
	 	 * richtigen Parameter zur Auswahl benutzen
	 	 * @param b
	 	 * @param param
	 	 */
	 	public static void printBook_param(Buch b, String param) {
	 			if(param.equals("klapptext" ))
	 			if(param.equals("sprache") && !b.sprache.equals(""))System.out.println("Sprache: "+b.sprache);
				if(param.equals("isbn") && !b.isbn.equals(""))System.out.println("ISBN: "+b.isbn);
	 			if(param.equals("url") && !b.url.equals(""))System.out.println("URL: "+b.url);
				if(param.equals("title") && !b.title.equals(""))System.out.println("Title of the Book: "+b.title); 
				if(param.equals("bücherreihe") && !b.buecherreihe.equals(""))System.out.println("buecherreihe of the Book: "+b.title); 
				if(param.equals("author") && b.Author.size() == 1) System.out.println("Author: "+b.Author.get(0));
				else if (param.equals("author") && b.Author.size() != 1) {System.out.print("Authors: "); for (int i=0; i<b.Author.size()-1; i++) System.out.print(b.Author.get(i)+", "); System.out.println(b.Author.get(b.Author.size()-1));}
				if(param.equals("year") && b.year != Integer.MIN_VALUE)System.out.println("Publihsing year: "+b.year);
//				System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
				if(param.equals("publisher") && !b.publisher.equals(""))System.out.println("Publisher: "+b.publisher);
				if(param.equals("rating") && b.rating != Integer.MIN_VALUE)System.out.println("Rating: "+b.rating);
				System.out.println("________________________________________________");
				if(param.equals("character") && b.Characters.size() > 0) {
					System.out.println("Characters in the Book: \n");
					for (String x: b.Characters) System.out.println(x);
					System.out.println("________________________________________________");
				}
				if(param.equals("thema") && b.shelves.size() > 0) {
					System.out.println("Thema of the Book: \n");
					for (String x: b.shelves)	System.out.println(x);
					System.out.println("________________________________________________");
				}
				if(param.equals("awards") && b.awards.size() > 0) {
					System.out.println("Awards of the Book: \n");
					for (String x: b.awards)	System.out.println(x);
					System.out.println("________________________________________________");
					
				}
				if(param.equals("blurb") && !b.blurb.equals("")) {
					System.out.println("Blurb of the Book: \n");
					System.out.println(b.blurb);
				}
				if(param.equals("klapptext") && !b.covertext.equals("")) {
					System.out.println("Covertext of the Book: \n");
					System.out.println(b.covertext);
				}
		
	 	}
	 	 
	 	public static void printBooklist(ArrayList<Buch> b) {
	 		 Buch.ausgebenBücherliste(b);
	 	 }
	 
		public static ArrayList<Buch> Schnitt(ArrayList<Buch> a, ArrayList<Buch> b){
//			for(Buch b1: b)System.out.println(b1.title);
//			System.out.println();
//			for(Buch b1: a)System.out.println(b1.title);

			ArrayList<Buch> list = new ArrayList<Buch>();
	        for (Buch buch : a) {
		        for (Buch buch1 : b) {
		        	if(!(buch1.isbn.equals("") || buch.isbn.equals(""))) {
		        		if(buch.isbn.equals(buch1.isbn)) {
			        		list.add(buch);
			        	}
		        		continue;
		        	}
		        	if(!(buch1.title.equals("") || buch.title.equals(""))) {
		        		if(buch.title.equals(buch1.title)) {
			        		list.add(buch);
			        	}
		        		continue;
		        	}
		        }
	        }
	        return list;
	 	 }
	 
	 	public static ArrayList<Buch> Vereinigung(ArrayList<Buch> a, ArrayList<Buch> b){
	 		 Set<Buch> set = new HashSet<Buch>();
		        set.addAll(a);
		        set.addAll(b);
		     return new ArrayList<Buch>(set);
	 	}
	 
	 	public static ArrayList<String> removeDoubles(ArrayList<String> a){
		 	ArrayList<String> b = new ArrayList<>();
		 	for(String s: a) {
		 		if(b.contains(s))continue;
		 		else b.add(s);
		 	}
		 	return b;
	 	}  

	 
	 public static ArrayList<Buch> buchThreading(ArrayList<String> urls, int threads) throws InterruptedException, ExecutionException, UnsupportedEncodingException, IOException {
		    ExecutorService pool = Executors.newFixedThreadPool(threads);
		    Set<Future<Buch>> set = new HashSet<Future<Buch>>();    	    
		    for (String url: urls) {
		    	Callable<Buch> b = new BookCallable(url);
		        Future<Buch> future = pool.submit(b);
		        set.add(future);
		    }
		    ArrayList<Buch> liste = new ArrayList<>();
		    for (Future<Buch> future : set) {
		    	liste.add(future.get());
		    }
		    pool.shutdownNow();
		    return liste;
	 }
	 
	 public static void load_dieBücherFehlen() throws IOException { 
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/el"));
		 oos.writeObject(dieBücherFehlen);
		 oos.close();
	 }
	 
	 public static void save_dieBücherFehlen() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/el"));
		 dieBücherFehlen = (ArrayList<String>) ois.readObject(); // cast is needed.
		 System.out.println("laenge: "+dieBücherFehlen.size());
		 ois.close();
	 }
	 
	 public static void save_Database() throws IOException, ClassNotFoundException { 
		 System.out.println("sichere Datenbank ... "+buecherliste.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/db"));
		 oos.writeObject(buecherliste);
		 oos.close();
	 }
	 
	 public static void load_Database() throws FileNotFoundException, IOException, ClassNotFoundException {
		 if(buecherliste.size() > 0)return;
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/db"));
		 Object o = ois.readObject();
		 buecherliste = (ArrayList<Buch>) o;// cast is needed.
		 System.out.println("load_Database ... : "+buecherliste.size());
		 //repariere_database();
		 ois.close();
	 }
	 
	 public static void printAllSimilar() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("Datenbank geladen");
		 for(Buch b: buecherliste) {
			 if(b.similar_Books.size() == 0)continue;
			 System.out.println(b.title);
			 if(b.similar_Books.size() > 0) {
				 for(String a: b.similar_Books)System.out.println(" "+a);
			 }
			 else System.out.println();
		 }
		 System.out.println(buecherliste.size());
	 }
	 	 
	 public static void printAllTitles() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("Datenbank geladen");
		 for(Buch b: buecherliste) {
			 System.out.print(b.title+" "+b.similar_Books.size());
			 if(!b.buecherreihe.equals(""))System.out.println(" ... aus der Reihe ..."+b.buecherreihe);
			 else System.out.println();
		 }
		 System.out.println(buecherliste.size());
	 }
	 	 
	 //TODO: moeglicherweise ineffizient
	 public static void sort_database() throws FileNotFoundException, ClassNotFoundException, IOException {
		if(buecherliste == null) load_Database();
		// Sorting
		 Collections.sort(buecherliste, new Comparator<Buch>() {
		         @Override
		         public int compare(Buch a, Buch b)
		         {
		             return  a.title.compareTo(b.title);
		         }
		     });
		 save_Database();
	 }
	 
	 public static ArrayList<Buch> sortAL(ArrayList<Buch> neu) {
		// Sorting
				 Collections.sort(neu, new Comparator<Buch>() {
				         @Override
				         public int compare(Buch a, Buch b)
				         {
				             return  a.title.compareTo(b.title);
				         }
				     });
				return neu;
	 }
	 
	 public static ArrayList<Buch> sortmerge(ArrayList<Buch> alt, ArrayList<Buch> neu) throws FileNotFoundException, ClassNotFoundException, IOException {
		 neu = sortAL(neu);
		 int index1;
		 int index2;
		 for (index1 = 0, index2 = 0; index2 < neu.size(); index1++) {
			 	//returns < 0 then the String calling the method is lexicographically first (comes first in a dictionary)
			 	//for(Buch buch: alt)System.out.println(buch.title);
			 	//System.out.println("index alt: "+index1+" index neu:"+index2);
			 	if (index1 == alt.size() || neu.get(index2).title.compareTo(alt.get(index1).title) <= 0) {
			            alt.add(index1, neu.get(index2++));
			    }
		 }
		 //save_Database();
		return alt;
	 }
	 
	
	 
	 private static boolean contains_duplicates(ArrayList<Buch> al, String title) {
		 int anz=0;
		 for(Buch b: al) {
			 if(b.title.equals(title))anz++;
		 }
		 if(anz == 0)return false;
		 else return true;
	 }
	 
	 /**
	  * löscht null objekte und duplikate
	  * @throws FileNotFoundException
	  * @throws ClassNotFoundException
	  * @throws IOException
	  */
	 public static void repariere_database() throws FileNotFoundException, ClassNotFoundException, IOException {
		if(buecherliste == null)load_Database();
		int anz =0;
	    ArrayList<Buch> bl = new ArrayList<Buch>();
	    buecherliste.removeAll(Collections.singleton(null));
		for(Buch a: buecherliste) {
			//diverse reperaturen
			if(a.similar_Books == null) {a.similar_Books = new ArrayList<String>();}
			if(!contains_duplicates(bl, a.title)) {bl.add(a); anz++;}
			//FIXME: thema x1 > x2 -> x2
			ArrayList<String> neu = new ArrayList<String>();
			ArrayList<String> remove = new ArrayList<String>();

			for(String t:a.shelves) {
				if(t.contains(">")) {
					neu.add(t.substring(t.lastIndexOf(">")+1)); 
				}
			}
			for(String t:a.shelves) {
				if(t.contains(">")) {
					remove.add(t);
				}
			}
			for(String n: neu) {
				a.shelves.add(n);
			}
			a.shelves.removeAll(remove);
			
		}
		//System.out.println(bl.size()+" / "+buecherliste.size());
		if(anz > 0) {
			buecherliste = new ArrayList<>(bl);
			save_Database();	
		}	
	 }
	 
	 		private static int min(int... numbers) {return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);}
	 		private static int costOfSubstitution(char a, char b) {return a == b ? 0 : 1;}
	 private static int levenshteinDistance(String x, String y) {
		    int[][] dp = new int[x.length() + 1][y.length() + 1];
		 
		    for (int i = 0; i <= x.length(); i++) {
		        for (int j = 0; j <= y.length(); j++) {
		            if (i == 0) {
		                dp[i][j] = j;
		            }
		            else if (j == 0) {
		                dp[i][j] = i;
		            }
		            else {
		                dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
		                  dp[i - 1][j] + 1, 
		                  dp[i][j - 1] + 1);
		            }
		        }
		    }
		 
		    return dp[x.length()][y.length()];
		}
	 
	 /**
	 	 * sortiert eine HashMap<Buch, Integer> aufsteigend nach ihren Values in O(n²)
	 	 * @param dif
	 	 * @return
	 	 */
	 	private static ArrayList<Buch> sort_aufsteigend(HashMap<Buch,Integer> dif) {
	 		ArrayList<Buch> results_aufsteigend = new ArrayList<Buch>();
	 		int minV = Integer.MAX_VALUE;
	 		Buch minB = new Buch();
	 		while(dif.size() > 0) {
	 			for(Buch b: dif.keySet()) {
	 				if(dif.get(b).intValue() < minV) {
	 					minB = b;
	 					minV = dif.get(b).intValue();
	 				}
	 			}
	 			minV = Integer.MAX_VALUE;
	 			results_aufsteigend.add(minB);
	 			dif.remove(minB);
	 		}
	 		return results_aufsteigend;
		}
	 
	 public static <Buch, Integer extends Comparable<? super Integer>> Map<Buch, Integer> sortByValue(Map<Buch, Integer> map) {
	     List<Map.Entry<Buch, Integer>> list = new LinkedList<Map.Entry<Buch, Integer>>(map.entrySet());
	     Collections.sort( list, new Comparator<Map.Entry<Buch, Integer>>() {
	         public int compare(Map.Entry<Buch, Integer> o1, Map.Entry<Buch, Integer> o2) {
	             return (o1.getValue()).compareTo( o2.getValue() );
	         }
	     });
	
	     Map<Buch, Integer> result = new LinkedHashMap<Buch, Integer>();
	     for (Map.Entry<Buch, Integer> entry : list) {
	         result.put(entry.getKey(), entry.getValue());
	     }
     return result;
	 }
	
	/**
	 * 
	 * @param books_nr
	 * @param anz_threads
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	 public static void refresh_Database_threading(int books_nr, int anz_threads) throws IOException, ClassNotFoundException, InterruptedException, ExecutionException {
		if(buecherliste != null) load_Database();
		 
		 int books=0;
		 int page_lists =(int) (Math.random()*1000);
		 b: while(true) {
			 String url_allLists= "https://www.goodreads.com/list/recently_active_lists?page=" + page_lists;
			 org.jsoup.nodes.Document doc_List_page_n = Jsoup.connect(url_allLists).get();
			 if(doc_List_page_n.html().toString().contains("No lists yet...")) {
				 System.out.println("Ende"); return;
			 } 
			 	//gehe durch buecherlisten
			 	Elements url_Lists_page_n= doc_List_page_n.select("a.listTitle");
			 	for(Element list: url_Lists_page_n) {
			 		int page_books = 0;
			 		while(true) {
			 			String url_list_x_page_n= "https://www.goodreads.com/"+list.attr("href")+"?page="+page_books;		
						org.jsoup.nodes.Document doc_list_X_page_x = Jsoup.connect(url_list_x_page_n).get();
						if(!doc_list_X_page_x.html().toString().contains("bookTitle")) {
							 break;
						} 
				 		System.out.println("page: "+page_books);
					 	Elements url_Books_list_x_page_n=  doc_list_X_page_x.select("a.bookTitle");
					 	System.out.println("Buchliste: "+list.text()+list.attr("href"));
						 	//gehe durch buecher
					 			//fuege alle urls in arrayList
					 			ArrayList<String> urlS = new ArrayList<>();
							 	for(Element book: url_Books_list_x_page_n) {
							 		urlS.add("https://www.goodreads.com"+book.attr("href"));
							 		books++;
							 		if(books > books_nr)break;
							 	}
							 	//ubertrage anzThreads buecher von url-liste zu teilmengen-liste
							 	while(urlS.size() > 0) {
						 			ArrayList<String> urlS_teil = new ArrayList<>();
						 			int i;
						 			for(i=0; i<anz_threads; i++) {
						 				if(urlS.size() == 0)break;
						 				urlS_teil.add(urlS.remove(0));
						 			}
						 			
						 			books+=i+1; 
						 			List<Buch> tmp = buchThreading(urlS_teil,anz_threads);
						 			buecherliste.addAll(tmp);
							 		if(books >= books_nr)break b;
							 	}
						 page_books++;
			 		}
			 	}
		 page_lists++;  
		 }
		 save_Database();
	 }
	 
	 /**
	  * traegt alle buecher dieser liste ein
	  * @param books_nr
	  * @param anz_threads
	  * @param url
	  * @throws IOException
	  * @throws ClassNotFoundException
	  * @throws InterruptedException
	  * @throws ExecutionException
	  */
	 public static void refresh_Database_threading(int anz_threads, String url, int pageoffset) throws IOException, ClassNotFoundException, InterruptedException, ExecutionException {
			if(buecherliste != null) load_Database();
			int page_books;
				 		if(pageoffset == -1) page_books= (int) (Math.random()*300);
				 		else page_books = pageoffset;
				 		while(true) {
				 			String url_list_x_page_n= url+"?page="+page_books;		
							org.jsoup.nodes.Document doc_list_X_page_x = Jsoup.connect(url_list_x_page_n).get();
							if(!doc_list_X_page_x.html().toString().contains("bookTitle")) {
								 break;
							} 
					 		System.out.println("page: "+page_books);
						 	Elements url_Books_list_x_page_n=  doc_list_X_page_x.select("a.bookTitle");
						 	System.out.println("Buchliste: "+doc_list_X_page_x.text()+doc_list_X_page_x.attr("href"));
							 	//gehe durch buecher
						 			//fuege alle urls in arrayList
						 			ArrayList<String> urlS = new ArrayList<>();
								 	for(Element book: url_Books_list_x_page_n) {
								 		urlS.add("https://www.goodreads.com"+book.attr("href"));
								 	}
								 	//ubertrage anzThreads buecher von url-liste zu teilmengen-liste
								 	while(urlS.size() > 0) {
							 			ArrayList<String> urlS_teil = new ArrayList<>();
							 			int i;
							 			for(i=0; i<anz_threads; i++) {
							 				if(urlS.size() == 0)break;
							 				urlS_teil.add(urlS.remove(0));
							 			}
//								 			ArrayList<Buch> tmp = buchThreading(urlS_teil,anz_threads);
//								 			System.out.println(tmp.size());
//								 			sortmerge(buecherliste, tmp);
							 			buecherliste.addAll(buchThreading(urlS_teil, 20));
							 			repariere_database();
							 			save_Database();
										System.out.println("Länge Bücherliste: "+buecherliste.size());
								 	}
									
							 page_books++;
				 		}
				 	
			 save_Database();
		 }
	
	 
	 /* Alle Listen: https://www.goodreads.com/list/recently_active_lists 
	  * 	öffne jede Liste 
	  * 		öffne jedes Buch
	  * 			Lese Daten aus
	  */
	 public static void refresh_Database(int books_nr) throws IOException, ClassNotFoundException, InterruptedException {
		 //
		 BufferedReader br = new BufferedReader(new FileReader("./src/source/db"));     
		 if (br.readLine() != null) {
			 load_Database();
		 }
		 int books=0;
		 int page_lists =(int) Math.random()*1000;
		 b: while(true) {
			 String url_allLists= "https://www.goodreads.com/list/recently_active_lists?page=" + page_lists;
			 org.jsoup.nodes.Document doc_List_page_n = Jsoup.connect(url_allLists).get();
			 if(doc_List_page_n.html().toString().contains("No lists yet...")) {
				 System.out.println("Ende"); return;
			 } 
			 	//gehe durch buecherlisten
			 	Elements url_Lists_page_n= doc_List_page_n.select("a.listTitle");
			 	for(Element list: url_Lists_page_n) {
			 		int page_books = 0;
			 		while(true) {
			 			String url_list_x_page_n= "https://www.goodreads.com/"+list.attr("href")+"?page="+page_books;		
						org.jsoup.nodes.Document doc_list_X_page_x = Jsoup.connect(url_list_x_page_n).get();
						if(!doc_list_X_page_x.html().toString().contains("bookTitle")) {
							 break;
						} 
					 	Elements url_Books_list_x_page_n=  doc_list_X_page_x.select("a.bookTitle");
					 	System.out.println("Buchliste: "+list.text()+list.attr("href"));
						 	//gehe durch buecher
						 	for(Element book: url_Books_list_x_page_n) {
						 		System.out.println("   "+books+" "+book.text());books++;
						 		addBookToDatabase("https://www.goodreads.com/"+book.attr("href"));
						 		if(books >= books_nr)break b;
						 		if(books%10==0)save_Database();
						 	}
						 page_books++;
			 		}
			 	}
		 page_lists++;  
		 }
		 save_Database();
	 }
	 
	 private static boolean databaseContains(Buch k) {
		 //suche nach url
		 for(Buch suche: buecherliste) {
			 if(suche.url == k.url)return true;
		 }
		 return false; 
	 }
	 
	 public static boolean addBookToDatabase(Buch b)  {
		 if(!databaseContains(b)) {
			 buecherliste.add(b);
			  System.out.println(b.title+" "+buecherliste.size());
			 return true;
		 }
		 return false;
	 }
	 
	 public static boolean addBookToDatabase(String url) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		 //prüfe ob Buch schon in Database
		 Buch neu = Buch.buchToinfosBuecher("","",url, 10);
		 System.out.println("add: "+url);
		 if(!databaseContains(neu)) {
			 buecherliste.add(neu);
			 return true;
		 }
		 return false;
	 }
	 
	 //TODO: testen
	 public static void buecher_similarBerechnen() throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
		 if(buecherliste.size() == 0)load_Database();
		 repariere_database();
		 for(Buch b: buecherliste) {
			 if(b.similar_Books.size() == 0) {
				System.out.println("ähnliche Bücher für "+b.title+" "+buecherliste.indexOf(b)+"/"+buecherliste.size());
				b.similar_Books =  Buch.buchZuAehnlicheBuecher("", "", b.url, 10);
				if(buecherliste.indexOf(b) % 100 == 0)save_Database();
			 }
		 }
		 save_Database();
	 }
	 
	 public static void fehlendeBücher_ermitteln() throws FileNotFoundException, ClassNotFoundException, IOException {
		 if(buecherliste.size() == 0)load_Database();
		 if(dieBücherFehlen.size() == 0)load_dieBücherFehlen();
		 for(Buch b: buecherliste) {
			 for(String s: b.similar_Books) {
				 if(searchBook_title(s).size() == 0)dieBücherFehlen.add(s);
			 }
		 }
		 save_Database();
		 save_dieBücherFehlen();
	 }
	 
	 
	 //FIXME: prüfe ob bücher in datenbank sonst in fehlt list eintragen
	 public static void datenbank_erweitern_umfehlendeBücher(int anzahl) throws UnsupportedEncodingException, ClassNotFoundException, IOException, InterruptedException {
		 if(buecherliste.size() == 0)load_Database();
		 for(String s: dieBücherFehlen) {
			 if(anzahl == 0)break;
			 buecherliste.add(Buch.buchToinfosBuecher("", "", s, 0));
			 dieBücherFehlen.remove(s);
			 anzahl--;
		 }
		 save_Database();
	 }
	
}
