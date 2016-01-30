package dna.gds.analysis;

import java.io.IOException;
import java.util.ArrayList;

import argList.ArgList;
import argList.types.array.EnumArrayArg;
import argList.types.array.IntArrayArg;
import argList.types.atomic.EnumArg;
import argList.types.atomic.IntArg;
import argList.types.atomic.StringArg;
import dna.gds.util.CountEstimation;
import dna.graph.Graph;
import dna.graph.datastructures.DArray;
import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.DHashArrayList;
import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.DHashSet;
import dna.graph.datastructures.DHashTable;
import dna.graph.datastructures.DLinkedList;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.datastructures.config.DSConfig;
import dna.graph.datastructures.config.DSConfigDirected;
import dna.graph.datastructures.config.DSConfigUndirected;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.cost.CostFunctionsS;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.Counting;
import dna.graph.datastructures.count.OperationCounts;
import dna.graph.edges.DirectedEdge;
import dna.graph.generators.GraphGenerator;
import dna.graph.nodes.DirectedNode;
import dna.metrics.Metric;
import dna.metrics.algorithms.IRecomputation;
import dna.profiler.Profiler;
import dna.updates.batch.Batch;
import dna.updates.batch.BatchSanitization;
import dna.updates.generators.BatchGenerator;
import dna.util.Config;
import dna.util.fromArgs.MetricFromArgs.MetricType;

public class StaticX_OLD extends Static {

	public static final String dir = "staticOptimization/";

	public static final String measurements = "measurements/";

	public static ArrayList<DSConfig> dscD;
	public static ArrayList<DSConfig> dscU;
	public static CostFunctionsSMap map;

	public String dataDir;

	public FitType fitType;
	public int[] fitSizes;

	public StaticX_OLD(String datasetName, String datasetDir,
			String graphFilename, String batchSuffix, String[] metricTypes,
			Integer batches, String dataDir, String fitType, Integer[] fitSizes) {
		super(datasetName, datasetDir, graphFilename, batchSuffix, metricTypes,
				batches);

		this.dataDir = dataDir;

		this.fitType = FitType.valueOf(fitType);
		this.fitSizes = new int[fitSizes.length];
		for (int i = 0; i < fitSizes.length; i++) {
			this.fitSizes[i] = fitSizes[i];
		}
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticX_OLD> argList = new ArgList<StaticX_OLD>(
				StaticX_OLD.class, new StringArg("datasetName",
						"name of the dataset"), new StringArg("datasetDir",
						"dir where the dataset is stored"), new StringArg(
						"graphFilename", "e.g., 0.dnag"), new StringArg(
						"batchSuffix", "e.g., .dnab"), new EnumArrayArg(
						"metricTypes", "metrics that should be computed", ",",
						MetricType.values()), new IntArg("batches",
						"number of batches"), new StringArg("dataDir",
						"where to store computed results"), new EnumArg(
						"fitType", "data type how function are fitted",
						FitType.values()), new IntArrayArg("fitSizes",
						"sizes for fitted functions", ","));

		String dataset = "/Users/benni/TUD/datasets/dynamic/md/pnB_th_7_short/";
		// dataset =
		// "/Users/benni/TUD/Projects/DNA/DNA.datasets/examples/random-undirected/";

		String metrics = "DegreeDistributionR,UndirectedClusteringCoefficientR,UnweightedAllPairsShortestPathsR";
		metrics = "UnweightedAllPairsShortestPathsR";
		// metrics = "DegreeDistributionR";
		// metrics = "UndirectedClusteringCoefficientR";

		args = new String[] { "theName", dataset, "0.dnag", ".dnab", metrics,
				"20", "data/test/1/", "MedSD", "100,1000,10000,50000,100000" };

		StaticX_OLD sr = argList.getInstance(args);
		sr.execute();
	}

