<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div class="flex">
                    <!-- <div class="item center">
                        <div class="title">用户id</div>
                        <div class="value">
                            <el-input v-model="userid" clearable></el-input>
                        </div>
                    </div> -->
                    <div class="item center">
                        <div class="title">用户昵称</div>
                        <div class="value">
                            <el-input v-model="nickName" clearable></el-input>
                        </div>
                    </div>
                    <div class="item center">
                        <div class="title">联系电话</div>
                        <div class="value">
                            <el-input v-model="tel" clearable></el-input>
                        </div>
                    </div>
                </div>
                <div class="flex">
                    <el-button type="primary" @click="reset">重置</el-button>
                    <el-button type="primary" @click="queryUserList">查询</el-button>
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
                        <el-button type="text" size="small" @click="showContactUser(scope.row)">联系用户</el-button>
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

        <div class="page_m" v-else>
            <div class="search_m">
                <div class="search between">
                    <div class="flex">
                        <!-- <div class="item center">
                            <div class="title">用户id</div>
                            <div class="value">
                                <el-input v-model="userid" clearable></el-input>
                            </div>
                        </div> -->
                        <div class="item center">
                            <div class="title">用户昵称</div>
                            <div class="value">
                                <el-input v-model="nickName" clearable></el-input>
                            </div>
                        </div>
                        <div class="item center">
                            <div class="title">联系电话</div>
                            <div class="value">
                                <el-input v-model="tel" clearable></el-input>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="flex seatchOptions">
                    <el-button type="primary" size="mini" @click="reset">重置</el-button>
                    <el-button type="primary" size="mini" @click="queryUserList">查询</el-button>
                </div>
            </div>
            <tableList
                type="userList"
                :list="tableData"
                :column="column"
                @showContactUser="showContactUser"
                :showPagination="true"
                @handleSizeChange="handleSizeChange"
                @handleCurrentChange="handleCurrentChange"
                :pageNumber="pageNumber"
                :pageSize="pageSize"
                :total="total"
            />
        </div>

        <el-dialog
            :title="'联系' + currentRow.nickName"
            :visible.sync="dialogVisible"
            :close-on-click-modal="false"
            :width="deviceType === 'pc' ? '500px' : '80%'"
            :before-close="handleClose">
            <div>
                <div style="margin-bottom: 30px">请输入联系内容</div>
                <div>
                    <el-input v-model="contact" type="textarea" :rows="4"></el-input>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="contactUser">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
  
<script>
import {queryUserList, contactUser} from "@/http/api"
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
export default {
    components: {tableList},
    data(){
        return {
            column: [
                { label: "ID", prop: "userid", width: "80" },
                { label: "用户昵称", prop: "nickName", width: "200" },
                { label: "真实姓名", prop: "realname", width: "120" },
                { label: "联系电话", prop: "phone", width: "" },
                { label: "联系状态", prop: "isReadText", width: "" },
                { label: "第二联系电话", prop: "twophone", width: "" },
            ],
            tableData: [],
            
            pageNumber: 1,
            pageSize: 30,
            total: 0,

            nickName: '',
            tel: '',
            userid: '',

            contact: '',
            currentRow: {},
            dialogVisible: false,
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.queryUserList()
    },
    methods: {
        handleSizeChange(val) {
            console.log('val', val)
            this.pageSize = val
            this.queryUserList()
        },
        handleCurrentChange(val) {
            this.pageNumber = val
            this.queryUserList()
        },
        handleClose(){
            this.dialogVisible = false
        },
        showContactUser(row){
            this.currentRow = row
            this.dialogVisible = true
            this.contact = row.contact
        },
        async contactUser(){
            this.loading = true
            const params = {
                userid: this.currentRow.userid,
                contact: this.contact,
            }
            const res = await contactUser(params)
            const {code, data, msg} = res
            if(code === 0){
                this.dialogVisible = false
                this.queryUserList()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        
        async queryUserList(){
            this.loading = true
            const params = {
                pageNumber: this.pageNumber,
                pageSize: this.pageSize,
                phone: this.tel,
                nickName: this.nickName,
                userid: this.userid,
            }
            const res = await queryUserList(params)
            const {code, data, msg} = res
            if(code === 0){
                this.total = data.total
                this.tableData = data.list.map((item, index)=>{
                    return {
                        ...item,
                        nickName: item.nickName ? item.nickName : '微信用户',
                        isReadText: item.isRead === 0 ? '未读' : '已读',
                        index: index + 1
                    }
                })
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },

        reset(){
            this.nickName = ''
            this.tel = ''
            this.userid = ''
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
  