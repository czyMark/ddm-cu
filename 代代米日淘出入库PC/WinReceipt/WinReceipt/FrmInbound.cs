using Newtonsoft.Json;
using ReceiptBLL;
using ReceiptModel;
using Sunny.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Printing;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml.Linq;
using ZXing;
using ZXing.Common;
using static System.Net.Mime.MediaTypeNames;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.StartPanel;

namespace WinReceipt
{
    public partial class FrmInbound : UIPage
    {
        #region 下拉集合
        public static List<InfoCariModel> CombCariListList = new List<InfoCariModel>();//拍行集合
        #endregion
        #region 扫码枪
        // 扫码枪数据缓冲区
        private string _barcodeBuffer = "";
        private DateTime _lastKeyTime = DateTime.Now;

        // 键盘钩子相关API
        private const int WH_KEYBOARD_LL = 13;
        private const int WM_KEYDOWN = 0x0100;
        private IntPtr _hookID = IntPtr.Zero;

        // 确保委托不会被垃圾回收
        private readonly LowLevelKeyboardProc _proc;

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr SetWindowsHookEx(int idHook, LowLevelKeyboardProc lpfn, IntPtr hMod, uint dwThreadId);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        private static extern bool UnhookWindowsHookEx(IntPtr hhk);

        [DllImport("kernel32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr GetModuleHandle(string lpModuleName);

        [DllImport("user32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern IntPtr CallNextHookEx(IntPtr hhk, int nCode, IntPtr wParam, IntPtr lParam);

        private delegate IntPtr LowLevelKeyboardProc(int nCode, IntPtr wParam, IntPtr lParam);
        #endregion
        private static InboundBLL Inbll = new InboundBLL();
        private static OutboundBll Outbll = new OutboundBll();
        private static AddInboundBLL AddMerOrderbll = new AddInboundBLL();
        int totalCount = 0;
        private ScanerHook listener = new ScanerHook();

        public FrmInbound()
        {
            InitializeComponent();
            //Refresh();
            ////设置分页控件总数
            //button();
            uiPagination1.PageSize = 20;
            uiDataGridView1.RowTemplate.Height = 50;
            LoadPageData(1);
            Addbutton();
            CombCariList();
            uiPagination1.ActivePage = 1;
            //扫码枪
            // 关键设置：让窗体先接收键盘事件
            this.KeyPreview = true;

            // 挂接键盘事件
            // 初始化委托实例
            _proc = HookCallback;

            // 安装键盘钩子
            InstallHook();

            // 确保窗体关闭时卸载钩子
            this.FormClosing += (s, e) => UninstallHook();
        }

        public void CombCariList()
        {
            CombCariListList.Add(new InfoCariModel() { Id = 1, Name = "雅虎" });
            CombCariListList.Add(new InfoCariModel() { Id = 2, Name = "煤炉" });
            UiComb_MerCari.ValueMember = "Id";
            UiComb_MerCari.DisplayMember = "Name";
            UiComb_MerCari.DataSource = CombCariListList;
        }
        public void Addbutton()
        {
            // 打印按钮列
            DataGridViewButtonColumn btnPrint = new DataGridViewButtonColumn();
            btnPrint.Name = "printbut";
            btnPrint.HeaderText = " ";
            btnPrint.DefaultCellStyle.NullValue = "打印";

            // 设置样式
            btnPrint.Width = 60;
            //btnDel.DefaultCellStyle.ForeColor = Color.White;  // 白色字体
            btnPrint.DefaultCellStyle.BackColor = Color.Black;  // 淡蓝色背景
            btnPrint.DefaultCellStyle.SelectionBackColor = Color.LightBlue;  // 选中时保持颜色
            btnPrint.FlatStyle = FlatStyle.Standard;  // 扁平样式

            uiDataGridView1.Columns.Add(btnPrint);

            // 可选：设置整个DataGridView的单元格样式
            uiDataGridView1.EnableHeadersVisualStyles = false;  // 禁用系统默认样式
            uiDataGridView1.DefaultCellStyle.Font = new Font("Microsoft YaHei", 9);  // 设置字体
        }


        /// <summary>
        /// 入库
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void but_AddInbound(object sender, EventArgs e)
        {
            //必填
            if (string.IsNullOrEmpty(UiComb_MerCari.Text))
            {
                this.ShowErrorTip("请选择拍行！");
                return;
            }
            if (string.IsNullOrEmpty(Text_UserID.Text))
            {
                this.ShowErrorTip("用户ID不能为空");
                return;
            }
            if (string.IsNullOrEmpty(Text_Ordernumber.Text))
            {
                this.ShowErrorTip("订单号不能为空");
                return;
            }
            if (string.IsNullOrEmpty(Text_ProductID.Text))
            {
                this.ShowErrorTip("商品ID不能为空");
                return;
            }

            updataType();

        }
        #region 入库出库接口

        #endregion
        /// <summary>
        /// 入库接口
        /// </summary>
        public void updataType()
        {
            YahooOrderModel merOrderMode = new YahooOrderModel();
            //merOrderMode.id = Text_Ordernumber.Text;//订单id
            //merOrderMode.userid = Text_UserID.Text;//用户id

            //先校验是否有这个商品
            int temp = 0;
            if (!int.TryParse(Text_UserID.Text.ToString(), out temp))
            {
                UIMessageBox.Show("用户ID必须是字符串");
                return;
            }

            merOrderMode.orderid = Text_Ordernumber.Text;
            merOrderMode.userid = int.Parse(Text_UserID.Text);
            if (UiComb_MerCari.Text.Contains("雅虎"))
            {
                merOrderMode.type = 5;//雅虎5
            }
            else
            {
                merOrderMode.type = 2;//煤炉2
            }
            string requestData = JsonConvert.SerializeObject(merOrderMode);
            string result = AddMerOrderbll.AddMerYahoOrderBll(requestData, Program.Strtoken, UiComb_MerCari.Text);
            if (result.Contains("登录已过期请重新登录"))
            {
                UIMessageBox.Show("登录已过期！请退出软件重新登录！");
                return;
            }
            var response = JsonConvert.DeserializeObject<ResponseExceModel>(result);
            if (response.data != null && response.code == 0)
            {
                InboundModel inboundModel = new InboundModel();
                inboundModel.UserID = Text_UserID.Text;
                inboundModel.Userphone = Text_Userphone.Text;
                inboundModel.Ordernumber = Text_Ordernumber.Text;
                inboundModel.ProductID = Text_ProductID.Text;
                inboundModel.ProductName = Text_ProductName.Text;
                inboundModel.price = Text_price.Text;
                inboundModel.MerCari = UiComb_MerCari.Text;
                inboundModel.Remark = inRichText_Remark.Text;
                inboundModel.Time = DateTime.Now;
                inboundModel.Status = "未出库";
                if (Inbll.AddInboundBll(inboundModel))
                {
                    LoadPageData(1);
                    Myprinter(inboundModel);
                    //CODE128格式

                }

                UIMessageBox.Show("入库成功");
            }
            else
            {
                UIMessageBox.ShowError("入库失败");
                return;
            }
        }
        /// <summary>
        /// 出库
        /// </summary>
        public void SendGoodType()
        {
            if (string.IsNullOrEmpty(TextNamenumber.Text))
            {
                this.ShowErrorTip("订单号不能为空!!");
                return;
            }
            var numberitem = FindBumber(1, TextNamenumber.Text.Trim());
            if (numberitem == null || numberitem.Count == 0)
            {
                this.ShowErrorTip("请通过扫码枪或者在入库列表中选择要出库的商品!!");
                return;
            }


            //出库信息验证
            {
                var outList = OutListData(1);
                if (outList.Count > 0)
                {
                    this.ShowErrorTip("商品已出库,禁止出库!!");
                    return;
                }


                //根据订单号  分煤炉 还是 雅虎  查询出订单状态，判断是否能否出库。
                //有数据就是能出库
                //接口地址： 参数:
                OutOrderModelVerify verify = new OutOrderModelVerify();
                verify.orderid = TextNamenumber.Text.Trim();
                if (numberitem[0].MerCari == "雅虎")
                {
                    verify.type = 8;
                }
                else if (numberitem[0].MerCari == "煤炉")
                {
                    verify.type = 5;
                }
                else
                {
                    this.ShowErrorTip("未知平台商品信息错误,禁止出库!!");
                    return;
                }
                string requestData = JsonConvert.SerializeObject(verify);
                string result = AddMerOrderbll.OutMerYahoOrderBllVerify(requestData, Program.Strtoken, numberitem[0].MerCari);
                if (result.Contains("登录已过期请重新登录"))
                {
                    UIMessageBox.Show("登录已过期！请退出软件重新登录！");
                    return;
                }
                var response = JsonConvert.DeserializeObject<ResponseModel>(result);
                if (response.data == null || response.code != 0 || response.data.totalData == 0)
                {
                    this.ShowErrorTip("商品已无法出库，请您通知代代米日淘购买商品用户二次支付商品费用。");
                    return;
                }


            }

            {

                OutOrderModel outOrderModel = new OutOrderModel();
                //merOrderMode.id = Text_Ordernumber.Text;
                //merOrderMode.userid = Text_UserID.Text;
                outOrderModel.wlcompany = companiesTextBox.Text.Trim();//物流公司
                outOrderModel.trackingNumber = uiTextBoxnumber.Text.Trim();//物流单号
                outOrderModel.wlRemarks = TextRemark.Text.Trim();//物流备注


                outOrderModel.userid = numberitem[0].UserID;//用户id
                outOrderModel.orderid = TextNamenumber.Text.Trim();//订单id
                string requestData = JsonConvert.SerializeObject(outOrderModel);
                string result = AddMerOrderbll.OutMerYahoOrderBll(requestData, Program.Strtoken, numberitem[0].MerCari);
                if (result.Contains("登录已过期请重新登录"))
                {
                    UIMessageBox.Show("登录已过期！请退出软件重新登录！");
                    return;
                }
                var response = JsonConvert.DeserializeObject<ResponseExceModel>(result);
                if (response.code == 0)
                {
                    UIMessageBox.Show("出库成功");
                }
                else if (response.code == 500)
                {
                    UIMessageBox.Show("出库失败");
                    return;
                }
            }
        }
        private List<InboundModel> FindBumber(int pageIndex, string nameNumbers)
        {
            string Ordernumber = nameNumbers; // 假设txtNumber是编号查询的文本框
            string Name = ""; // 假设txtNumber是编号查询的文本框
            DataSet DataList = Inbll.GetInboundPagedBll(
                pageIndex,
                uiPagination1.PageSize,
                Name,      // 添加名称查询条件
                Ordernumber,    // 添加编号查询条件
                out int totalCount
            );

            List<InboundModel> inboundList = ConvertToList(DataList);
            return inboundList;
        }
        private List<OutboundModel> OutListData(int pageIndex)
        {
            string NameNumber = TextNamenumber.Text.Trim(); // 
            int PageSize = 10;
            // 获取带查询条件的分页数据
            DataSet DataList = Outbll.GetOutboundPagedBll(
                pageIndex,
                PageSize,
                //Name,      // 添加名称查询条件
                NameNumber,    // 添加编号查询条件
                out int totalCount
            );

            List<OutboundModel> inboundList = OutConvertToList(DataList);
            return inboundList;
        }

        public List<OutboundModel> OutConvertToList(DataSet ds)
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
        public List<InboundModel> ConvertToList(DataSet ds)
        {

            List<InboundModel> list_Inbound = new List<InboundModel>();
            if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                foreach (DataRow row in ds.Tables[0].Rows)
                {
                    InboundModel model = new InboundModel
                    {
                        Id = row["Id"] != DBNull.Value ? Convert.ToInt32(row["Id"]) : 0,
                        UserID = row["UserID"]?.ToString(),
                        Userphone = row["Userphone"]?.ToString(),
                        Ordernumber = row["Ordernumber"]?.ToString(),
                        ProductID = row["ProductID"]?.ToString(),
                        ProductName = row["ProductName"]?.ToString(),
                        price = row["price"]?.ToString(),
                        MerCari = row["MerCari"]?.ToString(),
                        Remark = row["Remark"]?.ToString(),
                        Time = row["Time"] != DBNull.Value ? Convert.ToDateTime(row["Time"]) : DateTime.MinValue,
                        Status = row["Status"]?.ToString()
                    };

                    list_Inbound.Add(model);
                }
            }

            return list_Inbound;
        }

        #region 打印
        /// <summary>
        /// 打印
        /// </summary>
        /// <summary>
        private void Myprinter(InboundModel inboundModel)
        {
            try
            {

                PrintDocument pd = new PrintDocument();
                //pd.PrintPage += new PrintPageEventHandler(printDocument_PrintPage);
                // 使用匿名方法或Lambda表达式捕获inboundModel
                pd.PrintPage += (sender, e) =>
                {
                    printDocument_PrintPage(sender, e, inboundModel);
                };
                string Printtxt = readtext();
                if (Printtxt != null)
                {
                    // 设置打印机和纸张参数
                    //pd.DefaultPageSettings.PrinterSettings.PrinterName = "Deli DL-720C";
                    pd.DefaultPageSettings.PrinterSettings.PrinterName = Printtxt;
                    pd.DefaultPageSettings.PaperSize = new PaperSize("Custom", 315, 236); // 76mm×130mm（转换为百分之一英寸单位）
                    pd.DefaultPageSettings.Landscape = false; // 小票纸通常为纵向走纸
                    pd.PrintController = new StandardPrintController();
                    pd.Print();
                }
            }
            catch (Exception)
            {
                this.ShowErrorTip("未正确连接打印机，请在连接打印机成功后。在入库列表右侧点击打印按钮！打印标签。");
            }

        }

        private void printDocument_PrintPage(object sender, PrintPageEventArgs e, InboundModel inboundModel)
        {
            // 基础设置（单位：百分之一英寸）
            Font textFont = new Font("Microsoft YaHei", 9); // 小票建议用小字号
            Brush brush = Brushes.Black;
            float marginLeft = 10;  // 左边距（约8mm）
            float marginTop = 10;   // 上边距（约8mm）
            float lineHeight = textFont.GetHeight() + 4; // 行高+间距
            float columnWidth = e.PageBounds.Width / 2 - marginLeft + 20; // 计算列宽（左右两列）

            // 第一行：MerCari（左） + UserID（右）
            e.Graphics.DrawString("拍行:" + inboundModel.MerCari, textFont, brush, marginLeft, marginTop);
            e.Graphics.DrawString("用户ID:" + inboundModel.UserID.ToString(), textFont, brush, marginLeft + columnWidth, marginTop);

            // 第二行：ProductID（左） + Userphone（右）
            e.Graphics.DrawString("商品ID:" + inboundModel.ProductID.ToString(), textFont, brush, marginLeft, marginTop + lineHeight);

            e.Graphics.DrawString("入札礼:" + inboundModel.price.ToString(), textFont, brush, marginLeft + columnWidth, marginTop + lineHeight);

            // 第五行：Remark（单独一行，居中或左对齐）
            e.Graphics.DrawString("商品名称:" + inboundModel.ProductName.ToString(), textFont, brush, marginLeft, marginTop + lineHeight * 2);

            e.Graphics.DrawString("手机号:" + inboundModel.Userphone, textFont, brush, marginLeft, marginTop + lineHeight * 3);

            e.Graphics.DrawString("日期:" + inboundModel.Time.ToString(), textFont, brush, marginLeft, marginTop + lineHeight * 4);

            e.Graphics.DrawString("备注:" + inboundModel.Remark.ToString(), textFont, brush, marginLeft, marginTop + lineHeight * 5);

            // 生成条形码（缩小尺寸适配小票纸）
            string barcodeText = inboundModel.Ordernumber;
            var barcodeWriter = new BarcodeWriter
            {
                Format = BarcodeFormat.CODE_128,
                Options = new EncodingOptions
                {
                    Width = 320,  // 缩小宽度
                    Height = 80,  // 缩小高度
                    Margin = 0    // 去除多余边距
                }
            };

            // 绘制条形码（放在最后一行下方）
            var barcodeImage = barcodeWriter.Write(barcodeText);
            float barcodeY = marginTop + lineHeight * 7; // 调整位置，留出足够的空间
            e.Graphics.DrawImage(barcodeImage, marginLeft - 20, barcodeY);
        }
        public string readtext()
        {
            // 获取项目根目录路径
            string configDirectory = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "printConfig");

            // 如果目录不存在则创建
            if (!Directory.Exists(configDirectory))
            {
                Directory.CreateDirectory(configDirectory);
            }

            // 配置文件完整路径
            string configFilePath = Path.Combine(configDirectory, "printConfig.txt");

            // 读取文件内容（如果文件存在）
            string fileContent = "";
            if (File.Exists(configFilePath))
            {
                try
                {
                    // 读取文件所有内容
                    fileContent = File.ReadAllText(configFilePath);

                }
                catch (Exception ex)
                {

                }
            }
            else
            {

            }
            return fileContent;
        }

        #endregion


        private void LoadPageData(int pageIndex)
        {
            // 从界面控件获取查询条件
            string Name = TextSp.Text.Trim(); // 假设txtName是名称查询的文本框
            string Ordernumber = TextBm.Text.Trim(); // 假设txtNumber是编号查询的文本框



            // 创建Stopwatch实例
            Stopwatch stopwatch = new Stopwatch();
            // 开始计时
            stopwatch.Start();

            // 获取带查询条件的分页数据
            DataSet DataList = Inbll.GetInboundPagedBll(
                pageIndex,
                uiPagination1.PageSize,
                Name,      // 添加名称查询条件
                Ordernumber,    // 添加编号查询条件
                out int totalCount
            );
            stopwatch.Stop();
            List<InboundModel> inboundList = ConvertToList(DataList);
            // 关键！更新分页控件的总记录数
            uiPagination1.TotalCount = totalCount;
            uiDataGridView1.DataSource = inboundList;
            // 去除首列
            uiDataGridView1.RowHeadersVisible = false;
        }

        /// <summary>
        /// 页切换
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="pagingSource"></param>
        /// <param name="pageIndex"></param>
        /// <param name="count"></param>
        private void uiPagination1_PageChanged(object sender, object pagingSource, int pageIndex, int count)
        {

            LoadPageData(pageIndex);
        }

        private void uiSymbolButton1_Click(object sender, EventArgs e)
        {
            LoadPageData(1);
        }

        private void uiButton3_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(TextNamenumber.Text))
            {
                this.ShowErrorTip("扫描商品编码为必填项");
                return;
            }
            if (string.IsNullOrEmpty(TextNamesum.Text) && TextNamesum.Text == "0")
            {
                this.ShowErrorTip("商品数量不能为0");
                return;
            }
            if (string.IsNullOrEmpty(TextWheight.Text) && TextWheight.Text == "0")
            {
                this.ShowErrorTip("商品重量不能为0kg");
                return;
            }
            if (string.IsNullOrEmpty(companiesTextBox.Text))
            {
                this.ShowErrorTip("物流公司为必填项");
                return;
            }
            if (string.IsNullOrEmpty(uiTextBoxnumber.Text))
            {
                this.ShowErrorTip("物流单号为必填项");
                return;
            }


            OutboundModel OutboundModel = new OutboundModel();
            OutboundModel.NameNumber = TextNamenumber.Text;
            OutboundModel.NameSum = TextNamesum.Text;
            OutboundModel.NameWeight = TextWheight.Text;
            OutboundModel.Remark = TextRemark.Text;
            OutboundModel.Companies = companiesTextBox.Text;
            OutboundModel.Ordernumber = uiTextBoxnumber.Text;
            OutboundModel.Time = DateTime.Now;
            SendGoodType();
            if (Outbll.AddInboundBll(OutboundModel))
            {
                //Myprinter(inboundModel);
                //调用得力标签打印机打印条形码加UserID，Name，NameNumber这三个的数据生成的编号
                InboundModel inbound = new InboundModel();
                inbound.Status = "已出库";
                inbound.Ordernumber = TextNamenumber.Text;
                Inbll.UpdateInboundStatusBll(inbound);
                LoadPageData(1);
            }
            else
            {
                UIMessageBox.Show("出库写入数据库失败");
                return;
            }
        }

