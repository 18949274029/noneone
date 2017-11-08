package noneoneblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import noneoneblog.core.data.FriendLink;
import noneoneblog.core.persist.dao.FriendLinkDao;
import noneoneblog.core.persist.entity.FriendLinkPO;
import noneoneblog.core.persist.service.FriendLinkService;
import noneoneblog.core.persist.utils.BeanMapUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leisure
 */
@Service
@Transactional
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkDao friendLinkDao;

    @Autowired
    private ServletContext servletContext;

    @Override
    public void save(FriendLink friendLink) {
        FriendLinkPO friendLinkPO = new FriendLinkPO();

        BeanUtils.copyProperties(friendLink, friendLinkPO);

        friendLinkDao.save(friendLinkPO);

        servletContext.setAttribute("friendLinks", findAll());
    }

    @Override
    public void delete(long id) {
        FriendLinkPO friendLinkPO = friendLinkDao.findOne(id);
        friendLinkDao.delete(friendLinkPO);
        servletContext.setAttribute("friendLinks", findAll());
    }

    @Override
    public FriendLink findById(long id) {
        FriendLink friendLink = BeanMapUtils.copy(friendLinkDao.findOne(id));
        return friendLink;
    }

    @Override
    public List<FriendLink> findAll() {
        List<FriendLinkPO> linkPOs = friendLinkDao.findAll();

        List<FriendLink> rets = new ArrayList<>();
        for (FriendLinkPO po : linkPOs) {
            FriendLink m = new FriendLink();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }
}
