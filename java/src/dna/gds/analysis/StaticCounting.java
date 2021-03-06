package dna.gds.analysis;

import java.io.IOException;

import argList.ArgList;
import argList.types.array.EnumArrayArg;
import argList.types.atomic.IntArg;
import argList.types.atomic.StringArg;
import dna.graph.Graph;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.datastructures.count.Counting;
import dna.graph.generators.GraphGenerator;
import dna.metrics.Metric;
import dna.metrics.algorithms.IRecomputation;
import dna.updates.batch.Batch;
import dna.updates.batch.BatchSanitization;
import dna.updates.generators.BatchGenerator;
import dna.util.Timer;
import dna.util.fromArgs.MetricFromArgs.MetricType;

public class StaticCounting extends Static {

	public String countsDir;

	public StaticCounting(String datasetName, String datasetDir,
			String graphFilename, String batchSuffix, String[] metricTypes,
			Integer batches, String countsDir) {
		super(datasetName, datasetDir, graphFilename, batchSuffix, metricTypes,
				batches);

		this.countsDir = countsDir;
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticCounting> argList = new ArgList<StaticCounting>(
				StaticCounting.class, new StringArg("datasetName",
						"name of the dataset"), new StringArg("datasetDir",
						"dir where the dataset is stored"), new StringArg(
						"graphFilename", "e.g., 0.dnag"), new StringArg(
						"batchSuffix", "e.g., .dnab"), new EnumArrayArg(
						"metricTypes", "metrics that should be computed", ",",
						MetricType.values()), new IntArg("batches",
						"number of batches"), new StringArg("countsDir",
						"dir where to store counts"));

		// String dataset =
		// "/Users/benni/TUD/datasets/dynamic/md/pnB_th_7_short/";
		//
		// String metrics =
		// "UnweightedAllPairsShortestPathsR,UndirectedClusteringCoefficientR,DegreeDistributionR";
		//
		// args = new String[] { "theName", dataset, "0.dnag", ".dnab", metrics,
		// "3", "data/counts/" };

		StaticCounting sr = argList.getInstance(args);
		sr.execute();
	}

	public void execute() throws Exception {
		Timer total = new Timer();
		Counting.enable();

		GraphDataStructure gds = this.gds();
		GraphGenerator gg = this.gg(gds);
		BatchGenerator bg = this.bg();
		Metric[] metrics = this.metrics();

		System.out.println("===> " + this.countsDir);

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

	protected void start(GraphDataStructure gds) {
		Counting.init(gds);
	}

	protected void end(Graph g, long timestamp, String name) throws IOException {
		String filename = timestamp + "-" + name + CountsIO.suffix;
		Counting.oc.setSizes(g);
		System.out.println("=> " + filename);
		Counting.oc.writeValues(this.countsDir, filename);
	}

}
