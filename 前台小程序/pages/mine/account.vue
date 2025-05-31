<template>
	<view class="collect">
		<view class="header column">
			<!-- <view class="title">余额: {{balance || 0}}円 ( {{balance_rmb}} 元)</view> -->
			<view class="title center" style="margin-top: 20rpx">押金: {{deposit || 0}}元</view>
			<view class="value"> </view>
			<view class="rate column">
				<view class="title">平台汇率</view>
				<view class="value">{{rate || ''}}</view>
			</view>
		</view>
		
		<!-- <view class="tabs around">
			<view :class="['tabItem', currentTab === item.id ? 'active' : '']"
				v-for="(item, index) in tabList" :key="index" @click="choosetab(item.id)">
				{{item.title}}
			</view>
		</view> -->
		
		<view class="list">
			<view class="item between" v-for="(item, index) in list" :key="index">
				<view class="type type1">押金</view>
				<view class="money money1">{{item.balance}}元</view>
				<view class="time">{{item.date}}</view>
				<view class="btn btn2" @click="thaw(item)" v-if="item.type === 0">申请解冻</view>
				<view class="money" v-if="item.type === 1">申请中</view>
				<view class="money" v-if="item.type === 2">已解冻</view>
				<view class="btn btn2" @click="thaw(item)" v-if="item.type === 3">重新申请</view>
			</view>
			<view class="tips" v-if="list.length === 0">空空如也</view>
		</view>
		
		<view class="btns around">
			<!-- <view class="btn" @click="showCharge">储值</view> -->
			<view class="btn" @click="payDeposit">交押金</view>
			<!-- <view class="btn" @click="thaw" v-if="deposit > 0"> 申请解冻</view>  -->
			<!-- <view class="btn" @click="showWithdraw">提现</view> -->
		</view>
		
		<uni-popup ref="inputDialog" type="dialog">
			<uni-popup-dialog ref="inputClose"  mode="input" title="储值" :value="accountValue" :before-close="true"
				inputType="number" placeholder="请输入金额" @close="dialogInputClose" @confirm="dialogInputConfirm">
			</uni-popup-dialog>
		</uni-popup>
		
		<uni-popup ref="withdrawDialog" type="dialog">
			<uni-popup-dialog ref="withdrawClose"  mode="input" title="提现" value="" :before-close="true"
				inputType="number" placeholder="请输入金额" @close="withdrawClose" @confirm="withdrawConfirm">
			</uni-popup-dialog>
		</uni-popup>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
				tabList: [
					{id: 0, title: '全部'},
					{id: 1, title: '储值'},
					{id: 2, title: '消费'},
					{id: 3, title: '提现'},
					{id: 4, title: '解冻'},
				],
				currentTab: 0,
				list: [],
				userInfo: {},
				balance: '',
				jpbalance: '',
				balance_rmb: '',
				rate: null,
				accountValue: '',
				deposit: 0,
			}
		},
		onLoad() {
				this.userInfo = wx.getStorageSync('userInfo') || {}
				this.rate = wx.getStorageSync('rate') || null
				this.selectUserBalance()
				this.queryUserYJLog()
		},
		methods: {
			showWithdraw(){
				this.$refs.withdrawDialog.open('center')
			},
			async thaw(item){
				wx.showLoading({title: '操作中'})
				const params = {
					userid: this.userInfo.userid,
					transactionid: item.transactionid
				}
				const res = await this.$api.jdyj(params)
				const {code} = res.data
				if(code === 0){
					wx.showToast({
						title: '已申请，等待管理员审核',
						icon: 'none'
					})
					setTimeout(()=>{
						this.queryUserYJLog()
					}, 1000)
				}
			},
			async payDeposit(){
				wx.showLoading({title: '支付中'})
				const params = {
					userid: this.userInfo.userid,
					// balance: 0.01,
					balance: 2000,
					jpbalance: parseInt(2000 / this.rate)
				}
				const res = await this.$api.payYjBalance(params)
				console.log('res', res.data)
				const {timeStamp, nonceStr, signType, paySign} = res.data.data
				wx.requestPayment({
					timeStamp: timeStamp,
					nonceStr: nonceStr,
					package: res.data.data.package,
					signType: signType,
					paySign: paySign,
					success: (res)=>{
						console.log('success', res)
						this.$refs.inputDialog.close()
						this.selectUserBalance()
						this.queryUserYJLog()
						// this.usertx()
						wx.hideLoading()
					},
					fail: (res)=>{
						console.log('fail', res)
						wx.hideLoading()
					}
				})
			},
			async selectUserBlUpdata(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					type: this.currentTab
				}
				const res = await this.$api.selectUserBlUpdata(params)
				const { data } = res.data
				this.list = data
				wx.hideLoading()
			},
			async queryUserYJLog(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid,
					type: '',
					pageNumber: 1,
					pageSize: 50
				}
				const res = await this.$api.queryUserYJLog(params)
				const { data } = res.data
				this.list = data.list
				wx.hideLoading()
			},
			async selectUserBalance(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid
				}
				const res = await this.$api.selectUserBalance(params)
				const { data } = res.data
				console.log('data', data)
				this.balance = data.balance
				this.balance_rmb = parseInt(parseInt(data.balance * 100) * parseInt(this.rate * 100) / 10000 * 100) / 100
				// console.log(parseInt(data.balance * 100) * parseInt(this.rate * 100))
				// console.log(parseInt(data.balance * 100) * parseInt(this.rate * 100) / 10000)
				// console.log(parseInt(parseInt(data.balance * 100) * parseInt(this.rate * 100) / 10000 * 100))
				// console.log(parseInt(parseInt(data.balance * 100) * parseInt(this.rate * 100) / 10000 * 100) / 100)
				this.deposit = data.yjbalance
				wx.hideLoading()
			},
			choosetab(id){
				this.currentTab = id
				this.selectUserBlUpdata()
			},
			showCharge(){
				this.$refs.inputDialog.open('center')
			},
			async withdrawConfirm(val){
				wx.showLoading()
				if(/\D/ig.test(val)){
					wx.showToast({
						title: '只能输入整数',
						icon: 'none'
					})
					return false
				}
				if(val * 1 > this.balance){
					wx.showToast({
						title: '提现金额大于余额',
						icon: 'none'
					})
					return false
				}
				const params = {
					userid: this.userInfo.userid,
					balance: parseInt(val),
				}
				const res = await this.$api.createTx(params)
				console.log('res.data', res.data)
				const { code } = res.data
				if(code === 0){
					wx.hideLoading()
					wx.showToast({
						title: '申请提现成功',
						icon: 'none'
					})
					this.$refs.withdrawDialog.close()
					setTimeout(()=>{
						this.selectUserBalance()
						this.selectUserBlUpdata()
					}, 1000);
				}
			},
			async dialogInputConfirm(val){
				console.log(val)
				if(/\D/ig.test(val)){
					wx.showToast({
						title: '只能输入整数',
						icon: 'none'
					})
					return false
				}
				const params = {
					userid: this.userInfo.userid,
					balance: parseInt(val) * (this.rate * 1),
					// balance: 0.01,
					jpbalance: parseInt(val)
				}
				const res = await this.$api.payBalance(params)
				console.log('res', res.data.data)
				const {timeStamp, nonceStr, signType, paySign} = res.data.data
				wx.requestPayment({
					timeStamp: timeStamp,
					nonceStr: nonceStr,
					package: res.data.data.package,
					signType: signType,
					paySign: paySign,
					success: (res)=>{
						console.log('success', res)
						this.$refs.inputDialog.close()
						this.selectUserBalance()
						this.selectUserBlUpdata()
					},
					fail: (res)=>{
						console.log('fail', res)
					}
				})
			},
			dialogInputClose(){
				this.accountValue = ''
				this.$refs.inputClose.value = ''
				this.$refs.inputDialog.close()
			},
			withdrawClose(){
				this.$refs.withdrawClose.value = ''
				this.$refs.withdrawDialog.close()
			},
			forDate(timestamp){
			    const date = new Date(timestamp);
			    const y = date.getFullYear();
			    let m = date.getMonth() + 1;
			    m = m < 10 ? '0' + m : m;
			    let d = date.getDate();
			    d = d < 10 ? ('0' + d) : d;
			    let h = date.getHours();
			    h = h < 10 ? ('0' + h) : h;
			    let mm = date.getMinutes();
			    mm = mm < 10 ? ('0' + mm) : mm;
			    let ss = date.getSeconds();
			    ss= ss < 10 ? ('0' + ss) : ss;
			    return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + ss
			},
		}
	}
