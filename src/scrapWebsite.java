import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class scrapWebsite {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
	
	
		test();
	}	
	
	
	//TODO: funktionen javadoc ergaenzen
	private static void test() throws UnsupportedEncodingException, IOException {
	//Name(Buch) -> Autor, aehnlicheB�cher, Charaktere, Genre
		//Book.sammelBuchInformation("Metro 2033","");
			//a=  aehnlicheBuecher(testAehnlicheBuecher,"Metro 2033","");
			//infosBuecher(testInfoBuch,"Metro 2033");
		
		
		ArrayList<String> a= new ArrayList<>();
		//a = characterToBook("Alexander");
		//a = ThemaZuBuecherliste("Horror");
		//a = AutorToBuecher("Tolkien");
		a = search_titleToBuecherListe("Metro 2033", 5);

		for(String s: a)System.out.println(s);
		
		
		//Hilfsfunktionen
			//fetchCharacterList();

	}
	
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
					String href = "https://www.goodreads.com"+el.select("a").attr("href");                                                           //attr Attribut selektiert <tag Attribut=....> 
					x += name + " URL: " + href +"\n";
			}
		}
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter("./src/source/characters.txt"));
		    out.write(x);  //Replace with the string 
		                                             //you are trying to write
		    out.close();
		}
		catch (IOException e)
		{
		    System.out.println("Exception ");

		}
	}
	
	public static ArrayList<String> characterToBook(String character) throws IOException{
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
	
	

	public static ArrayList<String> AutorToBuecher(String autor) throws UnsupportedEncodingException, IOException {
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

	public static ArrayList<String> BuchToAehnlicheBuecher(Book book, String title, String author) throws UnsupportedEncodingException, IOException {
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

	/**
	 * searches for title
	 *
	 * @param search
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static ArrayList<String> search_titleToBuecherListe(String search, int maxresultpages) throws UnsupportedEncodingException, IOException {
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

	public static ArrayList<String> ThemaZuBuecherliste(String thema) throws UnsupportedEncodingException, IOException {
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
	

}
