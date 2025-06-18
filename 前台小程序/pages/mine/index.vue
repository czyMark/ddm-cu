<template>
	<view class="page">
		<view class="top">
			<Title :showBack="false" :showBg="false"/>
			<view class="userInfo">
				<view class="infoHeader flex">
					<view class="info" v-if="isLogin">
						<image class="avatar" src="@/static/icon/defaultAvatar.png"></image>
						<!-- 
						<image class="avatar" v-if="userProfile.avatarUrl" :src="userProfile.avatarUrl" @click="subscribe"></image>
						<image class="avatar" v-if="!userProfile.avatarUrl" src="@/static/defaultAvatar.png" @click="subscribe"></image>
						 -->
						<view>
							<view class="name">{{userProfile.nickName || '微信用户'}}</view>
							<view class="userId">用户id: ddmrt2506{{userProfile.userid}}</view>
						</view>
						<!-- <view class="setting" @click="toPath('/pages/mine/setting')">
							<uni-icons type="gear-filled" size="26"></uni-icons>
						</view> -->
					</view>
					
					<view class="info" v-else @click="showLogin = true">
						<image class="avatar" src="@/static/icon/defaultAvatar.png"></image>
						<view>
							<view class="label">登录/注册</view>
						</view>
					</view>
					
					<!-- <button v-if="!isLogin" class="login" @click="bindGetUserInfo">
						 授权登录 
					</button> -->
				</view>
			</view>
			
			<view class="tabs between">
				<view class="item column" @click="toTabbar(`/pages/mine/collect`)">
					<view class="value">0</view>
					<view class="label">商品收藏</view>
				</view>
				<image
					class="line"
					src="@/static/icon/25.png"
				>
				<view class="item column" @click="toPath(`/pages/mine/viewlog`)">
					<view class="value">0</view>
					<view class="label">足迹</view>
				</view>
				<image
					class="line"
					src="@/static/icon/25.png"
				>
				<view class="item column" @click="toPath(`/pages/mine/account`)">
					<view class="value">10000<text>(JPY)</text></view>
					<view class="label">账户余额</view>
				</view>
				<image
					class="line"
					src="@/static/icon/25.png"
				>
				<view class="item column" @click="toPath(`/pages/mine/account`)">
					<view class="value">50000<text>(JPY)</text></view>
					<view class="label">保证金</view>
				</view>
			</view>
		</view>
		
		
		<view class="orderMenu">
			<image
				class="subtTitle"
				src="@/static/icon/35.png"
			>
			<view class="menu">
				<view class="item column" @click="toYahooOrderList(item.id)"
					v-for="(item, index) in auctionMenu" :key="index"
				>
					<image :src="item.icon" mode=""></image>
					<!-- <view>{{item.text}}</view> -->
				</view>
				<i></i>
			</view>
			
			<image
				class="subtTitle"
				src="@/static/icon/36.png"
			>
			<view class="menu">
				<view class="item column" @click="toYahooOrderList2(item.id)"
					v-for="(item, index) in orderMenu" :key="index"
				>
					<image :src="item.icon" mode=""></image>
					<!-- <view>{{item.text}}</view> -->
				</view>
			</view>
		</view>
		
		<view class="bottomMenu">
			<view v-for="(item, index) in menu" :key="index" class="menuItem flex" @click="toPath(item.url, item)">
				<view class="left flex">
					<text>{{item.name}}</text>
				</view>
				<image class='arrow' src="@/static/img/right_arrow_gray.png"/>
			</view>
		</view>
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog type="info" cancelText="取消" confirmText="确认" title="提示"
				content="确认退出登陆吗？"
				@confirm="logout" @close="dialogClose"></uni-popup-dialog>
		</uni-popup>
		
		
		<uni-popup ref="contactDialog" type="dialog">
			<uni-popup-dialog type="info" cancelText="取消" confirmText="确认" title="您有新消息"
				:content="contact"
				@confirm="readContact" @close="readContact">
			</uni-popup-dialog>
		</uni-popup>
		
		<uni-popup ref="popup" background-color="none"
			borderRadius="40rpx"
		>
			<view class="popup-content-invite column">
				<image
					class="bg"
					src="@/static/icon/62.png"
				>
				<image
					class="logo"
					src="@/static/icon/1.png"
				>
				<view class="formItem flex">
					<image
						class="icon"
						src="@/static/icon/63.png"
					>
					<input placeholder="请输入邀请码" disabled v-model="inviteCode" type="text">
					<image
						class="copy"
						src="@/static/icon/copy.png"
						@click="copyCode(inviteCode)"
					>
				</view>
				
				<image
					@click="closeInvitePop"
					class="close"
					src="@/static/icon/64.png"
				>
			</view>
		</uni-popup>
		
		<Login :showLogin="showLogin" @close="closePop" @loginSuccess="loginSuccess" />
	</view>