</script>

<style lang="scss">
	page{
		background: #f7f7f7;
	}
	.header{
		position: relative;
		width: 750rpx;
		background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
		height: 200rpx;
		color: #FFF;
		.value{
			margin-top: 20rpx;
		}
	}
	.tabs{
		margin-top: 20rpx;
		background: #FFF;
		width: 100vw;
		.tabItem{
			box-sizing: border-box;
			width: 25%;
			text-align: center;
			height: 70rpx;
			line-height: 70rpx;
			border: 1px solid #e5e5e5
		}
		.active{
			background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
			color: #FFF;
		}
	}
	.list{
		background-color: #FFF;
		margin-top: 20rpx;
		padding: 30rpx;
		padding-bottom: 150rpx;
		.item{
			padding: 15rpx 0;
			.type{
				height: 100rpx;
				line-height: 100rpx;
				width: 100rpx;
				text-align: center;
				border-radius: 50%;
				color: #FFF;
				background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
			}
			.type2{
				background: red;
			}
			.type3{
				background: green;
			}
			.money{
				width: 100rpx;
				text-align: center;
				color: #f9bf3b;
				font-size: 28rpx;
			}
			.money2{
				color: red;
			}
			.money3{
				color: green;
			}
			.time{
				width: 280rpx;
				text-align: center;
				font-size: 26rpx;
			}
		}
	}
	.btns{
		position: fixed;
		bottom: 0;
		left: 0;
		width: 750rpx;
		height: 130rpx;
		background: #fff;
	}
	.btn{
		width: 150rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		color: #FFF;
		border-radius: 20rpx;
		background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
	
	}
	
	.btn2{
		font-size: 26rpx;
		width: 140rpx;
		height: 50rpx;
		line-height: 50rpx;
		text-align: center;
		color: #FFF;
		border-radius: 9rpx;
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
	.rate{
		color: #000;
		position: absolute;
		right: 10rpx;
		top: 10rpx;
		font-size: 24rpx;
		.value{
			margin-top: 10rpx;
		}
	}
	.rate2{
		left: 10rpx;
		width: 90rpx;
	}
	// .thaw{
	// 	font-size: 24rpx;
	// 	border: 1px solid #000;
	// 	padding: 2px 5px;
	// 	margin-left: 20rpx
	// }
</style>
