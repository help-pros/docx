<%@ Page Language="C#" AutoEventWireup="true" CodeFile="send.aspx.cs" Inherits="Send" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
		<div align="center">
			<h2 align="center">提交到快钱页面</h2>
			<font color="#ff0000">（该页面仅做参考）</font>
    		<table width="500" border="1" style="border-collapse: collapse" bordercolor="green" align="center">
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
					<td id="productName">
						商品名称
					</td>
					<td>
						<%= productName%>
					</td>
				</tr>
				<tr>
					<td id="productNum">
						商品数量
					</td>
					<td>
						<%= productNum%>
					</td>
				</tr>
			</table>
		</div>
		<div align="center" style="font-weight: bold;">
			<form name="kqPay" action="https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm" method="post">
				<input type="hidden" name="inputCharset" value="<%=inputCharset%>" />
				<input type="hidden" name="pageUrl" value="<%=pageUrl%>" />
				<input type="hidden" name="bgUrl" value="<%=bgUrl%>" />
				<input type="hidden" name="version" value="<%=version%>" />
				<input type="hidden" name="language" value="<%=language%>" />
				<input type="hidden" name="signType" value="<%=signType%>" />
				<input type="hidden" name="signMsg" value="<%=signMsg%>" />
				<input type="hidden" name="merchantAcctId" value="<%=merchantAcctId%>" />
				<input type="hidden" name="payerName" value="<%=payerName%>" />
				<input type="hidden" name="payerContactType" value="<%=payerContactType%>" />
				<input type="hidden" name="payerContact" value="<%=payerContact%>" />
				<input type="hidden" name="orderId" value="<%=orderId%>" />
				<input type="hidden" name="orderAmount" value="<%=orderAmount%>" />
				<input type="hidden" name="orderTime" value="<%=orderTime%>" />
				<input type="hidden" name="productName" value="<%=productName%>" />
				<input type="hidden" name="productNum" value="<%=productNum%>" />
				<input type="hidden" name="productId" value="<%=productId%>" />
				<input type="hidden" name="productDesc" value="<%=productDesc%>" />
				<input type="hidden" name="ext1" value="<%=ext1%>" />
				<input type="hidden" name="ext2" value="<%=ext2%>" />
				<input type="hidden" name="payType" value="<%=payType%>" />
				<input type="hidden" name="bankId" value="<%=bankId%>" />
				<input type="hidden" name="redoFlag" value="<%=redoFlag%>" />
				<input type="hidden" name="pid" value="<%=pid%>" />
				<input type="submit" name="submit" value="提交到快钱">
			</form>
		</div>
	</body>
</html>
