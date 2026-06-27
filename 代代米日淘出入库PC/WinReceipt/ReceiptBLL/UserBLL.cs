using ReceiptDAL;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptBLL
{
    public class UserBLL
    {
        public bool Login(string Name, string Password)
        {
            UserDAL dal = new UserDAL();
            if (dal.Login(Name, Password) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

     
    }
}
