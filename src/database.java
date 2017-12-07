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

public class database {
	
	static ArrayList<Book> buecherliste = new ArrayList<>();

	 public static void main(String args[]) throws Exception{  
		 refresh_Database(100);
		 //threading();
	 }
	 
	 /**
	  * funktion an der man sich später für threading orientieren kann.
	  * @throws InterruptedException
	  */
	 public static void threading() throws InterruptedException {
		 	ExecutorService executorService = Executors.newFixedThreadPool(5);
		    List<Future<Void>> handles = new ArrayList<Future<Void>>();
		    Future<Void> handle;
		    for (int i=0;i < 2; i++) {
		        handle = executorService.submit(new Callable<Void>() {

		            public Void call() throws Exception {
		                Document d = Jsoup.connect("http://www.google.de").timeout(0).get();
		                System.out.println(d.title());
		                return null;
		            }
		        });
		        handles.add(handle);
		        System.out.println(i);
		    }

		    for (Future<Void> h : handles) {
		        try {
		            h.get();
		        } 
		        catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }

		    executorService.shutdownNow();
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
		 int page_lists =0;
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
						 		addBookToDatabase(book.attr("href"));
						 		//System.out.println(book_y_list_x_page_n)
						 		if(books >= books_nr)break b;
						 	}
					page_books++;
			 		}
			 	}
		 page_lists++;  
		 }
		 save_Database();
	 }
	 
	 private static boolean  databaseContains(Book k) {
		 //suche nach url
		 for(Book suche: buecherliste) {
			 if(suche.url == k.url)return true;
		 }
		 return false; 
	 }
	 
	 public static void addBookToDatabase(String url) throws UnsupportedEncodingException, IOException {
		 //prüfe ob Buch schon in Database
		 Book neu = Book.buchToinfosBuecher("","",url);
		 if(!databaseContains(neu))buecherliste.add(neu);
	 }
}
