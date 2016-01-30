package dna.gds.util;

import java.io.IOException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.datastructures.count.Counting;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.evolvingNetworks.BarabasiAlbertGraph;
import dna.graph.generators.random.RandomGraph;
import dna.graph.generators.util.ReadableEdgeListFileGraph;
import dna.graph.generators.util.ReadableFileGraph;
import dna.io.filter.SuffixFilenameFilter;
import dna.metrics.algorithms.IRecomputation;
import dna.metrics.clustering.UndirectedClusteringCoefficientR;
import dna.metrics.connectivity.WeakConnectivityR;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.motifs.UndirectedMotifsR;
import dna.metrics.paths.UnweightedAllPairsShortestPathsR;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.random.RandomBatch;
import dna.updates.generators.random.RandomEdgeExchange;
import dna.updates.generators.util.ReadableDirBatchGenerator;

public class CountGeneration {
	public static final String countDir = "data/counts/";

	public static GraphDataStructure gdsU = GDS.undirected();
	public static GraphDataStructure gdsD = GDS.directed();

	public static void main(String[] args) throws IOException {
		// ggModels();
		// ggACC();
		// md();
		batches();
	}

	public static void batches() throws IOException {
		String datasets = "/Users/benni/TUD/datasets/_etc/Biology/Papers/ACC/";
		String[] filenames = new String[] { "CSphd", "e_coli", "Epa", "Roget",
				"yeast", "airport", "California", "facebook", "Foldoc",
				"ODLIS", "PairsFSG", "Words_English" };
		String[] filenames_small = new String[] { "CSphd", "e_coli", "Epa",
				"Roget", "yeast" };

		for (String filename : filenames_small) {
			GraphGenerator gg = new ReadableEdgeListFileGraph(datasets,
					filename + ".txt", " ", gdsU);
			BatchGenerator bg1 = new RandomBatch(10, 0, 40, 0);
			BatchGenerator bg2 = new RandomBatch(10, 5, 40, 10);
			BatchGenerator bg3 = new RandomEdgeExchange(100, 1000000);
			count(gg, "BG--" + filename + "-randGrowth", bg1, 100);
			count(gg, "BG--" + filename + "-rand", bg2, 100);
			count(gg, "BG--" + filename + "-xchange", bg3, 100);
		}
	}

	public static void ggModels() throws IOException {
		int[] n_ = new int[] { 100, 1000 };
		int[] d_ = new int[] { 2, 4, 10, 20 };
		for (int n : n_) {
			for (int d : d_) {
				count(new RandomGraph(gdsD, n, n * d), "R-d-" + n + "-" + d);
				count(new RandomGraph(gdsU, n, n * d), "R-u-" + n + "-" + d);
				count(new BarabasiAlbertGraph(gdsD, 2 * d, 2 * d, n - 2 * d, d),
						"BA-d-" + n + "-" + d);
				count(new BarabasiAlbertGraph(gdsU, 2 * d, 2 * d, n - 2 * d, d),
						"BA-u-" + n + "-" + d);
			}
		}
	}

	public static void ggACC() throws IOException {
		String datasets = "/Users/benni/TUD/datasets/_etc/Biology/Papers/ACC/";
		String[] filenames_large = new String[] { "airport", "California",
				"facebook", "Foldoc", "ODLIS", "PairsFSG", "Words_English" };
		String[] filenames_small = new String[] { "CSphd", "e_coli", "Epa",
				"Roget", "yeast" };
		String[] filenames = new String[] { "CSphd", "e_coli", "Epa", "Roget",
				"yeast", "airport", "California", "facebook", "Foldoc",
				"ODLIS", "PairsFSG", "Words_English" };

		for (String filename : filenames) {
			count(new ReadableEdgeListFileGraph(datasets, filename + ".txt",
					" ", gdsU), "ACC-" + filename);
		}
	}

	public static void md() throws IOException {
		String md7 = "/Users/benni/TUD/datasets/md/pnB_th_7/";
		String md8 = "/Users/benni/TUD/datasets/md/pnB_th_8/";

		countU(new ReadableFileGraph(md7, "0.dnag", gdsU), "MD--pnB_th_7",
				new ReadableDirBatchGenerator("pnB_th_7", md7,
						new SuffixFilenameFilter(".dnab")), 100);
		countU(new ReadableFileGraph(md8, "0.dnag", gdsU), "MD--pnB_th_8",
				new ReadableDirBatchGenerator("pnB_th_8", md8,
						new SuffixFilenameFilter(".dnab")), 100);
	}

	public static void countU(GraphGenerator gg, String name,
			BatchGenerator bg, int batches) throws IOException {
		Graph g = count(gg, name, bg, batches);
		count(g, new DegreeDistributionR(), name, "dd");
		count(g, new UndirectedClusteringCoefficientR(), name, "cc");
		count(g, new WeakConnectivityR(), name, "wc");
		count(g, new UndirectedMotifsR(), name, "m");
		count(g, new UnweightedAllPairsShortestPathsR(), name, "apsp");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	public static Graph count(GraphGenerator gg, String name,
			BatchGenerator bg, int batches) throws IOException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(">>> " + name);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Graph g = count(gg, name);
		if (bg != null) {
			count(g, name, bg, batches);
		}
		return g;
	}

	public static void countU(GraphGenerator gg, String name)
			throws IOException {
		countU(gg, name, null, 0);
	}

	public static Graph count(GraphGenerator gg, String name)
			throws IOException {
		Counting.init(gg.getGraphDataStructure());
		Graph g = gg.generate();
		Counting.oc.setSizes(g);
		Counting.oc.writeValues(countDir + name + "/_gg/");
		System.out.println("graph generation counts: " + name + " [" + g + "]");
		return g;
	}

	public static void count(Graph g, String name, BatchGenerator bg,
			int batches) throws IOException {
		Counting.init(g.getGraphDatastructures());
		int counter = 0;
		while (bg.isFurtherBatchPossible(g) && counter < batches) {
			Batch b = bg.generate(g);
			b.apply(g);
			counter++;
		}
		Counting.oc.setSizes(g);
		Counting.oc.writeValues(countDir + name + "/_bg/");
		System.out.println("batch generation counts: " + name + " [" + counter
				+ "]");
	}

	public static void count(Graph g, IRecomputation m, String name1,
			String name2) throws IOException {
		System.out.println("metric counts: " + name2 + " for " + name1);
		Counting.init(g.getGraphDatastructures());
		m.setGraph(g);
		m.recompute();
		Counting.oc.setSizes(g);
		Counting.oc.writeValues(countDir + name1 + "/" + name2 + "/");
	}
}
