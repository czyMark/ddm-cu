using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptDAL
{
    public class InCacheDAL
    {
        public InCacheDAL()
        {
            SqlHelper.CreateCmd("select 1=1", null);
        }
    }
}
