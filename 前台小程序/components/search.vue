<template>
	<view class="searchNav">
		<view class="input" :style="{width: width, height: height}">
			<input
				type="text"
				:placeholder="placeholder"
				@focus="onFocus"
				v-model="keyword"
				placeholder-style="color:#ADADAD;font-size:28rpx;"
			/>
			
			<view class="icon">
				<uni-icons type="search" size="22" color="#4C43E8"></uni-icons>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				navBarheight: 0,
				keyword: '',
			};
		},
		watch: {
			keyword(val){
				this.$emit('keywordChange', val)
			}
		},
		props: {
			placeholder: {
				type: String,
				default: '请输入关键词搜索'
			},
			width: {
				type: String,
				default: '450rpx'
			},
			height: {
				type: String,
				default: '70rpx'
			},
		},
		onReady(){
			this.navBarheight = wx.getStorageSync('statusBarHeight')
		},
		methods: {
			goback(){
				wx.navigateBack(-1)
			},
			onFocus(){
				this.$emit('onFocus')
			},
			onSearch(){
				this.$emit('onSearch')
			}
		}
	}
</script>

<style lang="scss" scoped>

.input{
	height: 70rpx;
	border-radius: 50rpx;
	box-sizing: border-box;
	padding: 12rpx 0;
	padding-left: 90rpx;
	border: 1px solid #4C43E8;
	position: relative;
	position: relative;
	.icon{
		position: absolute;
		left: 30rpx;
		top: 12rpx;
	}
}



</style>
