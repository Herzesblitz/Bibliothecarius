package source;
import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import opennlp.tools.postag.POSModel; 
import opennlp.tools.postag.POSSample; 
import opennlp.tools.postag.POSTaggerME; 
import opennlp.tools.tokenize.WhitespaceTokenizer;  

public class POS_Tagger_de { 
	
	
  
   public static void main(String args[]) throws Exception{ 
	   System.out.println(getTaggedSentence("Das Buch Herr der Ringe"));
      
   } 
   
   public static String getFULLTags(String eingabe) throws IOException{
	    Map<String,String>  rules = new HashMap<>();
		rules.put("ADJA","attributivesAdjektiv");
		rules.put("ADJD","adverbialesoderpr�dikativesAdjektiv");
		rules.put("ADV","Adverb,schon,bald,doch");	
		rules.put("APPR","Pr�position;Zirkumpositionlinks");
		rules.put("APPRART","Pr�positionmitArtikel");
		rules.put("APPO","Postposition");
		rules.put("APZR","Zirkumpositionrechts");
		rules.put("ART","bestimmteroderunbestimmterArtikel");
		rules.put("CARD","Kardinalzahl");
		rules.put("FM","FremdsprachlichesMaterial");
		rules.put("ITJ","Interjektion,mhm,ach,tja");
		rules.put("KOUI","unterordnendeKonjunktionmit``zu''undInfinitiv");
		rules.put("KOUS","unterordnendeKonjunktionmitSatz");
		rules.put("KON","nebenordnendeKonjunktion");
		rules.put("KOKOM","Vergleichskonjunktion,als,wie");
		rules.put("NN","normalesNomen");
		rules.put("NE","Eigennamen");
		rules.put("PDS","substituierendesDemonstrativpronomen");
		rules.put("PDAT","attribuierendesDemonstrativpronomen");
		rules.put("PIS","substituierendesIndefinitpronomen");
		rules.put("PIAT","attribuierendesIndefinitpronomenohneDeterminer");
		rules.put("PIDAT","attribuierendesIndefinitpronomenmitDeterminer");
		rules.put("PPER","irreflexivesPersonalpronomen");
		rules.put("PPOSS","substituierendesPossessivpronomen");
		rules.put("PPOSAT","attribuierendesPossessivpronomen");
		rules.put("PRELS","substituierendesRelativpronomen");
		rules.put("PRELAT","attribuierendesRelativpronomen");
		rules.put("PRF","reflexivesPersonalpronomen");
		rules.put("PWS","substituierendesInterrogativpronomen");
		rules.put("PWAT","attribuierendesInterrogativpronomen");
		rules.put("PWAV","adverbialesInterrogativ-oderRelativpronomen");
		rules.put("PAV","Pronominaladverb");
		rules.put("PTKZU","``zu''vorInfinitiv");
		rules.put("PTKNEG","Negationspartikel");
		rules.put("PTKVZ","abgetrennterVerbzusatz");
		rules.put("PTKANT","Antwortpartikel");
		rules.put("PTKA","PartikelbeiAdjektivoderAdverb");
		rules.put("TRUNC","Kompositions-Erstglied");
		rules.put("VVFIN","finitesVerb,voll");
		rules.put("VVIMP","Imperativ,voll");
		rules.put("VVINF","Infinitiv,voll");
		rules.put("VVIZU","Infinitivmit``zu''");	
		rules.put("VVPP","PartizipPerfekt");
		rules.put("VAFIN","finitesVerb,aux");
		rules.put("VAIMP","Imperativ,aux");
		rules.put("VAINF","Infinitiv,aux");
		rules.put("VAPP","PartizipPerfekt,aux");
		rules.put("VMFIN","finitesVerb,modal");
		rules.put("VMINF","Infinitiv,modal");
		rules.put("VMPP","PartizipPerfekt,modal");
		rules.put("XY","Nichtwort,Sonderzeichenenthaltend");
		rules.put("$,","Komma");
		rules.put("$.","SatzbeendendeInterpunktion");
		rules.put("$(","sonstigeSatzzeichen");
	    	//Finde subtext zum ersetzen
	    					    	
	    			Set<String> keyset = rules.keySet();	
	    			
	    			for(String rule: keyset){
	    				if(eingabe.contains(rule)) eingabe = eingabe.replace(rule, rules.get(rule));
	    			}

	    			//if(rules.containsKey(substring)) eingabe.replace(substring, rules.get(substring));
	    		
	    	
	    		
	    	return eingabe;	             
	    }
   
   
   public static String getTaggedSentence(String eingabe) throws IOException{
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
	
	     return sample.toString();  
   }
}  