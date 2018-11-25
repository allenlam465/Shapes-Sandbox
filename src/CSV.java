import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSV {
	
	private static final String HEADER = "Shape, ";

	private String outputFile;
	
	public CSV(String file) throws IOException {
		
		outputFile = file + ".csv";
		
		FileWriter fstream = new FileWriter(outputFile);
		BufferedWriter ostream = new BufferedWriter(fstream);
		
		ostream.write(HEADER);
		
		ostream.close();	
	}
	
	public void write(String input) throws IOException {
		
		FileWriter fstream = new FileWriter(outputFile, true);
		BufferedWriter ostream = new BufferedWriter(fstream);
		
		ostream.write("\n" + input);
		
		ostream.close();		
	}

}