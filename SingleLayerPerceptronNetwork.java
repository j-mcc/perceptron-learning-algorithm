import java.util.Arrays;
import java.util.Random;

public class SingleLayerPerceptronNetwork implements Comparable<SingleLayerPerceptronNetwork>{
	
	public static final int FORWARD = 0; //train network by iterating over the training set 0->n
	public static final int BACKWARD = 1; //train network by iterating over the training set n->0
	
	private double[] weights;
	private double[] initialWeights;
	private int numberOfWeightChanges;
	private DataSet trainingData;
	private DataSet testData;
	private double stepSize;
	private int maxNumberOfIterations;
	private int numberOfIterations;
	private double errorRate;
	private double testingErrorRate;
	private int trainingDirection;
	

	
	public SingleLayerPerceptronNetwork(DataSet trainingData, DataSet testData, int maxNumberOfIterations, double stepSize, int trainingDirection){
		this.trainingData = trainingData;
		this.testData = testData;
		this.maxNumberOfIterations = maxNumberOfIterations;
		this.stepSize = stepSize;
		this.errorRate = 0;
		this.trainingDirection = trainingDirection;
		this.weights = new double[trainingData.getAttributeNames().length];  //attributes include each datapoint + the classification; we need 1 weight for each datapoint + the bais weight
		this.initialWeights = new double[trainingData.getAttributeNames().length];
		randomizeWeights(weights);
		for(int i = 0; i < this.weights.length; i++) this.initialWeights[i] = this.weights[i];
	}
	
	public SingleLayerPerceptronNetwork(DataSet trainingData, DataSet testData, int maxNumberOfIterations, double stepSize, double[] startingWeights, int trainingDirection){
		this.trainingData = trainingData;
		this.testData = testData;
		this.maxNumberOfIterations = maxNumberOfIterations;
		this.stepSize = stepSize;
		this.errorRate = 0;
		this.trainingDirection = trainingDirection;
		this.weights = Arrays.copyOfRange(startingWeights, 0, startingWeights.length);
		this.initialWeights = Arrays.copyOfRange(startingWeights, 0, startingWeights.length);
	}
	
	public void trainNetwork(){  
		numberOfIterations = 1;
		
		
		for(; numberOfIterations <= maxNumberOfIterations; numberOfIterations++){
			int numberMisclassified = 0;
			
			if(trainingDirection == FORWARD){
				for(Record record : trainingData){
					//				System.out.println("Analyzing Record: " + record.toString());
					int result = queryNetwork(record.getValues());
					//				System.out.println("...........Result = " + result + "\n");
					if(result != record.getClassification()){
						numberMisclassified++;
						updateWeights(record);
					}
				}
			}
			else if(trainingDirection == BACKWARD){
				for(int i = trainingData.getRecords().size() - 1; i >= 0; i--){
//					System.out.println("Analyzing Record: " + record.toString());
						int result = queryNetwork(trainingData.getRecords().get(i).getValues());
						//				System.out.println("...........Result = " + result + "\n");
						if(result != trainingData.getRecords().get(i).getClassification()){
							numberMisclassified++;
							updateWeights(trainingData.getRecords().get(i));
						}
				}
			}
			errorRate = ((double)numberMisclassified/(double)trainingData.getRecords().size());
//			System.out.println("Epoch " + numberOfIterations + " -- Error Rate --> " + errorRate + "%\n");
			if(numberMisclassified == 0){
				break;
			}
		}
	}
	
	private void updateWeights(Record record){
		for(int i = 0; i < weights.length; i++){
			if(i == 0) weights[i] = weights[i] + stepSize * record.getClassification();
			else weights[i] = weights[i] + stepSize * record.getClassification() * record.getValues()[i-1];
		}
		numberOfWeightChanges++;
	}
	
	public void testNetwork(){
		int numberOfWrongClassifications = 0;
		for(Record record : testData.getRecords()){
			int result = queryNetwork(record.getValues());
			if(result != record.getClassification()) numberOfWrongClassifications++;
		}
		testingErrorRate = ((double)numberOfWrongClassifications/(double)testData.getRecords().size());
	}
	
	public int queryNetwork(Double[] query){
		double summation = 0;
		for(int i = 0; i < weights.length; i++){
			if(i == 0) summation += weights[i];
			else summation += weights[i] * query[i-1];
		}
		if(summation >= 0) return 1;
		else return -1;
	}
	
