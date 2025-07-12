<template>
	<view class="mercari page">
		<Title :type="6"/>
		<Header page="none" @onSearch="onSearch"/>
		<goodsClass type="mercari" :classList="classList" :bigClass="bigClass"></goodsClass>
	</view>
</template>

<script>
	// import clasMenu from "@/utils/mercariClass.js"
	import Title from "@/components/title.vue"
	import Header from "@/components/header.vue"
	import goodsClass from "@/components/goodsClass.vue"
	export default {
		components: {Title, Header, goodsClass},
		data() {
			return {
				classList: [],
				bigClass: 0,
			}
		},
		onShow(){
			const classes = wx.getStorageSync('classes') || {}
			this.classList = classes.mercariClass || []
			const showClasses = []
			this.classList.forEach(item=>{
				if(item.isShow) showClasses.push(item)
			})
			this.bigClass = showClasses[0].id
		},
		methods: {
			onBigClassChange(val){
				this.bigClass = val
			},
			onSearch(keyword){
				this.toPath(`/pages/mercari/mercariResult?bigClass=${this.bigClass}&keyword=${keyword}`)
			},
			toPath(url){
				wx.navigateTo({
					url,
				})
			}
		}
	}
</script>

<style>
	
</style>
