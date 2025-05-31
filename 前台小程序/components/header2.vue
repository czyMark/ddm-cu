<template>
	<view class="page">
		<view class="statusBar" :style="{height: navBarheight + 'px', background: showBg ? bgColor : 'none'}"></view>
		<view class="header between"  :style="{background: showBg ? bgColor : 'none', color: fontColor}">
			<view v-if="!showBack" class="blank"></view>
			<uni-icons v-if="showBack" type="back" :color="fontColor" size="26" @click="goback"></uni-icons>
			<view class="title">{{title}}</view>
			<view class="blank"></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				navBarheight: 0,
			};
		},
		props: {
			title: {
				type: String,
				default: ''
			},
			bgColor: {
				type: String,
				default: '#FFF'
			},
			fontColor: {
				type: String,
				default: '#000'
			},
			showBg: {
				type: Boolean,
				default: true
			},
			showBack: {
				type: Boolean,
				default: true
			},
		},
		onReady(){
			this.navBarheight = wx.getStorageSync('statusBarHeight')
		},
		methods: {
			goback(){
				wx.navigateBack(-1)
			}
		}
	}
</script>

<style lang="scss">
.header{
	height: 80rpx;
	padding: 0 30rpx;
	.blank{
		width: 80rpx;
	}
}
</style>
