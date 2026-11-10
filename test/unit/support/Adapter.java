package unit.support;

import java.lang.reflect.*;
import java.util.*;

/**
 * 契約テストの Target インターフェースを、型別クラス（int・long・char 版）のインスタンスへ自動でつなぐ。
 * Target のメソッドは、同じ名前・同じ引数の個数の公開メソッドへ委譲する。引数のないメソッドが見つからなければ、同名の public フィールドを読む。
 * 引数と戻り値は次のように変換する。
 * <ul>
 *   <li>long ⇔ int・char: int・char への変換は範囲外なら ArithmeticException を投げる</li>
 *   <li>long[]・long[][] ⇔ int[]・char[] などの配列</li>
 *   <li>関数型インターフェース同士（例: LongBinaryOperator → IntBinaryOperator）: 引数と戻り値を同じ規則で変換する</li>
 *   <li>戻り値の型が Target と同じなら、結果を Target で包む</li>
 * </ul>
 * 次の名前は特別に扱う。{@code self()} は対象そのもの、{@code copy()} はコピーコンストラクタ、
 * {@code fromCollection()} は対象を IntCollection などとして受け取るコンストラクタで作った新しいインスタンスを返す。
 * Target の default メソッドはそのまま実行する。
 */
public final class Adapter {

	private Adapter() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T of(final Class<T> iface, final Object target) {
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, new Handler(iface, target));
	}

	private record Handler(Class<?> iface, Object target) implements InvocationHandler {
		public Object invoke(final Object proxy, final Method m, final Object[] args) throws Throwable {
			if (m.isDefault()) return InvocationHandler.invokeDefault(proxy, m, args);
			final Object[] a = args == null ? new Object[0] : args;
			switch (m.getName()) {
				case "self":
					return target;
				case "toString":
					if (a.length == 0) return target.toString();
					break;
				case "hashCode":
					if (a.length == 0) return System.identityHashCode(proxy);
					break;
				case "equals":
					if (a.length == 1) return proxy == a[0];
					break;
				case "copy":
					if (a.length == 0) return of(iface, construct(target.getClass()));
					break;
				case "fromCollection":
					if (a.length == 0) return of(iface, construct(null));
					break;
				default:
					break;
			}
			final Method t = find(target.getClass(), m);
			if (t == null) {
				// 引数のないメソッドがなければ、同名の public フィールド（h・w など）を読む
				if (a.length == 0) {
					try {
						return convert(target.getClass().getField(m.getName()).get(target), m.getReturnType());
					} catch (final NoSuchFieldException ignored) {
					}
				}
				throw new NoSuchMethodException(target.getClass().getName() + "." + m.getName() + " が見つからない");
			}
			final Class<?>[] types = t.getParameterTypes();
			final Object[] converted = new Object[a.length];
			for (int i = 0; i < a.length; i++) converted[i] = convert(a[i], types[i]);
			final Object res;
			try {
				res = t.invoke(target, converted);
			} catch (final InvocationTargetException e) {
				throw e.getCause();
			}
			if (m.getReturnType() == iface) return of(iface, res);
			if (res == target) return target;
			return convert(res, m.getReturnType());
		}

		/**
		 * type が null なら、対象のクラスと異なる型を1つ受け取るコンストラクタ（IntCollection など）を使う。
		 */
		private Object construct(final Class<?> type) throws Throwable {
			for (final Constructor<?> c : target.getClass().getConstructors()) {
				if (c.getParameterCount() != 1) continue;
				final Class<?> p = c.getParameterTypes()[0];
				final boolean ok = type != null ? p == type : p != target.getClass() && p.isInterface() && p.isInstance(target);
				if (!ok) continue;
				try {
					return c.newInstance(target);
				} catch (final InvocationTargetException e) {
					throw e.getCause();
				}
			}
			throw new NoSuchMethodException(target.getClass().getName() + " のコンストラクタが見つからない");
		}
	}

	/**
	 * target の公開メソッド name を、引数の個数が一致するもので呼び出し、結果を type へ変換して返す。
	 * 次元ごとに引数の個数が変わる API（get(i)・get(i, j)・get(i, j, k) など）を1つのテストで扱うために使う。
	 * 引数は {@link #convert} の規則でメソッドの引数の型へ変換する。
	 */
	public static <R> R call(final Class<R> type, final Object target, final String name, final Object... args) {
		return invoke(type, target.getClass(), target, name, args);
	}

	/**
	 * cls の static メソッド name を、{@link #call} と同じ規則で呼び出す。
	 * 同じ名前・同じ引数の個数のオーバーロードは、引数を変換できるものを選ぶ。
	 */
	public static <R> R callStatic(final Class<R> type, final Class<?> cls, final String name, final Object... args) {
		return invoke(type, cls, null, name, args);
	}

	private static <R> R invoke(final Class<R> type, final Class<?> cls, final Object target, final String name, final Object... args) {
		// 引数の型がそのまま一致するオーバーロードを優先し、なければ変換して渡せるものを使う。
		// 一致するものが複数あれば、Object・Object[] の引数が少ない（より具体的な）ものを選ぶ
		final List<Method> exact = new ArrayList<>(), others = new ArrayList<>();
		for (final Method t : cls.getMethods()) {
			if (!t.getName().equals(name) || t.getParameterCount() != args.length) continue;
			if ((target == null) != Modifier.isStatic(t.getModifiers())) continue;
			boolean ok = true;
			for (int i = 0; i < args.length && ok; i++) ok = accepts(t.getParameterTypes()[i], args[i]);
			(ok ? exact : others).add(t);
		}
		exact.sort(Comparator.comparingLong(t -> Arrays.stream(t.getParameterTypes()).filter(p -> p == Object.class || p == Object[].class).count()));
		final List<Method> candidates = new ArrayList<>(exact);
		candidates.addAll(others);
		for (final Method t : candidates) {
			final Object[] converted = new Object[args.length];
			try {
				boolean ok = true;
				for (int i = 0; i < args.length && ok; i++) {
					converted[i] = convert(args[i], t.getParameterTypes()[i]);
					ok = accepts(t.getParameterTypes()[i], converted[i]);
				}
				if (!ok) continue;
			} catch (final ClassCastException | ArithmeticException | IllegalArgumentException e) {
				continue;
			}
			try {
				@SuppressWarnings("unchecked") final R res = (R) convert(t.invoke(target, converted), type);
				return res;
			} catch (final InvocationTargetException e) {
				if (e.getCause() instanceof RuntimeException re) throw re;
				throw new IllegalStateException(e.getCause());
			} catch (final IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		throw new IllegalArgumentException(cls.getName() + "." + name + " が見つからない");
	}

	/**
	 * 変換後の値 v を型 p の引数として渡せるか。
	 */
	private static boolean accepts(final Class<?> p, final Object v) {
		if (v == null) return !p.isPrimitive();
		if (!p.isPrimitive()) return p.isInstance(v);
		return p == int.class && v instanceof Integer || p == long.class && v instanceof Long || p == char.class && v instanceof Character
			|| p == boolean.class && v instanceof Boolean || p == double.class && v instanceof Double;
	}

	private static Method find(final Class<?> cls, final Method m) {
		for (final Method t : cls.getMethods()) {
			if (!t.getName().equals(m.getName()) || t.getParameterCount() != m.getParameterCount()) continue;
			boolean ok = true;
			for (int i = 0; i < t.getParameterCount() && ok; i++) ok = compatible(m.getParameterTypes()[i], t.getParameterTypes()[i]);
			if (ok) {
				t.setAccessible(true);
				return t;
			}
		}
		return null;
	}

	private static boolean compatible(final Class<?> from, final Class<?> to) {
		if (from == to) return true;
		if (isNumber(from) && isNumber(to)) return true;
		// Target 型の引数には、同じ Target で包んだ対象のクラスを渡せるとみなす
		if (from.isInterface() && !to.isInterface() && !to.isPrimitive() && !to.isArray() && functional(from) == null && to != Object.class && from.getEnclosingClass() != null) return true;
		// 汎用版の型引数 T（消去後は Object・Comparable など）には long をボックス化して渡す
		if (to == Object.class) return true;
		if (isNumber(from) && to.isAssignableFrom(Long.class)) return true;
		if (from.isArray() && to.isArray()) return compatible(from.getComponentType(), to.getComponentType());
		if (functional(from) != null && functional(to) != null) {
			return functional(from).getParameterCount() == functional(to).getParameterCount();
		}
		return to.isAssignableFrom(from);
	}

	private static boolean isNumber(final Class<?> c) {
		return c == long.class || c == int.class || c == char.class;
	}

	/**
	 * 関数型インターフェースなら唯一の抽象メソッドを返し、そうでなければ null を返す。
	 */
	private static Method functional(final Class<?> c) {
		if (!c.isInterface()) return null;
		Method sam = null;
		for (final Method m : c.getMethods()) {
			if (!Modifier.isAbstract(m.getModifiers())) continue;
			if (sam != null) return null;
			sam = m;
		}
		return sam;
	}

	/**
	 * 値 v を型 to へ変換する。
	 */
	public static Object convert(final Object v, final Class<?> to) {
		if (v == null || to == void.class) return v;
		// Target として渡された値は、包んでいる対象そのものへ戻す（ModInt.add(ModInt) など）
		if (Proxy.isProxyClass(v.getClass()) && Proxy.getInvocationHandler(v) instanceof Handler h && to.isInstance(h.target)) return h.target;
		if (to == long.class || to == Long.class) return v instanceof Character c ? (long) c : ((Number) v).longValue();
		if (to == int.class || to == Integer.class) {
			return v instanceof Character c ? (int) c : Math.toIntExact(((Number) v).longValue());
		}
		if (to == double.class || to == Double.class) return v instanceof Character c ? (double) c : ((Number) v).doubleValue();
		if (to == char.class || to == Character.class) {
			if (v instanceof Character) return v;
			final long x = ((Number) v).longValue();
			if (x != (char) x) throw new ArithmeticException("char overflow: " + x);
			return (char) x;
		}
		if (to.isArray() && v.getClass().isArray() && !to.isInstance(v)) {
			final int n = Array.getLength(v);
			final Object res = Array.newInstance(to.getComponentType(), n);
			for (int i = 0; i < n; i++) Array.set(res, i, convert(Array.get(v, i), to.getComponentType()));
			return res;
		}
		if (!to.isInstance(v) && functional(to) != null) {
			final Method sam = functional(to), source = functional(v.getClass().getInterfaces().length == 1 ? v.getClass().getInterfaces()[0] : null);
			if (source != null) {
				return Proxy.newProxyInstance(to.getClassLoader(), new Class<?>[]{to}, (proxy, m, args) -> {
					if (!m.equals(sam)) return m.invoke(v, args);
					final Object[] a = new Object[args.length];
					for (int i = 0; i < args.length; i++) a[i] = convert(args[i], source.getParameterTypes()[i]);
					source.setAccessible(true);
					try {
						return convert(source.invoke(v, a), sam.getReturnType());
					} catch (final InvocationTargetException e) {
						throw e.getCause();
					}
				});
			}
		}
		return v;
	}
}
