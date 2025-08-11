package io.github.juanpimr2.inditex_ranking.service.ranking.port;

import io.github.juanpimr2.inditex_ranking.domain.dto.request.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankResponse;

/** Puerto de entrada: calcula el ranking y devuelve un wrapper con la lista rankeada. */
public interface RankingInputPort {
    RankResponse rank(RankRequest req);
}
