package dna.gds.analysis;

import java.io.IOException;

import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.util.ReadableFileGraph;
import dna.metrics.Metric;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.util.ReadableDirConsecutiveBatchGenerator;
import dna.util.fromArgs.MetricFromArgs;
import dna.util.fromArgs.MetricFromArgs.MetricType;

public class Static {
	public String datasetName;
	public String datasetDir;
	public String graphFilename;
	public String batchSuffix;

	public MetricType[] metricTypes;
	public int batches;

	public Static(String datasetName, String datasetDir, String graphFilename,
			String batchSuffix, String[] metricTypes, int batches) {
		this.datasetName = datasetName;
		this.datasetDir = datasetDir;
		this.graphFilename = graphFilename;
		this.batchSuffix = batchSuffix;

		this.metricTypes = new MetricType[metricTypes.length];
		for (int i = 0; i < metricTypes.length; i++) {
			this.metricTypes[i] = MetricType.valueOf(metricTypes[i]);
		}
		this.batches = batches;
	}

	protected GraphDataStructure gds() {
		return GDS.undirected();
	}

	protected GraphGenerator gg(GraphDataStructure gds) throws IOException {
		return new ReadableFileGraph(this.datasetDir, this.graphFilename, gds);
	}

	protected BatchGenerator bg() {
		return new ReadableDirConsecutiveBatchGenerator(this.datasetName,
				this.datasetDir, this.batchSuffix);
	}

	protected Metric[] metrics() {
		Metric[] metrics = new Metric[this.metricTypes.length];
		for (int i = 0; i < this.metricTypes.length; i++) {
			metrics[i] = MetricFromArgs.parse(metricTypes[i]);
		}
		return metrics;
	}
}
