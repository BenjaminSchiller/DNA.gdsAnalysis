package dna.gds.table;

import java.io.IOException;

import argList.ArgList;
import argList.types.array.IntArrayArg;
import argList.types.atomic.EnumArg;
import dna.gds.analysis.StaticCostFunctions;
import dna.graph.IElement;
import dna.graph.datastructures.DArray;
import dna.graph.datastructures.DArrayList;
import dna.graph.datastructures.DHashArrayList;
import dna.graph.datastructures.DHashMap;
import dna.graph.datastructures.DHashSet;
import dna.graph.datastructures.DHashTable;
import dna.graph.datastructures.DLinkedList;
import dna.graph.datastructures.IDataStructure;
import dna.graph.datastructures.cost.CostFunctionFitted;
import dna.graph.datastructures.cost.CostFunctionFitted.FitType;
import dna.graph.datastructures.cost.CostFunctions;
import dna.graph.datastructures.cost.CostFunctionsSMap;
import dna.graph.datastructures.count.OperationCount.Operation;
import dna.graph.edges.Edge;
import dna.graph.nodes.Node;

public class Table extends StaticCostFunctions {
	public FitType fitType;
	public int[] fitSizes;
	public CostFunctionsSMap map;

	public Table(String fitType, Integer[] fitSizes)
			throws NumberFormatException, IOException {
		this.fitType = FitType.valueOf(fitType);
		this.fitSizes = new int[fitSizes.length];
		for (int i = 0; i < fitSizes.length; i++) {
			this.fitSizes[i] = fitSizes[i];
		}

		CostFunctionFitted.defaultFitType = this.fitType;
		CostFunctionFitted.defaultSizes = this.fitSizes;
		this.map = getMap();
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		ArgList<Table> argList = new ArgList<Table>(Table.class, new EnumArg(
				"fitType", "hot the functions are fitted", FitType.values()),
				new IntArrayArg("fitSizes", "sizes for fitted functions", ","));

		FitType[] fts = new FitType[] { FitType.MedSD };

		for (FitType ft : fts) {
			args = new String[] { ft.toString(),
					"100,1000,10000,50000,100000,1000000" };
			Table table = argList.getInstance(args);

			int[] sizes = new int[] { 10, 100, 1000, 10000, 100000 };
			System.out.println("% " + ft);
			table.printBest(sizes);
			System.out.println("\n\n");
		}
	}

	public void printBest(int[] sizes) {
		printHeader();
		this.printBest(v, sizes);
		System.out.println("\\midrule");
		this.printBest(e, sizes);
		printFooter();
	}

	public static final Class<? extends IElement> v = Node.class;
	public static final Class<? extends IElement> e = Edge.class;

	@SuppressWarnings("unchecked")
	public static final Class<? extends IDataStructure>[] dss = (Class<? extends IDataStructure>[]) new Class[] {
			DArray.class, DArrayList.class, DHashArrayList.class,
			DHashMap.class, DHashSet.class, DHashTable.class, DLinkedList.class };

	// public static final Operation[] os = new Operation[] { Operation.INIT,
	// Operation.ADD_SUCCESS, Operation.ADD_FAILURE,
	// Operation.RANDOM_ELEMENT, Operation.SIZE, Operation.ITERATE,
	// Operation.CONTAINS_SUCCESS, Operation.CONTAINS_FAILURE,
	// Operation.GET_SUCCESS, Operation.GET_FAILURE,
	// Operation.REMOVE_SUCCESS, Operation.REMOVE_FAILURE };
	public static final Operation[] os = new Operation[] { Operation.INIT,
			Operation.ADD_SUCCESS, Operation.ADD_FAILURE,
			Operation.REMOVE_SUCCESS, Operation.REMOVE_FAILURE,
			Operation.GET_SUCCESS, Operation.GET_FAILURE, Operation.ITERATE,
			Operation.RANDOM_ELEMENT, Operation.SIZE,
			Operation.CONTAINS_SUCCESS, Operation.CONTAINS_FAILURE };

	public static void printHeader() {
		System.out.println("\\begin{tabular}{rr | cccc cccc cccc}");
		System.out.println("\\toprule");
		System.out.print("t	&	s");
		for (Operation o : os) {
			System.out.print("	&	$" + getName(o) + "$");
		}
		System.out.println("\\\\");
		System.out.println("\\midrule");
	}

	public static void printFooter() {
		System.out.println("\\bottomrule");
		// System.out.println("& & \\multicolumn{12}{c}{A = Array, AL = ArrayList, HAL = HashArrayList, HM = HashMap, HS = HashSet, HT = HashTable, LL = LinkedList}");
		System.out
				.println("& & \\multicolumn{12}{c}{A = Array, AL = ArrayList, HAL = HashArrayList} \\\\");
		System.out
				.println("& & \\multicolumn{12}{c}{HM = HashMap, HS = HashSet, HT = HashTable, LL = LinkedList}");
		System.out.println("\\end{tabular}");
	}

