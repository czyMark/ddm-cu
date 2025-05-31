<template>
	<view class="page">
		<Header title="个人中心" bgColor="rgb(6, 138 ,246)" :showBack="false"/>
		<!-- <image class="bg" src="@/static/img/mine_bg.png"/> -->
		<view class="userInfo">
			<view class="infoHeader flex">
				<view class="info" v-if="isLogin">
					<image class="avatar" v-if="userProfile.avatarUrl" :src="userProfile.avatarUrl" @click="subscribe"></image>
					<image class="avatar" v-if="!userProfile.avatarUrl" src="@/static/defaultAvatar.png" @click="subscribe"></image>
					<view>
						<view class="name">{{userProfile.nickName || '微信用户'}}</view>
						<view class="userId">用户id: {{userProfile.userid}}</view>
					</view>
					<view class="setting" @click="toPath('/pages/mine/setting')">
						<uni-icons type="gear-filled" size="26"></uni-icons>
					</view>
				</view>
				<!-- <button v-if="!isLogin" class="login" open-type="getUserInfo" @getuserinfo="bindGetUserInfo"> 授权登录 </button> -->
				<button v-if="!isLogin" class="login" @click="bindGetUserInfo"> 授权登录 </button>
			</view>
			<!-- <view class="tabs flex">
				<view class="tabItem flex" @click="toPath('/pages/mercari/cart')">
					<uni-icons type="cart" size="30"></uni-icons>
					<view class="tabItemName">购物车</view>
				</view>
				<view class="tabItem flex" @click="toPath('/pages/mine/collect')">
					<uni-icons type="star" size="30"></uni-icons>
					<view class="tabItemName">我的收藏</view>
				</view>
				<view class="tabItem flex" @click="toPath('/pages/mine/viewlog')">
					<image src="@/static/viewlog.png" style="width: 60rpx; height: 60rpx"></image>
					<view class="tabItemName">我的足迹</view>
				</view>
				<view class="tabItem flex" @click="toPath('/pages/mine/orderList')">
					<uni-icons type="bars" size="30"></uni-icons>
					<view class="tabItemName">我的订单</view>
				</view>
				<view class="tabItem flex" @click="toPath('/pages/mine/address')">
					<uni-icons type="navigate" size="30"></uni-icons>
					<view class="tabItemName">我的地址</view>
				</view>
			</view> -->
		</view>
		
		<view class="orderMenu">
			<view class="subtTitle">雅虎订单</view>
			<view class="menu">
				<view class="item column" @click="toYahooOrderList(item.id)"
					v-for="(item, index) in yahooMenu" :key="index"
				>
					<image :src="item.icon" mode=""></image>
					<view>{{item.text}}</view>
				</view>
				<i></i>
				<i></i>
				<i></i>
			</view>
		</view>
		<view class="orderMenu">
			<view class="subtTitle">煤炉订单</view>
			<view class="menu">
				<view class="item column" @click="toMercariOrderList(item.id)"
					v-for="(item, index) in mercariMenu" :key="index"
				>
					<image :src="item.icon" mode=""></image>
					<view>{{item.text}}</view>
				</view>
				<i></i>
				<i></i>
				<i></i>
			</view>
		</view>
		
		<view class="menu">
			<view v-for="(item, index) in menu" :key="index" class="menuItem flex" @click="toPath(item.url)">
				<view class="left flex">
					<uni-icons v-if="item.icon" :type="item.icon" size="20"></uni-icons>
					<image v-else src="@/static/viewlog.png" style="width: 40rpx; height: 40rpx"></image>
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
				@confirm="readContact" @close="readContact"></uni-popup-dialog>
		</uni-popup>
		
	</view>
