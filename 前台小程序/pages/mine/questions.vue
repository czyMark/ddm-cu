<template>
	<view class="page">
		<view class="list">
			<view class="item between" @click="toPath(item.id)" v-for="(item, index) in list" :key="index">
				<view class="itemTitle">{{item.title}}</view>
				<uni-icons type="right" size="20"></uni-icons>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: []
			}
		},
		onLoad() {
			this.getList()
		},
		methods: {
			async getList(){
				wx.showLoading({title: '加载中'})
				this.loading = true
				const res = await this.$api.getQuestionList()
				const { data, code, msg } = res.data
				console.log(data, code, msg)
				if(code === 0){
					this.list = data
					wx.hideLoading()
				}else{
					wx.hideLoading()
					wx.showToast({title: msg})
				}
			},
			toPath(id){
				wx.navigateTo({
					url: '/pages/mine/questionDetail?id=' + id
				})
			}
		}
	}
</script>

<style lang="scss">
	.list{
		padding: 30rpx;
		.item{
			height: 100rpx;
			border-bottom: 1px solid #e5e5e5;
			.itemTitle{
				width: 600rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
		}
		.item:last-child{
			border: none
		}
	}
</style>
