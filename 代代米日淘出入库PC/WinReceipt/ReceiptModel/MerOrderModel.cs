using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    public class MerOrderModel
    {
        public string nickName { get; set; } = "";
        public string orderid { get; set; } = "";
        public string id { get; set; } = "";
        public string userid { get; set; } = "";
        public int pageNumber { get; set; }
        public int pageSize { get; set; } 
        public string title { get; set; } = "";
        public int type { get; set; }
    }
}
