package dna.gds.analysis;

import argList.ArgList;
import argList.types.array.IntArrayArg;
import argList.types.array.StringArrayArg;
import argList.types.atomic.EnumArg;
import argList.types.atomic.StringArg;
import dna.graph.datastructures.IDataStructure;
import dna.graph.datastructures.config.DSConfig;
import dna.graph.datastructures.config.DSConfigUndirected;
import dna.graph.datastructures.cost.CostEstimation;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.OperationCount.AggregationType;
import dna.graph.datastructures.count.OperationCounts;

public class StaticEstimation extends StaticCostFunctions {

	public String countsDir;
	public String countsFilename;

	public AggregationType aggregationType;
	public FitType fitType;
	public int[] fitSizes;

	public DSConfig dsc;

	@SuppressWarnings("unchecked")
	public StaticEstimation(String countsDir, String countsFilename,
			String aggregationType, String fitType, Integer[] fitSizes,
			String[] gdsType) throws ClassNotFoundException {
		this.countsDir = countsDir;
		this.countsFilename = countsFilename;
		this.aggregationType = AggregationType.valueOf(aggregationType);

		this.fitType = FitType.valueOf(fitType);
		this.fitSizes = new int[fitSizes.length];
		for (int i = 0; i < fitSizes.length; i++) {
			this.fitSizes[i] = fitSizes[i];
		}

		this.dsc = new DSConfigUndirected(
				(Class<? extends IDataStructure>) Class.forName("dna.graph.datastructures."
						+ gdsType[0]),
				(Class<? extends IDataStructure>) Class
						.forName("dna.graph.datastructures." + gdsType[1]),
				(Class<? extends IDataStructure>) Class
						.forName("dna.graph.datastructures." + gdsType[2]));

		CostFunctionFitted.defaultFitType = this.fitType;
		CostFunctionFitted.defaultSizes = this.fitSizes;
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticEstimation> argList = new ArgList<StaticEstimation>(
				StaticEstimation.class, new StringArg("countsDir",
						"dir where the operation counts are stored"),
				new StringArg("countsFilename",
						"filename where the operation counts are stored"),
				new EnumArg("aggregationType",
						"aggr type for the operation counts", AggregationType
								.values()), new EnumArg("fitType",
						"data type how function are fitted", FitType.values()),
				new IntArrayArg("fitSizes", "sizes for fitted functions", ","),
				new StringArrayArg("gdsType", "gds config (V, E, adj)", gdsSep));

		// args = new String[] {
		// "/Users/benni/TUD/Projects/DNA/DNA.gdsAnalysis/deploy/data/counts/FB-100-100/",
		// "1-,2-,3-,4-,5-", "-", "AVG", "MedSD",
		// "100,1000,10000,50000,100000,1000000",
		// "DLinkedList--DHashTable--DArrayList" };

		StaticEstimation se = argList.getInstance(args);
		se.execute();
	}

	public void execute() throws Exception {
		CostFunctionsSMap map = this.getMap();
		OperationCounts oc = CountsIO.read(countsDir,
				new String[] { countsFilename },
				new String[] { countsFilename }, aggregationType);
		double estimation = CostEstimation.estimateCosts(dsc, oc, map);
		System.out.println(estimation);
	}

}
