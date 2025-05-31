<template>
	<view class="page">
		<view class="menu">
			<view v-for="(item, index) in menu" :key="index" class="menuItem flex" @click="toPath(item.url)">
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
				@confirm="readContact" @close="readContact"></uni-popup-dialog>
		</uni-popup>
		
	</view>
</template>
<script>
	export default {
		data() {
			return {
				hasTel: false,
				isOuted: false,
				userProfile: {},
				isLogin: false,
				menu: [
					{name: '资料修改', url: '/pages/mine/profile'},
					{name: '联系客服', url: '/pages/mine/contact'},
					{name: '常见问题', url: '/pages/mine/questions'},
					{name: '隐私政策', url: '/pages/mine/privacyPolicy'},
					{name: '收费标准', url: '/pages/mine/rules'},
					{name: '用户协议', url: '/pages/mine/userAgreement'},
					{name: '退出登录', url: ''},
				],
				contact: '',
			}
		},
		methods: {
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
			logout(){
				wx.setStorage({ key: 'login' , data: false })
				wx.setStorage({ key: 'userInfo' , data: {} })
				this.isLogin = false
				wx.navigateBack(-1)
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
		width: 690rpx;
		box-sizing: border-box;
		padding: 0 30rpx;
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
				width: 150rpx;
				image{
					width: 60rpx;
					height: 60rpx;
					margin-bottom: 10rpx;
				}
			}
			i{
				width: 150rpx;
				height: 0;
			}
		}
	}
</style>
