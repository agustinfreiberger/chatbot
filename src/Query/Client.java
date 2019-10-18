package Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.lucene.queryparser.ext.Extensions.Pair;

import Utilities.Formateador;
import Utilities.FormateadorSimple;
import Utilities.FormateadorSubString;

public class Client {

	private String actualFile;
	private Integer actualChampId;
	private String actualChamp;
	private Indexador ind;
	private HashMap<String, Formateador> queryData;

	// Inicializador
	public Client(Indexador i) {
		this.ind = i;
		queryData = new HashMap<String, Formateador>();
		this.actualFile = "";
		this.actualChampId = 0;
		queryDataArraySetter();
	}

	// Palabras clave soportadas por el chatbot
	private void queryDataArraySetter() {
		
		Formateador kills = new FormateadorSimple(ind.getConfig(0), "kills");
		Formateador role = new FormateadorSubString(ind.getConfig(1), "role");
		Formateador winrate = new FormateadorSimple(ind.getConfig(2), "winrate");
		Formateador wins = new FormateadorSubString(ind.getConfig(3), "wins");
		Formateador items = new FormateadorSubString(ind.getConfig(4), "items");
		Formateador skills = new FormateadorSubString(ind.getConfig(5), "skills");
		Formateador juegosjugados = new FormateadorSimple(ind.getConfig(6), "juegosjugados");
		Formateador playRate = new FormateadorSimple(ind.getConfig(7), "playRate");

		queryData.put("kills", kills);
		queryData.put("role", role);
		queryData.put("winrate", winrate);
		queryData.put("wins", wins);
		queryData.put("items", items);
		queryData.put("skill", skills);
		queryData.put("juegos jugados", juegosjugados);
		queryData.put("playRate", playRate);
	}

	// Método para interaccionar con el usuario
	public String talk(String input) throws Exception {
		String data = "";
		String response = "";
		Collection<String> champs = ind.getTranslator().values();
		ArrayList<Formateador> queryArray = new ArrayList<>();

		while (input.charAt(input.length() - 1) == '!' || input.charAt(input.length() - 1) == '.'
				|| input.charAt(input.length() - 1) == '?') {
			input = input.substring(0, input.length() - 1);
		}

		input.toLowerCase();
		input.trim();
		String[] arrayWords = input.split(" "); // separo las palabras
		for (String word : arrayWords) {
			if (champs.contains(word)) {
				actualChamp = word; //////////////
				actualChampId = ind.getChamp(word);
			} else if (queryData.keySet().contains(word)) {
				queryArray.add(queryData.get(word)); // agrego el formateador de
														// la consulta
			}
		}
		
		if (!queryArray.isEmpty()) {
			response = query(queryArray);
		} else if (input.contains("hola")) {
			response = "Hola! Que deseas saber?";
		} else {
			response = "No entiendo ¿qué quieres decir?";
		}

		return response;
	}

	// Método buscador de datos requeridos
	public String query(ArrayList<Formateador> queryArray) throws Exception {
		String output = "Para " + actualChamp;
		for (Formateador fm : queryArray) {
			this.actualFile = ind.searchIndex(fm.getAtt(), actualChampId.toString());
			output = output + " " + fm.formatear(ind.simpleDataGetter(actualFile, fm.getAtt()));
		}
		return output;
	}

}
