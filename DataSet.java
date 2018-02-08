import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSet implements Iterable<Record>{
	
	private String name;
	private FileReader dataFile;
	private String[] attributeNames; 
	private List<Record> records;
	
	public DataSet(String name, FileReader dataFile){
		this.name = name;
		this.dataFile = dataFile;
		records = new ArrayList<>();
	}
	
	public void parseCSV() throws IOException{
		String seperator = ",";
		BufferedReader bufferedReader = new BufferedReader(dataFile);
		
		attributeNames = bufferedReader.readLine().split(seperator);
		
		int recordNumber = 1;
		
		String[] values;
		String line;
		while((line = bufferedReader.readLine()) != null){
			values = line.split(seperator);
			records.add(new Record(this, recordNumber, values));
			recordNumber++;
		}
		bufferedReader.close();
	}

	@Override
	public Iterator<Record> iterator() {
		return records.iterator();
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public List<Record> getRecords() {
		return records;
	}

	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("--" + name + " DATA SET--\n");
		for(Record record : records){
			stringBuilder.append(record.toString());
		}
		return stringBuilder.toString();
	}
}
