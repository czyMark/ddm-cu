namespace WinReceipt
{
    partial class LoginUI
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(LoginUI));
            this.SuspendLayout();
            // 
            // lblTitle
            // 
            this.lblTitle.Font = new System.Drawing.Font("宋体", 15.75F, System.Drawing.FontStyle.Bold);
            this.lblTitle.Location = new System.Drawing.Point(3, 4);
            this.lblTitle.Margin = new System.Windows.Forms.Padding(20);
            this.lblTitle.Size = new System.Drawing.Size(616, 63);
            this.lblTitle.Text = "代代米日淘";
            // 
            // uiPanel1
            // 
            this.uiPanel1.Location = new System.Drawing.Point(429, 127);
            // 
            // lblSubText
            // 
            this.lblSubText.Location = new System.Drawing.Point(437, 421);
            this.lblSubText.Text = "代代米日淘  V1.0";
            // 
            // LoginUI
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.BackgroundImage = global::WinReceipt.Properties.Resources.Login4;
            this.ClientSize = new System.Drawing.Size(750, 450);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "LoginUI";
            this.SubText = "代代米日淘  V1.0";
            this.Text = "LoginUI";
            this.Title = "代代米日淘";
            this.ButtonLoginClick += new System.EventHandler(this.LoginUI_ButtonLoginClick);
            this.ResumeLayout(false);

        }

        #endregion
    }
}