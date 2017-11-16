import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScrapWebsite {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
//
		autorBücher("Tolkien");
		//ähnlicheBücher("Metro 2033");
		//goodReads_search_title("Metro 2033", 5);
	}
	
	
	
	public static ArrayList<String> infosBücher(String titel){
		return null;
	}
		
	public static ArrayList<String> autorBücher(String autor) throws UnsupportedEncodingException, IOException{
		ArrayList<String> results = new ArrayList<>();
		//link für autor finden
			String search_1 = "https://www.goodreads.com/search?page=1&query="+autor+"&tab=books&utf8=%E2%9C%93";
			Document doc =  Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
				//System.out.println(doc.html());
				//TODO: funzt whrsch nicht immer
			String linkAutor = doc.html().substring(doc.html().indexOf("www.goodreads.com/author/show/")+30, doc.html().indexOf("\"", doc.html().indexOf("/www.goodreads.com/author/show/")));
				//System.out.println(linkAutor);
		
		doc = Jsoup.connect("https://www.goodreads.com/author/list/"+linkAutor).userAgent("bot101").get();
		org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
		
		String link="";
		for (Element result_doc : results_doc) {
			if(!result_doc.text().contains("—"))break; //TODO: dirty fix
			
			//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
			if(result_doc.html().contains("href")) link = "\n link:"+result_doc.html().substring(result_doc.html().indexOf("href")+6, result_doc.html().indexOf(">", result_doc.html().indexOf("href"))-1);
		    results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—")+1)+link);
		}
		for(String s: results)System.out.println(s);
		return results;
	}
	
	public static ArrayList<String> ähnlicheBücher(String title) throws UnsupportedEncodingException, IOException{
		ArrayList<String> results = new ArrayList<>();

		//finde link zu buch ~ rufe dafür 1 ergebnis 
			String search_1 = "https://www.goodreads.com/search?page=1&query="+title+"&tab=books&utf8=%E2%9C%93";
			Document doc =  Jsoup.connect(search_1 + URLEncoder.encode(search_1, "UTF-8")).get();
			org.jsoup.select.Elements first = doc.getElementsByTag("tr");
				//System.out.println(first.html());
			String link= "https://www.goodreads.com."+first.html().substring(first.html().indexOf("href")+6, first.html().indexOf(">", first.html().indexOf("href"))-1);
				//System.out.println(link);
			doc = Jsoup.connect(link + URLEncoder.encode(search_1, "UTF-8")).userAgent("bot101").get();
				//TODO: funzt u.u. nicht immer
			String linktext_schlecht = doc.html().substring(doc.html().indexOf("/trivia/work/")+13,doc.html().indexOf("\"", doc.html().indexOf("/trivia/work/"))); //System.out.println("link: "+linktext_ann1);
						
		//similar aufrufen
			String similar = "https://www.goodreads.com/book/similar/"+linktext_schlecht;
			System.out.println(similar);

			doc =  Jsoup.connect(similar).get();
			System.out.println(doc.html());
			
		//liste der ähnlichen bücher sammeln
			org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
			for (Element result_doc : results_doc) {
				//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
				results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—"))+"\n link:"+result_doc.html().substring(result_doc.html().indexOf("href")+6, result_doc.html().indexOf(">", result_doc.html().indexOf("href"))-1));
			}		
			for(String s: results)System.out.println(s);
			
			return results;
	}
	
	/**
	 * searches for title 
	 * @param search
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static ArrayList<String> goodReads_search_title(String search, int maxresultpages) throws UnsupportedEncodingException, IOException{
		String search_2 = "https://www.goodreads.com/search?page=1&query="+search+"&tab=books&utf8=%E2%9C%93";
		String search_1 = "https://www.goodreads.com/search?query="+search;
		
		System.out.println(search_1);
		
		//find number of result pages
			Document doc =  Jsoup.connect(search_2).userAgent("bot101").get();
			String title ="";
			for(Element meta : doc.select("meta")) if(meta.attr("name").contains("title"))title = meta.attr("content"); //search for meta attr that contains title
			if(title.length() > 0 && title.contains("showing") && title.contains("of"))title = title.substring(title.indexOf("showing")+10, title.indexOf("of", title.indexOf("showing"))-1);
			int pages_number = Math.min(maxresultpages, Integer.parseInt(title));
					
		//go through result pages
			ArrayList<String> results = new ArrayList<>();
			
			for(int page = 1; page < pages_number; page++){
				String url =  "https://www.goodreads.com/search?page="+page+"&query="+search+"&tab=books&utf8=%E2%9C%93";
				doc =  Jsoup.connect(url).userAgent("usrdasf").get();
	       
				//TODO: selektion des docs präzisieren um 
				org.jsoup.select.Elements results_doc = doc.getElementsByTag("tr");
				for (Element result_doc : results_doc) {
					//TODO: link-teil furchtbar, auswahl über html string. gibt es möglichtkeit das <href>-tag des <title> tag auszuwählen??
					results.add(result_doc.text().substring(0, result_doc.text().lastIndexOf("—"))+"\n link:"+result_doc.html().substring(result_doc.html().indexOf("href")+6, result_doc.html().indexOf(">", result_doc.html().indexOf("href"))-1));
				}
				System.out.println(page+"/"+pages_number);
			}
			
			for(String s: results)System.out.println(s);
		return results;
	}
	
	
	//TODO: charakter gegeben -> welches Buch?
	//TODO: Bücher für Themem
	public static ArrayList<String> ThemaZuBücherliste(String thema){
		return null;
	}
}
