package dna.gds.test;

import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.cost.CostFunction;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.DataType;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.count.OperationCount.Operation;

public class TestFunctionReading {

	public static void main(String[] args) {
		CostFunction f1 = new CostFunctionFitted(FitType.MedSD, DataType.Edge,
				DHashMap.class, Operation.INIT, new int[] { 100, 1000, 10000,
						50000, 100000 });
	}

}
