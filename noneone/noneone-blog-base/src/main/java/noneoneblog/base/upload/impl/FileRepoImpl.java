package noneoneblog.base.upload.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

/**
 * @author leisure
 *
 */
@Service("fileRepo")
public class FileRepoImpl extends AbstractFileRepo {
	private static String KEY = "absolute";

	@PostConstruct
	public void init() {
		fileRepoFactory.addRepo(KEY, this);
	}

	@Override
	public String getRoot() {
		return appContext.getRoot();
	}

}
