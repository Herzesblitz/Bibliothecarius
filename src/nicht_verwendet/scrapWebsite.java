package nicht_verwendet;
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
	
	
	/* Funktionen der Klasse:
	 *  Vorschlaege fuer Bucher in Bezug: Daten(Buch)  
	 *  	Ahnlichkeiten in Bezug: Daten Buch ()
	 *  		Daten(Buch): TItle, Autor, Genre, Thema, Charaktere, Plot, Rating, Erscheinungsjahr(meinstens bei publisher), Publisher, ISBN
	 * 		Variation in Bezug: Daten(Buch)
	 * 
	 * 		Vielleicht: billigster Preis fuer Buch (Quelle)
	 */

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {



		int pagenummer=3151;//next
		int tmpmax=3200;
		int max=3349;
		int index=25;
		int stop=0;
		while (tmpmax<=3349&&stop<2 ) {
			fetchCharacterList(index,pagenummer,tmpmax );
			tmpmax+=50;
			pagenummer+=50;
			index++;
			if(tmpmax>3349){ tmpmax=3349; stop++;}
		}
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
	
	public static void fetchCharacterList(int index, int pagenummer,int tmpmax) throws IOException {
		//anzahl pages 
			Elements elements = Jsoup.connect("https://www.goodreads.com/characters").get().select("a");
			int max=0;
			for(Element el: elements) {
			 if(isInteger(el.text(),10) && max < Integer.parseInt(el.text()))max = Integer.parseInt(el.text());
			}
		String 	x=""; 
			
		//durch pages gehen
		for(int page=pagenummer; page<=tmpmax; page++) {
			System.out.println(page+ "|| "+ tmpmax);
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
		    BufferedWriter out = new BufferedWriter(new FileWriter("./src/source/characters"+index+".txt"));
		    out.write(x);  //Replace with the string 
		                                             //you are trying to write
		    out.close();
		}
		catch (IOException e)
		{
		    System.out.println("Exception ");

		}
	}
	
	
	

}
