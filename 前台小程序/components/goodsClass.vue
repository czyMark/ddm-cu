<template>
	<view class="goodsClass between">
		<view class="left">
			<view :class="[
					'leftItem',
					currentBigClass === item.id ? 'current' : '',
					item.cid === '11' ? 'special' : ''
				]"
				v-for="(item, index) in classList"
				:key="index"
				@click="onBigChange(item)"
				v-if="item.isShow"
			>
				{{ item.name_cn || item.cname }}
			</view>
		</view>
		
		<view class="right">
			<view class="rightTop around">
				<view :class="['rightTopItem column', currentMiddleClass === item.id ? 'current' : '']"
					v-for="(item, index) in middleClass" :key="index" @click="toPath(item)"
					v-if="item.isShow">
					<!-- v-for="(item, index) in middleClass" :key="index" @click=onMiddleChange(item)> -->
					<image
						class="icon"
						:src="
							item.imageUrl || (type === 'yahoo' ? yahooImg : mercariImg)
						"
					>
					<view>
						{{ item.name_cn || item.cname }}
					</view>
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
				yahooImg: require('@/static/yahoo-fill.png'),
				mercariImg: require('@/static/mm.png'),
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

<style scoped lang="scss">
	.goodsClass{
		margin-top: 30rpx;
		height: calc(100vh - 320rpx);
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
		color: #949292;
		font-size: 26rpx;
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
		font-weight: 600;
		color: #4C43E8;
		position: relative;
	}
	.left .current::before{
		content: '';
		position: absolute;
		width: 8rpx;
		height: 28rpx;
		background-color: #4C43E8;
		left: 24rpx;
		top: 20rpx;
	}
	.right{
		width: 520rpx;
		padding-right: 10rpx;
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
		background: #949292;
		border-radius: 6rpx;
	}

	.rightTop::-webkit-scrollbar-track {
		background: #e7e7e7;
		border-radius: 6rpx;
	}
	.rightTopItem{
		width: 140rpx;
		font-weight: 500;
		color: #000;
		font-size: 20rpx;
		margin-bottom: 30rpx;
		image{
			width: 125rpx;
			height: 125rpx;
			border-radius: 50%;
			border: 1px solid #C4C4C4;
			margin-bottom: 25rpx;
		}
	}
	.rightTop .current{
		color: #4C43E8;
		position: relative;
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
		width: 125rpx;
		height: 0;
	}
	.rightBottom::-webkit-scrollbar {
		width: 4px;
	}
	
	.rightBottom::-webkit-scrollbar-thumb {
		background: #949292;
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
