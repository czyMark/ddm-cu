<template>
    <div class="page">
        <div v-if="deviceType === 'pc'">
            <el-form ref="form" :model="form" label-width="120px" v-loading="loading">
                <el-form-item label="当前cookie">
                    <el-input v-model="form.cookie" type="textarea" :disabled="disabled" style="width: 500px" :rows="4"></el-input>
                </el-form-item>
                <el-form-item label="">
                    <el-button type="primary" @click="disabled = false" v-if="disabled">修改</el-button>
                    <el-button type="primary" @click="onSubmit" v-if="!disabled">确定</el-button>
                    <el-button @click="()=>{$router.go(-1)}">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div v-else class="page_m column">
            <div>
                <p class="label">当前cookie:</p>
                <el-input v-model="form.cookie" type="textarea" :disabled="disabled" style="width: 95vw" :rows="10"></el-input>
            </div>
            <div class="opt">
                <el-button type="primary" @click="disabled = false" v-if="disabled">修改</el-button>
                <el-button type="primary" @click="onSubmit" v-if="!disabled">确定</el-button>
            </div>
        </div>
    </div>
</template>
  
<script>
import {queryCookie, updataCookie} from "@/http/api"
import { mapState } from 'vuex'
  
export default {
    data(){
        return {
            disabled: true,
            loading: false,
            form: {
                cookie: null
            }
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.getCookie()
    },
    methods: {
        async getCookie(){
            this.loading = true
            const res = await queryCookie()
            const {code, data, msg} = res
            if(code === 0){
                this.form.cookie = data.cookie
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async onSubmit(){
            this.loading = true
            const params = {
                cookie: this.form.cookie
            }
            const res = await updataCookie(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.disabled = true
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        }
    }
}
</script>
  
<style lang="scss" scoped>
.page_m{
    background: #fff;
}
.label{
    margin-bottom: 15px;
}
.opt{
    margin-top: 15px;
}
</style>
  