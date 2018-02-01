import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
		Set<String> ch = gebeCharacterZurueck(1000);
		for(String s: ch)System.out.println(s);
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
			if(doc.html().toString().contains("hasn't written any books"))break; //seite existiert nicht //FIXME eig schlecht

			org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
			for (Element result_doc : results_doc) {
				String url = "https://www.goodreads.com"+result_doc.select("td").select("a.bookTitle").attr("href");
				System.out.println(url);
			//	results.add(Buch.buchToinfosBuecher("", "", url));
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
				title.add(result_title.text());
			}
		
		//autoren holen
			org.jsoup.select.Elements results_authors = doc.select("a").select(".authorName");
			for (Element result_author : results_authors) {
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
	
  	private static void test_hilfsfkt() throws UnsupportedEncodingException, IOException, ClassNotFoundException {
  		//System.out.println("https://www.goodreads.com/book/show/5358.The_Firm?from_search=true".matches("https://www.goodreads.com/book/show/[0-9]+\\.(.)+?from_search=true"));
  		//teste sortmerge
  		ArrayList<Buch> A =new ArrayList<>();
  		Buch a = new Buch(); a.title = "a";
  		Buch c = new Buch(); c.title = "c";
  		Buch ba = new Buch(); ba.title = "ba";
  		A.add(a);
  		A.add(ba);
  		A.add(c);

  		ArrayList<Buch> B =new ArrayList<>();
  		Buch b = new Buch(); b.title = "b";
  		Buch e = new Buch(); e.title = "e";
  		Buch d = new Buch(); d.title = "d";
  		Buch f = new Buch(); f.title = "f";
  		Buch bb = new Buch(); bb.title = "1";

  		B.add(e);
  		B.add(bb);
  		B.add(b);
  		B.add(d);
  		B.add(f);

  		
  		A =Datenbank.sortmerge(A, B);
  		for(Buch buch: A)System.out.println(buch.title);
  	}
  	
    /**wichtig alle SÃ¤tze in einer Zeile,  keine LÃ¼cke zwischen Satzende(.) und nÃ¤chste Satzamfang
     *
     */
  	public static void erstelleTrainingsdaten(Set<String> daten, String[] trainingssatze, String Ersetzungstag) {
  			ArrayList<String> data = new ArrayList<String>(daten);
  		  	ArrayList<String> tr = new ArrayList<String>();
  		  	for(int i=0;  i<daten.size();  i++){
  		  		String t = trainingssatze[i%trainingssatze.length].replace(Ersetzungstag, data.get(i));
  		  		tr.add(t+".");
  		  	}
  		  	for(String t: tr) {
  		  		System.out.println(t);
  		  	}
  	}
  	
  	public static Set<String> gebeTitelZurueck(int anzahl) throws FileNotFoundException, ClassNotFoundException, IOException{
  		Datenbank.load_Database();
  		Set<String> titel = new HashSet<String>();
  		for(Buch b: Datenbank.buecherliste) {
  			if(titel.size() == anzahl) break;
  			titel.add(b.title);
  		}
  		return titel;
  	}
  	
  	//"t1, t2 und t3" "t1 und t2"
  	public static Set<String> gebeThemaZurueck(int anzahl) throws FileNotFoundException, ClassNotFoundException, IOException{
  		Datenbank.load_Database();
  		Set<String> thema = new HashSet<String>();
  		for(Buch b: Datenbank.buecherliste) {
				if(thema.size() == anzahl) break;
	  			String s="";
	  			for(int i=0; i<b.shelves.size(); i++) {
	  				String l=b.shelves.get(i).replaceAll(" ", " ").trim();
	  				if(l.contains(">"))l = l.substring(l.lastIndexOf(">")+1).replaceAll("  ", " ");
	  				while(l.contains(" +"))l = l.replaceAll(" +", " ");
	  	  			if(b.shelves.size()>=3 && i >= 0 && i<b.shelves.size()-2) {s+=l+", ";continue;}
	  				if(i == b.shelves.size()-1) {s+=l;continue;}
	  	  			if(b.shelves.size()>=2 && i == b.shelves.size()-2) {s+=l+" und ";continue;}
	  			}
	  			thema.add(s);
  		}
  		return thema;
  	}
  	

  	public static Set<String> gebeAutorZurueck(int anzahl) throws FileNotFoundException, ClassNotFoundException, IOException{
  		Datenbank.load_Database();
  		Set<String> autor = new HashSet<String>();
  		for(Buch b: Datenbank.buecherliste) {
				if(autor.size() == anzahl) break;
	  			String s="";
	  			for(int i=0; i<b.Author.size(); i++) {
	  				String l=b.Author.get(i);
	  	  			if(b.Author.size()>=2 && i == b.Author.size()-2) {s+=l+" und ";continue;}
	  				if(i == b.Author.size()-1) {s+=l;continue;}
	  	  			if(b.Author.size()>=3 && i >= 0 && i<b.Author.size()-2) {s+=l+", ";continue;}
	  			}
	  			autor.add(s);
  		}
  		return autor;
  	}
  
  	public static Set<String> gebeCharacterZurueck(int anzahl) throws FileNotFoundException, ClassNotFoundException, IOException{
  		Datenbank.load_Database();
  		Set<String> character = new HashSet<String>();
  		for(Buch b: Datenbank.buecherliste) {
				if(character.size() == anzahl) break;
	  			String s="";
	  			for(int i=0; i<b.Characters.size(); i++) {
	  				String l=b.Characters.get(i);
	  	  			if(b.Characters.size()>=2 && i == b.Characters.size()-2) {s+=l+" und ";continue;}
	  				if(i == b.Characters.size()-1) {s+=l;continue;}
	  	  			if(b.Characters.size()>=3 && i >= 0 && i<b.Characters.size()-2) {s+=l+", ";continue;}
	  			}
	  			character.add(s);
  		}
  		return character;
  	}
  	
  	public static Set<String> gebeYearZurueck(int anzahl){
  		Set<String> year = new HashSet<String>();
  		while(year.size() < anzahl) {
  			year.add(String.valueOf((int) (Math.random()*2000)));
  		}
  		return year;
  	}
  	
  	
	
	//TODO: funktionen javadoc ergaenzen
  	private static void test() throws UnsupportedEncodingException, IOException, ClassNotFoundException {
  	//Datenbank.printBooklist(Datenbank.searchBook_author(new ArrayList<String>(Arrays.asList("Tolkien"))));
  		//Datenbank.printBooklist(Datenbank.searchBook_characters(new ArrayList<String>(Arrays.asList("Hurin"))));
  	Datenbank.printBooklist(Datenbank.searchBook_sprache(("Deutsch")));
  	
  
  		
  		
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
