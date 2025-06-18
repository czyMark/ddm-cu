<template>
	<view class="collect page">
		<Title :type="1" title="充值"/>
		<view class="title">充值金额</view>
		
		<view class="charge between">
			<input placeholder="输入金额" v-model="money" type="number">
			<view class="label">日元</view>
		</view>
		<view class="rate">今日汇率{{rate}}</view>
		
		<view class="rule">
			<view class="subtitel">充值规则</view>
			<view class="item">1.充值规则充值规则充值规则充值规则充值规则。</view>
			<view class="item">2.充值规则充值规则充值规则充值规则充值规则充值规则
			  充值规则充值规则充值规则充值规则。</view>
			<view class="item">3.充值规则充值规则充值规则充值规则充值规则充值规则
			  充值规则充值规则充值规则充值规则则充值规则充值规
			  充值规则充值规则充值规则充值规则则充值规则充值规
			  充值规则充值规则充值规则充值规则则充值规则充值规
			  充值规则充值规则充值规则充值规则。</view>
			<view class="item">4.充值规则充值规则充值规则充值规则充值规则充值规则
			  充值规则充值规则充值。</view>
		</view>
		
		<view class="deal flex">
			<view class="icon"></view>
			<image
				class="icon"
				src="@/static/icon/45.png"
			>
			<view class="des">已阅读并同意<text>《代代米日淘服务协议》</text></view>
		</view>
		
		  
		<image
			class="submit"
			src="@/static/icon/65.png"
		>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
		data() {
			return {
				rate: ''
			}
		},
		onLoad() {
			
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>

.title{
	margin: 30rpx;
	font-weight: 600;
	font-size: 36rpx;
}
.charge{
	color: #000;
	margin-left: 30rpx;
	margin-top: 50rpx;
	width: 690rpx;
	padding: 30rpx 0;
	font-size: 30rpx;
	border-bottom: 1px solid #F2F2F2;
	.label{
		color: #C9C9C9;
	}
}
.rate{
	margin: 30rpx;
	width: 690rpx;
	text-align: right;
	font-size: 22rpx;
}
.rule{
	background-color: #DEF6FF;
	margin-left: 30rpx;
	width: 690rpx;
	box-sizing: border-box;
	padding: 20rpx;
	color: #7A3F25;
	.subtitel{
		font-size: 36rpx;
	}
	.item{
		font-size: 26rpx;
	}
}
.deal{
	margin: 30rpx 30rpx 50rpx;
	color: #898989;
	font-size: 20rpx;
	.icon{
		height: 24rpx;
		width: 24rpx;
	}
	text{
		color: #63A3EA;
	}
}

.submit{
	width: 690rpx;
	margin-left: 30rpx;
	height: 90rpx;
}
	
</style>
