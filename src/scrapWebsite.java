import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class scrapWebsite {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		Book testInfoBuch = new Book();
		Book testAehnlicheBuecher = new Book("testtitel", "testautor", "testpub", "testblurb", 0.1);
		BookList testBuchliste = new BookList();
		
		fetchCharacterList(); //TODO:Test
		

		//infosBuecher(testInfoBuch,"Metro 2033");
		//ThemaZuBuecherliste("Horror",testBuchliste);
		//autorBuecher("Tolkien");
		// aehnlicheBuecher(testAehnlicheBuecher,"Metro 2033");
		//search_title("Metro 2033", 5);


		//sammelBuchInformation("Metro 2033");
	}
	//
	
	private static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public static void fetchCharacterList() throws IOException {
		//anzahl pages 
			Elements elements = Jsoup.connect("https://www.goodreads.com/characters").get().select("a");
			int max=0;
			for(Element el: elements) {
			 if(isInteger(el.text(),10) && max < Integer.parseInt(el.text()))max = Integer.parseInt(el.text());
			}
				//System.out.println(max);
		String 	x=""; 
			
		//durch pages gehen
		for(int page=1; page<=2; page++) {
			 elements = Jsoup.connect("https://www.goodreads.com/characters?page="+page).get().select("div.elementList"); //. selektiert klassen (mit angegebenen namen) des tags vor dem punkt
			for(Element el: elements) {
					byte ptext[] = el.text().getBytes();
					String name = new String(ptext, "UTF-8");
					//TODO: namen mit nicht lateinischen zeichen werden rausgeschmissen (wegen ersetzung durch ?) -> fix
						boolean ok=false;
						char[] chars = name.toCharArray();
					    for (char c : chars) {
					        if(Character.isLetter(c)) {
					        	ok = true; break;
					        }
					    }	
					    if(!ok)continue;
					//System.out.println(value);
					
					String href = "https://www.goodreads.com"+el.select("a").attr("href");                                                           //attr Attribut selektiert <tag Attribut=....> 
					//System.out.println(href);
					x += name + " URL: " + href +"\n";
			}
			System.out.println(page);
		}
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter("characters.txt"));
		    out.write(x);  //Replace with the string 
		                                             //you are trying to write
		    out.close();
		}
		catch (IOException e)
		{
		    System.out.println("Exception ");

		}
	}


	public static Book infosBuecher(Book book, String title) throws UnsupportedEncodingException, IOException {
		//link zu buch finden
		String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + "&tab=books&utf8=%E2%9C%93";
		org.jsoup.nodes.Document doc = Jsoup.connect(search_1).get();
		String linkBuch = doc.getElementsByTag("tr").first().html().substring(doc.getElementsByTag("tr").first().html().indexOf("href") + 6, doc.getElementsByTag("tr").first().html().indexOf(">", doc.getElementsByTag("tr").first().html().indexOf("href")) - 1);
		//System.out.println(linkBuch);

		//link oeffnen und daten lesen
		String author = "";
		String publisher = "";
		String blurb = "";
		double rating = 0;
		doc = Jsoup.connect("https://www.goodreads.com" + linkBuch).userAgent("bot101").get();
		//System.out.println(doc.html());
		title = doc.select("title").text().substring(0, doc.select("title").text().indexOf("by") - 1);
		//TODO:Zeigt nur den ersten autor Rest wird nicht gefunden!
		//author = doc.select("title").text().substring(doc.select("title").text().indexOf("by")+3);

		blurb = doc.html().substring(doc.html().indexOf("span id=\"freeText") + 47, doc.html().indexOf("</span>", doc.html().indexOf("span id=\"freeText"))); //TODO: show less umbruecken
		rating = Double.parseDouble(doc.html().substring(doc.html().indexOf("ratingValue") + 13, doc.html().indexOf("ratingValue") + 17).toString());
		book.title = title;
		book.author = author;
		book.publisher = publisher;
		book.blurb = blurb;
		book.rating = rating;
		System.out.println(book.title);
		//System.out.println(book.author);
		System.out.println(book.publisher);
		System.out.println(book.blurb);
		System.out.println(book.rating);

		int zwischenindex;
		String TmpHtml = "";
		String html = "";
		String suchstring = "";

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
		// System.out.println(TmpHtml);


		suchstring = "\">";
		while (TmpHtml.contains(suchstring) && TmpHtml.contains("actionLinkLite bookPageGenreLink")) {
			book.addGenre(TmpHtml.substring(TmpHtml.indexOf(suchstring) + 2, zwischenindex = TmpHtml.indexOf("</a>")));
			TmpHtml = TmpHtml.substring(TmpHtml.indexOf("actionLinkLite bookPageGenreLink") + 1);
		}


		return book;
	}

	public static ArrayList<String> autorBuecher(String autor) throws UnsupportedEncodingException, IOException {
		ArrayList<String> results = new ArrayList<>();
		//link für autor finden
		String search_1 = "https://www.goodreads.com/search?page=1&query=" + autor + "&tab=books&utf8=%E2%9C%93";
		org.jsoup.nodes.Document doc = Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
		//System.out.println(doc.html());
		//TODO: funzt whrsch nicht immer
		String linkAutor = doc.html().substring(doc.html().indexOf("www.goodreads.com/author/show/") + 30, doc.html().indexOf("\"", doc.html().indexOf("/www.goodreads.com/author/show/")));
		//System.out.println(linkAutor);

		doc = Jsoup.connect("https://www.goodreads.com/author/list/" + linkAutor).userAgent("bot101").get();
		org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");

		String link = "";
		for (Element result_doc : results_doc) {
			if (!result_doc.text().contains("—")) break; //TODO: dirty fix

			//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
			if (result_doc.html().contains("href"))
				link = "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1);
			results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—") + 1) + link);
		}
		//for(String s: results)System.out.println(s);
		return results;
	}

	public static ArrayList<String> aehnlicheBuecher(Book book, String title) throws UnsupportedEncodingException, IOException {
		ArrayList<String> results = new ArrayList<>();

		//finde link zu buch ~ rufe dafür 1 ergebnis 
		String search_1 = "https://www.goodreads.com/search?page=1&query=" + title + "&tab=books&utf8=%E2%9C%93";
		org.jsoup.nodes.Document doc = Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
		org.jsoup.select.Elements first = doc.getElementsByTag("tr");
		//System.out.println(first.html());

		String link = "https://www.goodreads.com." + first.html().substring(first.html().indexOf("href") + 6, first.html().indexOf(">", first.html().indexOf("href")) - 1);
		//System.out.println(link);
		doc = Jsoup.connect(link + URLEncoder.encode(search_1, "UTF-8")).userAgent("bot101").get();
		//TODO: funzt u.u. nicht immer
		String linktext_schlecht = doc.html().substring(doc.html().indexOf("/trivia/work/") + 13, doc.html().indexOf("\"", doc.html().indexOf("/trivia/work/"))); //System.out.println("link: "+linktext_ann1);

		//similar aufrufen
		String similar = "https://www.goodreads.com/book/similar/" + linktext_schlecht;
		//System.out.println(similar);

		doc = Jsoup.connect(similar).get();
		//System.out.println(doc.html());

		//liste der ähnlichen bücher sammeln
		org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
		for (Element result_doc : results_doc) {
			//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
			results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—")) + "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1));
		}

		//for(String s: results)System.out.println(s);
		String tmptitle;
		for (int i = 0; i < results.size(); i++) {

			tmptitle = results.get(i).substring(0, results.get(i).indexOf("by") - 1);
			//System.out.println(tmptitle);
			book.addAehnlichBuch(tmptitle);
		}

		return results;
	}

	/**
	 * searches for title
	 *
	 * @param search
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static ArrayList<String> search_title(String search, int maxresultpages) throws UnsupportedEncodingException, IOException {
		String search_2 = "https://www.goodreads.com/search?page=1&query=" + search + "&tab=books&utf8=%E2%9C%93";
		String search_1 = "https://www.goodreads.com/search?query=" + search;

		System.out.println(search_1);

		//find number of result pages
		org.jsoup.nodes.Document doc = Jsoup.connect(search_2).userAgent("bot101").get();
		String title = "";
		for (Element meta : doc.select("meta"))
			if (meta.attr("name").contains("title"))
				title = meta.attr("content"); //search for meta attr that contains title
		if (title.length() > 0 && title.contains("showing") && title.contains("of"))
			title = title.substring(title.indexOf("showing") + 10, title.indexOf("of", title.indexOf("showing")) - 1);
		int pages_number = Math.min(maxresultpages, Integer.parseInt(title));

		//go through result pages
		ArrayList<String> results = new ArrayList<>();

		for (int page = 1; page < pages_number; page++) {
			String url = "https://www.goodreads.com/search?page=" + page + "&query=" + search + "&tab=books&utf8=%E2%9C%93";
			doc = Jsoup.connect(url).userAgent("usrdasf").get();

			//TODO: selektion des docs präzisieren um
			org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
			for (Element result_doc : results_doc) {
				//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
				results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—")) + "\n link:" + result_doc.html().substring(result_doc.html().indexOf("href") + 6, result_doc.html().indexOf(">", result_doc.html().indexOf("href")) - 1));
			}
			System.out.println(page + "/" + pages_number);
		}

		for (String s : results) System.out.println(s);
		return results;
	}


	//TODO: charakter gegeben -> welches title?

	//TODO: Bücher für Themem
	public static BookList ThemaZuBuecherliste(String thema, BookList liste) throws UnsupportedEncodingException, IOException {
		liste.thema = thema;
		ArrayList<String> title = new ArrayList<>();
		ArrayList<String> author = new ArrayList<>();


		String search_1 = "https://www.goodreads.com/shelf/show/" + thema;


		org.jsoup.nodes.Document doc = Jsoup.connect(search_1).userAgent("usrdasf").get();
		//System.out.println(doc.html());


		//TODO: selektion des docs präzisieren um 
		org.jsoup.select.Elements results_titles = doc.select("a").select(".bookTitle");

		for (Element result_title : results_titles) {
			//System.out.println(result_title.text());

			//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
			title.add(result_title.text());
		}

		org.jsoup.select.Elements results_authors = doc.select("a").select(".authorName");

		for (Element result_author : results_authors) {
			//System.out.println(result_author.text());

			//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
			author.add(result_author.text());
		}

		String title_raw = "";

		//merge //TODO: verbessern??
		for (int i = 0; i < title.size(); i++) {
			title_raw = title.get(i).toString().substring(0, title.get(i).lastIndexOf("("));
			liste.title.add(title_raw);
			liste.autor.add(author.get(i).toString());
			System.out.println(title_raw + "by " + author.get(i).toString());
		}

		return liste;
	}
	public static void printmetaInfo(Book Buchmeta) {

		System.out.println("________________________________________________");
		System.out.println("Author of  the Book: \n");
		for (int i = 0; i <Buchmeta.Author.size() ; i++)
		{
			System.out.println(Buchmeta.Author.get(i));
		}
		System.out.println("________________________________________________");
		System.out.println("Similarbooks: \n");
		for (int i = 0; i <Buchmeta.similar_Books.size() ; i++)
		{
			System.out.println(Buchmeta.similar_Books.get(i));
		}
		System.out.println("________________________________________________");
		System.out.println("Characters in the Book: \n");
		for (int i = 0; i <Buchmeta.Characters.size() ; i++)
		{
			System.out.println(Buchmeta.Characters.get(i));
		}
		System.out.println("________________________________________________");
		System.out.println("Genre of the Book: \n");
		for (int i = 0; i <Buchmeta.Genre.size() ; i++)
		{
			System.out.println(Buchmeta.Genre.get(i));
		}
		System.out.println("________________________________________________");

	}

	public static Book sammelBuchInformation(String Buchname) throws IOException {
		Book InfoBuch = new Book();
        List<Book> similarbooks=new ArrayList<Book>();
		infosBuecher(InfoBuch,Buchname);
		aehnlicheBuecher(InfoBuch,InfoBuch.title);
		//zu den Ähnlichen Bücher lassen sich auch Informationen sammeln
        for (int i = 0; i <InfoBuch.similar_Books.size() ; i++)
        {

            similarbooks.set(i,similarbooks.get(i));
        }
        printmetaInfo(InfoBuch);
		return InfoBuch;
	}
}
