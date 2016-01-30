package dna.gds.dataset;

import java.io.File;
import java.io.FileNotFoundException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.konect.KonectGraph;
import dna.graph.generators.konect.KonectReader;
import dna.graph.generators.konect.KonectReader.KonectBatchType;
import dna.graph.generators.konect.KonectReader.KonectEdgeType;
import dna.graph.generators.konect.KonectReader.KonectGraphType;
import dna.io.BatchWriter;
import dna.io.GraphWriter;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.konect.KonectBatch;

public class Dataset {

	public static void main(String[] args) throws FileNotFoundException {
		String dir = "/Users/benni/TUD/datasets/dynamic/Konect/datasets/";
		String filename = "W.txt";
		String name = "FB";
		GraphDataStructure gds = GDS.undirected();
		KonectEdgeType edgeType = KonectEdgeType.ADD_REMOVE;
		String edgeParameter = "";
		KonectReader reader = new KonectReader(dir, filename, name, gds,
				edgeType, edgeParameter, false);

		KonectGraphType graphType = KonectGraphType.TOTAL_EDGES;
		String graphParameter = "1000";
		GraphGenerator gg = new KonectGraph(reader, graphType, graphParameter);

		KonectBatchType batchType = KonectBatchType.PROCESSED_EDGES;
		String batchParameter = "100";
		BatchGenerator bg = new KonectBatch(reader, batchType, batchParameter);

		int batches = 1000;

		String dst = "datasets/W-" + graphParameter + "-" + batchParameter
				+ "/";
		if (!(new File(dir)).exists()) {
			(new File(dir)).mkdirs();
		}

		Graph g = gg.generate();
		GraphWriter.write(g, dst, "0.dnag");
		System.out.println(g);
		for (int i = 0; i < batches; i++) {
			if (!bg.isFurtherBatchPossible(g)) {
				break;
			}
			Batch b = bg.generate(g);
			BatchWriter.write(b, dst, b.getTo() + ".dnab");
			b.apply(g);
			System.out.println(b + " => " + g);
		}
	}
}
