<template>
	<view class="comp">
		<uni-popup ref="popup" background-color="none"
			:mask-click="false"
			:is-mask-click="false"
			borderRadius="40rpx"
		>
			<view class="popup-content column">
				<image
					class="bg"
					src="@/static/icon/68.png"
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
					<input placeholder="请输入手机号" v-model="tel" type="number">
				
					<view class="sendCode" v-if="!isSend" @click="sendCode">发送验证码</view>
					<view class="sendCode" v-else>{{second}} s</view>
				</view>
				<view class="formItem flex">
					<image
						class="icon"
						src="@/static/icon/69.png"
					>
					<input placeholder="请输入验证码" v-model="psd" type="number">
				</view>
				
				<view class="formItem flex" v-if="showInvitedCode">
					<i class="icon"></i>
					<input placeholder="请输入邀请码" v-model="inviteCode" type="text">
				</view>
				
				<view class="tips between">
					<image
						@click="agree = !agree"
						class="icon"
						src="@/static/icon/45.png"
						v-if="agree"
					>
					<image
						@click="agree = !agree"
						class="icon"
						src="@/static/icon/66.png"
						v-else
					>
					<view>
						查看并同意
						<text @click="toPath(`/pages/mine/userAgreement`)">《用户协议》</text>
						和
						<text @click="toPath(`/pages/mine/privacyPolicy`)">《隐私政策》</text>
						未注册的手机号验证通过后将自动注册
					</view>
				</view>
				
				<image
					class="loginBtn"
					src="@/static/icon/70.png"
					@click="wxlogin"
				>
				
				<view class="loginTip">第三方登录</view>
				
				<image
					class="wx"
					src="@/static/icon/71.png"
					@click="wxlogin(true)"
				>
				
				<image
					@click="close"
					class="close"
					src="@/static/icon/64.png"
				>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isSend: false,
				second: 60,
				timer: null,
				
				agree: false,
				
				tel: 18614286775,
				psd: '',
				inviteCode: '',
			};
		},
		props: {
			showLogin: {
				type: Boolean,
				default: false
			},
			showInvitedCode: {
				type: Boolean,
				default: true
			},
		},
		watch: {
			showLogin: {
				immediate: false,
				handler(val){
					console.log('val', val)
					if(val){
						this.$refs.popup.open()
					}else{
						this.$refs.popup.close()
					}
				}
			}
		},
		onReady(){
			// setTimeout(()=>{
				
			// }, 300)
		},
		onUnload() {
			clearInterval(this.timer)
		},
		methods: {
			// async login(){
			// 	uni.showLoading()
			// 		const params = {
			// 			phone: this.tel,
			// 			code: this.psd,
			// 		}
			// 		const res = await this.$api.phonecheckcode(params)
			// 		uni.hideLoading()
			// 		uni.showToast({
			// 			title: '设置成功',
			// 			icon: 'none'
			// 		})
			// },
			wxlogin(val){
				// 授权获取用户信息
				wx.showLoading({
					title: '授权登录中'
				})
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
									
									loginphone: val ? '' : this.tel,
									phonecode: val ? '' : this.psd,
									invitecode: val ? '' : this.inviteCode,
									code: res.code,
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
								  this.$emit('loginSuccess')
								  wx.hideLoading()
								} else {
								  console.log('登录失败！' + res.errMsg)
								}
							  }
							})
						}
					}
				})
			},
			async sendCode(){
				const reg = /^1[3|4|5|6|7|8]\d{9}$/
				const result = reg.test(this.tel)
				if(result){
					uni.showLoading()
					const params = {
						phone: this.tel,
					}
					const res = await this.$api.phonegetcode(params)
					uni.hideLoading()
					uni.showToast({
						title: '发送成功',
						icon: 'none'
					})
					this.isSend = true
					this.second = 59
					this.timer = setInterval(()=>{
						this.second = this.second - 1
						if(this.second === 0){
							clearInterval(this.timer)
							this.second = 60
							this.isSend = false
						}
					}, 1000)
				}else{
					uni.showToast({
						title: '请输入正确手机号',
						icon: 'none'
					})
				}
			},
			close(){
				console.log('close')
				this.$emit('close')
			},
			toPath(url){
				uni.navigateTo({
					url
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
.popup-content{
	padding-top: 42rpx;
	box-sizing: border-box;
	width: 670rpx;
	height: 1000rpx;
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
		width: 300rpx;
		height: 100rpx;
		margin-bottom: 50rpx;
	}
	.formItem{
		margin-bottom: 20rpx;
		width: 450rpx;
		height: 88rpx;
		align-items: center;
		border: 1px solid #dadada;
		border-radius: 20rpx;
		position: relative;
		z-index: 2;
		font-size: 24rpx;
		position: relative;
		.icon{
			flex-shrink: 0;
			width: 36rpx;
			height: 36rpx;
			margin: 0 30rpx;
			margin-right: 20rpx;
		}
		.sendCode{
			position: absolute;
			right: 20rpx;
			top: 0;
			bottom: 0;
			margin: auto;
			font-size: 20rpx;
			color: #1271FF;
			line-height: 88rpx;
		}
	}
	.tips{
		position: relative;
		align-items: flex-start;
		z-index: 2;
		font-size: 22rpx;
		color: #3D3D3D;
		margin: 60rpx 0 48rpx;
		width: 500rpx;
		.icon{
			width: 24rpx;
			height: 24rpx;
			position: relative;
			top: 4rpx;
		}
		view{
			width: 466rpx;
		}
		text{
			color: #1271FF;
		}
	}
	.loginBtn{
		width: 578rpx;
		height: 88rpx;
	}
	.loginTip{
		position: relative;
		z-index: 2;
		margin: 25rpx 0;
		font-size: 25rpx;
		color: #3D3D3D;
	}
	.wx{
		width: 110rpx;
		height: 90rpx;
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
