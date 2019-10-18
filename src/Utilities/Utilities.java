package Utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utilities {
	public static String readArch(String ruta) { // Lee el archivo y retorna
													// su contenido en un String
		File file = new File(ruta);
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String list = "";

		while (input.hasNextLine()) {
			String aux = input.nextLine();
		    list = list + aux;
		}
		if (!input.hasNextLine())
			input.close();
		return list;
	}
	
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json != JSONObject.NULL) {
	        retMap = JArraytoMap(json);
	    }
	    return retMap;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> JArraytoMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = JSONArraytoList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = JArraytoMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> JSONArraytoList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = JSONArraytoList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = JArraytoMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	public static void createTxt(String f, String ruta) {
		try {
			File file = new File(ruta);
			file.getParentFile().mkdirs(); 
			BufferedWriter salida = new BufferedWriter(new FileWriter(ruta));
			salida.write(f);
			salida.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}