import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//BUGLISTE
//TODO: quotes
//TODO: inhaltszusammenfassung => alle fkt. database 
public class Buch implements Serializable {
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
        public ArrayList<String> similar_Books =  new ArrayList<>();
        public List<String> Characters =  new ArrayList<>();
        public List<String> Author =  new ArrayList<>();

    public Buch() {
    	
	}    
        
    public Buch (String title, String author) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
    	setData(title, author);
    }
    
  
  	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		//Datenbank.printBook(buchToinfosBuecher("Der Herr der Ringe. Anhänge und Register", "","", 10));
			//buchZuAehnlicheBuecher("The Valentine Date: Long Distance, Billionaires and Former Bad Boy's Collection" , "", "", 10);
				//Datenbank.printBook(buchToinfosBuecher("Lord of the Rings", "", "", 10));
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
			for(Buch b: liste) {ausgebenBuch(b); if(b.title.equals(""))continue; System.out.println(("\n\n\n\n\n"));}
	}
	
	public static String ausgebenBücherliste_s(ArrayList<Buch> liste) {
		String ausgabe="";
			for(Buch b: liste) {ausgabe += ausgebenBuch_s(b); if(b.title.equals(""))continue; ausgabe+=("°°°°°");}
		return ausgabe;
	}
	
	public static String ausgebenBücherliste_s_param(ArrayList<Buch> liste, ArrayList<String> param) {
		String ausgabe="";
			for(Buch b: liste) {ausgabe += ausgebenBuch_s(b, param); if(b.title.equals(""))continue; ausgabe+=("°°°°°");}
		return ausgabe;
	}
	
	public static void ausgebenBuch(Buch Buchmeta) {
		if (Buchmeta.title.equals(""))
			return;
		if (!Buchmeta.url.equals(""))
			System.out.println("URL: " + Buchmeta.url);
		if (!Buchmeta.title.equals(""))
			System.out.println("Title of the Book: " + Buchmeta.title);
		if (Buchmeta.Author.size() == 1)
			System.out.println("Author: " + Buchmeta.Author.get(0));
		else if (Buchmeta.Author.size() > 1) {
			System.out.println(("Authors: "));
			for (int i = 0; i < Buchmeta.Author.size() - 1; i++) {
				System.out.println((Buchmeta.Author.get(i) + ", "));
			}
			System.out.println((Buchmeta.Author.get(Buchmeta.Author.size() - 1)));
		}
		if (Buchmeta.year != Integer.MIN_VALUE)
			System.out.println("Publihsing year: " + Buchmeta.year);
		// System.out.println("ISBN of the Book: "+Buchmeta.isbn);
		if (!Buchmeta.publisher.equals(""))
			System.out.println("Publisher: " + Buchmeta.publisher);
		if (Buchmeta.rating != Integer.MIN_VALUE)
			System.out.println("Rating: " + Buchmeta.rating);
		System.out.println(("") + "\n");
		if (Buchmeta.Characters.size() > 0) {
			System.out.println(("Characters in the Book:"));
			for (String x : Buchmeta.Characters)
				System.out.println(x);
			System.out.println(("") + "\n");
		}
		if (Buchmeta.shelves.size() > 0) {
			System.out.println(("Genre of the Book:"));
			for (String x : Buchmeta.shelves)
				System.out.println(x);
			System.out.println(("") + "\n");
		}
		if (Buchmeta.awards.size() > 0) {
			System.out.println(("Awards of the Book: "));
			for (String x : Buchmeta.awards)
				System.out.println(x);
			System.out.println(("") + "\n");

		}
		if (Buchmeta.similar_Books != null && Buchmeta.similar_Books.size() > 0) {
			System.out.println(("Ähnliche Bücher:"));
			for (String x : Buchmeta.similar_Books)
				System.out.println(x);
			System.out.println(("") + "\n");
		}
		if (!Buchmeta.blurb.equals("")) {
			System.out.println(("Blurb of the Book:"));
			System.out.println((Buchmeta.blurb) + "\n");
		}
	}

	public static String ausgebenBuch_s(Buch Buchmeta, ArrayList<String> param) {
		//		for (Iterator<String> iterator = Buchmeta.Characters.iterator(); iterator.hasNext(); ) {
		//	    String value = iterator.next();
		//	    if (value.contains("[a-zA-Z]+") == false) {
		//	        iterator.remove();
		//	    }
		//	}
		//	for (Iterator<String> iterator = Buchmeta.Author.iterator(); iterator.hasNext(); ) {
		//	    String value = iterator.next();
		//	    if (value.contains("[a-zA-Z]+") == false) {
		//	        iterator.remove();
		//	    }
		//	}
		//	for (Iterator<String> iterator = Buchmeta.shelves.iterator(); iterator.hasNext(); ) {
		//	    String value = iterator.next();
		//	    if (value.contains("[a-zA-Z]+") == false) {
		//	        iterator.remove();
		//	    }
		//	}
		//	for (Iterator<String> iterator = Buchmeta.similar_Books.iterator(); iterator.hasNext(); ) {
		//	    String value = iterator.next();
		//	    if (value.contains("[a-zA-Z]+") == false) {
		//	        iterator.remove();
		//	    }
		//	}
		//	for (Iterator<String> iterator = Buchmeta.awards.iterator(); iterator.hasNext(); ) {
		//	    String value = iterator.next();
		//	    if (value.contains("[a-zA-Z]+") == false) {
		//	        iterator.remove();
		//	    }
		//	}
		
		String ausgabe="";
			if(Buchmeta.title.equals(""))return "";
			//if(!Buchmeta.url.equals("") && param.contains("url"))ausgabe+="URL: "+Buchmeta.url+"\n";
			if(!Buchmeta.title.equals("") && param.contains("titel"))ausgabe+="Titel: "+Buchmeta.title+"°"; 
			if(Buchmeta.Author.size() == 1 && param.contains("autor")) ausgabe+="Autor: "+Buchmeta.Author.get(0)+"°";
			else if(Buchmeta.Author.size() > 1 && param.contains("autor")) {ausgabe+=("Autoren: "); for (int i=0; i<Buchmeta.Author.size()-1; i++) {ausgabe+=(Buchmeta.Author.get(i)+", ")+"°";} ausgabe+=(Buchmeta.Author.get(Buchmeta.Author.size()-1))+"°";}
			if(Buchmeta.year != Integer.MIN_VALUE && param.contains("jahr"))ausgabe+="Veröffentlichungsjahr: "+Buchmeta.year+"°";
//			System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
			if(!Buchmeta.publisher.equals("") && param.contains("publisher") )ausgabe+="Veröffentlichungsdaten: "+Buchmeta.publisher+"°";
			if(Buchmeta.rating != Integer.MIN_VALUE && param.contains("publisher"))ausgabe+="Bewertung: "+Buchmeta.rating+"°";
			ausgabe+=("°°");
			if(Buchmeta.Characters.size() > 0 && param.contains("charaktere") ) {
				ausgabe+=("Charaktere: °");
				for (String x: Buchmeta.Characters) System.out.println(x);
				ausgabe+=("°");
			}
			if(Buchmeta.shelves.size() > 0  && param.contains("thema") ) {
				ausgabe+=("Genres: °");
				for (String x: Buchmeta.shelves)	System.out.println(x);
				ausgabe+=("°");
			}
			if(Buchmeta.awards.size() > 0 && param.contains("awards")) {
				ausgabe+=("Auszeichnungen: °");
				for (String x: Buchmeta.awards)	System.out.println(x);
				ausgabe+=("°");
			}
			if(Buchmeta.similar_Books != null && Buchmeta.similar_Books.size()>0  && param.contains("similar") ){
				ausgabe+=("Ähnliche Bücher: \n");
				for (String x: Buchmeta.similar_Books)	System.out.println(x);
				ausgabe+=("°");
			}
			if(!Buchmeta.blurb.equals("") && param.contains("blurb") ) {
				ausgabe+=("Klappentext: °");
				ausgabe+=(Buchmeta.blurb)+"°";
			}
		return ausgabe;
	}
	
	public static String ausgebenBuch_s(Buch Buchmeta) {
		//BUG1
//			for (Iterator<String> iterator = Buchmeta.Characters.iterator(); iterator.hasNext(); ) {
//			    String value = iterator.next();
//			    if (value.contains("[a-zA-Z]+") == false) {
//			        iterator.remove();
//			    }
//			}
//			for (Iterator<String> iterator = Buchmeta.Author.iterator(); iterator.hasNext(); ) {
//			    String value = iterator.next();
//			    if (value.contains("[a-zA-Z]+") == false) {
//			        iterator.remove();
//			    }
//			}
//			for (Iterator<String> iterator = Buchmeta.shelves.iterator(); iterator.hasNext(); ) {
//			    String value = iterator.next();
//			    if (value.contains("[a-zA-Z]+") == false) {
//			        iterator.remove();
//			    }
//			}
//			for (Iterator<String> iterator = Buchmeta.similar_Books.iterator(); iterator.hasNext(); ) {
//			    String value = iterator.next();
//			    if (value.contains("[a-zA-Z]+") == false) {
//			        iterator.remove();
//			    }
//			}
//			for (Iterator<String> iterator = Buchmeta.awards.iterator(); iterator.hasNext(); ) {
//			    String value = iterator.next();
//			    if (value.contains("[a-zA-Z]+") == false) {
//			        iterator.remove();
//			    }
//			}
			
		
		String ausgabe="";
			if(Buchmeta.title.equals(""))return "";
			//if(!Buchmeta.url.equals(""))ausgabe+="URL: "+Buchmeta.url+"\n";
			if(!Buchmeta.title.equals(""))ausgabe+="Titel: "+Buchmeta.title+"°"; 
			if(Buchmeta.Author.size() == 1) ausgabe="Autor: "+Buchmeta.Author.get(0)+"°";
			else if(Buchmeta.Author.size() > 1) {ausgabe+=("Autoren: "); for (int i=0; i<Buchmeta.Author.size()-1; i++) {ausgabe+=(Buchmeta.Author.get(i)+", ")+"°";} ausgabe+=(Buchmeta.Author.get(Buchmeta.Author.size()-1))+"°";}
			if(Buchmeta.year != Integer.MIN_VALUE)ausgabe+="Veröffentlichungsjahr: "+Buchmeta.year+"°";
//			System.out.println("ISBN of the Book: "+Buchmeta.isbn);    	
			if(!Buchmeta.publisher.equals(""))ausgabe+="Veröffentlichungsdaten:"+Buchmeta.publisher+"°";
			if(Buchmeta.rating != Integer.MIN_VALUE)ausgabe+="Bewertung(1-5):"+Buchmeta.rating+"°";
			if(Buchmeta.Characters.size() > 0) {
				ausgabe+=("Charaktere: ");
				for (String x: Buchmeta.Characters) System.out.println(x);
				ausgabe+=("°");
			}
			if(Buchmeta.shelves.size() > 0) {
				ausgabe+=("Genres: °");
				for (String x: Buchmeta.shelves)	System.out.println(x);
				ausgabe+=("°");
			}
			if(Buchmeta.awards.size() > 0) {
				ausgabe+=("Auszeichnungen: °");
				for (String x: Buchmeta.awards)	System.out.println(x);
				ausgabe+=("°");
				
			}
			if(Buchmeta.similar_Books != null && Buchmeta.similar_Books.size()>0){
				ausgabe+=("Ähnliche Bücher: °");
				for (String x: Buchmeta.similar_Books)	System.out.println(x);
				ausgabe+=("°");
			}
			if(!Buchmeta.blurb.equals("")) {
				ausgabe+=("Klappentext: °");
				ausgabe+=(Buchmeta.blurb)+"°";
			}
		return ausgabe;
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
			return "";
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
			//System.out.println(linkBuch);
		}
		//link oeffnen und daten lesen
		if(linkBuch == null)return new Buch();
    	doc = Jsoup.connect(linkBuch).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:58.0) Gecko/20100101 Firefox/58.0").referrer("http://www.google.com").get();
 
		//Infos aus doc lesen
    		book.url = linkBuch;
			String publisher = ""; String blurb = ""; double rating = 0;
			
			//prüfe "Edition Language" auf Deutsch (mglw. auch Englisch)
				String sprache = doc.getElementsByClass("infoBoxRowItem").select("[itemprop=inLanguage]").text(); 
				if(!sprache.equals("German") && !sprache.equals("English"))return new Buch();
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
				
				for(Element el: elements) {
					String thema = el.text();
					if(thema.contains(">"))thema = thema.substring(thema.lastIndexOf(">")+1); 
					book.shelves.add(el.text());
				}
			
			
			//aehnlicheBuecher
				book.similar_Books = buchZuAehnlicheBuecher("","",linkBuch, anz_ähnliche );
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
				//System.out.println("Title: "+title);
		book.title = title; book.publisher = publisher; book.blurb = blurb; book.rating = rating;
		book.sortAuthors(); book.sortAwards(); book.sortCharacter(); book.sortSimilarBooks(); book.sortThemen();
		//zeilenumbrüche löschen
		 	book.url = book.url.replace("\n", ""); 
		 	book.sprache = book.sprache.replaceAll("\n",""); 
		 	book.isbn = book.isbn.replaceAll("\n",""); 
		 	book.buecherreihe = book.buecherreihe.replaceAll("\n",""); 
		 	book.title = book.title.replaceAll("\n",""); 
		 	book.publisher = book.publisher.replaceAll("\n",""); 
		 	book.covertext = book.covertext.replaceAll("\n",""); 
		 	book.blurb = book.blurb.replaceAll("\n",""); 
		 	for(String a: book.awards)a = a.replaceAll("\n",""); 
		 	for(String a: book.shelves)a = a.replaceAll("\n",""); 
		 	for(String a: book.similar_Books)a = a.replaceAll("\n",""); 
		 	for(String a: book.Characters)a = a.replaceAll("\n",""); 
		 	for(String a: book.Author)a = a.replaceAll("\n",""); 
		return book;
	}
    
    public static ArrayList<String> whatshouldireadnext_com(String isbn){
    	//prüfe auf valide isbn
    	if(true) {
    		
    	}
    	return null;
    }
		
	public static ArrayList<String> buchZuAehnlicheBuecher(String title, String author, String url, int anz_ähnliche) throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
		ArrayList<String> results = new ArrayList<>();
		int groeße = 0;
		
		//finde link zu buch
		String link_book ="";
		if(url == "") {
			link_book = BuchIDZuURL(title+" "+author);
		}
		else {
			link_book = url;
		}
		//prüfe ob url legal
		if(!link_book.matches("https://www.goodreads.com/book/show/[0-9]+(\\.|-)(.)+?from_search=true") && !link_book.matches("https://www.goodreads.com/book/show/[0-9]+(\\.|-)(.)+")) {
			//System.err.println("link für Buch nicht gefunden!");
			return new ArrayList<>();
		}

		//öffne similar link
		org.jsoup.nodes.Document doc = Jsoup.connect(link_book).userAgent("bot101").get();
		String similar_link = doc.getElementsMatchingText("Readers Also Enjoyed").attr("href");
		//System.out.println("similar: "+similar_link);
		if(similar_link.equals(null) || !similar_link.matches("https://www.goodreads.com/book/similar/[0-9]+.+")) {
			//System.err.println("similar_link nicht gefunden! für"+link_book);
			//"versuche die liste mit diesem Buch"-Funktion von goodreads.com zu nutzen
//				Elements  a= doc.getElementsByClass("actionLink");
//				String url_more_lists="";
//				for(Element el: a) {
//					if(el.text().contains("lists")) {
//						url_more_lists = el.attr("href").replace(" ", "");
//					}
//				}
//				if(url_more_lists.matches("/list/book/[0-9]+")) {
//					url_more_lists = "https://www.goodreads.com"+url_more_lists;
//					System.out.println(url_more_lists);
//					
//					URL url1 = new URL(url_more_lists);   
//					HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
//					conn.setRequestMethod("GET");
//					conn.connect();
//					System.out.println(conn.getContentType());
//					
//					org.jsoup.nodes.Document  docG = Jsoup.connect(url_more_lists).userAgent("bot101").get();
					
//				}
			 return results; 
		}
		doc = Jsoup.connect(similar_link).userAgent("bot101").get();
	
		//sammle die ersten 10 bücher
		ArrayList<String> authors = new ArrayList<String>();
		ArrayList<String> titles = new ArrayList<String>();
		
		

		Elements similars_title= doc.select("tr").select("td").select("a[title]");
		Elements similars_autor= doc.getElementsByClass("authorName");
		
		ArrayList<Integer> uebersprungen = new ArrayList<Integer>();
		//FIXME: "Exception in thread "main" java.util.concurrent.ExecutionException: java.lang.IndexOutOfBoundsException: Index: 9, Size: 9" -> title oder autor nicht gefunden?
		for(Element s: similars_title) {
			if(groeße == anz_ähnliche)break;
			if(!s.attr("abs:href").contains("show")) {
				if(s.text().matches("[0-9] of [0-9] stars"))continue;
				//mglw.  aus https://www.goodreads.com/book/similar/2528139-shadow-of-the-hegemon# => https://www.goodreads.com/book/show/2528139-shadow-of-the-hegemon#
				System.err.println("für mindestens ein Element aus similar konnte kein titel gefunden werden! "+s.text());
				uebersprungen.add(groeße);
				continue;
			}
				//System.out.println(s.attr("title"));
			titles.add(s.attr("title"));
			groeße++;
		}		
		
		for(Element a: similars_autor) {
			if(groeße == 0)break;
			if(uebersprungen.contains(anz_ähnliche-groeße))continue;
				//System.out.println(a.text());
			if(a.text() != null)authors.add(a.text());
			else authors.add("unbekannt");
			groeße--;
		}
		
		if(authors.size() != titles.size())return results;
		
		for(int i=0; i<authors.size(); i++) {
			results.add(titles.get(i)+" von "+authors.get(i));
		}
		return results;
	}
	
	
	
	public void sortCharacter() {
		 Collections.sort(this.Characters);		
	}
	
	public void sortThemen() {
		 Collections.sort(this.shelves);		
	}
	
	public void sortAwards() {
		 Collections.sort(this.awards);		
	}
	
	public void sortSimilarBooks() {
		 Collections.sort(this.similar_Books);		
	}
	
	public void sortAuthors() {
		 Collections.sort(this.Author);		
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

