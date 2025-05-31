<template>
	<view class="collect">
		<form class="form" @submit='submit'>
			<!-- <form></form> -->
			<view class="item center">
				<view class="label">输入昵称：</view>
				<input class="value nickName" type="nickname" name="nickName" :value="nickName" maxlength="14"
					ref="input"
					@nicknamereview="nicknamereview"
					@input="(e)=>{
						onInputChange(e.detail.value, 'nickName')
					}"/>
			</view>
			<view class="item center" style="height: 120rpx">
				<view class="label">选择头像：</view>
				<button open-type="chooseAvatar" class="value between" @chooseavatar="onChooseAvatar"
					style="height: 120rpx">
					<image class="avatar" :src="avatar"></image>
				</button>
				
			</view>
			<view class="item center">
				<view class="label">联系电话：</view>
				<input type="number" class="value" :value="phone"
					@input="(e)=>{
						onInputChange(e.detail.value, 'phone')
					}"/>
			</view>
			<view class="item center">
				
				<view class="label">真实姓名：</view>
				<input type="text" class="value" :value="name"
					@input="(e)=>{
						onInputChange(e.detail.value, 'name')
					}"/>
			</view>
			<view class="item center">
				<view class="label">第二联系电话：</view>
				<input type="number" class="value" :value="secondPhone"
					@input="(e)=>{
						onInputChange(e.detail.value, 'secondPhone')
					}"/>
			</view>
			<button class="btn" formType="submit">保存</button>
		</form>
	</view>
</template>

<script>
	import base_url from '@/utils/url.js'
	export default {
		data() {
			return {
				name: '',
				phone: '',
				secondPhone: '',
				userInfo: {},
				avatar: 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132',
				nickName: '',
			}
		},
		onLoad() {
			this.userInfo = wx.getStorageSync('userInfo') || {}
			this.getUserInfo()
		},
		loginForm(e) {
		    console.log('e1', e);
		    console.log(name);
		    console.log(company);
		},
		methods: {
			loginForm(e) {
				console.log('e2', e.detail.avatarUrl);
			},
			nicknamereview(e, e1){
				// setTimeout(()=>{
				// 	console.log('e', e)
				// 	console.log('e1', e1)
				// 	console.log('nickName', this.nickName)
				// 	var query = wx.createSelectorQuery();
				// 	query.select('.nickName').boundingClientRect();
				// 	query.exec(function (res) {
				// 	  console.log(res);  
				// 	})
				// }, 1000);
				// this.avatar = e.detail.avatarUrl
			},
			onChooseAvatar(e){
				const _this = this
				console.log('e', e)
				// this.avatar = e.detail.avatarUrl
				wx.uploadFile({
				    url: base_url + 'upload', //仅为示例，非真实的接口地址
				    filePath: e.detail.avatarUrl,
				    name: 'image',
					header: {
						'sessionid': wx.getStorageSync('userInfo')?.session?.sessionId || '',
					},
				    success (res){
						const data = JSON.parse(res.data)
						console.log('data', data)
						console.log('data2222', data.data)
						_this.avatar = data.data
				    },
					fail(err){
						console.log('err', err)
					}
				})
			},
			async getUserInfo(){
				wx.showLoading({title: '加载中'})
				const params = {
					userid: this.userInfo.userid
				}
				const res = await this.$api.selectzl(params)
				const { data } = res.data
				console.log(data)
				this.name = data.realname
				this.phone = data.phone
				this.secondPhone = data.twophone
				this.nickName = data.nickName
				this.avatar = data.avatarUrl
				wx.hideLoading()
				if(!this.phone || !this.name || !this.nickName || !this.avatar){
					wx.showToast({
						title: '请完善用户资料！',
						icon: "none"
					})
				}
			},
			async submit(e){
				wx.showLoading({title: '保存中'})
				const params = {
					userid: this.userInfo.userid,
					phone: this.phone,
					realname: this.name,
					twophone: this.secondPhone,
					nickName: e.detail.value.nickName,
					avatarUrl: this.avatar,
				}
				await this.$api.updatazl(params)
				wx.hideLoading()
				wx.showToast({title: '操作成功', icon: 'none'})
				setTimeout(()=>{
					wx.navigateBack(-1)
				}, 1000)
			},
			bindRegionChange(e){
				console.log('picker发送选择改变，携带值为', e.detail.value)
				this.region = e.detail.value
			},
			onDefaultChange(item){
				if(item.isDefalt) return false
				this.list.forEach(it=>{
					it.isDefalt = false
				})
				item.isDefalt = true
			},
			deleteItem(item){
				console.log(item)
			},
			toggle(type, item) {
				this.type = type
				if(item){
					this.reciveName = item.name
					this.phone = item.phone
					this.address = item.address
					this.region = item.region.split(',')
				}else{
					this.reciveName = ''
					this.phone = ''
					this.address = ''
					this.region = []
				}
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popupref.open('center')
			},
			onInputChange(val, key) {
				this[key] = val
				console.log(this.nickName)
			},
		}
	}
</script>

<style lang="scss">
	page{
		background: #f7f7f7;
	}
	.form{
		padding: 30rpx;
		.item{
			height: 80rpx;
			line-height: 120rpx;
			text-align: center;
			width: 100%;
			.label{
				width: 200rpx;
				font-size: 28rpx;
			}
			.value{
				text-align: left;
				width: 460rpx;
				height: 80rpx;
				border-bottom: 1px solid #e5e5e5;
			}
		}
	}
	button::after {
		border: none !important;
		padding: 0 !important;
		margin: 0 !important;
	}
	button {
		background-color: transparent !important;
		padding: 0 !important;
		line-height: inherit !important;
		margin: 0 !important;
		// width: auto !important;
		font-weight: 500 !important;
		border-radius: none !important;
		
		image{
			width: 92rpx;
			height: 92rpx;
			border-radius: 50%;
		}
	}
	.btn{
		margin: 30rpx auto !important;
		width: 650rpx;
		height: 80rpx;
		line-height: 80rpx !important;
		text-align: center;
		color: #FFF;
		border-radius: 20rpx;
		background: linear-gradient(270deg, #f9bf3b 0%, #eba82e 100%);
	}
</style>
