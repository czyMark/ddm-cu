<template>
	<view class="page">
		<Title :type="1" title="联系客服" :showBack="false"/>
		<view class="content">
			<rich-text :nodes="HtMLData"></rich-text>
		</view>
		<view class="btn">
			<button open-type="contact" @contact="handleContact">点此直接联系客服</button>
		</view>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	export default {
		components: {Title},
		data() {
			return {
				HtMLData: ``
			}
		},
		onLoad() {
			this.HtMLData = wx.getStorageSync('setting')?.customer || ''
		},
		methods: {
			handleContact(e){
				console.log(e)
				console.log(e.detail.path)
				console.log(e.detail.query)
			}
		}
	}
</script>

<style lang="scss">
	.content{
		width: 690rpx;
		padding: 30rpx;
		height: auto;
		margin-bottom: 150rpx;
	}
	.btn{
		position: fixed;
		bottom: 50rpx;
		left: 50rpx;
		width: 650rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		border-radius: 20rpx;
		button{
			color: #FFF;
			background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
		}
	}
</style>
