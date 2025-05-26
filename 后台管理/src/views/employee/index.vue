<template>
    <div class="page">
        <div class="search between" v-if="deviceType === 'pc'">
            <el-button type="primary" @click="toPath('/employee/detail', null, '1')">新增</el-button>
        </div>
        <el-table :data="tableData" border style="width: 100%" v-loading="loading" v-if="deviceType === 'pc'">
            <el-table-column v-for="(item, index) in column" :key="index"  header-align="center" align="center"
                :prop="item.prop" :label="item.label" :width="item.width">
            </el-table-column>
            
            <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="toPath('/employee/detail', scope.row.id, '3', scope.row)">查看</el-button>
                    <el-button type="text" size="small" @click="toPath('/employee/detail', scope.row.id, '2', scope.row)">编辑</el-button>
                    <el-button type="text" size="small" @click="deleteItem(scope.row.username)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <addBtn @add="toPath('/employee/detail', null, '1')" v-if="deviceType !== 'pc'"/>
        <div class="tableList" v-if="deviceType !== 'pc'">
            <tableList
                type="employee"
                :list="tableData"
                :column="column"
                @viewItem="viewItem"
                @editItem="editItem"
                @deleteItem="deleteItem2"
            />
        </div>
    </div>
</template>
  
<script>
import {employeeQuery, employeeDelete} from "@/http/api"
import permissionMenu from './permissionMenu.js'
import { mapState } from 'vuex'
import addBtn from "@/components/addBtn.vue"
import tableList from "@/components/tableList.vue"
export default {
    components: {addBtn, tableList},
    data(){
        return {
            column: [
                { label: "序号", prop: "index", width: "100" },
                { label: "员工账号", prop: "username", width: "200" },
                { label: "员工权限", prop: "permissionText", width: "" },
            ],
            tableData: [],
            permissionMap: {},
        }
    },
    created(){
        permissionMenu.forEach(item=>{
            this.permissionMap[item.id] = item.label
        })
        this.employeeQuery()
    },
    computed: {
        ...mapState(['deviceType'])
    },
    methods: {
        async deleteItem(username){
            this.loading = true
            const res = await employeeDelete({username})
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success("操作成功")
                this.employeeQuery()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async employeeQuery(){
            this.loading = true
            const res = await employeeQuery()
            const {code, data, msg} = res
            if(code === 0){
                this.tableData = data.map((item, index)=>{
                    const permissionArr = item.authtype?.split(',')
                    const arr = []
                    permissionArr?.forEach(item=>{
                        arr.push(this.permissionMap[item])
                    })

                    return {
                        ...item,
                        index: index + 1,
                        permissionText: arr.join('，')
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
        toPath(path, id, type, row){
            if(row){
                sessionStorage.setItem('employeeDetail', JSON.stringify(row))
            }
            this.$router.push({
                path,
                query:{id, type}
            })
        },
        viewItem(val){
            this.toPath('/employee/detail', val.id, '3', val)
        },
        editItem(val){
            this.toPath('/employee/detail', val.id, '2', val)
        },
        deleteItem2(val){
            this.deleteItem(val.username)
        },
    }
}
</script>
  
<style lang="scss" scoped>
    .search{
        margin: 30px 0;
    }
</style>
  