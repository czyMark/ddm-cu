using ReceiptDAL;
using ReceiptModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptBLL
{
    public class AddInboundBLL
    {
        AddInboundDAL Adddal = new AddInboundDAL();
        /// <summary>
        /// 待入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <param name="tokin"></param>
        /// <returns></returns>
        public string AddMerOrderBll(string requestData,string tokin,string Strtext)
        {
            string result=string.Empty;
            if (Strtext.Contains("雅虎")) 
            {
                result= Adddal.YahooOrderPost(requestData, tokin);
            }
            else
            {
                result= Adddal.MerOrderPossst(requestData, tokin);
            }
          
   

            // 处理返回数据
            if (!result.Contains("error"))
            {
                return result;
            }
            else
            {
                return null;
            }
        }
        /// <summary>
        /// 入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <param name="tokin"></param>
        /// <param name="Strtext"></param>
        /// <returns></returns>
        public string AddMerYahoOrderBll(string requestData, string tokin, string Strtext)
        {
            string result = string.Empty;
            if (Strtext.Contains("雅虎"))
            {
                result = Adddal.yahooOrderUpdata(requestData, tokin);
            }
            else
            {
                result = Adddal.MerOrderUpdata(requestData, tokin);
            }



            // 处理返回数据
            if (!result.Contains("error"))
            {
                return result;
            }
            else
            {
                return null;
            }
        }
        /// <summary>
        /// 出库
        /// </summary>
        /// <param name="requestData"></param>
        /// <param name="tokin"></param>
        /// <param name="Strtext"></param>
        /// <returns></returns>
        public string OutMerYahoOrderBll(string requestData, string tokin, string Strtext)
        {
            string result = string.Empty;
            if (Strtext.Contains("雅虎"))
            {
                result = Adddal.yahooOrdersendGood(requestData, tokin);
            }
            else
            {
                result = Adddal.MerOrdersendGood(requestData, tokin);
            }



            // 处理返回数据
            if (!result.Contains("error"))
            {
                return result;
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 出库前验证
        /// </summary>
        /// <param name="requestData"></param>
        /// <param name="tokin"></param>
        /// <param name="Strtext"></param>
        /// <returns></returns>
        public string OutMerYahoOrderBllVerify(string requestData, string tokin, string Strtext)
        {
            string result = string.Empty;
            if (Strtext.Contains("雅虎"))
            {
                result = Adddal.yahooOrdersendGoodVerify(requestData, tokin);
            }
            else
            {
                result = Adddal.MerOrdersendGoodVerify(requestData, tokin);
            }



            // 处理返回数据
            if (!result.Contains("error"))
            {
                return result;
            }
            else
            {
                return null;
            }
        }

        public string Login(string requestData)
        {

            string result = Adddal.Login(requestData);

            // 处理返回数据
            if (!result.Contains("error"))
            {
                return result;
            }
            else
            {
                return null;
            }
        }
    }
}
