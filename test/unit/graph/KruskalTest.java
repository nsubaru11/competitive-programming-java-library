package unit.graph;

import java.util.function.*;

import lib.graph.*;

final class KruskalTest extends SpanningForestContract {

	Function<UndirectedGraph, SpanningForestResult> minimum() {
		return Kruskal::minimum;
	}

	Function<UndirectedGraph, SpanningForestResult> maximum() {
		return Kruskal::maximum;
	}

	ToLongFunction<UndirectedGraph> minimumCost() {
		return Kruskal::minimumCost;
	}

	ToLongFunction<UndirectedGraph> maximumCost() {
		return Kruskal::maximumCost;
	}
}
