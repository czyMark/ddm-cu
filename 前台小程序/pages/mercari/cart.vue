<template>
	<view class="collect page">
		<Title :type="1" title="购物车"/>
		
		<view class="header between" v-if="list.length > 0">
			<view class="clearAll" @click="clearAll">
				<uni-icons type="trash" size="18"></uni-icons>
				<text>清空购物车</text>
			</view>
			<view class="edit" @click="showDelete" v-if="!showDeleteBtn">编辑</view>
			<view class="edit" @click="showDelete" v-if="showDeleteBtn">完成</view>
		</view>
		<view class="list">
			<view class="item center" v-for="(item, index) in list" :key="index">
				<view class="check">
					<radio :checked="item.isSelect" @click.navtive="onSelectChange(item)"  v-if="!showDeleteBtn"/>
					<uni-icons color="red" type="trash" size="24" v-if="showDeleteBtn" @click="deleteItem(item)"></uni-icons>
				</view>
				<view class="content center" @click="chooseItem(item)">
					<image :src="item.imageurl"></image>
					<view class="info column">
						<view class="title">{{item.title}}</view>
						<view class="price"><text>{{item.price}}</text></view>
						<!-- <view class="price">现价：<text>{{item.price}}円</text></view> -->
						<!-- <view class="price"><text>{{item.price_rmb}}元</text></view> -->
					</view>
					<view class="opt flex" @click.stop="">
						<button @click.stop="reduce(item)" :disabled="item.count<2">-</button>
						<input type="number" v-model="item.count" @input.stop="onInput" :disabled="true"/>
						<button @click.stop="add(item)" :disabled="item.count<2">+</button>
					</view>
				</view>
			</view>
			<view class="tips" v-if="list.length === 0">空空如也</view>
		</view>
		<view class="footer between" v-if="list.length > 0">
			<view class="selectAll">
				<radio :checked="isSelectAll" @click.navtive="onSelectAllChange"/><text>全选</text>
			</view>
			<view class="between">
				<view class="total">合计: <text>{{totalPrce}}日元</text></view>
				<!-- <view class="btn" @click="payOrder">结算</view> -->
				<image
					class="icon"
					src="@/static/icon/57.png"
					 @click="showTotalDialog"
				>
			</view>
		</view>
		
		<uni-popup ref="popupDialog3">
			<view class="popup-content popup-content2">
				<view class="header center">
					<p class="dialogTitle">付款详情</p>
				</view>
				<view class="costForm">
					<view class="item between">
						<view class="title">商品价格共计</view>
						<view class="value">{{ totalRmb }}元</view>
					</view>
					<view class="item between">
						<view class="title">代购费共计</view>
						<view class="value">{{buyingAgentFee}}元</view>
					</view>
					<view class="item between">
						<view class="title">合计</view>
						<view class="value">{{totalPrice}}元</view>
					</view>
				</view>
				<view class="submitBtn" @click="payOrder">支付</view>
			</view>
		</uni-popup>

	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
		data() {
			return {
				hasClass: false, // 有无分类参数
				keyword: '',
				list: [],
				selectList: [],
				page: 1,
				size: 50,
				
				showDeleteBtn: false,
				isSelectAll: false,
				totalPrce: 0,
				totalRmb: 0,
				userInfo: {},
				rate: null,
				
				buyingAgentFee: 0,
				totalPrice: 0,
			}
		},
		onLoad() {
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.rate = wx.getStorageSync('rate') || null
			this.queryCart()
		},
		methods: {
			
			showTotalDialog(){
				this.$refs.popupDialog3.open()
				
				let price = 0
				let selectList = this.selectList
				selectList?.forEach(item=>{
					// 处理代购费
					if(item.price <= 50000){
						item.buyingAgentFee =  40
						price += 40
					}else if(item.price > 50000 && item.price <= 200000){
						item.buyingAgentFee =  100
						price += 100
					}else{
						item.buyingAgentFee =  parseInt((price * 30000 / 100 * this.rate) / 100) / 100
						price = parseInt(((price * 10000) + (price * 30000 / 100 * this.rate)) / 100) / 100
					}
				})
				this.selectList = selectList
				
				this.buyingAgentFee = price
				this.totalPrice = (this.totalRmb * 10000 + this.buyingAgentFee * 10000 ) / 10000
			},
			async payOrder(){
				if(this.selectList.length === 0) return false
				wx.showLoading({title: '支付中'})
				const params = {
					userid: this.userInfo.userid,
					addressid: '',
					shppingCart: []
				}
				
				this.selectList?.forEach(item=>{
					const obj = {
						imageurl: item.imageurl,
						goodCode: item.goodcode,
						title: item.title,
						price: ((item.price_rmb * 10000) + (item.buyingAgentFee * 10000)) / 10000,
						// price: 0.01,
						count: 1,
					}
					params.shppingCart.push(obj)
				})
				
				console.log('shppingCart', params.shppingCart)
				
				const res = await this.$api.mercariCartOrder(params)
				const {data} = res.data
				const {timeStamp, nonceStr, signType, paySign} = data
				wx.requestPayment({
					timeStamp: timeStamp,
					nonceStr: nonceStr,
					package: data.package,
					signType: signType,
					paySign: paySign,
					success: (res)=>{
						console.log('success', res)
						wx.hideLoading()
						wx.showToast({
							title: '下单成功'
						})
						setTimeout(()=>{
							wx.navigateTo({
								url: '/pages/mine/orderList'
							})
						}, 1500)
					},
					fail: (res)=>{
						wx.hideLoading()
						console.log('fail', res)
					}
				})
			},
			async queryCart(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
				}
				const res = await this.$api.queryCart(params)
				const { data } = res.data
		
				data.forEach(item=>{
					item.count = 1
					item.price_rmb = parseInt((item.price * this.rate) * 100) / 100
				})
				
				this.list = data
				
				wx.hideLoading()
			},
			
			chooseItem(item){
				wx.navigateTo({
					url: '/pages/mercari/detail?id=' + item.goodcode
				})
			},
			
			reduce(item){
				this.list.forEach(it=>{
					if(it.id === item.id) it.count--
				})
			},
			add(item){
				this.list.forEach(it=>{
					if(it.id === item.id) it.count++
				})
			},
			onSelectChange(item){
				item.isSelect = !item.isSelect
				this.countTotal()
				console.log('list', this.list)
			},
			onSelectAllChange(){
				this.isSelectAll = !this.isSelectAll
				if(this.isSelectAll){
					this.list.forEach(item=>{
						item.isSelect = true
					})
				}else{
					this.list.forEach(item=>{
						item.isSelect = false
					})
				}
				this.countTotal()
			},
			countTotal(){
				let totalPrce = 0
				let totalRmb = 0
				this.selectList = []
				this.list?.forEach(item=>{
					if(item.isSelect){
						totalPrce += (item.price) * 100
						totalRmb += (item.price_rmb) * 100
						this.selectList.push(item)
					}
				})
				this.totalPrce = totalPrce / 100
				this.totalRmb = totalRmb / 100
			},
			async clearAll(){
				wx.showLoading({title: '操作中'})
				const params = {
					userid: this.userInfo.userid,
				}
				const res = await this.$api.clearCart(params)
				this.list = []
				this.countTotal()
				wx.hideLoading()
			},
			showDelete(){
				this.showDeleteBtn = !this.showDeleteBtn
			},
			async deleteItem(item){
				wx.showLoading({title: '操作中'})
				const params = {
					userid: this.userInfo.userid,
					id: item.id
				}
				const res = await this.$api.deleteCart(params)
				let index = ''
				this.list.forEach((it, i)=>{
					if(it.id === item.id) index = i
				})
				this.list.splice(index, 1)
				this.countTotal()
				wx.hideLoading()
			}
		}
	}
