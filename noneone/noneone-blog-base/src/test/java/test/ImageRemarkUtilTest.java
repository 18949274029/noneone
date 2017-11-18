package test;

import noneoneblog.base.utils.ImageRemarkUtil;

import org.junit.Test;


public class ImageRemarkUtilTest {
  @Test
  public void testDraw(){
		String srcImgPath = "d:/1111.jpg";
		String iconPath = "d:/remark.jpg";
		System.out.println("给图片添加水印图片开始...");
		  ImageRemarkUtil.markImageByIcon(iconPath, srcImgPath, srcImgPath, -45);
		System.out.println("给图片添加水印图片结束...");
	
  }
	
}