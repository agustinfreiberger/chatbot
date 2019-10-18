package Utilities;

import java.util.Formatter;

public class FormateadorSubString extends Formateador {

	Formatter fm = new Formatter();

	int li, ls;

	public FormateadorSubString(String config, String attribute) {
		this.attribute = attribute;
		String buffer = "";
		boolean limitSet = false;
		for (int i = 0; i < config.length(); i++) {
			if (config.charAt(i) != ';') {
				buffer = buffer + config.charAt(i);
			} else {
				if (text == null) {
					text = buffer;
					buffer = "";
				} else {
					if (!limitSet) {
						li = Integer.getInteger(buffer);
						limitSet = true;
						buffer = "";
					} else {
						ls = Integer.getInteger(buffer);
					}
				}
			}
		}
	}

	public String formatear(String data) {
		fm.format(text, data.substring(li, data.length()-ls));
		last = fm.toString();
		return last;
	}

	@Override
	public String getText() {
		return last;
	}
}
