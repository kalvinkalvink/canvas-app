package canvas.canvasapp.util.predicate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class DistinctByPredicate {
	public static <T> Predicate<T> distinctBy(Function<? super T, ?> f) {
		Set<Object> objects = ConcurrentHashMap.newKeySet();
		return t -> objects.add(f.apply(t));
	}
}
