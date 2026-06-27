using Newtonsoft.Json;
using ReceiptBLL;
using ReceiptModel;
using Sunny.UI;
using Sunny.UI.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using ZXing;
using static WinReceipt.FrmInbound;

namespace WinReceipt
{
    public partial class FrmAddInbound : UIEditForm
    {
        #region 下拉集合
        public static List<InfoCariModel> CombList = new List<InfoCariModel>();//拍行集合
        #endregion
        private static AddInboundBLL AddMerOrderbll = new AddInboundBLL();
        
        public DisplayDataModel orderItem = null;
        public string DisplayTypeName = "雅虎";
        private List<DisplayDataModel> _displayData;

        public FrmAddInbound()
        {
            InitializeComponent();
            CombCariList();
            outPagination.ActivePage = 1;
            AddDataGridView.RowTemplate.Height = 36;
        }

        public void CombCariList()
        {
            CombList.Clear();
            CombList.Add(new InfoCariModel() { Id = 1, Name = "雅虎" });
            CombList.Add(new InfoCariModel() { Id = 2, Name = "煤炉" });
            uiComboBox4.ValueMember = "Id";
            uiComboBox4.DisplayMember = "Name";
            uiComboBox4.DataSource = CombList;
            uiComboBox4.Text = CombList[0].Name;
        }


        private void uiSymbolButton1_Click(object sender, EventArgs e)
        {
            MerOrderModel merOrderMode = new MerOrderModel();
            merOrderMode.pageNumber = 1;
            merOrderMode.pageSize = 10;
            if (uiComboBox4.Text.Contains("雅虎"))
            {
                merOrderMode.type = 4;
            }
            else
            {
                merOrderMode.type = 1;
            }

            merOrderMode.nickName = UserNametext.Text;//用户名
            merOrderMode.orderid = orderNumbertext.Text;//订单号
            merOrderMode.title = titletext.Text;
            FindList(merOrderMode);


        }

        private void uiComboBox4_DropDown(object sender, EventArgs e)
        {
            //uiComboBox4.Text = "";
        }

        private void outPagination_PageChanged(object sender, object pagingSource, int pageIndex, int count)
        {
            //必填
            if (string.IsNullOrEmpty(uiComboBox4.Text))
            {
                this.ShowErrorTip("请选择拍行！");
                return;
            }
            MerOrderModel merOrderMode = new MerOrderModel();

            merOrderMode.pageNumber = pageIndex;
            merOrderMode.pageSize = 10;
            if (uiComboBox4.Text.Contains("雅虎"))
            {
                merOrderMode.type = 4;
            }
            else
            {
                merOrderMode.type = 1;
            }
            FindList(merOrderMode);
        }
        public void FindList(MerOrderModel merOrderMode)
        {

            //必填
            if (string.IsNullOrEmpty(uiComboBox4.Text))
            {
                this.ShowErrorTip("请选择拍行！");
                return;
            }
            if (uiComboBox4.Text.Contains("雅虎"))
            {
                DisplayTypeName = "雅虎";
            }
            else
            {
                DisplayTypeName = "煤炉";
            }

            string requestData = JsonConvert.SerializeObject(merOrderMode);
            string result = AddMerOrderbll.AddMerOrderBll(requestData, Program.Strtoken, uiComboBox4.Text);
            if (result.Contains("登录已过期请重新登录"))
            {
                UIMessageBox.Show("登录已过期！请退出软件重新登录！");
                return;
            }
            var response = JsonConvert.DeserializeObject<ResponseModel>(result);
            if (response != null && response.code == 0)
            {
                // 使用强类型（推荐）
                _displayData = response.data.list.Select(item => new DisplayDataModel
                {
                    userid = item.user?.userid ?? 0,
                    orderid = item.orderid,
                    goodCode = item.goodCode,
                    phone = item.user?.phone,
                    price = item.price,
                    title = item.title,
                    nickName = item.user?.nickName
                }).ToList();
                AddDataGridView.DataSource = _displayData;
                // 去除首列
                AddDataGridView.RowHeadersVisible = false;
            }
        }
        /// <summary>
        /// 确定按钮点击事件
        /// </summary>
        private void btnOK_Click(object sender, EventArgs e)
        {
            // 检查是否有选中的行
            if (AddDataGridView.SelectedRows.Count > 0)
            {
                // 获取第一个选中的行（如果MultiSelect=false则只有一行）
                DataGridViewRow selectedRow = AddDataGridView.SelectedRows[0];

                // 获取绑定的数据对象
                 orderItem = selectedRow.DataBoundItem as DisplayDataModel; // 使用您实际的类名
            }
            else
            {
                MessageBox.Show("请先选择要入库的行", "提示", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }
   
    }
}
