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
		 refresh_Database_threading(1000, 50);
		 remove_double_database();
		 sort_database();
		 printAllTitles();
		 save_Database();
		 //Book.printBook(search_database("Harry Potter"));
	 }
	 
	 private static void test() {
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
	 	
	 	 public static ArrayList<Book> searchBook_characters(String character) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 Book min_b = buecherliste.get(0);
	 		 double min_distance = Integer.MAX_VALUE;
			 for(Book b: buecherliste) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 for(String character_: b.Characters) {
					 if(levenshteinDistance_modifiziert(character_,character) == min_distance) {
						 if(!results.contains(b))results.add(b);
					 }
					 if(levenshteinDistance_modifiziert(character_,character) < min_distance) {
						 results = new ArrayList<>(); results.add(b);
						 min_b = b;
						 min_distance = levenshteinDistance_modifiziert(character_,character);
					 } 
				 }
			 }
	 		 return results;
	 	 }
	 	 
	 	public static ArrayList<Book> searchBook_author(String author) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		 load_Database();
	 		 ArrayList<Book> results = new ArrayList<Book>();
	 		 Book min_b = buecherliste.get(0);
	 		 for(Book b: buecherliste) {
				 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
				 if(levenshteinDistance_modifiziert(b.author,author) < levenshteinDistance_modifiziert(min_b.author,author) ) {
					 results = new ArrayList<>(); results.add(b);
					 min_b = b;
				 }
				 if(levenshteinDistance_modifiziert(b.author,author) == levenshteinDistance_modifiziert(min_b.author,author) ) {
					 results.add(b);
				 }
			 }
	 		 return results;
	 	}
	 	
	 	public static ArrayList<Book> searchBook_title(String title) throws FileNotFoundException, ClassNotFoundException, IOException {
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
	 	
	 	public static void searchBook(String title, String author, String ISBN) throws FileNotFoundException, ClassNotFoundException, IOException {
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
					 for(Book b: buecherliste) {
						 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
						 if((levenshteinDistance_modifiziert(b.author,author) + levenshteinDistance_modifiziert(b.title,title)) < (levenshteinDistance_modifiziert(min_b.author,author) + levenshteinDistance_modifiziert(min_b.title,title))) {
							 min_b = b;
						 }
					 } 
				 }
			 }
	 	 }
	
	 	 public static void empfehlungsschritt(ArrayList<Book> empfehlungen, boolean schnitt, String qualitaet, String inhalt) throws FileNotFoundException, ClassNotFoundException, IOException {
	 		load_empfehlungsliste();
	 		if(qualitaet.contains("charakter")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, Book.characterZuBuecherliste(inhalt));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, Book.characterZuBuecherliste(inhalt));
	 			}
	 		}
	 		if(qualitaet.contains("title")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, Book.titleZuBuecherliste(qualitaet, 1000));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, Book.titleZuBuecherliste(qualitaet, 1000));
	 			}
	 		}
	 		if(qualitaet.contains("author")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, Book.autorZuBuecherliste(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, Book.autorZuBuecherliste(qualitaet));
	 			}
	 		}
	 		if(qualitaet.contains("thema")) {
	 			if(schnitt) {
	 				empfehlungsliste = Schnitt(empfehlungsliste, Book.themaZuBuecherliste(qualitaet));
	 			}
	 			else {
	 				empfehlungsliste = Vereinigung(empfehlungsliste, Book.themaZuBuecherliste(qualitaet));
	 			}
	 		}
	 		save_empfehlungsliste();
	 	 }
	 	 
	 	 public static void printBook(Book b) {
	 		 Book.printBook(b);
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
