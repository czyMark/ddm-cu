<template>
	<view class="goodsDetail">
		<view v-if="!loading">
			
		<view class="swiper">
			<swiper
			  indicator-dots
			  circular>
			  <block v-for="(item, index) in imgList" :key="index">
			    <swiper-item>
					<view class="slide-image center">
						<image @load="bindload" :src="item" mode="heightFix" @click="prewImage(item)"/>
					</view>
			    </swiper-item>
			  </block>
			</swiper>
		</view>
		
		<view class="timer between">
			<view class="times">出价{{detail.BiddingCOunt || ''}}次</view>
			<view class="timerInfo">
				<view class="reflash">{{flashFlag}}秒后刷新</view>
				<view class="count">剩余 {{endtime}} 结束</view>
			</view>
		</view>
		
		<view class="title flex">
			{{detail.orther.title}}
			<image @click="copyTitle(detail.orther.title)" src="@/static/copy.png" mode=""></image>
		</view>
		<view class="price">
			<view class="currentPrice between" style="margin-bottom: 20rpx;">
				<view>
					<text style="color: #adabab">商品编码</text>
					<text style="color: #adabab">{{query.code}}</text>
				</view>
				<view @click.stop="copyCode(query.code)" style="font-size: 24rpx; border: 1px solid #c5c5c5; padding: 1px 5px; color: #423f3f;">复制</view>
			</view>
			<view class="currentPrice">
				<text>当前价格</text>
				<text>{{detail.orther.jpprice}}</text>
				<text>({{detail.orther.price_rmb}}元)</text>
			</view>
			<view class="currentPrice" v-if="isHaveRate">
				<text>税后价格</text>
				<text>{{parseInt(detail.orther.jpprice * 110) / 100}}</text>
				<text>({{parseInt(detail.orther.price_rmb * 110) / 100}}元)</text>
			</view>
			<view class="currentPrice" v-if="detail.orther.jijuePrice">
				<text style="opacity: 0.5;">即决价格</text>
				<text>{{detail.orther.jijuePrice}}</text>
				<text>({{parseInt(detail.orther.jijuePrice * rate * 100) / 100}}元)</text>
			</view>
			<view class="currentPrice" v-if="detail.orther.jijuePrice && isHaveRate">
				<text style="opacity: 0.5;">税后即决价格</text>
				<text>{{parseInt(detail.orther.jijuePrice * 110) / 100}}</text>
				<text>({{parseInt(detail.orther.jijuePrice * rate * 110) / 100}}元)</text>
			</view>
		</view>
		
		<view class="priceLog between">
			<view class="highest">
				最高出价者 <text>{{biddings.length > 0 ? biddings[0].name : '无'}}</text>
			</view>
			<view class="log center" @click="toPath(`/pages/yahoo/pricelog`, true)">
				出价记录 <text class="text">{{biddings.length || 0}}次</text>
				<uni-icons style="position: relative; top: 4rpx" type="right" size="15"></uni-icons>
			</view>
		</view>
		
		
		<view class="priceLog priceLog2 between">
			<view class="highest highest2">
				<view>
					{{salerInfo.seller}} <text>个人卖家</text>
				</view>
				<view class="count">
					好评 {{salerInfo.goodpj}} <text>差评 {{salerInfo.badpj}}</text>
				</view>
			</view>
			<view class="log center" @click="toSaler">
				卖家主页
				<uni-icons style="position: relative; top: 4rpx" type="right" size="15"></uni-icons>
			</view>
		</view>
		
		<view class="tips">
			<view class="tipTitle">日代拍提醒</view>
			<view class="item" v-for="(item, index) in tipsList" :key="index">
				{{item}}
			</view>
		</view>
		
		<view class="tabs between">
			<view :class="['item', currenTab === item.id ? 'active' : '']"
				v-for="(item, index) in tabs" :key="index"
				@click="chooseTab(item.id)">
				{{item.label}}
			</view>
		</view>
		
		<view class="richText" >
			<rich-text :nodes="detailData" v-if="currenTab === 1"></rich-text>
			<rich-text :nodes="priceRuleData" v-if="currenTab === 2"></rich-text>
			<rich-text :nodes="riskData" v-if="currenTab === 3"></rich-text>
		</view>
		
		<view class="options flex">
			<view class="item column" @click="verifyLogin('toPath', '/pages/mine/contact')">
				<image src="@/static/service.png"></image>
				<view>客服</view>
			</view>
			<view class="item column" @click="verifyLogin('toCollect')">
				<image src="@/static/star.png" v-if="!isCollect"></image>
				<image src="@/static/star-fill.png" v-if="isCollect"></image>
				<view v-if="!isCollect">收藏</view>
				<view v-if="isCollect">已收藏</view>
			</view>
			<view class="item column">
				<image src="@/static/recharge.png" @click="verifyLogin('toPath', '/pages/mine/account')"></image>
				<view>充值</view>
			</view>
			<view class="btns center">
				<view class="btn" @click="verifyLogin('showPopupPrice', 2)">预约出价</view>
				<view class="btn" @click="verifyLogin('showPopupPrice', 0)">立即出价</view>
			</view>
		</view>
		
		<!-- 价格计算弹窗 -->
		<uni-popup ref="popupCaculater" background-color="#fff">
			<view class="popup-content">
				<view class="title">费用计算</view>
				<view class="price flex">
					<view class="label">价格(含税):</view>
					<input type="number" v-model="wantedPrice"/>
					<text>円</text>
				</view>
				<view class="weight flex">
					<view class="label">包装重量预计:</view>
					<view class="opt flex">
						<button @click="reduce" :disabled="wantedWeight<2">-</button>
						<input type="number" v-model="wantedWeight" @input="onInput"/>
						<button @click="add">+</button>
					</view>
					<text>KG</text>
				</view>
				<view class="btns flex">
					<view class="btn">试算</view>
					<view class="btn" @click="closeCaculater">关闭</view>
				</view>
				
				<view class="info">
					<view class="item flex">
						<view class="label">代购服务费</view>
						<view class="value">5000円<text>(约 255元)</text></view>
					</view>
					<view class="item flex">
						<view class="label">银行转账费</view>
						<view class="value">250円<text>(约 13元)</text></view>
					</view>
					<view class="item flex">
						<view class="label">日本邮费(预估)</view>
						<view class="value">700円<text>(约 36元)</text></view>
					</view>
					<view class="item flex">
						<view class="label">日本打包费(预估)</view>
						<view class="value">600円<text>(约 31元)</text></view>
					</view>
					<view class="item flex">
						<view class="label">国际运费</view>
						<view class="value">
							<view class="value">EMS: 6400日<text>(约 327元)</text></view>
							<view class="value">邮局空运: 4850<text>(约 248元)</text></view>
							<view class="value">邮局船运: 3400<text>(约 174元)</text></view>
							<view class="value">日本自提:0</view>
							<view class="value">第三方转运:不明</view>
						</view>
					</view>
					<view class="item flex">
						<view class="label">进口关税(预估)</view>
						<view class="value">20401日<text>(约 1041元)</text></view>
					</view>
				</view>
				
				<view class="tips">
					<view>合计费用: 价格(含税)+代购服务费+银行转帐费+日本国内邮费+日本打包费+国际运费+进口关税</view>
					<view><text>含税</text>是指包含日本国内消费税。</view>
					<view><text>进口关税</text>以实际发生为主，平台预估只做参考。</view>
				</view>
			</view>
		</uni-popup>
		
		<!-- 出价弹窗 -->
		<uni-popup ref="popupPrice" background-color="#fff">
			<view class="popup-content-price">
				<!-- <view class="priceItem between">
					<view class="label">您的余额:</view>
					<view class="value"><text>{{jpbalance}}</text>円</view>
				</view> -->
				<view class="priceItem between">
					<view class="label">您的押金(可以出价{{parseInt(yjbalance / 2000 * 20)}}单，包括已成交未付款的订单):</view>
					<view class="value"><text>{{yjbalance}}</text>元</view>
				</view>
				<view class="priceItem between">
					<view class="label">现在价格:</view>
					<view class="value"><text>{{detail.orther.jpprice}}</text>円({{parseInt(detail.orther.jpprice * rate * 100) / 100}}元)</view>
				</view>
				<view class="priceItem between">
					<view class="label">您的出价:</view>
					<!-- <view class="label">您的{{priceType === 1 ? '预约出价' : '出价'}}:</view> -->
					<view class="value value2 flex">
						<input type="number" :disabled="priceType === 1" v-model="wantedPrice"/>
						円({{parseInt(wantedPrice * rate * 100) / 100}}元)
					</view>
				</view>
				
				<view class="tips">
					<view class="item" v-for="(item, index) in tipsList2" :key="index">
						{{item}}
					</view>
				</view>
				<view class="agree">
					<checkbox-group name=""  @change="onUserAgree">
						<label>
							<checkbox :value="userAgree"/><text>同意<text>《日代拍用户协议》</text>和上述条款</text>
						</label>
					</checkbox-group>
				</view>
				
				<view class="btns between">
					<view class="btn close" @click="closePopupPrice">关闭</view>
					<view class="btn order" @click="order(priceType)">确认{{priceType === 0 ? '立即出价' : '预约出价'}}</view>
				</view>
			</view>
		</uni-popup>
	</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: false,
				query: {},
				detail: {},
				endtime: '',	// 剩余结束时间
				endTimeTimer: null, // 剩余结束时间定时器
				flashFlag: 300,	// 倒计时刷新
				reflashTimer: null, // 倒计时刷新定时器
				biddings: [], // 出价记录
				wantedPrice: 999,	// 预估价格
				wantedWeight: 1,	// 预估重量
				userAgree: [], // 用户同意
				priceType: '', // 出价类型
				imgList: [],
				isCollect: false,
				tipsList: [
					'1、阅读拍卖的《出价规则》、《风险》、《费用》。',
					'2、了解清楚该拍品是否可以邮寄。',
					'3、拍卖的物品不支持退货。',
					'4、出价后无法取消出价，中标后不接受弃标(必须正常交易)。'
				],
				tipsList2: [
					'1、出价后无法取消出价，中标后不接受弃标。',
					// '2、退货需要卖家同意，不接受单方面退货。',
					'2、已阅读并知晓《出价规则》、《风险》、《费用》等重要说明。',
				],
				tabs: [
					{id: 1, label: '拍品展示'},
					{id: 2, label: '出价规则'},
					{id: 3, label: '风险'},
					// {id: 4, label: '费用'},
				],
				currenTab: 1,
				detailData: '',
				priceRuleData: '',
				riskData: '',
				
				userInfo: {},
				rate: '',
				
				balance: 0,
				jpbalance: 0,
				yjbalance: 0,
				
				isHaveRate: false,
				
				salerInfo: {}
			}
		},
		async onLoad(query) {
			this.query = query
			this.priceRuleData = wx.getStorageSync('setting')?.chujiarules || ''
			this.riskData = wx.getStorageSync('setting')?.riskwarning || ''
			
			this.rate = wx.getStorageSync('rate') || null
			this.userInfo = wx.getStorageSync('userInfo') || {}
			await this.getYahooGoodDetail(query)
			if(wx.getStorageSync('login') == 'true'){
				console.log('aaaaaaaa')
				this.queryCollectById()
				this.addViewLog()
			}
		},
		onShow(){
			if(wx.getStorageSync('login') == 'true'){
				this.selectUserBalance()
			}
		},
		onUnload(){
			clearInterval(this.endTimeTimer)
			clearInterval(this.reflashTimer)
		},
		methods: {
			addViewLog(){
				const params = {
					platform: 0,
					goodcode: this.query.code,
					imageUrl: this.imgList[0],
					title: this.detail.orther.title,
					counts: this.biddings?.length || 0,
					price: this.detail.orther.jpprice,
					userid: this.userInfo.userid,
				}
				this.$api.addViewLog(params)
			},
			toSaler(){
				if(this.query?.from && this.query.from === 'saler'){
					wx.navigateBack(-1)
				}else{
					const salerInfo = this.salerInfo
					this.toPath(`/pages/yahoo/salerGoodsList?seller=${salerInfo.seller}&goodpj=${salerInfo.goodpj}&badpj=${salerInfo.badpj}`, true)
				}
			},
			prewImage(item){
				wx.previewImage({
				  current: item,
				  urls: this.imgList
				})
			},
			async selectUserBalance(){
				const params = {
					userid: this.userInfo.userid
				}
				const res = await this.$api.selectUserBalance(params)
				const { data } = res.data
				this.jpbalance = data.balance
				this.yjbalance = data.yjbalance
				console.log('jpbalance', this.jpbalance)
			},
			async queryCollectById(){
				const params = {
					goodcode: this.query.code,
					userid: this.userInfo.userid,
				}
				const res = await this.$api.queryCollectById(params)
				const {data} = res.data
				this.isCollect = data.type === 1
			},
			async toCollect(){
				// this.isCollect = !this.isCollect
				wx.showLoading({title: '操作中'})
				if(this.isCollect){
					const params = {
						goodcode: this.query.code,
						userid: this.userInfo.userid,
					}
					const res = await this.$api.deleteCollect(params)
					wx.hideLoading()
					wx.showToast({title: '操作成功', icon: 'none'})
					this.isCollect = false
				}else{
					const params = {
						platform: 0,
						goodcode: this.query.code,
						imageUrl: this.imgList[0],
						title: this.detail.orther.title,
						counts: this.biddings?.length || 0,
						price: this.detail.orther.jpprice,
						userid: this.userInfo.userid,
					}
					const res = await this.$api.addCollect(params)
					wx.hideLoading()
					wx.showToast({title: '操作成功', icon: 'none'})
					this.isCollect = true
				}
				
			},
			async addCollect(){
				wx.showLoading({title: '收藏中'})
				const {seller, code, breakurl} = query
				const params = {
					platform: '0',
					goodcode: this.query.code,
					imageUrl: this.query.code,
				}
				const res = await this.$api.getYahooGoodDetail(params)
				const { data } = res.data
			},
			async getYahooGoodDetail(query){
				wx.showLoading({title: '加载中'})
				this.loading = true
				const {code} = query
				const params = {
					code
				}
				const res = await this.$api.getYahooGoodDetail(params)
				const { data } = res.data
				if(data.orther.jijuePrice === undefined) data.orther.jijuePrice = 0
				this.detail = data
				this.detail.orther.price_rmb = parseInt((this.detail.orther.jpprice * this.rate) * 100) / 100
				this.biddings = data.biddings || []
				this.detailData = data.orther.gooddetail.replace(/<img/ig, '<img style="width: 95vw"')
				this.imgList = data.imgList
				this.wantedPrice = data.orther.limitPrice
				this.salerInfo = data.Sellermessage
				this.isHaveRate = /1|2|3|4|5|6|7|8|9/ig.test(data.orther.ratePrice)
				
				
				// 处理结束时间
				const nowDate = Date.parse(this.getBeijingTime())
				const endDate = Date.parse(new Date((data.orther.endtime + ':00').replace(/-/g, '/')))
				const timeReduce = (endDate - nowDate) / 1000 - 3600
				
				console.log('nowDate', nowDate, this.getBeijingTime())
				console.log('endDate', endDate, new Date((data.orther.endtime + ':00').replace(/-/g, '/')))
				
				console.log('timeReduce', timeReduce)
				if(timeReduce > 0){
					// 开始倒计时商品过期时间
					this.startCount(timeReduce)
					// 开始倒计时刷新页面
					if(timeReduce > 600){
						this.flashFlag = 300
					}else{
						this.flashFlag = 60
					}
					this.countReflash()
				}else{
					this.endtime = "00:00:00"
					wx.showToast({
						title: '商品已过期!',
						icon: 'none'
					})
					setTimeout(()=>{
						wx.navigateBack(-1)
					}, 1000)
				}
				this.loading = false
				wx.hideLoading()
			},
			getBeijingTime(){
			    // 目标时区，东8区			    const targetTimezone = -8;			    // 当前时区与中时区时差，以min为维度			    const dif = new Date().getTimezoneOffset();			    // 本地时区时间 + 本地时区时差  = 中时区时间			    // 目标时区时间 + 目标时区时差 = 中时区时间			    // 目标时区时间 = 本地时区时间 + 本地时区时差 - 目标时区时差			    // 东8区时间			    const east9time = new Date().getTime() + dif * 60 * 1000 - (targetTimezone * 60 * 60 * 1000);
				return new Date(east9time)			},
			countReflash(){
				this.reflashTimer = setInterval(()=>{
					this.flashFlag--
					if(this.flashFlag < 1){
						clearInterval(this.reflashTimer)
						clearInterval(this.endTimeTimer)
						this.getYahooGoodDetail(this.query)
					}
				}, 1000)
			},
			startCount(timeReduce){
				const {day, hour, min, seconds} = this.formateTimeStamp(timeReduce)
				if(day === '00'){
					this.endtime = `${hour}:${min}:${seconds}`
				}else{
					this.endtime = `${day}天 ${hour}:${min}:${seconds}`
				}
				this.endTimeTimer = setInterval(()=>{
					timeReduce -= 1
					const timeObj = this.formateTimeStamp(timeReduce)
					if(this.endtime === "00:00:01"){
						wx.showToast({
							title: '商品已过期!',
							icon: 'none'
						})
						clearInterval(this.endTimeTimer)
						setTimeout(()=>{
							wx.navigateBack(-1)
						}, 1000)
					}
					const {day, hour, min, seconds} = timeObj
					if(timeObj.day === '00'){
						this.endtime = `${hour}:${min}:${seconds}`
					}else{
						this.endtime = `${day}天 ${hour}:${min}:${seconds}`
					}
				}, 1000)
			},
			setTime(secondTime){
				let minuteTime = 0;// 分
				let hourTime = 0;// 小时
				if(secondTime > 60) {//如果秒数大于60，将秒数转换成整数
					//获取分钟，除以60取整数，得到整数分钟
					minuteTime = parseInt(secondTime / 60);
					//获取秒数，秒数取佘，得到整数秒数
					secondTime = parseInt(secondTime % 60);
					//如果分钟大于60，将分钟转换成小时
					if(minuteTime > 60) {
						//获取小时，获取分钟除以60，得到整数小时
						hourTime = parseInt(minuteTime / 60);
						//获取小时后取佘的分，获取分钟除以60取佘的分
						minuteTime = parseInt(minuteTime % 60);
					}
				}
				let result = [parseInt(secondTime)];
		
				if(minuteTime > 0) {
					result.unshift(parseInt(minuteTime))
				}else{
					result.unshift(0)
				}
				if(hourTime > 0) {
					result.unshift(parseInt(hourTime))
				}else{
					result.unshift(0)
				}
				console.log(result)
				return result;
			},
			chooseTab(id){
				this.currenTab = id
			},
			toPath(url, val){
				if(val){
					wx.navigateTo({
						url,
						success: (res)=>{
							res.eventChannel.emit('acceptDataFromOpenerPage', { biddings: this.biddings })
						}
					})
				}else{
					wx.navigateTo({
						url
					})
				}
			},
			showCaculater(){
				this.$refs.popupCaculater.open('bottom')
			},
			closeCaculater(){
				this.$refs.popupCaculater.close('bottom')
			},
			reduce(){
				this.wantedWeight--
			},
			add(){
				this.wantedWeight++
			},
			onInput(e){
				const {value} = e.detail
				if(parseInt(value) < 1) this.wantedWeight = 1
			},
			showPopupPrice(type){
				// if(!this.detail?.orther?.jijuePrice && type === 1){
				// 	wx.showToast({
				// 		icon: 'none',
				// 		title: '此商品不支持一口价'
				// 	})
				// 	return false
				// }
				this.priceType = type
				// if(type === 1){
				// 	this.wantedPrice = this.detail.orther.jijuePrice
				// }
				this.$refs.popupPrice.open('bottom')
			},
			closePopupPrice(type){
				this.priceType = type
				this.$refs.popupPrice.close('bottom')
			},
			onUserAgree(e){
				this.userAgree = e.detail.value
			},
			async order(val){
				console.log(this.userAgree.length)
				if(this.userAgree.length === 0){
					wx.showToast({
						icon: 'none',
						title: '请点击同意上述说明'
					})
					return false
				}
				
				if(Number(this.wantedPrice) === 0){
					wx.showToast({
						icon: 'none',
						title: '出价不能为0'
					})
					return false
				}
				if(Number(this.wantedPrice) < this.detail.orther.jpprice){
					wx.showToast({
						icon: 'none',
						title: '出价不能小于现在价格'
					})
					return false
				}
				if(this.yjbalance < 2000){
					if(this.jpbalance < Number(this.wantedPrice)){
						wx.showToast({
							icon: 'none',
							title: '押金不足，押金超过2000元，可随便出价'
						})
						return false
					}
				}
				wx.showToast({
					icon: 'none',
					title: '暂未开放'
				})
				return false
				
				wx.showLoading({title: '下单中'})
				let canImmediate = false
				if(this.detail.orther.jijuePrice && this.wantedPrice >= this.detail.orther.jijuePrice) canImmediate = true
				const params = {
					userid: this.userInfo.userid,
					goodCode: this.query.code,
					imageurl: this.imgList[0],
					title: this.detail.orther.title,
					price: this.wantedPrice,
					type: canImmediate ? 1 : val,
					ratetype: this.isHaveRate
				}
				console.log('params', params)
				const res = await this.$api.yahooOrder(params)
				const { code, msg } = res.data
				if(code === 0){
					wx.showToast({
						title: '下单成功'
					})
					setTimeout(()=>{
						wx.navigateTo({
							url: '/pages/mine/orderList?type=1'
						})
					}, 1500)
				}else{
					wx.showToast({
						title: msg
					})
				}
				wx.hideLoading()
			},
			verifyLogin(method, val){
				if(wx.getStorageSync('login')){
					this[method](val)
				}else{
					wx.showToast({
						icon: 'none',
						title: '暂未登录，请先登录小程序'
					})
					setTimeout(()=>{
						wx.switchTab({
							url: '/pages/mine/index'
						})
					}, 1500)
				}
			},
			forDate(timestamp){
			    const date = new Date(timestamp);
			//     const date = new Date(timestamp);
			    const y = date.getFullYear();
			    let m = date.getMonth() + 1;
			    m = m < 10 ? '0' + m : m;
			    let d = date.getDate();
			    d = d < 10 ? ('0' + d) : d;
			    let h = date.getHours();
			    h = h < 10 ? ('0' + h) : h;
			    let mm = date.getMinutes();
			    mm = mm < 10 ? ('0' + mm) : mm;
			    let ss = date.getSeconds();
			    ss= ss < 10 ? ('0' + ss) : ss;
			    return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + ss
			},
			
			formateTimeStamp(timeStamp) {
			    var day;
			    var hour;
			    var min;
			    var seconds;
			 
			    day = parseInt(timeStamp / (24 * 60 * 60)) // 计算整数天数
			    var afterDay = timeStamp - day * 24 * 60 * 60 // 取得算出天数后剩余的秒数
			    hour = parseInt(afterDay / (60 * 60)) // 计算整数小时数
			    var afterHour = timeStamp - day * 24 * 60 * 60 - hour * 60 * 60 // 取得算出小时数后剩余的秒数
			    min = parseInt(afterHour / 60) // 计算整数分
			    seconds = parseInt(timeStamp - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60) // 取得算出分后剩余的秒数
			 
			    if (day < 10) {
			      day = '0' + day;
			    }
			 
			    if (hour < 10) {
			      hour = '0' + hour
			    };
			 
			    if (min < 10) {
			      min = '0' + min;
			    }
			 
			    if (seconds < 10) {
			      seconds = '0' + seconds;
			    }
			 
			    const restStamp = {
			      day: day,
			      hour: hour,
			      min: min,
			      seconds: seconds
			    }
			    return restStamp
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
			copyTitle(val){
				wx.setClipboardData({
					data: val,
					success: ()=>{
						wx.showToast({
							title: '复制成功'
						})
					}
				})
			},
			bindload(e){
				console.log(e)
				// const {height, width} = e.detail
				// if(width > height){
					
				// }
			}
		}
	}
