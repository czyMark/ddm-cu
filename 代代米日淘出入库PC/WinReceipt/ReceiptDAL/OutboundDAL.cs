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
    public class OutboundDAL
    {
        public int AddOutbound(OutboundModel Outbound)
        {
            string sql = "INSERT INTO boundtable (NameNumber, NameSum, NameWeight,Companies,Ordernumber, Remark, Time) VALUES (@NameNumber, @NameSum, @NameWeight,@Companies,@Ordernumber, @Remark, @Time); SELECT LAST_INSERT_ID();";
            MySqlParameter[] sqlParameters =
            {
                new MySqlParameter("NameNumber",Outbound.NameNumber),
                new MySqlParameter("NameSum",Outbound.NameSum),
                new MySqlParameter("NameWeight",Outbound.NameWeight),
                new MySqlParameter("Companies",Outbound.Companies),
                new MySqlParameter("Ordernumber",Outbound.Ordernumber),
                new MySqlParameter("Remark",Outbound.Remark),
                new MySqlParameter("Time",Outbound.Time),
            };
            return SqlHelper.operateData(sql, sqlParameters);

        }
        public DataSet GetInboundPaged(int pageIndex, int pageSize,  string NameNumber, out int totalCount)
        {
            // 确保页码和每页大小合法
            pageIndex = Math.Max(1, pageIndex);
            pageSize = Math.Max(1, pageSize);

            // 基础SQL查询语句
            string baseSql = "SELECT * FROM boundtable WHERE 1=1";
            string countSql = "SELECT COUNT(*) FROM boundtable WHERE 1=1";

            // 参数列表
            List<MySqlParameter> parameters = new List<MySqlParameter>();

            // 如果NameNumber不为空，添加模糊查询条件
            if (!string.IsNullOrEmpty(NameNumber))
            {
                baseSql += " AND NameNumber LIKE @NameNumber";
                countSql += " AND NameNumber LIKE @NameNumber";
                parameters.Add(new MySqlParameter("@NameNumber", $"%{NameNumber}%"));
            }

            // 获取符合条件的总记录数
            var dsCount = SqlHelper.getDataSet(countSql, parameters.ToArray());
            totalCount = dsCount.Tables[0].Rows.Count > 0 ? Convert.ToInt32(dsCount.Tables[0].Rows[0][0]) : 0;

            // 添加分页条件
            string dataSql = baseSql + " ORDER BY Id ASC LIMIT @PageSize OFFSET @Offset";

            // 添加分页参数
            parameters.Add(new MySqlParameter("@PageSize", pageSize));
            parameters.Add(new MySqlParameter("@Offset", (pageIndex - 1) * pageSize));

            // 返回分页查询结果
            return SqlHelper.getDataSet(dataSql, parameters.ToArray());
        }
    }
}