</script>

<style lang="scss" scoped>
	page{
		background-color: #f7f7f7;
	}
	.header{
		width: 750rpx;
		box-sizing: border-box;
		padding: 20rpx;
		font-size: 28rpx;
		.edit{
			color: #fb7917;
		}
	}
	.list{
		background: #FFF;
		padding-top: 20rpx;
		padding-bottom: 120rpx;
		// padding: 0 20rpx;
		.item{
			padding-left: 20rpx;
			.content{
				position: relative;
				margin: 0 auto;
				margin-bottom: 20rpx;
				height: 200rpx;
				width: 630rpx;
			}
			image{
				height: 152rpx;
				width: 192rpx;
			}
			.info{
				height: 152rpx;
				width: 410rpx;
				margin-left: 30rpx;
				font-size: 24rpx;
				justify-content: space-around;
				align-items: flex-start;
				.title{
					font-size: 30rpx;
					height: 86rpx;
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
			.opt{
				position: absolute;
				right: 0;
				bottom: 0;
				input{
					width: 50rpx;
					border: 1px solid #e5e5e5;
					color: #ffa500;
					padding: 0 10rpx;
					text-align: center;
				}
				button{
					width: 46rpx;
					height: 46rpx;
					line-height: 46rpx;
					padding: 0;
					text-align: center;
					background-color: #fff;
					border-radius: 0;
				}
			}
		}
	}
	::v-deep .uni-drawer__content{
		width: 600rpx !important;
	}
	.footer{
		font-size: 24rpx;
		height: 150rpx;
		width: 750rpx;
		box-sizing: border-box;
		padding: 0 20rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		background: #FFF;
		border-top: 8rpx solid #d6d6d6;
		.total text{
			margin-left: 10rpx;
			color: red;
		}
		.btn{
			letter-spacing: 6rpx;
			color: #FFF;
			font-weight: 600;
			border-radius: 20rpx;
			height: 70rpx;
			line-height: 70rpx;
			width: 120rpx;
			text-align: center;
			background: #fb7917;
		}
		.icon{
			width: 174rpx;
			height: 64rpx;
			margin-left: 50rpx;
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
	
	.popup-content{
		background: #FFF;
		padding: 20rpx 50rpx;
		border-radius: 25rpx;
		width: 600rpx;
		position: relative;
		height: 800rpx;
		padding-bottom: 50rpx;
		.header{
			width: 650rpx;
		}
	}
	.costForm{
		margin-top: 50rpx;
		.item{
			align-items: flex-start;
			margin-bottom: 20rpx;
			font-size: 30rpx;
			.value{
				text-align: right;
				width: 300rpx;
			}
		}
	}
	.submitBtn{
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		margin: 0 auto;
		margin: 20rpx auto;
		border-radius: 20rpx;
		text-align: center;
		color: #FFF;
		width: 270rpx;
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
	}
</style>
