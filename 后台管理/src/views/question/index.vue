<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div></div>
                <el-button type="primary" @click="toPath('/question/detail', null, '1')">新增</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" v-loading="loading">
                <el-table-column v-for="(item, index) in column" :key="index"  header-align="center" :align="item.prop === 'index' ? 'center' : ''"
                    :prop="item.prop" :label="item.label" :width="item.width">
                </el-table-column>
                
                <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="toPath('/question/detail', scope.row.id, '3')">查看</el-button>
                        <el-button type="text" size="small" @click="toPath('/question/detail', scope.row.id, '2')">编辑</el-button>
                        <el-button type="text" size="small" @click="deleteItem(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="page_m" v-else>
            <tableList
                type="question"
                :list="tableData"
                @viewItem="viewItem"
                @editItem="editItem"
                @deleteItem="deleteItem"
            />
            <addBtn @add="toPath('/question/detail', null, '1')"/>
        </div>
    </div>
</template>
  
<script>
import {getQuestionList, deleteQuestion} from "@/http/api"
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
import addBtn from "@/components/addBtn.vue"
export default {
    components: {tableList, addBtn},
    data(){
        return {
            column: [
                { label: "序号", prop: "index", width: "100" },
                { label: "问题标题", prop: "title", width: "" },
                { label: "时间", prop: "time", width: "" },
            ],
            tableData: []
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.getQuestionList()
    },
    methods: {
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
                this.getQuestionList()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async getQuestionList(){
            this.loading = true
            const res = await getQuestionList()
            const {code, data, msg} = res
            if(code === 0){
                this.tableData = data.map((item, index)=>{
                    return {
                        ...item,
                        index: index + 1
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
</style>
  