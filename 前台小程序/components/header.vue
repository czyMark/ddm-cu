<template>
	<view class="header between">
		<view class="logo center">
			<image src="./../static/logo.png" mode="" v-if="logo === '日代拍'"></image>
			<image src="./../static/mm.png" mode="" v-if="logo === 'mercari'"></image>
			<image src="./../static/yahoo-fill.png" mode="" v-if="logo === 'YaHoo'"></image>
		</view>
		<view class="uniInput" @click="onFocus">
			<uni-easyinput prefixIcon="search" v-model="value"
				placeholder="点我开始入札吧" @iconClick="iconClick">
			</uni-easyinput>
		</view>
		<view class="right flex">
			<view class="exchangeRate column">
				<view class="title">平台汇率</view>
				<view class="value">{{rate}}</view>
			</view>
			<view class="help column">
				<uni-icons type="help" size="20"></uni-icons>
				<view class="title" @click="toPath('/pages/mine/questions')">帮助</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			logo: {
				type: String,
				default:　"日代拍"
			},
			page: {
				type: String,
				default:　"home"
			},
			bigClass: {
				type: [String, Number],
				default:　0
			},
		},
		data() {
			return {
				rate: '查询中',
				rateTimer: null,
			}
		},
		onReady() {
			this.rateTimer = setInterval(async ()=>{
				console.log('rateTimer')
				this.rate = wx.getStorageSync('rate') || ''
				if(this.rate && this.rate !== '查询中'){
					clearInterval(this.rateTimer)
				}else if(this.rate === '查询失败'){
					wx.setStorage({ key: 'rate' , data: '查询中' })
					// 查询中日汇率
					const res = await this.$api.getRate()
					const { code, msg } = res.data
					if(code === 0){
						console.log('msg', msg)
						wx.setStorage({ key: 'rate' , data: msg.replace('今日汇率：', '') })
					}else{
						wx.setStorage({ key: 'rate' , data: '查询失败' })
						wx.showToast({title: msg})
					}
				}
			}, 1000)
		},
		methods: {
			iconClick(type) {
				uni.showToast({
					title: `点击了${type==='prefix'?'左侧':'右侧'}的图标`,
					icon: 'none'
				})
			},
			onFocus(){
				if(this.page === 'home'){
					wx.switchTab({
						url: '/pages/mercari/index'
					})
				}else if(this.page === 'YaHoo'){
					if(this.bigClass === 'jiu') return false
					this.toPath('/pages/yahoo/yahooResult?bigClass=' + this.bigClass)
				}else if(this.page === 'mercari'){
					this.toPath('/pages/mercari/mercariResult?bigClass=' + this.bigClass)
				}
			},
			toPath(url){
				wx.navigateTo({
					url,
				})
			}
		}
	}
</script>

<style lang="scss">
	.header{
		height: 85rpx;
		padding-right: 10rpx;
	}
	.logo{
		width: 160rpx;
		height: 70rpx;
		// background: gold;
		image{
			width: 70rpx;
			height: 70rpx;
			border-radius: 50%;
		}
		/* font-size: 40rpx;
		font-weight: 600;
		width: 160rpx;
		text-align: center; */
	}
	.uniInput{
		width: 380rpx;
	}
	.right{
		font-size: 28rpx;
	}
	.exchangeRate{
		font-size: 28rpx;
		margin-right: 10rpx;
	}
</style>
