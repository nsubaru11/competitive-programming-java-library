public final class SuffixArray {
	private final String text;
	private final int[] sa;  // Suffix Array
	private final int[] lcp; // Longest Common Prefix

	public SuffixArray(String text) {
		this.text = text + '\0'; // 番兵
		int n = this.text.length();
		this.sa = buildSA(this.text);
		this.lcp = buildLCP();
	}

	private int[] buildSA(String s) {
		int n = s.length();
		Integer[] suffixes = new Integer[n];
		for (int i = 0; i < n; i++) suffixes[i] = i;

		// ソート（簡易版、実用は SA-IS アルゴリズム）
		java.util.Arrays.sort(suffixes, (a, b) ->
				s.substring(a).compareTo(s.substring(b)));

		return java.util.Arrays.stream(suffixes)
				.mapToInt(Integer::intValue).toArray();
	}

	private int[] buildLCP() {
		int n = sa.length;
		int[] lcp = new int[n];
		int[] rank = new int[n];

		for (int i = 0; i < n; i++) rank[sa[i]] = i;

		int h = 0;
		for (int i = 0; i < n; i++) {
			if (rank[i] == 0) continue;

			int j = sa[rank[i] - 1];
			while (i + h < n && j + h < n &&
					text.charAt(i + h) == text.charAt(j + h)) {
				h++;
			}
			lcp[rank[i]] = h;
			if (h > 0) h--;
		}
		return lcp;
	}

	public boolean contains(String pattern) {
		int left = 0, right = sa.length;
		while (left < right) {
			int mid = (left + right) / 2;
			String suffix = text.substring(sa[mid]);
			if (suffix.compareTo(pattern) < 0) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		return left < sa.length &&
				text.substring(sa[left]).startsWith(pattern);
	}
}
