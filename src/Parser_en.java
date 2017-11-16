import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import opennlp.tools.cmdline.parser.ParserTool; 
import opennlp.tools.parser.Parse; 
import opennlp.tools.parser.Parser; 
import opennlp.tools.parser.ParserFactory; 
import opennlp.tools.parser.ParserModel;  

public class Parser_en { 
	
   ArrayList<String> words = new ArrayList<String>();
   
   public static void main(String args[]) throws Exception{  
	   			printParseTree(parse("I would like to buy it now ."));
   }
   
   public static void printParseTree(Parse[] Parse){
	 for(Parse P:Parse)P.show();;
	  
   }
   
   public static Parse[] parse(String eingabe) throws IOException{
			//System.out.println(System.getProperty("user.dir"));

	   //Laedt das Parser model aus FileInputStream
	   InputStream inputStream = new FileInputStream("C:\\Users\\Devin\\OneDrive\\Cloud\\Programme\\Projekte\\java\\DrThesaurus\\src\\source\\en-parser-chunking.bin");
	   ParserModel model = new ParserModel(inputStream); 

	   //erstellt parser aus Modell
		Parser parser = ParserFactory.create(model); 
		
		//Parsed den Satz
		String sentence = eingabe;
		Parse topParses[] = ParserTool.parseLine(sentence, parser, 1); 
		
//		for (Parse p : topParses){ 
//		 	pars.words.add(p.getLabel());
//			p.show();  
//			System.out.println("");
//		} 
//		
//		for(String k: pars.words)System.out.println(k);
		
		return topParses;
		
	}
   
 
}