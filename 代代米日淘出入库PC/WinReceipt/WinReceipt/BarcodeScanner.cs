using Sunny.UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using ZXing;

namespace WinReceipt
{
    public class BarcodeScanner
    {
        private UITextBox _targetTextBox; // 用于显示扫描结果的文本框
        private Timer _scanTimer;      // 检测连续输入的计时器
        private string _scanBuffer = ""; // 扫描缓存
        private const int ScanDelay = 100; // 扫码枪输入间隔(毫秒)

        public BarcodeScanner(UITextBox textBox)
        {
            _targetTextBox = textBox;

            // 初始化计时器
            _scanTimer = new Timer { Interval = ScanDelay };
            _scanTimer.Tick += (s, e) => ProcessScan();

            // 监听键盘输入
            _targetTextBox.KeyPress += OnKeyPress;
            _targetTextBox.KeyDown += OnKeyDown;
        }

        private void OnKeyPress(object sender, KeyPressEventArgs e)
        {
            // 只处理可打印字符（扫码枪通常发送ASCII字符）
            if (e.KeyChar >= 32 && e.KeyChar <= 126)
            {
                _scanBuffer += e.KeyChar;
                _scanTimer.Stop();
                _scanTimer.Start();
            }
            e.Handled = true; // 阻止输入框直接显示
        }

        private void OnKeyDown(object sender, KeyEventArgs e)
        {
            // 检测回车键（有些扫码枪会发送回车）
            if (e.KeyCode == Keys.Enter && !string.IsNullOrEmpty(_scanBuffer))
            {
                ProcessScan();
                e.Handled = true;
            }
        }

        private void ProcessScan()
        {
            _scanTimer.Stop();

            if (!string.IsNullOrEmpty(_scanBuffer))
            {
                string barcode = _scanBuffer.Trim();
                _scanBuffer = "";

                // 验证是否为有效CODE128（可选）
                try
                {
                    var reader = new BarcodeReader
                    {
                        Options = { PossibleFormats = new[] { BarcodeFormat.CODE_128 } }
                    };

                    // 如果要从图像解码（比如摄像头扫描）：
                    // var result = reader.Decode(cameraBitmap);

                    // 直接使用扫码枪输入（已经是文本）
                    _targetTextBox.Text = barcode;
                    OnBarcodeScanned?.Invoke(this, barcode);
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"解码失败: {ex.Message}");
                }
            }
        }

        // 扫码成功事件
        public event EventHandler<string> OnBarcodeScanned;
    }
}
