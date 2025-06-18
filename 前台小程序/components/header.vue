<template>
	<view class="header between">
		<!-- <view class="uniInput" @click="onFocus">
			<uni-easyinput prefixIcon="search" v-model="value"
				placeholder="点我开始入札吧" @iconClick="iconClick">
			</uni-easyinput>
		</view> -->
		<view>
			<Search
				ref="search"
				@onFocus="onFocus"
				@onSearch="onSearch"
				@keywordChange="keywordChange"
				:keyword="keyword"
				height="70rpx"
				width="372rpx"
			/>
		</view>
		<image
			@click="onSearch"
			class="icon"
			src="@/static/icon/40.png"
		>
		<image
			@click="onSearch"
			class="icon"
			src="@/static/icon/41.png"
		>
	</view>
</template>

<script>
	import Search from "@/components/search.vue"
	export default {
		components: {Search},
		props: {
			logo: {
				type: String,
				default:　"日代拍"
			},
			page: {
				type: String,
				default:　""
			},
			bigClass: {
				type: [String, Number],
				default:　0
			},
		},
		data() {
			return {
				keyword: '',
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
			keywordChange(val){
				this.keyword = val
			},
			iconClick(type) {
				uni.showToast({
					title: `点击了${type==='prefix'?'左侧':'右侧'}的图标`,
					icon: 'none'
				})
			},
			onSearch(){
				console.log('onSearch', this.keyword)
				this.$emit('onSearch', this.keyword)
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
		padding: 10rpx 40rpx;
		.icon{
			width: 126rpx;
			height: 62rpx;
		}
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