</template>
<script>
	import Title from "@/components/title.vue"
	import Login from "@/components/login.vue"
	
	export default {
		components: {Title, Login},
		data() {
			return {
				inviteCode: 'BKC19XRP',
				hasTel: false,
				isOuted: false,
				userProfile: {},
				isLogin: false,
				showLogin: false,
				menu: [
					{name: '煤炉购物车', url: '/pages/mercari/cart'},
					{name: '收货地址', url: '/pages/mine/address'},
					// {name: '收费标准', url: ''},
					// {name: '用户协议', url: ''},
					{name: '收费标准', url: '/pages/mine/rules'},
					{name: '用户协议', url: '/pages/mine/userAgreement'},
					{name: '我的邀请码', url: ''},
					// {name: '常用身份证', url: ''},
					{name: '联系客服', url: '/pages/mine/contact'},
					
				],
				auctionMenu: [
					// {id: 0, text: '全部', icon: require("@/static/order_icon/all.png")},
					{id: 100, text: '预约出价', icon: require("@/static/icon/26.png")},
					{id: 1, text: '竞拍中', icon: require("@/static/icon/27.png")},
					{id: 2, text: '已得标', icon: require("@/static/icon/28.png")},
					{id: 10, text: '竞拍失败', icon: require("@/static/icon/29.png")},
				],
				
				
				orderMenu: [
					{id: 2, text: '待支付', icon: require("@/static/icon/30.png")},
					{id: 4, text: '已支付', icon: require("@/static/icon/31.png")},
					{id: 5, text: '已入库', icon: require("@/static/icon/32.png")},
					{id: 7, text: '国际运费', icon: require("@/static/icon/33.png")},
					{id: 9, text: '已出库', icon: require("@/static/icon/34.png")},
				],
				
				
				// yahooMenu: [
				// 	{id: 0, text: '全部', icon: require("@/static/order_icon/all.png")},
				// 	{id: 1, text: '出价中', icon: require("@/static/order_icon/jingpaizhong.png")},
				// 	{id: 2, text: '成功订单', icon: require("@/static/order_icon/chenggong.png")},
				// 	{id: 10, text: '被超越订单', icon: require("@/static/order_icon/shibai.png")},
				// 	{id: 4, text: '待入库', icon: require("@/static/order_icon/dairuku.png")},
				// 	{id: 5, text: '已入库', icon: require("@/static/order_icon/yiruku.png")},
				// 	{id: 6, text: '待计算', icon: require("@/static/order_icon/daisuanweikuan.png")},
				// 	{id: 7, text: '待付尾款', icon: require("@/static/order_icon/daifukuan.png")},
				// 	{id: 8, text: '待发货', icon: require("@/static/order_icon/daifahuo.png")},
				// 	{id: 9, text: '已发货', icon: require("@/static/order_icon/yifahuo.png")},
				// ],
				mercariMenu: [
					{id: 0, text: '全部', icon: require("@/static/order_icon/all.png")},
					{id: 1, text: '待入库', icon: require("@/static/order_icon/dairuku.png")},
					{id: 2, text: '已入库', icon: require("@/static/order_icon/yiruku.png")},
					{id: 3, text: '待计算', icon: require("@/static/order_icon/daisuanweikuan.png")},
					{id: 4, text: '待付尾款', icon: require("@/static/order_icon/daifukuan.png")},
					{id: 5, text: '待发货', icon: require("@/static/order_icon/daifahuo.png")},
					{id: 6, text: '已发货', icon: require("@/static/order_icon/yifahuo.png")},
					{id: 7, text: '已退款', icon: require("@/static/order_icon/yituikuan.png")},
				],
				contact: '',
			}
		},
		onShow(){
			this.isLogin = wx.getStorageSync('login') || false
			this.userProfile = wx.getStorageSync('userInfo')?.userinfo || {}
			
			if (wx.getUserProfile) {
				console.log('getUserProfile')
			}
			if(this.isLogin) this.login()
			return false
			const {avatarUrl, nickName, phone, realname} = this.userProfile
			if(this.isLogin && (!avatarUrl || !nickName || !phone || !realname)){
				wx.showToast({
					title: '请完善用户资料！',
					icon: "none"
				})
				wx.navigateTo({
					url: '/pages/mine/profile'
				})
			}
		},
		methods: {
			toYahooOrderList(id){
				console.log(id)
				this.toPath(`/pages/mine/auctionList?platformType=1&statusType=${id}`)
			},
			toYahooOrderList2(id){
				this.toPath(`/pages/mine/allOrderList?platformType=1&statusType=${id}`)
			},
			toMercariOrderList(id){
				this.toPath(`/pages/mine/allOrderList?platformType=2&statusType=${id}`)
			},
			async readContact(){
				const res = await this.$api.userRead({userid: this.userProfile.userid})
				this.$refs.contactDialog.close()
			},
			subscribe(){
				if(this.userProfile.isAdmin === 0) return false
				wx.requestSubscribeMessage({
					tmplIds: ['SIAnL8-hwuUoxe92om2SLXdomZaqo1PacP36QrwXEI0'],
					success (res) {
						wx.showToast({
							title: '订阅成功'
						})
					}
				})
			},
			dialogClose(){
				this.$refs.alertDialog.close()
			},
			closeInvitePop(){
				this.$refs.popup.close()
			},
			// 授权获取用户信息
			bindGetUserInfo(){
				wx.showLoading({
					title: '授权登录中'
				})
				console.log('授权获取用户信息')
				wx.getUserProfile({
					desc: '用于完善会员资料',
					success: (res) => {
						console.log(res)
						const {encryptedData, iv, userInfo} = res
						if(encryptedData){
							wx.login({
							  success: async (res)=>{
							    if (res.code) {
								  const params = {
								  	code: res.code,
									encryptedData,
									iv
								  }
								  const loginRes = await this.$api.login(params)
								  const { data } = loginRes.data
								  console.log('datra', data)
								  data.userinfo.nickName = data.userinfo.nickName || userInfo.nickName
								  data.userinfo.avatarUrl = data.userinfo.avatarUrl || userInfo.avatarUrl
								  this.userProfile = {
									  ...data.userinfo,
								  }
								  wx.setStorage({ key: 'userInfo' , data })
								  wx.setStorage({ key: 'login', data: 'true' })
								  this.isLogin = true
								  wx.hideLoading()
								  if(data.userinfo.nickName === '微信用户'){
									  // this.toPath('/pages/mine/profile')
									  return false
								  }
								  if(data.userinfo.isRead === 0){
									  this.contact = data.userinfo.contact
									  this.$refs.contactDialog.open()
								  }
							    } else {
							      console.log('登录失败！' + res.errMsg)
							    }
							  }
							})
						}
					}
				})
			},
			async login(){
				await wx.login({
				  success: async (res)=>{
					if (res.code) {
					  const params = {
						code: res.code,
					  }
					  const loginRes = await this.$api.login(params)
					  const { data } = loginRes.data
					  this.userProfile = {
						  ...data.userinfo,
					  }
					  wx.setStorage({ key: 'userInfo' , data })
					  wx.setStorage({ key: 'login', data: 'true' })
					  this.isLogin = true
					  
					  if(data.userinfo.isRead === 0){
						  this.contact = data.userinfo.contact
						this.$refs.contactDialog.open()
					  }
					  
					  this.isLogin = true
					  this.showLogin = false
					  wx.hideLoading()
					} else {
					  console.log('登录失败！' + res.errMsg)
					}
				  }
				})
			},
			
			closePop(){
				this.showLogin = false
			},
			async loginSuccess(){
				await this.login()
			},
			logout(){
				wx.setStorage({ key: 'login' , data: false })
				wx.setStorage({ key: 'userInfo' , data: {} })
				this.isLogin = false
			},
			toPath(url, item){
				if(item?.name === '我的邀请码'){
					this.$refs.popup.open()
					// this.logout()
					return false
				}
				
				if(item?.name === '联系客服'){
					uni.switchTab({
						url,
					})
					return false
				}
				
				if(!wx.getStorageSync('login')){
					wx.showToast({
						icon: 'none',
						title: '暂未登录，请先登录小程序'
					})
					return false
				}
				wx.navigateTo({
					url,
				})
			},
			toTabbar(url){
				uni.switchTab({
					url,
				})
			},
			copyCode(val){
				wx.setClipboardData({
					data: val,
					success: ()=>{
						wx.showToast({
							title: '复制成功'
						})
					}
				})
			},
			verifyLogin(method, val){
				if(wx.getStorageSync('login')){
					this[method](val)
				}else{
					wx.showToast({
						icon: 'none',
						title: '暂未登录，请先登录小程序'
					})
				}
			}
		}
	}
