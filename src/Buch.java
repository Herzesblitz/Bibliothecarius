import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//BUGLISTE
//TODO: quotes
//TODO: inhaltszusammenfassung => alle fkt. database 
public class Buch implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public int year = Integer.MIN_VALUE;
		public String url="";
		public String sprache = "";
		public String isbn="";
		public String buecherreihe ="";
        public String title="";
        public String publisher="";
        public String covertext="";
        public String blurb=""; //Klappentext
        public double rating=Integer.MIN_VALUE;
        public List<String> awards =  new ArrayList<>();
        public List<String> shelves =  new ArrayList<>();
        public ArrayList<Buch> similar_Books =  new ArrayList<>();
        public List<String> Characters =  new ArrayList<>();
        public List<String> Author =  new ArrayList<>();

    public Buch() {
	}    
        
    public Buch (String title, String author) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
    	setData(title, author);
    }
    
  
  	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		Datenbank.printBook(buchToinfosBuecher("Dante", "","", 10));
		//System.out.println(BuchIDZuURL("Platon"));
		
	}
	
	
	private void setData(String title, String author) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		Buch k = buchToinfosBuecher(title, author,"", 10);
		this.url = k.url;
        this.title = k.title;
        this.isbn = k.isbn;
        this.publisher = k.publisher;
        this.blurb = k.blurb;
        this.rating = k.rating;
        this.awards = k.awards;
        this.shelves= k.shelves;
        //this.similar_Books=k.similar_Books;
        this.Characters=k.Characters;
        this.Author=k.Author;
	}
    
    //wird durch Datenbank aufgerufen
	
	public static void ausgebenBücherliste(ArrayList<Buch> liste) {
			for(Buch b: liste) {ausgebenBuch(b);System.out.println("\n------------------------------------------------------------------------------------------------\n");}
	}
	
	public static void ausgebenBuch(Buch Buchmeta) {
			if(!Buchmeta.url.equals(""))System.out.println("URL: "+Buchmeta.url);
			if(!Buchmeta.title.equals(""))System.out.println("Title of the Book: "+Buchmeta.title); 
			if(Buchmeta.Author.size() == 1) System.out.println("Author: "+Buchmeta.Author.get(0));
			else if(Buchmeta.Author.size() > 1) {System.out.print("Authors: "); for (int i=0; i<Buchmeta.Author.size()-1; i++) System.out.print(Buchmeta.Author.get(i)+", "); System.out.println(Buchmeta.Author.get(Buchmeta.Author.size()-1));}
			if(Buchmeta.year != Integer.MIN_VALUE)System.out.println("Publihsing year: "+Buchmeta.year);
//			System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
			if(!Buchmeta.publisher.equals(""))System.out.println("Publisher: "+Buchmeta.publisher);
			if(Buchmeta.rating != Integer.MIN_VALUE)System.out.println("Rating: "+Buchmeta.rating);
			System.out.println("________________________________________________");
			if(Buchmeta.Characters.size() > 0) {
				System.out.println("Characters in the Book: \n");
				for (String x: Buchmeta.Characters) System.out.println(x);
				System.out.println("________________________________________________");
			}
			if(Buchmeta.shelves.size() > 0) {
				System.out.println("Genre of the Book: \n");
				for (String x: Buchmeta.shelves)	System.out.println(x);
				System.out.println("________________________________________________");
			}
			if(Buchmeta.awards.size() > 0) {
				System.out.println("Awards of the Book: \n");
				for (String x: Buchmeta.awards)	System.out.println(x);
				System.out.println("________________________________________________");
				
			}
			if(Buchmeta.similar_Books != null && Buchmeta.similar_Books.size()>0){
				System.out.println("Ähnliche Bücher: \n");
				for (Buch x: Buchmeta.similar_Books)	System.out.println(x.title);
				System.out.println("________________________________________________");
			}
			if(!Buchmeta.blurb.equals("")) {
				System.out.println("Blurb of the Book: \n");
				System.out.println(Buchmeta.blurb);
			}
			
	}
    
    public static ArrayList<Buch> Schnitt(ArrayList<Buch> a, ArrayList<Buch> b){
    	ArrayList<Buch> results = new ArrayList<>();
    	for(Buch q: a) {
    		if(b.contains(q))results.add(q);
    	}
    	return results;
    }
    
    public static ArrayList<Buch> Vereinigung(ArrayList<Buch> a, ArrayList<Buch> b){
    	ArrayList<Buch> results = new ArrayList<>();
    	for(Buch q: a) {
    		results.add(q);
    	}
    	for(Buch p: b) {
    		if(!results.contains(p))results.add(p);
    	}
    	return results;
    }
    
    public static ArrayList<String> singularisierung(ArrayList<String> a){
    	ArrayList<String> b = new ArrayList<>();
    	for(String s: a) {
    		if(b.contains(s))continue;
    		else b.add(s);
    	}
    	return b;
    }
    
    //TODO: testen
    private static String BuchIDZuURL (String suchterm) throws IOException {
		org.jsoup.nodes.Document doc;
    	String search_1 = "https://www.goodreads.com/search?page=1&query=" + suchterm;
	    doc = Jsoup.connect(search_1).ignoreHttpErrors(true).get();
		if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) return ""; 
		try {return "https://www.goodreads.com"+doc.select("td").select("a.bookTitle").first().attr("href");}
		catch(NullPointerException e){
			return null;
		}
    }

    /**
     * Bestimmt alle Attribute eines Bookobjekt durch Webscrapping
     * @param title
     * @param author
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws InterruptedException 
     * @throws ClassNotFoundException 
     */
    public static Buch buchToinfosBuecher(String title, String author, String url, int anz_ähnliche) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		Buch book = new Buch();
		org.jsoup.nodes.Document doc;
		String linkBuch="";
		
		if(url=="") {
			linkBuch = BuchIDZuURL(title+" "+author);
		}
		else {
			linkBuch = url;
		}
		//link oeffnen und daten lesen
		if(linkBuch == null)return null;
    	doc = Jsoup.connect(linkBuch).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").referrer("http://www.google.com").timeout(20000).get();
 
		//Infos aus doc lesen
    		book.url = linkBuch;
    		
    	
			String publisher = ""; String blurb = ""; double rating = 0;
			
			//prüfe "Edition Language" auf Deutsch (mglw. auch Englisch)
				String sprache = doc.getElementsByClass("infoBoxRowItem").select("[itemprop=inLanguage]").text(); 
				if(!sprache.equals("German") && !sprache.equals("English"))return null;
			//if(doc.select("infoBoxRowItem.inLanguage").first().text() == "German")System.out.println("Deutsch!");
			

			//Suche Title
			title = doc.select("title").text();
					if(title.contains("by"))title = title.substring(0, doc.select("title").text().indexOf("by") - 1);
			
			//ggf. title von buchreihe trennen
			if(title.matches(".+\\(.+\\)")) {
				book.buecherreihe = title.substring(title.lastIndexOf("(")+1, title.lastIndexOf(")")-1);
				title = title.substring(0, title.lastIndexOf("(")-1);
			}

			//Suche blurb
			 blurb=doc.select("div#descriptionContainer").text();
			
			//Suche rating
			 	rating = Double.parseDouble(doc.select("span.average").text());
			 	//rating = Double.parseDouble(doc.html().substring(doc.html().indexOf("ratingValue") + 13, doc.html().indexOf("ratingValue") + 17).toString());
			
			//Suche autor
				if (doc.html().contains("authorName")) {
					Elements elements = doc.select("a.authorName");
					for(Element el: elements)book.Author.add(el.text());
				}

			//Suche Characters
				if (true/*doc.html().contains("/characters/")*/) {
						Elements elements = doc.select("div.clearFloats").select("div.infoBoxRowItem").select("a");
						for(Element el: elements) {
							if(el.parent().html().contains("character"))book.Characters.add(el.text());
						}
				}
				
			//Suche Genres
				Elements elements= doc.select("div.elementList").select("div.left");//select("a.ationLink right bookPageGenreLink__seeMoreLink").toString();
				for(Element el: elements) book.shelves.add(el.text());
			
			
			//aehnlicheBuecher
				buchZuAehnlicheBuecher("","",linkBuch, book, 10);
				//System.out.println("similar size"+book.similar_Books.size());
				
			//ISBN	
				String isbn= doc.select("div.infoBoxRowItem").select("span.greyText").text().toLowerCase();
				if(isbn.contains("isbn13"))book.isbn = isbn.substring(isbn.indexOf(" ")+1,isbn.indexOf(")"));
			
					
					//System.out.println("test");isbn = doc.select("META[property=isbn]").toString();
				
			//publisher
				elements = doc.select("div.row");
				for(Element el: elements) {
					if(el.text().toLowerCase().contains("publish"))publisher = el.text();//.substring(el.text().toLowerCase().indexOf("by")+2, el.text().indexOf(" ", el.text().toLowerCase().indexOf("by")+2));
				}
				
			//awards
				elements = doc.select("div.clearFloats").select("a.award");
				for(Element el: elements) {
					book.awards.add(el.text());
				}
			
			//year
				if(book.publisher.matches(".*\\d+.*")) book.year = Integer.valueOf(book.publisher.replaceAll("\\D+",""));

				System.out.println("Title: "+title);
		book.title = title; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
		return book;
	}
		
	public static void buchZuAehnlicheBuecher(String title, String author, String url, Buch book, int anz_ähnliche) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		ArrayList<String> results = new ArrayList<>();
		System.out.println(book.title);
		int groeße = 10;
		
		//finde link zu buch
		String link_book ="";
		if(url == "") {
			link_book = BuchIDZuURL(title+" "+author);
		}
		else {
			link_book = url;
		}
		
		//prüfe ob url legal
		if(!link_book.matches("https://www.goodreads.com/book/show/[0-9]+(\\.|-)(.)+?from_search=true"))return;

		//öffne similar link
		org.jsoup.nodes.Document doc = Jsoup.connect(link_book).userAgent("bot101").get();
		String similar_link = doc.getElementsMatchingText("Readers Also Enjoyed").attr("href");
		doc = Jsoup.connect(similar_link).userAgent("bot101").get();
	
		//sammle die ersten 10 bücher
		ArrayList<String> urls = new ArrayList<String>();
		ArrayList<String> titles = new ArrayList<String>();

		Elements similars= doc.select("tr").select("td").select("a[title]");
		
		for(Element s: similars) {
			if(groeße == 0)break;
			if(!s.attr("abs:href").contains("show"))continue;
				//System.out.println(s.attr("title"));
			urls.add(s.attr("abs:href"));
			titles.add(s.attr("title"));
			groeße--;
		}		
		//FIXME: prüfe ob bücher in Datenbank (über urls) sind
		for(String URL: urls) {
			Buch result = Datenbank.searchBook_URL(URL);
			if(result != null) {
				book.similar_Books.add(result);
			}
			else {
				Buch b = buchToinfosBuecher("", "", URL, 0);
				if(b == null) continue;
				//System.out.println(b.title);
				book.similar_Books.add(b);
				Thread.sleep(50);
			}
		}
		
		//FIXME //wenn ja füge ähnliche zu similar von Book hinzu
				//wenn nicht rufe infoToBuch für entsprechende Bücher auf
				//- WICHTIG warte 50ms Thread.sleep(50);
				// und füge ähnliche zu similar von Book hinzu
		return;
	}
		
	//testfunktionen
		
		private static String randomBookURL() throws IOException {
			//greife auf liste der aktualisierten buecherlisten zu
				int random_page = ((int) (Math.random()*1000)) % 1000;
				String url_allLists= "https://www.goodreads.com/list/recently_active_lists?page=" + random_page;
				 org.jsoup.nodes.Document doc_List_page_n = Jsoup.connect(url_allLists).get();
				 if(doc_List_page_n.html().toString().contains("No lists yet...")) {
					 System.out.println("Ende"); return"";
				 } 
		 	
			//gehe durch 1. buecherliste 
				Element firstList = doc_List_page_n.select("a.listTitle").first();
		 		String url_list_x_page_n= "https://www.goodreads.com/"+firstList.attr("href")+"?page="+1;		
				
		 		org.jsoup.nodes.Document doc_list_X_page_x = Jsoup.connect(url_list_x_page_n).get();
				if(!doc_list_X_page_x.html().toString().contains("bookTitle")) {
					 return "";
				} 
			 	
				Elements url_Books_list_x_page_n=  doc_list_X_page_x.select("a.bookTitle");
			
			//greife auf 1. ELement zu
				return "https://www.goodreads.com"+url_Books_list_x_page_n.first().attr("href");
		}
		
		private static String randomString() {
				String randomString ="";
				int length = ((int) (Math.random()*1000)) % 20;
				for(int pos=0; pos < length; pos++) {
					 int rnd = (int) (Math.random() * 52); // or use Random or whatever
				    char base = (rnd < 26) ? 'A' : 'a';
				    randomString += (char) (base + rnd % 26);
				}
				return randomString;
		}

}

