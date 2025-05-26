<template>
    <div class="page">
        <el-form ref="form" :model="form" label-width="80px" v-loading="loading" style="margin-top: 20px">
            <el-form-item label="当前汇率">
                <el-input v-model="form.rate" :disabled="disabled" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="">
                <el-button type="primary" @click="disabled = false" v-if="disabled">修改</el-button>
                <el-button type="primary" @click="onSubmit" v-if="!disabled">确定</el-button>
                <el-button @click="()=>{$router.go(-1)}" v-if="deviceType === 'pc'">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
  
<script>
import {queryRate, updataRate} from "@/http/api"
import { mapState } from 'vuex'
  
export default {
    data(){
        return {
            disabled: true,
            loading: false,
            form: {
                rate: null
            }
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.getRate()
    },
    methods: {
        async getRate(){
            this.loading = true
            const res = await queryRate()
            const {code, data, msg} = res
            if(code === 0){
                this.form.rate = data
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        async onSubmit(){
            this.loading = true
            const params = {
                rate: this.form.rate
            }
            const res = await updataRate(params)
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
  
</style>
  