using MySql.Data.MySqlClient;
using ReceiptModel;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptDAL
{
    public class InboundDAL
    {

        public int AddInbound(InboundModel inbound)
        {
            string sql = "INSERT INTO records (UserID, Userphone, Ordernumber,ProductID,ProductName,price,MerCari,Remark, Time, Status) VALUES" +
                " (@UserID, @Userphone, @Ordernumber,@ProductID,@ProductName,@price,@MerCari,@Remark, @Time, @Status); SELECT LAST_INSERT_ID();";
            MySqlParameter[] sqlParameters =
            {
                new MySqlParameter("UserID",inbound.UserID),
                new MySqlParameter("Userphone",inbound.Userphone),
                new MySqlParameter("Ordernumber",inbound.Ordernumber),
                new MySqlParameter("ProductID",inbound.ProductID),
                new MySqlParameter("ProductName",inbound.ProductName),
                new MySqlParameter("price",inbound.price),
                new MySqlParameter("MerCari",inbound.MerCari),
                new MySqlParameter("Remark",inbound.Remark),
                new MySqlParameter("Time",inbound.Time),
                new MySqlParameter("Status",inbound.Status)
            };
            return SqlHelper.operateData(sql, sqlParameters);

        }


        public DataSet GetInbound(InboundModel inbound)
        {
            // 修改为模糊查询语句
            string sql = @"
        SELECT * 
        FROM records 
        WHERE 1=1 ";

            List<MySqlParameter> sqlParameters = new List<MySqlParameter>();

            if (!string.IsNullOrEmpty(inbound.ProductName))
            {
                sql += " AND ProductName LIKE CONCAT('%', @ProductName, '%') ";
                sqlParameters.Add(new MySqlParameter("ProductName", inbound.ProductName));
            }

            if (!string.IsNullOrEmpty(inbound.ProductID))
            {
                sql += " AND ProductID LIKE CONCAT('%', @ProductID, '%') ";
                sqlParameters.Add(new MySqlParameter("ProductID", inbound.ProductID));
            }

            return SqlHelper.getDataSet(sql, sqlParameters.ToArray());
        }
        public DataSet GetInboundPaged(int pageIndex, int pageSize, string ProductName, string Ordernumber, out int totalCount)
        {
            // 确保页码和每页大小合法
            pageIndex = Math.Max(1, pageIndex);
            pageSize = Math.Max(1, pageSize);

            // 基础SQL查询语句
            string baseSql = "SELECT * FROM records WHERE 1=1";
            string countSql = "SELECT COUNT(*) FROM records WHERE 1=1";

            // 参数列表
            List<MySqlParameter> parameters = new List<MySqlParameter>();

            // 如果ProductName不为空，添加模糊查询条件
            if (!string.IsNullOrEmpty(ProductName))
            {
                baseSql += " AND ProductName LIKE @ProductName";
                countSql += " AND ProductName LIKE @ProductName";
                parameters.Add(new MySqlParameter("@ProductName", $"%{ProductName}%"));
            }

            // 如果ProductID不为空，添加模糊查询条件
            if (!string.IsNullOrEmpty(Ordernumber))
            {
                baseSql += " AND Ordernumber LIKE @Ordernumber";
                countSql += " AND Ordernumber LIKE @Ordernumber";
                parameters.Add(new MySqlParameter("@Ordernumber", $"%{Ordernumber}%"));
            }

            // 获取符合条件的总记录数
            var dsCount = SqlHelper.getDataSet(countSql, parameters.ToArray());
            if (dsCount == null)
                totalCount = 0;
            else
                totalCount = dsCount.Tables[0].Rows.Count > 0 ? Convert.ToInt32(dsCount.Tables[0].Rows[0][0]) : 0;

            // 添加分页条件
            string dataSql = baseSql + " ORDER BY Id ASC LIMIT @PageSize OFFSET @Offset";

            // 添加分页参数
            parameters.Add(new MySqlParameter("@PageSize", pageSize));
            parameters.Add(new MySqlParameter("@Offset", (pageIndex - 1) * pageSize));

            // 返回分页查询结果
            return SqlHelper.getDataSet(dataSql, parameters.ToArray());
        }
        /// <summary>
        /// 更改状态
        /// </summary>
        /// <param name="inbound"></param>
        /// <returns></returns>
        public int UpdateInboundStatus(InboundModel inbound)
        {
            // SQL 更新语句，根据订单号（Ordernumber）更新状态（Status）
            string sql = "UPDATE records SET Status = @Status WHERE Ordernumber = @Ordernumber;";

            // 参数化查询，防止 SQL 注入
            MySqlParameter[] sqlParameters =
            {
        new MySqlParameter("Ordernumber", inbound.Ordernumber),
        new MySqlParameter("Status", inbound.Status)
    };

            // 执行 SQL 并返回受影响的行数
            return SqlHelper.operateData(sql, sqlParameters);
        }



    }
}
