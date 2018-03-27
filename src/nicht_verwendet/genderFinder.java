package nicht_verwendet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.Gson;

public class genderFinder {
	
	static String maleNames= "";
	static String femaleNames ="";

	// Service URL
	private static final String SERVICE_URL = "https://gender-api.com/get?";
	
	// Your Secret Key provided by Service Provider
	private static final String SECRET_KEY = "21323";
	
	public static Result getGenderType_genderize(String name) {
		
		Result result = null;
		
		// Gson API for JSON to Object Conversion in Java
		Gson gson = new Gson();
		
		if(name != null && !name.isEmpty()) {
			
			try {
				
				// Preparing Request URL
				final String requestURL = SERVICE_URL + "name="+name + "&key="+ SECRET_KEY;
				
				URL url = new URL(requestURL);
				
				HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
				
				// Getting the InputStream from URL
				InputStream inputStream =  httpURLConnection.getInputStream();
				
				String response = "";
				
				// Reading the Response
				if(inputStream != null) {
					
					int data = inputStream.read();
					
					while(data != -1) {
						
						response = response + (char)data;
						
						data = inputStream.read();
					}
				}
				
				// Closing the Resource
				inputStream.close();
				
					//System.out.println("Response from (Gender-API Service) :  "+response);
				
				// Converting JSON to Result Object
				result = gson.fromJson(response, Result.class);
				
			} catch (MalformedURLException e) {
				
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static String getGender_genderize(String name){
		Result result = getGenderType_genderize(name);
		if(result != null) {		
			return result.getGender();
		} else {	
			System.out.println("No response from service...");
		}
		return "";
	}
	
	private static void loadTextfiles(){
		//textfileTOString - male
		try {
			 maleNames = readFile("C:\\Users\\Devin\\OneDrive\\Cloud\\Programme\\Projekte\\java\\DrThesaurus\\src\\source\\census-dist-male-first.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	//textfileTOString - female
		try {
			 femaleNames = readFile("C:\\Users\\Devin\\OneDrive\\Cloud\\Programme\\Projekte\\java\\DrThesaurus\\src\\source\\census-dist-female-first.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
	
	public static String getGender_file(String name){
		String toCapitals = name.toUpperCase();
		if(maleNames =="" || femaleNames =="")loadTextfiles();
		if(maleNames.contains(toCapitals))return"male";
		if(femaleNames.contains(toCapitals))return"female";
		else return "unknown";		
	}

	public static String getGender(String name){
		String output = "";
		//file
			output = getGender_file(name);
			if(output != "unknown")return output;
		//1 service
			//output =getGender_genderize(name);
		    //if(output != "unknown")return output;

		return "unknown";
	}
	
	//TODO: groeßere Liste (mehr Namen)
	public static void main(String[] args) {
		System.out.println(getGender("John"));
	}
	

	
}


class Result {

	private String name;
	private String gender;
	private String samples;
	private String accuracy;
	private String duration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSamples() {
		return samples;
	}

	public void setSamples(String samples) {
		this.samples = samples;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}