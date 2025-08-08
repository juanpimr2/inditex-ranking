package io.github.juanpimr2.inditex_ranking.infrastructure.sorting;

import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MergeSortService {
  public List<RankedProduct> sortDescByScore(List<RankedProduct> input){
    if (input.size() <= 1) return input;
    int mid = input.size()/2;
    var left = sortDescByScore(new ArrayList<>(input.subList(0, mid)));
    var right = sortDescByScore(new ArrayList<>(input.subList(mid, input.size())));
    return merge(left, right);
  }
  private List<RankedProduct> merge(List<RankedProduct> L, List<RankedProduct> R){
    var out = new ArrayList<RankedProduct>(L.size()+R.size());
    int i=0,j=0;
    while(i<L.size() || j<R.size()){
      if (j==R.size() || (i<L.size() && L.get(i).getScore() >= R.get(j).getScore()))
        out.add(L.get(i++));
      else out.add(R.get(j++));
    }
    return out;
  }
}
