package patterns.graph;

import java.util.*;
import java.util.function.*;

/**
 * BFSの基本フォーマットです。 gridのBFSを行います。
 */
public class GridBFS {
	private final int h, w;
	private final char[] grid;

	public GridBFS(int h, int w) {
		this(h, w, new char[h * w]);
	}

	public GridBFS(int h, int w, char[] grid) {
		this.h = h;
		this.w = w;
		this.grid = grid;
	}

	public void set(int i, int j, char c) {
		grid[i * w + j] = c;
	}

	public int searchGoal(int startX, int startY, char goalChar, Predicate<Character> predicate) {
		return searchGoal(List.of(startX * w + startY), goalChar, predicate);
	}

	public int searchGoal(List<Integer> start, char goalChar, Predicate<Character> predicate) {
		Predicate<Integer> isValid = i -> 0 <= i && i < h * w;
		ArrayDeque<Integer> dq = new ArrayDeque<>(h * w);
		int[] dist = new int[h * w];
		Arrays.fill(dist, -1);
		int[] directions = {-w, -1, 1, w};
		for (int s : start) {
			if (isValid.test(s) && predicate.test(grid[s])) {
				dq.add(s);
				dist[s] = 0;
			}
		}
		while (!dq.isEmpty()) {
			int current = dq.poll();
			if (grid[current] == goalChar) return dist[current];
			for (int d : directions) {
				int next = current + d;
				if (isValid.test(next) && dist[next] == -1 && predicate.test(grid[next])) {
					dist[next] = dist[current] + 1;
					dq.add(next);
				}
			}
		}
		return -1;
	}
}
