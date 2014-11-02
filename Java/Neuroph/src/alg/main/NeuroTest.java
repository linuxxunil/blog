package alg.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.LinkedList;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample shows how to create, train, save and load simple Multi Layer
 * Perceptron
 */
public class NeuroTest {
	private static String nnDir = "NNET/";
	public static void learning(String nnFile, String trainingFile,int nInput, int nHidden,int nOutput,double maxError) {
		DataSet trainingSet = 
				DataSet.createFromFile(trainingFile, nInput, nOutput, ",", false);
		
		// create multi layer perceptron
		MultiLayerPerceptron ml = new MultiLayerPerceptron(
				TransferFunctionType.SIGMOID, nInput, nHidden, nOutput);
		
		final BackPropagation bp = new BackPropagation();
		bp.setMaxError(maxError);
		bp.setBatchMode(true);
		// learn the training set
		bp.addListener(new LearningEventListener(){

			@Override
			public void handleLearningEvent(LearningEvent event) {
				// TODO Auto-generated method stub
				System.out.println(bp.getTotalNetworkError());
				
			}
			
		});
		ml.learn(trainingSet,bp);

		
		ml.save(nnDir+nnFile);
	}

	public static void testing(String nnFile, String testingFile,int nInput,int nOutput) {
		DataSet testingSet = 
				DataSet.createFromFile(testingFile, nInput, nOutput, ",", false);
		NeuralNetwork nn = NeuralNetwork.createFromFile(nnDir+nnFile);
		testNeuralNetwork(nn, testingSet);
	}
	
	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

		for (DataSetRow dataRow : testSet.getRows()) {

			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));

		}
	}

	private static void writeLine(String file,String str) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.newLine();
			
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// nothing
			}
		}
	}
	
	private static double[] getWeight(String nnName) {
		FileReader fr = null;
		BufferedReader br = null;
		double[] weight = null;
		LinkedList<Double> list = new LinkedList();
		try {
			fr = new FileReader(nnName+".wei");
			br = new BufferedReader(fr);
			
			String tmp="";
			while((tmp=br.readLine())!=null){
				list.add(Double.valueOf(tmp));
			}
			
			weight = new double[list.size()];
			
			for(int i=0; i<list.size(); i++)
				weight[i] = list.get(i);
			
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// nothing
			}
		}
		return  weight;
	}

	public static void writeWeightToTxt(String nnName) {
		
		NeuralNetwork nn = NeuralNetwork
				.createFromFile(nnDir+nnName+".nnet");
		
		Double[] w = nn.getWeights();
		for(int i=0; i<w.length; i++) {
			writeLine("Weight/"+nnName+".wei",String.valueOf(w[i]));
			System.out.println(w[i]);
		}
	}

	public static void bpnn() {
		
		learning("NN00.nnet", "training00.txt",3,26,11,0.01);
		testing("NN00.nnet", "testing00.txt",3,11);
	}
	

	public static void main(String[] args) {

				
		bpnn();
		//for (int i=1; i<4 ;i++) {
		//	writeWeightToTxt(String.format("NN%02d", i));
		//}
	}

}