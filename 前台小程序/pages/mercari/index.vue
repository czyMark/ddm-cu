<template>
	<view class="mercari">
		<Header logo="mercari"  page="mercari" :bigClass="bigClass"></Header>
		<goodsClass type="mercari" :classList="classList" :bigClass="bigClass"></goodsClass>
	</view>
</template>

<script>
	import clasMenu from "@/utils/mercariClass.js"
	import Header from "@/components/header.vue"
	import goodsClass from "@/components/goodsClass.vue"
	export default {
		components: {Header, goodsClass},
		data() {
			return {
				classList: clasMenu,
				bigClass: 0,
			}
		},
		onShow(){
			this.bigClass = 1
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
