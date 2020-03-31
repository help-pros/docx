package serverlet;

import baifubao.BfbSdkComm;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PayWapServlet extends HttpServlet {
 
	
	private ServletConfig scon = null;
	
	public PayWapServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}

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
		request.setCharacterEncoding("gbk");
		response.setCharacterEncoding("gbk");
		response.setContentType("text/html;charset=gbk");  
		response.setHeader("content-type","text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		//打印日志
		Logger logger =new  baifubao.BfbSdkComm().printLog("PayWapServlet");
		/**
		 * 2、获取 web.xml内的常量值
	     */
	    //商品分类号
	    String  service_code=
	    		"service_code="+scon.getServletContext().getInitParameter("BFB_PAY_INTERFACE_SERVICE_ID");
	    //商户号
	    String sp_no="sp_no=" +scon.getServletContext().getInitParameter("SP_NO");
	    //交易的超时时间,当前时间加2天
	    Calendar   c   =   Calendar.getInstance(); 
	    c.add(Calendar.DAY_OF_MONTH, 2);  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");  
        String mDateTime=formatter.format(c.getTime());  
	    String strExpire=mDateTime.substring(0, 14);//
	    String expire_time=
	    		"expire_time=" +strExpire;
	    //订单创建时间
	    String order_create_time1=formatter.format(System.currentTimeMillis()); 
	    //订单号
	    String order_no="order_no=" +order_create_time1+(int)(Math.random() * 100); ;
	    String order_create_time="order_create_time=" +order_create_time1;
	    //币种
	    String currency="currency=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_CURRENTCY");
	    //编码
	    String input_charset="input_charset=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_ENCODING");
	    //版本
	    String version="version=" +scon.getServletContext().getInitParameter("BFB_INTERFACE_VERSION");
	    //加密方式md5或者hash
	    String sign_method="sign_method=" +scon.getServletContext().getInitParameter("SIGN_METHOD_MD5");
	    //秘钥
	    String SP_KEY=scon.getServletContext().getInitParameter("SP_KEY");
	    //提交地址
	    String BFB_PAY_WAP_DIRECT_URL=scon.getServletContext().getInitParameter("BFB_PAY_WAP_DIRECT_URL");
	    
	    /**
	     *3、获取pay_unlogin.html页面post提交的变量值
	     */
	    //商品名称
	    String tempgoods_name=request.getParameter("goods_name");
	    String goods_name ="goods_name="+tempgoods_name;
	    String goods_name1="goods_name="+ URLEncoder.encode(request.getParameter("goods_name"),"gbk");
	    //String goods_ame1 ="goods_name="+tempgoods_name;
	    //商品描述
	    String tempgoods_desc=request.getParameter("goods_desc");
	    String goods_desc ="goods_desc="+tempgoods_desc;
	    String goods_desc1= "goods_desc="+URLEncoder.encode(request.getParameter("goods_desc"),"gbk");
	    //String goods_desc1 ="goods_desc="+tempgoods_desc;
	   //商品在商户网站上的URL
	    String goods_url ="goods_url="+request.getParameter("goods_url");
	    String goods_url1="goods_url="+URLEncoder.encode(request.getParameter("goods_url"),"gbk");
	    
	    //单价
	    String unit_amount ="unit_amount="+request.getParameter("unit_amount");
	    //数量
	    String unit_count ="unit_count="+request.getParameter("unit_count");
	    //运费
	    String transport_amount ="transport_amount="+request.getParameter("transport_amount");
	    //总金额
	    String total_amount ="total_amount="+request.getParameter("total_amount");
	   //买家在商户网站的用户名
	    String tempSPUserName=request.getParameter("buyer_sp_username");
	    String buyer_sp_username ="buyer_sp_username="+tempSPUserName;
        String buyer_sp_username1 ="buyer_sp_username="+URLEncoder.encode(tempSPUserName,"gbk");
	   //后台通知地址
	    String return_url ="return_url="+request.getParameter("return_url");
	    String return_url1="return_url="+URLEncoder.encode(request.getParameter("return_url"),"gbk");
	   //前台通知地址
	    String page_url ="page_url="+request.getParameter("page_url");
	    String page_url1="page_url="+URLEncoder.encode(request.getParameter("page_url"),"gbk");
       //支付方式
	    String pay_type ="pay_type="+request.getParameter("pay_type");
	    //默认银行的编码
	    String bank_no ="bank_no="+request.getParameter("bank_no");
	    //用户在商户端的用户ID
	    String sp_uno ="sp_uno="+request.getParameter("sp_uno");
	    //商户自定义数据
	    String tempextra=request.getParameter("extra");
	    String extra ="extra="+tempextra;
	    String extra1="extra="+URLEncoder.encode(tempextra,"gbk");
	  
	    //签名串拼接数组
		String[]array={
			    service_code,
				sp_no,
				order_create_time,
				order_no,
				goods_name,
				goods_desc,
				goods_url,
				unit_amount,
				unit_count,
				transport_amount,
				total_amount,
				currency,
				buyer_sp_username ,
				return_url,
				page_url,
				pay_type,
				bank_no,
				expire_time,
				input_charset,
				version,
				sign_method
				,extra
				};
		//浏览器参数拼接数组
		String[]array1={
			    service_code,
				sp_no,
				order_create_time,
				order_no,
				goods_name1,
				goods_desc1,
				goods_url1,
				unit_amount,
				unit_count,
				transport_amount,
				total_amount,
				currency,
				buyer_sp_username1,
				return_url1,
				page_url1,
				pay_type,
				bank_no,
				expire_time,
				input_charset,
				version,
				sign_method
				,extra1
				};
		/**
		 * 4、调用bfb_sdk_comm里create_baifubao_pay_order_url方法生成百付宝即时到账支付接口URL(不需要登录)
		 *   array是待签名串
		 *   array1地址栏拼接串
		 */
		String getURL=new BfbSdkComm().create_baifubao_pay_order_url(array,array1,BFB_PAY_WAP_DIRECT_URL);
		//在日志里面打印提交串
		logger.log(Level.INFO, "即时支付（不需要登陆）getURL提交地址：："+getURL);
		//打印完成关闭日志
		logger.setLevel(Level.OFF);
		/**
		 * 5、提交支付请求，跳转到百付宝收银台
		 */
		response.sendRedirect(getURL); 
	    out.flush();
	    out.close();	
	}
	public String getServletInfo() {
		return "pay_wapServlet";
	}
	public void init(ServletConfig config) throws ServletException {
		scon = config;
	}

}
