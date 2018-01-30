package source;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream; 
import opennlp.tools.tokenize.TokenizerME; 
import opennlp.tools.tokenize.TokenizerModel;  

public class TokenizerMaximumEntropy { 
  
   public static void main(String args[]) throws Exception{     
	   for(String s: tokenize("Hi. How are you? Welcome to Tutorialspoint. We provide free tutorials on various technologies"))System.out.println(s);
   }
   
   public static String[] tokenize(String eingabe) throws IOException{
	      //Loading the Tokenizer model 
		   InputStream inputStream = new FileInputStream("./src/source/en-token.bin");
		   TokenizerModel tokenModel = new TokenizerModel(inputStream); 
		    
		   //Instantiating the TokenizerME class 
		   TokenizerME tokenizer = new TokenizerME(tokenModel); 
		    
		   //Tokenizing the given raw text 
		   String tokens[] = tokenizer.tokenize(eingabe);       
		       
//		   //Printing the tokens  
//		   for (String a : tokens) 
//		      System.out.println(a);
		   return tokens;
   }
  
} 