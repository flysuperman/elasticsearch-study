package com.sailing.sdp.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sailing.sdp.entity.UserInfo;
public class ElasticsearchSupport {

	  /**
     * 获取客户端
     * TODO :这块最好单例
     * @return
     */
    public synchronized static TransportClient getTransportClient(String clusterName,String ip,int port) {
        TransportClient client = null ;
        try {
            Settings settings = Settings.builder()
            		 .put("cluster.name", clusterName)
            		 .put("client.transport.sniff", true)
                     .put("client.transport.ping_timeout", "30s").build();
            client = new PreBuiltTransportClient(settings);
            String[] ips = ip.split(",");
            for (int i = 0; i < ips.length; i++) {
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(ips[i]),port));
            }
        } catch (UnknownHostException e) {
           System.out.println("创建客户端失败...");
        	e.printStackTrace();
        }
        return client;
    }
    
    /**
     * 关闭客户端
     * @param client
     */
	public static void closeClient(TransportClient client) {
		if(null != client) {
			client.close();
		}
	}
	
	/**
	 * 测试连接是否可用
	 * @param clusterName
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean checkConn(String clusterName,String ip,int port) {
		boolean flag =true;
		TransportClient client = null;
		try {
			client = getTransportClient(clusterName,ip,port);
		} catch (Exception e) {
		    System.out.println("连接els失败");
			flag = false;
		}finally {
			closeClient(client);
		}
		return flag;
	}
	
	
	/**
	 * 创建空索引
	 * @param indexName 索引名称
	 */
	public static boolean createEmpInex(String indexName) {
		boolean flag =true;
		TransportClient client = null;
		try {
			 Settings settings = Settings.builder()
	 				 .put("index.number_of_shards", 3)
	 		         .put("index.number_of_replicas", 0)
                     .build();
			client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
			IndicesAdminClient admin = client.admin().indices();
			flag = admin.prepareCreate(indexName)
					    .setSettings(settings)
					    .execute().actionGet().isAcknowledged();
		} catch (Exception e) {
		    System.out.println("创建els索引失败"+indexName);
			flag = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
		return flag;
	}
	
	
	/**
	 * 删除索引
	 * @param index
	 */
	public static boolean delInex(String indexName) {
		boolean flag =true;
		TransportClient client = null;
		try {
			client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
			IndicesAdminClient admin = client.admin().indices();
			flag = admin.prepareDelete(indexName)
					    .execute().actionGet().isAcknowledged();
		} catch (Exception e) {
			System.out.println("删除els索引"+indexName+" 失败");
			flag = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
		return flag;
	}
	
	  /**
     * 根据信息自动创建索引与mapping
     * 构建mapping描述    有问题
     * @param fieldInfoList  字段信息
     * @param client   es客户端
     * @return
     */
    public static boolean  createEmpIndexByMapping(String indexName, String type,List<FieldInfo> fieldInfoList) {
    	
        boolean result =true;
		TransportClient client = null;
		XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties"); //设置字段属性
            
            for(FieldInfo info : fieldInfoList){
                String field = info.getField();
                String dateType = info.getType();
                if(dateType == null || "".equals(dateType.trim())){
                    dateType = "String";
                }
                
                dateType = dateType.toLowerCase();
                int participle = info.getParticiple();
                if("string".equals(dateType)){
                    if(1 == participle) {
                        mapping.startObject(field)
                                .field("type","text")
                                .field("analyzer","ik_smart")
                                .endObject();
                    }else if(2 == participle){
                        mapping.startObject(field)
                                .field("type","text")
                                .field("analyzer","ik_max_word")
                                .endObject();
                    }else {
                        mapping.startObject(field)
                                .field("type","keyword")
                                .field("index","not_analyzed")
                                .endObject();
                    }
 
                }else if("date".equals(dateType)){
                    mapping.startObject(field)
                            .field("type",dateType)
                            .field("format","yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
                            .endObject();
                }else {
                    mapping.startObject(field)
                            .field("type",dateType)
                            .field("index","not_analyzed")
                            .endObject();
                }
 
            }
            mapping.endObject()
                   .endObject();
            //获取client
            client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
            IndicesAdminClient admin = client.admin().indices();
            //创建索引
            result = admin.prepareCreate(indexName)
					      .addMapping(type, mapping)
					      .execute().actionGet().isAcknowledged();
            
        } catch (Exception e) {
        	System.out.println("创建els索引失败"+indexName);
        	result = false;
			e.printStackTrace();
        }finally {
			closeClient(client);
		}
		return result;
    }
    
    
    public static boolean  createEmpIndexByMapping(String indexName, String type) {
    	boolean result =true;
  		TransportClient client = null;
  		XContentBuilder mapping = null;
    	try {
    		
			mapping = XContentFactory.jsonBuilder()
			        .startObject()
			            .startObject("properties") //设置属性
			              .startObject("uuid")
			                .field("type","text") //设置字段uuid
			              .endObject()
			              .startObject("userName")
			                .field("type","text")
			              .endObject()
			              .startObject("fullName")
			                .field("type","text") 
			              .endObject()
			              .startObject("age")
			                 .field("type","long")
			              .endObject()
			              .startObject("birthDate")
			                 .field("type","date")  //设置Date类型
			                 .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
			              .endObject()
			              .startObject("desc")
			                 .field("type","text")  
			              .endObject()
			          .endObject()//properties end
			        .endObject();
	            
	           //获取client
	            client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	            IndicesAdminClient admin = client.admin().indices();
	            
	            result = admin.prepareCreate(indexName)//创建索引
						      .addMapping(type, mapping)//创建表
						      .execute().actionGet().isAcknowledged();
	            
		} catch (Exception e) {
			System.out.println("创建els索引失败"+indexName);
        	result = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    /**
     * 插入索引
     * 1、当索引是通过静态mapping创建的，则将数据直接插入进去
     * 2、如果索引没有则根据数据本身的类型，创建动态mapping，将数据插入进去
     * 3、创建索引名称不能为大写
     * @param indexName
     * @param type
     * @param userinfo
     * @return
     */
    public static boolean  addIndexByObj(String indexName, String type,UserInfo userinfo) {
    	boolean result =true;
  		TransportClient client = null;
  		try {
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        IndexResponse response = client.prepareIndex(indexName, type, userinfo.getUuid())
			  .setSource(XContentFactory.jsonBuilder()
			  .startObject()
			  .field("userName", userinfo.getUserName())
			  .field("fullName", userinfo.getFullName())
			  .field("age",userinfo.getAge())
			  .field("birthDate", userinfo.getBirthDate())
			  .endObject()).get();
	        System.out.println(
	        "索引名称:" + response.getIndex() 
	        + "\n类型:" + response.getType()
            + "\n文档ID:" + response.getId() 
            + "\n当前实例状态:" + response.status());
		} catch (Exception e) {
			System.out.println("增加索引数据失败"+indexName);
        	result = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    /**
     * 索引插入json对象或者json字符串
     * @param indexName
     * @param type
     * @return
     */
    public static boolean  addIndexByJson(String indexName, String type) {
    	
    	boolean result =true;
  		TransportClient client = null;
  		try {
  			 String jsonStr = "{"
  					 + "\"uuid\":\"张三11\"," 
  					 + "\"userName\":\"张三\"," 
  					 + "\"birthDate\":\"2017-11-30 11:11:11\"," 
  					 + "\"desc\":\"你好李四\"" 
  					 + "}";
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        IndexResponse response = client.prepareIndex(indexName, type)
			  .setSource(jsonStr,XContentType.JSON).get();
	        System.out.println(
	        "索引名称:" + response.getIndex() 
	        + "\n类型:" + response.getType()
            + "\n文档ID:" + response.getId() 
            + "\n当前实例状态:" + response.status());
		} catch (Exception e) {
			System.out.println("增加索引数据失败"+indexName);
        	result = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    /**
     * 索引插入json对象或者json字符串
     * @param indexName
     * @param type
     * @return
     */
    public static boolean  addIndexByMap(String indexName, String type) {
    	boolean result =true;
  		TransportClient client = null;
  		try {
  			Map<String, Object> map = new HashMap<String,Object>();
  			map.put("uuid", "001");
  			map.put("userName", "wangwu");
  			map.put("birthDate","2017-11-30 11:11:11");
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        IndexResponse response = client.prepareIndex(indexName, type)
			  .setSource(map).get();
	        System.out.println(
	        "索引名称:" + response.getIndex() 
	        + "\n类型:" + response.getType()
            + "\n文档ID:" + response.getId() 
            + "\n当前实例状态:" + response.status());
		} catch (Exception e) {
			System.out.println("增加索引数据失败"+indexName);
        	result = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    
    public static boolean  addIndexByList(String indexName, String type,List<UserInfo> list) {
    	boolean result =true;
  		TransportClient client = null;
  		try {
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	  		for (UserInfo userinfo: list) {
	  			client.prepareIndex(indexName, type, userinfo.getUuid())
	  				  .setSource(XContentFactory.jsonBuilder()
	  				  .startObject()
	  				  .field("userName", userinfo.getUserName())
	  				  .field("fullName", userinfo.getFullName())
	  				  .field("age",userinfo.getAge())
	  				  .field("birthDate", userinfo.getBirthDate())
	  				  .field("desc", userinfo.getDesc())
	  				  .endObject()).get();
	  		}
		} catch (Exception e) {
			System.out.println("增加索引数据失败"+indexName);
        	result = false;
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    /**
     * 获取索引数据
     * @param indexName 索引名称
     * @param type type
     * @param id
     * @return
     */
    public static String getIndexDataById(String index,String type,String id) {
    	String result = null;
  		TransportClient client = null;
  		try {
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        GetResponse response = client.prepareGet(index, type, id).get();
	        result =  response.getSourceAsString();
		} catch (Exception e) {
			System.out.println("获取索引数据失败："+index);
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    
    /**
     * 更新索引数据
     * @param indexName 索引名称
     * @param type type
     * @param id
     * @return
     */
    public static String updateIndexDataById(String index,String type,String id) {
    	String result = null;
  		TransportClient client = null;
  		JSONObject obj = new JSONObject();
  		obj.put("userName", "xj");
  		obj.put("fullName", "希杰");
  		try {
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        UpdateResponse updateResponse = client.prepareUpdate(index, type, id).setDoc(JSON.toJSONString(obj),XContentType.JSON).get();
	        result =  updateResponse.status().name();
		} catch (Exception e) {
			System.out.println("更新索引数据失败："+index);
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
    /**
     * 根据id删除索引数据
     * @param indexName 索引名称
     * @param type type
     * @param id
     * @return
     */
    public static String deleteIndexDataById(String index,String type,String id) {
    	String result = null;
  		TransportClient client = null;
  		try {
	  		 //获取client
	        client = getTransportClient(Constants.CLUSTER_NAME,Constants.IP,Constants.PORT);
	        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).get();
	        result =  deleteResponse.status().name();
		} catch (Exception e) {
			System.out.println("删除索引数据失败："+index);
			e.printStackTrace();
		}finally {
			closeClient(client);
		}
    	return result;
    }
    
}


