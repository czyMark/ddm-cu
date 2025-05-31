<script>
import api from './utils/api'
	export default {
		onLaunch: async ()=>{
			wx.getSystemInfo({
		        success (res) {
				console.log(res)
					// 设置状态栏高度
					wx.setStorage({key: "statusBarHeight", data: res.statusBarHeight})
				}
		    })
			
			wx.setStorage({ key: 'rate' , data: '查询中' })
			// 查询中日汇率
			const rateRes = await api.getRate()
			const rateData = rateRes.data
			if(rateData.code === 0){
				wx.setStorage({ key: 'rate' , data: rateData.data })
			}else{
				wx.setStorage({ key: 'rate' , data: '查询失败' })
				wx.showToast({title: rateData.msg})
			}
			
			
			const settingRes = await api.querySetting()
			const settingData = settingRes.data
			console.log('settingData', settingData)
			if(settingData.code === 0){
				wx.setStorage({ key: 'setting' , data: settingData.data })
			}else{
				wx.setStorage({ key: 'setting' , data: {} })
				wx.showToast({title: settingData.msg})
			}
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
