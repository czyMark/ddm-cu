<template>
	<view class="home">
		<!-- <Header logo="日代拍" page="home"></Header> -->
		
		<view class="swiper">
			<swiper
			  indicator-dots
			  circular>
			  <block v-for="(item, index) in banners" :key="index">
			    <swiper-item>
			      <image :src="item.url" class="slide-image" />
			    </swiper-item>
			  </block>
			</swiper>
		</view>
		<view class="swiperfoonter around">
			<view class="item center" @click="openSearchDialog">
				<image src="@/static/search.png" mode=""></image>
				<view class="text">搜索问题</view>
			</view>
			<view class="item center" @click="openChargeDialog">
				<image src="@/static/Gold_ico.png" mode=""></image>
				<view class="text">收费标准</view>
			</view>
			<view class="item center" @click="openYahooDialog">
				<image src="@/static/yahoo2.png" mode=""></image>
				<view class="text">关于雅虎竞拍</view>
			</view>
		</view>
		
		<!-- <view class="notice between">
			<swiper
			  circular
			  autoplay
			  vertical>
			  <block v-for="(item, index) in noticeList" :key="index">
			    <swiper-item>
			      <uni-notice-bar show-get-more show-icon more-text="查看更多"
					:text="item.title"
			      @getmore="getMore" style="width: 100%"/>
			    </swiper-item>
			  </block>
			</swiper>
		</view> -->
		
		<view class="subTitle center">
			<image src="../../static/class.png" mode=""></image>
			<view>热门分类</view>
		</view>
		<view class="menu flex">
			<view class="item column" 
				v-for="(item, index) in menuList" :key="index"
				@click="onMenuClick(item)"
			>
				<image :src="item.icon"></image>
				<text>{{item.label}}</text>
			</view>
		</view>
		
		<view class="subTitle center">
			<image src="../../static/good.png" mode=""></image>
			<view>新品推荐</view>
		</view>
		
		<view class="list list2 between">
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
		
		
		
		
		<!-- <view class="floatBtns">
			<view class="btn center search">搜索问题</view>
			<view class="btn center charge">收费明细</view>
			<view class="btn center yahoo">关于雅虎出价竞拍</view>
		</view> -->
		
		<!-- <view class="banners">
			<view class="banner center search" @click="openSearchDialog">
				搜索问题
				<image src="@/static/searchBg.png" mode=""></image>
			</view>
			<view class="banner center charge" @click="openChargeDialog">
				与其它平台费用比较
				<image src="@/static/chargeBg.png" mode=""></image>
			</view>
			<view class="banner center yahoo" @click="openYahooDialog">
				关于雅虎出价竞拍
				<image src="@/static/yahooBg.png" mode=""></image>
			</view>
		</view> -->
		
		
		<uni-popup ref="searchDialog" mask-background-color="rgba(0,0,0,0.8)">
			<view class="popup-content charge">
				<image src="@/static/searchBg.png"></image>
				<view class="content">
					<view class="substite center">搜索问题</view>
					<view class="item item2">
						<view class="tips">
							日代拍可以用<text>关键词搜索</text>和<text>编码搜索</text>
							<br/>
							<text>编码搜索</text>没有分类限制，可以在任何一个分类下直接搜索到产品。
							<br/>
							使用<text>关键词搜索</text>要先选择大分类，再进行搜索。
							<br/>
							也可以在别的小程序搜索产品，然后直接用<text>编码搜索</text>在日代拍下单。
						</view>
					</view>
				</view>
			</view>
		</uni-popup>
		
		<uni-popup ref="chargeDialog" mask-background-color="rgba(0,0,0,0.8)">
			<view class="popup-content charge">
				<image src="@/static/chargeBg.png"></image>
					<!-- <view class="content">
						<view class="substite center">收费明细</view>
						<view class="item">
							<view class="lebal">费用组成</view>
							<view class="value">货款+日本国内运费+代购费+国际运费</view>
						</view>
						<view class="item">
							<view class="lebal">货款</view>
							<view class="value">产品的成交价格</view>
						</view>
						<view class="item">
							<view class="lebal">日本国内运费</view>
							<view class="value">分为 卖家包邮，买家下单以后显示运费，还有到付运费</view>
						</view>
						<view class="item">
							<view class="lebal">代购费</view>
							<view class="value">日代拍所收取的代购费用</view>
						</view>
						<view class="item">
							<view class="lebal">国际运费</view>
							<view class="value">日本发到买家手里的快递费用，包括邮局EMS 海运.UPS.包税专线 运到全世界</view>
						</view>
						<view class="item">
							<view class="lebal">代购费</view>
							<view class="value">
								在日代拍下单只收代购费，无其它隐形收费，无银行转账费，无跨境支付手续费。
								<br/>
								代购费如下：
								<br/>
								产品价值5W日元以内      每单40元
								<br/>
								产品价值5W-20W日元      每单100元
								<br/>
								产品价值20W日元以上     每单商品含税价格的3%
							</view>
						</view>
						<view class="item item2">
							<view class="lebal">国际运费</view>
							<view class="value">
								<text @click="showEmsCharge">
									邮局EMS 运费价格表 (点击查看)
								</text>
								<br/>
								<text @click="showSeaCharge">
									海运 运费价格表 (点击查看)
								</text>
								<br/>
								人民币价格仅为参考.以日元为准
								<br/>
								UPS 适合体积重量超大的货物 请直接咨询客服
								<br/>
								包税包清关路线  每公斤100元（不足1公斤按1公斤计费）
								适用于普通日用品 化妆品 衣服 保健品偶像周边等普通杂货
								一个包裹3公斤以内，总价值5万日元以内，产品数量20个左右
								普通鞋或者电子产品 小牌手表 小牌珠宝首饰等产品每个包裹限发一个
								体积重（长宽高cm相乘除以6000）比实际重量大的时候以体积重计费
								<br/>
								其它路线 请咨询客服
								<br/>
							</view>
						</view>
						<view class="hr center"></view>
						<view class="substite center">以下为非必要收费</view>
							
						
						<view class="item">
							<view class="lebal">合箱费</view>
							<view class="value">
								非包税渠道免费合箱5个包裹，包税渠道免费合箱10个包裹
								<br/>
								超过每个包裹加收5元手续费
							</view>
						</view>
						<view class="item">
							<view class="lebal">加固包装</view>
							<view class="value">
								非包税渠道根据情况收取10-100元
								<br/>
								包税渠道免费
							</view>
						</view>
						<view class="item">
							<view class="lebal">纸箱费</view>
							<view class="value">
								再生纸箱免费
								<br/>
								新纸箱根据实际情况收取5-30元
							</view>
						</view>
						<view class="item">
							<view class="lebal">拍照查验费</view>
							<view class="value">
								根据情况收取10-100元
							</view>
						</view>
						<view class="item">
							<view class="lebal">仓储费</view>
							<view class="value">
								日代拍免费仓储时间为产品到达库房30天以内，普通商品超过30天每天10元/件，大件商品超过30天每天30元/件，当仓储费超过产品价值或者产品到达库房90天买家无联系且未付后续费用，视为买家自动放弃，该产品将予以销毁。退运包裹免费仓储5天，超过5天按以上条款收费。
							</view>
						</view>
						<view class="item">
							<view class="lebal">退运手续费</view>
							<view class="value">
								包裹因买家原因导致退运日本，从新发送收取100元手续费，如需拆分，拆分每个包裹加收50元。
							</view>
						</view>
						<view class="item">
							<view class="lebal">转寄日本国内手续费</view>
							<view class="value">
								当买家要求将包裹转寄日本国内，收取货物价值的5%手续费。
							</view>
						</view>
						<view class="item item2">
							<view class="lebal">大件超重物品手续费</view>
							<view class="value">
								重量20-30公斤或者体积长宽高之和200-300CM，每个包裹加收200元手续费。
								<br/>
								重量30公斤以上或者体积长宽高之和大于300CM，每个包裹加收1000元手续费。
							</view>
						</view>
					</view> -->
				<view class="content">
					<view class="title">本平台汇率略高，但总体费用节省很多，费用更加透明易懂。</view>
					<image @click="seeImg" class="chargeList" src="@/static/chargeList.png" mode="widthFix"></image>
				</view>
			</view>
		</uni-popup>
		
		<uni-popup ref="yahooDialog" mask-background-color="rgba(0,0,0,0.8)">
			<view class="popup-content charge">
				<image src="@/static/yahooBg.png"></image>
				<view class="content">
					<view class="substite center">关于雅虎出价竞拍</view>
					<view class="item item2">
						<view class="tips">
							为了防止出价以后不付尾款，雅虎竞拍出价需要缴纳2000元人民币的押金，支付押金以后可以自由出价。
							<br/>
							日代拍跟雅虎官网有时间延迟，买家出价尽量提前，避免出现漏拍情况。
							<br/>
							如果在出价过程中遇到什么问题可以联系人工客服。
						</view>
					</view>
				</view>
			</view>
		</uni-popup>

	</view>
