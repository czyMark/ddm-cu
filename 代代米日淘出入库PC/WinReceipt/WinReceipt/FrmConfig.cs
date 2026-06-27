using Sunny.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WinReceipt
{
    public partial class FrmConfig : UIPage
    {
        public static List<string> PrinterList = new List<string>();
        public FrmConfig()
        {
            InitializeComponent();
            PrintList();
        }
        public void PrintList()
        {
            PrinterList.Clear();
            foreach (string item in PrinterSettings.InstalledPrinters)
            {
                PrinterList.Add(item);
            }
            CombPrintList.DataGridView.Init();
            CombPrintList.ItemSize = new System.Drawing.Size(360, 240);

            CombPrintList.DataGridView.AddColumn("打印机名称", "PrinterName");

            CombPrintList.DataGridView.ReadOnly = true;
            DataTable dtPrinters = new DataTable();
            dtPrinters.Columns.Add("PrinterName", typeof(string)); 
            // 填充数据
            foreach (string printer in PrinterList)
            {
                DataRow row = dtPrinters.NewRow();
                row["PrinterName"] = printer;
                dtPrinters.Rows.Add(row);
            }
            CombPrintList.DataGridView.DataSource = dtPrinters;
            CombPrintList.FilterColumnName = "PrinterName";
            try
            {
                string printerText = readtext();

                if (!string.IsNullOrEmpty(printerText))
                {
                    if (CombPrintList.InvokeRequired)  // WinForms
                    {
                        CombPrintList.Invoke(new Action(() => CombPrintList.Text = printerText));
                    }
                    else
                    {
                        CombPrintList.Text = printerText;
                    }
                }
                else
                {
                    CombPrintList.Text = "未检测到打印机";
                }
            }
            catch (Exception ex)
            {
                // 记录错误或提示用户
                UIMessageBox.Show($"读取打印机信息失败: {ex.Message}");
                CombPrintList.Text = "读取失败";
            }
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

        private void CombPrintList_ValueChanged(object sender, object value)
        {
            CombPrintList.Text = "";
            if (value != null && value is DataGridViewRow)
            {
                DataGridViewRow row = (DataGridViewRow)value;
                CombPrintList.Text = row.Cells[0].Value.ToString();//通过ColumnName显示值

            }
        }

        private void uiButton1_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(CombPrintList.Text))
            {
                this.ShowErrorTip("打印机不能为空");
                return;
            }
            SavePrinterToConfig();
        }
        // 保存到配置文件的方法
        private void SavePrinterToConfig()
        {
            try
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

                // 获取当前选择的打印机名称
                string printerName = CombPrintList.Text.ToString();

                // 写入文件（覆盖模式）
                File.WriteAllText(configFilePath, printerName);

                // 可选：显示保存成功提示
                UIMessageBox.Show($"打印机配置已保存: {printerName}", "保存成功" );
            }
            catch (Exception ex)
            {
                UIMessageBox.Show($"保存配置失败: {ex.Message}");
            }
        }
    }
}
