<template>
	<view class="collect">
		<view class="header between">
			<view class="uniInput">
				<uni-easyinput prefixIcon="search" v-model="keyword"
					@focus="onFocus" placeholder="搜索" @confirm="onSearch">
				</uni-easyinput>
			</view>
			<view class="right flex" v-if="hasClass">
				<view class="exchangeRate flex">
					<view class="title" @click="onSearch">搜索</view>
				</view>
			</view>
			<view class="right flex" v-if="!hasClass">
				<view class="exchangeRate flex">
					<view class="title" @click="onSearch">搜索</view>
				</view>
			</view>
		</view>
		
		<view class="currenClass between" v-if="hasClass && !isOnFocus">
			<view class="value">
				当前分类: {{currentClasses}}
			</view>
			<!-- <view class="close" @click="closeClass">
				<uni-icons type="closeempty" size="16"></uni-icons>
			</view> -->
		</view>
		
		<view class="filter between" v-if="!isOnFocus">
			<view class="itemFilter center"
				v-for="(item, index) in tabs" :key="index"
				@click="chooseTab(item)">
				<view :class="['label', item.isCurrent ? 'active' : '']">{{item.label}}</view>
				<view class="icons column" v-if="item.sort === 0">
					<image src="@/static/Arrow-Up2.png"></image>
					<image src="@/static/Arrow-Down2.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 2">
					<image src="@/static/Arrow-Up2-active.png"></image>
					<image src="@/static/Arrow-Down2.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 1">
					<image src="@/static/Arrow-Up2.png"></image>
					<image src="@/static/Arrow-Down2-active.png"></image>
				</view>
			</view>
			<view class="itemFilter center" @click="showDrawer">
				<view class="label">筛选</view>
				<view class="icons column icons2">
					<image src="@/static/filter.png"></image>
				</view>
			</view>
			<view class="listMode" @click="changeIsList">
				<image src="@/static/list.png" mode="" v-if="!isList"></image>
				<image src="@/static/menu.png" mode="" v-if="isList"></image>
			</view>
		</view>
		
		<view class="list" v-if="!isOnFocus && isList">
			<view class="item center" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="imageUrl center">
					<image :src="item.imageUrl" mode="aspectFit"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price between" style="width: 478rpx">
						<view>编号：<text>{{item.code}}</text></view>
						<view @click.stop="copyCode(item.code)" style="border: 1px solid #c5c5c5; padding: 1px 5px; color: #423f3f;">复制</view>
					</view>
					<view class="saler">出品者：{{item.seller}}</view>
					<view class="price">现价：<text>{{item.jpprice}}円 ({{item.price_rmb}}元)</text></view>
					<view class="bottom between">
						<view class="times center">
							<image src="@/static/shiwu-chuizi.png" mode=""></image>
							<text>{{item.counts}}次</text>
						</view>
						<view class="times center">
							<image src="@/static/jishishalou.png" mode=""></image>
							<text>{{item.time}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="list list2 between" v-if="!isOnFocus && !isList">
			<view class="item" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="imageUrl center">
					<image :src="item.imageUrl" mode="aspectFit"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price between" style="width: 100%">
						<view>编号：<text>{{item.code}}</text></view>
						<view @click.stop="copyCode(item.code)" style="border: 1px solid #c5c5c5; padding: 1px 4px; color: #423f3f; font-size: 18rpx">复制</view>
					</view>
					<view class="saler">出品者：{{item.seller}}</view>
					<view class="price">现价：<text>{{item.jpprice}}円 ({{item.price_rmb}}元)</text></view>
					<view class="bottom between">
						<view class="times center">
							<image src="@/static/shiwu-chuizi.png" mode=""></image>
							<text>{{item.counts}}次</text>
						</view>
						<view class="times center">
							<image src="@/static/jishishalou.png" mode=""></image>
							<text>{{item.time}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="loading" v-if="!isOnFocus">
			{{noMore ? '没有更多了' : '正在加载...'}}
		</view>
		
		<view class="historyHeader between"  v-if="isOnFocus">
			<view class="title">最近搜索</view>
			<uni-icons type="trash" size="24" @click="deleteHistory"></uni-icons>
		</view>
		<view class="history flex" v-if="isOnFocus">
			<view class="historyItem" v-for="(item,index) in historyList" :key="index"
				@click="onHistoryTap(item)">
				{{item}}
			</view>
			<i></i>
			<i></i>
			<i></i>
			<i></i>
			<i></i>
			<i></i>
			<i></i>
		</view>
		
		<view class="drawer">
			<uni-drawer ref="showRight" mode="right" :width="200">
				<view class="drawerContent">
					<view class="item">
						<view class="label">关键词</view>
						<uni-easyinput v-model="keyword" placeholder="请输入内容"></uni-easyinput>
					</view>
					<view class="item">
						<view class="label">卖家</view>
						<view class="values between">
							<view :class="['vItem', currentSalerType === item.id ? 'active' : '']"
								v-for="(item, index) in salerTypes" :key="index"
								@click="onChoose(item, 'currentSalerType')"
							>
								{{item.label}}
							</view>
						</view>
					</view>
					<view class="item">
						<view class="label">新旧</view>
						<view class="values between">
							<view :class="['vItem', currentOldOrNew === item.id ? 'active' : '']"
								v-for="(item, index) in oldOrNew" :key="index"
								@click="onChoose(item, 'currentOldOrNew')"
							>
								{{item.label}}
							</view>
						</view>
					</view>
					<view class="item">
						<view class="label">商品价格</view>
						<view class="values between">
							<view :class="['vItem', currentPriceLimits === item.id ? 'active' : '']"
								v-for="(item, index) in priceLimits" :key="index"
								@click="onChoose(item, 'currentPriceLimits')"
							>
								{{item.label}}
							</view>
							<i></i>
							<i></i>
							<i></i>
							<i></i>
						</view>
					</view>
					
					<view class="btns flex">
						<view class="btn" @click="closeDrawer">取消</view>
						<view class="btn" @click="()=>{
							tabs.forEach(it=>{
								it.isCurrent = false
								it.sort = 0
							})
							onSearch()
						}">确定</view>
					</view>
				</view>
			</uni-drawer>
		</view>
	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	export default {
		data() {
			return {
				currentClasses: '',	// 当前分类
				hasClass: false, // 有无分类参数
				keyword: '',
				list: [],
				pagecount: 1,
				
				tabs: [
					{id: 1, sort: 0, label: '价格', isCurrent: false},
					{id: 2, sort: 0, label: '时间', isCurrent: false},
					{id: 3, sort: 0, label: '热度', isCurrent: false},
				],
				salerTypes: [
					{id: 0, label: '所有卖家'},
					{id: 1, label: '正规店铺'},
					{id: 2, label: '个人卖家'},
				],
				currentSalerType: '',
				oldOrNew: [
					{id: 0, label: '不指定'},
					{id: 1, label: '新品'},
					{id: 2, label: '中古'},
				],
				currentOldOrNew: '',
				priceLimits: [
					{id: 0, label: '5000円以下'},
					{id: 1, label: '5000-1万円'},
					{id: 2, label: '1万-2万円'},
					{id: 3, label: '2万-5万円'},
					{id: 4, label: '5万円以上'},
				],
				currentPriceLimits: '',
				
				query: {},
				noMore: false,	// 没有更多了
				rate: null,
				
				isOnFocus: false,
				isList: true,
				historyList: []
			}
		},
		onLoad(query) {
			this.historyList = wx.getStorageSync('yahooHistory') || []
			this.rate = wx.getStorageSync('rate') || null
			if(Object.keys(query).length > 0){
				this.query = query
				this.hasClass = true
				this.setClass(query)
			}
			// 查询商品列表
			const params = {
				urlid: parseInt(query.smallClass) || this.query.bigClass*1,
				pagecount: this.pagecount
			}
			this.selectcates(params)
		},
		onReachBottom(){
			this.pagecount++
			const params = {
				urlid: parseInt(this.query?.smallClass) || this.query.bigClass*1,
				pagecount: this.pagecount
			}
			this.selectcates(params)
		},
		methods: {
			changeIsList(){
				this.isList = !this.isList
			},
			deleteHistory(){
				this.historyList = []
			},
			onFocus(){
				this.isOnFocus = true
			},
			async selectcates(params){
				wx.showLoading({title: '加载中'})
				const res = await this.$api.getYahooGoodList({
					pagecount: this.pagecount,
					urlid: parseInt(this.query?.middleClass) || parseInt(this.query?.bigClass),
					keyword: this.keyword,
					priceType: this.currentPriceLimits,
					isNewType: this.currentOldOrNew,
					selltype: this.currentSalerType,
					priceOderType: this.tabs[0].sort,
					timeType: this.tabs[1].sort,
					hotType: this.tabs[2].sort,
				})
				const {data} = res.data
				
				data?.forEach(item=>{
					item.price_rmb = parseInt((item.jpprice * this.rate) * 100) / 100
				})
				if(data.length < 50){
					this.noMore = true
				}
				this.list = this.list.concat(data)
				wx.hideLoading()
			},
			// async getYahooGoodList(params){
			// 	wx.showLoading({title: '加载中'})
			// 	const res = await this.$api.getYahooGoodList(params)
			// 	const {data} = res.data
				
			// 	data?.forEach(item=>{
			// 		item.price_rmb = parseInt((item.jpprice * this.rate) * 100) / 100
			// 	})
			// 	if(data.length < 50){
			// 		this.noMore = true
			// 	}
			// 	this.list = this.list.concat(data)
			// 	wx.hideLoading()
			// },
			
			setClass(query){
				const arr = []
				const {bigClass='', middleClass='', smallClass=''} = query
				console.log(bigClass, middleClass, smallClass)
				
				let bigClassIndex = ''
				clasMenu?.forEach((item, index)=>{
					if(item.id === bigClass){
						arr.push(item.name_cn)
						bigClassIndex = index
					}
				})
				
				let middileClassIndex = ''
				if(middleClass){
					clasMenu[bigClassIndex].children?.forEach((item, index)=>{
						if(item.id === middleClass){
							arr.push(item.name_cn)
							middileClassIndex = index
						}
					})
				}
				if(smallClass){
					clasMenu[bigClassIndex].children[middileClassIndex].children?.forEach((item, index)=>{
						if(item.id === smallClass){
							arr.push(item.name_cn)
						}
					})
				}
				
				this.currentClasses = arr.join(' > ')
			},
			chooseTab(item){
				if(!item.isCurrent){
					this.tabs.forEach(it=>{
						it.isCurrent = false
						it.sort = 0
						if(it.id === item.id){
							it.isCurrent = true
						}
					})
				}
				if(item.sort === 0){
					item.sort = 2
				}else if(item.sort === 2){
					item.sort = 1
				}else if(item.sort === 1){
					item.sort = 2
				}
				this.onSearch()
			},
			chooseItem(item){
				const {seller, code, breakurl} = item
				wx.navigateTo({
					url: `/pages/yahoo/detail?code=${item.code}`,
				})
			},
			onHistoryTap(val){
				this.keyword = val
				this.onSearch()
			},
			setHistory(){
				const keyword = this.keyword.trim()
				if(this.historyList.length === 0){
					if(Boolean(keyword)) this.historyList.unshift(keyword)
				}else{
					const index = this.historyList.indexOf(keyword)
					if(index === -1){
						if(Boolean(keyword)) this.historyList.unshift(keyword)
					}else{
						if(Boolean(keyword)){
							this.historyList.splice(index, 1)
							this.historyList.unshift(keyword)
						}
					}
					if(this.historyList.length > 10){
						this.historyList = this.historyList.splice(0, 10)
					}
				}
				wx.setStorage({ key: 'yahooHistory' , data: this.historyList })
			},
			async onSearch(){
				wx.showLoading()
				this.isOnFocus = false
				this.setHistory()
				this.pagecount = 1
				this.list = []
				
				const res = await this.$api.getYahooGoodDetail({code: this.keyword})
				if(res.data.code !== 500){
					wx.hideLoading()
					wx.navigateTo({
						url: `/pages/yahoo/detail?code=${this.keyword}`,
					})
					return false
				}
				
				
				let params = null
				params = {
					pagecount: this.pagecount,
					urlid: parseInt(this.query?.middleClass) || parseInt(this.query?.bigClass),
					keyword: this.keyword,
					priceType: this.currentPriceLimits,
					isNewType: this.currentOldOrNew,
					selltype: this.currentSalerType,
					priceOderType: this.tabs[0].sort,
					timeType: this.tabs[1].sort,
					hotType: this.tabs[2].sort,
				}
				this.selectcates(params)
				this.closeDrawer()
			},
			showDrawer(){
				this.$refs['showRight'].open()
				this.currentSalerType = ''
				this.currentOldOrNew = ''
				this.currentPriceLimits = ''
			},
			closeDrawer(){
				this.$refs['showRight'].close()
			},
			onChoose(item, key){
				if(this[key] === item.id){
					this[key] = ''
				}else{
					this[key] = item.id
				}
				this[key] = item.id
			},
			closeClass(){
				this.hasClass = false
			},
			copyCode(val){
				wx.setClipboardData({
					data: val,
					success: ()=>{
						wx.showToast({
							title: '复制成功'
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.header{
		height: 85rpx;
		padding: 0 20rpx;
		.uniInput{
			width: 580rpx;
		}
		.uniInput2{
			width: 580rpx;
		}
		.right{
			font-size: 28rpx;
		}
		.exchangeRate{
			font-size: 28rpx;
			color: #FFF;
			.title{
				padding: 6rpx 20rpx;
				border-radius: 20rpx;
				margin-right: 10rpx;
				background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
			}
		}
	}
	
	.currenClass{
		width: 750rpx;
		box-sizing: border-box;
		padding: 0 20rpx;
		margin-top: 20rpx;
		font-size: 26rpx;
		color: #000;
		opacity: 0.4;
		font-weight: 600;
		
	}
	
	.filter{
		margin-top: 30rpx;
		.itemFilter{
			width: 180rpx;
			.icons{
				margin-left: 10rpx;
				image{
					width: 20rpx;
					height: 20rpx;
				}
			}
			.active{
				color: #ffa500;
			}
			.icons2 image{
				width: 30rpx;
				height: 40rpx;
			}
		}
	}
	
	.loading{
		height: 80rpx;
		line-height: 80rpx;
		width: 100vw;
		text-align: center;
		font-size: 28rpx;
		color: #9e9a9a;
		margin-bottom: 30rpx;
	}
	
	.drawerContent{
		width: 600rpx;
		height: 100%;
		box-sizing: border-box;
		padding: 30rpx 0;
		position: relative;
		
		.item{
			box-sizing: border-box;
			padding: 0 30rpx 20rpx 30rpx;
			border-bottom: 20rpx solid #e5e5e5;
			.label{
				color: #e89909;
				padding-top: 10rpx;
				margin-bottom: 10rpx;
			}
			.values{
				flex-wrap: wrap;
			}
			.vItem{
				margin-top: 20rpx;
				font-size: 26rpx;
				padding: 8rpx 20rpx;
				border-radius: 20rpx;
				background-color: #e5e5e5;
				color: #6f6d6d;
				border: 1px solid #e5e5e5;
			}
			i{
				width: 100rpx;
				height: 0;
			}
			.active{
				box-sizing: border-box;
				color: #eba82e;
				border: 1px solid #eba82e;
				background: #FFF;
			}
		}
		.item:last-child{
			border-bottom: none
		}
		
		.btns{
			width: 100%;
			position: absolute;
			bottom: 0;
			left: 0;
			.btn{
				height: 80rpx;
				line-height: 80rpx;
				text-align: center;
				width: 50%;
				color: #FFF;
				background: #d0cbcb;
			}
			.btn:last-child{
				background: #e89909;
			}
		}
	}
	
	.list{
		margin-top: 20rpx;
		// padding: 0 20rpx;
		.item{
			margin: 0 auto;
			margin-bottom: 20rpx;
			height: 250rpx;
			width: 710rpx;
			.imageUrl{
				height: 200rpx;
				width: 200rpx;
				border-radius: 20rpx;
				overflow: hidden;
			}
			image{
				max-height: 200rpx;
				max-width: 200rpx;
			}
			.info{
				height: 250rpx;
				width: 480rpx;
				margin-left: 30rpx;
				font-size: 24rpx;
				justify-content: space-around;
				align-items: flex-start;
				.title{
					font-size: 30rpx;
					height: 80rpx;
					width: 100%;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow : ellipsis; 
				}
				.bottom{
					width: 100%;
					.times{
						image{
							width: 30rpx;
							height: 30rpx;
						}
					}
				}
				.price text{
					font-size: 24rpx;
					color: red;
				}
			}
		}
	}
	::v-deep .uni-drawer__content{
		width: 600rpx !important;
	}
	.historyHeader{
		padding: 10rpx 20rpx;
		color: #000;
		font-size: 32rpx;
	}
	
	.history{
		padding: 10rpx 20rpx;
		flex-wrap: wrap;
		.historyItem{
			padding: 6rpx 20rpx;
			background: #ececec;
			font-size: 28rpx;
			color: #736f6f;
			margin-top: 20rpx;
			margin-left: 20rpx;
			border-radius: 20rpx;
		}
		i{
			height: 0;
			width: 150rpx;
		}
	}
	.listMode{
		width: 30rpx;
		height: 30rpx;
		margin-right: 20rpx;
		image{
			width: 100%;
			height: 100%;
		}
	}
	.list2{
		flex-wrap: wrap;
		.item{
			height: 515rpx;
			width: 350rpx;
			.imageUrl{
				width: 300rpx;
				height: 300rpx;
				margin-left: 25rpx;
				image{
					max-width: 300rpx;
					max-height: 300rpx;
				}
			}
			.info{
				font-size: 22rpx;
				width: 350rpx;
				height: 200rpx;
				margin-left: 0;
				box-sizing: border-box;
				padding: 0 20rpx;
				.title{
					font-size: 22rpx;
					width: 330rpx;
					height: 60rpx;
				}
				.price{
					width: 100%;
					image{
						width: 30rpx;
						height: 30rpx;
					}
				}
			}
		}
	}
	
</style>