        private void uiDataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            // 确保点击的是有效行且不是标题行
            if (e.RowIndex >= 0 && e.ColumnIndex >= 0)
            {
                // 获取当前点击的列名
                string columnName = uiDataGridView1.Columns[e.ColumnIndex].Name;
                // 获取当前行的数据
                DataGridViewRow row = uiDataGridView1.Rows[e.RowIndex];
                if (columnName == "printbut") // 打印按钮
                {
                    // 更安全的获取选中行方式
                    if (uiDataGridView1.SelectedRows.Count > 0)
                    {
                        DataGridViewRow selectedRow = uiDataGridView1.SelectedRows[0];

                        // 获取绑定的数据对象
                        var dataitem = selectedRow.DataBoundItem as InboundModel;
                        InboundModel inboundModel = new InboundModel();
                        inboundModel.UserID = dataitem.UserID;
                        inboundModel.Userphone = dataitem.Userphone;
                        inboundModel.Ordernumber = dataitem.Ordernumber;
                        inboundModel.ProductID = dataitem.ProductID;
                        inboundModel.ProductName = dataitem.ProductName;
                        inboundModel.price = dataitem.price;
                        inboundModel.MerCari = dataitem.MerCari;
                        inboundModel.Remark = dataitem.Remark;
                        inboundModel.Time = DateTime.Now;
                        inboundModel.Status = "未出库";
                        Myprinter(inboundModel);
                    }
                    else
                    {
                        this.ShowErrorTip("请先选中一行");
                    }
                }
            }


        }

