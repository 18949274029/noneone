package noneoneblog.core.persist.service;

import noneoneblog.core.data.Game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author leisure
 */
public interface GameService {
	Page<Game> getGameLists(Pageable pageable);

}
