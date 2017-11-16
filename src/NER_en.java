import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME; 
import opennlp.tools.namefind.TokenNameFinderModel; 
import opennlp.tools.util.Span;  

public class NER_en  { 
   public static void main(String args[]) throws Exception{ 
      //Loading the NER - Person model       
      for(int s: namen_finden("bed")) System.out.println(s);
      
   }
   
   public static void test(){
	  
   }
   
  
   
   public static int[] namen_finden(String eingabe) throws IOException{
	   InputStream inputStream = new FileInputStream("sources/en-ner-person.bin"); 
	      TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
	      
	      //Instantiating the NameFinder class 
	      NameFinderME nameFinder = new NameFinderME(model); 
	    
	      //Getting the sentence in the form of String array  
	      String [] sentence = eingabe.split(" ");
//	      new String[]{ 
//	         "Mike", 
//	         "and", 
//	         "Smith", 
//	         "are", 
//	         "good", 
//	         "friends" 
//	      }; 
	      
	      
	       
	      //Finding the names in the sentence 
	      Span nameSpans[] = nameFinder.find(sentence); 
	      if(nameSpans.length == 0)return null;
	       
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
	      return ret;
   }
   
}