package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;


public class DBGenerator {

	//Genera la base de datos desde la cual se genera el indice.
	@SuppressWarnings("unchecked")
	public DBGenerator(String elo) throws Exception 
		{
			 String apiKey = "ec545d402ebd648449b6cf282cf288fb"; 
			 //'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'PLATINUM,DIAMOND,MASTER,CHALLENGER'
			 String parameters = "kda,hashes";
		     String url = "http://api.champion.gg/v2/champions?elo=" + elo + "&champData=" + parameters +"&limit=200&api_key=" + apiKey;
			 URL obj = new URL(url);
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		     con.setRequestMethod("GET");
		     con.setRequestProperty("User-Agent", "Mozilla/5.0");
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine + '\n');
		     }
		     String entrada = response.toString();
		     JSONArray array = new JSONArray(entrada); //Creo estructura de la consulta
		     
		     List<Object> DataBase = Utilities.JSONArraytoList(array); //Convierto a Lista de Maps
		     
		     for (Object j : DataBase){
		    	 Integer aux = (Integer) ((HashMap<String, Object>) j).get("championId");
		    	 Utilities.createTxt(j.toString().replace("," , "," + System.lineSeparator()), new File("").getAbsoluteFile() + "/Database/" + aux.toString() + ".txt");
		     }
		   }
}
