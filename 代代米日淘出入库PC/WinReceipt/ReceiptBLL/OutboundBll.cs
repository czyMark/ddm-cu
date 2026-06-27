using ReceiptDAL;
using ReceiptModel;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptBLL
{
    public class OutboundBll
    {
        OutboundDAL dal = new OutboundDAL();
        public bool AddInboundBll(OutboundModel Outbound)
        {

            if (dal.AddOutbound(Outbound) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        public DataSet GetOutboundPagedBll(int pageIndex, int pageSize,  string NameNumber, out int totalCount)
        {
            return dal.GetInboundPaged(pageIndex, pageSize,  NameNumber, out totalCount);

        }

    }
}
