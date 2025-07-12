<template>
	<view class="page collect">
		<Title :title="orderTypeMap[orderType]"/>
		
		<view class="tabs between typeTab">
			<view :class="['tabItem', orderType === item.id ? 'active' : '']" 
				v-for="(item, index) in orderTypeList" :key="index" @click="chooseType(item.id)">
				{{item.text}}
			</view>
		</view>
		<view class="list" v-if="currentTab === 1">
			<view :class="['item', selectMode ? 'center selectMode' : '']"
				v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)" @longpress="selectOrder(item)">
				<view class="radio" v-if="selectMode">
					<radio :checked="item.isSelect" :disabled="item.type !== 5" @click.navtive="onSelectChange(item)"/>
				</view>
				<view class="container">
					<view class="itemNav flex">
						<image :src="item.imageurl"></image>
						<!-- <image src="https://img.alicdn.com/imgextra/i3/2200632700688/O1CN01guYXUz1Gx9KuHSeRB_!!2200632700688-0-scmitem6000.jpg"></image> -->
						
						<view class="info column">
							<view :class="['title', orderType === '10' ? 'disabled' : '']">{{item.title}}</view>
							<view :class="['between time', orderType === '1' ? 'time2' : '']" v-if="orderType === '100' || orderType === '1'">
								<view class="remainTime">剩余时间：5天12:39:39</view>
							</view>
							
							<view :class="['between time3']" v-if="orderType === '2'">
								<view class="remainTime">您已得标</view>
							</view>
							<view :class="['between time2']" v-if="orderType === '10'">
								<view class="remainTime"><text>竞拍失败</text>结束时间:2025-5-18 22:22</view>
							</view>
							<view class="auctionTime" v-if="orderType === '1' || orderType === '100'">已出价{{'88'}}次数</view>
							<view class="auctionTime" v-if="orderType === '2' || orderType === '10'">出价ID: XXX</view>
							<!-- <view class="bottom between">
								<view class="price">订单总价<text>111{{item.totalPrice}}</text> 円</view>
							</view> -->
							<!-- <view class="bottom between">
								<view class="price" v-if="item.ratetype === 0">出价价格<text>111{{item.price}}</text> 円</view>
								<view class="price" v-if="item.ratetype === 1">商品价格(含税)<text>111{{parseInt(item.price * 110) / 100}}</text> 円</view>
							</view> -->
							<view class="bottom between">
								<view class="price" v-if="orderType === '100'">预约出价：<text>{{item.price}}円</text> </view>
								<view class="price" v-if="orderType === '1' || orderType === '2'">出价金额：<text>{{item.price}}円</text> </view>
								<view class="price" v-if="orderType === '10'">出价金额：{{item.price}}円</view>
							</view>
							<image
								class="icon"
								src="@/static/icon/38.png"
							>
							<image
								v-if="orderType === '2'"
								class="tag"
								src="@/static/icon/39.png"
							>
						</view>
					</view>
				</view>
				<view class="item" v-for="it in item.children" :key="it.id" v-if="item.showChildren">
					<view class="container">
						<view class="itemHeader between">
							<view class="no">订单号：{{it.orderid}}</view>
							<view class="copy" @click.stop="copyOrder(it.orderid)">复制</view>
						</view>
						<view class="itemNav flex">
							<image :src="it.imageurl"></image>
							<view class="info column">
								<view class="title">{{it.title}}</view>
								<view class="price">数量：x 1</view>
								<view class="bottom between">
									<view class="price" v-if="it.ratetype === 0">商品价格<text>{{it.price}}</text> 円</view>
									<view class="price" v-if="it.ratetype === 1">商品价格(含税)<text>{{parseInt(it.price * 110) / 100}}</text> 円</view>
								</view>
								<view class="status between">
									<view>订单状态：</view>
									<view>{{yahooTypeMap[item.type]}}</view>
								</view>
							</view>
						</view>
					</view>
				</view>
				<!-- <view class="options center" v-if="!selectMode">
					<view class="btn" @click.stop="showChildren(item)" v-if="item.children && item.children.length>0 && !item.showChildren">显示子订单</view>
					<view class="btn" @click.stop="showChildren(item)" v-if="item.children && item.children.length>0 && item.showChildren">隐藏子订单</view>
					<view class="btn" @click.stop="showReducePrice(item)" v-if="item.type === 2">付款</view>
					<view class="btn" @click.stop="toPath('/pages/mercari/requestSend?type=1&id=' + item.id)" v-if="item.type === 5">申请单独发货</view>
					<view class="btn" @click.stop="showCostDetail(item)" v-if="item.type === 7">支付尾款</view>
					<view class="btn" @click.stop="deleteOrder(item, 1)" v-if="item.type === 3">删除订单</view>
				</view> -->
			</view>
			<view class="tips" v-if="list.length === 0">空空如也</view>
		</view>
		<view class="selectModeBtns around" v-if="selectMode">
			<view @click="cancelSelect">取消选择</view>
			<view @click="submitSelect">合并发货</view>
		</view>
		
		<uni-popup ref="popupDialog2">
			<view class="popup-content popup-content2">
				<view class="header center">
					<p class="dialogTitle">尾款详情</p>
				</view>
				<view class="costForm">
					<view class="item between">
						<view class="title">日本邮费</view>
						<view class="value">{{currentOrder.jpyunfei}}元</view>
					</view>
					<view class="item between">
						<view class="title">国际运费</view>
						<view class="value">{{currentOrder.gjyunfei}}元</view>
					</view>
					<view class="item between">
						<view class="title">仓储费</view>
						<view class="value">{{currentOrder.cangcufei}}元</view>
					</view>
					<view class="item between">
						<view class="title">包装费</view>
						<view class="value">{{currentOrder.baozhuangfei}}元</view>
					</view>
					<view class="item between">
						<view class="title">其他费用</view>
						<view class="value">{{currentOrder.otherPrice}}元</view>
					</view>
					<view class="item between">
						<view class="title">其他费用备注</view>
						<view class="value" style="font-size: 26rpx;">{{currentOrder.otherPriceContent}}</view>
					</view>
					<view class="item between">
						<view class="title">共计</view>
						<view class="value">
							{{currentOrder.totalPrice}}元
						</view>
					</view>
				</view>
				<view class="submitBtn" @click="payDetailCost">支付</view>
			</view>
		</uni-popup>
		
		<!-- 竞拍成功后的付款 -->
		<uni-popup ref="popupDialog3">
			<view class="popup-content popup-content2">
				<view class="header center">
					<p class="dialogTitle">付款详情</p>
				</view>
				<view class="costForm">
					<view class="item between">
						<view class="title" v-if="currentOrder.ratetype !== 1">商品价格</view>
						<view class="value">{{currentOrder.price}}円({{ currentOrder.price_rmb }}元)</view>
					</view>
					<view class="item between" v-if="currentOrder.ratetype === 1">
						<view class="title">商品价格(含税)</view>
						<view class="value">{{currentOrder.price}}円({{ currentOrder.item.price_rmb }}元)</view>
					</view>
					<view class="item between">
						<view class="title">代购费</view>
						<view class="value">{{buyingAgentFee}}元</view>
					</view>
					<view class="item between">
						<view class="title">合计</view>
						<view class="value">{{firstTotal}}元</view>
					</view>
					<view class="item between" v-if="currentOrder.ratetype === 0">
						<view class="title">备注</view>
						<view class="value" style="font-size: 26rpx;">{{currentOrder.otherPriceContent}}</view>
					</view>
					<view class="item between" v-if="currentOrder.ratetype === 1">
						<view class="title">备注</view>
						<view class="value" style="font-size: 26rpx;">此商品含税，需支付含税价格</view>
					</view>
				</view>
				<view class="submitBtn" @click="fixPrice">支付</view>
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
				orderType: '',
				orderTypeMap: {
					'100': '预约出价',
					'1': '竞拍中',
					'2': '已得标',
					'10': '竞拍失败',
				},
				orderTypeList: [
					{id: '100', text: '预约出价'},
					{id: '1', text: '竞拍中'},
					{id: '2', text: '已得标'},
					{id: '10', text: '竞拍失败'},
				],
				buyingAgentFee: '', // 代购费
				firstTotal: '', // 雅虎第一次支付总计
				tabList: [
					{id: 1, title: "雅虎"},
					{id: 2, title: "煤炉"},
				],
				currentTab: 1,
				list: [],
				addressList: [],
				currentAddressId: null,
				page: 1,
				size: 50,
				selectMode: false,
				showAddressModel: false,
				
				userInfo: {},
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
				
				
				yahooOrderList: [],
				currentYahooType: 0,
				yahooTypeList: [
					{id: 0, title: '全部'},
					{id: 1, title: '出价中'},
					{id: 10, title: '被超越订单'},
					{id: 2, title: '成功订单'},
					{id: 4, title: '待入库'},
					{id: 5, title: '已入库'},
					{id: 6, title: '申请发货'},
					{id: 7, title: '待付尾款'},
					{id: 8, title: '待发货'},
					{id: 9, title: '已发货'},
					{id: 100, title: '预约出价'},
				],
				mercariOrderList: [],
				currentMercariType: 0,
				mercariTypeList: [
					{id: 0, title: '全部'},
					{id: 1, title: '待入库'},
					{id: 2, title: '已入库'},
					{id: 3, title: '申请发货'},
					{id: 4, title: '待付尾款'},
					{id: 5, title: '待发货'},
					{id: 6, title: '已发货'},
					{id: 7, title: '已退款'},
				],
				
				currentOrder: {},
				rate: null,
				loading: false
			}
		},
		onLoad(query) {
			console.log(query)
			this.orderType = query.statusType
			this.rate = wx.getStorageSync('rate') || null
			this.userInfo = wx.getStorageSync('userInfo') || {}
			if(query.platformType){
				this.currentTab = query.platformType*1
			}
			if(query.statusType){
				if(this.currentTab === 1) this.currentYahooType = query.statusType*1
				if(this.currentTab === 2) this.currentMercariType = query.statusType*1
				
			}
		},
		onShow(){
			this.getList()
		},
		async onPullDownRefresh(){
			await this.getList()
			wx.stopPullDownRefresh()
		},
		methods: {
			async deleteOrder(item, type){
				wx.showLoading({title: '操作中'})
				const params = {
					id: item.id,
				}
				let res = null
				if(this.currentTab === 1){
					res = await this.$api.deleteYahooOrder(params)
				}else{
					res = await this.$api.deleteMercariOrder(params)
				}
				const { code, msg } = res.data
				if(code === 0){
					wx.showToast({
						title: '操作成功',
						icon: 'none'
					})
					setTimeout(()=>{
						this.getList()
					}, 1000);
				}else{
					wx.showToast({
						title: msg,
						icon: 'none'
					})
				}
			},
			getList(){
				if(this.currentTab === 1){
					this.queryYahooOrderList()
				}else{
					this.queryMercariOrderList()
				}
			},
			async fixPrice(){
				const item = this.currentOrder
				wx.showLoading({title: '支付中'})
				const params = {
					id: item.id,
					userid: this.userInfo.userid,
					price: this.firstTotal
				}
				const res = await this.$api.fixYahooPrice2(params)
				const { code, msg, data } = res.data
				if(code === 0){
					const {timeStamp, nonceStr, signType, paySign} = data
					wx.requestPayment({
						timeStamp: timeStamp,
						nonceStr: nonceStr,
						package: data.package,
						signType: signType,
						paySign: paySign,
						success: (res)=>{
							wx.hideLoading()
							this.$refs.popupDialog3.close()
							wx.showToast({
								title: '支付成功',
								icon: 'none'
							})
							setTimeout(()=>{
								this.queryYahooOrderList()
							}, 1000);
						},
						fail: (res)=>{
							wx.showToast({
								title: '支付取消',
								icon: 'none'
							})
							wx.hideLoading()
							this.$refs.popupDialog3.close()
						}
					})
				}else{
					wx.showToast({
						title: msg,
						icon: 'none'
					})
				}
			},
			showAddress(item){
				this.currentOrder = item
				this.getAddress()
			},
			async payDetailCost(){
				wx.showLoading({title: '加载中'})
				const params = {
					id: this.currentOrder.id,
					userid: this.userInfo.userid,
					price: this.currentOrder.totalPrice
				}
				let res = null
				if(this.currentTab === 1){
					res = await this.$api.yahoopayFinalPrice(params)
				}else{
					res = await this.$api.payFinalPrice(params)
				}
				const { data } = res.data
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
							this.$refs.popupDialog2.close()
							this.getList()
						}, 1500)
					},
					fail: (res)=>{
						wx.hideLoading()
						console.log('fail', res)
					}
				})
			},
			showCostDetail(item){
				item.totalPrice = (
								(item.dgPrice * 100)
								+ 
								(item.jpyunfei * 100)
								+ 
								(item.gjyunfei * 100)
								 + 
								(item.cangcufei * 100)
								 + 
								(item.baozhuangfei * 100)
								 + 
								(item.otherPrice * 100)
								) / 100
				this.currentOrder = item
				this.$refs.popupDialog2.open()
			},
			
			// <!-- 竞拍成功后的付款 -->
			showReducePrice(item){
				this.currentOrder = item
				this.currentOrder.price_rmb = parseInt(this.currentOrder.price * this.rate * 100) / 100
				let price = 0
				if(this.currentOrder.ratetype === 1){ // 含税
					price = parseInt(this.currentOrder.price * 110) / 100
				}else{
					price = this.currentOrder.price
				}
				// 处理代购费
				if(price <= 50000){
					this.buyingAgentFee = 40
				}else if(price > 50000 && price <= 200000){
					this.buyingAgentFee = 100
				}else{
					this.buyingAgentFee = price * 3 / 100 * this.rate
				}
				
				this.firstTotal = (item.price_rmb * 100 + this.buyingAgentFee * 100) / 100
				
				this.$refs.popupDialog3.open()
			},
			async queryMercariOrderList(){
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					// userid: 141,
					type: this.currentMercariType,
				}
				const res = await this.$api.queryMercariOrderList(params)
				const { data } = res.data
				data?.forEach(item=>{
					let total = 0
					item.children?.forEach(it=>{
						total += (it.price * 100)
					})
					total += (item.price * 100)
					item.totalPrice = total / 100
					item.isSelect = false
					item.showChildren = false
				})
				this.list = data
				this.mercariOrderList = data
				this.loading = false
				wx.hideLoading()
			},
			async queryYahooOrderList(){
				this.loading = true
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					// userid: 21,
					type: this.orderType,
				}
				const res = await this.$api.queryYahooOrderList(params)
				const { data } = res.data
				data?.forEach(item=>{
					console.log('paytime', item.paytime)
					let total = 0
					item.children?.forEach(it=>{
						if(it.ratetype === 1){
							total += (parseInt(it.price * 110) / 100 * 100)
						}else{
							total += (it.price * 100)
						}
					})
					if(item.ratetype === 1){
						total += (parseInt(item.price * 110) / 100 * 100)
					}else{
						total += (item.price * 100)
					}
					item.totalPrice = total / 100
					item.isSelect = false
					item.showChildren = false
				})
				this.list = data
				// this.list = [{}]
				this.yahooOrderList = data
				wx.hideLoading()
				this.loading = false
			},
			choosetab(id){
				if(this.loading) return false
				this.list = []
				this.page = 1
				this.size = 50
				this.currentTab = id
				this.getList()
			},
			chooseType(id){
				if(this.loading) return false
				this.orderType = id
				this.queryYahooOrderList()
				// if(id === 0){
				// 	this.list = this.yahooOrderList
				// }else{
				// 	this.list = this.yahooOrderList.filter(item=>item.type === id)
				// }
			},
			chooseMercariType(id){
				if(this.loading) return false
				this.currentMercariType = id
				this.queryMercariOrderList()
				// if(id === 0){
				// 	this.list = this.mercariOrderList
				// }else{
				// 	this.list = this.mercariOrderList.filter(item=>item.type === id)
				// }
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
			favClick(item) {
				item.isCollect = !item.isCollect
			},
			chooseItem(item){
				return false
				if(this.selectMode) return false
				if(this.currentTab === 1){
					wx.navigateTo({
						url: `/pages/mine/orderDetail?type=1&id=${item.id}&addressid=${item.addressid || ''}`
					})
				}else{
					wx.navigateTo({
						url: `/pages/mine/orderDetail?type=2&id=${item.id}&addressid=${item.addressid || ''}`
					})
				}
			},
			selectOrder(item){
				console.log('长按，等增加判断逻辑，只有已付款状态下才能合并发货')
				// this.selectMode = true
				// if(item.type === 2) item.isSelect = true
			},
			onSelectChange(item){
				item.isSelect = !item.isSelect
			},
			cancelSelect(){
				this.selectMode = false
				this.list.forEach(item=>{
					item.isSelect = false
				})
			},
			submitSelect(){
				this.selectMode = false
				let arr = []
				this.list?.forEach(item=>{
					if(item.isSelect){
						arr.push(item.id)
					}
				})
				if(arr.length === 0){
					wx.showToast({
						title: '未选择订单',
						icon: 'none'
					})
					return false
				}
				const id = arr.join('-')
				this.toPath(`/pages/mercari/requestSend?type=${this.currentTab}&id=${id}`)
			},
			toPath(url){
				wx.navigateTo({
					url,
				})
			},
			showChildren(item){
				item.showChildren = !item.showChildren
			}
		}
	}
