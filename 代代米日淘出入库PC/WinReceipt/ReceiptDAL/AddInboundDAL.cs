using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptDAL
{
    public class AddInboundDAL
    {
        string baseUri = "https://ddm-cu.com";
        /// <summary>
        /// 煤炉待入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string MerOrderPossst(string requestData ,string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "MerOrder/SelectAll";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }

        /// <summary>
        /// 雅虎待入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string YahooOrderPost(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "yahooOrder/SelectAll";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                      return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }
        /// <summary>
        /// 雅虎已入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string yahooOrderUpdata(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "yahooOrder/updataTypeByOrderid";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }
        }
        /// <summary>
        /// 煤炉已入库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string MerOrderUpdata(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "MerOrder/updataTypeByorderid";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }
        /// <summary>
        /// 雅虎出库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string yahooOrdersendGood(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "yahooOrder/sendGood";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }


        /// <summary>
        /// 雅虎出库验证
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string yahooOrdersendGoodVerify(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "yahooOrder/SelectAll";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }

        /// <summary>
        /// 煤炉出库验证
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string MerOrdersendGoodVerify(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "MerOrder/SelectAll";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }

        /// <summary>
        /// 煤炉出库
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        public string MerOrdersendGood(string requestData, string tokin)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "MerOrder/sendGood";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint, tokin);
                if (string.IsNullOrEmpty(tokin))
                {
                    return "登录已过期请重新登录";
                }
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }

        }
        public string Login(string requestData)
        {
            try
            {
                HttpClientHelper httpClientHelper = new HttpClientHelper(baseUri);
                string specificApiEndpoint = "admin/login";
                // 调用Post方法获取API响应数据
                string responseFromApi = httpClientHelper.Post(requestData, specificApiEndpoint);
                // 直接返回API响应数据
                return responseFromApi;
            }
            catch (Exception ex)
            {
                return ex.ToString();
                //throw;
            }
        
        }






    }
}