</template>
<script>
	import Header from "@/components/header2.vue"
	export default {
		components: {Header},
		data() {
			return {
				hasTel: false,
				isOuted: false,
				userProfile: {},
				isLogin: false,
				menu: [
					{name: '购物车', url: '/pages/mercari/cart', icon: 'cart'},
					{name: '我的收藏', url: '/pages/mine/collect', icon: 'star'},
					{name: '我的足迹', url: '/pages/mine/viewlog', icon: ''},
					{name: '我的地址', url: '/pages/mine/address', icon: 'navigate'},
					{name: '我的钱包', url: '/pages/mine/account', icon: 'wallet'},
				],
				yahooMenu: [
					{id: 0, text: '全部', icon: require("@/static/order_icon/all.png")},
					{id: 1, text: '出价中', icon: require("@/static/order_icon/jingpaizhong.png")},
					{id: 2, text: '成功订单', icon: require("@/static/order_icon/chenggong.png")},
					{id: 10, text: '被超越订单', icon: require("@/static/order_icon/shibai.png")},
					{id: 4, text: '待入库', icon: require("@/static/order_icon/dairuku.png")},
					{id: 5, text: '已入库', icon: require("@/static/order_icon/yiruku.png")},
					{id: 6, text: '待计算', icon: require("@/static/order_icon/daisuanweikuan.png")},
					{id: 7, text: '待付尾款', icon: require("@/static/order_icon/daifukuan.png")},
					{id: 8, text: '待发货', icon: require("@/static/order_icon/daifahuo.png")},
					{id: 9, text: '已发货', icon: require("@/static/order_icon/yifahuo.png")},
				],
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
				this.toPath(`/pages/mine/orderList?platformType=1&statusType=${id}`)
			},
			toMercariOrderList(id){
				this.toPath(`/pages/mine/orderList?platformType=2&statusType=${id}`)
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
									  this.toPath('/pages/mine/profile')
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
			login(){
				wx.login({
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
					  wx.hideLoading()
					} else {
					  console.log('登录失败！' + res.errMsg)
					}
				  }
				})
			},
			logout(){
				wx.setStorage({ key: 'login' , data: false })
				wx.setStorage({ key: 'userInfo' , data: {} })
				this.isLogin = false
			},
			toPath(url){
				if(!url){
					this.$refs.alertDialog.open()
					// this.logout()
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
	.bg{
		width: 750rpx;
		height: 320rpx;
		position: absolute;
		top: 0;
		left: 0;
		z-index: -1;
	}
	.flex{
		display: flex;
		align-items: center;
	}
	.userInfo{
		margin: 30rpx auto;
		width: 690rpx;
		background: #FFFFFF;
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
		width: 690rpx;
		box-sizing: border-box;
		padding: 0 20rpx 30rpx;
		justify-content: space-between;
		.tabItem{
			justify-content: center;
			flex-direction: column;
			position: relative;
			image{
				width: 48rpx;
				height: 48rpx;
			}
			.tabItemName{
				margin-top: 8rpx;
				font-size: 24rpx;
				font-family: PingFangSC-Regular, PingFang SC;
				font-weight: 400;
				color: #666666;
			}
			.mark{
				width: 35rpx;
				height: 24rpx;
				line-height: 24rpx;
				text-align: center;
				background: #EF3F3F;
				border-radius: 12rpx;
				position: absolute;
				top: -14rpx;
				right: 0;
				font-size: 20rpx;
				font-family: PingFangSC-Regular, PingFang SC;
				font-weight: 400;
				color: #FFFFFF;
			}
		}
		.tabItem:nth-child(2) image{
			width: 40rpx;
			height: 42rpx;
		}
		.tabItem:nth-child(3) image{
			width: 56rpx;
			height: 46rpx;
		}
	}
	.menu{
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
	.orderMenu{
		padding: 0 50rpx;
		.subtTitle{
			font-weight: 600;
			font-size: 28rpx;
		}
		.menu{
			margin-top: 30rpx;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			.item{
				margin-bottom: 30rpx;
				font-size: 24rpx;
				width: 140rpx;
				image{
					width: 60rpx;
					height: 60rpx;
					margin-bottom: 10rpx;
				}
			}
			i{
				width: 140rpx;
				height: 0;
			}
		}
	}
</style>
