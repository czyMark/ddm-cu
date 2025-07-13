<template>
	<view class="page">
		<view class="loading column" v-if="loading">
			<image
				class="logo_avatar"
				:src="companyLogo"
			>
			
			<view class="company">{{companyName}}</view>
			<view class="desc">成长每时每刻</view>
			
			
			<view class="hxl-loading-animation">
				<view class="hxl-rect hxl-rect1"></view>
				<view class="hxl-rect hxl-rect2"></view>
				<view class="hxl-rect hxl-rect3"></view>
				<view class="hxl-rect hxl-rect4"></view>
				<view class="hxl-rect hxl-rect5"></view>
			</view>
		</view>
		<view :class="['video', loading ? 'video_loading' : '']">
			<video
				:src="loading ? '' : detail.img"
				:poster="loading ? '' : poster"
			>
				
			</video>
		</view>
		<view class="main">
			<view class="title">{{detail.title}}</view>
			<view class="subtitle">加强营养美味享</view>
			<view class="num">0人次</view>
		</view>
		
		<view class="tabs between">
			<view :class="['item center', currentTab === item.id ? 'active' : '']"
				v-for="(item, index) in tabs"
				:key="index"
				@click="currentTab = item.id"
			>
				{{item.label}}
			</view>
		</view>
		
		<template v-if="currentTab === 1">
			<uni-section title="讲师介绍" type="line" titleFontSize="16px"></uni-section>
			<view class="info between">
				<image class="avatar" :src="detail.teacherlogo"></image>
				<view class="instruduce">
					<view class="name">{{detail.teacher || ''}}</view>
					<view class="des">
						{{detail.teacherdetails || ''}}
					</view>
				</view>
			</view>
			
			<uni-section title="详情介绍" type="line" titleFontSize="16px"></uni-section>
			<view class="instruduce_des">{{detail.details}}</view>
		</template>
		
		<template v-if="currentTab === 2">
			<view class="empty column">
				<image src="@/static/empty.png"></image>
				<view>没有找到数据~</view>
			</view>
		</template>
		
		
		<template v-if="currentTab === 3">
			<view style="position: relative;">
				<uni-section title="精选心得 (0)" type="line" titleFontSize="16px"></uni-section>
				<view class="write center" @click="showLogin">
					<image src="@/static/write.png"></image>
					<view>记录我的心得</view>
				</view>
			</view>
			<view class="empty column">
				<image src="@/static/empty.png"></image>
				<view>暂无评论~</view>
			</view>
		</template>
		
		<view class="more" @click="showMore">
			<image
				class="icon"
				src="@/static/more.png"
			>
		</view>
		
		
		
		<uni-popup ref="popup" background-color="none">
			<view class="popup-content">
				<view class="list between">
					<view class="item column"
						v-for="(item, index) in menu"
						:key="index"
					>
						<image
							class="icon"
							:src="item.img"
						>
						<view class="label">{{item.label}}</view>
					</view>
				</view>
				
				<view class="close" @click="close">关闭</view>
			</view>
		</uni-popup>
		
		
		<uni-popup ref="popup2" background-color="none">
			<view class="popup-content2 popup-content column">
				
				<image
					class="icon"
					src="@/static/71.png"
				>
				<view class="nav">
					<view class="tips">申请获取以下权限</view>
					<view class="desc">获得你的公开信息(昵称、头像等)</view>
					<view class="close center" @click="closeLogin">授权登录</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: true,
				currentTab: 1,
				tabs: [
					{id: 1, label: '课程介绍'},
					{id: 2, label: '课件下载'},
					{id: 3, label: '学员心得'},
				],
				detail: {},
				poster: require('@/static/cover.png'),
				menu: [
					{
						img: 'https://kc.erevip.top/addons/hxl_yunke/html5/resources/mobile/images/menus/home.png',
						label: '首页'
					},
					{
						img: 'https://kc.erevip.top/addons/hxl_yunke/html5/resources/mobile/images/menus/shoucang.png',
						label: '收藏'
					},
					{
						img: 'https://kc.erevip.top/addons/hxl_yunke/html5/resources/mobile/images/menus/tousu.png',
						label: '反馈'
					},
					{
						img: 'https://kc.erevip.top/addons/hxl_yunke/html5/resources/mobile/images/menus/zixun.png',
						label: '咨询'
					},
				],
				
				companyName: '',
				companyLogo: '',
			}
		},
		onLoad(query){
			this.id = query.id*1
			try{
				const detail = JSON.parse(localStorage.getItem('detail'))
				this.companyName = detail.company || '桂林博图文化传媒有限责任公司'
				this.companyLogo = detail.companylogo || require('@/static/logo_avatar.png')
			}catch{}
			console.log('11111111', query)
			this.search()
			setTimeout(()=>{
				this.loading = false
			}, 1500)
		},
		methods: {
			async search(){
				console.log('2222222222')
				const params = {
					title: ''
				}
				const res = await this.$api.select(params)
				const list = res.data.data
				
				list?.forEach(item=>{
					if(this.id === item.id){
						this.detail = item
					}
				})
				
				
			},
			showMore(){
				this.$refs.popup.open('bottom')
			},
			close(){
				this.$refs.popup.close()
			},
			showLogin(){
				this.$refs.popup2.open('bottom')
			},
			closeLogin(){
				this.$refs.popup2.close()
			},
		}
	}
