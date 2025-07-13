<template>
	<view class="collect page">
		<view class="sticky">
			
			<Title :type="7" :currentClasses="currentClasses"/>
			
			<Header @onSearch="onSearch" @keywordChange="keywordChange" :keyword="keyword" ref="header" page="none"/>
			
			<!-- <view class="header between">
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
			</view>
			
			<view class="currenClass between" v-if="hasClass && !isOnFocus">
				<view class="value">
					当前分类: {{currentClasses}}
				</view>
			</view> -->
			
			<view class="subclass" v-if="subClasses.length > 0">
				<view class="subtitle between">
					<view>类别细分</view>
					<view class="icon" @click="showSubClasses = !showSubClasses">
						<image v-if="!showSubClasses" src="@/static/icon/down.png"></image>
						<image v-if="showSubClasses" src="@/static/icon/up.png"></image>
					</view>
				</view>
				<view class="classList around" v-if="showSubClasses">
					<view class="classItem between"
						v-for="(item, index) in subClasses"
						:key="index"
						@click="toChild(item, index)"
					>
						<view class="label">{{item.cname}}</view>
						
						<uni-icons type="right" color="#aba9a9" size="20"></uni-icons>
						
					</view>
					<i></i>
				</view>
			</view>
		
			<view class="filter between" v-if="!isOnFocus">
				<view class="itemFilter center"
					v-for="(item, index) in tabs" :key="index"
					@click="chooseTab(item)">
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
				<view class="itemFilter center" @click="showDrawer">
					<view class="label">筛选</view>
					<view class="icons column icons2">
						<image src="@/static/filter.png"></image>
					</view>
				</view>
				<!-- <view class="listMode" @click="changeIsList">
					<image src="@/static/list.png" mode="" v-if="!isList"></image>
					<image src="@/static/menu.png" mode="" v-if="isList"></image>
				</view> -->
			</view>
		</view>
		
		<!-- <view class="currenClass currenClass2 between" v-if="!isOnFocus">
			<div class="center">
				<uni-data-checkbox multiple v-model="isFilter" :localdata="hobby"></uni-data-checkbox>
				<view class="value">
					过滤已售商品
				</view>
			</div>
		</view> -->
		
		<view
			class="list"
			v-if="!isOnFocus && isList"
		>
		<!-- :style="{
			height: `calc(100vh - ${navBarheight}px - 200rpx)`
		}" -->
			<view class="item center" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="img center">
					<image :src="item.thumbnails[0]" mode="aspectFit"></image>
					<image src="@/static/soldout.png" class="status" v-if="item.status === 'ITEM_STATUS_TRADING' || item.status === 'ITEM_STATUS_SOLD_OUT'"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.name}}</view>
					<view class="price"><text>{{item.price}}円</text></view>
					<!-- <view class="price"><text>{{item.price_rmb}}元</text></view> -->
					<image
						class="icon"
						src="@/static/icon/56.png"
					>
				</view>
			</view>
		</view>
		
		
		<view class="list list2 between" v-if="!isOnFocus && !isList">
			<view class="item" v-for="(item, index) in list" :key="index"
				@click="chooseItem(item)">
				<view class="img center">
					<image :src="item.thumbnails[0]" class="imageUrl" mode="aspectFit"></image>
					<image src="@/static/soldout.png" class="status" v-if="item.status === 'ITEM_STATUS_TRADING' || item.status === 'ITEM_STATUS_SOLD_OUT'"></image>
				</view>
				<view class="info column">
					<view class="title">{{item.name}}</view>
					<view class="price between" style="width: 100%">
						<view>编号：<text>{{item.id}}</text></view>
						<view @click.stop="copyCode(item.id)" style="border: 1px solid #c5c5c5; padding: 0px 3px; color: #423f3f;">复制</view>
					</view>
					<view class="price">现价：<text>{{item.price}}円</text></view>
					<view class="price"><text>{{item.price_rmb}}元</text></view>
				</view>
			</view>
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
							closeDrawer()
							onSearch()
						}">确定</view>
					</view>
				</view>
			</uni-drawer>
		</view>
		
		
	</view>
</template>

