<template>
    <div class="page">
        <div class="search between">
            <div class="item center">
                <div class="title">经销商id</div>
                <div class="value" style="margin-left: 10px">
                    <el-input v-model="subId" clearable></el-input>
                </div>
            </div>
            <el-button type="primary" @click="onSearch">查询</el-button>
        </div>

        
        <div class="search between">
            <div class="item center">
                <div class="title">下级总消费金额</div>
                <div class="value" style="margin-left: 10px">
                    <el-input v-model="totla_xf" clearable disabled></el-input>
                </div>
            </div>
            <div class="item center">
                <div class="title">总提成金额</div>
                <div class="value" style="margin-left: 10px">
                    <el-input v-model="totla_tc" clearable disabled></el-input>
                </div>
            </div>
            <div class="item center">
                <div class="title">已提现金额</div>
                <div class="value" style="margin-left: 10px">
                    <el-input v-model="totla_ytx" clearable disabled></el-input>
                </div>
            </div>
            
            <el-button type="primary"
                @click="showDialog"
            >
                <!-- :disabled="totla_tc <= totla_ytx" -->

                提现
            </el-button>
        </div>


        <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column label="序号" type="index" width="50"></el-table-column>

                <el-table-column label="用户头像" width="120"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <img v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                        <img v-else src="@/assets/defaultAvatar.png" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                    </template>
                </el-table-column>
                
                <el-table-column v-for="(item, index) in column" :key="index"  header-align="center"  align="center"
                    :prop="item.prop" :label="item.label" :width="item.width">
                </el-table-column>
                
                <!-- <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="showContactUser(scope.row)">联系用户</el-button>
                    </template>
                </el-table-column> -->
        </el-table>


        
        <el-dialog
            title="请输入提现金额"
            :visible.sync="dialogVisible"
            :close-on-click-modal="false"
            width="300px"
            :before-close="handleClose">
            <div>
                <el-input v-model="drawMoney" type="number"></el-input>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="draw">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
  
<script>
import {updatatxprice, selectMyxiaji} from "@/http/api"

export default {
    data(){
        return {
            column: [
                { label: "ID", prop: "userid", width: "80" },
                { label: "用户昵称", prop: "nickName", width: "200" },
                { label: "总消费金额", prop: "myxfprice", width: "120" },
                { label: "真实姓名", prop: "realname", width: "120" },
                { label: "联系电话", prop: "phone", width: "" },
                { label: "第二联系电话", prop: "twophone", width: "" },
            ],
            tableData: [],
            subId: '',
            loading: false,


            totla_xf: '',
            totla_tc: '',
            totla_ytx: '',

            drawMoney: '',

            dialogVisible: false,

            
        }
    },
    created(){
        // this.onSearch()
    },
    methods: {
        showDialog(){
            this.dialogVisible = true
        },
        handleClose(){
            this.drawMoney = ''
            this.dialogVisible = false
        },

        viewItem(val){
            this.toPath('/question/detail', val.id, '3', val)
        },
        editItem(val){
            this.toPath('/question/detail', val.id, '2', val)
        },
        async deleteItem(id){
            this.loading = true
            const res = await deleteQuestion({id})
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success("操作成功")
                this.onSearch()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async onSearch(){
            if(!this.subId) return this.$message.error('请输入经销商ID')
            this.loading = true
            const res = await selectMyxiaji({userid: this.subId})
            const {code, data, msg} = res
            if(code === 0){
                this.tableData = data.list?.map((item, index)=>{
                    return {
                        ...item,
                        index: index + 1
                    }
                })

                this.totla_xf = data.totalxjprice
                this.totla_tc = data.totalxjprice / 10
                this.totla_ytx = data.yjtxprice 
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async draw(){
            this.loading = true
            const res = await updatatxprice({userid: this.subId, yjtxprice: this.drawMoney})
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success("操作成功")
                this.handleClose()
                this.onSearch()
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
</style>
  