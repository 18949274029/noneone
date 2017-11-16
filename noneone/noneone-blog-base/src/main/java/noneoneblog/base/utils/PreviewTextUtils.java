
package noneoneblog.base.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Created by leisure
 */
public class PreviewTextUtils {
    /**
     * 提取纯文本
     * @param html 代码
     * @return string
     */
    public static String getText(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.none()).trim();
    }

    /**
     * 提取纯文本
     * @param html 代码
     * @param length 提取文本长度
     * @return string
     */
    public static String getText(String html, int length){
        String text = getText(html);
        text = StringUtils.abbreviate(text, length);
        return text;
    }

    /**
     * 以下标签可以通过 (b, em, i, strong, u. 纯文本)
     * @param html 代码
     * @return string
     */
    public static String getSimpleHtml(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.simpleText());
    }

    /**
     * 获取文章中的img url
     * @param html 代码
     * @return string
     */
    public static String getImgSrc(String html) {
        if (html == null)
            return null;
        Document doc = Jsoup.parseBodyFragment(html);
        Element image = doc.select("img").first();
        return image == null ? null : image.attr("src");
    }

    public static void main(String[] args) {
        System.out.println(PreviewTextUtils.getText("<script>alert</script>test  ", 5));
        String s = " <p>&lt;iframe height=498 width=510 src='http://player.youku.com/embed/XMzE0OTkyNTQ5Mg==' frameborder=0 'allowfullscreen'&gt;&lt;/iframe&gt; &nbsp; sadasd</p>";
        System.out.println(getChangeHTML(s));
    }
    
    /**
     * 替换文本中的< > 符号
     */
    public static String getChangeHTML(String fromString){
    	if (StringUtils.isNotEmpty(fromString)) {

    		fromString =  StringEscapeUtils.unescapeHtml4(fromString);
//    		fromString = fromString.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&nbsp;", " ").replaceAll("&#x3D;", "=").replaceAll("&quot;", "\"");
		}
    	return fromString; 
    }
    
}
