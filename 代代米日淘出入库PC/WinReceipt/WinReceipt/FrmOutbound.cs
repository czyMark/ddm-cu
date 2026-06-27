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
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WinReceipt
{
    public partial class FrmOutbound : UIPage
    {
        private static OutboundBll Outbll = new OutboundBll();

        public FrmOutbound()
        {
            InitializeComponent();
            LoadPageData(1);
            outPagination.ActivePage = 1;
            outDataGridView.RowTemplate.Height = 50;
        }
        private void LoadPageData(int pageIndex)
        {
            string NameNumber = TextOutBm.Text.Trim(); // 

            // 获取带查询条件的分页数据
            DataSet DataList = Outbll.GetOutboundPagedBll(
                pageIndex,
                outPagination.PageSize,
                //Name,      // 添加名称查询条件
                NameNumber,    // 添加编号查询条件
                out int totalCount
            );

            List<OutboundModel> inboundList = ConvertToList(DataList);
            // 关键！更新分页控件的总记录数
            outPagination.TotalCount = totalCount;
            outDataGridView.DataSource = inboundList;
            // 去除首列
            outDataGridView.RowHeadersVisible = false;
        }
        public List<OutboundModel> ConvertToList(DataSet ds)
        {

            List<OutboundModel> list_Inbound = new List<OutboundModel>();
            if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                foreach (DataRow row in ds.Tables[0].Rows)
                {
                    OutboundModel model = new OutboundModel
                    {
                        Id = row["Id"] != DBNull.Value ? Convert.ToInt32(row["Id"]) : 0,
                        NameNumber = row["NameNumber"]?.ToString(),
                        NameSum = row["NameSum"]?.ToString(),
                        NameWeight = row["NameWeight"]?.ToString(),
                        Companies = row["Companies"]?.ToString(),
                        Ordernumber = row["Ordernumber"]?.ToString(),
                        Time = row["Time"] != DBNull.Value ? Convert.ToDateTime(row["Time"]) : DateTime.MinValue,
                        Remark = row["Remark"]?.ToString(),
                    };

                    list_Inbound.Add(model);
                }
            }

            return list_Inbound;
        }

        private void outPagination_PageChanged(object sender, object pagingSource, int pageIndex, int count)
        {
            LoadPageData(pageIndex);
        }

        private void uiSymbolButton1_Click(object sender, EventArgs e)
        {
            LoadPageData(1);
        }

        private void uiSplitContainer1_Panel2_Paint(object sender, PaintEventArgs e)
        {
            LoadPageData(1);
        }
    }
}
