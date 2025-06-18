<template>
	<view class="page searchNav">
		<view class="input">
			<input
				type="text"
				:placeholder="placeholder"
			/>
			
			<uni-icons type="search" size="30" color="#000"></uni-icons>
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
			placeholder: {
				type: String,
				default: ''
			}
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

<style lang="scss" scoped>

</style>
