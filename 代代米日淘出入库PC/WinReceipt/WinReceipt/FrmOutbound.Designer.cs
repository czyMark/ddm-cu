namespace WinReceipt
{
    partial class FrmOutbound
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle3 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle4 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle5 = new System.Windows.Forms.DataGridViewCellStyle();
            this.outPagination = new Sunny.UI.UIPagination();
            this.outDataGridView = new Sunny.UI.UIDataGridView();
            this.Column6 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column2 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column3 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column5 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column8 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column7 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column4 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.uiLabel10 = new Sunny.UI.UILabel();
            this.uiSymbolButton1 = new Sunny.UI.UISymbolButton();
            this.uiSplitContainer1 = new Sunny.UI.UISplitContainer();
            this.TextOutBm = new Sunny.UI.UITextBox();
            ((System.ComponentModel.ISupportInitialize)(this.outDataGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.uiSplitContainer1)).BeginInit();
            this.uiSplitContainer1.Panel1.SuspendLayout();
            this.uiSplitContainer1.Panel2.SuspendLayout();
            this.uiSplitContainer1.SuspendLayout();
            this.SuspendLayout();
            // 
            // outPagination
            // 
            this.outPagination.ActivePage = 20;
            this.outPagination.ButtonFillSelectedColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(128)))), ((int)(((byte)(204)))));
            this.outPagination.ButtonStyleInherited = false;
            this.outPagination.CausesValidation = false;
            this.outPagination.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.outPagination.Font = new System.Drawing.Font("宋体", 12F);
            this.outPagination.Location = new System.Drawing.Point(0, 666);
            this.outPagination.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.outPagination.MinimumSize = new System.Drawing.Size(1, 1);
            this.outPagination.Name = "outPagination";
            this.outPagination.PagerCount = 11;
            this.outPagination.PageSize = 50;
            this.outPagination.RadiusSides = Sunny.UI.UICornerRadiusSides.None;
            this.outPagination.RectSides = System.Windows.Forms.ToolStripStatusLabelBorderSides.None;
            this.outPagination.ShowText = false;
            this.outPagination.Size = new System.Drawing.Size(1328, 35);
            this.outPagination.TabIndex = 18;
            this.outPagination.Text = "uiDataGridPage1";
            this.outPagination.TextAlignment = System.Drawing.ContentAlignment.MiddleCenter;
            this.outPagination.TotalCount = 40000;
            this.outPagination.PageChanged += new Sunny.UI.UIPagination.OnPageChangeEventHandler(this.outPagination_PageChanged);
            // 
            // outDataGridView
            // 
            this.outDataGridView.AllowUserToAddRows = false;
            this.outDataGridView.AllowUserToDeleteRows = false;
            dataGridViewCellStyle1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            this.outDataGridView.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            this.outDataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.outDataGridView.BackgroundColor = System.Drawing.Color.White;
            this.outDataGridView.ColumnHeadersBorderStyle = System.Windows.Forms.DataGridViewHeaderBorderStyle.Single;
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleCenter;
            dataGridViewCellStyle2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle2.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle2.ForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle2.SelectionBackColor = System.Drawing.SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.outDataGridView.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            this.outDataGridView.ColumnHeadersHeight = 48;
            this.outDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.DisableResizing;
            this.outDataGridView.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.Column6,
            this.Column2,
            this.Column3,
            this.Column5,
            this.Column8,
            this.Column7,
            this.Column4,
            this.Column1});
            dataGridViewCellStyle3.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = System.Drawing.SystemColors.Window;
            dataGridViewCellStyle3.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle3.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            dataGridViewCellStyle3.SelectionBackColor = System.Drawing.SystemColors.Highlight;
            dataGridViewCellStyle3.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle3.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.outDataGridView.DefaultCellStyle = dataGridViewCellStyle3;
            this.outDataGridView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.outDataGridView.EnableHeadersVisualStyles = false;
            this.outDataGridView.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.outDataGridView.GridColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            this.outDataGridView.Location = new System.Drawing.Point(0, 0);
            this.outDataGridView.Margin = new System.Windows.Forms.Padding(15);
            this.outDataGridView.Name = "outDataGridView";
            this.outDataGridView.ReadOnly = true;
            dataGridViewCellStyle4.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle4.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle4.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            dataGridViewCellStyle4.SelectionBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle4.SelectionForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle4.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.outDataGridView.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewCellStyle5.BackColor = System.Drawing.Color.White;
            dataGridViewCellStyle5.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.outDataGridView.RowsDefaultCellStyle = dataGridViewCellStyle5;
            this.outDataGridView.RowTemplate.DefaultCellStyle.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.outDataGridView.RowTemplate.Height = 23;
            this.outDataGridView.SelectedIndex = -1;
            this.outDataGridView.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.outDataGridView.Size = new System.Drawing.Size(1328, 666);
            this.outDataGridView.StripeOddColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            this.outDataGridView.TabIndex = 19;
            // 
            // Column6
            // 
            this.Column6.DataPropertyName = "Id";
            this.Column6.HeaderText = "序号";
            this.Column6.Name = "Column6";
            this.Column6.ReadOnly = true;
            // 
            // Column2
            // 
            this.Column2.DataPropertyName = "NameNumber";
            this.Column2.HeaderText = "扫描商品编码（条形码）";
            this.Column2.Name = "Column2";
            this.Column2.ReadOnly = true;
            // 
            // Column3
            // 
            this.Column3.DataPropertyName = "NameSum";
            this.Column3.HeaderText = "商品数量(个)";
            this.Column3.Name = "Column3";
            this.Column3.ReadOnly = true;
            // 
            // Column5
            // 
            this.Column5.DataPropertyName = "NameWeight";
            this.Column5.HeaderText = "商品重量kg";
            this.Column5.Name = "Column5";
            this.Column5.ReadOnly = true;
            // 
            // Column8
            // 
            this.Column8.DataPropertyName = "Companies";
            this.Column8.HeaderText = "物流公司";
            this.Column8.Name = "Column8";
            this.Column8.ReadOnly = true;
            // 
            // Column7
            // 
            this.Column7.DataPropertyName = "Ordernumber";
            this.Column7.HeaderText = "物流单号";
            this.Column7.Name = "Column7";
            this.Column7.ReadOnly = true;
            // 
            // Column4
            // 
            this.Column4.DataPropertyName = "Time";
            this.Column4.HeaderText = "出库时间";
            this.Column4.Name = "Column4";
            this.Column4.ReadOnly = true;
            // 
            // Column1
            // 
            this.Column1.DataPropertyName = "Remark";
            this.Column1.HeaderText = "备注";
            this.Column1.Name = "Column1";
            this.Column1.ReadOnly = true;
            // 
            // uiLabel10
            // 
            this.uiLabel10.Font = new System.Drawing.Font("宋体", 18F);
            this.uiLabel10.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            this.uiLabel10.Location = new System.Drawing.Point(13, 22);
            this.uiLabel10.Name = "uiLabel10";
            this.uiLabel10.Size = new System.Drawing.Size(121, 23);
            this.uiLabel10.TabIndex = 30;
            this.uiLabel10.Text = "商品编号:";
            // 
            // uiSymbolButton1
            // 
            this.uiSymbolButton1.Cursor = System.Windows.Forms.Cursors.Hand;
            this.uiSymbolButton1.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.uiSymbolButton1.Location = new System.Drawing.Point(364, 12);
            this.uiSymbolButton1.MinimumSize = new System.Drawing.Size(1, 1);
            this.uiSymbolButton1.Name = "uiSymbolButton1";
            this.uiSymbolButton1.Size = new System.Drawing.Size(88, 40);
            this.uiSymbolButton1.Symbol = 61442;
            this.uiSymbolButton1.TabIndex = 31;
            this.uiSymbolButton1.Text = "查找";
            this.uiSymbolButton1.TipsFont = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.uiSymbolButton1.Click += new System.EventHandler(this.uiSymbolButton1_Click);
            // 
            // uiSplitContainer1
            // 
            this.uiSplitContainer1.Cursor = System.Windows.Forms.Cursors.Default;
            this.uiSplitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiSplitContainer1.Location = new System.Drawing.Point(0, 0);
            this.uiSplitContainer1.MinimumSize = new System.Drawing.Size(20, 20);
            this.uiSplitContainer1.Name = "uiSplitContainer1";
            this.uiSplitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // uiSplitContainer1.Panel1
            // 
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiSymbolButton1);
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiLabel10);
            this.uiSplitContainer1.Panel1.Controls.Add(this.TextOutBm);
            // 
            // uiSplitContainer1.Panel2
            // 
            this.uiSplitContainer1.Panel2.Controls.Add(this.outDataGridView);
            this.uiSplitContainer1.Panel2.Controls.Add(this.outPagination);
            this.uiSplitContainer1.Panel2.Paint += new System.Windows.Forms.PaintEventHandler(this.uiSplitContainer1_Panel2_Paint);
            this.uiSplitContainer1.Size = new System.Drawing.Size(1328, 774);
            this.uiSplitContainer1.SplitterDistance = 72;
            this.uiSplitContainer1.SplitterWidth = 1;
            this.uiSplitContainer1.TabIndex = 0;
            // 
            // TextOutBm
            // 
            this.TextOutBm.ButtonWidth = 100;
            this.TextOutBm.CanEmpty = true;
            this.TextOutBm.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.TextOutBm.Font = new System.Drawing.Font("宋体", 12F);
            this.TextOutBm.Location = new System.Drawing.Point(141, 14);
            this.TextOutBm.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.TextOutBm.MinimumSize = new System.Drawing.Size(1, 1);
            this.TextOutBm.Name = "TextOutBm";
            this.TextOutBm.Padding = new System.Windows.Forms.Padding(5);
            this.TextOutBm.ShowText = false;
            this.TextOutBm.Size = new System.Drawing.Size(195, 38);
            this.TextOutBm.TabIndex = 29;
            this.TextOutBm.TextAlignment = System.Drawing.ContentAlignment.MiddleLeft;
            this.TextOutBm.Watermark = "请输入编号";
            // 
            // FrmOutbound
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.ClientSize = new System.Drawing.Size(1328, 774);
            this.Controls.Add(this.uiSplitContainer1);
            this.Name = "FrmOutbound";
            this.Text = "FrmOutbound";
            ((System.ComponentModel.ISupportInitialize)(this.outDataGridView)).EndInit();
            this.uiSplitContainer1.Panel1.ResumeLayout(false);
            this.uiSplitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.uiSplitContainer1)).EndInit();
            this.uiSplitContainer1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private Sunny.UI.UIPagination outPagination;
        private Sunny.UI.UIDataGridView outDataGridView;
        private Sunny.UI.UILabel uiLabel10;
        private Sunny.UI.UISymbolButton uiSymbolButton1;
        private Sunny.UI.UISplitContainer uiSplitContainer1;
        private Sunny.UI.UITextBox TextOutBm;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column6;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column2;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column3;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column5;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column8;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column7;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column4;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column1;
    }
}