package Utilities;

import java.util.Formatter;

public class FormateadorSimple extends Formateador {
	Formatter fm = new Formatter();


	public FormateadorSimple(String config, String attribute) {
		this.attribute = attribute;
		String buffer = "";
		for (int i = 0; i < config.length(); i++) {
			if (config.charAt(i) != ';') {
				buffer = buffer + config.charAt(i);
			} else {
				text = buffer;
			}
		}
	}

	public String formatear(String data) {
		fm.format(text, data);
		last = fm.toString();
		return last;
	}

	@Override
	public String getText() {
		return last;
	}
}
