package dna.gds.dataset;

import dna.graph.Graph;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.util.ReadableFileGraph;
import dna.io.filter.SuffixFilenameFilter;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.util.ReadableDirBatchGenerator;

public class DatasetStats {

	public static void main(String[] args) throws Exception {
		print("/Users/benni/TUD/datasets/dynamic/md/pnB_th_7/", 20000);
		// print("/Users/benni/TUD/Projects/DNA/DNA.gdsAnalysis/datasets/FB-1000-100/",
		// 200);
	}

	public static void print(String dir, int batches) throws Exception {
		GraphGenerator gg = new ReadableFileGraph(dir, "0.dnag");
		BatchGenerator bg = new ReadableDirBatchGenerator("name", dir,
				new SuffixFilenameFilter(".dnab"));

		Graph g = gg.generate();
		print(g);
		for (int i = 0; i < batches; i++) {
			if (!bg.isFurtherBatchPossible(g)) {
				break;
			}
			Batch b = bg.generate(g);
			b.apply(g);
			print(g);
		}
	}

	protected static void print(Graph g) {
		System.out.println(g.getTimestamp() + "	" + g.getNodeCount() + "	"
				+ g.getEdgeCount());
	}

}