</script>

<style lang="scss">
	.collect{
		.tabs{
			margin: 60rpx auto 30rpx;
			width: 670rpx;
			.tabItem{
				font-size: 30rpx;
				text-align: center;
				height: 60rpx;
				line-height: 60rpx;
				color: #ADADAD;
			}
			.active{
				color: #000;
			}
		}
	}
	.list{
		margin-top: 20rpx;
		margin-bottom: 150rpx;
		.item{
			box-shadow: 0rpx 2rpx 5rpx 3rpx rgba(0,0,0,0.07);
			border-radius: 20rpx;
			padding: 20rpx;
			background: #FFF;
			margin-bottom: 30rpx;
			// height: 310rpx;
			width: 710rpx;
			.item{
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
				width: 228rpx;
				height: 194rpx;
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
				position: relative;
				width: 450rpx;
				margin-left: 30rpx;
				font-size: 26rpx;
				justify-content: space-around;
				align-items: flex-start;
				.icon{
					width: 18rpx;
					height: 34rpx;
					position: absolute;
					z-index: 3;
					top: 0;
					bottom: 0;
					right: 0;
					margin: auto;
				}
				.tag{
					width: 234rpx;
					height: 158rpx;
					position: absolute;
					z-index: 2;
					bottom: 16rpx;
					right: 0;
				}
				.title{
					font-size: 28rpx;
					height: 70rpx;
					width: 100%;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow : ellipsis; 
				}
				.disabled{
					color: #ADADAD;
				}
				.time{
					width: 100%;
					font-size: 25rpx;
					color: #FF0000;
					margin-top: 20rpx;
				}
				.time2{
					color: #ADADAD;
					text{
						color: #FF0000;
					}
				}
				.time3{
					color: #004FFF
				}
				.auctionTime{
					margin: 4rpx 0;
					font-size: 25rpx;
					color: #ADADAD;
				}
				.bottom{
					width: 100%;
				}
				.price{
					font-size: 25rpx;
					color: #ADADAD;
				}
				.price text{
					color: #FF0000;
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
		.selectMode .container{
			width: 690rpx
		}
		.selectMode .itemHeader{
			width: 660rpx
		}
		.selectMode .info{
			width: 400rpx
		}
	}
	.selectModeBtns{
		background: rgba(0 ,0, 0, 0.3);
		position: fixed;
		bottom: 0;
		padding-bottom: 30rpx;
		left: 0;
		width: 100vw;
		height: 120rpx;
		view{
			text-align: center;
			color: #FFF;
			width: 270rpx;
			height: 90rpx;
			line-height: 90rpx;
			background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
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
	.tips2{
		height: 80rpx;
		line-height: 80rpx;
		width: 100%;
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
	}
	.addresslist{
		margin-top: 20rpx;
		margin-bottom: 30rpx;
		height: 650rpx;
		overflow-y: auto;
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
					color: #f9bf3b;
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
				.price{
					font-size: 24rpx;
				}
				.price text{
					font-size: 28rpx;
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
		color: #f9bf3b;
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
	.options{
		margin-top: 20rpx;
		justify-content: flex-end;
		font-size: 26rpx;
		.btn{
			color: #FFF;
			padding: 6rpx 10rpx;
			border-radius: 10rpx;
			background: #de9e0d;
			margin-left: 18rpx;
		}
	}
	.popup-content2{
		width: 500rpx;
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
</style>
