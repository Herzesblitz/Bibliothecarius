import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class database {
	
	static ArrayList<Book> buecherliste = new ArrayList<>();

	 public static void main(String args[]) throws Exception{  
		 createDatabase();
	 }
	 
	
	 
	 /* Alle Listen: https://www.goodreads.com/list/recently_active_lists 
	  * 	öffne jede Liste 
	  * 		öffne jedes Buch
	  * 			Lese Daten aus
	  */
	 public static void createDatabase() throws IOException {
		 //
		 int books=0;
		 int page_lists =0;
		 while(true) {
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
					 	//System.out.println("Buchliste: "+list.text()+list.attr("href"));
						 	//gehe durch buecher
						 	for(Element book: url_Books_list_x_page_n) {
						 		String url_book_y_list_x_page_n= "https://www.goodreads.com"+book.attr("href");
						 		System.out.println(books+": "+url_book_y_list_x_page_n);books++;

						 		addBookToDatabase(url_book_y_list_x_page_n);
						 		//System.out.println(book_y_list_x_page_n);
						 	}
					page_books++;
			 		}
			 	}
		 page_lists++;  
		 }
		 
	 }
	 
	 private static boolean  databaseContains(Book k) {
		 //suche nach url
		 for(Book suche: buecherliste) {
			 if(suche.isbn == k.isbn)return true;
		 }
		 return false; 
	 }
	 
	 public static void addBookToDatabase(String url) throws UnsupportedEncodingException, IOException {
		 //prüfe ob Buch schon in Database
		 Book neu = Book.buchToinfosBuecher("", "",url);
		 if(!databaseContains(neu))buecherliste.add(neu);
	 }
}
