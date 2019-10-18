package Query;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

import Utilities.Utilities;

	

public class Indexador {
	
	private static StandardAnalyzer sA;
	private static Directory directory ;
	private static IndexWriterConfig config;
	private static FileReader fr;
	private static HashMap<Integer, String> champsTranslator;
	private String[] configuration;
	
	@SuppressWarnings("deprecation")
	public Indexador() throws IOException {
		configuration = Utilities.readArch("config.txt").split("@");             //Leemos la configuraci�n
		CharArraySet stopSet = CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
		stopSet.add("el");
	    stopSet.add("la");
	    stopSet.add("las");
	    stopSet.add('?');
	    stopSet.add("de");
	    stopSet.add("dame");
		Indexador.sA = new StandardAnalyzer(stopSet);
		Indexador.directory = new SimpleFSDirectory(Paths.get("Index"));
		Indexador.config = new IndexWriterConfig(sA);
		Indexador.champsTranslator = new HashMap<Integer, String>();
		try {
			ChampSetter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Devuelve el traductor
	public HashMap<Integer, String> getTranslator() {
		return champsTranslator;
	};
	
	//Obtiene el ID de los campeones segun su nombre
	public Integer getChamp(String champ) {		
		    for (Entry<Integer, String> entry : champsTranslator.entrySet()) {
		        if (champ.equals(entry.getValue())) {
		            return entry.getKey();
		        }
		    }
		    return null;
		}
	
	//Obtiene los nombres de los campeones segun su ID
	public static String getChampsTranslator(int id) {
		return champsTranslator.get(id);
	}
	
	public String getConfig(int pos){
		return configuration[pos];
		
	}
	
	//Genera el traductor (ID - Nombre de campeón)
	@SuppressWarnings("unchecked")
	private static void ChampSetter() throws Exception {

	     String url = "http://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-summary.json";
		 URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     con.setRequestMethod("GET");
	     con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     String entrada = response.toString();
	     JSONArray array = new JSONArray(entrada); //Creo estructura de la consulta
	     
	     List<Object> DataBase = Utilities.JSONArraytoList(array); //Convierto a Lista de Maps
	     
	     for (Object j : DataBase){
	    	 Integer id = (Integer) ((HashMap<String, Object>) j).get("id");
	    	 String name = (String) ((HashMap<String, Object>) j).get("name");
	    	 champsTranslator.put(id, name.toLowerCase());
	     }
	   }
	
	//Obtiene los nombres de items segun su ID
	public static String itemGetter(String itemId) throws Exception{
		String url = "http://ddragon.leagueoflegends.com/cdn/7.8.1/data/es_ES/item.json";
		 URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     con.setRequestMethod("GET");
	     con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     String entrada = response.toString();
	     
	    
	     String output = "";
	     
	     JSONObject asd = new JSONObject(entrada);
	     JSONObject data = (JSONObject) asd.get("data");
	     JSONObject prueba = (JSONObject) data.get(itemId);
	     output = prueba.get("name").toString();
	     
	     return output;
	    
	}
	//Crea el indice de campeones con sus datos
	public void createIndex() throws Exception {
		
		
		IndexWriter indexWriter = new IndexWriter(directory, config);
		File dir = new File( new File("").getAbsoluteFile() + "/Database/" );
		File[] files = dir.listFiles();
		for (File file : files) {
			Document document = new Document();
			String path = file.getCanonicalPath();
			document.add(new TextField("path", path, Field.Store.YES));
			Reader reader = new FileReader(file);
			String AuxName = file.getName();
			document.add(new TextField(AuxName.substring(0, AuxName.length()-4), reader));
			indexWriter.addDocument(document);
		}
		indexWriter.close();
	}
	
	//Busca el archivo donde está el dato
	public String searchIndex(String searchString, String content) throws IOException, ParseException {
		String path = "";
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		QueryParser queryParser = new QueryParser(content, sA);
		Query query = queryParser.parse(searchString);
		TopDocs hits = indexSearcher.search(query, 5);

		for (int i = 0; i<hits.scoreDocs.length; i++) {
			int documentId = hits.scoreDocs[i].doc;
			Document d = indexSearcher.doc(documentId);
			path = d.get("path");
		}
		return path;
	}
	
	/*Busca el dato en 
	el archivo */
		
	public String simpleDataGetter(String file, String dato) throws Exception {
		String data = "";
		String line;
		fr = new FileReader(file);
		int inicio = dato.length()+2;
		BufferedReader br = new BufferedReader(fr);
		while ((line = br.readLine()) != null) {
		       if (line.contains(dato)) {
		    	   data = line.substring(inicio, line.length()-1);
		       }
		    }
		br.close();
		return data;
	}

	public void clear() {
		File dir = new File( new File("").getAbsoluteFile() + "/Database/" );
		String[]entries = dir.list();
		for(String s: entries){
		    File currentFile = new File(dir.getPath(),s);
		    currentFile.delete();
		}
		dir.delete();
		
		File dir2 = new File( new File("").getAbsoluteFile() + "/Index/" );
		String[]entries2 = dir2.list();
		for(String s: entries2){
		    File currentFile = new File(dir2.getPath(),s);
		    currentFile.delete();
		}
		dir2.delete();
	}
	
}


