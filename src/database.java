import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.org.apache.xpath.internal.axes.WalkingIterator;
import com.sun.webkit.network.URLs;

class MyThread implements Runnable {
	String store;
	Book b = new Book();

	   public MyThread(String s) {
	       // store parameter for later user
		   store = s;
	   }

	   public void run() {
		   try {
			  this.b = Book.buchToinfosBuecher("", "", store);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
	   public Book getBook() {
			  System.out.println(b.title);
		   return b;
	   }
}

public class database {
	
	static ArrayList<Book> buecherliste = new ArrayList<>();

	 public static void main(String args[]) throws Exception{  
		 BufferedReader br = new BufferedReader(new FileReader("./src/source/db"));     
		 if (br.readLine() != null) {
			 load_Database();
		 }
		 refresh_Database_threading(2,2);
		// sort_database();
		// printAllTitles();
		 //Book.printBook(search_database("Harry Potter"));
	 }
	 
	 
	 
	 public static void fuege_n_buecher_hinzu(ArrayList<String> url) {
		 for(String s: url) {
			 Runnable r = new MyThread(s);
			 new Thread(r).start();
			 addBookToDatabase((((MyThread) r).getBook()));
		 }
	 }
	 
	 public static void save_Database() throws IOException { 
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/source/db"));
		 oos.writeObject(buecherliste);
		 oos.close();
	 }
	 
	 public static void load_Database() throws FileNotFoundException, IOException, ClassNotFoundException {
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/source/db"));
		 buecherliste = (ArrayList<Book>) ois.readObject(); // cast is needed.
		 ois.close();
	 }
	 
	 public static void printAllTitles() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("Datenbank geladen");
		 for(Book b: buecherliste) {
			 System.out.println(b.title+" "+b.url);
		 }
		 System.out.println();
	 }
	 
	 public static Book search_database(String title) throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("datenbank geladen ...");
		 Book min_b = buecherliste.get(0);
		 for(Book b: buecherliste) {
			 //System.out.println(b.title+" "+levenshteinDistance(b.title, title));
			 if(levenshteinDistance_modifiziert(b.title,title) < levenshteinDistance_modifiziert(min_b.title,title) ) {
				 min_b = b;
			 }
		 }
		 return min_b;
	 }
	 
	 //TODO: moeglicherweise ineffizient
	 public static void sort_database() throws FileNotFoundException, ClassNotFoundException, IOException {
		 load_Database();
		 System.out.println("datenbank geladen ...");
		// Sorting
		 Collections.sort(buecherliste, new Comparator<Book>() {
		         @Override
		         public int compare(Book a, Book b)
		         {
		             return  a.title.compareTo(b.title);
		         }
		     });
		 save_Database();
		 System.out.println("datenbank geladen ...");
		 printAllTitles();
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
	
	
	 public static void refresh_Database_threading(int books_nr, int anz_threads) throws IOException, ClassNotFoundException {
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
						 			//ArrayList<Book> teilmenge_buecher = new ArrayList<>();
						 			
						 			books+=i+1; 
						 			//starte entsprechend viele threads
						 			fuege_n_buecher_hinzu(urlS_teil);
						 			printAllTitles();
						 			save_Database(); //if(books%10==0)save_Database();						 			
							 			//boolean zugefuegt = addBookToDatabase(+book.attr("href"));
							 			//if(zugefuegt)books++;
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