	// public void printRuntimes(Class<? extends IElement> et, int size)
	// throws NumberFormatException, IOException {
	// printHeader();
	//
	// for (Class<? extends IDataStructure> ds : dss) {
	// System.out.print(getName(ds));
	// for (Operation o : os) {
	// double rt = getRuntime(ds, et, o, size);
	// System.out.print("	&	" + ((int) rt));
	// }
	// System.out.println("\\\\");
	// }
	//
	// printFooter();
	// }

	public void printBest(Class<? extends IElement> et, int[] sizes) {
		for (int size : sizes) {
			System.out.print((et.isAssignableFrom(Node.class) ? "v" : "e")
					+ "	&	");
			switch (size) {
			case 10:
				System.out.print("$10^1$");
				break;
			case 100:
				System.out.print("$10^2$");
				break;
			case 1000:
				System.out.print("$10^3$");
				break;
			case 10000:
				System.out.print("$10^4$");
				break;
			case 100000:
				System.out.print("$10^5$");
				break;
			default:
				System.out.print("???");
				break;
			}
			for (Operation o : os) {
				Class<? extends IDataStructure> ds = getBest(et, o, size);
				System.out.print("	&	" + getName(ds));
			}
			System.out.println("\\\\");
		}
	}

	protected static String getName(Class<? extends IDataStructure> ds) {
		if (ds.isAssignableFrom(DArray.class)) {
			return "A";
		} else if (ds.isAssignableFrom(DArrayList.class)) {
			return "AL";
		} else if (ds.isAssignableFrom(DHashArrayList.class)) {
			return "HAL";
		} else if (ds.isAssignableFrom(DHashMap.class)) {
			return "HM";
		} else if (ds.isAssignableFrom(DHashSet.class)) {
			return "HS";
		} else if (ds.isAssignableFrom(DHashTable.class)) {
			return "HT";
		} else if (ds.isAssignableFrom(DLinkedList.class)) {
			return "LL";
		} else {
			throw new IllegalArgumentException("unknown DS: "
					+ ds.getSimpleName());
		}
	}

	protected static String getName(Operation o) {
		switch (o) {
		case ADD_FAILURE:
			return "add_f";
		case ADD_SUCCESS:
			return "add_s";
		case CONTAINS_FAILURE:
			return "cont_f";
		case CONTAINS_SUCCESS:
			return "cont_s";
		case GET_FAILURE:
			return "get_f";
		case GET_SUCCESS:
			return "get_s";
		case INIT:
			return "init";
		case ITERATE:
			return "iter";
		case RANDOM_ELEMENT:
			return "rand";
		case REMOVE_FAILURE:
			return "rem_f";
		case REMOVE_SUCCESS:
			return "rem_s";
		case SIZE:
			return "size";
		default:
			return null;
		}
	}

	protected Class<? extends IDataStructure> getBest(
			Class<? extends IElement> et, Operation o, int size) {
		Class<? extends IDataStructure> best = dss[0];
		for (int i = 1; i < dss.length; i++) {
			if (getRuntime(dss[i], et, o, size) < getRuntime(best, et, o, size)) {
				best = dss[i];
			}
		}
		return best;
	}

	protected double getRuntime(Class<? extends IDataStructure> ds,
			Class<? extends IElement> et, Operation o, int size) {
		CostFunctions f = null;
		if (et.isAssignableFrom(Node.class)) {
			f = map.get(ds).nodes;
		} else if (et.isAssignableFrom(Edge.class)) {
			f = map.get(ds).edges;
		} else {
			return -1;
		}

		switch (o) {
		case ADD_FAILURE:
			return f.ADD_FAILURE.getCost(size);
		case ADD_SUCCESS:
			return f.ADD_SUCCESS.getCost(size);
		case CONTAINS_FAILURE:
			return f.CONTAINS_FAILURE.getCost(size);
		case CONTAINS_SUCCESS:
			return f.CONTAINS_SUCCESS.getCost(size);
		case GET_FAILURE:
			return f.GET_FAILURE.getCost(size);
		case GET_SUCCESS:
			return f.GET_SUCCESS.getCost(size);
		case INIT:
			return f.INIT.getCost(size);
		case ITERATE:
			return f.ITERATE.getCost(size);
		case RANDOM_ELEMENT:
			return f.RANDOM_ELEMENT.getCost(size);
		case REMOVE_FAILURE:
			return f.REMOVE_FAILURE.getCost(size);
		case REMOVE_SUCCESS:
			return f.REMOVE_SUCCESS.getCost(size);
		case SIZE:
			return f.SIZE.getCost(size);
		default:
			return -1.0;
		}
	}
}