<script>
	let clasMenu = []
	import Title from "@/components/title.vue"
	import Header from "@/components/header.vue"
	export default {
		components: {Title, Header},
		data() {
			return {
				navBarheight: 0,
				currentClasses: '',	// 当前分类
				hasClass: false, // 有无分类参数
				keyword: '',
				list: [],
				page: 1,
				size: 100,
				
				tabs: [
					{id: 1, sort: 0, label: '价格', isCurrent: false},
					{id: 2, sort: 0, label: '时间', isCurrent: false},
				],
				rate: null,
				isFilter: [],
				hobby: [{value: 1}],
				defaultList: [],
				
				isOnFocus: false,
				isList: true,
				historyList: [],
				
				priceLimits: [
					{id: 0, label: '5000円以下'},
					{id: 1, label: '5000-1万円'},
					{id: 2, label: '1万-2万円'},
					{id: 3, label: '2万-5万円'},
					{id: 4, label: '5万円以上'},
				],
				currentPriceLimits: '',
				
				priceListMap: {
					0: [0, 5000],
					1: [5000, 10000],
					2: [10000, 20000],
					3: [20000, 50000],
					4: [50000, 999999999],
				},
				
				subClasses: [],
				showSubClasses: true,
				query: {},
				
				classArr: [],
			}
		},
		async onLoad(query) {
			this.query = query
			this.keyword = query.keyword || ''
			console.log('load', this.keyword)
			// 获取煤炉分类
			const classes = wx.getStorageSync('classes') || {}
			clasMenu = classes.mercariClass || []
			
			this.navBarheight = wx.getStorageSync('statusBarHeight')
			this.historyList = wx.getStorageSync('mercariHistory') || []
			this.rate = wx.getStorageSync('rate') || null
			this.query = query
			if(Object.keys(query).length > 0){
				this.hasClass = true
				this.setClass(query)
			}
			this.page = 1
			await this.getMercariSelectcates()
		},
		onReachBottom(){
			this.page++
			this.getMercariSelectcates()
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
		methods: {
			keywordChange(val){
				this.keyword = val
			},
			toChild(item, index){
				const {bigClass='', middleClass='', smallClass=''} = this.query
				wx.setStorage({ key: 'classArr' , data: this.classArr })
				const arr = item.path.split(',')
				const path = arr[arr.length - 1]
				const url = `/pages/mercari/mercariResult2?smallClass=${item.id}&from=1&path=${path}`
				
				wx.navigateTo({
					url,
				})
			},
			setClass(query){
				uni.showLoading()
				
				const arr = []
				const {bigClass='', middleClass='', smallClass='', from=''} = query
				
				if(!from){
					let bigClassIndex = ''
					clasMenu?.forEach((item, i)=>{
						if(item.id === bigClass){
							arr.push({
								label: item.cname,
								index: i,
								id: item.id
							})
							bigClassIndex = i
						}
					})
					let middileClassIndex = ''
					if(middleClass){
						clasMenu[bigClassIndex].children?.forEach((item, i)=>{
							if(item.id === middleClass){
								arr.push({
									label: item.cname,
									index: i,
									id: item.id
								})
								middileClassIndex = i
							}
						})
					}
					
					this.classArr = arr
				}else{
					const arr = wx.getStorageSync('classArr') || []
					
					let subClasses = []
					let new_clasMenu = clasMenu
					arr.forEach(item=>{
						subClasses = new_clasMenu[item.index].children
						new_clasMenu = subClasses
					})
					
					if(smallClass){
						new_clasMenu?.forEach((item, i)=>{
							if(item.id === smallClass){
								arr.push({
									label: item.cname,
									index: i,
									id: item.id
								})
							}
						})
					}
					
					this.classArr = arr
				}
				
				// 处理下级分类菜单
				this.setClassMenu()
				
				uni.hideLoading()
			},
			onChoose(item, key){
				if(this[key] === item.id){
					this[key] = ''
				}else{
					this[key] = item.id
				}
			},
			setClassMenu(){
				let currentClasses = []
				let subClasses = []
				let new_clasMenu = clasMenu
				this.classArr.forEach(item=>{
					subClasses = new_clasMenu[item.index].children
					new_clasMenu = subClasses
					currentClasses.push(item.label)
				})
				this.subClasses = subClasses
				this.currentClasses = currentClasses.join(' > ')
			},
			closeDrawer(){
				this.$refs['showRight'].close()
			},
			showDrawer(){
				this.$refs['showRight'].open()
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
			async getMercariSelectcates(){
				console.log('this.query', this.query)
				console.log('keyword', this.query.keyword)
				console.log('this.keyword', this.keyword)
				wx.showLoading({title: '加载中'})
				const length = this.classArr.length
				const urlid = this.query.path
				const res = await this.$api.getMercariSelectcates({
					urlid,
					pageSize: this.size,
					pageCount: this.page,
					keyWord: this.keyword,
					priceOderType: this.tabs[0].sort,
					isNewType: this.tabs[1].sort,
					lowPrice: this.currentPriceLimits === '' ? '' : this.priceListMap[this.currentPriceLimits][0],
					maxPrice: this.currentPriceLimits === '' ? '' : this.priceListMap[this.currentPriceLimits][1]
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
					item.sort = 1
				}else if(item.sort === 1){
					item.sort = 2
				}else if(item.sort === 2){
					item.sort = 1
				}
				this.onSearch()
			},
			chooseItem(item){
				if(item.status !== 'ITEM_STATUS_ON_SALE'){
					wx.showToast({
						icon: 'none',
						title: '当前商品状态不可购买'
					})
					return false
				}
				wx.navigateTo({
					url: '/pages/mercari/detail?id=' + item.id
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
				wx.setStorage({ key: 'mercariHistory' , data: this.historyList })
			},
			async onSearch(){
				this.isOnFocus = false
				this.setHistory()
				this.pagecount = 1
				this.list = []
				this.defaultList = []
				
				const res = await this.$api.getMercariGoodDetail({goodcode: this.keyword})
				if(res.data.code !== 500){
					wx.hideLoading()
					wx.navigateTo({
						url: `/pages/mercari/detail?id=${this.keyword}`,
					})
					return false
				}
				this.getMercariSelectcates()
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
	.sticky{
		position: sticky;
		top: 0;
		left: 0;
		z-index: 8;
		background: linear-gradient( #DAEBFD 0%, #DEEAFC 32%, #ECEAFC 58%, #F1EAFC 63%, #EFF8FD 83%, #FFFFFF 100%);
	}
	.header{
		height: 85rpx;
		padding: 0 20rpx;
		.uniInput{
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
		align-items: center;
		width: 750rpx;
		box-sizing: border-box;
		padding: 0 20rpx;
		margin-top: 20rpx;
		font-size: 26rpx;
		color: #000;
		opacity: 0.4;
		font-weight: 600;
		
	}
	.currenClass2{
		opacity: 1;
		color: #9a9a9a;
	}
	
	.filter{
		margin-top: 30rpx;
		padding-bottom: 30rpx;
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
		// padding: 0 20rpx;
		.item{
			margin: 0 auto;
			margin-bottom: 20rpx;
			height: 200rpx;
			width: 710rpx;
			.img{
				height: 200rpx;
				width: 200rpx;
				border-radius: 20rpx;
				overflow: hidden;
				image{
					max-height: 200rpx;
					max-width: 200rpx;
				}
			}
			.info{
				height: 200rpx;
				width: 480rpx;
				margin-left: 30rpx;
				font-size: 24rpx;
				justify-content: space-around;
				align-items: flex-start;
				position: relative;
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
				.icon{
					width: 54rpx;
					height: 58rpx;
					position: absolute;
					right: 18rpx;
					bottom: 18rpx;
				}
			}
		}
	}
	::v-deep .uni-drawer__content{
		width: 600rpx !important;
	}
	::v-deep .uni-data-checklist .checklist-group .checklist-box{
		margin-right: 0 !important;
		border: 1xp ;
	}
	::v-deep .uni-data-checklist .checklist-group .checklist-box.is--default.is-checked .checkbox__inner {
		border-color: #f9bf3b !important;
		background-color: #f9bf3b !important;
	}
	.list .img{
		position: relative;
		.status{
			position: absolute;
			top: 0;
			left: 0;
		}
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
	.list2{
		flex-wrap: wrap;
		.img{
			position: relative;
		}
		.item{
			height: 515rpx;
			width: 350rpx;
			.img{
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
	
	.listMode{
		width: 30rpx;
		height: 30rpx;
		margin-right: 20rpx;
		image{
			width: 100%;
			height: 100%;
		}
	}

.subclass{
	margin: 30rpx;
	.subtitle{
		.icon image{
			width: 40rpx;
			height: 40rpx;
		}
	}
	.classList{
		margin-top: 30rpx;
		flex-wrap: wrap;
		.classItem{
			width: 300rpx;
			margin: 8rpx 0;
			height: 50rpx;
			background-color: #fff;
			box-sizing: border-box;
			padding: 0 20rpx;
			font-size: 24rpx;
			border-radius: 20rpx;
			
			.icon{
				width: 20rpx;
				height: 20rpx;
			}
		}
		i{
			width: 300rpx;
			height: 0;
		}
	}
}
</style>
