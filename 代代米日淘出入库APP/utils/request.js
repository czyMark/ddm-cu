
export default function requset(method, url, data, header){
  return new Promise((resolve, reject)=>{
    wx.request({
			method,
			url: url,
			data,
			header: {
				'content-type': 'application/json',
				'sessionid': wx.getStorageSync('userInfo')?.session?.sessionId || '',
				// 'Authori-cacheKey': wx.getStorageSync('cacheKey') || '',
				// 'Authori-logintype': 'xcx',
				...header
			},
			success(res) {
				const {code, msg} = res.data
				if(code === 0){
					resolve(res)
				}else{
					if(url === '/account/payBalance' || url === '/good/selectGoodDetail' || url === '/Mercarigood/selectdetial'){
						resolve(res)
					}else{
						wx.showToast({
							title: msg,
							icon: 'none'
						})
					}
				}
			},
			fail(err){
				wx.showToast({
					title: err.msg,
					icon: 'none'
				})
				reject(err)
			}
		})
	})
}


