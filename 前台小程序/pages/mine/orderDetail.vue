<template>
	<view class="page">
		<uni-section title="收货信息" type="line" v-if="!loading && addressDetail">
			<view class="address">
				<view class="container">
					<view class="itemNav flex">
						<view class="info column">
							<view class="bottom between">
								<view class="price">{{addressDetail.username}} {{addressDetail.phone}}</view>
							</view>
							<view class="title">{{addressDetail.region}} {{addressDetail.address}}</view>
						</view>
					</view>
				</view>
			</view>
		</uni-section>
		<view class="orderInfoDetail">
			<view class="orderStatus">
				<view v-if="query.type === '2'">{{mercariTypeMap[orderDetail.type]}}</view>
				<view v-if="query.type === '1'">{{yahooTypeMap[orderDetail.type]}}</view>
			</view>
			<uni-section title="订单信息" type="line">
				<view class="goodItem">
					<view class="container">
						<view class="itemHeader between">
							<view class="no">订单号：{{orderDetail.orderid}}</view>
							<view class="copy" @click="copyOrder(orderDetail.orderid)">复制</view>
						</view>
						<view class="itemNav flex" @click="toGoodDetail(orderDetail.goodCode)">
							<image :src="orderDetail.imageurl"></image>
							<view class="info column">
								<view class="title">{{orderDetail.title}}</view>
								<view class="price">数量：x 1</view>
								<view class="bottom between">
									<view class="price" v-if="orderDetail.ratetype === 0">支付价格<text>{{orderDetail.price}}</text> 円</view>
									<view class="price" v-if="orderDetail.ratetype === 1">支付价格(含税)<text>{{parseInt(orderDetail.price * 110 ) / 100}}</text> 円</view>
								</view>
							</view>
						</view>
					</view>
					<view class="goodItem" v-for="it in orderDetail.children" :key="it.id">
						<view class="container">
							<view class="itemHeader between">
								<view class="no">订单号：{{it.orderid}}</view>
								<view class="copy" @click="copyOrder(it.orderid)">复制</view>
							</view>
							<view class="itemNav flex" @click="toGoodDetail(it.goodCode)">
								<image :src="it.imageurl"></image>
								<view class="info column">
									<view class="title">{{it.title}}</view>
									<view class="price">数量：x 1</view>
									<view class="bottom between">
										<view class="price" v-if="it.ratetype === 0">支付价格<text>{{it.price}}</text> 元</view>
										<view class="price" v-if="it.ratetype === 1">支付价格(含税)<text>{{parseInt(it.price * 110 ) / 100}}</text> 元</view>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</uni-section>
			
			<view class="" v-if="query.type === '1'">
				<uni-section title="尾款详情" type="line" v-if="orderDetail.type > 6 && orderDetail.type !== 100">
					<view class="costForm" v-if="orderDetail.type > 6 && orderDetail.type !== 100">
						<view class="item between">
							<view class="title">代购费</view>
							<view class="value">{{orderDetail.dgPrice}}元</view>
						</view>
						<view class="item between">
							<view class="title">日本邮费</view>
							<view class="value">{{orderDetail.jpyunfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">国际运费</view>
							<view class="value">{{orderDetail.gjyunfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">仓储费</view>
							<view class="value">{{orderDetail.cangcufei}}元</view>
						</view>
						<view class="item between">
							<view class="title">包装费</view>
							<view class="value">{{orderDetail.baozhuangfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">其他费用</view>
							<view class="value">{{orderDetail.otherPrice}}元</view>
						</view>
						<view class="item between">
							<view class="title">其他费用备注</view>
							<view class="value" style="font-size: 26rpx;">{{orderDetail.otherPriceContent}}</view>
						</view>
						<view class="item between">
							<view class="title">共计</view>
							<view class="value">
								{{orderDetail.totalPrice}}元
							</view>
						</view>
					</view>
				</uni-section>
				
				<uni-section title="物流信息" type="line" v-if="orderDetail.type > 8 && orderDetail.type !== 100">
					<view class="header between">
						<view class="copy" @click="copyOrder(orderDetail.trackingNumber)">复制单号</view>
					</view>
					<view class="costForm">
						<view class="item between">
							<view class="title">物流公司</view>
							<view class="value">{{orderDetail.wlcompany}}</view>
						</view>
						<view class="item between">
							<view class="title">物流单号</view>
							<view class="value" style="font-size: 26rpx;">{{orderDetail.trackingNumber}}</view>
						</view>
						<view class="item between">
							<view class="title">物流备注</view>
							<view class="value" style="font-size: 26rpx">{{orderDetail.wlRemarks}}</view>
						</view>
					</view>
				</uni-section>
			</view>
			<view class="" v-if="query.type === '2'">
				<uni-section title="尾款详情" type="line" v-if="orderDetail.type > 3 && orderDetail.type !== 7">
					<view class="costForm">
						<view class="item between">
							<view class="title">代购费</view>
							<view class="value">{{orderDetail.dgPrice}}元</view>
						</view>
						<view class="item between">
							<view class="title">日本邮费</view>
							<view class="value">{{orderDetail.jpyunfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">国际运费</view>
							<view class="value">{{orderDetail.gjyunfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">仓储费</view>
							<view class="value">{{orderDetail.cangcufei}}元</view>
						</view>
						<view class="item between">
							<view class="title">包装费</view>
							<view class="value">{{orderDetail.baozhuangfei}}元</view>
						</view>
						<view class="item between">
							<view class="title">其他费用</view>
							<view class="value">{{orderDetail.otherPrice}}元</view>
						</view>
						<view class="item between">
							<view class="title">其他费用备注</view>
							<view class="value" style="font-size: 26rpx;">{{orderDetail.otherPriceContent}}</view>
						</view>
						<view class="item between">
							<view class="title">共计</view>
							<view class="value">
								{{orderDetail.totalPrice}}元
							</view>
						</view>
					</view>
				</uni-section>
				
				<uni-section title="物流信息" type="line" v-if="orderDetail.type > 5 && orderDetail.type !== 7">
					<view class="copy" @click="copyOrder(orderDetail.trackingNumber)">复制单号</view>
					<view class="costForm">
						<view class="item between">
							<view class="title">物流公司</view>
							<view class="value">{{orderDetail.wlcompany}}</view>
						</view>
						<view class="item between">
							<view class="title">物流单号</view>
							<view class="value" style="font-size: 26rpx;">{{orderDetail.trackingNumber}}</view>
						</view>
						<view class="item between">
							<view class="title">物流备注</view>
							<view class="value" style="font-size: 26rpx">{{orderDetail.wlRemarks}}</view>
						</view>
					</view>
				</uni-section>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: false,
				query: {},
				addressDetail: null,
				orderDetail: {},
				mercariTypeMap: {
					1: "付款成功，等待入库",
					2: "已入库，长按订单可合并发货",
					3: "已申请发货，等待计算尾款",
					4: "待付尾款",
					5: "待发货",
					6: "已发货",
					7: "已退款",
				},
				yahooTypeMap: {
					1: "最高出价中，等待竞拍结束",
					2: "竞拍成功，请补差价",
					3: "竞拍失败",
					4: "付款成功，等待入库",
					5: "已入库，长按订单可合并发货",
					6: "已申请发货，等待计算尾款",
					7: "待付尾款",
					8: "待发货",
					9: "已发货",
					10: "竞价被超越",
					100: "预约出价中，等待竞拍结束"
				},
			}
		},
		onLoad(query) {
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.query = query
			this.getDetail()
		},
		methods: {
			toGoodDetail(val){
				if(this.query.type === '2'){
					wx.navigateTo({
						url: '/pages/mercari/detail?id=' + (val || this.orderDetail.goodCode)
					})
				}else{
					wx.navigateTo({
						url: `/pages/yahoo/detail?code=${val || this.orderDetail.goodcode}`,
					})
				}
			},
			getDetail(){
				if(this.query.type === '1'){
					this.queryYahooOrderDetail()
				}else{
					this.queryOrderDetail()
				}
			},
			copyOrder(val){
				wx.setClipboardData({
					data: val,
					success: ()=>{
						wx.showToast({
							title: '复制成功'
						})
					}
				})
			},
			async queryYahooOrderDetail(){
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					id: this.query.id,
					addressid: this.query.addressid,
				}
				const res = await this.$api.queryYahooOrderDetail(params)
				const { data } = res.data
				
				console.log(data.userAddress)
				
				if(data.userAddress){
					data.userAddress.region = data.userAddress.address.split('----')[0].replace(/,/g, ' ')
					data.userAddress.address = data.userAddress.address.split('----')[1]
				}
				
				data.orderDetile.totalPrice = (
								(data.orderDetile.dgPrice * 100)
								+ 
								(data.orderDetile.jpyunfei * 100)
								+ 
								(data.orderDetile.gjyunfei * 100)
								 + 
								(data.orderDetile.cangcufei * 100)
								 + 
								(data.orderDetile.baozhuangfei * 100)
								 + 
								(data.orderDetile.otherPrice * 100)
								) / 100
				this.addressDetail = data.userAddress
				data.orderDetile.children = data.childOrder || []
				this.orderDetail = data.orderDetile
				wx.hideLoading()
				this.loading = false
			},
			async queryOrderDetail(){
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					id: this.query.id,
					addressid: this.query.addressid,
				}
				const res = await this.$api.queryOrderDetail(params)
				const { data } = res.data
				
				if(data.userAddress){
					data.userAddress.region = data.userAddress.address.split('----')[0].replace(/,/g, ' ')
					data.userAddress.address = data.userAddress.address.split('----')[1]
				}
				
				data.orderDetile.totalPrice = (
								(data.orderDetile.dgPrice * 100)
								+ 
								(data.orderDetile.jpyunfei * 100)
								+ 
								(data.orderDetile.gjyunfei * 100)
								 + 
								(data.orderDetile.cangcufei * 100)
								 + 
								(data.orderDetile.baozhuangfei * 100)
								 + 
								(data.orderDetile.otherPrice * 100)
								) / 100
				this.addressDetail = data.userAddress
				data.orderDetile.children = data.childOrder || []
				this.orderDetail = data.orderDetile
				wx.hideLoading()
				this.loading = false
			},
		}
	}
</script>

<style lang="scss">
	.page{
		padding-bottom: 50px;
	}
	.header{
		margin-top: 30rpx;
		padding: 0 20rpx;
		.copy{
			padding: 4rpx 8rpx;
			border: 1px solid #e5e5e5;
			color: #848282;
			font-size: 26rpx;
		}
	}
	.address{
		box-shadow: 0rpx 2rpx 5rpx 3rpx rgba(0,0,0,0.07);
		border-radius: 20rpx;
		padding: 20rpx;
		background: #FFF;
		margin-bottom: 30rpx;
		height: 130rpx;
		width: 710rpx;
		.container{
			width: 710rpx;
		}
		.itemHeader{
			color: #848282;
			padding: 0 20rpx;
			box-sizing: border-box;
			width: 100%;
			font-size: 26rpx;
			.copy{
				padding: 4rpx 8rpx;
				border: 1px solid #e5e5e5;
			}
			radio .wx-radio-input{
				width: 30rpx;
				height: 30rpx;
			}
			.isDefalt{
				color: #f9bf3b;
			}
		}
		.itemNav{
			width: 100%;
			margin-top: 10rpx;
		}
		.info{
			width: 100%;
			margin-left: 30rpx;
			font-size: 28rpx;
			justify-content: space-around;
			align-items: flex-start;
			.title{
				margin-top: 20rpx;
				font-size: 28rpx;
				height: 80rpx;
				width: 90%;
				word-wrap: break-word;
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
				margin: 0 10rpx;
			}
			.status{
				color: red;
			}
		}
	}
	.goodItem{
		box-shadow: 0rpx 2rpx 5rpx 3rpx rgba(0,0,0,0.07);
		border-radius: 20rpx;
		padding: 20rpx;
		background: #FFF;
		margin-bottom: 30rpx;
		// height: 310rpx;
		width: 710rpx;
		.goodItem{
			width: 650rpx;
			.container{
				width: 650rpx;
			}
			image{
				height: 190rpx;
				width: 190rpx;
			}
		}
		.container{
			width: 710rpx;
		}
		image{
			height: 250rpx;
			width: 250rpx;
		}
		.itemHeader{
			width: 100%;
			font-size: 26rpx;
			.copy{
				padding: 4rpx 8rpx;
				border: 1px solid #e5e5e5;
			}
		}
		.itemNav{
			width: 100%;
			margin-top: 10rpx;
		}
		.info{
			width: 430rpx;
			margin-left: 30rpx;
			font-size: 26rpx;
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
			}
			.price text{
				font-size: 36rpx;
				color: red;
				margin: 0 10rpx;
			}
			.status{
				font-size: 22rpx;
				width: 100%;
				view:last-child{
					color: red;
				}
			}
		}
	}
	
	
	.costForm{
		margin-top: 50rpx;
		border-bottom: 1px solid #e5e5e5;
		.item{
			padding: 0 20px;
			align-items: flex-start;
			margin-bottom: 20rpx;
			.value{
				text-align: right;
				width: 300rpx;
			}
		}
	}
	
	
	
	::v-deep .uni-section-header__decoration{
		background-color: #f5d45b !important;
	}
	
	.orderInfoDetail{
		position: relative;
		.orderStatus{
			font-size: 24rpx;
			position: absolute;
			right: 28rpx;
			top: 28rpx;
			color: red;
		}
	}
</style>
