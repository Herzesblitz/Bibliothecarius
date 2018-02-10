import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME; 
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;  

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class primitiveNLU {

		
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		//System.out.println("return: "+searchForTitles_online("Der Name des Buchs ist Herr der Ringe"));
		System.out.println(namen_finden_POS_Tagger("Guten Tag, kannst du mir ein gutes Buch suchen, das ich gerne lesen soll, des wurde von Harald Blauzahn geschrieben."));;
		System.out.println(namen_finden_NER("Guten Tag, kannst du mir ein gutes Buch suchen, das ich gerne lesen soll, des wurde von Harald Blauzahn geschrieben."));;

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
	
	
	public static String randomGreet() {
		ArrayList<String> pool = new ArrayList<String>();
			pool.add("Hallo, mein Name ist Bibliothecarius!");
			pool.add("Hallo, ich bin Bibliothecarius!");
			pool.add("Grüße, mein Name ist Bibliothecarius!");
			pool.add("Grüße, mein Name ist Bibliothecarius. Wie kann ich dir helfen?");
		return pool.get((int) (Math.random()*pool.size()));
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
	
	public static String namen_finden_POS_Tagger(String eingabe) throws IOException{
		String author="";
		
		//Loading Parts of speech-maxent model       
		InputStream inputStream = new FileInputStream("./src/source/de-pos-maxent.bin");
	    POSModel model = new POSModel(inputStream); 
	     
	    //Instantiating POSTaggerME class 
	    POSTaggerME tagger = new POSTaggerME(model); 
	     
	    String sentence = eingabe; 
	     
	    //Tokenizing the sentence using WhitespaceTokenizer class  
	    WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
	    String[] tokens = whitespaceTokenizer.tokenize(sentence); 
	    
	    
	     
	    //Generating tags 
	    String[] tags = tagger.tag(tokens);
	    	    
	    //Instantiating the POSSample class 
	    POSSample sample = new POSSample(tokens, tags); 
	    
	    for(String satzteile: sample.toString().split(" ")) {
	    	if(satzteile.endsWith("_NE")) {
	    		author+= satzteile.replaceAll("_NE", "").replace(".", "")+" ";
	    	}
	    }
	    
	     return author;  

	}

	
	
	public static String namen_finden_NER(String eingabe) throws IOException{
		   InputStream inputStream = new FileInputStream("./src/source/en-ner-person.bin"); 
		      TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
		      
		      //Instantiating the NameFinder class 
		      NameFinderME nameFinder = new NameFinderME(model); 
		    
		      //Getting the sentence in the form of String array  
		      String [] sentence = eingabe.split(" ");     
		       
//		      sentence= new String[]{ 
//		         "Mike", 
//		         "and", 
//		         "Smith", 
//		         "are", 
//		         "good", 
//		         "friends" 
//		      }; 
		      
		      //Finding the names in the sentence 
		      Span nameSpans[] = nameFinder.find(sentence); 
		      if(nameSpans.length == 0) {
		    	  return "";
		      }
		       
		      //Printing the spans of the names in the sentence 
		      String[] nameLocs = new String[nameSpans.length];
		      
		      int ret[] = new int[nameLocs.length];
		      
		      int f=0;
		      for(Span s: nameSpans){ 
		    	  String k = s.toString();
		    	  Matcher matcher = Pattern.compile("\\d+").matcher(k);
				  matcher.find();
				  ret[f] = Integer.valueOf(matcher.group()); 
		    	  f++;
		      }
		      
		      String ausgabe = "";
		      for(int pos: ret)ausgabe+=sentence[pos]+" ";
		      return ausgabe;
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
