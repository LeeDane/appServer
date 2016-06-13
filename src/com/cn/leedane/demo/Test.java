package com.cn.leedane.demo;

public class Test implements Static{
	 public String staticStr;
	 
	 /**
	  * @return the staticStr
	  */
	 public String getStaticStr() {
	  return staticStr;
	 }

	 /**
	  * @param staticStr the staticStr to set
	  */
	 public void setStaticStr(String staticStr) {
	  this.staticStr = staticStr;
	 }
	 
	 public static void main(String[] a){
		 Test t = new Test();
		 t.setStaticStr("testing");
		 System.out.println(t.getStaticStr());
	 }
	}