	public void execute() throws Exception {
		init();
		CostFunctionFitted.defaultFitType = this.fitType;
		CostFunctionFitted.defaultSizes = this.fitSizes;
		OperationCounts[] oc = this.count();

		DSConfig best = CountEstimation.estimateBestConfig(oc[1], map);
		System.out.println(best);

		oc[0].writeValues("data/test/oc1/", "filename.txt");
		oc[0].writeValues("data/test/oc2/");

		// System.out.println(best.getGDS());
		// System.out.println(best.getStimpleName("--"));

		// long[][] estimations = new long[dscU.size() + 1][2];
		// for (int i = 0; i < dscU.size(); i++) {
		// estimations[i][0] = (long) CostEstimation.estimateCosts(
		// dscU.get(i), oc[0], map);
		// estimations[i][1] = (long) CostEstimation.estimateCosts(
		// dscU.get(i), oc[1], map);
		// }
		// estimations[dscU.size()][0] = (long)
		// CostEstimation.estimateCosts(best,
		// oc[0], map);
		// estimations[dscU.size()][1] = (long)
		// CostEstimation.estimateCosts(best,
		// oc[1], map);

		// for (int i = 0; i < dscU.size(); i++) {
		// opt.write(dscU.get(i), false, opt.measure(gg, bg, m, dscU.get(i)),
		// estimations[i]);
		// }
		// opt.write(best, true, opt.measure(gg, bg, m, best),
		// estimations[dscU.size()]);
	}

	protected OperationCounts[] count() throws Exception {
		Config.overwrite("PROFILER_ACTIVATED", "true");
		Config.overwrite("HOTSWAP_ENABLED", "false");
		Profiler.reset();
		Counting.enable();

		GraphDataStructure gds = this.gds();
		GraphGenerator gg = this.gg(gds);
		BatchGenerator bg = this.bg();
		Metric[] metrics = this.metrics();

		Counting.init(gds);
		Graph g = gg.generate();
		Counting.oc.setSizes(g);
		OperationCounts ocGG = Counting.oc;

		Counting.init(gds);
		for (int i = 0; i < this.batches; i++) {
			Batch b = bg.generate(g);
			BatchSanitization.sanitize(b);
			b.apply(g);
			for (Metric m : metrics) {
				m.setGraph(g);
				((IRecomputation) m).recompute();
			}
		}
		Counting.oc.setSizes(g);
		OperationCounts ocBG = Counting.oc;

		return new OperationCounts[] { ocGG, ocBG };
	}

	public static void init() throws NumberFormatException, IOException {
		dscD = new ArrayList<DSConfig>();
		dscD.add(new DSConfigDirected(DArray.class));
		dscD.add(new DSConfigDirected(DArrayList.class));
		dscD.add(new DSConfigDirected(DHashSet.class));
		dscD.add(new DSConfigDirected(DHashMap.class));
		dscD.add(new DSConfigDirected(DHashTable.class));
		dscD.add(new DSConfigDirected(DLinkedList.class));
		dscD.add(new DSConfigDirected(DHashArrayList.class));

		dscU = new ArrayList<DSConfig>();
		dscU.add(new DSConfigUndirected(DArray.class));
		dscU.add(new DSConfigUndirected(DArrayList.class));
		dscU.add(new DSConfigUndirected(DHashSet.class));
		dscU.add(new DSConfigUndirected(DHashMap.class));
		dscU.add(new DSConfigUndirected(DHashTable.class));
		dscU.add(new DSConfigUndirected(DLinkedList.class));
		dscU.add(new DSConfigUndirected(DHashArrayList.class));

		map = new CostFunctionsSMap();
		map.add(CostFunctionsS.read(measurements, DArray.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DArrayList.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DHashSet.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DHashMap.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DHashTable.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DLinkedList.class,
				DirectedNode.class, DirectedEdge.class));
		map.add(CostFunctionsS.read(measurements, DHashArrayList.class,
				DirectedNode.class, DirectedEdge.class));
	}

}
