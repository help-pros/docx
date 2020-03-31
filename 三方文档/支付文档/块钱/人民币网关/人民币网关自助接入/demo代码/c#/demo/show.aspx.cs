using System;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class show : System.Web.UI.Page
{
    public string msg = "";
    public string orderId = "";
    public string orderAmount = "";
    public string dealId = "";
    public string orderTime = "";

    protected void Page_Load(object sender, EventArgs e)
    {
        msg = Request.QueryString["msg"].ToString().Trim();
        orderId = Request.QueryString["orderId"].ToString().Trim();
        orderAmount = Request.QueryString["orderAmount"].ToString().Trim();
        dealId = Request.QueryString["dealId"].ToString().Trim();
        orderTime = Request.QueryString["orderTime"].ToString().Trim();
    }
}