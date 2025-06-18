<template>
	<view class="page">
		<Title :type="1" :title="type + '地址'"/>
		<view class="form">
			<view class="item between">
				<view class="label">收件人</view>
				<input type="text" class="value" :value="reciveName"
					@input="(e)=>{
						onInputChange(e.detail.value, 'reciveName')
					}"/>
			</view>
			<view class="item between">
				<view class="label">手机号码</view>
				<input type="number" class="value" :value="phone"
					@input="(e)=>{
						onInputChange(e.detail.value, 'phone')
					}"/>
			</view>
			<view class="item between">
				<view class="label">所在地区</view>
				<view class="value">
					<picker mode="region" @change="bindRegionChange" :value="region" :custom-item="customItem">
						<view class="picker">
						  {{!region[0] && !region[1] && !region[2] ? '请选择' : region.join(' ')}}
						</view>
					</picker>
					
				</view>
			</view>
			<view class="item between">
				<view class="label">详细地址</view>
				<input type="text" class="value" :value="address"
					@input="(e)=>{
						onInputChange(e.detail.value, 'address')
					}"/>
			</view>
			<view class="item item2">
				<view class="titleHeader">身份证照片</view>
			</view>
			
			
			<view class="photos column">
				<view class="container column" @click="onUpload(1)">
					<uni-icons v-if="!idImgFoward" type="plusempty" size="30"></uni-icons>
					<view v-if="!idImgFoward">正面(人面)</view>
					<image v-if="idImgFoward" :src="idImgFoward" mode=""></image>
				</view>
				<view class="container column" @click="onUpload(2)">
					<uni-icons v-if="!idImgBack" type="plusempty" size="30"></uni-icons>
					<view v-if="!idImgBack">反面(国徽)</view>
					<image v-if="idImgBack" :src="idImgBack" mode=""></image>
				</view>
			</view>
		</view>
		
		<view class="addbtn center">
			<image
				class="icon"
				src="@/static/icon/61.png"
				@click="submit"
			>
		</view>
		
		<uni-popup ref="popupref">
			<view class="popup-content">
				<view class="header">{{type}}地址</view>
				
				<view class="btn" @click="submit">保存</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	import Title from "@/components/title.vue"
	import baseurl from '@/utils/url.js'
	export default {
		components: {Title},
		data() {
			return {
				list: [],
				currentAddress: {},
				customItem: '',
				type: '',
				region: ['', '', ''],
				reciveName: '',
				phone: '',
				address: '',
				userInfo: '',
				addressId: '',
				idImgFoward: '',
				idImgBack: '',
			}
		},
		onLoad(query) {
			this.type = query.type === '1' ? '新增' : '编辑'
			this.id = query.id*1
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.getAddress()
		},
		methods: {
			async onDefaultChange(item){
				if(item.defaultType === 1) return false
				wx.showLoading({title: '操作中'})
				const params = {
					userid: this.userInfo.userid,
					id: item.id
				}
				const res = await this.$api.defaultType(params)
				this.getAddress()
				wx.hideLoading()
			},
			async getAddress(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid
				}
				const res = await this.$api.selectByAduserid(params)
				const { data } = res.data
				data.forEach(item=>{
					item.region = item.address.split('----')[0]
					item.address = item.address.split('----')[1]
				})
				this.list = data
				
				this.list?.forEach(item=>{
					if(item.id === this.id){
						this.currentAddress = item
						
						this.addressId = item.id
						this.reciveName = item.username
						this.phone = item.phone
						this.address = item.address
						this.region = item.region.split(',')
						this.idImgFoward = item.idZ
						this.idImgBack = item.idF
					}
				})
				
				wx.hideLoading()
			},
			async submit(){
				console.log(this.region)
				if(!this.reciveName){
					wx.showToast({title: '请输入收件人', icon: 'none'})
				}else if(!this.phone){
					wx.showToast({title: '请输入手机号', icon: 'none'})
				}else if(this.region.length === 0 || this.region[0] === '' || this.region[1] === '' || this.region[2] === ''){
					wx.showToast({title: '请选择地区', icon: 'none'})
				}else if(!this.address){
					wx.showToast({title: '请输入详细地址', icon: 'none'})
				}else if(!this.idImgFoward){
					wx.showToast({title: '请上传身份证正面', icon: 'none'})
				}else if(!this.idImgBack){
					wx.showToast({title: '请上传身份证反面', icon: 'none'})
				}else{
					const address = this.region.join(',') + '----' + this.address
					console.log('address', address)
					const params = {
						userid: this.userInfo.userid,
						address,
						username: this.reciveName,
						phone: this.phone,
						idZ: this.idImgFoward,
						idF: this.idImgBack,
					}
					let res = null
					if(this.type === "新增"){
						res = await this.$api.addAddress(params)
					}else{
						params.id = this.addressId
						res = await this.$api.updataByAdid(params)
					}
					wx.showToast({
						title: '操作成功',
						icon: 'none'
					})
					
					setTimeout(()=>{
						uni.navigateBack(-1)
					}, 1000)
				}
			},
			bindRegionChange(e){
				this.region = e.detail.value
			},
			async deleteItem(item){
				wx.showLoading({title: '操作中'})
				const params = {
					id: item.id
				}
				const res = await this.$api.deleteByAdid(params)
				this.getAddress()
				wx.hideLoading()
			},
			toggle(type, item) {
				this.type = type
				if(item){
					this.addressId = item.id
					this.reciveName = item.username
					this.phone = item.phone
					this.address = item.address
					this.region = item.region.split(',')
					this.idImgFoward = item.idZ
					this.idImgBack = item.idF
				}else{
					this.reciveName = ''
					this.phone = ''
					this.address = ''
					this.region = []
					this.idImgFoward = ''
					this.idImgBack = ''
				}
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popupref.open('center')
			},
			onInputChange(val, key) {
				this[key] = val
			},
			onUpload(val){
				const _this = this
				wx.chooseImage({
				  success (res) {
				    const tempFilePaths = res.tempFilePaths
					console.log("tempFilePaths", tempFilePaths)
				    wx.uploadFile({
				      // url: `https://ridaipai.top/upload`, //仅为示例，非真实的接口地址
				      url: `${baseurl}/upload`, //仅为示例，非真实的接口地址
				      filePath: tempFilePaths[0],
				      name: 'image',
				      success (res){
						  console.log('res', res)
							const data = JSON.parse(res.data)
							console.log('data', data)
						  if(val === 1){
							  _this.idImgFoward = data.data
						  }else{
							  _this.idImgBack = data.data
						  }
				      }
				    })
				  }
				})
			},
		}
	}