</template>

<script>
	import clasMenu from "@/utils/yahooClass.js"
	import Header from "@/components/header.vue"
	export default {
		components: {Header},
		data() {
			return {
				banners: [],
				noticeList: [],
				menuList: [
					{id: '23000', label: '服饰包包', icon: '/static/baobao.png'},
					{id: '23632', label: '家电数码', icon: '/static/xiyiji.png'},
					{id: '22152', label: '音乐唱片', icon: '/static/vynil.png'},
					{id: '25464', label: '玩具游戏', icon: '/static/youxiyouxiji.png'},
					{id: '23140', label: '首饰手表', icon: '/static/shoubiao.png'},
					{id: '20000', label: '收藏艺术', icon: '/static/moxing.png'},
					{id: 'jiu', label: '酒类厨刀', icon: '/static/jiubeihongjiu.png'},
					{id: '0', label: '全部分类', icon: '/static/caidan.png'},
				],
				list: []
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
			await this.selectcates(ids[result])
			
			// await this.getNoticeList()
		},
		methods: {
			
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
			onMenuClick(item){
				wx.setStorage({
					key: 'yahooId',
					data: item.id
				})
				wx.switchTab({
					url: '/pages/yahoo/index'
				})
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
	.notice{
		margin-top: 30rpx;
		width: 750rpx;
		height: 100rpx;
		swiper{
			width: 750rpx;
			height: 100rpx;
		}
	}
	.swiper{
		width: 750rpx;
		height: 350rpx;
		swiper{
			width: 750rpx;
			height: 350rpx;
			.slide-image{
				width: 750rpx;
				height: 350rpx;
				display: block;
			}
		}
	}
	
	.swiperfoonter{
		padding: 0 30rpx;
		.item{
			font-size: 28rpx;
			image{
				width: 30rpx;
				height: 30rpx;
			}
			.text{
				margin-left: 10rpx;
			}
		}
	}
	
	.menu{
		margin: 30rpx;
		width: 690rpx;
		flex-shrink: 0;
		flex-wrap: wrap;
		justify-content: space-between;
		.item{
			font-size: 26rpx;
			width: 150rpx;
			margin-bottom: 20rpx;
			image{
				width: 60rpx;
				height: 60rpx;
			}
			text{
				margin-top: 10rpx;
				color: #5a5a5a;
			}
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
</style>
