package com.cn.leedane.test;
import com.cn.leedane.Utils.WebserviceUtil;

public class Server200CloudTest {

	public static void main(String[] args) {
		String url = "http://192.168.12.31:8043/services/EngineAPIWebService";
		System.out.println("start...");
		WebserviceUtil client = new WebserviceUtil(url);
		//610123197207143275
		String xml = 
					"<business>"+
						"<baseInfo>"+
							"<funcCode>PeerRelationship</funcCode>"+
							"<version>1.0</version>"+
						"</baseInfo>"+
						"<sendData>"+
							"<idCard>143191048950770016</idCard>"+
							"<ip></ip>"+
							"<userIdCard>230403197901010018</userIdCard>"+
							"<xm>ldx</xm>"+
						"</sendData>"+
					"</business>";
		
		String result = client.call("execute",new Object[]{xml});
			
		System.out.println("服务器返回的结果："+result);
		
	}
}
