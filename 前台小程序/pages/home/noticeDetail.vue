<template>
	<view class="page">
		<view class="title">
			{{title}}
		</view>
		<view class="content">
			<rich-text :nodes="HtMLData"></rich-text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: ``,
				HtMLData: ``,
			}
		},
		onLoad(query) {
			this.getDetail(query.id)
		},
		methods: {
			async getDetail(id){
				wx.showLoading({title: '加载中'})
				this.loading = true
				const res = await this.$api.getNoticetById({id})
				const { data, code, msg } = res.data
				if(code === 0){
					this.title = data.title
					const arr = data.content.split('<img')
					
					console.log('arr', arr)
					
					this.HtMLData = arr.join(`<img style="width: 365px;"`)
					wx.hideLoading()
				}else{
					wx.hideLoading()
					wx.showToast({title: msg})
				}
			},
		}
	}
</script>

<style lang="scss">
	.title{
		padding: 30rpx;
		font-size: 46rpx;
		font-weight: 600;
	}
	.content{
		width: 690rpx;
		padding: 30rpx;
		height: auto;
		margin-bottom: 150rpx;
	}
</style>
