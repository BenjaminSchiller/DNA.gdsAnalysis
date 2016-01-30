package dna.gds.analysis;

import java.io.IOException;

import argList.ArgList;
import argList.types.array.IntArrayArg;
import argList.types.array.StringArrayArg;
import argList.types.atomic.EnumArg;
import argList.types.atomic.StringArg;
import dna.graph.datastructures.DArray;
import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.DHashArrayList;
import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.DHashSet;
import dna.graph.datastructures.DHashTable;
import dna.graph.datastructures.DLinkedList;
import dna.graph.datastructures.IDataStructure;
import dna.graph.datastructures.config.DSConfig;
import dna.graph.datastructures.config.DSConfigUndirected;
import dna.graph.datastructures.cost.CostEstimation;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.cost.CostFunctionsS;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.OperationCount.AggregationType;
import dna.graph.datastructures.count.OperationCounts;
import dna.graph.edges.DirectedEdge;
import dna.graph.nodes.DirectedNode;

public class StaticEstimationOld {

	public static final String gdsSep = "--";

	public String dir;
	public String[] prefixes;
	public String[] suffixes;

	public AggregationType aggregationType;

	public FitType fitType;
	public int[] fitSizes;

	public DSConfig dsc;

	public StaticEstimationOld(String dir, String[] prefixes, String[] suffixes,
			String aggregationType, String fitType, Integer[] fitSizes) {
		this.dir = dir;
		this.prefixes = prefixes;
		this.suffixes = suffixes;

		this.aggregationType = AggregationType.valueOf(aggregationType);

		this.fitType = FitType.valueOf(fitType);
		this.fitSizes = new int[fitSizes.length];
		for (int i = 0; i < fitSizes.length; i++) {
			this.fitSizes[i] = fitSizes[i];
		}

		CostFunctionFitted.defaultFitType = this.fitType;
		CostFunctionFitted.defaultSizes = this.fitSizes;
	}

	@SuppressWarnings("unchecked")
	public StaticEstimationOld(String dir, String[] prefixes, String[] suffixes,
			String aggregationType, String fitType, Integer[] fitSizes,
			String[] gdsType) throws ClassNotFoundException {
		this(dir, prefixes, suffixes, aggregationType, fitType, fitSizes);
		this.dsc = new DSConfigUndirected(
				(Class<? extends IDataStructure>) Class.forName("dna.graph.datastructures."
						+ gdsType[0]),
				(Class<? extends IDataStructure>) Class
						.forName("dna.graph.datastructures." + gdsType[1]),
				(Class<? extends IDataStructure>) Class
						.forName("dna.graph.datastructures." + gdsType[2]));
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticEstimationOld> argList = new ArgList<StaticEstimationOld>(
				StaticEstimationOld.class,
				new StringArg("dir",
						"dir where the operation counts are stored"),
				new StringArrayArg(
						"prefixes",
						"prefixes of the files that should be read "
								+ "(e.g., 1-, 2-ba, 2-bg, 2-DegreeDistributionR) "
								+ "- for no prefix check", ","),
				new StringArrayArg(
						"suffixes",
						"suffixes of the files that should be read "
								+ "(e.g., gg - bg - ba - R) without the .counts at the end "
								+ "- for no suffix check", ","), new EnumArg(
						"aggregationType",
						"aggr type for the operation counts",
						AggregationType.values()), new EnumArg("fitType",
						"data type how function are fitted", FitType.values()),
				new IntArrayArg("fitSizes", "sizes for fitted functions", ","),
				new StringArrayArg("gdsType", "gds config (V, E, adj)", gdsSep));

		// args = new String[] {
		// "/Users/benni/TUD/Projects/DNA/DNA.gdsAnalysis/deploy/data/counts/FB-100-100/",
		// "1-,2-,3-,4-,5-", "-", "AVG", "MedSD",
		// "100,1000,10000,50000,100000,1000000",
		// "DLinkedList--DHashTable--DArrayList" };

		StaticEstimationOld sr = argList.getInstance(args);
		sr.execute();
	}

	public void execute() throws Exception {
		CostFunctionsSMap map = this.getMap();
		OperationCounts oc = CountsIO.read(dir, prefixes, suffixes,
				aggregationType);
		double estimation = CostEstimation.estimateCosts(dsc, oc, map);
		System.out.println(estimation);
	}

	protected CostFunctionsSMap getMap() throws NumberFormatException,
			IOException {
		CostFunctionsSMap map = new CostFunctionsSMap();
		map.add(CostFunctionsS.read("", DArray.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DArrayList.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashSet.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashMap.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashTable.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DLinkedList.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashArrayList.class,
				DirectedNode.class, DirectedEdge.class));

		return map;
	}

}
