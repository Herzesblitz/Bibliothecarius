import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Book{
        public String title;
        public String author;
        public String publisher;
        public String covertext;
        public String blurb; //Klappentext
        public double rating;
        public List<String> Genre;
        public List<String> similar_Books;
        public List<String> Characters;
        public List<String> Author;

        public Book (String title, String author, String publisher, String blurb, double rating) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.blurb = blurb;
            this.rating = rating;
        }
    
    public Book () {
        this.title = "";
        this.author = "";
        this.publisher = "";
        this.blurb = "";
        this.rating = 0;
        this.Genre=new ArrayList<String>();
        this.similar_Books=new ArrayList<String>();
        this.Characters=new ArrayList<String>();
        this.Author=new ArrayList<String>();
    }
    
  //TODO: funktionen javadoc ergaenzen
  	private static void test() throws UnsupportedEncodingException, IOException {
  	//Name(Buch) -> Autor, aehnlicheB�cher, Charaktere, Genre
  		
  		printBook(buchToinfosBuecher( "Metro 2033",""));
  			//a=  aehnlicheBuecher(testAehnlicheBuecher,"Metro 2033","");
  			//infosBuecher(testInfoBuch,"Metro 2033");
  		
  		
//  		ArrayList<String> a= new ArrayList<>();
//  		a = characterZuBuecherliste("Alexander");
//  		a = themaZuBuecherliste("Horror");
//  		a = autorZuBuecherliste("Tolkien");
//  		a = titleZuBuecherliste("Metro 2033", 5);
//
//  		for(String s: a)System.out.println(s);
  		
  		
  		//Hilfsfunktionen
  			//fetchCharacterList();

  	}
  	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		test();
	}
    
    public static void printBook(Book Buchmeta) {
		System.out.println("________________________________________________");
		System.out.println("Author of  the Book: \n");
		for (int i = 0; i <Buchmeta.Author.size() ; i++)System.out.println(Buchmeta.Author.get(i));
		System.out.println("________________________________________________");
		System.out.println("Similarbooks: \n");
		for (int i = 0; i <Buchmeta.similar_Books.size() ; i++)	System.out.println(Buchmeta.similar_Books.get(i));

		System.out.println("________________________________________________");
		System.out.println("Characters in the Book: \n");
		for (int i = 0; i <Buchmeta.Characters.size() ; i++) System.out.println(Buchmeta.Characters.get(i));
		System.out.println("________________________________________________");
		System.out.println("Genre of the Book: \n");
		for (int i = 0; i <Buchmeta.Genre.size() ; i++)	System.out.println(Buchmeta.Genre.get(i));
		System.out.println("________________________________________________");
	}

    public static Book buchToinfosBuecher(String title, String author) throws UnsupportedEncodingException, IOException {
		Book book = new Book();
    	//link zu buch finden
			String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + " "+ author + "&tab=books&utf8=%E2%9C%93";
			org.jsoup.nodes.Document doc = Jsoup.connect(search_1).get();
			if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) return null; 
			String linkBuch = doc.getElementsByTag("tr").first().html().substring(doc.getElementsByTag("tr").first().html().indexOf("href") + 6, doc.getElementsByTag("tr").first().html().indexOf(">", doc.getElementsByTag("tr").first().html().indexOf("href")) - 1);

		//link oeffnen und daten lesen

			doc = Jsoup.connect("https://www.goodreads.com" + linkBuch).userAgent("bot101").get();
			
		//Infos aus doc lesen
			String publisher = ""; String blurb = ""; double rating = 0;

			title = doc.select("title").text().substring(0, doc.select("title").text().indexOf("by") - 1);
            
			blurb = doc.html().substring(doc.html().indexOf("span id=\"freeText") + 47, doc.html().indexOf("</span>", doc.html().indexOf("span id=\"freeText"))); //TODO: show less umbruecken
		
			rating = Double.parseDouble(doc.html().substring(doc.html().indexOf("ratingValue") + 13, doc.html().indexOf("ratingValue") + 17).toString());
		
		
		int zwischenindex; 	String TmpHtml = ""; String html = ""; String suchstring = "";

		//Suche autor
			if (doc.html().contains("authorName")) {
				html = doc.html().substring(doc.html().indexOf("authorName"));
	
				TmpHtml += html.substring(html.indexOf("authorName"), zwischenindex = html.indexOf("bookMeta"));
				html = html.substring(zwischenindex + 2);
				//   System.out.println(TmpHtml);
	
				suchstring = "name\">";
				while (TmpHtml.contains(suchstring)) {
					book.addAuthor(TmpHtml.substring(TmpHtml.indexOf(suchstring) + 6, zwischenindex = TmpHtml.indexOf("</span>")));
					TmpHtml = TmpHtml.substring(zwischenindex + 4);
				}
			}

		//Suche Characters
			if (doc.html().contains("/characters/")) {
				html = doc.html().substring(doc.html().indexOf("/characters/"));
	
				TmpHtml += html.substring(html.indexOf("/characters/"), zwischenindex = html.indexOf("<span class=\"toggleLink\">"));
				html = html.substring(zwischenindex + 2);
				TmpHtml += html.substring(html.indexOf("/characters/"), zwischenindex = html.indexOf("<span class=\"toggleLink\">"));
				//System.out.println(TmpHtml);
	
				suchstring = "\">";
				while (TmpHtml.contains(suchstring)) {
					book.addCharacter(TmpHtml.substring(TmpHtml.indexOf(suchstring) + 2, zwischenindex = TmpHtml.indexOf("</a>")));
					TmpHtml = TmpHtml.substring(zwischenindex + 4);
				}
			}
			
		//Suche Genres
			TmpHtml = "";
			html = doc.html().substring(doc.html().indexOf("bookPageGenreLink"));
			TmpHtml += html.substring(html.indexOf("/genres/"), zwischenindex = html.indexOf("See top shelves"));
			html = html.substring(zwischenindex + 2);
			suchstring = "\">";
			while (TmpHtml.contains(suchstring) && TmpHtml.contains("actionLinkLite bookPageGenreLink")) {
				book.addGenre(TmpHtml.substring(TmpHtml.indexOf(suchstring) + 2, zwischenindex = TmpHtml.indexOf("</a>")));
				TmpHtml = TmpHtml.substring(TmpHtml.indexOf("actionLinkLite bookPageGenreLink") + 1);
			}
		
		//aehnlicheBuecher
			buchZuAehnlicheBuecher(book,title,author);

		book.title = title; book.author = author; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
			// printmetaInfo(InfoBuch);
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
				if (!result_doc.text().contains("�")) break; //TODO: dirty fix
				//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
				if (result_doc.html().contains("href"))
					link = "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1);
				results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("�") + 1) + link);
			}
			return results;
		}
	
		public static ArrayList<String> characterZuBuecherliste(String character) throws IOException{
			//link f�r charakter finden
				String link = "";
				ArrayList<String> ret = new ArrayList<>();
				String content = new Scanner(new File("./src/source/characters.txt")).useDelimiter("\\Z").next();
				if(content.contains(character))link = content.substring(content.indexOf("URL:", content.indexOf(character))+5, content.indexOf("\n",content.indexOf(character)));
					//link = content.substring(content.indexOf("URL:", content.indexOf(character)),content.indexOf("\n", content.indexOf(content.indexOf("URL:", content.indexOf(character)))));
				if(link.length()==0)System.err.println("charakter nicht gefunden!");
			
			//link �ffnen
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
					//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
					title.add(result_title.text());
				}
			
			//autoren holen
				org.jsoup.select.Elements results_authors = doc.select("a").select(".authorName");
				for (Element result_author : results_authors) {
					//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
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
						//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
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
	
		public static ArrayList<String> buchZuAehnlicheBuecher(Book book, String title, String author) throws UnsupportedEncodingException, IOException {
		ArrayList<String> results = new ArrayList<>();

		//finde link zu buch ~ rufe dafür 1 ergebnis 
			String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + " " + author + "&tab=books&utf8=%E2%9C%93";
			org.jsoup.nodes.Document doc = Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
			
			if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) return null; 
		
			org.jsoup.select.Elements first = doc.getElementsByTag("tr");
			String link = "https://www.goodreads.com." + first.html().substring(first.html().indexOf("href") + 6, first.html().indexOf(">", first.html().indexOf("href")) - 1);
			doc = Jsoup.connect(link + URLEncoder.encode(search_1, "UTF-8")).userAgent("bot101").get();
			//TODO: funzt u.u. nicht immer
			String linktext_schlecht = doc.html().substring(doc.html().indexOf("/trivia/work/") + 13, doc.html().indexOf("\"", doc.html().indexOf("/trivia/work/"))); //System.out.println("link: "+linktext_ann1);

		//similar aufrufen
			String similar = "https://www.goodreads.com/book/similar/" + linktext_schlecht;
			doc = Jsoup.connect(similar).get();

		//liste der ähnlichen bücher sammeln
			org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
			for (Element result_doc : results_doc) {
				//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
				results.add(result_doc.text());//.substring(0, result_doc.text().lastIndexOf("�")) + "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1));
			}

			String tmptitle;
			for (int i = 0; i < results.size(); i++) {
				tmptitle = results.get(i).substring(0, results.get(i).indexOf("by") - 1);
				book.addAehnlichBuch(tmptitle);
			}

		return results;
	}
    

	
	
	
	
    //Setter, Getter etc.
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
					        Genre.addAll(genre) ;
					    }
					
					    public void addGenre(String genre) {
					        if (!Genre.contains(genre)) Genre.add(genre);
					    }
					    public void removeGenre(String genre){
					        if(Genre.contains(genre))   Genre.remove(genre);
					    }
					
					    public void setSimilar_Books(List<String> similarBuch) {
					        similar_Books.addAll(similarBuch) ;
					    }
					
					    public void addAehnlichBuch(String bookname) {
					        if (!similar_Books.contains(bookname)) similar_Books.add(bookname);
					    }
					    public void removeAehnlichBuch(String bookname){
					        if(Genre.contains(bookname)) Genre.remove(bookname);
					    }
					
					    public void setCharacters(List<String> Characters) {
					        similar_Books.addAll(Characters) ;
					    }
					
					    public void addCharacter(String Character) {
					        if (!Characters.contains(Character))  Characters.add(Character);
					    }
					    
					    public void removeCharacter(String Character){
					        if(Genre.contains(Characters)) Genre.remove(Characters);
					    }
}

