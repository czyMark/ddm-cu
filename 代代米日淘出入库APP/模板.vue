<template>
	<view class="page">
		<Header title="模板"></Header>
		
		
	</view>
</template>

<script>
	
import Header from "@/components/header.vue"
export default {
	components: {Header},
	data(){
		return {
			
		}
	},
	onLoad() {
	},
	async onShow(){
		
		
	},
	
	methods: {
		async getMapList(val){
			const params = {
				type: this.role,
				lng: this.lng,
				lat: this.lat,
				search_type: val || 1
			}
			const res = await api.getMapList(params)
		},
		
		
		toPath(url){
			uni.navigateTo({
				url
			})
		},
		
		
	}
}
</script>

<style lang="scss" scoped>

	.page{
		width: 750rpx;
		background-color: #fff;
	}

	
	
</style>