</script>
<style lang="scss" scoped>
	.top{
		background: linear-gradient( 138deg, #FFFFFF 3%, #EFF8FD 32%, #F1EAFC 58%, #ECEAFC 63%, #DEEAFC 83%, #DAEBFD 100%);
		border-radius: 0rpx 0rpx 0rpx 0rpx;
		padding-bottom: 20rpx;
	}
	.flex{
		display: flex;
		align-items: center;
	}
	.userInfo{
		margin: 30rpx auto;
		width: 690rpx;
		border-radius: 12rpx 12rpx 0px 0px;
	}
	.infoHeader{
		padding: 30rpx 0;
		position: relative;
		.info{
			height: 92rpx;
			box-sizing: border-box;
			margin-left: 30rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			.setting{
				position: absolute;
				right: 50rpx;
				top: 46rpx;
			}
			image{
				width: 92rpx;
				height: 92rpx;
				border-radius: 50%;
			}
			.label{
				margin-left: 30rpx;
				font-size: 40rpx;
				font-family: PingFangSC-Medium, PingFang SC;
				font-weight: 600;
				color: #333333;
			}
			.name{
				margin-left: 30rpx;
				font-size: 36rpx;
				font-family: PingFangSC-Medium, PingFang SC;
				font-weight: 600;
				color: #333333;
			}
			.userId{
				margin-left: 30rpx;
				font-size: 24rpx;
				color: #999999;
				margin-top: 8rpx;
			}
			.options{
				image{
					width: 26rpx;
					height: 26rpx;
				}
				text{
					margin-left: 10rpx;
					font-size: 26rpx;
					font-family: PingFangSC-Regular, PingFang SC;
					font-weight: 400;
					color: #999999;
				}
			}
		}
		.login{
			background: none;
			padding: 0;
			margin-left: 30rpx;
			font-size: 36rpx;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 600;
			color: #333333;
			display: flex;
			align-items: center;
			height: 92rpx;
		}
		.login::after{
			border: none;
		}
	}
	
	.tabs{
		width: 708rpx;
		margin: 0 auto;
		background-color: #F5F5FF;
		box-sizing: border-box;
		padding: 10rpx 30rpx;
		justify-content: space-between;
		.line{
			margin: 0 20rpx;
			width: 2rpx;
			height: 30rpx;
		}
		.item{
			.value{
				font-size: 32rpx;
				text{
					font-size: 20rpx;
				}
			}
			.label{
				margin-top: 10rpx;
				font-size: 28rpx;
			}
		}
	}
	
	
	.orderMenu{
		width: 710rpx;
		box-sizing: border-box;
		margin: 30rpx auto;
		background: linear-gradient( 90deg, #B0EAFD 6%, #B0EAFD 100%);
		padding: 25rpx 10rpx;
		.subtTitle{
			width: 118rpx;
			height: 38rpx;
			margin-bottom: 20rpx;
		}
		.menu{
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			.item{
				margin-bottom: 30rpx;
				font-size: 24rpx;
				width: 120rpx;
				image{
					width: 100rpx;
					height: 98rpx;
				}
			}
			i{
				width: 120rpx;
				height: 0;
			}
		}
	}
	
	.bottomMenu{
		margin: 20rpx auto 0;
		width: 650rpx;
		box-sizing: border-box;
		background: #FFFFFF;
		border-radius: 12rpx;
		.menuItem{
			width: 100%;
			height: 110rpx;
			justify-content: space-between;
			border-bottom: 1px solid #EEEEEE;
			.left{
				image{
					width: 35rpx;
					height: 35rpx;
				}
				text{
					margin-left: 18rpx;
					font-size: 30rpx;
					font-family: PingFangSC-Regular, PingFang SC;
					font-weight: 400;
					color: #333333;
				}
			}
			.arrow{
				width: 17rpx;
				height: 24rpx;
			}
		}
		.menuItem:last-child{
			border-bottom: none
		}
	}
	
	.popup-content-invite{
		padding-top: 42rpx;
		box-sizing: border-box;
		width: 670rpx;
		height: 439rpx;
		position: relative;
		justify-content: flex-start;
		image{
			display: block;
			position: relative;
			z-index: 2;
		}
		.bg{
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			z-index: 1;
		}
		.logo{
			width: 200rpx;
			height: 100rpx;
			margin-bottom: 100rpx;
		}
		.formItem{
			width: 450rpx;
			height: 88rpx;
			align-items: center;
			border: 1px solid #dadada;
			border-radius: 20rpx;
			position: relative;
			z-index: 2;
			.icon{
				width: 36rpx;
				height: 36rpx;
				margin: 0 30rpx;
			}
			.copy{
				position: absolute;
				right: 30rpx;
				top: 24rpx;
				width: 40rpx;
				height: 40rpx;
			}
		}
		.close{
			position: absolute;
			width: 68rpx;
			height: 68rpx;
			left: 0;
			right: 0;
			bottom: -100rpx;
			margin: auto;
		}
	}
</style>
