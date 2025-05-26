<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div class="flex">
                    <div class="searchItem center">
                        <div class="searchItemTilte">用户Id</div>
                        <div class="searchItemNav">
                            <el-input v-model="userid" clearable></el-input>
                        </div>
                    </div>
                    <div class="searchItem center">
                        <div class="searchItemTilte">提现状态</div>
                        <div class="searchItemNav">
                            <el-select v-model="type" placeholder="请选择">
                                <el-option
                                    v-for="item in typeList"
                                    :key="item.type"
                                    :label="item.label"
                                    :value="item.type">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                </div>
                <div class="flex">
                    <el-button type="primary" @click="queryDrawRecord">查询</el-button>
                </div>
            </div>
            <el-table :data="tableData" border style="width: 100%" v-loading="loading">
                <el-table-column label="序号" type="index" width="50"></el-table-column>

                <el-table-column label="用户头像" width="120"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <img v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" alt="" style="width: 80px; height: 80px; border-radius: 50%; margin: auto">
                        <img v-else src="@/assets/defaultAvatar.png" alt="" style="width: 80px; height: 80px; border-radius: 50%; margin: auto">
                    </template>
                </el-table-column>
                
                <el-table-column v-for="(item, index) in column" :key="index"  header-align="center"  align="center"
                    :prop="item.prop" :label="item.label" :width="item.width">
                </el-table-column>
                
                <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.type === 1" type="text" size="small" @click="updataDrawRecord(scope.row.id, scope.row.userid, 2)">提现成功</el-button>
                        <el-button v-if="scope.row.type === 1" type="text" size="small" @click="updataDrawRecord(scope.row.id, scope.row.userid, 3)">不予提现</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="page_m" v-else>
            <div class="search_m">
                <div class="search between">
                    <div class="flex">
                        <div class="searchItem item center">
                        <div class="title">用户Id</div>
                        <div class="value">
                            <el-input v-model="userid"></el-input>
                        </div>
                    </div>
                    <div class="searchItem item center">
                        <div class="title">提现状态</div>
                        <div class="value">
                            <el-select v-model="type" placeholder="请选择">
                                <el-option
                                    v-for="item in typeList"
                                    :key="item.type"
                                    :label="item.label"
                                    :value="item.type">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    </div>
                </div>
                <div class="flex seatchOptions">
                    <el-button type="primary" size="mini" @click="queryDrawRecord">查询</el-button>
                </div>
            </div>
            <tableList
                type="drawRequest"
                :list="tableData"
                :column="column"
                @updataDrawRecord="updataDrawRecord"
            />
        </div>
    </div>
</template>
  
<script>
import {queryDrawRecord, updataDrawRecord} from "@/http/api"
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
export default {
    components: {tableList},
    data(){
        return {
            column: [
                { label: "用户昵称", prop: "nickName", width: "200" },
                { label: "提现金额", prop: "balance", width: "120" },
                { label: "提现状态", prop: "typeText", width: "120" },
                { label: "真实姓名", prop: "realname", width: "120" },
                { label: "联系电话", prop: "phone", width: "" },
                { label: "第二联系电话", prop: "twophone", width: "" },
            ],
            tableData: [],
            
            typeMap: {
                1: "提现中",
                2: "提现成功",
                3: "提现失败",
            },
            typeList: [
                {type: 0, label: '全部'},
                {type: 1, label: '提现中'},
                {type: 2, label: '提现成功'},
                {type: 3, label: '提现失败'},
            ],
            type: 0,
            userid: '',

            pageNumber: 1,
            pageSize: 10,
            total: 0
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.queryDrawRecord()
    },
    methods: {
        async updataDrawRecord(id, userid, type){
            this.loading = true
            const params = {
                id, userid, type
            }
            const res = await updataDrawRecord(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.queryDrawRecord()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async queryDrawRecord(){
            this.loading = true
            const params = {
                pageNumber: this.pageNumber,
                pageSize: this.pageSize,
                userid: this.userid,
                type: this.type,
            }
            const res = await queryDrawRecord(params)
            const {code, data, msg} = res
            if(code === 0){
                this.tableData = data?.txRecords?.map((item, index)=>{
                    return {
                        ...item,
                        index: index + 1,
                        typeText: this.typeMap[item.type]
                    }
                })
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleClick(row){
            console.log('row', row)
        },
        toPath(path, id, type){
            this.$router.push({
                path,
                query:{id, type}
            })
        }
    }
}
</script>
  
<style lang="scss" scoped>
    .search{
        margin: 30px 0;
    }
    .searchItem{
        margin-right: 30px;
    }
    .searchItemTilte{
        margin-right: 15px;
    }
</style>
  