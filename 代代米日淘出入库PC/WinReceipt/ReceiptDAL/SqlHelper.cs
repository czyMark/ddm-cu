using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;

namespace ReceiptDAL
{
    public static class SqlHelper
    {
        private static readonly string connectionString = "server=127.0.0.1;database=recpiptuserdb;uid=root;pwd=your-db-password;"
     + "Pooling=true;MinPoolSize=5;MaxPoolSize=100;ConnectionTimeout=30";

        private static MySqlConnection _connection;
        private static readonly object _lock = new object();
         
        public static MySqlCommand CreateCmd(string strSql, MySqlParameter[] sqlParameters)
        {
            try
            {

                // 双重检查锁定确保线程安全
                if (_connection == null)
                {
                    lock (_lock)
                    {
                        if (_connection == null)
                        {
                            _connection = new MySqlConnection(connectionString);
                            _connection.Open();
                        }
                    }
                }

                // 确保连接是打开的
                if (_connection.State != ConnectionState.Open)
                {
                    _connection.Close();
                    _connection.Open();
                }
                MySqlCommand cmd = new MySqlCommand(strSql, _connection);
                if (sqlParameters != null)
                {
                    cmd.Parameters.AddRange(sqlParameters);
                }

                return cmd;
            }
            catch (Exception)
            {
                return null;
            }

        }

        // 添加关闭连接的静态方法（按需调用）
        public static void CloseGlobalConnection()
        {
            if (_connection != null && _connection.State == ConnectionState.Open)
            {
                _connection.Close();
                _connection.Dispose();
                _connection = null;
            }
        }

        /// <summary>
        /// 查询
        /// </summary>
        /// <param name="strSql"></param>
        /// <param name="sqlParameters"></param>
        /// <returns></returns>
        public static DataSet getDataSet(string strSql, MySqlParameter[] sqlParameters)
        {
            try
            {

                using (MySqlCommand cmd = CreateCmd(strSql, sqlParameters))
                using (MySqlDataAdapter sda = new MySqlDataAdapter(cmd))
                {
                    DataSet ds = new DataSet();
                    sda.Fill(ds);
                    // 检查DataSet是否有数据
                    if (ds.Tables.Count == 0 || ds.Tables[0].Rows.Count == 0)
                    {
                        return null;
                    }
                    return ds;
                }
            }
            catch (Exception)
            {
                return null;
            }
        }


   

        /// <summary>
        /// 增删改
        /// </summary>
        /// <param name="strSql"></param>
        /// <param name="sqlParameters"></param>
        /// <returns></returns>
        public static int operateData(string strSql, MySqlParameter[] sqlParameters)
        {
            try
            {
                MySqlCommand cmd = CreateCmd(strSql, sqlParameters);
                return cmd.ExecuteNonQuery();
            }
            catch (Exception)
            {

                return 0;
            }
        }

        /// <summary>
        /// 查询返回一条数据
        /// </summary>
        /// <param name="strSql"></param>
        /// <param name="sqlParameters"></param>
        /// <returns></returns>
        public static int countData(string strSql, MySqlParameter[] sqlParameters)
        {
            try
            {
                using (MySqlCommand cmd = CreateCmd(strSql, sqlParameters))
                {
                    object result = cmd.ExecuteScalar();

                    if (result == null || result is DBNull)
                    {
                        return 0;
                    }

                    return Convert.ToInt32(result);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("数据库错误: " + ex.Message); // 可以换成日志记录
                return 0;
            }
        }

    
     
    }
}
