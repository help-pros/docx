<%@ Page Language="C#" AutoEventWireup="true" CodeFile="show.aspx.cs" Inherits="show" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
   <BODY>
	<div align="center">
		<h2 align="center">快钱支付结果页面</h2>
		<font color="#ff0000">（该页面仅做参考）</font>
    	<table width="500" border="1" style="border-collapse: collapse" bordercolor="green" align="center">
			<tr>
				<td id="dealId">
					快钱交易号
				</td>
				<td>
					<%=dealId%>
				</td>
			</tr>
			<tr>
				<td id="orderId">
					订单号
				</td>
				<td>
					<%=orderId%>
				</td>
			</tr>
			<tr>
				<td id="orderAmount">
					订单金额
				</td>
				<td>
					<%=orderAmount%>
				</td>
			</tr>
			<tr>
				<td id="orderTime">
					下单时间
				</td>
				<td>
					<%=orderTime%>
				</td>
			</tr>
			<tr>
				<td id="payResult">
					处理结果
				</td>
				<td>
					<%= msg%>
				</td>
			</tr>	
		</table>
	</div>
</BODY>
</body>
</html>
