package dna.gds.analysis;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import dna.graph.datastructures.count.OperationCount.AggregationType;
import dna.graph.datastructures.count.OperationCounts;
import dna.graph.datastructures.count.OperationCountsUndirected;

public class CountsIO {
	public static final String suffix = ".counts";
	public static final String gg = "gg";
	public static final String bg = "bg";
	public static final String ba = "ba";

	public static OperationCounts read(String dir, String[] prefixes,
			String[] suffixes, AggregationType aggregationType)
			throws IOException {
		String[] files = (new File(dir)).list(new CountsFilenameFilter(
				prefixes, suffixes));
		OperationCounts[] ocs = new OperationCounts[files.length];
		for (int i = 0; i < files.length; i++) {
			ocs[i] = new OperationCountsUndirected();
			ocs[i].readValues(dir, files[i]);
			System.out.println("read " + files[i]);
		}
		OperationCountsUndirected oc = new OperationCountsUndirected();
		return oc.add(aggregationType, ocs);
	}

	public static class CountsFilenameFilter implements FilenameFilter {
		protected String[] prefixes;
		protected String[] suffixes;

		public CountsFilenameFilter(String[] prefixes, String[] suffixes) {
			this.prefixes = prefixes;
			this.suffixes = suffixes;
		}

		@Override
		public boolean accept(File dir, String name) {
			for (String pre : this.prefixes) {
				if (name.startsWith(pre)) {
					return true;
				}
			}
			for (String suf : this.suffixes) {
				if (name.endsWith(suf + suffix)) {
					return true;
				}
			}
			return false;
		}
	}
}
