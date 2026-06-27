using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinReceipt.Tool
{
    public  static class AppConfigHelper
    {
        // 读取 StartDebug 配置值
        public static bool IsDebugMode
        {
            get
            {
                string value = ConfigurationManager.AppSettings["StartDebug"];
                if (!string.IsNullOrEmpty(value))
                {
                    // 尝试解析为布尔值
                    if (bool.TryParse(value, out bool result))
                    {
                        return result;
                    }

                    // 尝试解析为整数（1=true，0=false）
                    if (int.TryParse(value, out int intValue))
                    {
                        return intValue == 1;
                    }
                }

                // 默认值（当配置不存在或解析失败时）
                return false;
            }
        }

        // 获取其他配置项（通用方法）
        public static string GetAppSetting(string key, string defaultValue = "")
        {
            string value = ConfigurationManager.AppSettings[key];
            return string.IsNullOrWhiteSpace(value) ? defaultValue : value;
        }
    }
}
