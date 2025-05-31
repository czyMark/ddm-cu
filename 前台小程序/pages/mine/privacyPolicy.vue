<template>
	<view class="page">
		<view class="content">
			<rich-text :nodes="HtMLData"></rich-text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				HtMLData: ``
			}
		},
		onLoad() {
			this.HtMLData = wx.getStorageSync('setting')?.privacy || ''
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
</style>
