package dna.gds.analysis;

import java.io.IOException;

import argList.ArgList;
import argList.types.array.EnumArrayArg;
import argList.types.array.StringArrayArg;
import argList.types.atomic.IntArg;
import argList.types.atomic.StringArg;
import dna.graph.Graph;
import dna.graph.datastructures.DataStructure.ListType;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.datastructures.IDataStructure;
import dna.graph.datastructures.count.Counting;
import dna.graph.edges.UndirectedEdge;
import dna.graph.generators.GraphGenerator;
import dna.graph.nodes.UndirectedNode;
import dna.metrics.Metric;
import dna.metrics.algorithms.IRecomputation;
import dna.updates.batch.Batch;
import dna.updates.batch.BatchSanitization;
import dna.updates.generators.BatchGenerator;
import dna.util.Timer;
import dna.util.fromArgs.MetricFromArgs.MetricType;

public class StaticExecution extends Static {

	public String[] gdsString;

	public StaticExecution(String datasetName, String datasetDir,
			String graphFilename, String batchSuffix, String[] metricTypes,
			Integer batches, String[] gdsString) {
		super(datasetName, datasetDir, graphFilename, batchSuffix, metricTypes,
				batches);

		this.gdsString = gdsString;
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticExecution> argList = new ArgList<StaticExecution>(
				StaticExecution.class, new StringArg("datasetName",
						"name of the dataset"), new StringArg("datasetDir",
						"dir where the dataset is stored"), new StringArg(
						"graphFilename", "e.g., 0.dnag"), new StringArg(
						"batchSuffix", "e.g., .dnab"), new EnumArrayArg(
						"metricTypes", "metrics that should be computed", ",",
						MetricType.values()), new IntArg("batches",
						"number of batches"), new StringArrayArg("gdsString",
						"List of the datastructures to use (V,E,adj)",
						StaticRecommendation.gdsSep));

		// String dataset =
		// "/Users/benni/TUD/datasets/dynamic/md/pnB_th_7_short/";
		//
		// String metrics =
		// "UnweightedAllPairsShortestPathsR,UndirectedClusteringCoefficientR,DegreeDistributionR";
		//
		// String gdsString = "DArray--DHashSet--DArrayList";
		// // gdsString = "DArray--DArray--DArray";
		// gdsString = "DLinkedList--DLinkedList--DLinkedList";
		//
		// args = new String[] { "theName", dataset, "0.dnag", ".dnab", metrics,
		// "3", gdsString };

		StaticExecution ex = argList.getInstance(args);
		ex.execute();
	}

	public void execute() throws Exception {
		GraphDataStructure gds = this.getGDS();
		GraphGenerator gg = this.gg(gds);
		BatchGenerator bg = this.bg();
		Metric[] metrics = this.metrics();

		System.out.println("using: " + this.gdsString[0] + " "
				+ this.gdsString[1] + " " + this.gdsString[2]);

		Timer total = new Timer();

		this.start(gds);
		Graph g = gg.generate();
		this.end(g, g.getTimestamp(), CountsIO.gg);

		for (Metric m : metrics) {
			this.start(gds);
			m.setGraph(g);
			((IRecomputation) m).recompute();
			this.end(g, g.getTimestamp(), m.getNamePlain());
		}

		for (int i = 0; i < this.batches; i++) {
			if (!bg.isFurtherBatchPossible(g)) {
				break;
			}

			this.start(gds);
			Batch b = bg.generate(g);
			BatchSanitization.sanitize(b);
			this.end(g, b.getTo(), CountsIO.bg);

			this.start(gds);
			b.apply(g);
			this.end(g, g.getTimestamp(), CountsIO.ba);

			for (Metric m : metrics) {
				this.start(gds);
				m.setGraph(g);
				((IRecomputation) m).recompute();
				this.end(g, g.getTimestamp(), m.getNamePlain());
			}
		}

		total.end();
		System.out.println("TOTAL: " + total.getDutation());
	}

	@SuppressWarnings("unchecked")
	public GraphDataStructure getGDS() throws Exception {
		Class<? extends IDataStructure> v, e, adj;
		String pre = "dna.graph.datastructures.";
		v = (Class<? extends IDataStructure>) Class.forName(pre + gdsString[0]);
		e = (Class<? extends IDataStructure>) Class.forName(pre + gdsString[1]);
		adj = (Class<? extends IDataStructure>) Class.forName(pre
				+ gdsString[2]);

		return new GraphDataStructure(GraphDataStructure.getList(
				ListType.GlobalNodeList, v, ListType.GlobalEdgeList, e,
				ListType.LocalEdgeList, adj), UndirectedNode.class,
				UndirectedEdge.class);
	}

	protected Timer timer;

	protected void start(GraphDataStructure gds) {
		this.timer = new Timer();
	}

	protected void end(Graph g, long timestamp, String name) throws IOException {
		this.timer.end();
		System.out.println(timestamp + "-" + name + ": " + timer.getDutation());
	}
}
