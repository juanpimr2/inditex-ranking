package io.github.juanpimr2.inditex_ranking.domain.algorithm.sorting;

import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * Servicio de ordenación basado en el algoritmo Merge Sort.
 * <p>
 * Este algoritmo divide la lista en mitades recursivamente hasta llegar a listas de un solo elemento,
 * y luego combina ("merge") esas listas ya ordenadas en una lista final ordenada.
 * <p>
 * Ventajas:
 * <ul>
 *     <li>Complejidad O(n log n) en el peor de los casos (eficiente para listas grandes).</li>
 *     <li>Ordenación estable: no altera el orden relativo de elementos con el mismo valor.</li>
 *     <li>No modifica la lista original: trabaja sobre copias defensivas.</li>
 * </ul>
 * <p>
 * En este caso, se utiliza para ordenar productos por su score de forma descendente,
 * garantizando consistencia y rendimiento incluso con colecciones grandes.
 */
@Service
public class MergeSortService {

  /**
   * Ordena la lista de productos por score de mayor a menor.
   * Es estable y no modifica la lista original.
   */
  public List<RankedProduct> sortDescByScore(List<RankedProduct> input) {
    if (input.size() <= 1) return new ArrayList<>(input); // lista ya ordenada

    Comparator<RankedProduct> byScoreDesc =
            Comparator.comparingDouble(RankedProduct::getScore).reversed();

    return mergeSort(new ArrayList<>(input), byScoreDesc);
  }

  /**
   * Aplica Merge Sort de forma recursiva.
   */
  private static <T> List<T> mergeSort(List<T> list, Comparator<T> cmp) {
    if (list.size() <= 1) return list;

    int mid = list.size() / 2;
    List<T> left = mergeSort(new ArrayList<>(list.subList(0, mid)), cmp);
    List<T> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), cmp);

    return merge(left, right, cmp);
  }

  /**
   * Une dos listas ya ordenadas en una sola lista ordenada.
   * Usa colas para evitar manejar índices manualmente.
   */
  private static <T> List<T> merge(List<T> left, List<T> right, Comparator<T> cmp) {
    ArrayList<T> result = new ArrayList<T>(left.size() + right.size());
    Deque<T> l = new ArrayDeque<>(left);
    Deque<T> r = new ArrayDeque<>(right);

    while (!l.isEmpty() || !r.isEmpty()) {
      if (r.isEmpty() || (!l.isEmpty() && cmp.compare(l.peekFirst(), r.peekFirst()) <= 0)) {
        result.add(l.pollFirst());
      } else {
        result.add(r.pollFirst());
      }
    }
    return result;
  }
}
