<template>
    <div class="detailPage between" v-loading="loading">
        <div v-if="deviceType === 'pc'" class="between">
            <div class="left scrollbar">
                <div class="pagetitle">
                    订单详情：共{{orderList.length}}件商品(共计：{{totalPrice}} {{type === '1' ? '円' : '元'}})
                </div>
                <div class="list">
                    <div class="item" v-for="(item, index) in orderList" :key="index">
                        <div class="itemHeader between">
                            <div class="orderNo">订单号：{{item.orderid}}</div>
                        </div>
                        <div class="detail between">
                            <img :src="item.imageurl" alt=""/>
                            <div class="info column">
                                <div class="title">{{item.title}}</div>
                                <div class="price">支付价格：{{item.price}}</div>
                                <div class="code">商品编码：{{item.goodCode}}</div>
                                <div class="status" v-if="query.type === '2'">状态：{{mercariTypeMap[item.type]}}</div>
                                <div class="status" v-if="query.type === '1'">状态：{{yahooTypeMap[item.type]}}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="addressDetail" v-if="mainOrderDetail.luxian">
                    <div class="pagetitle">邮寄要求</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>邮寄路线：</p>
                            <p>{{ mainOrderDetail.luxian }}</p>
                        </div>
                        <div class="item flex">
                            <p>备注：</p>
                            <p>{{ mainOrderDetail.lxRemarks }}</p>
                        </div>
                    </div>
                </div>


                <div class="addressDetail" v-if="addressDetail">
                    <div class="pagetitle">地址详情</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>收件人：</p>
                            <p>{{ addressDetail.username }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话：</p>
                            <p>{{ addressDetail.phone }}</p>
                        </div>
                        <div class="item flex">
                            <p>收件地址：</p>
                            <p>{{ addressDetail.addressPlus }}</p>
                        </div>
                    </div>
                </div>

                
                <div class="addressDetail">
                    <div class="pagetitle">下单用户</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>用户头像：</p>
                            <p>
                                <img :src="userDetail.avatarUrl" alt=""/>
                            </p>
                        </div>
                        <div class="item flex">
                            <p>微信昵称：</p>
                            <p>{{ userDetail.nickName }}</p>
                        </div>
                        <div class="item flex">
                            <p>微信id：</p>
                            <p>{{ userDetail.userid }}</p>
                        </div>
                        <div class="item flex">
                            <p>用户姓名：</p>
                            <p>{{ userDetail.realname }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话：</p>
                            <p>{{ userDetail.phone }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话2：</p>
                            <p>{{ userDetail.twophone }}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="line"></div>
            <div class="right scrollbar" >
                <div class="payRest" v-if="mainOrderDetail.type > ( query.type === '1' ? 4 : 2)">
                    <div class="pagetitle">补差价详情</div>
                    <el-form class="detail" ref="form" :model="form" label-width="120px">
                        <el-form-item label="日本邮费(元)">
                        <!-- <el-form-item label="日本邮费(円)"> -->
                            <!-- <el-input v-model="form.jpPostage_yuan" @input="onPostYuanChange" type="number" :disabled="isReivew" style="width: 200px"></el-input> -->
                            <el-input v-model="form.jpPostage" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                            <!-- <div>{{form.jpPostage}}元</div> -->
                        </el-form-item>
                        <el-form-item label="国际运费(元)">
                            <el-input v-model="form.internationalPostage" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="仓储费(元)">
                            <el-input v-model="form.storageCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="包装费(元)">
                            <el-input v-model="form.packingCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="其他费用(元)">
                            <el-input v-model="form.otherCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="其他费用备注">
                            <el-input v-model="form.otherChargeMarks" type="textarea" :rows="4" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item v-if="!isReivew">
                            <el-button type="primary" @click="onSubmit" v-if="query.option === 'countRest'">确认</el-button>
                            <el-button type="primary" @click="onSubmit" v-if="query.option === 'changeCount'">更正</el-button>
                            <el-button @click="handleClose">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <div class="sendPackage" v-if="mainOrderDetail.type > ( query.type === '1' ? 7 : 4)">
                    <div class="pagetitle">邮寄详情</div>
                    <el-form ref="form" :model="sendForm" label-width="120px">
                        <el-form-item label="物流公司">
                            <el-input v-model="sendForm.expressDeliveryCompany" type="text" :disabled="isReivew && query.option !== 'sendPackage'"></el-input>
                        </el-form-item>
                        <el-form-item label="物流单号">
                            <el-input v-model="sendForm.expressDeliveryNo" type="text" :disabled="isReivew && query.option !== 'sendPackage'"></el-input>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input v-model="sendForm.remarks" type="textarea" :rows="4" :disabled="isReivew && query.option !== 'sendPackage'"></el-input>
                        </el-form-item>
                        <el-form-item v-if="!isReivew || query.option === 'sendPackage'">
                            <el-button type="primary" @click="onSendFormSubmit">确认</el-button>
                            <el-button @click="handleClose">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>

        <div class="page_m" v-else>
            <div class="left">
                <div class="pagetitle">
                    订单详情：共{{orderList.length}}件商品(共计：{{totalPrice}} {{type === '1' ? '円' : '元'}})
                </div>
                <div class="list">
                    <div class="item" v-for="(item, index) in orderList" :key="index">
                        <div class="itemHeader between">
                            <div class="orderNo">订单号：{{item.orderid}}</div>
                        </div>
                        <div class="detail between">
                            <img :src="item.imageurl" alt=""/>
                            <div class="info column">
                                <div class="title">{{item.title}}</div>
                                <div class="price">支付价格：{{item.price}}</div>
                                <div class="code">商品编码：{{item.goodCode}}</div>
                                <div class="status" v-if="query.type === '2'">状态：{{mercariTypeMap[item.type]}}</div>
                                <div class="status" v-if="query.type === '1'">状态：{{yahooTypeMap[item.type]}}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="addressDetail" v-if="mainOrderDetail.luxian">
                    <div class="pagetitle">邮寄要求</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>邮寄路线：</p>
                            <p>{{ mainOrderDetail.luxian }}</p>
                        </div>
                        <div class="item flex">
                            <p>备注：</p>
                            <p>{{ mainOrderDetail.lxRemarks }}</p>
                        </div>
                    </div>
                </div>


                <div class="addressDetail" v-if="addressDetail">
                    <div class="pagetitle">地址详情</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>收件人：</p>
                            <p>{{ addressDetail.username }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话：</p>
                            <p>{{ addressDetail.phone }}</p>
                        </div>
                        <div class="item flex">
                            <p>收件地址：</p>
                            <p>{{ addressDetail.addressPlus }}</p>
                        </div>
                    </div>
                </div>

                
                <div class="addressDetail">
                    <div class="pagetitle">下单用户</div>
                    <div class="addressInfo">
                        <div class="item flex">
                            <p>用户头像：</p>
                            <p>
                                <img :src="userDetail.avatarUrl" alt=""/>
                            </p>
                        </div>
                        <div class="item flex">
                            <p>微信昵称：</p>
                            <p>{{ userDetail.nickName }}</p>
                        </div>
                        <div class="item flex">
                            <p>微信id：</p>
                            <p>{{ userDetail.userid }}</p>
                        </div>
                        <div class="item flex">
                            <p>用户姓名：</p>
                            <p>{{ userDetail.realname }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话：</p>
                            <p>{{ userDetail.phone }}</p>
                        </div>
                        <div class="item flex">
                            <p>联系电话2：</p>
                            <p>{{ userDetail.twophone }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="arrow center" @click="showRight">
                <i class="el-icon-arrow-left"></i>
            </div>
        </div>

        <el-drawer
            title=""
            size="90%"
            :visible.sync="drawer"
            :direction="'rtl'"
            :wrapperClosable="false"
            :before-close="closeDrawer">
            <div class="right right2" >
                <div class="payRest" v-if="mainOrderDetail.type > ( query.type === '1' ? 4 : 2)">
                    <div class="pagetitle">补差价详情</div>
                    <el-form class="detail" ref="form" :model="form" label-width="120px">
                        <el-form-item label="日本邮费(元)">
                        <!-- <el-form-item label="日本邮费(円)"> -->
                            <!-- <el-input v-model="form.jpPostage_yuan" @input="onPostYuanChange" type="number" :disabled="isReivew" style="width: 200px"></el-input> -->
                            <el-input v-model="form.jpPostage" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                            <!-- <div>{{form.jpPostage}}元</div> -->
                        </el-form-item>
                        <el-form-item label="国际运费(元)">
                            <el-input v-model="form.internationalPostage" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="仓储费(元)">
                            <el-input v-model="form.storageCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="包装费(元)">
                            <el-input v-model="form.packingCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="其他费用(元)">
                            <el-input v-model="form.otherCharge" type="number" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="其他费用备注">
                            <el-input v-model="form.otherChargeMarks" type="textarea" :rows="4" :disabled="isReivew" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item v-if="!isReivew">
                            <el-button type="primary" @click="onSubmit" v-if="query.option === 'countRest'">确认</el-button>
                            <el-button type="primary" @click="onSubmit" v-if="query.option === 'changeCount'">更正</el-button>
                            <el-button @click="handleClose">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <div class="sendPackage" v-if="mainOrderDetail.type > ( query.type === '1' ? 7 : 4)">
                    <div class="pagetitle">邮寄详情</div>
                    <el-form ref="form" :model="sendForm" label-width="120px">
                        <el-form-item label="物流公司">
                            <el-input v-model="sendForm.expressDeliveryCompany" type="text" :disabled="isReivew && query.option !== 'sendPackage'" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="物流单号">
                            <el-input v-model="sendForm.expressDeliveryNo" type="text" :disabled="isReivew && query.option !== 'sendPackage'" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input v-model="sendForm.remarks" type="textarea" :rows="4" :disabled="isReivew && query.option !== 'sendPackage'" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item v-if="!isReivew || query.option === 'sendPackage'">
                            <el-button type="primary" @click="onSendFormSubmit">确认</el-button>
                            <el-button @click="handleClose">取消</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </el-drawer>


    </div>
</template>
  
<script>

import {
    queryMercariOrderDetail,
    countCost,
    sendGood,
    queryYahooGoodDetail,
    countYahooCost,
    sendYahooGood,
    queryRate
}
    from "@/http/api"

import { mapState } from 'vuex'

export default {
    data(){
        return {
            drawer: false,
            
            totalPrice: 0,
            loading: false,
            type: null,
            query: {},
            orderList: [],
            mainOrderDetail: {},
            addressDetail: null,
            userDetail: {},
            mercariTypeMap: {
                1: "付款成功，等待入库",
                2: "已入库",
                3: "已申请发货，等待计算尾款",
                4: "待付尾款",
                5: "待发货",
                6: "已发货",
            },
            yahooTypeMap: {
                1: "最高出价中，等待竞拍结束",
                2: "竞拍成功，请补差价",
                3: "竞拍失败",
                4: "付款成功，等待入库",
                5: "已入库",
                6: "已申请发货，等待计算尾款",
                7: "待付尾款",
                8: "待发货",
                9: "已发货",
                10: "竞价被超越",
                100: "预约出价中，等待竞拍结束"
            },
            
            form: {
                jpPostage_yuan: '',
                agentFee: '',
                jpPostage: '',
                internationalPostage: '',
                storageCharge: '',
                packingCharge: '',
                otherCharge: '',
                otherChargeMarks: '',
            },
            sendForm: {
                expressDeliveryCompany: '',
                expressDeliveryNo: '',
                remarks: '',
            },
            isReivew: false,

            rate: 0,
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    async created(){
        await this.getRate()
        this.setTabs()
        this.type = this.$route.query.type
        this.query = this.$route.query
        this.isReivew = Boolean(this.$route.query.review)
        if(Boolean(this.$route.query.review) || this.$route.query.option === 'sendPackage'){
            this.isReivew = true
        }
        this.orderList = []
        if(this.type === '1'){
            await this.queryYahooGoodDetail()
        }else{
            await this.queryMercariOrderDetail()
        }

        let total = 0
        this.orderList?.forEach(item=>{
            console.log(item.jpyunfei)
            total = total + parseInt(item.jpyunfei*100)
        })

        this.form.jpPostage = total / 100
            
    },
    methods: {
        async getRate(){
            this.loading = true
            const res = await queryRate()
            const {code, data, msg} = res
            if(code === 0){
                this.rate = data
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        onPostYuanChange(val){
            this.form.jpPostage = parseInt((this.form.jpPostage_yuan * this.rate) * 100) / 100
        },
        async queryMercariOrderDetail(){
            this.loading = true
            const params = {
                userid: this.query.userid,
                id: this.query.id,
                addressid: this.query.addressid,
            }
            const res = await queryMercariOrderDetail(params)
            const { data } = res
            

            this.mainOrderDetail = data.orderDetile
            this.orderList.push(data.orderDetile)
            if(data.childOrder) this.orderList.push(...data.childOrder)

            let totalPrice = 0
            this.orderList?.forEach(item=>{
                totalPrice = totalPrice + item.price * 10000
            })
            this.totalPrice = totalPrice / 10000
            

            if(data.userAddress){
                data.userAddress.region = data.userAddress.address.split('----')[0].replace(/,/g, ' ')
                data.userAddress.address = data.userAddress.address.split('----')[1]
                data.userAddress.addressPlus = data.userAddress.region + data.userAddress.address
            }
            this.addressDetail = data.userAddress

            this.userDetail = data.user

            
            this.form.agentFee = data.orderDetile.dgPrice || ''
            this.form.jpPostage = data.orderDetile.jpyunfei || ''
            this.form.internationalPostage = data.orderDetile.gjyunfei || ''
            this.form.storageCharge = data.orderDetile.cangcufei || ''
            this.form.packingCharge = data.orderDetile.baozhuangfei || ''
            this.form.otherCharge = data.orderDetile.otherPrice || ''
            this.form.otherChargeMarks = data.orderDetile.otherPriceContent || ''

            
            this.sendForm.expressDeliveryCompany = data.orderDetile.wlcompany || ''
            this.sendForm.expressDeliveryNo = data.orderDetile.trackingNumber || ''
            this.sendForm.remarks = data.orderDetile.wlRemarks || ''

            this.loading = false

        },

        async queryYahooGoodDetail(){
            this.loading = true
            const params = {
                userid: this.query.userid,
                id: this.query.id * 1,
                addressid: this.query.addressid,
            }
            const res = await queryYahooGoodDetail(params)
            const { data, code } = res
            if(code !== 0){
                this.loading = false
                this.$message.error('服务器出错')
                return false
            }
            this.mainOrderDetail = data.orderDetile
            this.orderList.push(data.orderDetile)
            if(data.childOrder) this.orderList.push(...data.childOrder)

            let totalPrice = 0
            this.orderList?.forEach(item=>{
                totalPrice = totalPrice + item.price * 10000
            })
            this.totalPrice = totalPrice / 10000

            if(data.userAddress){
                data.userAddress.region = data.userAddress.address.split('----')[0].replace(/,/g, ' ')
                data.userAddress.address = data.userAddress.address.split('----')[1]
                data.userAddress.addressPlus = data.userAddress.region + data.userAddress.address
            }
            this.addressDetail = data.userAddress

            this.userDetail = data.user
            
            this.form.agentFee = data.orderDetile.dgPrice || ''
            this.form.jpPostage = data.orderDetile.jpyunfei || ''
            this.form.internationalPostage = data.orderDetile.gjyunfei || ''
            this.form.storageCharge = data.orderDetile.cangcufei || ''
            this.form.packingCharge = data.orderDetile.baozhuangfei || ''
            this.form.otherCharge = data.orderDetile.otherPrice || ''
            this.form.otherChargeMarks = data.orderDetile.otherPriceContent || ''

            
            this.sendForm.expressDeliveryCompany = data.orderDetile.wlcompany || ''
            this.sendForm.expressDeliveryNo = data.orderDetile.trackingNumber || ''
            this.sendForm.remarks = data.orderDetile.wlRemarks || ''

            this.loading = false

        },
        
        
        onSubmit(){
            const count =   (this.form.agentFee * 100)
                            + 
                            (this.form.jpPostage * 100)
                            + 
                            (this.form.internationalPostage * 100)
                             + 
                            (this.form.storageCharge * 100)
                             + 
                            (this.form.packingCharge * 100)
                             + 
                            (this.form.otherCharge * 100)
            this.$confirm(`共计费用${parseInt(count) / 100}元，是否确认结算`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    dgPrice: this.form.agentFee * 1 || 0,
                    jpyunfei: this.form.jpPostage * 1 || 0,
                    gjyunfei: this.form.internationalPostage * 1 || 0,
                    cangcufei: this.form.storageCharge * 1 || 0,
                    baozhuangfei: this.form.packingCharge * 1 || 0,
                    otherPrice: this.form.otherCharge * 1 || 0,
                    otherPriceContent: this.form.otherChargeMarks,
                    id: this.query.id,
                    userid: this.query.userid,
                }
                let res = null
                if(this.query.type === '1'){
                    res = await countYahooCost(params)
                }else{
                    res = await countCost(params)
                }
                
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.$router.go(-1)
                }else{
                    this.$message.error(msg)
                }
                this.loading = false
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });          
            });
        },
        
        async onSendFormSubmit(){
            this.loading = true
            const params = {
                wlcompany: this.sendForm.expressDeliveryCompany,
                trackingNumber: this.sendForm.expressDeliveryNo,
                wlRemarks: this.sendForm.remarks,
                userid: this.query.userid,
                id: this.query.id,
            }
            let res = null
            if(this.query.type === '1'){
                res = await sendYahooGood(params)
            }else{
                res = await sendGood(params)
            }
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.$router.go(-1)
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleClose(){
            this.$router.go(-1)
        },
        setTabs(){
            console.log(window.location.hash.replace('#', ''))
            this.$store.commit('add_historyRecord', {
                path: window.location.hash.replace('#', ''),
                meta: {
                    title: '商品详情'
                }
            })
        },
        showRight(){
            this.drawer = true
        },
        closeDrawer(){
            this.drawer = false
        },
    }
}
</script>
  
<style lang="scss" scoped>

    .pagetitle{
        font-size: 24px;
        position: relative;
        left: 10px;
    }
    .pagetitle::after{
        content: '';
        position: absolute;
        width: 3px;
        height: 20px;
        background: gold;
        top: 0;
        bottom: 0;
        left: -10px;
        margin: auto;
    }
    .detailPage{
        width: 100%;
        height: calc(100vh - 112px);
        align-items: flex-start;
        .left{
            height: calc(100vh - 150px);
            width: 70%;
            overflow-y: auto;
            overflow-x: hidden;
        }
        .line{
            width: 1px;
            height: 100%;
            background: #bbb;
        }
        .right{
            padding-left: 10px;
            width: 30%;
            height: calc(100vh - 150px);
            overflow-y: auto;
            overflow-x: hidden;
        }
    }
    .list{
        margin-top: 20px;
        .item{
            margin-bottom: 50px;
            .detail{
                margin-top: 10px;
                img{
                    width: 150px;
                    height: 150px;
                }
                .info{
                    height: 150px;
                    width: calc(100% - 180px);
                    align-items: flex-start;
                    justify-content: space-between;
                }
            }
        }
    }
    .addressDetail{
        margin-bottom: 30px;

    }
    .addressDetail .addressInfo .item{
        margin: 10px 0;
        p:first-child{
            width: 100px;
        }
        p:last-child{
            width: calc(100% - 100px);
            word-wrap: break-word;
            img{
                width: 50px;
                height: 50px;
                border-radius: 50%;
            }
        }
    }

    .right{
        .detail{
            margin-top: 20px;
        }
    }


    ::v-deep input::-webkit-outer-spin-button, ::v-deep input::-webkit-inner-spin-button{
        -webkit-appearance: none !important;
        -moz-appearance: none !important;
        -o-appearance: none !important;
        -ms-appearance: none !important;
        appearance: none !important;
        margin: 0;
    }
    ::v-deep input[type="number"]{
        -webkit-appearance:textfield;
        -moz-appearance:textfield;
        -o-appearance:textfield;
        -ms-appearance:textfield;
        appearance:textfield;
    }


    .status{
        color: red;
    }

.page_m{
    background: #FFF;
    font-size: 14px;
    padding: 15px;
    box-sizing: border-box;
    width: 100vw;
    position: relative;
    .pagetitle{
        margin-top: 20px;
        font-size: 14px;
    }
    .left{
        width: 100%;
        .list{
            .item{
                .detail{
                    img{
                        width: 25vw;
                        height: 25vw;
                    }
                    .info{
                        height: 30vw;
                        width: 65vw;
                        font-size: 12px;
                    }
                }
            }
        }
    }

    .arrow{
        position: fixed;
        top: 50vh;
        right: 0;
        width: 25px;
        height: 50px;
        border-radius: 5px;
        border: 1px solid #d1cfcf;
    }
}

.right2{
    width: 100% !important;
    font-size: 14px !important;
    .pagetitle{
        font-size: 14px !important;
    }
}
</style>
  