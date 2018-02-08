

public class Record {
	
	private DataSet parentDataSet;
	private int recordNumber;
	private Double[] values;
	private int classification;
	
	public Record(DataSet parentSet, int recordNumber, String[] values){
		this.parentDataSet = parentSet;
		this.recordNumber = recordNumber;
		this.values = new Double[values.length-1];
		for(int i = 0; i < values.length-1; i++)
			this.values[i] = Double.parseDouble(values[i]);
		classification = Integer.parseInt(values[values.length-1]);
	}
	
	public Double[] getValues(){
		return values;
	}
	
	public int getClassification() {
		return classification;
	}
	
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Record #" + recordNumber + " :: Classification: " + classification);
		stringBuilder.append("  \tValues:");
		for(int i = 0; i < values.length; i++) stringBuilder.append("\t" + parentDataSet.getAttributeNames()[i] + " :: " + values[i] + " ");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}