        private void TextSp_ButtonClick(object sender, EventArgs e)
        {
            TextSp.Text = "";
        }

        private void TextBm_ButtonClick(object sender, EventArgs e)
        {
            TextBm.Text = "";
        }

        private void UiCombMerCariList_SelectedValueChanged(object sender, EventArgs e)
        {
            UiComb_MerCari.SelectedValue.WriteConsole();
            UiComb_MerCari.SelectedItem.WriteConsole();
            UiComb_MerCari.Text.WriteConsole();
        }



        private void btnAdd_Click(object sender, EventArgs e)
        {

            FrmAddInbound frm = new FrmAddInbound();
            frm.Render();
            frm.ShowDialog();
            if (frm.IsOK)
            {
                Text_UserID.Text = frm.orderItem.userid.ToString();//用户ID
                Text_Userphone.Text = frm.orderItem.phone;//用户手机号
                Text_Ordernumber.Text = frm.orderItem.orderid;//订单号
                Text_ProductID.Text = frm.orderItem.goodCode;//商品ID
                Text_ProductName.Text = frm.orderItem.title;//商品名称
                UiComb_MerCari.Text = frm.uiComboBox4.Text;
                Text_price.Text = frm.orderItem.price;
                //绑定选中的数据，反填到文本框UserID
            }
            frm.Dispose();
        }
        #region 扫码枪 
        private void InstallHook()
        {
            using (var curProcess = Process.GetCurrentProcess())
            using (var curModule = curProcess.MainModule)
            {
                _hookID = SetWindowsHookEx(WH_KEYBOARD_LL, _proc,
                    GetModuleHandle(curModule.ModuleName), 0);

                if (_hookID == IntPtr.Zero)
                {
                    int errorCode = Marshal.GetLastWin32Error();
                    throw new Exception($"设置钩子失败，错误代码: {errorCode}");
                }
            }
        }

