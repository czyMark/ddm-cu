<template>
	<view class="yahoo page">
		<Title :type="4"/>
		<Header page="none" @onSearch="onSearch"/>
		<goodsClass type="yahoo" :classList="classList" :bigClass="bigClass"></goodsClass>
	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	import Title from "@/components/title.vue"
	import Header from "@/components/header.vue"
	import goodsClass from "@/components/goodsClass.vue"
	export default {
		components: {Title, goodsClass,Header},
		data() {
			return {
				classList: [],
				bigClass: 23336,
			}
		},
		onShow(){
			const bigClass = wx.getStorageSync('yahooId') || '23336'
			this.bigClass = bigClass === '0' ? '23336' : bigClass
			
			clasMenu.forEach(item=>{
				item.isShow = true
				item.children.forEach(it=>{
					it.isShow = true
				})
			})
			this.classList = clasMenu
		},
		methods: {
			onBigClassChange(val){
				this.bigClass = val
			},
			onSearch(keyword){
				this.toPath(`/pages/yahoo/yahooResult?bigClass=${this.bigClass}&keyword=${keyword}`)
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
