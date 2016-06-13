package com.cn.leedane.test;
import java.io.IOException;

import com.cn.leedane.Utils.Base64ImageUtil;

/***
 * 图像相关的测试类
 * @author LeeDane
 * 2015年7月3日 下午6:24:57
 * Version 1.0
 */
public class ImageTest {

	public static void main(String[] args) {
		
		/*String oldPath = "F:\\";
		String oldFileName = "aa.jpg";
		
		String newPath = "E:\\";
		String newFileName = "aa5.jpg"; 
		ImageUtil imageUtil = new ImageUtil(oldPath, oldFileName,newPath,newFileName,100,100);
		//imageUtil.setProportion(true);
		//imageUtil.setProportionPer(0.01f);
		//imageUtil.setImageType(BufferedImage.TYPE_INT_RGB);
		imageUtil.setHighQuality(true);
		System.out.println(imageUtil.ConverBigImageToSmallImage());*/
		
		String filePath = "G://mx4image.jpg";
		try {
			Base64ImageUtil.convertImageToBase64(filePath, null);
			System.out.println("处理完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ImageUtil.convertBase64ToImage("F://qq1.png", ImageUtil.convertImageToBase64(filePath));

	}

}
