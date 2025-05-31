<template>
	<view class="page">
		<view class="popup-content charge">
				<view class="content">
					<view class="substite center">收费标准</view>
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
				</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				HtMLData: ``
			}
		},
		onLoad() {
			this.HtMLData = wx.getStorageSync('setting')?.biddingrules || ''
			// const newUserId = res.userId
			// const oldUserId = sessionStorage.getItem('userId')
			// if(newUserId !== oldUserId){
			// 	// 清空导航栏
			// }
			// sessionStorage.setItem('userId', newUserId)
			
		},
		methods: {
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
		}
	}
</script>

<style lang="scss">
	.content{
		height: auto;
		margin-bottom: 150rpx;
	}
	.charge{
		width: 750rpx;
		min-height: 200rpx;
		position: relative;
		border-radius: 30rpx;
		padding: 0 30rpx 30rpx;
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
		}
	}
</style>
