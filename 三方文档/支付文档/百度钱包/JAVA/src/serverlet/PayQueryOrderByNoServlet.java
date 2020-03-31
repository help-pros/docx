package serverlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import baifubao.BfbSdkComm;
//import org.dom4j.Document;  



public class PayQueryOrderByNoServlet extends HttpServlet {
 
	
	private ServletConfig scon = null;
	
	public PayQueryOrderByNoServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}
	/* 通过百付宝订单号查询接口查询订单信息，返回该订单是否已经支付成功
	 *
	 * @param string $order_no        	
	 * @return string | boolean 订单支付成功返回订单查询结果，其它情况（包括查询失败以及支付状态不是支付成功的情况）返回false
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 *接收post请求的方法
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        /**
         * 1、设置编码
         */
		response.setContentType("text/html;charset=gbk");  
		response.setHeader("content-type","text/html;charset=gbk");
		request.setCharacterEncoding("gbk");
		response.setCharacterEncoding("gbk");
		
		PrintWriter out = response.getWriter();
		//打印日志
	    Logger logger =new  baifubao.BfbSdkComm().printLog("PayQueryOrderByNoServlet");

		logger.log(Level.INFO, "response编码："+response.getCharacterEncoding());
		/**
		 * 2、获取 web.xml的常量值
	     */
	    //商品分类号
	    String  service_code=
	    		"service_code="+scon.getServletContext().getInitParameter("BFB_QUERY_INTERFACE_SERVICE_ID");
	    //商户号
	    String sp_no="sp_no=" +scon.getServletContext().getInitParameter("SP_NO");
	   
	    String output_charset="output_charset=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_ENCODING");
	    //返回格式
	    String output_type="output_type=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_OUTPUT_FORMAT");
	    //版本
	    String version="version=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_VERSION");
	    //加密方式md5或者hash
	    String sign_method="sign_method=" +scon.getServletContext().getInitParameter("SIGN_METHOD_MD5");
	    //秘钥
	    String SP_KEY=scon.getServletContext().getInitParameter("SP_KEY");
	    //提交地址
	    String BFB_QUERY_ORDER_URL=scon.getServletContext().getInitParameter("BFB_QUERY_ORDER_URL");
	    /**
	     *3、获取pay_unlogin.html页面提交的变量值_订单号
	     */
	    //订单号
	    String order_no ="order_no="+request.getParameter("order_no");
	    
		//给提交参数数值赋值
		String[]array={
				order_no,
				output_type,
				output_charset,
				service_code,
				sign_method,
				sp_no,
				version};
		/**
		 * 4、调用bfb_sdk_comm里create_baifubao_pay_order_url方法生成百付宝即时到账支付接口URL(不需要登录)
		 *
		 */
		String getURL=new BfbSdkComm().create_baifubao_pay_order_url(array,array,BFB_QUERY_ORDER_URL);
        /**
         * 5、向百付宝发起订单查询请求
         */
	    HttpClient httpClient =  new  HttpClient();    
	    httpClient.getParams().setParameter(
	    	      HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
	    // 设置 Http 连接超时为5秒    
	    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( 5000 );   
	     /* 生成 GetMethod 对象并设置参数 */    
	    GetMethod getMethod =  new  GetMethod(getURL);    
	     // 设置 get 请求超时为 5 秒    
	    getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,  5000 );   
	      // 设置请求重试处理，用的是默认的重试处理：请求三次    
	    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new  DefaultHttpMethodRetryHandler());         
	    String responseStr = "" ;    
	     /**
	      * 6、 执行 HTTP GET 请求
	      */ 
	     try  
	     {    
	         int  statusCode = httpClient.executeMethod(getMethod); 
	         /*  判断访问的状态码 */    
	         if  (statusCode != HttpStatus.SC_OK) 
	         {    
	            out.println( "HttpStatus.SC_OK :" + getMethod.getStatusLine());   
	          }    
	        /**
	         *  7、 解析百付宝返回的查询结果(返回结果是xml格式的)
	         **/    
	        // HTTP响应头部信息，这里简单打印                 	         
	        // 读取 HTTP 响应内容，这里简单打印网页内容    
            String result = getMethod.getResponseBodyAsString();
            logger.log(Level.INFO, "getMethod.getResponseBodyAsString()返回的XML原文:" +result+"XML原文返回值"+"<br/>");
	        //解析返回的XML
	        Document doc = null;
	        doc = DocumentHelper.parseText(result); 
	        // 获取根节点
	        Element root = doc.getRootElement(); 
	        List listTemp = root.elements();  
	        String []resultArry=new String[listTemp.size()];
	        //拼接返回结果，并打印到页面
	        StringBuffer strBur=new StringBuffer();
	        //循环取xml的值，并赋给数组resultArry
	        int i=0;
	        for(Iterator its =  listTemp.iterator();its.hasNext();){   
	            Element chileEle = (Element)its.next();  
	            resultArry[i]=chileEle.getName()+"="+chileEle.getStringValue();
	            strBur.append(resultArry[i]+"<br/>");
	            i++;
	        }   
	        out.println("查询结果打印============"+"<br/>");
	        out.println(strBur.toString());
	        out.println("查询结果打印结束============"+"<br/>");
	        //获取签名字段
	        String signTemp=resultArry[resultArry.length-1];
	        String resultSign=signTemp.substring(signTemp.indexOf("=")+1,signTemp.length());
	        System.out.println("查询返回的sing:"+resultSign);
	        logger.log(Level.INFO, "根节点：" + root.getName()); // 拿到根节点的名	  
	        //获取到返回结果以后，需要再进行一次签名，确保返回结果是百付宝返回
	        String[] SignArry=new String [resultArry.length-1];
	        for(int j=0;j<resultArry.length-1;j++)
	        {
	        	SignArry[j]=resultArry[j];
	        }
    	    //判断查询请求的结果,0查询成功，详细情况请参看文档
    	   if(root.element("query_status").getText().equals("0"))
    	   {   
    		   out.println("查询成功: "+"<br/>");
    		   logger.log(Level.INFO, "查询成功:\n\r  ");
    		   /**
   	            * 8、 调用签名方法make_sign，对返回结果进行验签
   	            **/  
	    	   String RspLocalSign=new BfbSdkComm().make_sign(SignArry);
	    	   out.println("本地签名串            :" + RspLocalSign + "本地签名串<br/>  ");
	    	   out.println("百付宝返回签名串:" + resultSign + "百付宝返回签名串<br/> " );
	    	   logger.log(Level.INFO, "本地签名串           : " + RspLocalSign + "本地签名串  \n\r");
	    	   logger.log(Level.INFO, "百付宝返回签名串: " + resultSign + "百付宝返回签名串" );
	    	   if(RspLocalSign.equalsIgnoreCase(resultSign))
	    	   {
	    		   out.println("查询成功，验证签名通过 <br/>  ");
	    		   logger.log(Level.INFO, "查询成功，验证签名通过\n\r  ");
	    	   }else 
	    	   {
	    		   out.println("查询成功，验证签名失败 <br/>  ");
	    		   logger.log(Level.INFO, "发起查询请求异常，请检查httpClient\n\r" );
	    	   }
    	   }
	 
	      }catch (HttpException e) {    
	         // 发生致命的异常，可能是协议不对或者返回的内容有问题   
	        out.println( "HttpException异常" );   
	        logger.log(Level.INFO, "HttpException异常，请检查代码" );
	      } catch (DocumentException e) 
	      {
		        out.println("解析xml出错");
		        logger.log(Level.INFO, "解析xml出错\n\r");
		  } 
	      finally  
	      {    
	         /* 释放连接 */    
	    	  getMethod.releaseConnection();    
	      }    
    }   
	public String getServletInfo() {
		return "pay_query_order_by_noServlet";
	}
	
	public void init(ServletConfig config) throws ServletException {
		scon = config;
	}

	
}