</script>

<style lang="scss">
	.page{
		background: #fff;
		width: 100vw;
		padding-bottom: 300rpx;
	}
	.list{
		margin-top: 20rpx;
		margin-bottom: 150rpx;
		.item{
			box-shadow: 0rpx 2rpx 5rpx 3rpx rgba(0,0,0,0.07);
			border-radius: 20rpx;
			padding: 20rpx;
			background: #FFF;
			margin-bottom: 30rpx;
			height: 220rpx;
			width: 710rpx;
			.container{
				width: 710rpx;
			}
			.itemHeader{
				color: #848282;
				padding: 0 20rpx;
				box-sizing: border-box;
				width: 100%;
				font-size: 26rpx;
				.copy{
					padding: 4rpx 8rpx;
					border: 1px solid #e5e5e5;
				}
				radio .wx-radio-input{
					width: 30rpx;
					height: 30rpx;
				}
				.isDefalt{
					color: #f9bf3b;
				}
			}
			.itemNav{
				width: 100%;
				margin-top: 10rpx;
			}
			.info{
				width: 100%;
				margin-left: 30rpx;
				font-size: 28rpx;
				justify-content: space-around;
				align-items: flex-start;
				.title{
					margin-top: 20rpx;
					font-size: 28rpx;
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
				}
				.price text{
					font-size: 36rpx;
					color: red;
					margin: 0 10rpx;
				}
				.status{
					color: red;
				}
			}
		}
	}
	.addbtn{
		background: #fff;
		position: fixed;
		bottom: 0;
		left: 0;
		width: 750rpx;
		height: 66rpx;
		line-height: 66rpx;
		text-align: center;
		color: #FFF;
		border-radius: 20rpx;
		padding: 50rpx 0;
		border-top: 10rpx solid #eaeaea;
		.icon{
			width: 690rpx;
			height: 66rpx;
		}
	}
	.form{
		padding: 30rpx;
		.item{
			height: 100rpx;
			line-height: 100rpx;
			text-align: center;
			width: 100%;
			.label{
				width: 150rpx;
				padding-right: 10rpx;
				text-align: right;
			}
			.value{
				text-align: left;
				width: 500rpx;
				height: 100rpx;
				border-bottom: 1px solid #e5e5e5;
			}
		}
		.item{
			text-align: left;
		}
	}
	
	.container{
		width: 690rpx;
		height: 370rpx;
		margin-bottom: 30rpx;
		border: 1px dashed #b3b3b3;
		view{
			height: 40rpx;
			line-height: 40rpx;
		}
		image{
			width: 500rpx;
			height: 370rpx;
		}
	}
	.nodata{
		width: 100vw;
		text-align: center;
		font-size: 28rpx;
		color: #9e9a9a;
		margin: 180rpx 0 0;
		.icon{
			width: 266rpx;
			height: 296rpx;
			margin-bottom: 20rpx;
		}
	}
</style>
