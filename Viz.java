import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
public class Viz {
	public static void main(String[] args) {
	}

	// reads a CSV stream
	private static ArrayList<Map<String, String>> readCSV(InputStream csv) throws IOException {
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(csv));
		String[] headerLine = br.readLine().split(","); // reads the header of the file
		String[] line = br.readLine().split(","); // reads the first line of data
		while (line != null) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < line.length; i++) {
				map.put(headerLine[i], line[i]); // maps the header with corresponding value
			}
			al.add(map); // adds the map to the ArrayList
			line = br.readLine().split(",");
		}
		br.close();
		return al;
	}
}
