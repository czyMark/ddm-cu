<template>
	<view class="collect">
		<Title :type="3" @onSearch="onSearch"/>
		
		<view class="plant flex">
			<image
				class="item"
				v-for="(item, index) in plants" :key="index"
				@click="choosePlants(item)"
				:src="currentPlant === item.id ? item.icon : item.icon2"
			>
		</view>
		
		<!-- 雅虎 -->
		<view class="filter flex" v-if="currentPlant === 1">
			<view :class="['itemFilter center']" @click="resetYahooSearch">
				<view :class="['label', currentYahooTab === '' ? 'active' : '1']">推荐</view>
			</view>
			<view class="itemFilter center"
				v-for="(item, index) in yahoo_tabs" :key="index"
				@click="chooseYahooTab(item)">
				<view :class="['label', item.isCurrent ? 'active' : '']">{{item.label}}</view>
				<view class="icons column" v-if="item.sort === 0">
					<image src="@/static/icon/22.png"></image>
					<image src="@/static/icon/24.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 2">
					<image src="@/static/icon/21.png"></image>
					<image src="@/static/icon/24.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 1">
					<image src="@/static/icon/22.png"></image>
					<image src="@/static/icon/23.png"></image>
				</view>
			</view>
			<!-- <view class="listMode" @click="changeIsList">
				<image src="@/static/list.png" mode="" v-if="!isList"></image>
				<image src="@/static/menu.png" mode="" v-if="isList"></image>
			</view> -->
		</view>
		
		<!-- 煤炉 -->
		<view class="filter flex" v-if="currentPlant === 2">
			<view :class="['itemFilter center']" @click="resetMercariSearch">
				<view :class="['label', currentMercariTab === '' ? 'active' : '1']">推荐</view>
			</view>
			<view class="itemFilter center"
				v-for="(item, index) in mercari_tabs" :key="index"
				@click="chooseMetcariTab(item)">
				<view :class="['label', item.isCurrent ? 'active' : '']">{{item.label}}</view>
				<view class="icons column" v-if="item.sort === 0">
					<image src="@/static/icon/22.png"></image>
					<image src="@/static/icon/24.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 2">
					<image src="@/static/icon/21.png"></image>
					<image src="@/static/icon/24.png"></image>
				</view>
				<view class="icons column" v-if="item.sort === 1">
					<image src="@/static/icon/22.png"></image>
					<image src="@/static/icon/23.png"></image>
				</view>
			</view>
			<!-- <view class="listMode" @click="changeIsList">
				<image src="@/static/list.png" mode="" v-if="!isList"></image>
				<image src="@/static/menu.png" mode="" v-if="isList"></image>
			</view> -->
		</view>
		
		
		
		<view class="list list2 between" v-if="!isOnFocus && !isList && currentPlant === 1">
			<view class="item column" v-for="(item, index) in list" :key="index"
				@click="chooseYahooItem(item)">
				<view class="imageUrl center">
					<image class="main" :src="item.imageUrl" mode="aspectFit"></image>
					<image
						v-show="currentPlant === 1"
						class="logo"
						src="@/static/icon/14.png"
					>
					<image
						v-show="currentPlant === 2"
						class="logo"
						src="@/static/icon/17.png"
					>
				</view>
				<view class="info column">
					<view class="title">{{item.title}}</view>
					<view class="price">{{item.jpprice}}日元</view>
				</view>
			</view>
		</view>
		
		
		<view class="list list2 between" v-if="!isOnFocus && !isList && currentPlant === 2">
			<view class="item column" v-for="(item, index) in list" :key="index"
				@click="chooseMercariItem(item)">
				<view class="imageUrl center">
					<image class="main" :src="item.thumbnails[0]" mode="aspectFit"></image>
					<image
						v-show="currentPlant === 1"
						class="logo"
						src="@/static/icon/14.png"
					>
					<image
						v-show="currentPlant === 2"
						class="logo"
						src="@/static/icon/17.png"
					>
				</view>
				<view class="info column">
					<view class="title">{{item.name}}</view>
					<view class="price">{{item.price}}日元</view>
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
							onYahooSearch()
						}">确定</view>
					</view>
				</view>
			</uni-drawer>
		</view>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	import clasMenu from "@/utils/yahooClass.js"
	export default {
		components: {Title},
		data() {
			return {
				currentClasses: '',	// 当前分类
				hasClass: false, // 有无分类参数
				keyword: '',
				list: [],
				pagecount: 1,
				
				currentPlant: 1, // 1雅虎  2煤炉
				plants: [
					{id: 1, icon: '/static/icon/14.png', icon2: '/static/icon/15.png'},
					{id: 2, icon: '/static/icon/17.png', icon2: '/static/icon/16.png'},
				],
				
				currentYahooTab: '',
				yahoo_tabs: [
					{id: 3, sort: 0, label: '人气', isCurrent: false},
					{id: 1, sort: 0, label: '价格', isCurrent: false},
					{id: 2, sort: 0, label: '最新', isCurrent: false},
				],
				
				mercari_tabs: [
					{id: 1, sort: 0, label: '价格', isCurrent: false},
					{id: 2, sort: 0, label: '最新', isCurrent: false},
				],
				currentMercariTab: '',
				
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
				isList: false,
				historyList: [],
				
				
				
				isFilter: [],
				hobby: [{value: 1}],
				defaultList: [],
			}
		},
		watch: {
			isFilter: {
				handler(val){
					if(val.length > 0){
						const list = []
						this.defaultList.map(item=>{
							if(item.status === "ITEM_STATUS_ON_SALE"){
								list.push(item)
							}
						})
						this.list = list
					}else{
						this.list = JSON.parse(JSON.stringify(this.defaultList))
					}
				},
				immediate: true
			}
		},
		onLoad(query) {
			this.historyList = wx.getStorageSync('yahooHistory') || []
			this.rate = wx.getStorageSync('rate') || null
			
			if(this.currentPlant === 1){
				// 查询商品列表
				const params = {
					urlid: '',
					pagecount: this.pagecount
				}
				this.selectcates(params)
			}else{
				this.getMercariSelectcates()
			}
		},
		onReachBottom(){
			this.pagecount++
			const params = {
				urlid: '',
				pagecount: this.pagecount
			}
			this.selectcates(params)
		},
		methods: {
			onSearch(val){
				this.list = []
				this.pagecount = 1
				this.keyword = val
				if(this.currentPlant === 1){
					// 查询商品列表
					const params = {
						urlid: '',
						pagecount: this.pagecount
					}
					this.selectcates(params)
				}else{
					this.getMercariSelectcates()
				}
			},
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
					urlid: '22436',
					keyword: this.keyword,
					priceType: this.currentPriceLimits,
					isNewType: this.currentOldOrNew,
					selltype: this.currentSalerType,
					priceOderType: this.yahoo_tabs[0].sort,
					timeType: this.yahoo_tabs[1].sort,
					hotType: this.yahoo_tabs[2].sort,
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
			choosePlants(item){
				this.list = []
				this.pagecount = 1
				this.currentPlant = item.id
				if(item.id === 1){
					this.selectcates()
				}else{
					this.getMercariSelectcates()
				}
			},
			async getMercariSelectcates(){
				wx.showLoading({title: '加载中'})
				
				const res = await this.$api.getMercariSelectcates({
					urlid: 11,
					pageSize: 100,
					pageCount: 1,
					keyWord: this.keyword,
					priceOderType: this.mercari_tabs[0].sort,
					isNewType: this.mercari_tabs[1].sort,
					lowPrice: '',
					maxPrice: ''
					
				})
				const {data} = res.data
				if(data.items.length < 50){
					this.noMore = true
				}
				data.items?.forEach(item=>{
					item.price_rmb = parseInt((item.price * this.rate) * 100) / 100
					if(item.thumbnails[0].indexOf("@webp") === -1){
						item.thumbnails[0] = item.thumbnails[0].replace('https://', 'https://imghk.ripai.com/')
					}
				})
				this.defaultList = this.defaultList.concat(data.items)
				if(this.isFilter.length > 0){
					const list = []
					this.defaultList.map(item=>{
						if(item.status === "ITEM_STATUS_ON_SALE"){
							list.push(item)
						}
					})
					this.list = list
				}else{
					this.list = JSON.parse(JSON.stringify(this.defaultList))
				}
				wx.hideLoading()
			},
			chooseYahooTab(item){
				this.currentYahooTab = item.id
				if(!item.isCurrent){
					this.yahoo_tabs.forEach(it=>{
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
				this.onYahooSearch()
			},
			
			chooseMetcariTab(item){
				this.currentMercariTab = item.id
				if(!item.isCurrent){
					this.mercari_tabs.forEach(it=>{
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
				this.onMercariSearch()
			},
			
			chooseYahooItem(item){
				const {seller, code, breakurl} = item
				wx.navigateTo({
					url: `/pages/yahoo/detail?code=${item.code}`,
				})
			},
			chooseMercariItem(item){
				wx.navigateTo({
					url: '/pages/mercari/detail?id=' + item.id
				})
			},
			onHistoryTap(val){
				this.keyword = val
				this.onYahooSearch()
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
			resetYahooSearch(){
				this.currentYahooTab = ''
				this.yahoo_tabs.forEach(item=>{
					item.sort = 0
				})
				this.onYahooSearch()
			},
			resetMercariSearch(){
				this.currentMercariTab = ''
				this.mercari_tabs.forEach(item=>{
					item.sort = 0
				})
				this.onMercariSearch()
			},
			async onYahooSearch(){
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
					priceOderType: this.yahoo_tabs[0].sort,
					timeType: this.yahoo_tabs[1].sort,
					hotType: this.yahoo_tabs[2].sort,
				}
				this.selectcates(params)
				this.closeDrawer()
			},
			
			async onMercariSearch(){
				this.list = []
				this.defaultList = []
				
				const res = await this.$api.getMercariGoodDetail({goodcode: this.keyword})
				console.log('res', res)
				if(res.data.code !== 500){
					wx.hideLoading()
					wx.navigateTo({
						url: `/pages/mercari/detail?id=${this.keyword}`,
					})
					return false
				}
				this.getMercariSelectcates()
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
	
	
	.plant{
		padding: 20rpx 40rpx;
		.item{
			width: 146rpx;
			height: 58rpx;
			margin-right: 30rpx;
		}
	}
	
	.filter{
		margin-top: 30rpx;
		margin-left: 40rpx;
		align-items: center;
		.itemFilter{
			margin-right: 70rpx;
			.icons{
				margin-left: 10rpx;
				image{
					width: 20rpx;
					height: 20rpx;
				}
			}
			.label{
				color: #AFAFAF;
			}
			.active{
				color: #000;
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
		width: 670rpx;
		margin: 25rpx auto;
		flex-wrap: wrap;
		.item{
			border-radius: 12rpx;
			overflow: hidden;
			margin-bottom: 30rpx;
			width: 310rpx;
			box-shadow: 0px 0px 19px -5px #AFAFAF;
			.imageUrl{
				width: 310rpx;
				height: 232rpx;
				position: relative;
				.main{
					border-radius: 12rpx;
					max-width: 310rpx;
					max-height: 232rpx;
				}
				.logo{
					width: 88rpx;
					height: 34rpx;
					position: absolute;
					bottom: 8rpx;
					left: 4rpx;
				}
			}
			.info{
				width: 290rpx;
				margin-left: 0;
				box-sizing: border-box;
				.title{
					font-weight: 600;
					margin: 10rpx 0 20rpx;
					font-size: 32rpx;
					width: 290rpx;
					overflow : hidden;              //超出部分隐藏
					white-space : nowrap;       //不换行
					text-overflow : ellipsis;       //超出部分...代替
				}
				.price{
					font-weight: 600;
					color: red;
					width: 100%;
					padding-bottom: 10rpx;
					image{
						width: 30rpx;
						height: 30rpx;
					}
				}
			}
		}
	}
	
</style>
