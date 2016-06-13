package com.cn.leedane.file.download;

/**
 * 文件的下载实体类
 * @author LeeDane
 * 2015-5-25 16:51
 * Version 1.0
 */
public class Download {
	
	private long lenght;  //总的文件大小
	private long currentLenght; //当前下载的大小
	private boolean isComplete; //是否完成

	public long getLenght() {
		return lenght;
	}

	public void setLenght(long lenght) {
		this.lenght = lenght;
	}

	public long getCurrentLenght() {
		return currentLenght;
	}

	public void setCurrentLenght(long currentLenght) {
		this.currentLenght = currentLenght;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	
}
