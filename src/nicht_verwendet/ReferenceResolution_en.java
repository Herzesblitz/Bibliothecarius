package nicht_verwendet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceResolution_en {
	
	static String last1person = "User"; static String[] firstpersonRef = {"I", "me", "my", "mine", "myself"};
	static String last2person = "DrThesaurus";  static String[] secondpersonRef = {"you", "your", "yours", "yourself"};
	static String last3personMale; static String[] thirdMalepersonRef = {"he", "him", "his", "hisself"};
	static String last3personFemale;	static  String[] thirdFemalepersonRef ={"she", "her", "hers", "herself"};
	static String last3PersonNeutral;  static String[] thirdPersonNeutral = {"it", "its","itself"};
	
	/*
	static ArrayList<String>last5Person; static String[] fifthpersonRef = secondpersonRef;
	static ArrayList<String>last6Person;static  String[] sixtpersonRef = {"they", "them", "their", "theirs", "themselves"};
	*/
	

	public static void main(String[] args) {
		
	}

	 public static void test(){
		   String s = new String("Str87uyuy232");
		   Matcher matcher = Pattern.compile("\\d+").matcher(s);
		   matcher.find();
		   int i = Integer.valueOf(matcher.group());
		   System.out.println("test");
		   System.out.println(i);
		   
	}
	 
	private static void refreshIdentityRefs(String eingabe) throws IOException{
		//3personFemale
			for(String word: TokenizerMaximumEntropy.tokenize(eingabe)){
				if(genderFinder.getGender(word) == "female")last3personFemale = word;
			}
		//3personMale
			for(String word: TokenizerMaximumEntropy.tokenize(eingabe)){
				if(genderFinder.getGender(word) == "male")last3personMale = word;
			}

		//4person
			for(String word: TokenizerMaximumEntropy.tokenize(eingabe)){
				if(genderFinder.getGender(word) == "male" &&  genderFinder.getGender(word) == "female")continue;
				
			}
	}
	
	public static void detect1pIdentity(String eingabe) throws IOException{
		for(String fpR: firstpersonRef)	 eingabe = eingabe.replace(fpR, last1person);
		for(String spR: secondpersonRef) eingabe = eingabe.replace(spR, last2person);

		for(String tFpR: thirdFemalepersonRef)	 eingabe = eingabe.replace(tFpR, last3personFemale);
		for(String tMpR: thirdMalepersonRef) eingabe = eingabe.replace(tMpR, last3personMale);
		for(String fopR: thirdPersonNeutral)	 eingabe = eingabe.replace(fopR, last3PersonNeutral);
		
		//plural faelle abfragen
	}
	
	
	/**
	 * dissolves Anaphors by exchanging Anaphoras with the expression they represent
	 * @param eingabe 
	 */
	public static void Anaphora(String eingabe){
		//1. detect Refphora AND 2. replace Refphora
			//1. person
			for(String fpR: firstpersonRef){
				if(eingabe.contains(fpR))eingabe.replaceAll(fpR, last1person);
			}
			//2. person
			for(String spR: secondpersonRef){
				if(eingabe.contains(spR))eingabe.replaceAll(spR, last2person);
			}
			//3. person male
			for(String tmpR: thirdMalepersonRef ){
				if(eingabe.contains(tmpR))eingabe.replaceAll(tmpR, last3personMale);
			}
			//3. person female
			for(String tfpR: thirdFemalepersonRef ){
				if(eingabe.contains(tfpR))eingabe.replaceAll(tfpR, last3personFemale);
			}
			//3. person
			for(String fopR: thirdPersonNeutral){
				if(eingabe.contains(fopR))eingabe.replaceAll(fopR, last3PersonNeutral);
			}
	}
	
	

}
