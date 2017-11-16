import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import opennlp.tools.*;
import opennlp.tools.tokenize.lang.en.TokenSampleStream;
import edu.berkeley.nlp.PCFGLA.BerkeleyParser;


public class Steuerung {

	Interpreter interpreter=new Interpreter();
	DialogManager dialogManager=new DialogManager();
	ResponseGenerator responseGenerator=new ResponseGenerator();

	public static void main(String[] args) throws IOException {
		
		//opennlp.tools.cmdline.parser  parser;
		//Parser_en.printParseTree(Parser_en.parse("Mike and Smith are playing with Sarah"));





		// TODO Auto-generated method stub
		try {
			dialog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void dialog() throws IOException
	{



		while(true){
			// ("Hallo, ich bin Johannes.");
		}
	

		
		
		
	}

}
