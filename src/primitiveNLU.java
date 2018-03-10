import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class primitiveNLU {

		
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		//System.out.println("return: "+searchForTitles_online("Der Name des Buchs ist Herr der Ringe"));
		System.out.println(searchForTitles_online("Ich brauche eine Buchempfehlung zu die ewige Geschichte"));
	}
	
	
	public static String searchForTitles_online(String input) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		System.out.println("wortsuche nach titel: "+input);
		ArrayList<String> suchurl = new ArrayList<>();
		ArrayList<String> suchterm = new ArrayList<>();
		String[] wörter = input.split(" ");
		for(int laenge = wörter.length ; laenge >= 0; laenge--) {
			for(int startpos=0; startpos+laenge <= wörter.length-1; startpos++) {
				String suche = "";
				for(int pos=startpos; pos<=startpos+laenge; pos++) {
					suche += wörter[pos];
					if(pos < startpos+laenge)suche+=" ";
				}
				suchterm.add(suche);
				suchurl.add("https://www.goodreads.com/search?q="+suche);
			}
		}
		ArrayList<String> results = searchThreading(suchurl, 20);
		for(int i=0; i<results.size(); i++) {
			if(results.get(i).length()==0)continue;
			String a =results.get(i).substring(results.get(i).indexOf("of")+3, results.get(i).lastIndexOf(" ")-1);
			if(!a.matches("[0-9]+"))continue;
			int res_nr = Integer.valueOf(a);
			System.out.println("max: "+a+" von "+results.get(i));
			if(res_nr > 10) return results.get(i).substring(results.get(i).indexOf("\"")+1,results.get(i).lastIndexOf("\""));
		}	
		return "";
	}
	
	private static String checkForSearchTerm(String url) throws IOException {
		System.out.println("suche: "+url);
		Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").referrer("http://www.google.com").timeout(20000).get();
    	if(doc.html().contains("No results"))return "";
    	else{
    		
    		return url+": "+doc.select("title").html();
    	}
	}
	
	private static class CheckURL implements Callable {
		 private String url;
		 public CheckURL(String url) throws UnsupportedEncodingException, IOException {
			 this.url = url; 
		 }
		 public String call() throws UnsupportedEncodingException, IOException, InterruptedException, ClassNotFoundException {
			 return checkForSearchTerm(url);
		 }
	 }
	
	private static ArrayList<String> searchThreading(ArrayList<String> urls, int threads) throws InterruptedException, ExecutionException, UnsupportedEncodingException, IOException {
	    ExecutorService pool = Executors.newFixedThreadPool(threads);
	    Set<Future<String>> set = new HashSet<Future<String>>();    	    
	    for (String url: urls) {
	    	Callable<String> b = new CheckURL(url);
	        Future<String> future = pool.submit(b);
	        set.add(future);
	    }
	    ArrayList<String> liste = new ArrayList<>();
	    for (Future<String> future : set) {
	    	liste.add(future.get());
	    }
	    pool.shutdownNow();
	    return liste;
 }
 
	
	public static String errateAutor(String input) throws FileNotFoundException, ClassNotFoundException, IOException {
		System.out.println("verzweifelte Suche nach autor: "+input);
				ArrayList<Buch> ergebnisse = new ArrayList<Buch>();
				String[] wörter = input.split(" ");
				for(int laenge = 3; laenge >= 0; laenge--) {
					for(int startpos=0; startpos+laenge <= wörter.length-1; startpos++) {
						String suche = "";
						for(int pos=startpos; pos<=startpos+laenge; pos++) {
							suche += wörter[pos];
							if(pos < startpos+laenge)suche+=" ";
						}
						System.out.println(suche);
						ArrayList<String> autor = new ArrayList<>(); autor.add(suche);
						ergebnisse = Datenbank.searchBook_author(autor);
						//System.out.println("Suche nach:" +suche+" "+ergebnisse.size());
						if(ergebnisse.size() > 0)return suche;
					}
				}
				return "";
	}
	
	public static String errateTitel(String input) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException {
		//System.out.println("verzweifelte Suche nach title: "+input);
		ArrayList<Buch> ergebnisse = new ArrayList<Buch>();
		String[] wörter = input.split(" ");
		for(int laenge = wörter.length-2; laenge >= 1; laenge--) {
			for(int startpos=0; startpos+laenge <= wörter.length-1; startpos++) {
				String suche = "";
				for(int pos=startpos; pos<=startpos+laenge; pos++) {
					suche += wörter[pos];
					if(pos < startpos+laenge)suche+=" ";
				}
				ergebnisse = Datenbank.searchBook_title(suche);
				//System.out.println("Suche nach:" +suche+" "+ergebnisse.size());
				if(ergebnisse.size() > 0)return suche;
			}
		}
		return "";
	}
}
