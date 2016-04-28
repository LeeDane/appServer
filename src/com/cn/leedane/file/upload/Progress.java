package com.cn.leedane.file.upload;

import org.apache.commons.fileupload.ProgressListener;

public class Progress implements ProgressListener{
	
	//文件总长度
	private long length;
	
	//当前已经上传的文件长度
	private long currentLength;
	
	//上传是否成功
	private boolean isComplete;

	@Override
	public void update(long bytesRead, long contentLength, int items) {
		System.out.println("执行Progress的update()方法");
		this.currentLength = bytesRead;  
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getCurrentLength() {
		return currentLength;
	}

	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
}
