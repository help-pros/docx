package serverlet;

import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import baifubao.BfbSdkComm;

public class ReturnServlet extends HttpServlet {

	public ReturnServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			 /**
	         * 1、设置编码
	         */
			request.setCharacterEncoding("gbk");
			response.setContentType("text/html;charset=gbk");
			response.setCharacterEncoding("gbk");

			PrintWriter out = response.getWriter(); 
			//打印日志
			Logger logger =new  baifubao.BfbSdkComm().printLog("ReturnServlet");
			logger.log(Level.INFO, "response编码："+response.getCharacterEncoding());
			
		    
		    /**
		    * 2、支付返回串,查看返回那些字段，返回的字段不管是否为空，均参与签名
		    */
		    String getStrPre=request.getQueryString();
	    	//url解码
		    String getStr=java.net.URLDecoder.decode(getStrPre, "gbk");
		    logger.log(Level.INFO,"浏览器地址栏中？后面的字符串:"+getStr+";打印结束");
		    //把返回的参数用split取出来
		    String  []resultStrTemp=getStr.split("&");
		    String  []resultStr=new String[resultStrTemp.length-1];
		    //取签名串
		    String signtemp=resultStrTemp[resultStrTemp.length-1];
		    String sign=signtemp.substring(signtemp.indexOf("=")+1,signtemp.length());

		    for(int i=0;i<resultStrTemp.length-1;i++){ 
		    	resultStr[i]=resultStrTemp[i];
		    	System.out.println(resultStr[i]);
		    }
			/**
			 *3、 调用bfb_sdk_comm里make_sign方法生成签名串
			 * 
			 */
	        
			//签名拼接，需要去掉返回的签名串字段sign
			String  Localsign=new BfbSdkComm().make_sign(resultStr);
			logger.log(Level.INFO,"本地签名串            ："+Localsign);
			logger.log(Level.INFO,"百付宝返回签名串："+sign+"<br/>");
			//打印完成关闭日志
			logger.setLevel(Level.OFF);
			//比对签名
			//out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			
			if(request.getParameter("pay_result").equals("1"))
			{
				
				if(sign.trim().equalsIgnoreCase(Localsign.trim()))
				{    
					
					
					/**
					 * 支付通知结果的回执
					 * 作用：	收到通知，并验证通过，向百付宝发起回执。百付宝GET请求商户的return_url页面，商户这边的响应
					 * 		中必须包含以下部分，百付宝只有接收到特定的响应信息后，才能确认商户已经收到通知，并验证通过。这样
					 * 		百付宝才不会再向商户发送支付结果通知
					 */
					out.println("<HTML><head>");
					out.println("<meta name=\"VIP_BFB_PAYMENT\" content=\"BAIFUBAO\">");
					out.println("</head>");
					out.println("<body>");
					out.println("支付成功，验签通过"+"订单号："+request.getParameter("order_no")+"<br/>");
					out.println("百付宝返回的签名串 :"+sign+"<br/>");
					out.println("本地生成的签名串     :"+Localsign+"<br/>");
					out.println("</body></html>");
					/**
					*   1、重要：接收到百度钱包的后台通知后，商户须返回特定的HTML页面。该页面应该满足以下要求：
					*	2、重要：HTML头部须包括<meta name="VIP_BFB_PAYMENT" content="BAIFUBAO">
					*	3、重要： 商户可以通过百付宝订单查询接口再次查询订单状态，二次校验
					*	4、重要： 该查询接口存在一定的延迟，商户可以不用二次校验，信任后台的支付结果通知便行
					* */
				}else if(!sign.trim().equalsIgnoreCase(Localsign.trim()))
				{
					out.println("支付成功，验签失败"+"<br/>");
				}   
			}else{
				out.println("支付失败"+"<br/>");
			}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public String getServletInfo() {
		return "returnServlet";
	}


	public void init() throws ServletException {
	}

}
