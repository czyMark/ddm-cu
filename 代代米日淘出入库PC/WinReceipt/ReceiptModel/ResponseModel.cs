using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    using System;
    using System.Collections.Generic;

    public class ResponseModel
    {
        public string msg { get; set; }
        public int code { get; set; }
        public ResponseData data { get; set; }
    }

    public class ResponseExceModel
    {
        public string msg { get; set; }
        public int code { get; set; }
        public int data { get; set; }
    }

    public class ResponseData
    {
        public int totalData { get; set; }
        public List<OrderItem> list { get; set; }
    }

    public class OrderItem
    {
        public string cangcufei { get; set; }
        public string dgPrice { get; set; }
        public string wlRemarks { get; set; }
        public string title { get; set; }
        public string type { get; set; }
        public int userid { get; set; }
        public string parentid { get; set; }
        public string transactionid { get; set; }
        public string luxian { get; set; }
        public string otherPriceContent { get; set; }
        public List<object> children { get; set; } = new List<object>();
        public string otherPrice { get; set; }
        public string price { get; set; }
        public string gjyunfei { get; set; }
        public string id { get; set; }
        public string trackingNumber { get; set; }
        public string orderid { get; set; }
        public int count { get; set; }
        public string jpyunfei { get; set; }
        public string baozhuangfei { get; set; }
        public string lxRemarks { get; set; }
        public string imageurl { get; set; }
        public string time { get; set; }
        public string goodCode { get; set; }
        public string wlcompany { get; set; }
        public UserInfo user { get; set; }
    }

    public class UserInfo
    {
        public int userid { get; set; }
        public string account { get; set; }
        public string openid { get; set; }
        public string realname { get; set; }
        public string phone { get; set; }
        public string twophone { get; set; }
        public string balance { get; set; }
        public string isRead { get; set; }
        public string contact { get; set; }
        public string nickName { get; set; }
        public string avatarUrl { get; set; }
        public string yjBalance { get; set; }
        public string isAdmin { get; set; }
        public string loginphone { get; set; }
        public string invitecode { get; set; }
        public string myinvitecode { get; set; }
    }
}