</script>

<style lang="scss">
	.goodsDetail{
		padding-bottom: 160rpx;
	}
	.swiper{
		width: 750rpx;
		height: 650rpx;
		swiper{
			width: 750rpx;
			height: 650rpx;
			.slide-image{
				width: 750rpx;
				height: 650rpx;
				image{
					max-width: 750rpx;
					max-height: 650rpx;
					// width: 750rpx;
					// height: 650rpx;
				}
			}
			// background: rgb(251, 121, 23);
		}
	}
	.timer{
		box-sizing: border-box;
		padding: 0 20rpx;
		background: rgb(251, 121, 23);
		color: #FFF;
		font-size: 26rpx;
		height: 100rpx;
		.reflash{
			text-align: right;
		}
	}
	
	.title{
		display: block;
		font-weight: 600;
		// line-height: 50rpx;
		margin: 30rpx 0;
		padding: 0 20rpx;
		// display: -webkit-box;
		// -webkit-box-orient: vertical;
		// -webkit-line-clamp: 2;     //  限制行数  
		// overflow: hidden;
		// text-overflow : ellipsis; 
		image{
			width: 30rpx;
			height:28rpx;
			position: relative;
			top: 4rpx;
		}
	}
	.currentPrice{
		padding: 0 20rpx;
		text:nth-child(1), text:nth-child(4){
			font-weight: 600;
			font-size: 24rpx;
		}
		text:nth-child(2), text:nth-child(5){
			font-weight: 600;
			font-size: 30rpx;
			color: #ffa500;
			margin: 0 10rpx 0 30rpx;
		}
		text:nth-child(3), text:nth-child(6){
			font-weight: 600;
			font-size: 20rpx;
			opacity: 0.5;
		}
	}
	.priceLog{
		padding: 40rpx 20rpx;
		font-size: 24rpx;
		margin-top: 30rpx;
		border-top: 16rpx solid #f7f7f7;
		border-bottom: 16rpx solid #f7f7f7;
		.log .text{
			opacity: 0.5;
			margin: 0 10rpx;
		}
		.highest text{
			margin-left: 20rpx;
			color: red;
		}
		.highest2{
			font-weight: 600;
			.count{
				font-weight: 500;
				margin-top: 30rpx;
				color: #848080;
			}
			text{
				font-weight: 500;
				color: #848080;
			}
		}
	}
	.priceLog2{
		padding: 20rpx;
	}
	
	.tips{
		padding: 40rpx 20rpx;
		font-size: 24rpx;
		border-bottom: 16rpx solid #f7f7f7;
		.tipTitle{
			color: #ffa500;
		}
		.item{
			margin-top: 20rpx;
			opacity: 0.7;
		}
	}
	
	.tabs{
		padding: 40rpx 20rpx 0 20rpx;
		border-bottom: 1px solid #e7e7e7;
		.item{
			width: 150rpx;
			text-align: center;
			padding-bottom: 40rpx;
		}
		.active{
			color: #ffa500;
			font-weight: 600;
			border-bottom: 1px solid #ffa500;
		}
	}
	
	.richText{
		min-height: 500rpx;
		padding: 40rpx 20rpx;
		overflow: auto;
	}
	.options{
		align-items: center;
		font-size: 26rpx;
		width: 750rpx;
		box-sizing: border-box;
		background: #fff;
		position: fixed;
		bottom: 0;
		left: 0;
		padding: 0 20rpx;
		height: 100rpx;
		.item{
			width: 120rpx;
			margin-right: 20rpx;
			image{
				width: 40rpx;
				height: 40rpx;
			}
		}
		.btns{
			width: 400rpx;
			height: 70rpx;
			color: #FFF;
			border-radius: 20rpx;
			overflow: hidden;
			.btn{
				height: 70rpx;
				line-height: 70rpx;
				width: 50%;
				text-align: center;
				background: #ffa500;
			}
			.btn:last-child{
				background: #fb7917;
			}
		}
	}
	
	.popup-content{
		font-size: 26rpx;
		.title{
			font-size: 30rpx;
			font-weight: 600;
			text-align: center;
			padding-bottom: 30rpx;
			border-bottom: 1px solid #e5e5e5;
		}
		.price{
			padding: 0 20rpx;
			.label{
				width: 250rpx;
				font-weight: 600;
			}
			input{
				width: 150rpx;
				border: 1px solid #e5e5e5;
				color: #ffa500;
				padding: 0 10rpx;
			}
			text{
				color: #ffa500;
				margin-left: 15rpx;
			}
		}
		.weight{
			margin-top: 30rpx;
			padding: 0 20rpx;
			.label{
				width: 250rpx;
				font-weight: 600;
			}
			input{
				width: 80rpx;
				border: 1px solid #e5e5e5;
				color: #ffa500;
				padding: 0 10rpx;
				text-align: center;
			}
			button{
				width: 46rpx;
				height: 46rpx;
				line-height: 46rpx;
				padding: 0;
				text-align: center;
			}
			text{
				color: #ffa500;
				margin-left: 15rpx;
			}
		}
		.btns{
			padding: 30rpx 20rpx;
			height: 80rpx;
			color: #FFF;
			font-weight: 600;
			.btn{
				width: 300rpx;
				text-align: center;
				height: 80rpx;
				line-height: 80rpx;
				background: #ffa500;
				border-radius: 20rpx;
			}
			.btn:last-child{
				margin-left: 30rpx;
				width: 200rpx;
				background: #d2cbcb;
			}
		}
		.info{
			padding: 0 20rpx;
			.item{
				margin-bottom: 30rpx;
				.label{
					width: 300rpx
				}
				text{
					color: #ffa500;
				}
			}
		}
		.tips{
			view{
				margin-bottom: 8rpx;
			}
			text{
				font-weight: 600;
			}
		}
	}
	
	.popup-content-price{
		padding: 60rpx 30rpx 0;
		font-size: 26rpx;
		.priceItem{
			margin-bottom: 60rpx;
			.label{
				font-weight: 600;
			}
			.value{
				width: 500rpx;
				text-align: right;
				font-weight: 600;
				text{
					color: red;
					margin-right: 20rpx;
				}
				input{
					width: 200rpx;
					border: 1px solid #e5e5e5;
					color: red;
					padding: 0 10rpx;
					text-align: right;
					margin-right: 20rpx;
				}
			}
			.value2{
				justify-content: flex-end;
			}
		}
		.tips{
			.item:first-child{
				color: red;
				font-weight: 600;
			}
		}
		.agree text text{
			color: #2440b3;
		}
		.btns{
			margin-top: 30rpx;
			width: 710rpx;
			height: 70rpx;
			color: #fff;
			.btn{
				height: 70rpx;
				line-height: 70rpx;
				width: 45%;
				text-align: center;
				background: #d2cbcb;
				border-radius: 20rpx;
				font-weight: 600;
			}
			.btn:last-child{
				background: #fb7917;
			}
		}
	}
</style>
