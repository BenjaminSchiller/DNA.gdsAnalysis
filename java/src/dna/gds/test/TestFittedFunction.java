package dna.gds.test;

import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.DataType;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.count.OperationCount.Operation;

public class TestFittedFunction {

	public static void main(String[] args) {
		int[] sizes = new int[] { 100, 1000, 10000, 50000, 100000 };
		CostFunctionFitted f1 = new CostFunctionFitted(FitType.AvgSD,
				DataType.Node, DArrayList.class, Operation.ADD_SUCCESS, sizes);
		System.out.println(f1.getCost(20));
		System.out.println(f1.getCost(200));
		System.out.println(f1.getCost(2000));
		System.out.println(f1.getCost(20000));
		System.out.println(f1.getCost(200000));
		System.out.println(f1.getCost(2000000));
	}

}
