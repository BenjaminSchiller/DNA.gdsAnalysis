package dna.gds.analysis;

import argList.ArgList;
import argList.types.array.IntArrayArg;
import argList.types.array.StringArrayArg;
import argList.types.atomic.EnumArg;
import argList.types.atomic.StringArg;
import dna.gds.util.CountEstimation;
import dna.graph.datastructures.config.DSConfig;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.OperationCount.AggregationType;
import dna.graph.datastructures.count.OperationCounts;
import dna.util.Timer;

public class StaticRecommendation extends StaticEstimationOld {

	public String countsDir;
	public String countsFilename;

	public StaticRecommendation(String dir, String[] prefixes,
			String[] suffixes, String aggregationType, String fitType,
			Integer[] fitSizes, String countsDir, String countsFilename) {
		super(dir, prefixes, suffixes, aggregationType, fitType, fitSizes);

		this.countsDir = countsDir;
		this.countsFilename = countsFilename;
	}

	public static void main(String[] args) throws Exception {
		ArgList<StaticRecommendation> argList = new ArgList<StaticRecommendation>(
				StaticRecommendation.class,
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
				new StringArg("countsDir",
						"dir where to write the aggregated counts"),
				new StringArg("countsFilename",
						"filename where to write the aggregated counts"));

		StaticRecommendation sr = argList.getInstance(args);
		sr.execute();
	}

	public void execute() throws Exception {
		Timer total = new Timer();
		CostFunctionsSMap map = this.getMap();

		OperationCounts oc = CountsIO.read(dir, prefixes, suffixes,
				aggregationType);

		oc.writeValues(countsDir, countsFilename + CountsIO.suffix);
		System.out.println("wrote to " + countsFilename + CountsIO.suffix);

		Timer recommendation = new Timer();
		DSConfig best = CountEstimation.estimateBestConfig(oc, map);
		recommendation.end();
		total.end();
		System.out.println("RECOMMENDATION: " + recommendation.getDutation());
		System.out.println("TOTAL: " + total.getDutation());

		System.out.println(best);
		System.out.println(best.getGDS());
		System.out.println(best.getStimpleName(gdsSep));
	}

}
