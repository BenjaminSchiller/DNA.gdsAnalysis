package dna.gds.analysis;

import java.io.IOException;

import dna.graph.datastructures.DArray;
import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.DHashArrayList;
import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.DHashSet;
import dna.graph.datastructures.DHashTable;
import dna.graph.datastructures.DLinkedList;
import dna.graph.datastructures.cost.CostFunctionsS;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.edges.DirectedEdge;
import dna.graph.nodes.DirectedNode;

public class StaticCostFunctions {

	public static final String gdsSep = "--";

	protected CostFunctionsSMap getMap() throws NumberFormatException,
			IOException {
		CostFunctionsSMap map = new CostFunctionsSMap();
		map.add(CostFunctionsS.read("", DArray.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DArrayList.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashSet.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashMap.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashTable.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DLinkedList.class, DirectedNode.class,
				DirectedEdge.class));
		map.add(CostFunctionsS.read("", DHashArrayList.class,
				DirectedNode.class, DirectedEdge.class));

		return map;
	}
}
