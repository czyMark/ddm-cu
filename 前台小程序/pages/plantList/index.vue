<template>
	<view class="page home">
		<Title :type="1" :showBack="false" title="优选"/>
		
		<view class="tips">敬请期待...</view>
	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	import Title from "@/components/title.vue"
	
	export default {
		components: {Title, },
		data() {
			return {
				
			}
		},
		async onLoad() {
			
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
	.tips{
		margin: 200rpx 0;
		width: 750rpx;
		text-align: center;
		opacity: 0.5;
	}
</style>
