package unit.graph;

import java.util.function.*;

import lib.graph.*;

final class PrimTest extends SpanningForestContract {

	Function<UndirectedGraph, SpanningForestResult> minimum() {
		return Prim::minimum;
	}

	Function<UndirectedGraph, SpanningForestResult> maximum() {
		return Prim::maximum;
	}

	ToLongFunction<UndirectedGraph> minimumCost() {
		return Prim::minimumCost;
	}

	ToLongFunction<UndirectedGraph> maximumCost() {
		return Prim::maximumCost;
	}
}
