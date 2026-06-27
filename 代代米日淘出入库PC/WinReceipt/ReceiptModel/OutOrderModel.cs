using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    public class OutOrderModel
    {
        /// <summary>
        /// 物流公司
        /// </summary>
        public string wlcompany { get; set; }
        /// <summary>
        /// 物流单号
        /// </summary>
        public string trackingNumber { get; set; }
        /// <summary>
        /// 物流备注
        /// </summary>
        public string wlRemarks { get; set; }
        /// <summary>
        /// 用户id
        /// </summary>
        public string userid { get; set; }
        /// <summary>
        /// 订单id
        /// </summary>
        public string orderid { get; set; }
    }
    public class OutOrderModelVerify
    {
        public string nickName { get; set; } = "";
        public string orderid { get; set; }
        public int pageNumber { get; set; } = 1;
        public int pageSize { get; set; } = 30;
        public string title { get; set; } = "";
        public int type { get; set; }
    }
}
