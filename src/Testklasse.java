import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Klasse zum Testen der anderen Klassen und mit Funktionen die nicht dort gebraucht worden
 * @author Johannes
 *
 */
public class Testklasse {
	Datenbank db = new Datenbank();

	public static void main(String[] args) throws UnsupportedEncodingException, IOException, ClassNotFoundException {
		test_hilfsfkt();
		}

	public static ArrayList<Buch> autorZuBuecherliste(String autor) throws UnsupportedEncodingException, IOException {
		ArrayList<Buch> results = new ArrayList<>();
		//link fuer autor finden
			String search_1 = "https://www.goodreads.com/search?page=1&query=" + autor + "&tab=books&utf8=%E2%9C%93";
			org.jsoup.nodes.Document doc = Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
		//TODO: funzt whrsch nicht immer
			String linkAutor = "https://"+doc.html().substring(doc.html().indexOf("www.goodreads.com/author/show/"), doc.html().indexOf("\"", doc.html().indexOf("/www.goodreads.com/author/show/"))).replaceFirst("/show/", "/list/");			
				
		int page_results=1;
		
		while(true) {
			//rufe seiten fuer ergbnisse auf
			doc = Jsoup.connect(linkAutor+"?page="+page_results).ignoreHttpErrors(true).userAgent("bot101").get();
			if(doc.html().toString().contains("hasn't written any books"))break; //seite existiert nicht //TODO eig schlecht

			org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
			for (Element result_doc : results_doc) {
				String url = "https://www.goodreads.com"+result_doc.select("td").select("a.bookTitle").attr("href");
				System.out.println(url);
				results.add(Buch.buchToinfosBuecher("", "", url));
			}			
		}
		
		return results;
	}

	public static ArrayList<String> charakterZuBuecherliste(String character) throws IOException{
		//link für charakter finden
			String link = "";
			ArrayList<String> ret = new ArrayList<>();

			String content = new Scanner(new File("./src/source/Charaktere.txt")).useDelimiter("//Z").next();	
			if(content.contains(character))link = content.substring(content.indexOf("URL:", content.indexOf(character))+5, content.indexOf("\n",content.indexOf(character)));
				//link = content.substring(content.indexOf("URL:", content.indexOf(character)),content.indexOf("\n", content.indexOf(content.indexOf("URL:", content.indexOf(character)))));
			if(link.length()==0) {
				//System.err.println("charakter nicht gefunden!");
				ArrayList<String> a = new ArrayList(); a.add("");
				return a;
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
	
	 public static org.jsoup.nodes.Document thread_connect(String url) {
		    org.jsoup.nodes.Document doc = null;
		    try {
		        doc = Jsoup.connect(url).timeout(0).get();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		    return doc;
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
	
  	private static void test_hilfsfkt() throws UnsupportedEncodingException, IOException {
  		System.out.println("https://www.goodreads.com/book/show/5358.The_Firm?from_search=true".matches("https://www.goodreads.com/book/show/[0-9]+\\.(.)+?from_search=true"));

  	}

	
	//TODO: funktionen javadoc ergaenzen
  	private static void test() throws UnsupportedEncodingException, IOException {
  		//  	for(int i=0; i<1000; i++) {
//			String randomBook = randomBookURL();
//			ArrayList<String> b= buchZuAehnlicheBuecher("", "", randomBook);
//			for(String a: b)System.out.println("   "+a);
//		}
  
  	//Name(Buch) -> Autor, aehnlicheBücher, Charaktere, Genre
//  		ArrayList<String> b= buchZuAehnlicheBuecher("Alexander", "");
  		Buch.ausgebenBuch(Buch.buchToinfosBuecher("Platon's Republic", "Platon", ""));
  	//Name(Buch) -> Autor, aehnlicheBücher, Charaktere, Genre
//  		ArrayList<String> b= buchZuAehnlicheBuecher("Holy Bible: King James Version", "");
//  			for(String a: b)System.out.println(a);
  		//printBook(buchToinfosBuecher("The Name of the Wind (The Kingkiller Chronicle, #1)","",""));
  			//a=  aehnlicheBuecher(testAehnlicheBuecher,"Metro 2033","");
  			//infosBuecher(testInfoBuch,"Metro 2033");
  		

  		for(int i=0; i<1000; i++) {
  			ArrayList<String> a= new ArrayList<>();
//  	  		a = characterZuBuecherliste(randomString());
//  	  		a = themaZuBuecherliste("Horror");
//  	  		a = autorZuBuecherliste("Tolkien");
//  	  		a = titleZuBuecherliste("Metro 2033", 5);
  	  		for(String s: a)System.out.println(s);
  		}
  		
  		//a = characterZuBuecherliste("Alexander");
//  		a = themaZuBuecherliste("Horror");
  			//printListofBooks(autorZuBuecherliste("Tolkien"));
//  		a = titleZuBuecherliste("Metro 2033", 5);
//
  		
  		
  		//Hilfsfunktionen
  			//fetchCharacterList();

  	}
  	
  	public static void gebeausDatabase() throws FileNotFoundException, ClassNotFoundException, IOException {
  		Datenbank.load_Database();
  		Datenbank.printBooklist(Datenbank.buecherliste);
		Datenbank.printAllTitles();
  	}
}