</script>

<style scoped lang="scss">
.page {
	width: 750rpx;
	height: 100vh;
	box-sizing: border-box;
	overflow: auto;
	font-size: 14px;
}
.video{
	width: 750rpx;
	height: 420rpx;
	video{
		background-color: #fff;
		width: 100%;
		height: 100%;
	}
}
.main{
	padding: 30rpx 30rpx 20rpx;
	background-color: #fff;
	width: 690rpx;
	.title{
		font-weight: 600;
		font-size: 18px;
		color: #333;
	}
	.subtitle{
		margin-top: 12rpx;
		font-weight: 400;
		font-size: 24rpx;
		color: #999;
	}
	.num{
		font-size: 24rpx;
		color: #999;
		position: relative;
		margin-top: 30rpx;
	}
}
.tabs{
	background-color: #fff;
	margin-top: 30rpx;
	padding-bottom: 30rpx;
	width: 750rpx;
	.item{
		width: 33.3333%;
		height: 90rpx;
		font-size: 28rpx;
		font-weight: 400;
		color: #999;
		transition: all .2s ease-in-out;
	}
	.active{
		font-size: 32rpx;
		font-weight: 600;
		color: #333;
		position: relative;
	}
	.active::before{
		position: absolute;
		left: 0;
		right: 0;
		bottom: -4rpx;
		margin: auto;
		width: 40rpx;
		height: 8rpx;
		background-color: #ff5a31;
		border-radius: 20rpx;
		content: "";
	}
}

.info{
	padding: 40rpx 30rpx;
	background-color: #fff;
	align-items: flex-start;
	.avatar{
		width: 40px;
		height: 32px;
		border-radius: 13px;
	}
	.instruduce{
		margin-left: 10rpx;
		width: 580rpx;
		.name{
			margin-bottom: 5px;
			overflow: hidden;
			white-space: nowrap;
			font-weight: 700;
			font-size: 15px;
			color: #333;
		}
		.des{
			width: 100%;
			    font-size: 12px;
			    font-weight: 400;
			    color: #999;
			    line-height: 16px;
			    white-space: pre-wrap;
			    word-wrap: break-word;
			    text-align: left;
			    word-break: break-all;
			    display: inline-block;
		}
	}
}

.empty{
	width: 750rpx;
	height: 750rpx;
	background-color: #fff;
	image{
		width: 300rpx;
		height: 300rpx;
	}
	view{
		margin-top: 100rpx;
		margin-bottom: 30px;
		text-align: center;
		font-size: 14px;
		color: #9a9a9a;
	}
}
.write{
	position: absolute;
	top: 20rpx;
	right: 30rpx;
	height: 30px;
	box-sizing: border-box;
	padding: 8px 10px;
	background: rgba(255,90,49,.1);
	border-radius: 15px;
	margin-left: auto;
	letter-spacing: 0;
	line-height: 14px;
	font-size: 14px;
	font-weight: 400;
	color: #ff5a31;
	image{
		width: 28rpx;
		height: 28rpx;
	}
}