        private void UninstallHook()
        {
            if (_hookID != IntPtr.Zero)
            {
                UnhookWindowsHookEx(_hookID);
                _hookID = IntPtr.Zero;
            }
        }

        private IntPtr HookCallback(int nCode, IntPtr wParam, IntPtr lParam)
        {
            if (nCode >= 0 && wParam == (IntPtr)WM_KEYDOWN)
            {
                if ((DateTime.Now - _lastKeyTime).TotalSeconds > 1)
                {
                    // 更新最后时间戳
                    _barcodeBuffer = "";
                    _lastKeyTime = DateTime.Now;
                }


                int vkCode = Marshal.ReadInt32(lParam);
                var key = (Keys)vkCode;

                // 处理回车键（扫码枪通常以回车结束）
                if (key == Keys.Enter && _barcodeBuffer.Length >= 15)
                {
                    // 从后往前截取15个字符
                    string last15 = _barcodeBuffer.Substring(_barcodeBuffer.Length - 15);
                    // 检查是否全是数字
                    if (last15.All(char.IsDigit))
                    {
                        this.BeginInvoke((MethodInvoker)delegate
                        {
                            TextNamenumber.Text = _barcodeBuffer;
                            _barcodeBuffer = "";
                        });
                        return (IntPtr)1;
                    }
                    else
                    {
                        _barcodeBuffer = "";
                    }

                }
                else if (key == Keys.Enter)
                {
                    _barcodeBuffer = "";
                }
                else if (char.IsLetterOrDigit((char)key) ||
                    key == Keys.Space ||
                    key == Keys.OemMinus)
                {
                    _barcodeBuffer += (char)key;
                }
                _lastKeyTime = DateTime.Now;
            }
            return CallNextHookEx(_hookID, nCode, wParam, lParam);
        }




        #endregion
        private void UiComb_MerCari_DropDown(object sender, EventArgs e)
        {
            //UiComb_MerCari.Text = "";
        }
        private void uiDataGridView1_SelectionChanged(object sender, EventArgs e)
        {

            // 检查是否有选中的行
            if (uiDataGridView1.SelectedRows.Count > 0)
            {
                // 获取第一个选中的行（如果MultiSelect=false则只有一行）
                DataGridViewRow selectedRow = uiDataGridView1.SelectedRows[0];

                // 获取绑定的数据对象
                var dataitem = selectedRow.DataBoundItem as InboundModel;
                TextNamenumber.Text = dataitem.Ordernumber;
            }


        }

        private void Text_ProductName_TextChanged(object sender, EventArgs e)
        {

        }

        private void uiLabel8_Click(object sender, EventArgs e)
        {

        }
    }
}
