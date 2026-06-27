using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    public class OutboundModel
    {
        public int Id { get; set; }
        public string NameNumber { get; set; }
        public string NameSum { get; set; }
        public string NameWeight { get; set; }
        /// <summary>
        /// 物流公司
        /// </summary>
        public string Companies { get; set; }
        /// <summary>
        /// 物流单号
        /// </summary>
        public string Ordernumber { get; set; }
        public string Remark { get; set; }
        public DateTime Time { get; set; }
    }
}
