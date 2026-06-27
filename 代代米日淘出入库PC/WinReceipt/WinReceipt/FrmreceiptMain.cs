using ReceiptBLL;
using Sunny.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace WinReceipt
{
    public partial class FrmreceiptMain : UIAsideHeaderMainFrame
    {
        public FrmreceiptMain()
        {
            InitializeComponent();

            //设置关联
            Aside.TabControl = MainTabControl;


            //增加页面到
            // 模拟一段耗时的代码，比如这里让线程休眠1秒钟
            AddPage(new FrmInbound(), 1001);
             
            AddPage(new FrmConfig(), 1002);
             
            AddPage(new FrmOutbound(), 1003);
             
            //设置Header节点索引
            Aside.CreateNode("商品出入库", 1001);
            Aside.CreateNode("硬件配置", 1002);
            Aside.CreateNode("商品出库记录", 1003);

            //显示默认界面
            Aside.SelectFirst();
        }
        private void Aside_MenuItemClick(System.Windows.Forms.TreeNode node, NavMenuItem item, int pageIndex)
        {
            Header.Text = "当前界面: " + node.Text;
        }
    }
}
