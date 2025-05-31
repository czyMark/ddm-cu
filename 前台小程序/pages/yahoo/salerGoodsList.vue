<template>
	<view class="salerGoodsList">
		<view class="salerInfo between">
			<view>卖家：{{query.seller}}</view>
			<view>好评：{{query.goodpj}}</view>
			<view>差评：{{query.badpj}}</view>
		</view>
		
		
		<view class="filter between">
			<view class="itemFilter center"
				v-for="(item, index) in tabs" :key="index"
				@click="chooseTab(item)">
				<view :class="['label', item.isCurrent ? 'active' : '']">{{item.label}}</view>
				<view class="icons column" v-if="item.sort === 0">
					<image src="@/static/Arrow-Up2.png"></image>
					<image src="@/static/Arrow-Down2.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 2">
					<image src="@/static/Arrow-Up2-active.png"></image>
					<image src="@/static/Arrow-Down2.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 1">
					<image src="@/static/Arrow-Up2.png"></image>
					<image src="@/static/Arrow-Down2-active.png"></image>
				</view>
			</view>
			<view class="listMode" @click="changeIsList">
				<image src="@/static/list.png" mode="" v-if="!isList"></image>
				<image src="@/static/menu.png" mode="" v-if="isList"></image>
			</view>
		</view>
		
		<view class="list" v-if="!isOnFocus && isList">
			<view class="item center" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="imageUrl center">
					<image :src="item.imageUrl" mode="aspectFit"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price between" style="width: 478rpx">
						<view>编号：<text>{{item.code}}</text></view>
						<view @click.stop="copyCode(item.code)" style="border: 1px solid #c5c5c5; padding: 1px 5px; color: #423f3f;">复制</view>
					</view>
					<view class="saler">出品者：{{item.seller}}</view>
					<view class="price">现价：<text>{{item.jpprice}}円 ({{item.price_rmb}}元)</text></view>
					<view class="bottom between">
						<view class="times center">
							<image src="@/static/shiwu-chuizi.png" mode=""></image>
							<text>{{item.counts}}次</text>
						</view>
						<view class="times center">
							<image src="@/static/jishishalou.png" mode=""></image>
							<text>{{item.time}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="list list2 between" v-if="!isList">
			<view class="item" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="imageUrl center">
					<image :src="item.imageUrl" mode="aspectFit"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price between" style="width: 100%">
						<view>编号：<text>{{item.code}}</text></view>
						<view @click.stop="copyCode(item.code)" style="border: 1px solid #c5c5c5; padding: 1px 4px; color: #423f3f; font-size: 18rpx">复制</view>
					</view>
					<view class="saler">出品者：{{item.seller}}</view>
					<view class="price">现价：<text>{{item.jpprice}}円 ({{item.price_rmb}}元)</text></view>
					<view class="bottom between">
						<view class="times center">
							<image src="@/static/shiwu-chuizi.png" mode=""></image>
							<text>{{item.counts}}次</text>
						</view>
						<view class="times center">
							<image src="@/static/jishishalou.png" mode=""></image>
							<text>{{item.time}}</text>
						</view>
					</view>
				</view>
			</view>
			<i style="width: 350rpx;height: 0;"></i>
		</view>
		
		<view class="loading">
			{{noMore ? '没有更多了' : '正在加载...'}}
		</view>
		
	</view>
</template>

<script>
export default {
	data() {
		return {
			tabs: [
				{id: 1, sort: 0, label: '价格', isCurrent: false},
				{id: 2, sort: 0, label: '时间', isCurrent: false},
				{id: 3, sort: 0, label: '热度', isCurrent: false},
			],
			isList: true,
			pagecount: 1,
			list: [],
			noMore: false,
			rate: '',
			
			query: {},
		}
	},
	onLoad(query){
		this.rate = wx.getStorageSync('rate') || null
		this.query = query
		this.list = []
		this.pagecount = 1
		this.queryGoodsBySalerCode()
	},
	methods: {
		async queryGoodsBySalerCode(){
			wx.showLoading()
			const params = {
				seller: this.query.seller,
				pagecount: this.pagecount,
				priceOderType: this.tabs[0].sort,
				timeType: this.tabs[1].sort,
				hotType: this.tabs[2].sort,
			}
			const res = await this.$api.queryGoodsBySalerCode(params)
			const {data} = res.data
			data?.forEach(item=>{
				item.price_rmb = parseInt((item.jpprice * this.rate) * 100) / 100
			})
			if(data.length < 50){
				this.noMore = true
			}
			this.list = this.list.concat(data)
			wx.hideLoading()
		},
		chooseTab(item){
			if(!item.isCurrent){
				this.tabs.forEach(it=>{
					it.isCurrent = false
					it.sort = 0
					if(it.id === item.id){
						it.isCurrent = true
					}
				})
			}
			if(item.sort === 0){
				item.sort = 2
			}else if(item.sort === 2){
				item.sort = 1
			}else if(item.sort === 1){
				item.sort = 2
			}
			this.queryGoodsBySalerCode()
		},
		changeIsList(){
			this.isList = !this.isList
		},
		chooseItem(item){
			const {seller, code, breakurl} = item
			wx.navigateTo({
				url: `/pages/yahoo/detail?code=${item.code}&from=saler`,
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
		}
	},
	
	onReachBottom(){
		this.pagecount++
		this.queryGoodsBySalerCode()
	},
}
</script>

<style lang="scss">
	.salerInfo{
		width: 750rpx;
		margin: 20rpx auto;
		box-sizing: border-box;
		padding: 10rpx 30rpx;
		font-size: 28rpx;
		border-bottom: 10px solid #efe6e6;
	}
.filter{
	margin-top: 30rpx;
	.itemFilter{
		width: 180rpx;
		.icons{
			margin-left: 10rpx;
			image{
				width: 20rpx;
				height: 20rpx;
			}
		}
		.active{
			color: #ffa500;
		}
		.icons2 image{
			width: 30rpx;
			height: 40rpx;
		}
	}
	.listMode{
		width: 30rpx;
		height: 30rpx;
		margin-right: 20rpx;
		image{
			width: 100%;
			height: 100%;
		}
	}
}
.loading{
	height: 80rpx;
	line-height: 80rpx;
	width: 100vw;
	text-align: center;
	font-size: 28rpx;
	color: #9e9a9a;
	margin-bottom: 30rpx;
}
.list{
	margin-top: 20rpx;
	// padding: 0 20rpx;
	.item{
		margin: 0 auto;
		margin-bottom: 20rpx;
		height: 250rpx;
		width: 710rpx;
		.imageUrl{
			height: 200rpx;
			width: 200rpx;
			border-radius: 20rpx;
			overflow: hidden;
		}
		image{
			max-height: 200rpx;
			max-width: 200rpx;
		}
		.info{
			height: 250rpx;
			width: 480rpx;
			margin-left: 30rpx;
			font-size: 24rpx;
			justify-content: space-around;
			align-items: flex-start;
			.title{
				font-size: 30rpx;
				height: 80rpx;
				width: 100%;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 2;
				overflow: hidden;
				text-overflow : ellipsis; 
			}
			.bottom{
				width: 100%;
				.times{
					image{
						width: 30rpx;
						height: 30rpx;
					}
				}
			}
			.price text{
				font-size: 24rpx;
				color: red;
			}
		}
	}
}
::v-deep .uni-drawer__content{
	width: 600rpx !important;
}
.list2{
	flex-wrap: wrap;
	.item{
		height: 515rpx;
		width: 350rpx;
		.imageUrl{
			width: 300rpx;
			height: 300rpx;
			margin-left: 25rpx;
			image{
				max-width: 300rpx;
				max-height: 300rpx;
			}
		}
		.info{
			font-size: 22rpx;
			width: 350rpx;
			height: 225rpx;
			margin-left: 0;
			box-sizing: border-box;
			padding: 0 20rpx;
			.title{
				font-size: 22rpx;
				width: 330rpx;
				height: 60rpx;
			}
			.price{
				width: 100%;
				image{
					width: 30rpx;
					height: 30rpx;
				}
			}
		}
	}
}
</style>
