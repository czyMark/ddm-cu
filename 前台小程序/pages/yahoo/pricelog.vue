<template>
	<view class="pricelog">
		<view class="list">
			<view class="item between" v-for="(item, index) in  list" :key="index">
				<view class="name">{{item.name}}</view>
				<view class="status">{{item.status}}</view>
				<view class="time">{{item.time}}</view>
				<view class="price">{{item.price}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: []
			}
		},
		onLoad() {
			const eventChannel = this.getOpenerEventChannel()
			// 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
			eventChannel.on('acceptDataFromOpenerPage', (data)=>{
				data.biddings?.forEach((item, index)=>{
					if(index === 0){
						item.status = '最高'
					}else{
						item.status = '出局'
					}
					// item.rmb = parseInt(item.price * 1 / 20 * 100) / 100
					item.rmb = ''
				})
				this.list = data.biddings
			})
		},
	}
</script>

<style lang="scss">
	.list{
		font-size: 26rpx;
		padding: 40rpx 20rpx;
		.item{
			margin-bottom: 40rpx;
			color: #b1b1b1;
			.name{
				color: #000;
				width: 100rpx;
				overflow : hidden;
				white-space : nowrap; 
				text-overflow : ellipsis;
			}
			.status{
				width: 60rpx;
			}
			.time{
				width: 260rpx;
			}
			.price{
				// width: 250rpx;
				width: 120rpx;
				overflow : hidden;
				white-space : nowrap; 
				text-overflow : ellipsis;
				text-align: right;
			}
		}
		.item:first-child{
			color: #ffa500;
			.name{
				color: #ffa500
			}
		}
	}
</style>
