<template>
	<view class="page">
		<view class="statusBar" :style="{height: navBarheight + 'px', background: showBg ? bgColor : 'none'}"></view>
		<view class="header between"  :style="{background: showBg ? bgColor : 'none', color: fontColor}">
			<view v-if="!showBack" class="blank"></view>
			<uni-icons v-if="showBack" type="back" :color="fontColor" size="26" @tap="goback"></uni-icons>
			<view class="title">{{title}}</view>
			<view class="blank center">
				<!-- <image v-if="deleteBtn" @tap="onDelete" class="delete" src="../static/img/delete.png"></image> -->
				<view v-if="showMa" @tap="onManage" class="manage center">管理</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				navBarheight: ''
			};
		},
		props: {
			title: {
				type: String,
				default: ''
			},
			isStopBack: {
				type: Boolean,
				default: false
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
			isLogin: {
				type: Boolean,
				default: false
			},
			deleteBtn: {
				type: Boolean,
				default: false
			},
			showMa: {
				type: Boolean,
				default: false
			},
			
		},
		mounted(){
			this.navBarheight = this.$store.state.navBarheight
		},
		methods: {
			goback(){
				if(this.isStopBack){
					uni.showToast({
						title: '请先完善资料',
						icon: 'none'
					})
				}else{
					uni.navigateBack()
				}
			},
			onManage(){
				this.$emit('onManage')
			}
		},
	}
</script>

<style lang="scss" scoped>
.header{
	height: 90rpx;
	padding: 0 25rpx;
	padding-left: 12rpx;
	position: relative;
	z-index: 2;
	.blank{
		min-width: 80rpx;
	}
	.blank2{
		display: flex;
		align-items: center;
		justify-content: flex-end;
	}
	.delete{
		width:35rpx;
		height:35rpx
	}
	.manage{
		border: 1px solid #333;
		font-size: 28rpx;
		width: 126rpx;
		height: 56rpx;
		border-radius: 10rpx;
	}
}
</style>
