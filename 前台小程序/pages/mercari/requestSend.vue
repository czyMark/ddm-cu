<template>
	<view class="page">
		<uni-section title="商品信息" type="line" v-if="!loading && addressDetail">
			<view class="goodItem" v-for="(item, index) in goodList" :key="index">
				<view class="container">
					<view class="itemHeader between">
						<view class="no">订单号：{{item.orderid}}</view>
						<view class="copy" @click="copyOrder(item.orderid)">复制</view>
					</view>
					<view class="itemNav flex" @click="toGoodDetail(item)">
						<image :src="item.imageurl"></image>
						<view class="info column">
							<view class="title">{{item.title}}</view>
							<view class="price">数量：x 1</view>
							<view class="bottom between" v-if="query.type === '1'">
								<view class="price" v-if="item.ratetype === 1">支付价格(含税)<text>{{parseInt(item.price * 110) / 100}}</text> 円</view>
								<view class="price" v-else>支付价格<text>{{item.price}}</text> 円</view>
							</view>
							<view class="bottom between" v-if="query.type === '2'">
								<view class="price">支付价格<text>{{item.price}}</text> 元</view>
							</view>
							<view class="status between">
								<view>订单状态：</view>
								<view v-if="query.type === '2'">{{mercariTypeMap[item.type]}}</view>
								<view v-if="query.type === '1'">{{yahooTypeMap[item.type]}}</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</uni-section>
		
		<uni-section title="当前收货信息" type="line" v-if="!loading">
			<view class="address" v-if="addressDetail">
				<view class="container">
					<view class="itemNav flex">
						<view class="info column">
							<view class="bottom between">
								<view class="price">{{addressDetail.username}} {{addressDetail.phone}}</view>
							</view>
							<view class="title">{{addressDetail.region.replace(/,/g, ' ')}} {{addressDetail.address}}</view>
						</view>
					</view>
					<image src="@/static/switch.png" class="switch" @click="showAddress"></image>
				</view>
			</view>
			<view v-else>
				<button class="btn" @click="toPath('/pages/mine/address')">新增地址</button>
			</view>
		</uni-section>
		
		<uni-section title="选择运送路线" type="line">
			<view style="padding: 0 30rpx;">
				<uni-data-select
					v-model="routeValue"
					:localdata="postRoutes"
				></uni-data-select>
			</view>
		</uni-section>
		
		<uni-section title="备注" type="line">
			<view style="padding: 0 30rpx;">
				<uni-easyinput type="textarea" v-model="remark" placeholder="请输入内容"></uni-easyinput>
			</view>
		</uni-section>
		
		<button class="btn" @click="requestSend">申请发货</button>
		
		<uni-popup ref="popupDialog">
			<view class="popup-content">
				<view class="header between">
					<p></p>
					<p class="dialogTitle">请选择选择地址</p>
					<p class="dialogAdd" @click="toPath('/pages/mine/address')">新增</p>
				</view>
				<view class="addresslist">
					<view :class="['item', currentAddressId === item.id ? 'currentAddress' : '']"
						v-for="(item, index) in addressList" :key="index"
						@click="currentAddressId = item.id">
						<view class="container">
							<view class="itemNav flex">
								<view class="info column">
									<view class="bottom between">
										<view class="price">{{item.username}} {{item.phone}}</view>
										<view class="price">
											
										</view>
									</view>
									<view class="title">{{item.region.replace(/,/g, ' ')}} {{item.address}}</view>
								</view>
							</view>
							<view class="itemHeader between" v-if="item.defaultType === 1">
								<view :class="['no', item.defaultType === 1 ? 'isDefalt' : '']" @click="onDefaultChange(item)">
									默认地址
								</view>
							</view>
						</view>
					</view>
					<view class="tips2" v-if="addressList.length === 0">暂未添加地址</view>
				</view>
				<view class="submitBtn" @click="submitAddress">确认</view>
			</view>
		</uni-popup>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: false,
				query: {},
				addressDetail: null,
				goodList: [],
				mercariTypeMap: {
					1: "付款成功，等待入库",
					2: "已入库",
					3: "已申请发货，等待计算尾款",
					4: "待付尾款",
					5: "待发货",
					6: "已发货",
				},
				yahooTypeMap: {
					1: "最高出价中，等待竞拍结束",
					2: "竞拍成功，请补差价",
					3: "竞拍失败",
					4: "付款成功，等待入库",
					5: "已入库",
					6: "已申请发货，等待计算尾款",
					7: "待付尾款",
					8: "待发货",
					9: "已发货",
					10: "竞价被超越",
					100: "预约出价中，等待竞拍结束"
				},
				addressList: [],
				currentAddressId: '',
				postRoutes: [
					{ value: '包税包清关路线', text: "包税包清关路线" },
					{ value: '邮局EMS路线', text: "邮局EMS路线" },
					{ value: '邮局海运路线', text: "邮局海运路线" },
					{ value: 'UPS路线', text: "UPS路线" },
					{ value: '其它路线', text: "其它路线" },
				],
				routeValue: '',
				remark: '',
				idList: []
			}
		},
		onLoad(query) {
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.query = query
			console.log(this.query)
			this.idList = query.id.split('-')
			this.idList?.forEach(item=>{
				this.getDetail(item)
			})
		},
		onShow(){
			this.getAddress()
		},
		methods: {
			toGoodDetail(item){
				if(this.query.type === '2'){
					wx.navigateTo({
						url: '/pages/mercari/detail?id=' + item.goodCode
					})
				}else{
					wx.navigateTo({
						url: `/pages/yahoo/detail?code=${item.goodcode}`,
					})
				}
			},
			getDetail(id){
				if(this.query.type === '1'){
					this.queryYahooOrderDetail(id)
				}else{
					this.queryOrderDetail(id)
				}
			},
			
			async getAddress(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid
				}
				const res = await this.$api.selectByAduserid(params)
				const { data } = res.data
				console.log('data', data)
				data?.forEach(item=>{
					item.region = item.address.split('----')[0]
					item.address = item.address.split('----')[1]
					if(item.defaultType === 1){
						this.currentAddressId = item.id
						this.addressDetail = item
					}
				})
				this.addressList = data
				if(this.addressList.length > 0 && !this.currentAddressId){
					this.currentAddressId = this.addressList[0].id
				}
				wx.hideLoading()
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
			async queryYahooOrderDetail(id){
				console.log('id', id)
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					id: parseInt(id),
					addressid: this.query.addressid || '',
				}
				const res = await this.$api.queryYahooOrderDetail(params)
				const { data } = res.data
				
				this.goodList.push(data.orderDetile)
				wx.hideLoading()
				this.loading = false
			},
			async queryOrderDetail(id){
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					id,
					addressid: this.query.addressid || '',
				}
				const res = await this.$api.queryOrderDetail(params)
				const { data } = res.data
				this.goodList.push(data.orderDetile)
				wx.hideLoading()
				this.loading = false
			},
			showAddress(){
				this.$refs.popupDialog.open()
			},
			toPath(url){
				wx.navigateTo({
					url,
				})
			},
			submitAddress(){
				this.addressList?.forEach(item=>{
					if(item.id === this.currentAddressId) this.addressDetail = item
				})
				this.$refs.popupDialog.close()
				
			},
			async requestSend(){
				if(this.currentAddressId === ''){
					wx.showToast({
						title: '请选择收货地址',
						icon: 'none'
					})
					return false
				}
				if(this.routeValue === ''){
					wx.showToast({
						title: '请选择运送路线',
						icon: 'none'
					})
					return false
				}
				if(this.idList.length === 1){
					this.sendAlone()
				}else{
					this.sendMultiple()
				}
			},
			
			async sendAlone(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					order: [{
						id: this.query.id,
						orderid: this.query.orderid,
					}],
					addressid: this.currentAddressId,
					luxian: this.routeValue,
					lxRemarks: this.remark,
				}
				let res = null
				if(this.query.type === '1'){
					res = await this.$api.createYahoomergeOrder(params)
				}else{
					res = await this.$api.createmergeOrder(params)
				}
				const { code, msg } = res.data
				wx.hideLoading()
				if(code === 0){
					wx.showToast({
						title: '操作成功',
						icon: 'none'
					})
					setTimeout(()=>{
						wx.navigateBack({
						  delta: 1
						})
					}, 1000);
				}else{
					wx.showToast({
						title: msg,
						icon: 'none'
					})
				}
			},
			
			async sendMultiple(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					order: [],
					addressid: this.currentAddressId,
					luxian: this.routeValue,
					lxRemarks: this.remark,
				}
				this.idList?.forEach(item=>{
					params.order.push({
						id: item
					})
				})
				let res= null
				if(this.query.type === '1'){
					res = await this.$api.createYahoomergeOrder(params)
				}else{
					res = await this.$api.createmergeOrder(params)
				}
				const { code, msg } = res.data
				wx.hideLoading()
				if(code === 0){
					wx.showToast({
						title: '操作成功',
						icon: 'none'
					})
					setTimeout(()=>{
						wx.navigateBack(-1)
					}, 1000);
				}else{
					wx.showToast({
						title: msg,
						icon: 'none'
					})
				}
			}
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
			position: relative;
			.switch{
				position: absolute;
				width: 48rpx;
				height: 48rpx;
				top: -8rpx;
				right: 40rpx;
			}
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
				color: #f5d45b;
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
	
	
	.popup-content{
		background: #FFF;
		padding: 20rpx 50rpx;
		border-radius: 25rpx;
		width: 600rpx;
		position: relative;
	}
	.addresslist{
		margin-top: 20rpx;
		margin-bottom: 30rpx;
		height: 650rpx;
		overflow-y: auto;
		overflow-x: hidden;
		.item{
			box-shadow: 0rpx 2rpx 5rpx 3rpx rgba(0,0,0,0.07);
			border-radius: 20rpx;
			padding: 20rpx;
			background: #FFF;
			margin-bottom: 16rpx;
			height: 180rpx;
			width: 550rpx;
			border: 2px solid #eae6e6;
			.container{
				width: 570rpx;
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
					color: #f5d45b;
				}
			}
			.itemNav{
				width: 100%;
				margin-top: 10rpx;
			}
			.info{
				width: 100%;
				margin-left: 14rpx;
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
		.currentAddress{
			border: 2px solid orange;
		}
	}
	.popup-content{
		height: 800rpx;
		padding-bottom: 50rpx;
	}
	.dialogAdd{
		color: #f5d45b;
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
		background: linear-gradient(270deg, #f5d45b 0%, #eba82e 100%);
	}
	.options{
		margin-top: 20rpx;
		justify-content: flex-end;
		font-size: 26rpx;
		.btn{
			color: #FFF;
			padding: 6rpx 10rpx;
			border-radius: 10rpx;
			background: #f5d45b;
			margin-left: 18rpx;
		}
	}
	.popup-content2{
		width: 500rpx;
	}
	
	.btn{
		margin: 30rpx auto !important;
		width: 650rpx;
		height: 80rpx;
		line-height: 80rpx !important;
		text-align: center;
		color: #FFF;
		border-radius: 20rpx;
		background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
	}
	
	.copy{
		padding: 4rpx 8rpx;
		border: 1px solid #e5e5e5;
	}
	::v-deep .uni-section-header__decoration{
		background-color: #f5d45b !important;
	}
</style>
