<template>
	<view class="goodsClass between">
		<view class="left">
			<view :class="['leftItem', currentBigClass === item.id ? 'current' : '', item.cid === '11' ? 'special' : '']"
				v-for="(item, index) in classList" :key="index" @click=onBigChange(item)>
				{{ item.name_cn || item.cname }}
			</view>
		</view>
		
		<view class="right">
			<view class="rightTop flex">
				<view :class="['rightTopItem', currentMiddleClass === item.id ? 'current' : '']"
					v-for="(item, index) in middleClass" :key="index" @click=toPath(item)>
					<!-- v-for="(item, index) in middleClass" :key="index" @click=onMiddleChange(item)> -->
					{{ item.name_cn || item.cname }}
				</view>
				<i></i>
				<i></i>
				<i></i>
			</view>
			<!-- <view class="rightBottom">
				<view class="rightBottomItem" @click="toPath(item)"
					v-for="(item, index) in smallClass" :key="index">
					{{ item.name_cn || item.cname }}
				</view>
				<i></i>
				<i></i>
				<i></i>
				<i></i>
				<i></i>
			</view> -->
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			type: {
				type: String,
				default: ''
			},
			classList: {
				type: Array,
				default: ()=>[]
			},
			bigClass: {
				type: [String, Number],
				default: ''
			},
		},
		data() {
			return {
				middleClass:[],
				smallClass: [],
				currentBigClass: null,
				currentMiddleClass: null,
			}
		},
		watch: {
			bigClass(val){
				this.currentBigClass = val
				console.log('val', val)
				this.classList.forEach(item=>{
					console.log('item', item)
					if(item.id === val) this.middleClass = item.children
				})
			}
		},
		methods: {
			onBigChange(item){
				if(this.type === 'yahoo'){
					wx.setStorage({
						key: 'yahooId',
						data: item.id
					})
				}
				this.currentBigClass = item.id
				this.middleClass = item.children
				this.$parent.onBigClassChange(item.id)
			},
			onMiddleChange(item){
				this.currentMiddleClass = item.id
				this.smallClass = item.children
			},
			toPath(item){
				this.currentMiddleClass = item.id
				if(this.type === 'yahoo'){
					wx.navigateTo({
						url: `/pages/yahoo/yahooResult?bigClass=${this.currentBigClass}&middleClass=${this.currentMiddleClass}`
					})
				}else{
					wx.navigateTo({
						url: `/pages/mercari/mercariResult?bigClass=${this.currentBigClass}&middleClass=${this.currentMiddleClass}`
					})
				}
			}
		}
	}
</script>

<style>
	.goodsClass{
		margin-top: 30rpx;
		height: calc(100vh - 140rpx);
	}
	.left{
		height: calc(100% + 30rpx);
		width: 200rpx;
		overflow-y: auto;
		overflow-x: hidden;
		background-color: #FFF;
		border-right: 1px solid #e7e7e7;
	}
	.left::-webkit-scrollbar {
		width: 4px;
	}
	
	.left::-webkit-scrollbar-thumb {
		background: #e89909;
		border-radius: 6rpx;
	}
	
	.left::-webkit-scrollbar-track {
		background: #e7e7e7;
		border-radius: 6rpx;
	}
	.right{
		height: 100%;
	}
	.leftItem{
		padding: 0 15rpx;
		box-sizing: border-box;
		padding:  0 10rpx;
		width: 200rpx;
		text-align: center;
		height: 70rpx;
		line-height: 70rpx;
		overflow : hidden;
		white-space : nowrap; 
		text-overflow : ellipsis;
	}
	.special{
		font-size: 28rpx;
		line-height: 39rpx;
		white-space: unset;
		height: 80rpx;
		line-break: auto;
		text-overflow: unset;
		overflow: unset;
	}
	.left .current{
		background: #ffe997;
		font-weight: 600;
	}
	.right{
		width: 490rpx;
	}
	.rightTop{
		min-height: 100rpx;
		max-height: calc(100vh - 200rpx);
		flex-wrap: wrap;
		margin-bottom: 10rpx;
		padding-bottom: 30rpx;
		overflow-y: scroll;
	}
	.rightTop::-webkit-scrollbar {
		width: 4px;
	}

	.rightTop::-webkit-scrollbar-thumb {
		background: #e89909;
		border-radius: 6rpx;
	}

	.rightTop::-webkit-scrollbar-track {
		background: #e7e7e7;
		border-radius: 6rpx;
	}
	.rightTopItem{
		font-weight: 600;
		color: #000;
		padding: 10rpx 20rpx;
		margin-bottom: 15rpx;
	}
	.rightTop .current{
		color: orange;
		position: relative;
	}
	.rightTop .current::before{
		content: '';
		position: absolute;
		left: 0;
		right: 0;
		margin: 0 auto;
		width: 60rpx;
		height: 2px;
		background: orange;
		bottom: 0;
	}
	.rightBottom{
		max-height: calc(100% - 250rpx);
		overflow: auto;
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		padding-top: 30rpx;
		padding-right: 20rpx;
	}
	.right i{
		width: 60rpx;
		height: 0;
	}
	.rightBottom::-webkit-scrollbar {
		width: 4px;
	}
	
	.rightBottom::-webkit-scrollbar-thumb {
		background: #e89909;
		border-radius: 6rpx;
	}
	
	.rightBottom::-webkit-scrollbar-track {
		background: #e7e7e7;
		border-radius: 6rpx;
	}
	.rightBottomItem{
		box-sizing: border-box;
		min-width: 45%;
		height: 70rpx;
		text-align: center;
		padding: 0 20rpx;
		line-height: 70rpx;
		background: #e5e5e5;
		border-radius: 35rpx;
		margin-bottom: 30rpx;
		font-size: 28rpx;
		
	}
</style>
