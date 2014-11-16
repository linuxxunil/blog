package alg;

import java.io.IOException;
import libsvm.*;

public class SVMTest {
	private String modelFile = "xor.model";
	
	
	// Generate XOR for training 
	private svm_problem initTrainingDataSet() {
		svm_problem svmProb = new svm_problem();
		svmProb.l = 4;						// number of training data
		svmProb.y = new double[svmProb.l];	// an array containing their target values
	    
		
		svmProb.x = new svm_node[svmProb.l][2];
		
		for ( int j=0; j<svmProb.l; j++) 	
			for ( int i=0; i<2; i++ )
				svmProb.x[j][i] = new svm_node(); 
		
		// XOR(0,0) = 0
		svmProb.x[0][0].value =-1; svmProb.x[0][0].index = 1; 
		svmProb.x[0][1].value =-1; svmProb.x[0][1].index = 2; 
		svmProb.y[0] = 0;
		
		// XOR(0,1) = 1
		svmProb.x[1][0].value =-1; svmProb.x[1][0].index = 1; 
		svmProb.x[1][1].value = 1; svmProb.x[1][1].index = 2; 
		svmProb.y[1] = 1;
		
		// XOR(1,0) = 1
		svmProb.x[2][0].value = 1; svmProb.x[2][0].index = 1; 
		svmProb.x[2][1].value =-1; svmProb.x[2][1].index = 2; 
		svmProb.y[2] = 1;
		
		// XOR(1,1) = 0
		svmProb.x[3][0].value = 1; svmProb.x[3][0].index = 1; 
		svmProb.x[3][1].value = 1; svmProb.x[3][1].index = 2; 
		svmProb.y[3] = 0;
		
		return svmProb;
	}
	
	// Generate XOR for testing
	private svm_problem initTestingDataSet() {
		svm_problem svmProb = new svm_problem();
		svmProb.l = 4;						// number of training data
		svmProb.y = new double[svmProb.l];	// an array containing their target values
	    
		
		svmProb.x = new svm_node[svmProb.l][2];
		for ( int j=0; j<svmProb.l; j++) 	
			for ( int i=0; i<2; i++ )
				svmProb.x[j][i] = new svm_node(); 
		
		// XOR(0,1) = 0
		svmProb.x[0][0].value =-1; svmProb.x[0][0].index = 1; 
		svmProb.x[0][1].value = 1; svmProb.x[0][1].index = 2; 
		svmProb.y[0] = 1;
		
		// XOR(1,0) = 1
		svmProb.x[1][0].value = 1; svmProb.x[1][0].index = 1; 
		svmProb.x[1][1].value =-1; svmProb.x[1][1].index = 2; 
		svmProb.y[1] = 1;
		
		// XOR(0,0) = 1
		svmProb.x[2][0].value =-1; svmProb.x[2][0].index = 1; 
		svmProb.x[2][1].value =-1; svmProb.x[2][1].index = 2; 
		svmProb.y[2] = 0;
		
		// XOR(1,1) = 0
		svmProb.x[3][0].value = 1; svmProb.x[3][0].index = 1; 
		svmProb.x[3][1].value = 1; svmProb.x[3][1].index = 2; 
		svmProb.y[3] = 0;
		
		return svmProb;
	}
	

	public void training() {
		try {
			svm_parameter svmParm = initParmeter();
			svm_problem svmProb = initTrainingDataSet();
			svm_model model = svm.svm_train(svmProb, svmParm);
			
			svm.svm_save_model(modelFile, model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testing() {
		int correct = 0, total = 0;
		try {
			svm_problem svmProb = initTestingDataSet();
			svm_model model = svm.svm_load_model(modelFile);
			
			for(int i=0;i<svmProb.l;i++){
				double v;
				svm_node[] x = svmProb.x[i];
			
				v = svm.svm_predict(model, x);
				
				System.out.println(String.format("XOR(%d,%d)=%d", 
						(x[0].value==-1)?0:1,
						(x[1].value==-1)?0:1,
						(int)v));
				
				total++;
				if(v == svmProb.y[i]) correct++;
			}
			
			double accuracy = (double)correct/total*100;
			System.out.println("Accuracy = "+accuracy+"% ("+correct+"/"+total+")");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public svm_parameter initParmeter() {
		svm_parameter svmParm = new svm_parameter();

		svmParm.svm_type = svm_parameter.C_SVC;
		svmParm.kernel_type = svm_parameter.RBF;
		svmParm.degree = 3;
		svmParm.gamma = 1; 
		svmParm.coef0 = 0;
		svmParm.nu = 0.5;
		svmParm.cache_size = 100;
		svmParm.C = 1;
		svmParm.eps = 1e-3;
		svmParm.p = 0.1;
		svmParm.shrinking = 1;
		svmParm.probability = 0;
		svmParm.nr_weight = 0;
		svmParm.weight_label = new int[0];
		svmParm.weight = new double[0];
		return svmParm;
	}

	public static void main(String[] args) {
		SVMTest svmTest = new SVMTest();
		
		System.out.println("##### Start Training #####");
		svmTest.training();
		
		System.out.println("##### Start Testing  #####");
		svmTest.testing();
	}
}
