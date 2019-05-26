package com.sailing.sdp.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.sailing.sdp.entity.UserInfo;

public class ElasticsearchSupportTest {

	static String indexName = "user_db";
	
	static String type = "user_info";
	
	@Test
	public void testCheckConn() {
		boolean flag = ElasticsearchSupport.checkConn(Constants.CLUSTER_NAME, Constants.IP, Constants.PORT);
		System.out.println("flag="+flag);
	}

	@Test
	public void createEmpInex(){
		String indexName ="test_shard_index";
		int shardsNum = 7;
		int replicasNum = 2;
		boolean empInex = ElasticsearchSupport.createEmpInex(indexName, shardsNum, replicasNum);
		System.out.println(empInex);
	}

	@Test
	public void testCreateEmpIndexByMapping1() {
	List<FieldInfo> list = new ArrayList<FieldInfo>();
		FieldInfo info = new FieldInfo();
		info.setField("userName");
		info.setType("string");
		list.add(info);
		
		FieldInfo info1 = new FieldInfo();
		info1.setField("age");
		info1.setType("long");
		list.add(info1);
		
		FieldInfo info3 = new FieldInfo();
		info3.setField("birth");
		info3.setType("Date");
		list.add(info3);
		
		String indexName = "sip2";
		String type = "doc";
		
		ElasticsearchSupport.createEmpIndexByMapping(indexName, type, list);
	}

	@Test
	public void testAddIndexByList() {
		List<UserInfo> list = new ArrayList<UserInfo>();
		for(int i=0;i<100;i++) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUuid("uuid_"+i);
			userInfo.setUserName("zs"+i);
			userInfo.setFullName("zhangsan"+i);
			userInfo.setAge(10);
			userInfo.setBirthDate(new Date());
			userInfo.setDesc("非常好的孩子");
			list.add(userInfo);
		}
		
		 String indexName = "user_db";
		
		String type = "user_info";
		
		ElasticsearchSupport.addIndexByList(indexName, type, list);
	}
	
	@Test
	public void testAddIndexByObj() {
			UserInfo userInfo = new UserInfo();
			userInfo.setUuid("uuid001");
			userInfo.setUserName("lis");
			userInfo.setFullName("lisi");
			userInfo.setAge(10);
			userInfo.setBirthDate(new Date());
			userInfo.setDesc("非常好的孩子");
		   String indexName = "user_db1";
		   String type = "user_info2";
		   ElasticsearchSupport.addIndexByObj(indexName, type, userInfo);
	}
	
	
	
	@Test
	public void testCreateEmpIndexByMapping() {
		ElasticsearchSupport.createEmpIndexByMapping(indexName, type);
	}
	
	@Test
	public void testAddIndexByJson() {
		ElasticsearchSupport.addIndexByJson(indexName, type);
	}
	
	@Test
	public void testAddIndexByMap() {
		ElasticsearchSupport.addIndexByMap(indexName, type);
	}
	
	@Test
	public void testDelIndex() {
		ElasticsearchSupport.delInex(indexName);
	}
	
	@Test
	public void testgetIndexDataById() {
		String indexName = "user_db1";
		String type = "user_info1";
		String id = "uuid_45";
		String indexData = ElasticsearchSupport.getIndexDataById(indexName, type, id);
		System.out.println("indexData="+indexData);
	}
	
	@Test
	public void testUpdateIndexDataById() {
		String indexName = "user_db1";
		String type = "user_info1";
		String id = "uuid_5";
		String result = ElasticsearchSupport.updateIndexDataById(indexName, type, id);
		System.out.println("result="+result);
	} 

	@Test
	public void testDeleteIndexDataById() {
		String indexName = "user_db1";
		String type = "user_info1";
		String id = "uuid_5";
		String result = ElasticsearchSupport.deleteIndexDataById(indexName, type, id);
		System.out.println("result="+result);
	}
	
}