	public double[] getNetworkWeights(){
		return weights;
	}
	
	public int getNumberOfWeightChanges(){
		return numberOfWeightChanges;
	}
	
	public int getNumberOfEpochs(){
		return numberOfIterations;
	}
	
	public double getStepSize(){
		return stepSize;
	}
	
	public double getErrorRate(){
		return errorRate;
	}
	
	public double getTestingErrorRate(){
		return testingErrorRate;
	}
	
	public String toString(){
		StringBuilder printString = new StringBuilder();
		printString.append("\nNetwork Info:\n");
		printString.append("\tTesting Error Rate: " + testingErrorRate + "%\n");
		printString.append("\tTraining Error Rate: " + errorRate + "%\n");
		if(trainingDirection == BACKWARD) printString.append("\tTraining Direction: N -> 0 (backward)\n");
		else if(trainingDirection == FORWARD) printString.append("\tTraining Direction: 0 -> N (forward)\n");
		printString.append("\tNumber of Updates: " + numberOfWeightChanges + "\n");
		printString.append("\tNumber of Epochs: " + numberOfIterations + "\n");
		printString.append("\tStep Size: " + stepSize + "\n");
		printString.append("\tSlope: " + (-(weights[0]/weights[2])/(weights[0]/weights[1])) + "\n");
		printString.append("\tY-Intercept: " + (-weights[0]/weights[2]) + "\n");
		
		printString.append("\n\tInitial Weights:");
		for(int i = 0; i < initialWeights.length; i++){
			if(i == 0)
				printString.append("\tBias: " + initialWeights[i]);
			else
				printString.append("\t" + trainingData.getAttributeNames()[i-1] + ": " + initialWeights[i]);
		}
		
		printString.append("\n\tFinal Weights:");
		for(int i = 0; i < weights.length; i++){
			if(i == 0)
				printString.append("\t\tBias: " + weights[i]);
			else
				printString.append("\t" + trainingData.getAttributeNames()[i-1] + ": " + weights[i]);
		}
		return printString.toString();
	}
	
	public static double[] createWeights(int size){
		double[] weights = new double[size];
		randomizeWeights(weights);
		return weights;
	}
	
	private static void randomizeWeights(double[] weights){
		Random random = new Random();
		for(int i = 0; i < weights.length; i++) weights[i] = random.nextGaussian();
	}

	@Override
	public int compareTo(SingleLayerPerceptronNetwork slpn) {
		if(errorRate == slpn.getErrorRate()){
			if(testingErrorRate == slpn.getTestingErrorRate()){
				int cost = numberOfIterations*trainingData.getRecords().size() + numberOfWeightChanges*weights.length;
				int cost2 = slpn.getNumberOfEpochs()*trainingData.getRecords().size() + slpn.getNumberOfWeightChanges()*weights.length;
				if(cost == cost2) return 0;
				else if(cost < cost2) return -1;
				else return 1;
			} 
			else if(testingErrorRate < slpn.getTestingErrorRate()){
				return -1;
			}
			else return 1;
		} else if(errorRate < slpn.getErrorRate()){
			if(testingErrorRate == slpn.getTestingErrorRate()){
				int cost = numberOfIterations*trainingData.getRecords().size() + numberOfWeightChanges*weights.length;
				int cost2 = slpn.getNumberOfEpochs()*trainingData.getRecords().size() + slpn.getNumberOfWeightChanges()*weights.length;
				if(cost == cost2) return 0;
				else if(cost < cost2) return -1;
				else return 1;
			}
			else if(testingErrorRate < slpn.getTestingErrorRate()){
				return -1;
			}
			else return 1;
		} else {
			if(testingErrorRate == slpn.getTestingErrorRate()){
				int cost = numberOfIterations*trainingData.getRecords().size() + numberOfWeightChanges*weights.length;
				int cost2 = slpn.getNumberOfEpochs()*trainingData.getRecords().size() + slpn.getNumberOfWeightChanges()*weights.length;
				if(cost == cost2) return 0;
				else if(cost < cost2) return -1;
				else return 1;
			}
			else if(testingErrorRate < slpn.getTestingErrorRate()){
				return -1;
			}
			else return 1;
		}
	}
	
}
