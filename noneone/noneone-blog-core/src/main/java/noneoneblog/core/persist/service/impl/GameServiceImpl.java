package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import noneoneblog.core.data.Game;
import noneoneblog.core.persist.dao.GameDao;
import noneoneblog.core.persist.entity.GamePO;
import noneoneblog.core.persist.service.GameService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

	@Resource
	private GameDao gameDao;

	@Override
	public Page<Game> getGameLists(Pageable pageable) {
		Page<GamePO> page = gameDao.findAllByOrderByCreatedDesc(pageable);
		List<Game> games = new ArrayList<Game>();
		page.getContent().forEach(po -> {
			games.add(BeanMapUtils.copy(po));
		});

		return new PageImpl<>(games, pageable, page.getTotalElements());
	}

}
