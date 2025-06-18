<template>
	<view :class="['titleNav header_sticky', showBg ? '' : 'hideBg']"
	>
		<view class="statusBar" :style="{height: navBarheight + 'px', background: showBg ? bgColor : 'none'}"></view>
		<view class="header between"  :style="{color: fontColor}" v-if="type === 1">
			<view v-if="!showBack" class="blank"></view>
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
				v-if="showBack"
			>
			<view class="title">{{title}}</view>
			<view class="blank"></view>
		</view>
		
		<view class="header flex"  v-if="type === 2">
			<image
				class="logo"
				src="@/static/icon/1.png"
			>
			<Search
				height="70rpx"
				width="365rpx"
				@onFocus="onFocus"
			/>
		</view>
		
		<view class="header flex"  v-if="type === 3">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
			<Search
				height="70rpx"
				width="482rpx"
			/>
		</view>
		
		<view class="header flex"  v-if="type === 4">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
			<image
				class="plantLogo"
				src="@/static/icon/76.png"
			>
		</view>
		
		<view class="header flex"  v-if="type === 6">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
			<image
				class="plantLogo"
				src="@/static/icon/55.png"
			>
		</view>
		
		<view class="header flex"  v-if="type === 5">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
			<view class="currentClass center">雅虎 > {{currentClasses}}</view>
		</view>
		
		<view class="header flex"  v-if="type === 7">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
			<view class="currentClass center">煤炉 > {{currentClasses}}</view>
		</view>
		
		<view class="header  flex"  v-if="type === 8">
			<image
				class="arrow"
				src="@/static/icon/20.png"
				@click="goback"
			>
		</view>
	</view>
</template>

<script>
	import Search from "./search.vue"
	export default {
		components: {Search},
		data() {
			return {
				navBarheight: 0,
			};
		},
		props: {
			currentClasses: {
				type: String,
				default: ''
			},
			type: {
				type: Number,
				default: 1
			},
			title: {
				type: String,
				default: ''
			},
			bgColor: {
				type: String,
				default: 'none'
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
			console.log('currentClasses',this.currentClasses)
			this.navBarheight = wx.getStorageSync('statusBarHeight')
		},
		methods: {
			goback(){
				wx.navigateBack(-1)
			},
			onFocus(){
				uni.navigateTo({
					url: '/pages/home/search'
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
.hideBg{
	background: none !important;
}
.sticky{
	position: fixed;
	top: 0;
	left: 0;
	z-index: 9
}
.header{
	height: 80rpx;
	padding: 0 30rpx;
	align-items: center;
	.blank{
		width: 80rpx;
	}
}
.logo{
	width: 145rpx;
	height: 72rpx;
	margin-right: 22rpx;
}
.arrow{
	width: 18rpx;
	height: 34rpx;
	padding: 16rpx;
}
.plantLogo{
	margin-right: 40rpx;
	width: 245rpx;
	height: 62rpx;
}

.currentClass{
	justify-content: flex-start;
	width: 410rpx;
	height: 54rpx;
	background-color: #fff;
	color: #ADADAD;
	font-size: 28rpx;
	padding-left: 20rpx;
}

</style>
