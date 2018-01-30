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
	//TODO: manchmal taucht goodreads.com...goodreads.com auf 
	//TODO: falscher blurb bei  printBook(buchToinfosBuecher("Platon's Republic", "Platon", ""));

public class Buch implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public int year = Integer.MIN_VALUE;
		public String url="";
		public String isbn="";
        public String title="";
        public String publisher="";
        public String covertext="";
        public String blurb=""; //Klappentext
        public double rating=Integer.MIN_VALUE;
        public List<String> awards =  new ArrayList<>();
        public List<String> shelves =  new ArrayList<>();
        public List<String> similar_Books =  new ArrayList<>();
        public List<String> Characters =  new ArrayList<>();
        public List<String> Author =  new ArrayList<>();

    public Buch() {
	}    
        
    public Buch (String title, String author) throws UnsupportedEncodingException, IOException {
    	setData(title, author);
    }
    
  
  	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
	}
	
	private void setData(String title, String author) throws UnsupportedEncodingException, IOException {
		Buch k = buchToinfosBuecher(title, author,"");
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
			for(Buch b: liste)ausgebenBuch(b);
	}
	
	public static void ausgebenBuch(Buch Buchmeta) {
			if(!Buchmeta.url.equals(""))System.out.println("URL: "+Buchmeta.url);
			if(!Buchmeta.title.equals(""))System.out.println("Title of the Book: "+Buchmeta.title); 
			if(Buchmeta.Author.size() == 1) System.out.println("Author: "+Buchmeta.Author.get(0));
			else {System.out.print("Authors: "); for (int i=0; i<Buchmeta.Author.size()-1; i++) System.out.print(Buchmeta.Author.get(i)+", "); System.out.println(Buchmeta.Author.get(Buchmeta.Author.size()-1));}
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
    private static String BuchIDZuURL (String title, String author, String url) throws IOException {
		org.jsoup.nodes.Document doc;
    	String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + " "+ author + "&tab=books&utf8=%E2%9C%93";
	    doc = Jsoup.connect(search_1).get();
		if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) return null; 
		return "https://www.goodreads.com"+doc.getElementsByTag("tr").first().html().substring(doc.getElementsByTag("tr").first().html().indexOf("href") + 6, doc.getElementsByTag("tr").first().html().indexOf(">", doc.getElementsByTag("tr").first().html().indexOf("href")) - 1);	
    }

    /**
     * Bestimmt alle Attribute eines Bookobjekt durch Webscrapping
     * @param title
     * @param author
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static Buch buchToinfosBuecher(String title, String author, String url) throws UnsupportedEncodingException, IOException {
		Buch book = new Buch();
		org.jsoup.nodes.Document doc;
		String linkBuch="";
		
		if(url=="") {
			linkBuch = BuchIDZuURL(title, author, url);
		}
		else {
			linkBuch = url;
		}
		//link oeffnen und daten lesen
    	doc = Jsoup.connect(linkBuch).timeout(10000).userAgent("bot101").get();

		//Infos aus doc lesen
    		book.url = linkBuch;
    	
			String publisher = ""; String blurb = ""; double rating = 0;

			//Suche Title
			title = doc.select("title").text();
					if(title.contains("by"))title = title.substring(0, doc.select("title").text().indexOf("by") - 1);

			//Suche blurb
			 blurb=doc.select("div#descriptionContainer").text();
			
			//Suche rating
			 	rating = Double.parseDouble(doc.select("span.average").text());
			 	//rating = Double.parseDouble(doc.html().substring(doc.html().indexOf("ratingValue") + 13, doc.html().indexOf("ratingValue") + 17).toString());
			
			//Suche autor
				if (doc.html().contains("authorName")) {
					Elements elements = doc.select("a.authorName");
					for(Element el: elements)book.addAuthor(el.text());
				}

			//Suche Characters
				if (true/*doc.html().contains("/characters/")*/) {
						Elements elements = doc.select("div.clearFloats").select("div.infoBoxRowItem").select("a");
						for(Element el: elements) {
							if(el.parent().html().contains("character"))book.addCharacter(el.text());
						}
				}
				
			//Suche Genres
				Elements elements= doc.select("div.elementList").select("div.left");//select("a.ationLink right bookPageGenreLink__seeMoreLink").toString();
				for(Element el: elements) book.addGenre(el.text());
			
			
			//aehnlicheBuecher
				buchZuAehnlicheBuecher(title,author,"");
				
			//ISBN	
				String isbn= doc.select("div.infoBoxRowItem").select("span.greyText").text().toLowerCase();
				if(isbn.contains("isbn13"))book.setISBN(isbn.substring(isbn.indexOf(" ")+1,isbn.indexOf(")")));
				
				//TODO: ISBN wird zB. bei the republic (plato) nicht gelesen: for(Element el: doc.select("span").select("span"))System.out.println(el.text());
					
					//System.out.println("test");isbn = doc.select("META[property=isbn]").toString();
				
			//publisher
				elements = doc.select("div.row");
				for(Element el: elements) {
					if(el.text().toLowerCase().contains("publish"))publisher = el.text();//.substring(el.text().toLowerCase().indexOf("by")+2, el.text().indexOf(" ", el.text().toLowerCase().indexOf("by")+2));
				}
				
			//awards
				elements = doc.select("div.clearFloats").select("a.award");
				for(Element el: elements) {
					book.addAwards(el.text());
				}
			
			//year
				if(book.publisher.matches(".*\\d+.*")) book.year = Integer.valueOf(book.publisher.replaceAll("\\D+",""));

		book.title = title; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
		return book;
	}

    	
		
		public static ArrayList<String> buchZuAehnlicheBuecher(String title, String author, String url) throws UnsupportedEncodingException, IOException {
		ArrayList<String> results = new ArrayList<>();
		

		//finde link zu buch
		String link_book ="";
		if(url == "") {
			link_book = BuchIDZuURL(title, author, "");
		}
		else {
			link_book = url;
		}
		System.out.println(link_book);

		org.jsoup.nodes.Document doc = Jsoup.connect(link_book).userAgent("bot101").get();

		//TODO: immer noch nicht perfekt, aber besser als vorher, da keine substringsuche
		String similar_link = "https://www.goodreads.com/book/similar/";
		for(Element e:doc.select("div").select("h2.brownBackground").select("a")) {
			if(e.attr("href").contains("work")) { //contains("work") ist kritisch
				similar_link += e.attr("href").replaceAll("[^0-9]+", "");
				break;
			}
			//ignoreHttpErrors(true) wird Probleme wie Status 404 loesen (wird ggf. durch Bot verursacht
			doc = Jsoup.connect(similar_link).ignoreHttpErrors(true).get();

			//liste der Ã¤hnlichen buecher sammeln
			org.jsoup.select.Elements results_doc = doc.select("div").select("a");
			for (Element result_doc : results_doc) {
				String link = result_doc.attr("href").replaceAll(" ", "");
				if(link.contains("/book/show/") && !results.contains(link))results.add("https://www.goodreads.com"+link);
			}
		}
		return singularisierung(results);
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

    //Setter, adder etc.
						public void setPublisher(String publisher) {
							this.publisher = publisher;
						}
		
						public void setISBN(String isbn) {
							this.isbn = isbn;
						}
			
					    public void setAuthor(List<String> autor) {
					        Author.addAll(autor) ;
					    }
					
					    public void addAuthor(String autor) {
					        if (!Author.contains(autor)) Author.add(autor);
					    }
					    
					    public void removeAuthor(String autor){
					        if(Author.contains(autor))  Author.remove(autor);
					    }
					
					    public void setGenre(List<String> genre) {
					    	shelves.addAll(genre) ;
					    }
					
					    public void addGenre(String genre) {
					        if (!shelves.contains(genre)) shelves.add(genre);
					    }
					    
					    public void removeGenre(String genre){
					        if(shelves.contains(genre))   shelves.remove(genre);
					    }
					
					    public void setSimilar_Books(List<String> similarBuch) {
					        similar_Books.addAll(similarBuch) ;
					    }
				
					    public void addAehnlichBuch(String bookname) {
					        if (!similar_Books.contains(bookname)) similar_Books.add(bookname);
					    }
					    
					    public void removeAehnlichBuch(String bookname){
					        if(shelves.contains(bookname)) shelves.remove(bookname);
					    }
					    
					    public void setAwards(List<String> Awards) {
					        awards.addAll(Awards) ;
					    }
					
					    public void addAwards(String award) {
					        if (!awards.contains(award))  awards.add(award);
					    }	
					
					    public void setCharacters(List<String> Characters) {
					        similar_Books.addAll(Characters) ;
					    }
					
					    public void addCharacter(String Character) {
					        if (!Characters.contains(Character))  Characters.add(Character);
					    }
					    
					    public void removeCharacter(String Character){
					        if(shelves.contains(Characters)) shelves.remove(Characters);
					    }
}

