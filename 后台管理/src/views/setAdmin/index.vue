<template>
    <div class="page">
        <el-form ref="form" :model="form" label-width="80px" v-loading="loading" style="margin-top:20px">
            <el-form-item label="用户id">
                <el-input v-model="form.userid" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="">
                <el-button type="primary" @click="onSubmit(1)">设为管理员</el-button>
                <el-button type="primary" @click="onSubmit(0)">取消管理员</el-button>
                <!-- <el-button @click="()=>{$router.go(-1)}">取消</el-button> -->
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import {updataAdmin} from "@/http/api"
  
export default {
    data(){
        return {
            disabled: true,
            loading: false,
            form: {
                userid: ''
            }
        }
    },
    methods: {
        async onSubmit(val){
            this.loading = true
            const params = {
                userid: this.form.userid,
                isAdmin: val
            }
            const res = await updataAdmin(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        }
    }
}
</script>
  
<style lang="scss" scoped>
  
</style>
  