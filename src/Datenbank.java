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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.plaf.BorderUIResource.BevelBorderUIResource;
import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Datenbank {
	
	static ArrayList<Buch> buecherliste = new ArrayList<>();
	static ArrayList<Buch> empfehlungsliste = new ArrayList<>();


	 public static void main(String args[]) throws Exception{  
		datenbankErweitern("https://www.goodreads.com/list/show/1.Best_Books_Ever");
		 //test();

	 }
	 
	 private static void test() throws FileNotFoundException, ClassNotFoundException, IOException {
		 //printAllTitles();
		 //printBooklist(searchBook_thema("Humor"));
		 //printBooklist(searchBook_author("Funky Chicken"));
		 printBooklist(Schnitt(searchBook_rating_höher(3), searchBook_thema("Humor")));
				 
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
			while(true) {
				refresh_Database_threading(20, url_liste,-1);
				repariere_database();
				sort_database();
				save_Database(); 
			}
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
		 public Buch call() throws UnsupportedEncodingException, IOException {
			 return Buch.buchToinfosBuecher("", "", url);
		 }
	 }
	 
	 //Kann durch RASA aufgerufen werden: 
	 	//..._LS benutzen die LevensteinDistanz zur Suche (werden immer die Buecher mit dem ähnlichsten String zurueckgeben)
	 
	 
	 	/**
	 	 * füge jedes buch hinzu was mind. einen awards
	 	 * @param awards
	 	 * @return
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
		 public static ArrayList<Buch> searchBook_awards(String awards) throws FileNotFoundException, ClassNotFoundException, IOException {
			 load_Database();
	 		 ArrayList<String> awardliste= new ArrayList<>(Arrays.asList(awards.split("\\s+")));
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String a: awardliste) {
					if(b.awards.contains(a)) {
						results.add(b);
						break;
					}
				}
			 } 
	 		return results;
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
		public static ArrayList<Buch> searchBook_thema(String thema) throws FileNotFoundException, ClassNotFoundException, IOException {
			 load_Database();
	 		 ArrayList<String> themenliste= new ArrayList<>(Arrays.asList(thema.split("\\s+")));
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String a: themenliste) {
					if(b.shelves.contains(a)) {
						results.add(b);
						break;
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
	    public static ArrayList<Buch> searchBook_characters(String character) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<String> charakterliste= new ArrayList<>(Arrays.asList(character.split("\\s+")));
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String a: charakterliste) {
					if(b.Characters.contains(a)) {
						results.add(b);
						break;
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
	 	public static ArrayList<Buch> searchBook_author(String author) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Autor
			 for(Buch b: buecherliste) {
				for(String a: b.Author) {
					if(b.Author.contains(a)) {
						results.add(b);
						break;
					}
				}
			 } 
//	 		 ArrayList<Buch> results = new ArrayList<Buch>();
//	 		 Buch min_b = buecherliste.get(0);
//	 		 double min_distance = Integer.MAX_VALUE;
//
//	 		 for(int i=0; i<buecherliste.size(); i++) {
//				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
//				 for(String author_: buecherliste.get(i).Author) {
//					 if(levenshteinDistance(author_,author) == min_distance) {
//						 if(!results.contains(buecherliste.get(i)))results.add(buecherliste.get(i));
//					 }
//					 if(levenshteinDistance(author_,author) < min_distance) {
//						 min_distance = levenshteinDistance(author_,author);
//						 i=0;
////TODO: folgendes ist denke ich fehlerhaft da dann nicht mehr alle buecher zugefuegt werden die dieses suchwort haben						 
////						 results = new ArrayList<>(); results.add(buecherliste.get(i));
////						 min_b = buecherliste.get(i);
////						 min_distance = levenshteinDistance(thema_,thema);
//					 } 
//				 }
//			 }
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
	 		 load_Database();
	 		 ArrayList<Buch> results = new ArrayList<Buch>();
	 		 //suche nach exaktem Titel
			 for(Buch b: buecherliste) {
				 if(b.title.toLowerCase().contains(title.toLowerCase())) {
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
	
	 	/**
	 	 * 
	 	 * @param empfehlungen
	 	 * @param schnitt
	 	 * @param qualitaet
	 	 * @param inhalt
	 	 * @throws FileNotFoundException
	 	 * @throws ClassNotFoundException
	 	 * @throws IOException
	 	 */
	 	public static void empfehlungsschritt(ArrayList<Buch> empfehlungen, boolean schnitt, String qualitaet, String inhalt) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		load_empfehlungsliste();
	 		if(qualitaet.contains("charakter")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_characters(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_characters(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("title")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_title(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_title(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("author")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_author(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_author(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("thema")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_thema(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_thema(qualitaet));
	 			}
	 		}
	 		save_empfehlungsliste();
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
				if(param.equals("url") && !b.url.equals(""))System.out.println("URL: "+b.url);
				if(param.equals("title") && !b.title.equals(""))System.out.println("Title of the Book: "+b.title); 
				if(param.equals("bücherreihe") && !b.buecherreihe.equals(""))System.out.println("buecherreihe of the Book: "+b.title); 
				if(param.equals("author") && b.Author.size() == 1) System.out.println("Author: "+b.Author.get(0));
				else if (param.equals("author") && b.Author.size() != 1) {System.out.print("Authors: "); for (int i=0; i<b.Author.size()-1; i++) System.out.print(b.Author.get(i)+", "); System.out.println(b.Author.get(b.Author.size()-1));}
				if(param.equals("year") && b.year != Integer.MIN_VALUE)System.out.println("Publihsing year: "+b.year);
//				System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
				if(param.equals("publisher") && !b.publisher.equals(""))System.out.println("Publisher: "+b.publisher);
				if(param.equals("ratin") && b.rating != Integer.MIN_VALUE)System.out.println("Rating: "+b.rating);
				System.out.println("________________________________________________");
				if(param.equals("character") && b.Characters.size() > 0) {
					System.out.println("Characters in the Book: \n");
					for (String x: b.Characters) System.out.println(x);
					System.out.println("________________________________________________");
				}
				if(param.equals("genre") && b.shelves.size() > 0) {
					System.out.println("Genre of the Book: \n");
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
	 
	 public static void save_empfehlungsliste() throws IOException { 
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/el"));
		 oos.writeObject(empfehlungsliste);
		 oos.close();
	 }
	 
	 public static void load_empfehlungsliste() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/el"));
		 empfehlungsliste = (ArrayList<Buch>) ois.readObject(); // cast is needed.
		 System.out.println("laenge: "+empfehlungsliste.size());
		 ois.close();
	 }
	 
	 public static void save_Database() throws IOException, ClassNotFoundException { 
		 System.out.println("sichere Datenbank ... "+buecherliste.size());
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./source/db"));
		 oos.writeObject(buecherliste);
		 oos.close();
	 }
	 
	 public static void load_Database() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./source/db"));
		 buecherliste = (ArrayList<Buch>) ois.readObject(); // cast is needed.
		 System.out.println("load_Database - laenge: "+buecherliste.size());
		 ois.close();
	 }
	 
	 public static void printAllTitles() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("Datenbank geladen");
		 for(Buch b: buecherliste) {
			 System.out.print(b.title);
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
	 
	 public static void merge_and_sort(ArrayList<Buch> neu) throws FileNotFoundException, ClassNotFoundException, IOException {
		 
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
		for(Buch a: buecherliste) {
			if(a == null) {  anz++;continue;}
			if(!contains_duplicates(bl, a.title)) {bl.add(a); anz++;}
		}
		//System.out.println(bl.size()+" / "+buecherliste.size());
		if(anz > 0) {
			buecherliste = new ArrayList<>(bl);
			save_Database();	
		}	
	 }
	 
	 /**
	  * levenshteinDistance ist ein Maß fuer Aehnlichkeit von Strings
	  * @param lhs
	  * @param rhs
	  * @return
	  */
	 public static int levenshteinDistance(String lhs, String rhs) {     
		 	rhs = rhs.toLowerCase(); lhs = lhs.toLowerCase();
		    int len0 = lhs.length() + 1;                                                     
		    int len1 = rhs.length() + 1;                                                     
		                                                                                    
		    // the array of distances                                                       
		    int[] cost = new int[len0];                                                     
		    int[] newcost = new int[len0];                                                  
		                                                                                    
		    // initial cost of skipping prefix in String s0                                 
		    for (int i = 0; i < len0; i++) cost[i] = i;                                     
		                                                                                    
		    // dynamically computing the array of distances                                  
		                                                                                    
		    // transformation cost for each letter in s1                                    
		    for (int j = 1; j < len1; j++) {                                                
		        // initial cost of skipping prefix in String s1                             
		        newcost[0] = j;                                                             
		                                                                                    
		        // transformation cost for each letter in s0                                
		        for(int i = 1; i < len0; i++) {                                             
		            // matching current letters in both strings                             
		            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;             
		                                                                                    
		            // computing cost for each transformation                               
		            int cost_replace = cost[i - 1] + match;                                 
		            int cost_insert  = cost[i] + 1;                                         
		            int cost_delete  = newcost[i - 1] + 1;                                  
		                                                                                    
		            // keep minimum cost                                                    
		            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
		        }                                                                           
		                                                                                    
		        // swap cost/newcost arrays                                                 
		        int[] swap = cost; cost = newcost; newcost = swap;                          
		    }                                                                               
		                                                                                    
		    // the distance is the cost for transforming all letters in both strings       
		    int dif_len = Math.abs(lhs.length() - rhs.length())+1;
		    return cost[len0 - 1] / dif_len ;                                                          
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
				 		if(pageoffset == -1) page_books= (int) (Math.random()*1000);
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
							 			
							 			List<Buch> tmp = buchThreading(urlS_teil,anz_threads);
							 			buecherliste.addAll(tmp);
							 			 save_Database();
							 			 repariere_database();
							 			 sort_database();
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
	 public static void refresh_Database(int books_nr) throws IOException, ClassNotFoundException {
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
	 
	 public static boolean addBookToDatabase(String url) throws UnsupportedEncodingException, IOException {
		 //prüfe ob Buch schon in Database
		 Buch neu = Buch.buchToinfosBuecher("","",url);
		 System.out.println("add: "+url);
		 if(!databaseContains(neu)) {
			 buecherliste.add(neu);
			 return true;
		 }
		 return false;
	 }
}
