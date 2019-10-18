package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	public FileManager() {

	}

	public static String Read(File f) throws IOException {
		FileInputStream in = new FileInputStream(f);
		byte[] fuente = new byte[in.available()];
		in.read(fuente);
		in.close();
		return new String(fuente);
	}

	public void Write(String entry, File f) throws IOException {
		f.delete();
		BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
		writer.write(entry);
		writer.close();
	}
}
