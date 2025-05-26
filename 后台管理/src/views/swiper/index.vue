<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <div class="search between">
                <div></div>
                <el-button type="primary" @click="toPath('/Swiper/detail', null, '1')">新增</el-button>
            </div>
            <el-table :data="tableData" border style="width: 100%" v-loading="loading">
                <el-table-column v-for="(item, index) in column" :key="index"  header-align="center" :align="item.prop === 'index' ? 'center' : ''"
                    :prop="item.prop" :label="item.label" :width="item.width">
                    <template slot-scope="scope" >
                        <img :src="scope.row.url" alt="" style="width: 375px; height: 175px; margin: auto" v-if="item.prop === 'image'">
                        <p v-else>{{ scope.row[item.prop] }}</p>
                    </template>
                </el-table-column>
                
                <el-table-column fixed="right" label="操作" width="200"  header-align="center" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="toPath('/Swiper/detail', scope.row.id, '3')">查看</el-button>
                        <el-button type="text" size="small" @click="toPath('/Swiper/detail', scope.row.id, '2')">编辑</el-button>
                        <el-button type="text" size="small" @click="deleteItem(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="page_m" v-else>
            <tableList
                type="swiper"
                :list="tableData"
                @viewItem="viewItem"
                @editItem="editItem"
                @deleteItem="deleteItem"
            />
            <addBtn @add="toPath('/Swiper/detail', null, '1')"/>
        </div>
    </div>
</template>
  
<script>
import {getSwiperList, deleteSwiper} from "@/http/api"
import { mapState } from 'vuex'
import tableList from "@/components/tableList.vue"
import addBtn from "@/components/addBtn.vue"
export default {
    components: {tableList, addBtn},
    data(){
        return {
            column: [
                { label: "序号", prop: "index", width: "100" },
                { label: "轮播图图片", prop: "image", width: "" },
                { label: "时间", prop: "time", width: "300" },
            ],
            tableData: []
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.getSwiperList()
    },
    methods: {
        viewItem(val){
            this.toPath('/Swiper/detail', val.id, '3', val)
        },
        editItem(val){
            this.toPath('/Swiper/detail', val.id, '2', val)
        },
        async deleteItem(item){
            this.loading = true
            const res = await deleteSwiper({id: item.id})
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success("操作成功")
                this.getSwiperList()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async getSwiperList(){
            this.loading = true
            const res = await getSwiperList()
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
  