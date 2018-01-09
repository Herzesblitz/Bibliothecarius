import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Book implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public String url="";
		public String isbn="";
        public String title="";
        public String author="";
        public String publisher="";
        public String covertext="";
        public String blurb=""; //Klappentext
        public double rating=0;
        public List<String> awards =  new ArrayList<>();
        public List<String> shelves =  new ArrayList<>();
        public List<String> similar_Books =  new ArrayList<>();
        public List<String> Characters =  new ArrayList<>();
        public List<String> Author =  new ArrayList<>();

    public Book() {
	}    
        
    public Book (String title, String author) throws UnsupportedEncodingException, IOException {
    	setData(title, author);
    }
    
  //TODO: funktionen javadoc ergaenzen
  	private static void test() throws UnsupportedEncodingException, IOException {
  		System.out.println(randomString());
  		
//  	for(int i=0; i<1000; i++) {
//			String randomBook = randomBookURL();
//			ArrayList<String> b= buchZuAehnlicheBuecher("", "", randomBook);
//			for(String a: b)System.out.println("   "+a);
//		}
  
  	//Name(Buch) -> Autor, aehnlicheBücher, Charaktere, Genre
//  		ArrayList<String> b= buchZuAehnlicheBuecher("Alexander", "");
//  			for(String a: b)System.out.println(a);
  		//printBook(buchToinfosBuecher("The Name of the Wind (The Kingkiller Chronicle, #1)","",""));
  			//a=  aehnlicheBuecher(testAehnlicheBuecher,"Metro 2033","");
  			//infosBuecher(testInfoBuch,"Metro 2033");
  		
  		for(int i=0; i<1000; i++) {
  			ArrayList<String> a= new ArrayList<>();
  	  		a = characterZuBuecherliste(randomString());
//  	  		a = themaZuBuecherliste("Horror");
//  	  		a = autorZuBuecherliste("Tolkien");
//  	  		a = titleZuBuecherliste("Metro 2033", 5);
  	  		for(String s: a)System.out.println(s);
  		}
  		
  		
  		
  		//Hilfsfunktionen
  			//fetchCharacterList();

  	}
  	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		test();
	}
	
	private void setData(String title, String author) throws UnsupportedEncodingException, IOException {
		Book k = buchToinfosBuecher(title, author,"");
		this.url = k.url;
        this.title = k.title;
        this.isbn = k.isbn;
        this.author = k.author;
        this.publisher = k.publisher;
        this.blurb = k.blurb;
        this.rating = k.rating;
        this.awards = k.awards;
        this.shelves= k.shelves;
        //this.similar_Books=k.similar_Books;
        this.Characters=k.Characters;
        this.Author=k.Author;
	}
    
    public static void printBook(Book Buchmeta) {
    	System.out.println("URL: "+Buchmeta.url);
		System.out.println("Title of the Book: "+Buchmeta.title);    	
		System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
		System.out.println("Publisher: "+Buchmeta.publisher);
		System.out.println("Rating: "+Buchmeta.rating);
		System.out.println("________________________________________________");
		System.out.println("Author(s) of the Book: \n");
		for (String x: Buchmeta.Author)System.out.println(x);
		System.out.println("________________________________________________");
		System.out.println("Similar Books: \n");
			for (String x: Buchmeta.similar_Books)	System.out.println(x);
			System.out.println("________________________________________________");
			System.out.println("Characters in the Book: \n");
		for (String x: Buchmeta.Characters) System.out.println(x);
		System.out.println("________________________________________________");
		System.out.println("Genre of the Book: \n");
		for (String x: Buchmeta.shelves)	System.out.println(x);
		System.out.println("________________________________________________");
		System.out.println("Awards of the Book: \n");
		for (String x: Buchmeta.awards)	System.out.println(x);
		System.out.println("________________________________________________");
		System.out.println("Blurb of the Book: \n");
		System.out.println(Buchmeta.blurb);
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
    
    //TODO: testen
    private static String BookIDToLink (String title, String author, String url) throws IOException {
		org.jsoup.nodes.Document doc;
    	String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + " "+ author + "&tab=books&utf8=%E2%9C%93";
	    doc = Jsoup.connect(search_1).get();
		if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) return null; 
		return "https://www.goodreads.com"+doc.getElementsByTag("tr").first().html().substring(doc.getElementsByTag("tr").first().html().indexOf("href") + 6, doc.getElementsByTag("tr").first().html().indexOf(">", doc.getElementsByTag("tr").first().html().indexOf("href")) - 1);	
    }

    public static Book buchToinfosBuecher(String title, String author, String url) throws UnsupportedEncodingException, IOException {
		Book book = new Book();
		org.jsoup.nodes.Document doc;
		String linkBuch="";
		
		if(url=="") {
			BookIDToLink(title, author, url);
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

		book.title = title; book.author = author; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
		return book;
	}

    
    //Funktionentyp: Daten -> Buecherliste
    
		public static ArrayList<String> autorZuBuecherliste(String autor) throws UnsupportedEncodingException, IOException {
			ArrayList<String> results = new ArrayList<>();
			//link fuer autor finden
				String search_1 = "https://www.goodreads.com/search?page=1&query=" + autor + "&tab=books&utf8=%E2%9C%93";
				org.jsoup.nodes.Document doc = Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
			//TODO: funzt whrsch nicht immer
				String linkAutor = doc.html().substring(doc.html().indexOf("www.goodreads.com/author/show/") + 30, doc.html().indexOf("\"", doc.html().indexOf("/www.goodreads.com/author/show/")));
				doc = Jsoup.connect("https://www.goodreads.com/author/list/" + linkAutor).userAgent("bot101").get();
				org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
	
			String link = "";
			for (Element result_doc : results_doc) {
				if (!result_doc.text().contains("–")) break; //TODO: dirty fix
				//TODO: link-teil furchtbar, auswahl Ã¼ber html string. gibt es mÃ¶glichtkeit das <href>-tag des <title> tag auszuwÃ¤hlen??
				if (result_doc.html().contains("href"))
					link = "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1);
				results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("–") + 1) + link);
			}
			return results;
		}
	
		public static ArrayList<String> characterZuBuecherliste(String character) throws IOException{
			//link für charakter finden
				String link = "";
				ArrayList<String> ret = new ArrayList<>();
				String content = new Scanner(new File("./Bibliothecarius/src/sourceCharacterslist/Characters.txt").getAbsolutePath()).useDelimiter("\\Z").next();
				if(content.contains(character))link = content.substring(content.indexOf("URL:", content.indexOf(character))+5, content.indexOf("\n",content.indexOf(character)));
					//link = content.substring(content.indexOf("URL:", content.indexOf(character)),content.indexOf("\n", content.indexOf(content.indexOf("URL:", content.indexOf(character)))));
				if(link.length()==0) {
					System.err.println("charakter nicht gefunden!");
					return null;
				}
				System.out.println(character+" "+link);
			
			//link öffnen
				ArrayList<String> authors = new ArrayList<>();
				ArrayList<String> titles = new ArrayList<>();
	
				Elements elements_titles = Jsoup.connect(link).get().select("a.bookTitle");
				for(Element el: elements_titles) titles.add(el.text());
	
				Elements elements_authors = Jsoup.connect(link).get().select("a.authorName");
				for(Element el: elements_authors) authors.add(el.text());
						
				if(authors.size() != titles.size())System.err.println("PECH!! authorlist andere laenge als title liste");
				for(int i=0; i<authors.size(); i++) ret.add("title: "+titles.get(i)+" author: "+authors.get(i));				
			return ret;
		}
		
		public static ArrayList<String> themaZuBuecherliste(String thema) throws UnsupportedEncodingException, IOException {
			ArrayList<String> title = new ArrayList<>();
			ArrayList<String> author = new ArrayList<>();
	        String search_1 = "https://www.goodreads.com/shelf/show/" + thema;
			ArrayList<String> liste = new ArrayList<>();
	
			org.jsoup.nodes.Document doc = Jsoup.connect(search_1).userAgent("usrdasf").get();
			//booktitles holen
				org.jsoup.select.Elements results_titles = doc.select("a").select(".bookTitle");
				for (Element result_title : results_titles) {
					//TODO: link-teil furchtbar, auswahl Ã¼ber html string. gibt es mÃ¶glichtkeit das <href>-tag des <title> tag auszuwÃ¤hlen??
					title.add(result_title.text());
				}
			
			//autoren holen
				org.jsoup.select.Elements results_authors = doc.select("a").select(".authorName");
				for (Element result_author : results_authors) {
					//TODO: link-teil furchtbar, auswahl Ã¼ber html string. gibt es mÃ¶glichtkeit das <href>-tag des <title> tag auszuwÃ¤hlen??
					author.add(result_author.text());
				}
	
			//merge titles und autoren
			String title_raw;
			for (int i = 0; i < title.size(); i++) {
				title_raw = title.get(i).toString().substring(0, title.get(i).lastIndexOf("("));
				liste.add(title_raw+" BY "+author.get(i).toString());
			}
	
			return liste;
		}
	
		public static ArrayList<String> titleZuBuecherliste(String search, int maxresultpages) throws UnsupportedEncodingException, IOException {
			String search_2 = "https://www.goodreads.com/search?page=1&query=" + search + "&tab=books&utf8=%E2%9C%93";
	
			//find number of result pages
				org.jsoup.nodes.Document doc = Jsoup.connect(search_2).userAgent("bot101").get();
				String title_website = "";
				for (Element meta : doc.select("meta"))
					if (meta.attr("name").contains("title"))
						title_website = meta.attr("content"); //search for meta attr that contains title
				if (title_website.length() > 0 && title_website.contains("showing") && title_website.contains("of"))
					title_website = title_website.substring(title_website.indexOf("showing") + 10, title_website.indexOf("of", title_website.indexOf("showing")) - 1);
				int pages_number = Math.min(maxresultpages, Integer.parseInt(title_website));
	
			//go through result pages
				ArrayList<String> results = new ArrayList<>();
				
				for (int page = 1; page < pages_number; page++) {
					String url = "https://www.goodreads.com/search?page=" + page + "&query=" + search + "&tab=books&utf8=%E2%9C%93";
					doc = Jsoup.connect(url).userAgent("usrdasf").get();
					
		    //get titles and authors 
					ArrayList<String> titles = new ArrayList<>();
					ArrayList<String> authors = new ArrayList<>();
		
					
					//booktitles holen
					org.jsoup.select.Elements results_titles = doc.getElementsByTag("tr").select("td").select("a.bookTitle");
					for (Element result_title : results_titles) {
						titles.add(result_title.text());
					}
				
					//autoren holen
					org.jsoup.select.Elements results_authors = doc.getElementsByTag("tr").select("td").select("a.authorName");
					for (Element result_author : results_authors) {
						//TODO: link-teil furchtbar, auswahl Ã¼ber html string. gibt es mÃ¶glichtkeit das <href>-tag des <title> tag auszuwÃ¤hlen??
						authors.add(result_author.text());
					}
	
					//merge titles und autoren
					for (int i = 0; i < titles.size(); i++) {
						results.add(titles.get(i)+" BY "+authors.get(i));
					}
					
					//for(String r: results)System.out.println(r);
				}
				
			return results;
		}
	
		public static ArrayList<String> buchZuAehnlicheBuecher(String title, String author, String url) throws UnsupportedEncodingException, IOException {
		ArrayList<String> results = new ArrayList<>();
		
		//finde link zu buch
		String link_book ="";
		if(url == "") {
			link_book = BookIDToLink(title, author, "");
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
			}
			
		//similar aufrufen 
			//ignoreHttpErrors(true) wird Probleme wie Status 404 loesen (wird ggf. durch Bot verursacht)
			doc = Jsoup.connect(similar_link).ignoreHttpErrors(true).get();

		//liste der Ã¤hnlichen buecher sammeln
			org.jsoup.select.Elements results_doc = doc.select("div").select("a");
			for (Element result_doc : results_doc) {
				String link = result_doc.attr("href").replaceAll(" ", "");
				if(link.contains("/book/show/") && !results.contains(link))results.add("https://www.goodreads.com"+link);
			}
		return removeDoubles(results);
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

