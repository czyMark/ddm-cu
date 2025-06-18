<template>
	<view class="collect page">
		<Title :type="1" title="我的收藏" :showBack="false"/>
		
		<!-- <view class="tabs around">
			<view :class="['tabItem', currentPlant === item.id ? 'active' : '']" 
				v-for="(item, index) in tabList" :key="index" @click="choosetab(item.id)">
				{{item.title}}
			</view>
		</view> -->
		
		<view class="plant between">
			<image
				class="item"
				v-for="(item, index) in plants" :key="index"
				@click="choosePlants(item)"
				:src="currentPlant === item.id ? item.icon : item.icon2"
			>
		</view>
		
		<view class="list" v-if="currentPlant === 0">
			<view class="item flex" v-for="(item, index) in yahooList" :key="index"
				@click="chooseItem(item, 1)">
				<image :src="item.imageUrl"></image>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price"><text>{{item.price}}日元</text></view>
				</view>
			</view>
			<view class="tips column" v-if="yahooList.length === 0">
				<image
					class="icon"
					src="@/static/icon/58.png"
				>
				<view>您的收藏空空如也</view>
			</view>
		</view>
		<view class="list" v-if="currentPlant === 1">
			<view class="item flex"  v-for="(item, index) in mercariList" :key="index"
				@click="chooseItem(item, 2)">
				<image :src="item.imageUrl"></image>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="bottom between">
						<view class="price"><text>{{item.price}}日元</text></view>
					</view>
				</view>
			</view>
			<view class="tips column" v-if="mercariList.length === 0">
				<image
					class="icon"
					src="@/static/icon/58.png"
				>
				<view>您的收藏空空如也</view>
			</view>
		</view>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
		data() {
			return {
				currentPlant: 0, // 0雅虎  1煤炉
				plants: [
					{id: 0, icon: '/static/icon/14.png', icon2: '/static/icon/15.png'},
					{id: 1, icon: '/static/icon/17.png', icon2: '/static/icon/16.png'},
				],
				
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
					platform: this.currentPlant
				}
				const res = await this.$api.queryCollect(params)
				const { data } = res.data

				data.forEach(item=>{
					item.isCollect = true
				})
				
				if(this.currentPlant === 0){
					this.yahooList = data
				}else{
					this.mercariList = data
				}
				
				wx.hideLoading()
			},
			
			choosePlants(item){
				this.list = []
				this.page = 1
				this.size = 50
				this.currentPlant = item.id
				if(item.id === 1){
					
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
			width: 710rpx;
			image{
				width: 220rpx;
				height: 180rpx;
				border-radius: 20rpx ;
			}
			.info{
				width: 450rpx;
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
		width: 100vw;
		text-align: center;
		font-size: 28rpx;
		color: #9e9a9a;
		margin: 80rpx 0 0;
		image{
			width: 428rpx;
			height: 470rpx;
			margin-bottom: 20rpx;
		}
	}
	
	.plant{
		padding: 20rpx 70rpx;
		.item{
			width: 220rpx;
			height: 86rpx;
			margin-right: 30rpx;
		}
	}
</style>
