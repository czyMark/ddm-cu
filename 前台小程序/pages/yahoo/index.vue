<template>
	<view class="yahoo">
		<Header logo="YaHoo" page="YaHoo" :bigClass="bigClass"></Header>
		<goodsClass type="yahoo" :classList="classList" :bigClass="bigClass"></goodsClass>
	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	import Header from "@/components/header.vue"
	import goodsClass from "@/components/goodsClass.vue"
	export default {
		components: {Header, goodsClass},
		data() {
			return {
				classList: clasMenu,
				bigClass: 23336,
			}
		},
		onShow(){
			const bigClass = wx.getStorageSync('yahooId') || '23336'
			this.bigClass = bigClass === '0' ? '23336' : bigClass
		},
		methods: {
			onBigClassChange(val){
				this.bigClass = val
			}
		}
	}
</script>

<style>
	
</style>
