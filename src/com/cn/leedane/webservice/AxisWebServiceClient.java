package com.cn.leedane.webservice;

import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
public class AxisWebServiceClient {
	private Call call=null;
	/**
	 * ��ʼ ����
	 * @param url
	 */
	public AxisWebServiceClient(String url){		
		try {
			Service service = new Service();
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
		} catch (ServiceException e) {
			
		}		
	}
	/**
	 * ���� webservice
	 * @param opName
	 * @param args
	 * @return
	 */
	public String call(String opName,Object[] args) {
		String result="";
		try{
			if(call!=null){
				call.setOperationName(opName);
				Object[] args2=new Object[args.length];
				for(int i=0;i<args.length;i++){
					args2[i]=args[i];
				}
				result=(String)call.invoke(args2);
			}
		}catch(Exception e){
			
		}	
		return result;
	}
	
	/**
	 * ���� webservice
	 * @param opName
	 * @return
	 */
	public String call(String opName) {
		String result="";
		try{
			if(call!=null){
				call.setOperationName(opName);
				result=(String)call.invoke(new Object[] {});
			}
		}catch(Exception e){
			
		}	
		return result;
	}
	
	/**
	 * ���� webservice
	 * @param opName
	 * @return
	 */
	public void invokeOneWay(String opName,Object[] objects) {		
		try{			
			if(call!=null){
				call.setOperationName(opName);
				call.invokeOneWay(objects);
			}
		}catch(Exception e){
			
		}	
		
	}
}
