
<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >
<html>
	<head>
		<title>快钱支付结果</title>
			<meta http-equiv=Content-Type content="text/html;charset=utf-8">
		<style type="text/css">
			td{text-align:center}
		</style>
	</head>
	
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
					<?PHP echo $_REQUEST['dealId']; ?>
				</td>
			</tr>
			<tr>
				<td id="orderId">
					订单号
				</td>
				<td>
					<?PHP echo $_REQUEST['orderId']; ?>
				</td>
			</tr>
			<tr>
				<td id="orderAmount">
					订单金额
				</td>
				<td>
					<?PHP echo $_REQUEST['orderAmount']; ?>
				</td>
			</tr>
			<tr>
				<td id="orderTime">
					下单时间
				</td>
				<td>
					<?PHP echo $_REQUEST['orderTime']; ?>
				</td>
			</tr>
			<tr>
				<td id="payResult">
					处理结果
				</td>
				<td>
					<?PHP echo $_REQUEST['msg']; ?>
				</td>
			</tr>	
		</table>
	</div>
</BODY>
</HTML>