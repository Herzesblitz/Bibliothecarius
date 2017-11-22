import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

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
    
    public static void printmetaInfo(Book Buchmeta) {
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

    public static Book BuchToinfosBuecher(Book book, String title, String author) throws UnsupportedEncodingException, IOException {
		//link zu buch finden
		String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + " "+ author + "&tab=books&utf8=%E2%9C%93";
		org.jsoup.nodes.Document doc = Jsoup.connect(search_1).get();

		if(doc.select("h3.searchSubNavContainer").toString().toLowerCase().contains("no results")) {
			return null; 
		}
		
		String linkBuch="";
		linkBuch = doc.getElementsByTag("tr").first().html().substring(doc.getElementsByTag("tr").first().html().indexOf("href") + 6, doc.getElementsByTag("tr").first().html().indexOf(">", doc.getElementsByTag("tr").first().html().indexOf("href")) - 1);

		//link oeffnen und daten lesen
			String publisher = "";
			String blurb = "";
			double rating = 0;
			doc = Jsoup.connect("https://www.goodreads.com" + linkBuch).userAgent("bot101").get();
			title = doc.select("title").text().substring(0, doc.select("title").text().indexOf("by") - 1);

		blurb = doc.html().substring(doc.html().indexOf("span id=\"freeText") + 47, doc.html().indexOf("</span>", doc.html().indexOf("span id=\"freeText"))); //TODO: show less umbruecken
		rating = Double.parseDouble(doc.html().substring(doc.html().indexOf("ratingValue") + 13, doc.html().indexOf("ratingValue") + 17).toString());
		
		book.title = title; book.author = author; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
		
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


		return book;
	}
    
	public static Book sammelBuchInformation(String Buchname, String author) throws IOException {
		Book InfoBuch = new Book();
        List<String> similarbooks=new ArrayList<String>();
		BuchToinfosBuecher(InfoBuch,Buchname,author);
		scrapWebsite.BuchToAehnlicheBuecher(InfoBuch,InfoBuch.title,author);
		
	    printmetaInfo(InfoBuch);
		return InfoBuch;
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

