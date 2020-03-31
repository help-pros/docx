<%@page contentType="text/html; charset=gb2312" language="java" %>
<%
    String orderId = (String) request.getParameter("orderId").trim();
    String orderAmount = (String) request.getParameter("orderAmount").trim();
    String dealId = (String) request.getParameter("dealId").trim();
    String orderTime = (String) request.getParameter("orderTime").trim();
    String payResult = (String) request.getParameter("payResult").trim();
    String msg = (String) request.getParameter("msg").trim();
%>
<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >
<html>
<head>
    <title>快钱支付结果</title>
    <meta http-equiv="content-type" content="text/html; charset=gb2312">
</head>

<BODY>
<div align="center">
    <h2 align="center">快钱支付结果页面</h2>
    <font color="#ff0000">（该页面仅做参考）</font>
    <table width="500" border="1" style="border-collapse: collapse" bordercolor="green" align="center">
        <tr>
            <td align="center">
                快钱交易号
            </td>
            <td align="center">
                <%=dealId%>
            </td>
        </tr>
        <tr>
            <td align="center">
                订单号
            </td>
            <td align="center">
                <%=orderId%>
            </td>
        </tr>
        <tr>
            <td align="center">
                订单金额
            </td>
            <td align="center">
                <%=orderAmount%>
            </td>
        </tr>
        <tr>
            <td align="center">
                下单时间
            </td>
            <td align="center">
                <%=orderTime%>
            </td>
        </tr>
        <tr>
            <td align="center">
                处理结果
            </td>
            <td align="center">
                <%= msg%>
            </td>
        </tr>
    </table>
</div>
</BODY>
</HTML>