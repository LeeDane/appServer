package com.cn.leedane.webservice;

import com.cn.leedane.Utils.StringUtil;

public class Server200CloudTest {
	public static void main(String[] args) {
		String url = "http://192.168.12.175:8043/services/EngineAPIWebService";
		AxisWebServiceClient client = new AxisWebServiceClient(url);
		
		String xml = 
					"<business>"+
						"<baseInfo>"+
							"<funcCode>LoadPeople</funcCode>"+
							"<version>1.0</version>"+
						"</baseInfo>"+
						"<sendData>"+
							"<idCard>143191048950770016</idCard>"+
							"<ip></ip>"+
							"<userIdCard>230403197901010018</userIdCard>"+
							"<xm>禹健</xm>"+
						"</sendData>"+
					"</business>";
		String result = client.call("execute",new Object[]{xml});
		if(!StringUtil.isNull(result)){			
			System.out.println("获得200上检索系统返回的字符串："+result);
		}else{
			System.out.println("获得200上检索系统返回的失败字符串："+result);
		}
	}
}
