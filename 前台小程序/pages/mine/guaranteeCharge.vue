<template>
	<view class="collect page">
		<Title :type="1" title="保证金充值"/>
		<view class="title">请选择存入金额</view>
		
		<view class="tips">弃标金扣除保证金，请谨慎竞拍！</view>
		
		<table class="table">
			<tr>
				<th>保证金金额(JPY)</th>
				<th>可出价金额(JPY)</th>
			</tr>
			
			<tr v-for="(item, index) in list" :key="index">
				<td @click="currentNum = item.num">
					<image
						v-if="currentNum === item.num"
						class="icon"
						src="@/static/icon/45.png"
					>
					<image
						v-else
						class="icon"
						src="@/static/icon/66.png"
					>
					<text>{{item.num}}</text>
				</td>
				<td> 
					<text>{{item.limit}}</text>
				</td>
			</tr>
		</table>

		<image
			class="submit"
			src="@/static/icon/67.png"
		>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
		data() {
			return {
				rate: '',
				currentNum: 1000,
				list: [
					{id: 1, num: 1000, limit: '1-1000'},
					{id: 2, num: 2000, limit: '1-5000'},
					{id: 3, num: 5000, limit: '1-10000'},
					{id: 4, num: 10000, limit: '1-30000'},
					{id: 5, num: 15000, limit: '1-50000'},
					{id: 6, num: 30000, limit: '1-100000'},
					{id: 7, num: 60000, limit: '1-200000'},
					{id: 8, num: 150000, limit: '1-500000'},
					{id: 9, num: 300000, limit: '1-1000000'},
				]
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
.tips{
	color: #FF2727;
	font-size: 26rpx;
	width: 750rpx;
	text-align: center;
	margin: 20rpx 0;
}
.table{
	margin: 0 auto;
	font-size: 26rpx;
	width: 500rpx;
	th{
		color: #828282;
	}
	tr{
		display: flex;
		align-items: center;
	}
	th, td{
		width: 250rpx;
		display: flex;
		align-items: center;
		height: 60rpx;
	}
	td{
		.icon{
			width: 36rpx;
			height: 36rpx;
			margin-right: 10rpx;
		}
	}
}
.submit{
	position: fixed;
	bottom: 50rpx;
	left: 30rpx;
	width: 690rpx;
	height: 70rpx;
}
	
</style>
