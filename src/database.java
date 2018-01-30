import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class database {
	
	static ArrayList<Book> buecherliste = new ArrayList<>();
	static ArrayList<Book> empfehlungsliste = new ArrayList<>();


	public static void main(String args[]) throws Exception{
		test();
		 //Book.printBook(search_database("Harry Potter"));
	 }
	 
	 private static void refreshDB() throws ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		 refresh_Database_threading(1000, 20);
		 remove_double_database();
		 sort_database();
		 printAllTitles();
		 save_Database();
	 }
	 
	 private static void test() throws FileNotFoundException, ClassNotFoundException, IOException {
		 sort_database();
		 printBooklist(searchBook_title_LS("Blessed are the dead"));
	 }
	 
	 public static class BookCallable implements Callable {
		 private String url;
		 public BookCallable(String url) throws UnsupportedEncodingException, IOException {
			 this.url = url; 
		 }
		 public Book call() throws UnsupportedEncodingException, IOException {
			 return Book.buchToinfosBuecher("", "", url);
		 }
	 }
	 
	 //Kann durch RASA aufgerufen werden: 
	 	//..._LS benutzen die LevensteinDistanz zur Suche (werden immer die Buecher mit dem ähnlichsten String zurueckgeben)
	 
	 	
	 
	    /**
	  	* sucht nach ALLEN Buechern die dieses Thema haben
	    * @param character
	    * @return
	 	* @throws FileNotFoundException
	 	* @throws ClassNotFoundException
	    * @throws IOException
	 	*/
		public static ArrayList<Book> searchBook_thema_LS(String thema) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 Book min_b = buecherliste.get(0);
	 		 double min_distance = Integer.MAX_VALUE;
			 for(int i=0; i<buecherliste.size(); i++) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 for(String thema_: buecherliste.get(i).shelves) {
					 if(levenshteinDistance_modifiziert(thema_,thema) == min_distance) {
						 if(!results.contains(buecherliste.get(i)))results.add(buecherliste.get(i));
					 }
					 if(levenshteinDistance_modifiziert(thema_,thema) < min_distance) {
						 min_distance = levenshteinDistance_modifiziert(thema_,thema);
						 i=0;
//TODO: folgendes ist denke ich fehlerhaft da dann nicht mehr alle buecher zugefuegt werden die dieses suchwort haben						 
//						 results = new ArrayList<>(); results.add(buecherliste.get(i));
//						 min_b = buecherliste.get(i);
//						 min_distance = levenshteinDistance_modifiziert(thema_,thema);
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
	    public static ArrayList<Book> searchBook_characters_LS(String character) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 double min_distance = Integer.MAX_VALUE;
	 		 for(int i=0; i<buecherliste.size(); i++) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 for(String character_: buecherliste.get(i).Characters) {
					 if(levenshteinDistance_modifiziert(character_,character) == min_distance) {
						 if(!results.contains(buecherliste.get(i)))results.add(buecherliste.get(i));
					 }
					 if(levenshteinDistance_modifiziert(character_,character) < min_distance) {
						 min_distance = levenshteinDistance_modifiziert(character_,character);
						 i=0;
//TODO: folgendes ist denke ich fehlerhaft da dann nicht mehr alle buecher zugefuegt werden die dieses suchwort haben						 
//						 results = new ArrayList<>(); results.add(buecherliste.get(i));
//						 min_b = buecherliste.get(i);
//						 min_distance = levenshteinDistance_modifiziert(thema_,thema);
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
	 	public static ArrayList<Book> searchBook_author_LS(String author) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 Book min_b = buecherliste.get(0);
	 		 double min_distance = Integer.MAX_VALUE;

	 		 for(int i=0; i<buecherliste.size(); i++) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 for(String author_: buecherliste.get(i).Author) {
					 if(levenshteinDistance_modifiziert(author_,author) == min_distance) {
						 if(!results.contains(buecherliste.get(i)))results.add(buecherliste.get(i));
					 }
					 if(levenshteinDistance_modifiziert(author_,author) < min_distance) {
						 min_distance = levenshteinDistance_modifiziert(author_,author);
						 i=0;
//TODO: folgendes ist denke ich fehlerhaft da dann nicht mehr alle buecher zugefuegt werden die dieses suchwort haben						 
//						 results = new ArrayList<>(); results.add(buecherliste.get(i));
//						 min_b = buecherliste.get(i);
//						 min_distance = levenshteinDistance_modifiziert(thema_,thema);
					 } 
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
	 	//TODO: gibt unsinnige titel aus bsp: Blessed be the dead
	 	public static ArrayList<Book> searchBook_title_LS(String title) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 Book min_b = buecherliste.get(0);
			 for(Book b: buecherliste) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 if(levenshteinDistance_modifiziert(b.title,title) < levenshteinDistance_modifiziert(min_b.title,title) ) {
					 results = new ArrayList<>(); results.add(b);
					 min_b = b;
				 }
				 if(levenshteinDistance_modifiziert(b.title,title) == levenshteinDistance_modifiziert(min_b.title,title) ) {
					 results.add(b);
				 }
			 }
	 		 return results;
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
	 	public static void searchBook_LS(String title, String author, String ISBN) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
			 System.out.println("datenbank geladen ...");
			 if(ISBN!="") {
				 Book min_b = buecherliste.get(0);
				 for(Book b: buecherliste) {
					 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
					 if(levenshteinDistance_modifiziert(b.isbn,ISBN) < levenshteinDistance_modifiziert(min_b.isbn,ISBN) ) {
						 min_b = b;
					 }
				 } 
			 }
			 else {
				 if(ISBN!="") {
					 Book min_b = buecherliste.get(0);
					 double min_distance = Integer.MAX_VALUE;
					 for(Book b: buecherliste) {
						 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
						 for(String author_: b.Author) {
							 if((levenshteinDistance_modifiziert(author_,author) + levenshteinDistance_modifiziert(b.title,title)) < min_distance) {
								 min_distance = (levenshteinDistance_modifiziert(author_,author) + levenshteinDistance_modifiziert(b.title,title));
								 min_b = b;
							 } 
						 }
						 
					 } 
				 }
			 }
	 	 }
	

	 	public static void empfehlungsschritt_LS(ArrayList<Book> empfehlungen, boolean schnitt, String qualitaet, String inhalt) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		load_empfehlungsliste();
	 		if(qualitaet.contains("charakter")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_characters_LS(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_characters_LS(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("title")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_title_LS(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_title_LS(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("author")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_author_LS(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_author_LS(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("thema")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, searchBook_thema_LS(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, searchBook_thema_LS(qualitaet));
	 			}
	 		}
	 		save_empfehlungsliste();
	 	 }
	 	 
	 	public static void printBook(Book b) {
	 		 Book.printBook(b);
	 	}
	 	
	 	/**
	 	 * richtigen Parameter zur Auswahl benutzen
	 	 * @param b
	 	 * @param param
	 	 */
	 	public static void printBook_param(Book b, String param) {
				if(param.equals("url") && !b.url.equals(""))System.out.println("URL: "+b.url);
				if(param.equals("title") && !b.title.equals(""))System.out.println("Title of the Book: "+b.title); 
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
	 	 
	 	public static void printBooklist(ArrayList<Book> b) {
	 		 Book.printListofBooks(b);
	 	 }
	 
		public static ArrayList<Book> Schnitt(ArrayList<Book> a, ArrayList<Book> b){
		    	ArrayList<Book> results = new ArrayList<>();
		    	for(Book q: a) {
		    		if(b.contains(q))results.add(q);
		    	}
		    	return results;
	 	 }
	 
	 	public static ArrayList<Book> Vereinigung(ArrayList<Book> a, ArrayList<Book> b){
		 	ArrayList<Book> results = new ArrayList<>();
		 	for(Book q: a) {
		 		results.add(q);
		 	}
		 	for(Book p: b) {
		 		if(!results.contains(p))results.add(p);
		 	}
		 	return results;
	 	}
	 
	 	public static ArrayList<String> removeDoubles(ArrayList<String> a){
		 	ArrayList<String> b = new ArrayList<>();
		 	for(String s: a) {
		 		if(b.contains(s))continue;
		 		else b.add(s);
		 	}
		 	return b;
	 	}  

	 
	 public static ArrayList<Book> thread_try2(ArrayList<String> urls, int threads) throws InterruptedException, ExecutionException, UnsupportedEncodingException, IOException {
		    ExecutorService pool = Executors.newFixedThreadPool(threads);
		    Set<Future<Book>> set = new HashSet<Future<Book>>();    	    
		    for (String url: urls) {
		    	Callable<Book> b = new BookCallable(url);
		        Future<Book> future = pool.submit(b);
		        set.add(future);
		    }
		    ArrayList<Book> liste = new ArrayList<>();
		    for (Future<Book> future : set) {
		    	liste.add(future.get());
		    }
		    pool.shutdownNow();
		    for(Book b: liste)System.out.println(b.title);
		    return liste;
	 }
	 
	 public static org.jsoup.nodes.Document thread_connect(String url) {
		    org.jsoup.nodes.Document doc = null;
		    try {
		        doc = Jsoup.connect(url).timeout(0).get();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		    return doc;
		}

	 static void save_empfehlungsliste() throws IOException { 
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/el"));
		 oos.writeObject(empfehlungsliste);
		 oos.close();
	 }
	 
	 public static void load_empfehlungsliste() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/el"));
		 empfehlungsliste = (ArrayList<Book>) ois.readObject(); // cast is needed.
		 System.out.println("laenge: "+empfehlungsliste.size());
		 ois.close();
	 }
	 
	 public static void save_Database() throws IOException { 
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/db"));
		 oos.writeObject(buecherliste);
		 oos.close();
	 }
	 
	 public static void load_Database() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/db"));
		 buecherliste = (ArrayList<Book>) ois.readObject(); // cast is needed.
		 System.out.println("laenge: "+buecherliste.size());
		 ois.close();
	 }
	 
	 public static void printAllTitles() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("Datenbank geladen");
		 for(Book b: buecherliste) {
			 System.out.println(b.title);
		 }
		 System.out.println(buecherliste.size());
	 }
	 	 
	 //TODO: moeglicherweise ineffizient
	 public static void sort_database() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		// Sorting
		 Collections.sort(buecherliste, new Comparator<Book>() {
		         @Override
		         public int compare(Book a, Book b)
		         {
		             return  a.title.compareTo(b.title);
		         }
		     });
		 save_Database();
	 }
	 
	 private static boolean contains_duplicates(ArrayList<Book> al, String title) {
		 int anz=0;
		 for(Book b: al) {
			 if(b.title.equals(title))anz++;
		 }
		 if(anz == 0)return false;
		 else return true;
	 }
	 
	 public static void remove_double_database() throws FileNotFoundException, ClassNotFoundException, IOException {
		load_Database();
	    ArrayList<Book> bl = new ArrayList<Book>();
		for(Book a: buecherliste) {
			if(!contains_duplicates(bl, a.title))bl.add(a);
		}
		//System.out.println(bl.size()+" / "+buecherliste.size());
		buecherliste = new ArrayList<>(bl);
		save_Database();
	 }
	 
	 /**
	  * levenshteinDistance ist ein Maß fuer Aehnlichkeit von Strings
	  * @param lhs
	  * @param rhs
	  * @return
	  */
	 public static int levenshteinDistance_modifiziert (String lhs, String rhs) {     
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
		    return cost[len0 - 1] / dif_len*10 ;                                                          
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
				 		System.out.println("page: "+page_books);
					 	Elements url_Books_list_x_page_n=  doc_list_X_page_x.select("a.bookTitle");
					 	System.out.println("Buchliste: "+list.text()+list.attr("href"));
						 	//gehe durch buecher
					 			//fuege alle urls in arrayList
					 			ArrayList<String> urlS = new ArrayList<>();
							 	for(Element book: url_Books_list_x_page_n) {
							 		//System.out.println("   "+books+" "+book.text()+" "+book.attr("href"));
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
						 			//starte entsprechend viele threads
						 			buecherliste.addAll(thread_try2(urlS_teil,anz_threads));
						 			save_Database(); 
							 		if(books >= books_nr)break b;
							 	}
						 page_books++;
			 		}
			 	}
		 page_lists++;  
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
	 
	 private static boolean databaseContains(Book k) {
		 //suche nach url
		 for(Book suche: buecherliste) {
			 if(suche.url == k.url)return true;
		 }
		 return false; 
	 }
	 
	 public static boolean addBookToDatabase(Book b)  {
		 if(!databaseContains(b)) {
			 buecherliste.add(b);
			  System.out.println(b.title+" "+buecherliste.size());
			 return true;
		 }
		 return false;
	 }
	 
	 public static boolean addBookToDatabase(String url) throws UnsupportedEncodingException, IOException {
		 //prüfe ob Buch schon in Database
		 Book neu = Book.buchToinfosBuecher("","",url);
		 System.out.println("add: "+url);
		 if(!databaseContains(neu)) {
			 buecherliste.add(neu);
			 return true;
		 }
		 return false;
	 }
}
