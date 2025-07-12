<template>
	<view class="goodsDetail">
		<Title :type="1" title="商品详情"/>
		<view class="swiper">
			<swiper
			  indicator-dots
			  circular>
			  <block v-for="(item, index) in imgList" :key="index">
			    <swiper-item>
			      	<view class="slide-image center">
			      		<image :src="item" mode="heightFix" @click="prewImage(item)"/>
			      	</view>
			    </swiper-item>
			  </block>
			</swiper>
		</view>
		
		
		<view class="title">
			{{detail.name}}
			<!-- <image @click="copyTitle(detail.name)" src="@/static/copy.png" mode=""></image> -->
		</view>
		<view class="price">
			<!-- <view class="currentPrice between" style="margin-bottom: 20rpx;">
				<view>
					<text style="color: #adabab">商品编码</text>
					<text style="color: #adabab">{{query.id}}</text>
				</view>
				<view @click.stop="copyCode(query.id)" style="font-size: 24rpx; border: 1px solid #c5c5c5; padding: 1px 5px; color: #423f3f;">复制</view>
			</view> -->
				
			<view class="currentPrice">
				<!-- <text>当前价格</text> -->
				<view>{{detail.price}}円</view>
				<view>约 {{detail.price_rmb}} 人民币</view>
			</view>
		</view>
		
		<view class="des">
			<view class="maintitle">产品详情</view>
			<view class="richText">
				<rich-text :nodes="htmlData" selectable></rich-text>
			</view>
		</view>
		
		<view class="options around">
			<!-- <view class="item column" @click="verifyLogin('toPath', '/pages/mine/contact')">
				<image src="@/static/service.png"></image>
				<view>客服</view>
			</view> -->
			<view class="item column" @click="verifyLogin('toCollect')">
				<image src="@/static/star.png" v-if="!isCollect"></image>
				<image src="@/static/star-fill.png" v-if="isCollect"></image>
				<view v-if="!isCollect">收藏</view>
				<view v-if="isCollect">已收藏</view>
			</view>
			<view class="item column" @click="verifyLogin('toPath', '/pages/mercari/cart')">
				<uni-icons type="cart" size="24"></uni-icons>
				<view>购物车</view>
			</view>
			<view class="btns center">
				<view class="btn" @click="verifyLogin('addToCart')">加入购物车</view>
				<view class="btn" @click="verifyLogin('showTips')">立即购买</view>
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
						<view class="value">{{ detail.price_rmb }}元</view>
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
				<view class="submitBtn" @click="verifyLogin('giveOrder')">支付</view>
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
				detail: {
					goodId: '',	// 商品id
					goodPrice: 7350
				},
				imgList: [],
				isCollect: false,
				htmlData: '',
				rate: null,
				userInfo: {},
				query: null,
				buyingAgentFee: 0,
				totalPrice: 0,
			}
		},
		async onLoad(query) {
			this.query = query
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.rate = wx.getStorageSync('rate') || null
			
			await this.getMercariGoodDetail(query)
			if(wx.getStorageSync('login') == 'true'){
				this.addViewLog()
			}
		},
		onShow(){
			if(wx.getStorageSync('login') == 'true'){
				this.queryCollectById()
			}
		},
		methods: {
			showTips(){
				const _this = this
				 wx.showModal({
				    title: '提示',
				    content: '请注意！煤炉下单付款以后，无法取消，退款，退换货，请再次核对产品后支付',
				    success (res) {
				        if (res.confirm) {
				            console.log('用户点击确定')
							_this.showTotalDialog()
				        } else if (res.cancel) {
				            console.log('用户点击取消')
				        }
				    }
				})
			},
			showTotalDialog(){
				this.$refs.popupDialog3.open()
				const {price} = this.detail
				
				// 处理代购费
				if(price <= 50000){
					this.buyingAgentFee = 40
				}else if(price > 50000 && price <= 200000){
					this.buyingAgentFee = 100
				}else{
					this.buyingAgentFee = price * 3 / 100 * this.rate
				}
				
				this.totalPrice = (this.buyingAgentFee * 10000 + this.detail.price_rmb * 10000 ) / 10000
			},
			addViewLog(){
				const params = {
					platform: 1,
					goodcode: this.query.id,
					imageUrl: this.imgList[0],
					title: this.detail.name,
					counts: 0,
					price: this.detail.price,
					userid: this.userInfo.userid,
				}
				this.$api.addViewLog(params)
			},
			prewImage(item){
				wx.previewImage({
				  current: item,
				  urls: this.imgList
				})
			},
			async giveOrder(){
				wx.showLoading({title: '支付中'})
				const params = {
					userid: this.userInfo.userid,
					imageurl: this.imgList[0],
					goodCode: this.query.id,
					title: this.detail.name,
					price: this.totalPrice,
					// price: 0.01,
					count: 1,
					addressid: '',
				}
				const res = await this.$api.mercariOrder(params)
				const {data} = res.data
				
				console.log('success', res)
				wx.hideLoading()
				wx.showToast({
					title: '下单成功'
				})
				setTimeout(()=>{
					wx.navigateTo({
						url: '/pages/mine/allOrderList?type=2'
					})
				}, 1500)
				
				return false
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
								url: '/pages/mine/orderList?type=2'
							})
						}, 1500)
					},
					fail: (res)=>{
						wx.hideLoading()
						console.log('fail', res)
					}
				})
			},
			async queryCollectById(){
				const query = this.query
				const params = {
					goodcode: query.id,
					userid: this.userInfo.userid,
				}
				const res = await this.$api.queryCollectById(params)
				const {data} = res.data
				this.isCollect = data.type === 1
			},
			toPath(url){
				wx.navigateTo({
					url
				})
			},
			goback(){
				uni.navigateBack(-1)
			},
			async toCollect(){
				wx.showLoading({title: '操作中'})
				if(this.isCollect){
					const params = {
						goodcode: this.query.id,
						userid: this.userInfo.userid,
					}
					const res = await this.$api.deleteCollect(params)
					wx.hideLoading()
					wx.showToast({title: '操作成功', icon: 'none'})
					this.isCollect = false
				}else{
					const params = {
						platform: 1,
						goodcode: this.query.id,
						imageUrl: this.imgList[0],
						title: this.detail.name,
						counts: 0,
						price: this.detail.price,
						userid: this.userInfo.userid,
					}
					const res = await this.$api.addCollect(params)
					wx.hideLoading()
					wx.showToast({title: '操作成功', icon: 'none'})
					this.isCollect = true
				}
			},
			async addToCart(){
				wx.showLoading({title: '操作中'})
				this.loading = true
				const params = {
					imageurl: this.imgList[0],
					goodcode: this.query.id,
					title: this.detail.name,
					price: this.detail.price,
					userid: this.userInfo.userid,
				}
				const res = await this.$api.addCart(params)
				wx.showToast({title: '操作成功', icon: 'none'})
				wx.hideLoading()
			},
			
			async getMercariGoodDetail(query){
				wx.showLoading({title: '加载中'})
				this.loading = true
				const {id} = query
				const params = {
					goodcode: id
				}
				const res = await this.$api.getMercariGoodDetail(params)
				const { data } = res.data
				data.price_rmb = parseInt((data.price * this.rate) * 100) / 100
				this.detail = data
				this.imgList = data.photos?.map(item=>{
					return item.replace('https://', 'https://imghk.ripai.com/')
				})
				this.htmlData = data.description
				wx.hideLoading()
			},
			verifyLogin(method, val){
				if(wx.getStorageSync('login')){
					this[method](val)
				}else{
					wx.showToast({
						icon: 'none',
						title: '暂未登录，请先登录小程序'
					})
					setTimeout(()=>{
						wx.switchTab({
							url: '/pages/mine/index'
						})
					}, 1500)
				}
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
			},
			copyTitle(val){
				wx.setClipboardData({
					data: val,
					success: ()=>{
						wx.showToast({
							title: '复制成功'
						})
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.goodsDetail{
		padding-bottom: 160rpx;
	}
	.swiper{
		width: 750rpx;
		height: 650rpx;
		position: relative;
		.icon{
			width: 18rpx;
			height: 34rpx;
			position: absolute;
			left: 30rpx;
			top: 100rpx;
			z-index: 999;
		}
		swiper{
			width: 750rpx;
			height: 650rpx;
			.slide-image{
				width: 750rpx;
				height: 650rpx;
				image{
					max-width: 750rpx;
					max-height: 650rpx;
					// width: 750rpx;
					// height: 650rpx;
				}
			}
			// background: rgb(251, 121, 23);
		}
	}
	.timer{
		box-sizing: border-box;
		padding: 0 20rpx;
		background: rgb(251, 121, 23);
		color: #FFF;
		font-size: 26rpx;
		height: 100rpx;
		.reflash{
			text-align: right;
		}
	}
	
	.title{
		display: block;
		font-weight: 600;
		// line-height: 50rpx;
		margin: 30rpx 0;
		padding: 0 20rpx;
		// display: -webkit-box;
		// -webkit-box-orient: vertical;
		// -webkit-line-clamp: 2;     //  限制行数  
		// overflow: hidden;
		// text-overflow : ellipsis; 
		image{
			width: 30rpx;
			height:28rpx;
			position: relative;
			top: 4rpx;
		}
	}
	.currentPrice{
		padding: 0 20rpx;
		view:nth-child(1){
			color: #C91818;
			font-size: 60rpx;
		}
		view:nth-child(2){
			font-weight: 600;
			font-size: 24rpx;
			color: #000;
		}
	}
	.des{
		padding-top: 30rpx;
		background-color: #F2F2F2;
		width: 690rpx;
		margin: 30rpx auto;
		.maintitle{
			width: 100%;
			text-align: center;
			font-size: 40rpx;
		}
	}
	.priceLog{
		padding: 40rpx 20rpx;
		font-size: 24rpx;
		margin-top: 30rpx;
		border-top: 16rpx solid #f7f7f7;
		border-bottom: 16rpx solid #f7f7f7;
		.log .text{
			opacity: 0.5;
			margin: 0 10rpx;
		}
		.highest text{
			margin-left: 20rpx;
			color: red;
		}
	}
	.tips{
		padding: 40rpx 20rpx;
		font-size: 24rpx;
		border-bottom: 16rpx solid #f7f7f7;
		.tipTitle{
			color: #ffa500;
		}
		.item{
			margin-top: 20rpx;
			opacity: 0.7;
		}
	}
	
	.tabs{
		padding: 40rpx 20rpx 0 20rpx;
		border-bottom: 1px solid #e7e7e7;
		.item{
			width: 150rpx;
			text-align: center;
			padding-bottom: 40rpx;
		}
		.active{
			color: #ffa500;
			font-weight: 600;
			border-bottom: 1px solid #ffa500;
		}
	}
	
	.richText{
		padding: 40rpx 20rpx;
	}
	.options{
		align-items: center;
		font-size: 26rpx;
		width: 750rpx;
		box-sizing: border-box;
		background: #fff;
		position: fixed;
		bottom: 0;
		left: 0;
		padding: 0 20rpx;
		height: 100rpx;
		.item{
			width: 100rpx;
			margin-right: 20rpx;
			image{
				width: 40rpx;
				height: 40rpx;
			}
		}
		.btns{
			height: 70rpx;
			color: #FFF;
			border-radius: 40rpx;
			overflow: hidden;
			.btn{
				padding: 0rpx 35rpx;
				height: 70rpx;
				line-height: 70rpx;
				// width: 400rpx;
				text-align: center;
				background: #ffa500;
			}
			.btn:last-child{
				background: #fb7917;
			}
		}
	}
	.popup-content{
		background: #FFF;
		padding: 20rpx 50rpx;
		border-radius: 25rpx;
		width: 600rpx;
		position: relative;
		height: 800rpx;
		padding-bottom: 50rpx;
		.dialogTitle{
			font-size: 38rpx;
			font-weight: 600;
		}
		.costForm{
			margin-top: 52rpx;
			font-size: 38rpx;
			color: #606060;
			.title{
				font-weight: 500;
				margin: 10rpx 0;
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
		width: 616rpx;
		height: 72rpx;
		line-height: 72rpx;
		background: #FF9B24;
	}
</style>
