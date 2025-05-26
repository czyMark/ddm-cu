<template>
    <div class="page" v-loading="loading">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="客服联系方式" name="0">
            </el-tab-pane>
            <el-tab-pane label="用户协议" name="1">
            </el-tab-pane>
            <el-tab-pane label="竞拍规则" name="2">
            </el-tab-pane>
            <el-tab-pane label="隐私政策" name="3">
            </el-tab-pane>
            <el-tab-pane label="出价规则" name="4">
            </el-tab-pane>
            <el-tab-pane label="风险提示" name="5">
            </el-tab-pane>
        </el-tabs>

        <ckeditor v-model="editorValue" class="ckeditor" :config="editorConfig"/>

        <div class="search center">
            <el-button type="primary" @click="submit">确定</el-button>
            <el-button  @click="querySetting">取消</el-button>
        </div>
    </div>
</template>
  
<script>
import {querySetting, updataSetting } from "@/http/api"
export default {
    data(){
        return {
            loading: false,
            activeName: '0',
            editorValue: '',
            editorConfig: {
                height: '400px',
            },

            serviceContact: '',
            userAgreement: '',
            bidRule: '',
            privacyPolicy: '',
            priceRule: '',
            riskWarning: '',
        }
    },
    created(){
        this.querySetting()
    },
    methods: {
        handleClick() {
            if(this.activeName === '0') this.editorValue = this.serviceContact
            if(this.activeName === '1') this.editorValue = this.userAgreement
            if(this.activeName === '2') this.editorValue = this.bidRule
            if(this.activeName === '3') this.editorValue = this.privacyPolicy
            if(this.activeName === '4') this.editorValue = this.priceRule
            if(this.activeName === '5') this.editorValue = this.riskWarning
        },
        async querySetting(){
            // this.loading = true
            const res = await querySetting()
            const {code, data, msg} = res
            if(code === 0){
                this.serviceContact = data.customer
                this.userAgreement = data.usergreement
                this.bidRule = data.biddingrules
                this.privacyPolicy = data.privacy
                this.priceRule = data.chujiarules
                this.riskWarning = data.riskwarning
                this.handleClick()
            }else{
                this.$message.error(msg)
            }
            // this.loading = false
        },
        async submit(){
            this.loading = true
            
            if(this.activeName === '0') this.serviceContact = this.editorValue
            if(this.activeName === '1') this.userAgreement = this.editorValue
            if(this.activeName === '2') this.bidRule = this.editorValue
            if(this.activeName === '3') this.privacyPolicy = this.editorValue
            if(this.activeName === '4') this.priceRule = this.editorValue
            if(this.activeName === '5') this.riskWarning = this.editorValue

            const params = {
                customer: this.serviceContact,
                usergreement: this.userAgreement,
                biddingrules: this.bidRule,
                privacy: this.privacyPolicy,
                chujiarules: this.priceRule,
                riskwarning: this.riskWarning,
            }
            const res = await updataSetting(params)
            const {code, data, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.querySetting()
                this.handleClick()
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
    }
}
</script>
  
<style lang="scss" scoped>
.search{
    margin: 30px 0;
}
</style>
  