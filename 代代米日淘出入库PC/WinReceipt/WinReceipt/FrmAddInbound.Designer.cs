namespace WinReceipt
{
    partial class FrmAddInbound
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
            this.uiSplitContainer1 = new Sunny.UI.UISplitContainer();
            this.uiLabel2 = new Sunny.UI.UILabel();
            this.titletext = new Sunny.UI.UITextBox();
            this.uiLabel1 = new Sunny.UI.UILabel();
            this.orderNumbertext = new Sunny.UI.UITextBox();
            this.uiComboBox4 = new Sunny.UI.UIComboBox();
            this.uiSymbolButton1 = new Sunny.UI.UISymbolButton();
            this.uiLabel10 = new Sunny.UI.UILabel();
            this.UserNametext = new Sunny.UI.UITextBox();
            this.AddDataGridView = new Sunny.UI.UIDataGridView();
            this.outPagination = new Sunny.UI.UIPagination();
            this.Column2 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column3 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column5 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column4 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column7 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column8 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.pnlBtm.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.uiSplitContainer1)).BeginInit();
            this.uiSplitContainer1.Panel1.SuspendLayout();
            this.uiSplitContainer1.Panel2.SuspendLayout();
            this.uiSplitContainer1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.AddDataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // pnlBtm
            // 
            this.pnlBtm.Location = new System.Drawing.Point(1, 945);
            this.pnlBtm.Size = new System.Drawing.Size(1591, 55);
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(1463, 12);
            // 
            // btnOK
            // 
            this.btnOK.Location = new System.Drawing.Point(1348, 12);
            this.btnOK.Click += new System.EventHandler(this.btnOK_Click);
            // 
            // uiSplitContainer1
            // 
            this.uiSplitContainer1.Cursor = System.Windows.Forms.Cursors.Default;
            this.uiSplitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.uiSplitContainer1.Location = new System.Drawing.Point(1, 35);
            this.uiSplitContainer1.MinimumSize = new System.Drawing.Size(20, 20);
            this.uiSplitContainer1.Name = "uiSplitContainer1";
            this.uiSplitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // uiSplitContainer1.Panel1
            // 
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiLabel2);
            this.uiSplitContainer1.Panel1.Controls.Add(this.titletext);
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiLabel1);
            this.uiSplitContainer1.Panel1.Controls.Add(this.orderNumbertext);
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiComboBox4);
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiSymbolButton1);
            this.uiSplitContainer1.Panel1.Controls.Add(this.uiLabel10);
            this.uiSplitContainer1.Panel1.Controls.Add(this.UserNametext);
            // 
            // uiSplitContainer1.Panel2
            // 
            this.uiSplitContainer1.Panel2.Controls.Add(this.AddDataGridView);
            this.uiSplitContainer1.Size = new System.Drawing.Size(1591, 965);
            this.uiSplitContainer1.SplitterDistance = 89;
            this.uiSplitContainer1.SplitterWidth = 1;
            this.uiSplitContainer1.TabIndex = 1;
            // 
            // uiLabel2
            // 
            this.uiLabel2.Font = new System.Drawing.Font("宋体", 18F);
            this.uiLabel2.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            this.uiLabel2.Location = new System.Drawing.Point(995, 33);
            this.uiLabel2.Name = "uiLabel2";
            this.uiLabel2.Size = new System.Drawing.Size(121, 23);
            this.uiLabel2.TabIndex = 80;
            this.uiLabel2.Text = "商品名称:";
            // 
            // titletext
            // 
            this.titletext.ButtonWidth = 100;
            this.titletext.CanEmpty = true;
            this.titletext.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.titletext.Font = new System.Drawing.Font("宋体", 12F);
            this.titletext.Location = new System.Drawing.Point(1123, 25);
            this.titletext.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.titletext.MinimumSize = new System.Drawing.Size(1, 1);
            this.titletext.Name = "titletext";
            this.titletext.Padding = new System.Windows.Forms.Padding(5);
            this.titletext.ShowText = false;
            this.titletext.Size = new System.Drawing.Size(195, 38);
            this.titletext.TabIndex = 79;
            this.titletext.TextAlignment = System.Drawing.ContentAlignment.MiddleLeft;
            this.titletext.Watermark = "请输入商品名称";
            // 
            // uiLabel1
            // 
            this.uiLabel1.Font = new System.Drawing.Font("宋体", 18F);
            this.uiLabel1.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            this.uiLabel1.Location = new System.Drawing.Point(634, 33);
            this.uiLabel1.Name = "uiLabel1";
            this.uiLabel1.Size = new System.Drawing.Size(121, 23);
            this.uiLabel1.TabIndex = 78;
            this.uiLabel1.Text = "订单号:";
            // 
            // orderNumbertext
            // 
            this.orderNumbertext.ButtonWidth = 100;
            this.orderNumbertext.CanEmpty = true;
            this.orderNumbertext.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.orderNumbertext.Font = new System.Drawing.Font("宋体", 12F);
            this.orderNumbertext.Location = new System.Drawing.Point(762, 25);
            this.orderNumbertext.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.orderNumbertext.MinimumSize = new System.Drawing.Size(1, 1);
            this.orderNumbertext.Name = "orderNumbertext";
            this.orderNumbertext.Padding = new System.Windows.Forms.Padding(5);
            this.orderNumbertext.ShowText = false;
            this.orderNumbertext.Size = new System.Drawing.Size(195, 38);
            this.orderNumbertext.TabIndex = 77;
            this.orderNumbertext.TextAlignment = System.Drawing.ContentAlignment.MiddleLeft;
            this.orderNumbertext.Watermark = "请输入订单号";
            // 
            // uiComboBox4
            // 
            this.uiComboBox4.DataSource = null;
            this.uiComboBox4.DropDownStyle = Sunny.UI.UIDropDownStyle.DropDownList;
            this.uiComboBox4.FillColor = System.Drawing.Color.White;
            this.uiComboBox4.FilterMaxCount = 50;
            this.uiComboBox4.Font = new System.Drawing.Font("宋体", 12F);
            this.uiComboBox4.ItemHoverColor = System.Drawing.Color.FromArgb(((int)(((byte)(155)))), ((int)(((byte)(200)))), ((int)(((byte)(255)))));
            this.uiComboBox4.Items.AddRange(new object[] {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "111",
            "112",
            "113",
            "114",
            "115",
            "116"});
            this.uiComboBox4.ItemSelectForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            this.uiComboBox4.Location = new System.Drawing.Point(64, 19);
            this.uiComboBox4.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.uiComboBox4.MinimumSize = new System.Drawing.Size(63, 0);
            this.uiComboBox4.Name = "uiComboBox4";
            this.uiComboBox4.Padding = new System.Windows.Forms.Padding(0, 0, 30, 2);
            this.uiComboBox4.RadiusSides = Sunny.UI.UICornerRadiusSides.None;
            this.uiComboBox4.RectSides = System.Windows.Forms.ToolStripStatusLabelBorderSides.Bottom;
            this.uiComboBox4.Size = new System.Drawing.Size(155, 40);
            this.uiComboBox4.SymbolSize = 24;
            this.uiComboBox4.TabIndex = 76;
            this.uiComboBox4.TextAlignment = System.Drawing.ContentAlignment.MiddleLeft;
            this.uiComboBox4.Watermark = "请选择类型";
            this.uiComboBox4.DropDown += new System.EventHandler(this.uiComboBox4_DropDown);
            // 
            // uiSymbolButton1
            // 
            this.uiSymbolButton1.Cursor = System.Windows.Forms.Cursors.Hand;
            this.uiSymbolButton1.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.uiSymbolButton1.Location = new System.Drawing.Point(1383, 25);
            this.uiSymbolButton1.MinimumSize = new System.Drawing.Size(1, 1);
            this.uiSymbolButton1.Name = "uiSymbolButton1";
            this.uiSymbolButton1.Size = new System.Drawing.Size(88, 40);
            this.uiSymbolButton1.Symbol = 61442;
            this.uiSymbolButton1.TabIndex = 31;
            this.uiSymbolButton1.Text = "查找";
            this.uiSymbolButton1.TipsFont = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.uiSymbolButton1.Click += new System.EventHandler(this.uiSymbolButton1_Click);
            // 
            // uiLabel10
            // 
            this.uiLabel10.Font = new System.Drawing.Font("宋体", 18F);
            this.uiLabel10.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            this.uiLabel10.Location = new System.Drawing.Point(266, 29);
            this.uiLabel10.Name = "uiLabel10";
            this.uiLabel10.Size = new System.Drawing.Size(121, 23);
            this.uiLabel10.TabIndex = 30;
            this.uiLabel10.Text = "用户名:";
            // 
            // UserNametext
            // 
            this.UserNametext.ButtonWidth = 100;
            this.UserNametext.CanEmpty = true;
            this.UserNametext.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.UserNametext.Font = new System.Drawing.Font("宋体", 12F);
            this.UserNametext.Location = new System.Drawing.Point(394, 21);
            this.UserNametext.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.UserNametext.MinimumSize = new System.Drawing.Size(1, 1);
            this.UserNametext.Name = "UserNametext";
            this.UserNametext.Padding = new System.Windows.Forms.Padding(5);
            this.UserNametext.ShowText = false;
            this.UserNametext.Size = new System.Drawing.Size(195, 38);
            this.UserNametext.TabIndex = 29;
            this.UserNametext.TextAlignment = System.Drawing.ContentAlignment.MiddleLeft;
            this.UserNametext.Watermark = "请输入用户名";
            // 
            // AddDataGridView
            // 
            this.AddDataGridView.AllowUserToAddRows = false;
            this.AddDataGridView.AllowUserToDeleteRows = false;
            dataGridViewCellStyle1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            this.AddDataGridView.AlternatingRowsDefaultCellStyle = dataGridViewCellStyle1;
            this.AddDataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.AddDataGridView.BackgroundColor = System.Drawing.Color.White;
            this.AddDataGridView.ColumnHeadersBorderStyle = System.Windows.Forms.DataGridViewHeaderBorderStyle.Single;
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleCenter;
            dataGridViewCellStyle2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle2.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle2.ForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle2.SelectionBackColor = System.Drawing.SystemColors.Highlight;
            dataGridViewCellStyle2.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.AddDataGridView.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle2;
            this.AddDataGridView.ColumnHeadersHeight = 48;
            this.AddDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.DisableResizing;
            this.AddDataGridView.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.Column2,
            this.Column1,
            this.Column3,
            this.Column5,
            this.Column4,
            this.Column7,
            this.Column8});
            dataGridViewCellStyle3.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle3.BackColor = System.Drawing.SystemColors.Window;
            dataGridViewCellStyle3.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle3.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            dataGridViewCellStyle3.SelectionBackColor = System.Drawing.SystemColors.Highlight;
            dataGridViewCellStyle3.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle3.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.AddDataGridView.DefaultCellStyle = dataGridViewCellStyle3;
            this.AddDataGridView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.AddDataGridView.EnableHeadersVisualStyles = false;
            this.AddDataGridView.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.AddDataGridView.GridColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            this.AddDataGridView.Location = new System.Drawing.Point(0, 0);
            this.AddDataGridView.Margin = new System.Windows.Forms.Padding(15);
            this.AddDataGridView.Name = "AddDataGridView";
            this.AddDataGridView.ReadOnly = true;
            dataGridViewCellStyle4.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle4.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle4.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle4.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(48)))), ((int)(((byte)(48)))), ((int)(((byte)(48)))));
            dataGridViewCellStyle4.SelectionBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(80)))), ((int)(((byte)(160)))), ((int)(((byte)(255)))));
            dataGridViewCellStyle4.SelectionForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle4.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.AddDataGridView.RowHeadersDefaultCellStyle = dataGridViewCellStyle4;
            dataGridViewCellStyle5.BackColor = System.Drawing.Color.White;
            dataGridViewCellStyle5.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            dataGridViewCellStyle5.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.AddDataGridView.RowsDefaultCellStyle = dataGridViewCellStyle5;
            this.AddDataGridView.RowTemplate.Height = 32;
            this.AddDataGridView.SelectedIndex = -1;
            this.AddDataGridView.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.AddDataGridView.Size = new System.Drawing.Size(1591, 875);
            this.AddDataGridView.StripeOddColor = System.Drawing.Color.FromArgb(((int)(((byte)(235)))), ((int)(((byte)(243)))), ((int)(((byte)(255)))));
            this.AddDataGridView.TabIndex = 20;
            // 
            // outPagination
            // 
            this.outPagination.ActivePage = 20;
            this.outPagination.ButtonFillSelectedColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(128)))), ((int)(((byte)(204)))));
            this.outPagination.ButtonStyleInherited = false;
            this.outPagination.CausesValidation = false;
            this.outPagination.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.outPagination.Font = new System.Drawing.Font("宋体", 12F);
            this.outPagination.Location = new System.Drawing.Point(1, 910);
            this.outPagination.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.outPagination.MinimumSize = new System.Drawing.Size(1, 1);
            this.outPagination.Name = "outPagination";
            this.outPagination.PagerCount = 11;
            this.outPagination.PageSize = 50;
            this.outPagination.RadiusSides = Sunny.UI.UICornerRadiusSides.None;
            this.outPagination.RectSides = System.Windows.Forms.ToolStripStatusLabelBorderSides.None;
            this.outPagination.ShowText = false;
            this.outPagination.Size = new System.Drawing.Size(1591, 35);
            this.outPagination.TabIndex = 19;
            this.outPagination.Text = "uiDataGridPage1";
            this.outPagination.TextAlignment = System.Drawing.ContentAlignment.MiddleCenter;
            this.outPagination.TotalCount = 40000;
            this.outPagination.PageChanged += new Sunny.UI.UIPagination.OnPageChangeEventHandler(this.outPagination_PageChanged);
            // 
            // Column2
            // 
            this.Column2.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column2.DataPropertyName = "userid";
            this.Column2.FillWeight = 0.5F;
            this.Column2.HeaderText = "用户id";
            this.Column2.MinimumWidth = 80;
            this.Column2.Name = "Column2";
            this.Column2.ReadOnly = true;
            // 
            // Column1
            // 
            this.Column1.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column1.DataPropertyName = "nickName";
            this.Column1.FillWeight = 0.5F;
            this.Column1.HeaderText = "用户名";
            this.Column1.MinimumWidth = 100;
            this.Column1.Name = "Column1";
            this.Column1.ReadOnly = true;
            this.Column1.Width = 140;
            // 
            // Column3
            // 
            this.Column3.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column3.DataPropertyName = "phone";
            this.Column3.FillWeight = 1F;
            this.Column3.HeaderText = "用户手机号";
            this.Column3.MinimumWidth = 100;
            this.Column3.Name = "Column3";
            this.Column3.ReadOnly = true;
            this.Column3.Width = 120;
            // 
            // Column5
            // 
            this.Column5.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column5.DataPropertyName = "orderid";
            this.Column5.FillWeight = 1F;
            this.Column5.HeaderText = "订单号";
            this.Column5.MinimumWidth = 100;
            this.Column5.Name = "Column5";
            this.Column5.ReadOnly = true;
            this.Column5.Width = 150;
            // 
            // Column4
            // 
            this.Column4.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column4.DataPropertyName = "goodCode";
            this.Column4.FillWeight = 1F;
            this.Column4.HeaderText = "商品ID";
            this.Column4.MinimumWidth = 140;
            this.Column4.Name = "Column4";
            this.Column4.ReadOnly = true;
            this.Column4.Width = 140;
            // 
            // Column7
            // 
            this.Column7.DataPropertyName = "title";
            this.Column7.FillWeight = 1F;
            this.Column7.HeaderText = "商品名称";
            this.Column7.MinimumWidth = 100;
            this.Column7.Name = "Column7";
            this.Column7.ReadOnly = true;
            // 
            // Column8
            // 
            this.Column8.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Column8.DataPropertyName = "price";
            this.Column8.FillWeight = 1F;
            this.Column8.HeaderText = "入扎礼";
            this.Column8.MinimumWidth = 140;
            this.Column8.Name = "Column8";
            this.Column8.ReadOnly = true;
            this.Column8.Width = 140;
            // 
            // FrmAddInbound
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.ClientSize = new System.Drawing.Size(1593, 1003);
            this.Controls.Add(this.outPagination);
            this.Controls.Add(this.uiSplitContainer1);
            this.Name = "FrmAddInbound";
            this.Text = "查询数据";
            this.TopMost = false;
            this.Controls.SetChildIndex(this.uiSplitContainer1, 0);
            this.Controls.SetChildIndex(this.pnlBtm, 0);
            this.Controls.SetChildIndex(this.outPagination, 0);
            this.pnlBtm.ResumeLayout(false);
            this.uiSplitContainer1.Panel1.ResumeLayout(false);
            this.uiSplitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.uiSplitContainer1)).EndInit();
            this.uiSplitContainer1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.AddDataGridView)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private Sunny.UI.UISplitContainer uiSplitContainer1;
        private Sunny.UI.UISymbolButton uiSymbolButton1;
        private Sunny.UI.UILabel uiLabel10;
        private Sunny.UI.UITextBox UserNametext;
        private Sunny.UI.UIPagination outPagination;
        private Sunny.UI.UIDataGridView AddDataGridView;
        public Sunny.UI.UIComboBox uiComboBox4;
        private Sunny.UI.UILabel uiLabel2;
        private Sunny.UI.UITextBox titletext;
        private Sunny.UI.UILabel uiLabel1;
        private Sunny.UI.UITextBox orderNumbertext;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column2;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column1;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column3;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column5;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column4;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column7;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column8;
    }
}