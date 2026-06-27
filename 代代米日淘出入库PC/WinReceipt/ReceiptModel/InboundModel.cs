using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    public class InboundModel
    {
        /// <summary>
        /// ID
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// 用户ID
        /// </summary>
        public string UserID { get; set; }
        /// <summary>
        /// 手机号
        /// </summary>
        public string Userphone { get; set; }
        /// <summary>
        /// 订单号
        /// </summary>
        public string Ordernumber { get; set; }
        /// <summary>
        /// 商品ID
        /// </summary>
        public string ProductID { get; set; }
        /// <summary>
        /// 商品名称
        /// </summary>
        public string ProductName { get; set; }
        /// <summary>
        /// 入扎礼
        /// </summary>
        public string price { get; set; }
        /// <summary>
        /// 拍行
        /// </summary>
        public string MerCari { get; set; }
        /// <summary>
        /// 备注
        /// </summary>
        public string Remark { get; set; }
        /// <summary>
        /// 入库时间
        /// </summary>
        public DateTime Time { get; set; }
        /// <summary>
        /// 状态;默认入库
        /// </summary>
        public string Status { get; set; }
    }
}
