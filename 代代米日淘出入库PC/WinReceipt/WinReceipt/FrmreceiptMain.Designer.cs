namespace WinReceipt
{
    partial class FrmreceiptMain
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmreceiptMain));
            this.SuspendLayout();
            // 
            // Header
            // 
            this.Header.FillColor = System.Drawing.Color.White;
            this.Header.ForeColor = System.Drawing.Color.Black;
            this.Header.Location = new System.Drawing.Point(183, 35);
            this.Header.Size = new System.Drawing.Size(841, 26);
            this.Header.Style = Sunny.UI.UIStyle.Custom;
            this.Header.StyleCustomMode = true;
            // 
            // Aside
            // 
            this.Aside.MenuStyle = Sunny.UI.UIMenuStyle.Custom;
            this.Aside.Size = new System.Drawing.Size(183, 685);
            this.Aside.Style = Sunny.UI.UIStyle.Custom;
            this.Aside.MenuItemClick += new Sunny.UI.UINavMenu.OnMenuItemClick(this.Aside_MenuItemClick);
            // 
            // FrmreceiptMain
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.ClientSize = new System.Drawing.Size(1024, 720);
            this.CloseAskString = "确定要退出吗？";
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "FrmreceiptMain";
            this.Style = Sunny.UI.UIStyle.Custom;
            this.Text = "代代米日淘系统";
            this.ZoomScaleRect = new System.Drawing.Rectangle(15, 15, 1024, 720);
            this.ResumeLayout(false);

        }

        #endregion
    }
}