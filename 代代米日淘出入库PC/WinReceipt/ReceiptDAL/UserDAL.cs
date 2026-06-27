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
    public class UserDAL
    {
        //登录
        public int Loginback(string Name, string Password)
        {
            string sql = "SELECT COUNT(*) FROM users WHERE username=@username AND password=@pwd";
            MySqlParameter[] sqlParameters =
            {
                new MySqlParameter("username",Name),
                new MySqlParameter("pwd",Password)
            };
            return SqlHelper.countData(sql, sqlParameters);
       
        }
        public int Login(string Name, string Password)
        {
            string sql = "SELECT COUNT(*) FROM users WHERE username=@username AND password=@pwd";
            MySqlParameter[] sqlParameters =
            {
                new MySqlParameter("username",Name),
                new MySqlParameter("pwd",Password)
            };
            return SqlHelper.countData(sql, sqlParameters);
       
        }

  

        //查询用户
        public DataSet GetUser()
        {
            string sql = "select * from [User]";
            MySqlParameter[] sqlParameters = new MySqlParameter[] { };
            return SqlHelper.getDataSet(sql, sqlParameters);
        }
    }
}
