using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ReceiptModel
{
    public  class LoginResponseModel
    {
            public string msg { get; set; }
            public int code { get; set; }
            public LoginData data { get; set; }
        
        public class LoginData
        {
            public int id { get; set; }
            public string username { get; set; }
            public string password { get; set; }
            public int type { get; set; }
            public string authtype { get; set; }
            public string token { get; set; }
        }
    }
}
