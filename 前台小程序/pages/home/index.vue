<template>
	<view class="page home">
		<!-- <view class="header_sticky"> -->
			<Title :type="2"/>
		<!-- </view> -->
		
		<view class="swiper">
			<swiper
			  :indicator-dots="banners.length !== 1"
			  circular>
			  <block v-for="(item, index) in banners" :key="index">
			    <swiper-item>
			      <image :src="item.url" class="slide-image" />
			    </swiper-item>
			  </block>
			</swiper>
		</view>
		
		<!-- 平台tab -->
		<view class="plant between">
			<image
				class="item"
				src="@/static/icon/2.png"
				@click="toPath(`/pages/yahoo/index`)"
			>
			<image
				class="item"
				src="@/static/icon/3.png"
				@click="toPath(`/pages/mercari/index`)"
			>
		</view>
		
		<!-- 雅虎大类 -->
		<view class="menu menu2 flex">
			<view class="item column" 
				v-for="(item, index) in menuList" :key="index"
				@click="onMenuClick(item)"
			>
				<image :src="item.icon"></image>
				<text>{{item.label}}</text>
			</view>
		</view>
		
		<!-- 登录模块 -->
		<view class="loginNav" v-if="!isLogin">
			<image
				class="bg"
				src="@/static/icon/10.png"
			>
			<image
				class="sign"
				src="@/static/icon/11.png"
				@click="showLogin = true"
			>
			<image
				class="login"
				src="@/static/icon/12.png"
				@click="showLogin = true"
			>
		</view>
		
		
		<view class="menu around">
			<view class="item column" 
				v-for="(item, index) in menuList2" :key="index"
				@click="toPath(item.url)"
			>
				<image :src="item.icon"></image>
				<!-- <text>{{item.label}}</text> -->
			</view>
		</view>
		
		
		<view class="notice between">
			<image
				class="icon"
				src="@/static/icon/13.png"
			>
			<swiper
			  circular
			  autoplay
			  vertical>
			  <block v-for="(item, index) in noticeList" :key="index">
			    <swiper-item>
					<view class="item">
						<view class="nav">
							<uni-notice-bar
								background-color="none"
								color="#000"
								:text="item.title"
								style="width: 100%"
							/>
						</view>
					</view>
			    </swiper-item>
			  </block>
			</swiper>
		</view>
		
		<!-- <view class="goods">
			<view class="list list2 between">
				<view class="item" v-for="(item, index) in list" :key="index"
					@click="chooseItem(item)">
					<view class="imageUrl center">
						<image :src="item.imageUrl" mode="aspectFit"></image>
					</view>
					<view class="info column">
						<view class="title">{{item.title}}</view>
						<view class="saler">出品者：{{item.seller}}</view>
						<view class="price"><text>{{item.jpprice}}円</text></view>
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
		</view> -->
		
		<view
			v-for="(item, index) in childrenList"
			:key="index"
		>
			<view class="subtitle center">
				<view class="line"></view>
				<view class="name center">{{item.title}}</view>
			</view>
			
			<view class="classList between">
				<view class="item column"
					v-for="(it, ii) in item.children" :key="ii"
					@click="onSecondMenuClick(item, it)"
				>
					<image
						class="icon"
						src="@/static/yahoo-fill.png"
					>
					<view class="label">{{it.name_cn}}</view>
				</view>
				<i></i>
				<i></i>
				<i></i>
			</view>
		</view>
		
		
		
		<Login :showLogin="showLogin" @close="closePop" @loginSuccess="loginSuccess" />
	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	import Title from "@/components/title.vue"
	import Search from "@/components/search.vue"
	import Login from "@/components/login.vue"
	
	
	export default {
		components: {Title, Search, Login},
		data() {
			return {
				banners: [],
				noticeList: [],
				menuList: [
					{id: '20000', label: '收藏艺术', icon: '/static/icon/4.png',},
					{id: '22152', label: '影音娱乐', icon: '/static/icon/5.png'},
					{id: '23632', label: '数码家电', icon: '/static/icon/6.png'},
					{id: '25464', label: '游戏动漫', icon: '/static/icon/7.png'},
					{id: '23000', label: '时尚穿搭', icon: '/static/icon/8.png'},
					{id: '23140', label: '首饰手表', icon: '/static/icon/9.png'},
					// {id: 'jiu', label: '酒类厨刀', icon: '/static/jiubeihongjiu.png'},
					// {id: '0', label: '全部分类', icon: '/static/caidan.png'},
				],
				childrenList: [],
				currentClass: {},
				menuList2: [
					{id: '20000', label: '竞拍流程', icon: '/static/icon/42.png', url: '/pages/mine/progress'},
					{id: '22152', label: '费用说明', icon: '/static/icon/43.png', url: '/pages/mine/rules'},
					{id: '23632', label: '帮助中心', icon: '/static/icon/44.png', url: '/pages/mine/questions'},
				],
				list: [],
				isLogin: false,
				showLogin: false,
			}
		},
		async onLoad() {
			const ids = []
			clasMenu.forEach(item=>{
				item.children?.forEach(it=>{
					ids.push(it.id)
				})
			})
			
			let result = Math.floor(Math.random()*149)
			if(result>=148){
				result = 148
			}
			if(result<0){
				result = 0
			}
			
			await this.getSwiperList()
			// await this.selectcates(ids[result])
			
			await this.getNoticeList()
			
			this.setChildrenList()
		},
		onShow(){
			this.isLogin = wx.getStorageSync('login') || false
		},
		methods: {
			loginSuccess(){
				this.isLogin = true
				this.showLogin = false
			},
			chooseItem(item){
				const {seller, code, breakurl} = item
				wx.navigateTo({
					url: `/pages/yahoo/detail?code=${item.code}`,
				})
			},
			async selectcates(urlid){
				wx.showLoading({title: '加载中'})
				const res = await this.$api.getYahooGoodList({
					pagecount: 50,
					urlid,
					keyword: '',
					priceType: '',
					isNewType: '',
					selltype: '',
					priceOderType: '',
					timeType: '',
					hotType: '',
				})
				const {data} = res.data
				
				data?.forEach(item=>{
					item.price_rmb = parseInt((item.jpprice * this.rate) * 100) / 100
				})
				this.list = this.list.concat(data)
				wx.hideLoading()
			},
			
			seeImg(){
				const fs = wx.getFileSystemManager()
				fs.readFile({
				  filePath: `/static/chargeList.png`,
				  encoding: 'base64',
				  position: 0,
				  success(res) {
					const url = `data:image/jpeg;base64,${res.data}`
					wx.previewImage({
					  current: url,
					  urls: [url],
					})
				  },
				})
			},
			openSearchDialog(){
				this.$refs.searchDialog.open()
			},
			openChargeDialog(){
				wx.navigateTo({
					url: '/pages/mine/rules'
				})
				// this.$refs.chargeDialog.open()
			},
			openYahooDialog(){
				this.$refs.yahooDialog.open()
			},
			async getSwiperList(){
				wx.showLoading({title: '加载中'})
				const res = await this.$api.getSwiperList()
				const { data, code, msg } = res.data
				if(code === 0){
					this.banners = data
					wx.hideLoading()
				}else{
					wx.hideLoading()
					wx.showToast({title: msg})
				}
			},
			async getNoticeList(){
				wx.showLoading({title: '加载中'})
				const res = await this.$api.getNoticetList()
				const { data, code, msg } = res.data
				if(code === 0){
					this.noticeList = data
					wx.hideLoading()
				}else{
					wx.hideLoading()
					wx.showToast({title: msg})
				}
			},
			getMore() {
				wx.navigateTo({
					url: '/pages/home/notice'
				})
			},
			showEmsCharge(){
				const fs = wx.getFileSystemManager()
				fs.readFile({
				  filePath: `/static/ems.jpg`,
				  encoding: 'base64',
				  position: 0,
				  success(res) {
					const url = `data:image/jpeg;base64,${res.data}`
					wx.previewImage({
					  current: url,
					  urls: [url],
					})
				  },
				})
			},
			showSeaCharge(){
				const fs = wx.getFileSystemManager()
				fs.readFile({
				  filePath: `/static/sea.jpg`,
				  encoding: 'base64',
				  position: 0,
				  success(res) {
					const url = `data:image/jpeg;base64,${res.data}`
					wx.previewImage({
					  current: url,
					  urls: [url],
					})
				  },
				})
			},
			onSecondMenuClick(item, it){
				wx.navigateTo({
					url: `/pages/yahoo/yahooResult?bigClass=${item.id}&middleClass=${it.id}`
				})
			},
			onMenuClick(item){
				
				// this.currentClass = item
				
				// clasMenu.forEach(it=>{
				// 	if(it.id === item.id){
				// 		this.childrenList = it.children
				// 	}
				// })
				
				wx.setStorage({
					key: 'yahooId',
					data: item.id
				})
				wx.navigateTo({
					url: `/pages/yahoo/yahooResult?bigClass=${item.id}`
				})
			},
			
			setChildrenList(){
				const list = []
				this.menuList?.forEach(item=>{
					clasMenu.forEach(it=>{
						if(item.id === it.id){
							const obj = {
								title: item.label,
								id: item.id,
								children: it.children
							}
							list.push(obj)
						}
					})
				})
				this.childrenList = list
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
			},
			
			closePop(){
				this.showLogin = false
			},
			
			toPath(url, val){
				if(val){
					uni.switchTab({
						url,
					})
				}else{
					wx.navigateTo({
						url,
					})
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.search{
		.icon{
			width: 50rpx;
			height: 50rpx;
		}
	}
	.notice{
		margin: 44rpx auto 20rpx;
		width: 670rpx;
		height: 72rpx;
		background-color: #F7F7F9;
		.icon{
			width: 78rpx;
			height: 44rpx;
			margin-right: 56rpx;
		}
		swiper{
			width: 610rpx;
			height: 44rpx;
		}
		
		
		.item{
			width: 78rpx;
			height: 44rpx;
			align-items: center;
		}
		.nav{
			width: 610rpx;
		}
	}
	.swiper{
		margin-top: 30rpx;
		width: 750rpx;
		height: 225rpx;
		swiper{
			width: 750rpx;
			height: 225rpx;
			.slide-image{
				width: 750rpx;
				height: 225rpx;
				display: block;
			}
		}
	}
	
	.swiperfoonter{
		padding: 0 30rpx;
		.item{
			font-size: 28rpx;
			.icon{
				width: 30rpx;
				height: 30rpx;
			}
			.arrow{
				width: 30rpx;
				height: 30rpx;
			}
			.text{
				margin-left: 10rpx;
			}
		}
	}
	
	.plant{
		padding: 0 40rpx;
		.item{
			width: 320rpx;
			height: 125rpx;
		}
	}
	
	.menu{
		margin: 40rpx auto;
		width: 570rpx;
		flex-shrink: 0;
		justify-content: space-between;
		overflow: auto;
		.item{
			flex-shrink: 0;
			font-size: 26rpx;
			margin-right: 30rpx;
			image{
				width: 112rpx;
				height: 148rpx;
			}
			text{
				margin-top: 16rpx;
				font-size: 26rpx;
				color: #000000;
			}
		}
	}
	.menu2{
		width: 670rpx;
		.item image{
			width: 94rpx;
			height: 94rpx;
		}
	}
	
	.loginNav{
		width: 660rpx;
		height: 165rpx;
		margin: 46rpx auto;
		position: relative;
		image{
			position: absolute;
		}
		.bg{
			width: 660rpx;
			height: 165rpx;
			top: 0;
			left: 0;
		}
		.sign{
			width: 220rpx;
			height: 54rpx;
			left: 58rpx;
			bottom: 36rpx;
		}
		.login{
			width: 220rpx;
			height: 54rpx;
			right: 70rpx;
			bottom: 36rpx;
		}
	}
	
	.charge{
		margin: 30rpx 0;
		width: 750rpx;
		min-height: 200rpx;
		position: relative;
		border-radius: 30rpx;
		padding: 30rpx;
		box-sizing: border-box;
		overflow: hidden;
		image{
			width: 100%;
			height: 100%;
			position: absolute;
			top: 0;
			left: 0;
			z-index: -1;
			// opacity: 0.5;
		}
		.content{
			max-height: 70vh;
			overflow-y: auto;
			overflow-x: hidden;
			font-size: 26rpx;
			.hr{
				width: 690rpx;
				border-bottom: 1px dashed #b7b7b7;
				margin-top: 30rpx;
			}
			.substite{
				font-size: 30rpx;
				font-weight: 600;
				margin: 20rpx 0;
				text-decoration: underline;
			}
			.item{
				margin-top: 20rpx;
				display: flex;
				padding-bottom: 10rpx;
				border-bottom: 1px solid #dedada;
				.lebal{
					width: 200rpx
				}
				.value{
					width: 500rpx;
					br{
						margin-top: 10rpx;
					}
					text{
						color: #0f16f3;
					}
				}
				.tips{
					line-height: 48rpx;
					font-size: 28rpx;
					text{
						color: red;
					}
				}
			}
			.item2{
				padding-bottom: 0;
				border-bottom: none;
			}
			.title{
				font-size: 28rpx;
				font-weight: 600;
			}
			.chargeList{
				position: relative;
				z-index: 1;
				width: 650rpx;
				margin-left: 25rpx;
				margin-top: 30rpx;
			}
		}
	}
	.floatBtns{
		.btn{
			flex-wrap: wrap;
			position: fixed;
			top: 200rpx;
			width: 40rpx;
			background-color: #f7cb68;
			color: #fff;
			// height: ;
			text-align: center;
			padding: 15rpx 0;
			border-radius: 10rpx;
			font-size: 26rpx;
		}
		
		.search{
			right: 0;
		}
		.charge{
			right: 0;
			top: 400rpx;
		}
	}
	
	.banners{
		.banner{
			width: 690rpx;
			height: 200rpx;
			margin: 20rpx auto;
			position: relative;
			font-weight: 500;
			font-size: 40rpx;
			color: #000;
			-webkit-text-stroke: 1rpx #616060;
			letter-spacing: 20rpx;
			image{
				width: 100%;
				height: 100%;
				position: absolute;
				top: 0;
				left: 0;
				z-index: -1;
			}
		}
	}
	.popup-content{
		position: relative;
	}
	.subTitle{
		margin-top: 50rpx;
		width: 100vw;
		font-size: 36rpx;
		font-weight: 600;
		image{
			width: 60rpx;
			height: 60rpx;
			margin-right: 20rpx;
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
	.list2{
		margin-top: 50rpx;
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

.subtitle{
	width: 750rpx;
	height: 50rpx;
	position: relative;
	.line{
		width: 80%;
		height: 4rpx;
		background-color: #F4F4F4;
	}
	.name{
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		z-index: 1;
		font-size: 30rpx;
		font-weight: 600;
		width: 150rpx;
		background-color: #fff;
	}
}
.classList{
	margin: 50rpx 30rpx;
	width: 690rpx;
	flex-wrap: wrap;
	.item{
		width: 148rpx;
		margin-bottom: 20rpx;
		image{
			width: 148rpx;
			height: 148rpx;
			margin-bottom: 10rpx;
		}
		.label{
			font-size: 20rpx;
		}
	}
	i{
		width: 148rpx;
		height: 0;
	}
}


::v-deep .uni-noticebar{
	padding: 0 !important;
}
</style>
