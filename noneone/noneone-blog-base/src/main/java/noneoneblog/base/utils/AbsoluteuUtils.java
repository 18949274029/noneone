package noneoneblog.base.utils;

import noneoneblog.base.context.Global;
import noneoneblog.base.lang.Consts;

/**
 * 
 * @author leisure
 *
 */
public class AbsoluteuUtils {

	/**
	 * 判断是否是absolute存储
	 * @return
	 */
	public static boolean isAbsolute() {
		if (Consts.ABSOLUTE.equals(Global.getConfig("store.repo"))) {
			return true;
		}
		return false;
	}

}
