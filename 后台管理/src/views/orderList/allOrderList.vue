<template>
    <div class="page" v-loading="loading">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div class="flex form">
                    <div class="item center">
                        <div class="title">订单编号</div>
                        <div class="value">
                            <el-input v-model="orderid" clearable></el-input>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">商品标题</div>
                        <div class="value">
                            <el-input v-model="title" clearable></el-input>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">用户昵称</div>
                        <div class="value">
                            <el-input v-model="nickName" clearable></el-input>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">下单时间</div>
                        <div class="value">
                            <el-date-picker
                                v-model="timeLimit"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                style="width: 545px">
                            </el-date-picker>
                        </div>
                    </div>
                    <i></i>
                </div>
                <div class="flex">
                    <el-button type="primary" @click="reset">重置</el-button>
                    <el-button type="primary" @click="searchOrder()">查询</el-button>
                    <el-button type="primary"
                        :loading="exportLoading"
                        @click="exportFile"
                        style="margin-left: 10px"
                        :disabled="multipleSelection.length === 0"
                        >
                            导出订单
                    </el-button>
                </div>
            </div>

            <div class="tabs">
                <el-radio-group v-model="currentPlant" style="margin-bottom: 30px;" @change="onCurrentPlantClick">
                    <el-radio-button label="yahoo">雅虎</el-radio-button>
                    <el-radio-button label="mercari" :disabled="!$route.query.mercariType">煤炉</el-radio-button>

                </el-radio-group>
            </div>

            <template v-if="currentPlant === 'yahoo'">
                <el-table :data="tableData_yahoo" border
                    style="width: 100%; margin-bottom: 20px;" row-key="id"
                    :default-expand-all="true"
                    :row-class-name="tableRowClassName"
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                    :span-method="objectSpanMethod"
                    @selection-change="handleSelectionChange"
                >
                    <!-- <el-table-column
                        v-if="$route.query.yahooType === '8'"
                        type="selection"
                        width="55">
                    </el-table-column> -->

                    <el-table-column label="序号" type="index" width="50"></el-table-column>

                    <el-table-column label="订单号" prop="orderid"  width="160">
                        <template slot-scope="scope">
                            <div :class="scope.row.isCurrentDay ? 'isCurrentDay' : ''">{{scope.row.orderid}}</div>
                        </template>
                    </el-table-column>

                    <el-table-column label="商品id" prop="goodCode"  width="100">
                        <template slot-scope="scope">
                            <a :href="`https://page.auctions.yahoo.co.jp/jp/auction/${scope.row.goodCode}`" target="_blank" style="color: blue"><div>{{ scope.row.goodCode }}</div></a>
                        </template>
                    </el-table-column>

                    <el-table-column label="商品图片" width="120"  header-align="center" align="center">
                        <template slot-scope="scope">
                            <img :src="scope.row.imageurl || ''" alt="" style="width: 80px; height: 80px; border-radius: 50%; margin: auto">
                        </template>
                    </el-table-column>

                    <el-table-column v-for="item in column1_yahoo" :key="item.index"  header-align="center"  align="center"
                        :prop="item.prop" :label="item.label" :width="item.width">
                    </el-table-column>
                    
                    <el-table-column v-for="item in column2_yahoo" :key="item.index"  header-align="center"  align="center"
                        :prop="item.prop" :label="item.label" :width="item.width">
                    </el-table-column>
                    
                    <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                        <template slot-scope="scope">
                            <div v-if="!scope.row.parentid" class="around">
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 2, scope.row.userid)" v-if="scope.row.type === 1">竞拍成功</el-button>
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 3, scope.row.userid)" v-if="scope.row.type === 1">竞拍失败</el-button>

                                <!-- 测试付款 -->
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 4, scope.row.userid)" v-if="scope.row.type === 2">支付</el-button>

                                <!-- 测试付款 -->
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 8, scope.row.userid)" v-if="scope.row.type === 7">支付</el-button>

                                <!-- 测试改已入库 -->
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 4, scope.row.userid)">改回待入库</el-button>

                                <!-- 测试改待发货 -->
                                <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 8, scope.row.userid)">改回待发货</el-button>

                                <el-button type="text" size="small" @click="showPostFee(scope.row)" v-if="scope.row.type === 4">已入库</el-button>
                                <!-- <el-button type="text" size="small" @click="changeStatus_yahoo(scope.row.id, 5, scope.row.userid)" v-if="scope.row.type === 4">已入库</el-button> -->
                                

                                <!-- 测试已入库 -->
                                <el-button type="text" size="small" @click="changeStatus_yahoo2(scope.row.orderid, 5, scope.row.userid)" v-if="scope.row.type === 4">已入库2</el-button>


                                <el-button type="text" size="small" @click="toDetail(scope.row, 1)" v-if="scope.row.type === 6">计算尾款</el-button>
                                <el-button type="text" size="small" @click="changeCount(scope.row)" v-if="scope.row.type === 7">更正尾款金额</el-button>
                                <el-button type="text" size="small" @click="sendPackage(scope.row, 1)" v-if="scope.row.type === 8">发货</el-button>
                                <el-button type="text" size="small" @click="review(scope.row)">查看</el-button>
                                <el-button type="text" size="small" @click="deleteItem(scope.row)" style="color: red">删除</el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>


                <div class="between">
                    <p></p>
                    <p></p>
                    <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="pageNumber"
                        :page-sizes="[5, 10, 30, 50]"
                        :page-size="pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total"
                    >
                    </el-pagination>
                </div>
            </template>

            <template v-if="currentPlant === 'mercari'">
                <el-table :data="tableData_mercari" border
                    style="width: 100%; margin-bottom: 20px;" row-key="id"
                    :row-class-name="tableRowClassName"
                    :default-expand-all="true" 
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                    :span-method="objectSpanMethod"
                    @selection-change="handleSelectionChange"
                    >
                    
                    <!-- <el-table-column
                        v-if="$route.query.mercariType === '5'"
                        type="selection"
                        width="55">
                    </el-table-column> -->

                    <el-table-column label="序号" type="index" width="50"></el-table-column>

                    <el-table-column label="订单号" prop="orderid"  width="150">
                        <template slot-scope="scope">
                            <div :class="scope.row.isCurrentDay ? 'isCurrentDay' : ''">{{scope.row.orderid}}</div>
                        </template>
                    </el-table-column>

                    <el-table-column label="商品id" prop="goodCode"  width="120">
                        <template slot-scope="scope">
                            <a :href="`https://jp.mercari.com/item/${scope.row.goodCode}`" target="_blank" style="color: blue"><div>{{ scope.row.goodCode }}</div></a>
                        </template>
                    </el-table-column>

                    <el-table-column label="商品图片" width="120"  header-align="center" align="center">
                        <template slot-scope="scope">
                            <img :src="scope.row.imageurl || ''" alt="" style="width: 80px; height: 80px; border-radius: 50%; margin: auto">
                        </template>
                    </el-table-column>

                    <el-table-column v-for="item in column1_mercari" :key="item.index"  header-align="center"  align="center"
                        :prop="item.prop" :label="item.label" :width="item.width">
                    </el-table-column>

                    <!-- <el-table-column label="下单用户头像" width="120"  header-align="center" align="center">
                        <template slot-scope="scope">
                            <img :src="scope.row.user.avatarUrl || ''" alt="" style="width: 80px; height: 80px; border-radius: 50%; margin: auto">
                        </template>
                    </el-table-column> -->
                    
                    <el-table-column v-for="item in column2_mercari" :key="item.index"  header-align="center"  align="center"
                        :prop="item.prop" :label="item.label" :width="item.width">
                    </el-table-column>
                    
                    <el-table-column fixed="right" label="操作" width="150"  header-align="center" align="center">
                        <template slot-scope="scope">
                            <div v-if="!scope.row.parentid" class="around">
                                <el-button type="text" size="small" @click="orderRefund(scope.row)" v-if="scope.row.type === 1">退款</el-button>
                                <el-button type="text" size="small" @click="showPostFee(scope.row)" v-if="scope.row.type === 1">已入库</el-button>


                                <!-- 测试改已入库 -->
                                <el-button type="text" size="small" @click="changeStatus_mercari2(scope.row.orderid, 1, scope.row.userid)">改回待入库</el-button>

                                <!-- 测试改待发货 -->
                                <el-button type="text" size="small" @click="changeStatus_mercari2(scope.row.orderid, 4, scope.row.userid)">改回待发货</el-button>

                                <!-- 测试已入库 -->
                                <el-button type="text" size="small" @click="changeStatus_mercari2(scope.row.orderid, 5, scope.row.userid)" v-if="scope.row.type === 1">测试已入库</el-button>
                                <!-- 测试付尾款支付 -->
                                <el-button type="text" size="small" @click="changeStatus_mercari2(scope.row.orderid, 5, scope.row.userid)" v-if="scope.row.type === 4">支付尾款</el-button>


                                <!-- <el-button type="text" size="small" @click="changeStatus(scope.row.id, 2, scope.row.userid)" v-if="scope.row.type === 1">已入库</el-button> -->
                                <el-button type="text" size="small" @click="toDetail(scope.row, 2)" v-if="scope.row.type === 3">计算尾款</el-button>
                                <el-button type="text" size="small" @click="changeCount(scope.row)" v-if="scope.row.type === 4">更正尾款金额</el-button>
                                <el-button type="text" size="small" @click="sendPackage(scope.row, 2)" v-if="scope.row.type === 5">发货</el-button>
                                <el-button type="text" size="small" @click="review(scope.row)">查看</el-button>
                                <el-button type="text" size="small" @click="deleteItem(scope.row)" style="color: red">删除</el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>

                <div class="between">
                    <p></p>
                    <p></p>
                    <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="pageNumber"
                        :page-sizes="[5, 10, 30, 50]"
                        :page-size="pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="total"
                    >
                    </el-pagination>
                </div>
            </template>
            
        </div>

        
        <div class="page_m" v-else>
            <div class="search_m">
                <div class="search between">
                    <div class="flex">
                        <div class="item center">
                            <div class="title">订单编号</div>
                            <div class="value">
                                <el-input v-model="orderid" clearable></el-input>
                            </div>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">商品标题</div>
                        <div class="value">
                            <el-input v-model="title" clearable></el-input>
                        </div>
                    </div>
                    <div class="item time center">
                        <div class="title">下单时间</div>
                        <div class="value">
                            <el-date-picker
                                v-model="timeLimit"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                :style="{width: deviceType === 'pc' ? '545px' : '85vw'}">
                            </el-date-picker>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">用户昵称</div>
                        <div class="value">
                            <el-input v-model="nickName" clearable></el-input>
                        </div>
                    </div>
                    <i></i>
                </div>
                <div class="flex seatchOptions">
                    <el-button type="primary" size="mini" @click="reset">重置</el-button>
                    <el-button type="primary" size="mini" @click="searchOrder()">查询</el-button>
                    <el-button type="primary" size="mini"
                        :loading="exportLoading"
                        @click="exportFile"
                        style="margin-left: 10px"
                        :disabled="multipleSelection.length === 0"
                        >
                            导出订单
                    </el-button>
                </div>
            </div>

            
            <div class="tabs tabs2">
                <el-radio-group v-model="tabPosition" style="margin-bottom: 30px;" @change="onTabClick">
                    <el-radio-button :label="0">全部</el-radio-button>
                    <el-radio-button :label="1">出价中</el-radio-button>
                    <el-radio-button :label="2">竞拍成功</el-radio-button>
                    <el-radio-button :label="4">待入库</el-radio-button>
                    <el-radio-button :label="5">已入库</el-radio-button>
                    <el-radio-button :label="6">申请发货</el-radio-button>
                    <el-radio-button :label="7">待付尾款</el-radio-button>
                    <el-radio-button :label="8">待发货</el-radio-button>
                    <el-radio-button :label="9">已发货</el-radio-button>
                    <el-radio-button :label="100">预约订单</el-radio-button>
                </el-radio-group>
            </div>

            <tableList
                type="yahooList"
                :list="tableData_yahoo"
                :column="column1_yahoo"
                :yahooTypeMap="statusMap"
                :showPagination="true"
                @handleSizeChange="handleSizeChange"
                @handleCurrentChange="handleCurrentChange"
                :pageNumber="pageNumber"
                :pageSize="pageSize"
                :total="total"

                @changeStatus_yahoo="changeStatus_yahoo"
                @showPostFee="showPostFee"
                @toDetail="toDetail"
                @changeCount="changeCount"
                @sendPackage="sendPackage"
                @review="review"
                @deleteItem="deleteItem"
            />
        </div>

        <el-dialog
            title="请输入运费"
            :visible="dialogVisible"
            :width="deviceType === 'pc' ? '600px' : '80%'"
            :close-on-click-modal="false"
            :before-close="handleClose">
            <div class="center">
                <div>运费：</div>
                <el-input v-model="postFee" style="width: 400px"></el-input>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="confirmGot">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
  
