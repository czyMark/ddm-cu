<template>
	<view class="page">
		<Header title="模板"></Header>
		
		
		
	</view>
</template>

<script>
import Header from "@/components/header.vue"
export default {
	components: {Header},
	data() {
		return {
			searchValue: '',
			list: []
		}
	},
	onLoad(){
		this.search()
	},
	methods: {
		
	}
	}
</script>

<style scoped lang="scss">
.page {
	width: 750rpx;
	height: 100vh;
	box-sizing: border-box;
	overflow: auto;
	font-size: 14px;
}

</style>
