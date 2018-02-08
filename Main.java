
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
	
	private static int maxNumberOfEpochs = 10000;
	private static double stepSize = .025;
	private static int iterationsOfWeightLoop = 2;
	
	
	public static void main(String[] args) {
		FileReader trainDataFile;
		FileReader testDataFile;
		List<SingleLayerPerceptronNetwork> perceptronNetworks = new ArrayList<>();
		
		Scanner scanner = new Scanner(System.in);
		
		do {
			System.out.println("Input path to training data file.");
			try {
				trainDataFile = new FileReader(new File(scanner.nextLine()));
				break;
			} catch (FileNotFoundException fnfe) {
				System.out.println("File Not Found.");
				continue;
			}
		} while (true);
		
		do {
			System.out.println("Input path to testing data file.");
			try {
				testDataFile = new FileReader(new File(scanner.nextLine()));
				break;
			} catch (FileNotFoundException fnfe) {
				System.out.println("File Not Found.");
				continue;
			}
		} while (true);
		
		scanner.close();
		
		DataSet trainingData = new DataSet("TRAINING", trainDataFile);
		try {
			trainingData.parseCSV();
			System.err.println(trainingData.toString());
		} catch (IOException e) {
			System.err.println("Problem Parsing Training Data.");
		}
		
		DataSet testData = new DataSet("TESTING", testDataFile);
		try {
			testData.parseCSV();
			System.err.println(testData.toString());
		} catch (IOException e) {
			System.err.println("Problem Parsing Test Data.");
		}
		
		for(int newWeightsLoop = 0; newWeightsLoop < iterationsOfWeightLoop; newWeightsLoop++){
			double [] weights = SingleLayerPerceptronNetwork.createWeights(trainingData.getAttributeNames().length); 
			
			for(int i = 1; i < 3; i++){
				SingleLayerPerceptronNetwork singleLayerPerceptronNetworkForward = new SingleLayerPerceptronNetwork(trainingData, testData, maxNumberOfEpochs, stepSize*i*5, weights, SingleLayerPerceptronNetwork.FORWARD);
				singleLayerPerceptronNetworkForward.trainNetwork();
				singleLayerPerceptronNetworkForward.testNetwork();
				perceptronNetworks.add(singleLayerPerceptronNetworkForward);
				System.out.println(singleLayerPerceptronNetworkForward.toString());
				
				SingleLayerPerceptronNetwork singleLayerPerceptronNetworkBackward = new SingleLayerPerceptronNetwork(trainingData, testData, maxNumberOfEpochs, stepSize*i*5, weights, SingleLayerPerceptronNetwork.BACKWARD);
				singleLayerPerceptronNetworkBackward.trainNetwork();
				singleLayerPerceptronNetworkBackward.testNetwork();
				perceptronNetworks.add(singleLayerPerceptronNetworkBackward);
				System.out.println(singleLayerPerceptronNetworkBackward.toString());
				
				
				
//				if(i == 0) perceptronNetworks.add(singleLayerPerceptronNetwork);
//				else if(perceptronNetworks.get(newWeightsLoop).compareTo(singleLayerPerceptronNetwork) >= 0) perceptronNetworks.set(newWeightsLoop, singleLayerPerceptronNetwork);
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		}
		
//		for(SingleLayerPerceptronNetwork singleLayerPerceptronNetwork : perceptronNetworks){
//			System.out.println(singleLayerPerceptronNetwork.toString());
//		}
	}
}