<script>
import {
    queryYahooOrderList,
    updataYahooOrderStatus,
    updataYahooOrderStatus2,
    countYahooCost,
    sendYahooGood,
    fixYahooPrice,
    exportYahooOrder,
    deleteYahooOrderList,
    queryMercariOrderList,
    updataMercariOrderStatus,
    updataMercariOrderStatus2,
} from "@/http/api"
import fileDownload from "js-file-download";
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
export default {
    components: {tableList},
    data(){
        return {
            postFee: '',
            hasChildren: true,
            loading: false,
            column1_yahoo: [
                { label: "商品标题", prop: "title", width: "" },
                { label: "商品价格(円)", prop: "price", width: "100" },
                { label: "价格总计(円)", prop: "priceTotal", width: "100" },
                { label: "订单状态", prop: "typeText", width: "120" },
                { label: "下单时间", prop: "createtime", width: "90" },
                { label: "用户id", prop: "userid", width: "60" },
            ],
            column2_yahoo: [
                { label: "用户昵称", prop: "user.nickName", width: "80" },
            ],

            column1_mercari: [
                { label: "商品标题", prop: "title", width: "" },
                { label: "商品价格(円)", prop: "JPprice", width: "100" },
                { label: "价格总计(元)", prop: "priceTotal", width: "100" },
                { label: "订单状态", prop: "typeText", width: "120" },
                { label: "下单时间", prop: "time", width: "90" },
                { label: "用户id", prop: "userid", width: "60" },
            ],
            column2_mercari: [
                { label: "用户昵称", prop: "user.nickName", width: "80" },
            ],


            tableData_yahoo: [],
            tableData_mercari: [],
            
            pageNumber: 1,
            pageSize: 30,
            total: 0,
            statusMap: {
                1: "出价中",
                2: "竞拍成功",
                3: "竞拍失败",
                4: "待入库",
                5: "已入库",
                6: "申请发货",
                7: "待付尾款",
                8: "待发货",
                9: "已发货",
                10: "竞拍被超越",
                100: "预约订单",
            },

            options: [
                {label: '全部', value: 0},
                {label: '出价中', value: 1},
                {label: '竞拍成功', value: 2},
                {label: '竞拍失败', value: 3},
                {label: '待入库', value: 4},
                {label: '已入库', value: 5},
                {label: '申请发货', value: 6},
                {label: '待付尾款', value: 7},
                {label: '待发货', value: 8},
                {label: '已发货', value: 9},
                {label: '竞拍被超越', value: 10},
                {label: '预约订单', value: 100},
            ],
            
            currentRow: {},
            dialogVisible: false,

            form: {
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

            reduceForm: {
                reducePrice: '',
                remarks: '',
            },
            allData: {},

            orderid: '',
            title: '',
            nickName: '',
            type: 0,
            timeLimit: [],
            tabPosition: 0,
            multipleSelection: [],
            exportLoading: false,


            currentPlant: 'yahoo',

        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.queryYahooOrderList()
    },
    methods: {
        searchOrder(){
            this.pageNumber = 1
            this.pageSize = 10
            if(this.currentPlant === 'yahoo'){
                this.queryYahooOrderList()
            }else if(this.currentPlant === 'mercari'){
                this.queryMercariOrderList()
            }
        },
        async confirmGot(){
            this.btnLoading = true
            const params = {
                dgPrice: '',
                jpyunfei: this.postFee,
                gjyunfei: '',
                cangcufei: '',
                baozhuangfei: '',
                otherPrice: '',
                otherPriceContent: '',
                id: this.currentRow.id,
                userid: this.currentRow.userid,
            }
            const res = await countYahooCost(params)
            const {code, data, msg} = res
            if(code === 0){
                this.handleClose()
                this.changeStatus_yahoo(this.currentRow.id, 5, this.currentRow.userid)
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        showPostFee(row){
            this.dialogVisible = true
            this.currentRow = row
        },
        deleteItem(row){
            this.$confirm('此操作将进行删除操作, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    id: row.id,
                }
                const res = await deleteYahooOrderList(params)
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryYahooOrderList()
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
        handleSelectionChange(val){
            this.multipleSelection = val;
        },
        toDetail(row, type){
            this.$router.push({
                path: '/orderList/orderDetail',
                query: {
                    id: row.id,
                    type,
                    userid: row.userid,
                    addressid: row.addressid,
                    option: 'countRest',
                    orderid: row.orderid,
                }
            })
        },
        changeCount(row){
            this.$router.push({
                path: '/orderList/orderDetail',
                query: {
                    id: row.id,
                    type: 1,
                    userid: row.userid,
                    addressid: row.addressid,
                    option: 'changeCount',
                    orderid: row.orderid,
                }
            })
        },
        sendPackage(row, type){
            this.$router.push({
                path: '/orderList/orderDetail',
                query: {
                    id: row.id,
                    type,
                    userid: row.userid,
                    addressid: row.addressid,
                    option: 'sendPackage',
                    orderid: row.orderid,
                }
            })
        },
        review(row){
            this.$router.push({
                path: '/orderList/orderDetail',
                query: {
                    id: row.id,
                    type: 1,
                    userid: row.userid,
                    addressid: row.addressid,
                    review: 1
                }
            })
        },

        tableRowClassName({row, rowIndex}){
            if(row.isMerge){
                return 'isMerge'
            }else{
                return ''
            }
        },
        objectSpanMethod({ row, column, rowIndex, columnIndex }) {
            if (columnIndex === 13) {
                if (row.isMerge && row.children && row.children.length > 0) {
                    return {
                        rowspan: row.children.length + 1,
                        colspan: 1
                    };
                } else {
                    return {
                        rowspan: 1,
                        colspan: 1
                    };
                }
            }else{
                return {
                    rowspan: 1,
                    colspan: 1
                };
            }
        },
        onTabClick(val){
            if(val === 8){
                this.multipleSelection = []
            }
            this.pageNumber = 1
            this.queryYahooOrderList(val)
        },
        onCurrentPlantClick(val){
            this.pageNumber = 1
            this.pageSize = 10
            if(val === 'yahoo'){
                console.log('222')
                this.queryYahooOrderList()
            }else if(val === 'mercari'){
                this.queryMercariOrderList()
            }
        },
        async queryMercariOrderList(){
            this.loading = true
            const params = {
                pageNumber: this.pageNumber,
                pageSize: this.pageSize,
                orderid: this.orderid,
                title: this.title,
                nickName: this.nickName,
                type: this.$route.query.mercariType * 1,
            }
            if(this.timeLimit.length > 0){
                params.begintime =  this.forDate(this.timeLimit[0])
                params.endtime =  this.forDate(this.timeLimit[1])
            }
            const res = await queryMercariOrderList(params)
            const {code, data, msg} = res
            if(code === 0){
                let index = 0
                this.tableData_mercari = data.list.map((item, index)=>{
                    let priceTotal = item.price * 10000
                    item.JPprice = parseInt(item.price / this.rate) + 1
                    const createtime = item.time?.substr(0, 10)
                    const time = this.forDate(Date.parse(new Date()))?.substr(0, 10)
                    if(time === createtime) item.isCurrentDay = true

                    if(item.children && item.children.length > 0){
                        item.isMerge = true
                        item.index = index++
                        item.children.forEach(it=>{
                            it.JPprice = parseInt(it.price / this.rate) + 1
                            it.isMerge = true
                            it.index = index++
                            priceTotal = priceTotal + it.price * 10000
                        })
                    }
                    return {
                        ...item,
                        typeText: this.statusMap[item.type],
                        priceTotal: priceTotal / 10000,

                    }
                })
                this.total = data.totalData
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        forDate(timestamp){
             const date = new Date(timestamp);
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
        reset(){
            this.orderid = ''
            this.title = ''
            this.nickName = ''
            this.type = 0
            this.timeLimit = []
        },
        async exportFile(){
            this.exportLoading = true
            const params = {
                list: this.multipleSelection
            }
            const res = await exportYahooOrder(params)
            fileDownload(res, `雅虎待发货列表导出.xlsx`);
            this.$message.success("下载成功");
            this.exportLoading = false
        },
        async onReduceFormSubmit(){
            return false
            this.loading = true
            const params = {
                price: this.reduceForm.reducePrice,
                userid: this.currentRow.userid,
                id: this.currentRow.id,
            }
            const res = await fixYahooPrice(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                const query = {
                    type: 3,
                    id: this.currentRow.id,
                    userid: this.currentRow.userid
                }
                const res2 = await updataYahooOrderStatus(query)
                const {code, data, msg} = res2
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryYahooOrderList()
                }else{
                    this.$message.error(msg)
                }
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleClose(){
            console.log('-------------')
            this.dialogVisible = false
            this.postFee = ''
        },
        changeStatus_yahoo(id, type, userid){
            this.$confirm('此操作将更改订单状态, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    type,
                    id,
                    userid
                }
                const res = await updataYahooOrderStatus(params)
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryYahooOrderList()
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

        changeStatus_yahoo2(id, type, userid){
            this.$confirm('此操作将更改订单状态, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    type,
                    orderid: id,
                    userid
                }
                const res = await updataYahooOrderStatus2(params)
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryYahooOrderList()
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

        
        changeStatus_mercari(id, type, userid){
            this.$confirm('此操作将更改订单状态, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    type,
                    id,
                    userid
                }
                const res = await updataMercariOrderStatus(params)
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryMercariOrderList()
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
        changeStatus_mercari2(id, type, userid){
            this.$confirm('此操作将更改订单状态, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                this.loading = true
                const params = {
                    type,
                    orderid: id,
                    userid
                }
                const res = await updataMercariOrderStatus2(params)
                const {code, data, msg} = res
                if(code === 0){
                    this.$message.success('操作成功')
                    this.queryMercariOrderList()
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

        showSendForm(row){
            
            this.sendForm.expressDeliveryCompany = ''
            this.sendForm.expressDeliveryNo = ''
            this.sendForm.remarks = ''
            
            this.currentRow = row
        },
        showReduceForm(row){
            this.reduceForm.reducePrice = ''
            this.reduceForm.remarks = ''
            
            this.currentRow = row
        },
        async queryYahooOrderList(){
            this.loading = true
            const params = {
                pageNumber: this.pageNumber,
                pageSize: this.pageSize,
                orderid: this.orderid,
                title: this.title,
                nickName: this.nickName,
                type: this.$route.query.yahooType * 1,
            }
            if(this.timeLimit.length > 0){
                params.begintime =  this.forDate(this.timeLimit[0])
                params.endtime =  this.forDate(this.timeLimit[1])
            }
            const res = await queryYahooOrderList(params)
            const {code, data, msg} = res
            if(code === 0){
                this.allData = data
                let index = 0

                this.tableData_yahoo = data.list?.map((item, index)=>{
                    let priceTotal = item.price * 10000
                    
                    const createtime = item.createtime?.substr(0, 10)
                    const time = this.forDate(Date.parse(new Date()))?.substr(0, 10)
                    if(time === createtime) item.isCurrentDay = true

                    if(item.children && item.children.length > 0){
                        item.isMerge = true
                        item.index = index++
                        item.children.forEach(it=>{
                            it.isMerge = true
                            it.index = index++
                            priceTotal = priceTotal + it.price * 10000
                        })
                    }

                    return {
                        ...item,
                        index: index + 1,
                        typeText: this.statusMap[item.type],
                        priceTotal: priceTotal / 10000
                    }
                })
                console.log(this.tableData_yahoo)
                this.total = data.totalData
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleSizeChange(val) {
            this.pageSize = val
            this.queryYahooOrderList()
        },
        handleCurrentChange(val) {
            this.pageNumber = val
            this.queryYahooOrderList()
        }
    }
}
</script>
  
<style lang="scss" scoped>
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

::v-deep .el-table__expand-icon{
    position: absolute;
    left: 10px;
}
::v-deep  .el-table__indent{
    display: none;
}
.search{
    .form{
        flex-wrap: wrap;
    }
    margin: 30px 0;
    .item{
        margin-right: 30px;
        margin-top: 10px;
        .title{
            width: 80px;
        }
    }
}
::v-deep .el-table__body .isMerge{
    background: #e9e9e9 !important;
}

::v-deep .el-table__expand-icon{
    display: none !important;
}
.isCurrentDay{
    color: red;
    font-weight: 600;
}
::v-deep tr{
    font-size: 12px;
}
.tabs2{
    margin-top: 30px;
}
::v-deep .tabs2 .el-radio-group{
    display: flex;
    width: 95%;
    overflow: auto;
    margin: auto;
}



</style>
  