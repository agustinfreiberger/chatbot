package Utilities;

public abstract class Formateador {
	String text;
	String last;
	String attribute;
	public abstract String getText();
	public abstract String formatear(String data);
	public String getAtt(){
		return attribute;
	}
}
