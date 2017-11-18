package test;

import noneoneblog.base.utils.ImageRemarkUtil;

import org.junit.Test;


public class ImageRemarkUtilTest {
  @Test
  public void testDraw(){
		String srcImgPath = "d:/1111.jpg";
		String iconPath = "d:/3.jpg";
		String targerIconPath2 = "d:/1111.jpg";
		System.out.println("给图片添加水印图片开始...");
		  ImageRemarkUtil.markImageByIcon(iconPath, srcImgPath, targerIconPath2, -45);
		System.out.println("给图片添加水印图片结束...");
	
  }
	
}