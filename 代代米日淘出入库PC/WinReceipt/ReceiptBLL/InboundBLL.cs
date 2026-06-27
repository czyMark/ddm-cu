using ReceiptDAL;
using ReceiptModel;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptBLL
{
    public class InboundBLL
    {
        InboundDAL dal = new InboundDAL();
        public bool AddInboundBll(InboundModel inbound)
        {
           
            if (dal.AddInbound(inbound) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        public bool UpdateInboundStatusBll(InboundModel inbound)
        {

            if (dal.UpdateInboundStatus(inbound) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public DataSet GetInboundBll(InboundModel inbound)
        {
            return dal.GetInbound(inbound);
        }
      
        public DataSet GetInboundPagedBll(int pageIndex, int pageSize,string Name, string Ordernumber, out int totalCount)
        {
            return dal.GetInboundPaged(pageIndex, pageSize , Name, Ordernumber, out totalCount);

        }
       

    }
}
