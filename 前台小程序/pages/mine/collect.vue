<template>
	<view class="collect">
		<view class="tabs around">
			<view :class="['tabItem', currentTab === item.id ? 'active' : '']" 
				v-for="(item, index) in tabList" :key="index" @click="choosetab(item.id)">
				{{item.title}}
			</view>
		</view>
		<view class="list" v-if="currentTab === 0">
			<view class="item flex" v-for="(item, index) in yahooList" :key="index"
				@click="chooseItem(item, 1)">
				<image :src="item.imageUrl"></image>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price">现价 <text>{{item.price}}</text>円</view>
					<view class="bottom between">
						<view class="times">次数: {{item.counts}}次</view>
						<view class="status">
							<uni-fav :checked="item.isCollect" class="favBtn"
								:circle="true" bg-color="#dd524d"
								bg-color-checked="#007aff" fg-color="#ffffff"
								fg-color-checked="#ffffff" @click="favClick(item)"
							/>
						</view>
					</view>
				</view>
			</view>
			<view class="tips" v-if="yahooList.length === 0">空空如也</view>
		</view>
		<view class="list" v-if="currentTab === 1">
			<view class="item flex"  v-for="(item, index) in mercariList" :key="index"
				@click="chooseItem(item, 2)">
				<image :src="item.imageUrl"></image>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="bottom between">
						<view class="price">现价 <text>{{item.price}}</text>円</view>
						<uni-fav :checked="item.isCollect" class="favBtn"
							:circle="true" bg-color="#dd524d"
							bg-color-checked="#007aff" fg-color="#ffffff"
							fg-color-checked="#ffffff" @click="favClick(item)"
						/>
					</view>
				</view>
			</view>
			<view class="tips" v-if="mercariList.length === 0">空空如也</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tabList: [
					{id: 0, title: "雅虎"},
					{id: 1, title: "煤炉"},
				],
				currentTab: 0,
				yahooList: [],
				mercariList: [],
				page: 1,
				size: 50,
				userInfo: {}
			}
		},
		onLoad() {
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.getCollect()
		},
		async onPullDownRefresh(){
			await this.getCollect()
			wx.stopPullDownRefresh()
		},
		methods: {
			async getCollect(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					platform: this.currentTab
				}
				const res = await this.$api.queryCollect(params)
				const { data } = res.data

				data.forEach(item=>{
					item.isCollect = true
				})
				
				if(this.currentTab === 0){
					this.yahooList = data
				}else{
					this.mercariList = data
				}
				
				wx.hideLoading()
			},
			choosetab(id){
				this.list = []
				this.page = 1
				this.size = 50
				this.currentTab = id
				if(id === 1){
					
				}else{
					
				}
				this.getCollect()
			},
			async favClick(item) {
				if(!item.isCollect) return false
				// item.isCollect = !item.isCollect
				wx.showLoading({title: '操作中'})
				const params = {
					goodcode: item.goodcode,
					userid: this.userInfo.userid,
				}
				const res = await this.$api.deleteCollect(params)
				wx.showToast({title: '操作成功', icon: 'none'})
				item.isCollect = false
				setTimeout(()=>{
					this.getCollect()
				}, 1000)
			},
			chooseItem(item, type){
				console.log(item)
				if(type === 1){
					const {goodcode} = item
					wx.navigateTo({
						url: `/pages/yahoo/detail?code=${item.goodcode}`,
					})
				}else{
					wx.navigateTo({
						url: '/pages/mercari/detail?id=' + item.goodcode
					})
				}
			},
		}
	}
</script>

<style lang="scss">
	.collect{
		.tabs{
			width: 100vw;
			.tabItem{
				width: 50%;
				text-align: center;
				height: 70rpx;
				line-height: 70rpx;
				border: 1px solid #e5e5e5
			}
			.active{
				background: #dea156;
				color: #FFF;
			}
		}
	}
	.list{
		margin-top: 20rpx;
		padding: 0 20rpx;
		.item{
			margin-bottom: 20rpx;
			height: 250rpx;
			width: 710rpx;
			image{
				height: 250rpx;
				width: 250rpx;
			}
			.info{
				width: 430rpx;
				margin-left: 30rpx;
				font-size: 28rpx;
				justify-content: space-around;
				align-items: flex-start;
				.title{
					font-size: 34rpx;
					height: 90rpx;
					width: 100%;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow : ellipsis; 
				}
				.bottom{
					width: 100%;
				}
				.price text{
					font-size: 36rpx;
					color: red;
				}
			}
		}
	}
	.tips{
		height: 80rpx;
		line-height: 80rpx;
		width: 100vw;
		text-align: center;
		font-size: 28rpx;
		color: #9e9a9a;
		margin-bottom: 30rpx;
	}
</style>
