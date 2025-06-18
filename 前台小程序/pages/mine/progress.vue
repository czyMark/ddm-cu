<template>
	<view class="page">
		<Title :type="1" title="竞拍流程"/>
		<view class="content">
			<rich-text :nodes="HtMLData"></rich-text>
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
			this.HtMLData = wx.getStorageSync('setting')?.riskwarning || ''
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
