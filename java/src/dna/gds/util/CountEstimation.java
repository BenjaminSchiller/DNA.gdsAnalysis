package dna.gds.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import dna.graph.datastructures.DArray;
import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.DHashArrayList;
import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.DHashSet;
import dna.graph.datastructures.DHashTable;
import dna.graph.datastructures.DLinkedList;
import dna.graph.datastructures.IDataStructure;
import dna.graph.datastructures.config.DSConfig;
import dna.graph.datastructures.config.DSConfigComparator;
import dna.graph.datastructures.config.DSConfigDirected;
import dna.graph.datastructures.config.DSConfigUndirected;
import dna.graph.datastructures.cost.CostFunctionsS;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.OperationCounts;
import dna.graph.datastructures.count.OperationCountsDirected;
import dna.graph.datastructures.count.OperationCountsUndirected;
import dna.graph.datastructures.recommendation.CostFunctionsSComparator;
import dna.graph.edges.DirectedEdge;
import dna.graph.nodes.DirectedNode;
import dna.util.Log;

public class CountEstimation {

	public static final String measurements = "measurements/";

	public static ArrayList<DSConfig> dscD;
	public static ArrayList<DSConfig> dscU;
	public static CostFunctionsSMap map;

	public static void main(String[] args) throws IOException {

		init();

		String[] graphs = (new File(CountGeneration.countDir))
				.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return !name.startsWith(".");
					}
				});
		for (String graph : graphs) {
			String[] metrics = (new File(CountGeneration.countDir + graph + "/"))
					.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return !name.startsWith(".");
						}
					});
			for (String metric : metrics) {
				estimate(graph, metric);
			}
		}
	}

	@SuppressWarnings("unused")
	public static void estimate(String graph, String metric) throws IOException {
		OperationCounts oc = new OperationCountsUndirected();
		oc.readValues(CountGeneration.countDir + graph + "/" + metric + "/");
		Collections.sort(dscU, new DSConfigComparator(oc, map));
		if (false) {
			System.out.println(graph + " >>> " + metric);
			System.out.println("  => " + dscU.get(0));
			System.out.println("  => " + estimateBestConfig(oc, map));
		} else {
			String sep = "	|	";
			DSConfig opt = estimateBestConfig(oc, map);
			System.out.println(graph + sep + metric + sep + dscU.get(0) + sep
					+ opt);
			String str = "\\optimization{" + graph + "}{" + metric + "}{"
					+ dscU.get(0).V.getSimpleName() + "}{"
					+ opt.V.getSimpleName() + "}{" + opt.E.getSimpleName()
					+ "}{" + ((DSConfigUndirected) opt).adj.getSimpleName()
					+ "}";
			System.out.println(str.replace("_", "\\_") + "{" + graph + "}{"
					+ metric + "}");
		}
	}

	public static DSConfig estimateBestConfig(OperationCounts oc,
			CostFunctionsSMap map) {
		ArrayList<CostFunctionsS> list = new ArrayList<CostFunctionsS>(
				map.map.size());
		for (CostFunctionsS cf : map.map.values()) {
			list.add(cf);
		}

		if (oc instanceof OperationCountsDirected) {
			return estimateBestConfig((OperationCountsDirected) oc, map, list);
		} else if (oc instanceof OperationCountsUndirected) {
			return estimateBestConfig((OperationCountsUndirected) oc, map, list);
		} else {
			Log.error("unsupported operation counts: " + oc.getClass());
			return null;
		}
	}

	public static DSConfig estimateBestConfig(OperationCountsDirected oc,
			CostFunctionsSMap map, ArrayList<CostFunctionsS> list) {
		Class<? extends IDataStructure> V, E, in, out, neighbors;

		Collections.sort(list, new CostFunctionsSComparator(oc.V));
		V = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.E));
		E = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.in));
		in = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.out));
		out = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.neighbors));
		neighbors = list.get(0).ds;

		return new DSConfigDirected(V, E, in, out, neighbors);
	}

	public static DSConfig estimateBestConfig(OperationCountsUndirected oc,
			CostFunctionsSMap map, ArrayList<CostFunctionsS> list) {
		Class<? extends IDataStructure> V, E, adj;

		Collections.sort(list, new CostFunctionsSComparator(oc.V));
		V = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.E));
		E = list.get(0).ds;
		Collections.sort(list, new CostFunctionsSComparator(oc.adj));
		adj = list.get(0).ds;

		return new DSConfigUndirected(V, E, adj);
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
