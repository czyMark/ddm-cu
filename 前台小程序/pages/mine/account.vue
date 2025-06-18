<template>
	<view class="page">
		<Title :type="1" title="我的钱包"/>
		
		<view class="nav">
			<view class="moneyNav column">
				<view class="item between">
					<view class="label">账户总余额(总的余额):</view>
					<view class="value">
						88888
						<text>(JPY)</text>
					</view>
					<image
						class="icon"
						src="@/static/icon/74.png"
					>
				</view>
				<view class="item between">
					<view class="label">保证金(用于拍卖杠杆):</view>
					<view class="value">
						88888
						<text>(JPY)</text>
					</view>
					<image
						class="icon"
						src="@/static/icon/74.png"
					>
				</view>
				
				<view class="item between">
					<view class="label">可出价总金额:</view>
					<view class="value">
						88888
						<text>(JPY)</text>
					</view>
					<view class="icon"></view>
				</view>
				<view class="item between">
					<view class="label">剩余总出价金额:</view>
					<view class="value">
						88888
						<text>(JPY)</text>
					</view>
					<view class="icon"></view>
				</view>
			</view>
			
			<view class="tips">
				如需提现,联系XXXX,XXXX,如退回需要收取1%的手续费,7个工作内日到账。
			</view>
			
			<view class="log">
				<view class="subtitle">钱包明细</view>
				<view class="item">2025年5月16日 11:11:11 充值余额 +10000</view>
				<view class="item">2025年5月16日 11:11:11 充值余额 +10000</view>
				<view class="item">2025年5月16日 11:11:11 充值余额 +10000</view>
				<view class="item">2025年5月16日 11:11:11 充值余额 +10000</view>
				<view class="item">2025年5月16日 11:11:11 充值余额 +10000</view>
			</view>
			
			<view class="opt">
				<image
					class="icon"
					src="@/static/icon/72.png"
					@click="toPath(`/pages/mine/charge`)"
				>
				<image
					class="icon"
					src="@/static/icon/73.png"
					@click="toPath(`/pages/mine/guaranteeCharge`)"
				>
			</view>
		</view>
		
		
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
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
			toPath(url){
				wx.navigateTo({
					url,
				})
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

<style lang="scss" scoped>
.nav{
	width: 700rpx;
	margin: 30rpx auto 0;
	padding-bottom: 300rpx
}

.moneyNav{
	width: 100%;
	height: 288rpx;
	background: #FFBDBB;
	justify-content: space-between;
	padding: 24rpx 15rpx;
	font-weight: 600;
	font-size: 30rpx;
	.item{
		width: 100%;
		.label{
			width: 325rpx;
		}
		.value{
			width: 180rpx;
			text{
				font-size: 24rpx;
			}
		}
		.icon{
			width: 80rpx;
			height: 40rpx;
		}
	}
}
.tips{
	width: 100%;
	box-sizing: border-box;
	background: #FFE1E1;
	padding: 20rpx;
	color: #191919;
	font-size: 28rpx;
	line-height: 48rpx;
	margin: 20rpx 0;
}
.log{
	width: 100%;
	box-sizing: border-box;
	background: #EFEFEF;
	padding: 20rpx;
	color: #A8A8A8;
	.subtitle{
		color: #000;
		font-size: 36rpx;
		font-weight: 600;
	}
	.item{
		margin-top: 10rpx;
		font-size: 26rpx;
	}
}
.opt{
	position: fixed;
	bottom: 30rpx;
	left: 25rpx;
	width: 700rpx;
	.icon{
		width: 700rpx;
		height: 72rpx;
		margin: 10rpx 0;
	}
}
</style>
