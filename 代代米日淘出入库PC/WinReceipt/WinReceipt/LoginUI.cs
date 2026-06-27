using Newtonsoft.Json;
using ReceiptBLL;
using ReceiptModel;
using Sunny.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml.Linq;
using WinReceipt.Tool;
using ZXing;

namespace WinReceipt
{
    public partial class LoginUI : UILoginForm
    {
        private static UserBLL bll = new UserBLL();
        private static AddInboundBLL AddMerOrderbll = new AddInboundBLL();
        private bool ConnectionOk = false;
        public LoginUI()
        {
            InitializeComponent();
            //检查网络环境 初始化与数据库连接 让其后续操作变快
            ThreadPool.QueueUserWorkItem(new WaitCallback((obj) =>
            {
                InCache inCache = new InCache(); ConnectionOk = true;
            }));
            Thread.Sleep(1000);
        }

        private void LoginUI_ButtonLoginClick(object sender, System.EventArgs e)
        {
            try
            {

                if (string.IsNullOrEmpty(UserName) || string.IsNullOrEmpty(Password))
                {
                    this.ShowErrorTip("请输入用户名和密码！");
                    return;
                }
                LoginModel loginmodel = new LoginModel();
                loginmodel.username = UserName;
                loginmodel.password = Password;

                string requestData = JsonConvert.SerializeObject(loginmodel);
                if (!ConnectionOk)
                {

                    this.ShowErrorTip("请稍后重试正在尝试连接网络！");
                    return;
                }

                string result = AddMerOrderbll.Login(requestData);
                var response = JsonConvert.DeserializeObject<LoginResponseModel>(result);
                if (response != null && response.code == 0 && !string.IsNullOrEmpty(response.data?.token))
                {
                    Program.Strtoken = response.data.token;
                    this.Hide();
                    ShowFrame(new FrmreceiptMain());
                    this.Close();
                }
                else
                {
                    if (response != null)
                        this.ShowErrorTip(response.msg);
                }
            }
            catch (Exception ex)
            {
                //if (ex.Message != null && ex.Message.Contains("Unexpected character encountered while parsing value"))
                //{
                //    this.ShowErrorTip("请稍后重试正在尝试连接网络！");
                //}
                //else
                //{
                if (AppConfigHelper.IsDebugMode)
                {
                    UIMessageBox.Show(ex.StackTrace);
                    return;
                }
                else
                {

                    this.ShowErrorTip("检测到网络环境波动严重！为避免产生数据记录错误。请检查网络后在重启软件!");

                    Thread.Sleep(2000);
                    this.Close();
                }


                //}
            }

            //if (bll.Login(UserName, Password) == true)
            //{
            //    ShowFrame(new FrmreceiptMain());
            //   this.Close();
            //}
            //else
            //{
            //    this.ShowErrorTip("登录失败，请检查用户或密码");
            //}
        }
        private void ShowFrame(UIMainFrame frame)
        {
            frame.WindowState = FormWindowState.Maximized;
            frame.ShowDialog();
            frame.Dispose();
        }
    }
}
