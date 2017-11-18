package test;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * @author leisure
 */
public class PreviewTextUtilsTest {

    public static void main(String[] args) {
        String html = "<p>这是一个测试文本</p><p><img src='test.png'></p><script>alert('1');</script>  select * from tb_user where 1=1";

        System.out.println(HtmlUtils.htmlEscape(html));

        System.out.println(StringEscapeUtils.escapeSql(html));
    }

}