.loading{
	width: 100vw;
	height: 100vh;
	position: fixed;
	z-index: 2;
	top: 0;
	left: 0;
	background-color: rgba(255, 255, 255, 0.8);
	.hxl-loading-animation{
		margin: 16px auto;
		width: 56px;
		height: 40px;
		text-align: center;
		font-size: 10px;
		.hxl-rect{
			background-color: #ef313b;
			height: 100%;
			width: 6px;
			display: inline-block;
			-webkit-animation: ani 1.2s infinite ease-in-out;
			animation: ani 1.2s infinite ease-in-out;
		}
		.hxl-rect1{
			-webkit-animation-delay: -1.2s;
			animation-delay: -1.2s;
		}
		.hxl-rect2{
			-webkit-animation-delay: -1.1s;
			animation-delay: -1.1s;
			margin-left: 2px;
		}
		.hxl-rect3{
			-webkit-animation-delay: -1s;
			animation-delay: -1s;
			margin-left: 2px;
		}
		.hxl-rect4{
			-webkit-animation-delay: -0.9s;
			animation-delay: -0.9s;
			margin-left: 2px;
		}
		.hxl-rect5{
			-webkit-animation-delay: -0.8s;
			animation-delay: -0.8s;
			margin-left: 2px;
		}
	}
	
	.logo_avatar{
		width: 160rpx;
		height: 160rpx;
	}
	.company{
		text-align: center;
		line-height: 60px;
		font-size: 20px;
		color: #888;
	}
	.desc{
		text-align: center;
		margin-bottom: 15px;
		line-height: 20px;
		font-size: 14px;
		color: #888;
	}
}

.more{
	width: 108rpx;
	height: 108rpx;
	display: flex;
	flex-flow: row nowrap;
	justify-content: center;
	align-items: center;
	background: #fff;
	border-radius: 50%;
	box-shadow: 0 0 4px 0 rgba(0,0,0,.2);
	position: fixed;
	z-index: 10;
	cursor: pointer;
	bottom: 300rpx;
	right: 40rpx;
	.icon{
		width: 52rpx;
		height: 68rpx;
	}
}

.popup-content{
	border-radius: 60rpx;
	background-color: #fff;
	width: 690rpx;
	margin: 0 auto 30rpx;
	padding-bottom: 30rpx;
	.list{
		padding: 50rpx 30rpx 30rpx;
		.item{
			width: 120rpx;
			.icon{
				width: 120rpx;
				height: 120rpx;
			}
			.label{
				margin-top: 6px;
				font-size: 14px;
				font-weight: 400;
				color: #666;
			}
		}
	}
	.close{
		text-align: center;
		font-size: 18px;
		color: #666;
	}
}
.popup-content2{
	padding: 50rpx 30rpx;
	box-sizing: border-box;
	.icon{
		width: 120rpx;
		height: 100rpx;
	}
	.nav{
		width: 670rpx;
		box-sizing: border-box;
		padding-left: 50rpx;
		padding-top: 30rpx;
		margin-top: 50rpx;
		border-top: 1px solid #eaeaea;
		.tips{
			font-size: 16px;
			color: #000;
		}
		.desc{
			margin-top: 20rpx;
		}
		.close{
			margin-top: 50rpx;
			width: 580rpx;
			height: 80rpx;
			font-size: 15px;
			color: #fff;
			background-color: green;
			border-radius: 20rpx;
		}
	}
}


.instruduce_des{
	padding: 30rpx 30rpx 80rpx;
	background-color: #fff;
}

::v-deep .uni-video-cover-play-button{
	background-image: url('/static/play.png');
}

::v-deep .uni-video-cover-play-button{
	width: 100px;
	height: 100px;
}
::v-deep .uni-video-cover-duration{
	display: none;
}
::v-deep .uni-section-header__decoration{
	background-color: #ff5a31 !important;
	height: 16px !important;
}
::v-deep .uni-section__content-title{
	font-weight: 600;
}

::v-deep .video_loading .uni-video-cover{
	background-color: #dad9d9;
}

@keyframes ani {
	40% {
	    -webkit-transform: scaleY(.4);
	    transform: scaleY(.4);
	}
	20% {
	    -webkit-transform: scaleY(1);
	    transform: scaleY(1);
	}
}
</style>
