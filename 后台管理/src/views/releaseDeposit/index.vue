<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div class="flex">
                    <div class="item center">
                        <div class="title">用户Id</div>
                        <div class="value">
                            <el-input v-model="userId" clearable></el-input>
                        </div>
                    </div>
                    
                    <div class="item center">
                        <div class="title">申请状态</div>
                        <div class="value">
                            <el-select v-model="checkType" placeholder="请选择" clearable>
                                <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                </div>
                <div class="flex">
                    <el-button type="primary" @click="reset">重置</el-button>
                    <el-button type="primary" @click="queryReleaseDepositList">查询</el-button>
                </div>
            </div>
            <el-table :data="tableData" border style="width: 100%" v-loading="loading">
                <el-table-column label="序号" type="index" width="50"></el-table-column>

                <el-table-column label="用户头像" width="100"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <img v-if="scope.row.user?.avatarUrl" :src="scope.row.user.avatarUrl" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                        <img v-else src="@/assets/defaultAvatar.png" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                    </template>
                </el-table-column>
                
                <el-table-column v-for="(item, index) in column" :key="index"  header-align="center"  align="center"
                    :prop="item.prop" :label="item.label" :width="item.width">
                </el-table-column>
                
                <el-table-column fixed="right" label="操作" width="120"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="updataYjBalance(scope.row, 2)" v-if="scope.row.type === 1">通过</el-button>
                        <el-button type="text" size="small" style="color: red" @click="updataYjBalance(scope.row, 0)" v-if="scope.row.type === 1">驳回</el-button>
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
        </div>
    </div>
</template>
  
<script>

import {queryReleaseDepositList, updataYjBalance} from "@/http/api"
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
export default {
    components: {tableList},
    data(){
        return {
            loading: false,
            column: [
                { label: "充值金额", prop: "balance", width: "120" },
                { label: "状态", prop: "typeText", width: "80" },
                { label: "订单号", prop: "transactionid", width: "" },
                { label: "用户ID", prop: "userid", width: "80" },
                { label: "用户昵称", prop: "user.nickName", width: "200" },
                { label: "真实姓名", prop: "user.realname", width: "120" },
                { label: "联系电话", prop: "user.phone", width: "" },
                { label: "充值时间", prop: "date", width: "200" },

            ],
            tableData: [],
            
            pageNumber: 1,
            pageSize: 30,
            total: 0,

            userId: '',
            checkType: '',

            currentRow: {},
            typeMap:{
                0: '已充值',
                1: '申请中',
                2: '已退款',
            },
            options: [
                {value: 0, label: '已充值'},
                {value: 1, label: '申请中'},
                {value: 2, label: '已退款'},
            ],

        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.queryReleaseDepositList()
    },
    methods: {
        async updataYjBalance(row, type){
            this.loading = true
            const params = {
                type,
                id: row.id,
                balance: row.balance,
                userid: row.userid,
            }
            console.log('row', row)
            const res = await updataYjBalance(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.queryReleaseDepositList()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleSizeChange(val) {
            this.pageSize = val
            this.queryReleaseDepositList()
        },
        handleCurrentChange(val) {
            this.pageNumber = val
            this.queryReleaseDepositList()
        },
        
        async queryReleaseDepositList(){
            this.loading = true
            const params = {
                pageNumber: this.pageNumber,
                pageSize: this.pageSize,
                userid: this.userId,
                type: this.checkType
            }
            const res = await queryReleaseDepositList(params)
            const {code, data, msg} = res
            if(code === 0){
                this.total = data.totalData
                this.tableData = data.list.map((item, index)=>{
                    return {
                        ...item,
                        typeText: this.typeMap[item.type],
                        index: index + 1
                    }
                })
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },

        reset(){
            this.userId = ''
            this.checkType = ''
        }
    }
}
</script>
  
<style lang="scss" scoped>
    .search{
        margin: 30px 0;
        .item{
            margin-right: 30px;
            .value{
                margin-left: 20px;
            }
        }
    }
</style>
  