<template>
    <div class="page">
        <header class="center" v-if="deviceType === 'pc'">
            <i class="el-icon-back" @click="()=>{$router.go(-1)}"></i>
            <h4>{{statusText}}员工</h4>
        </header>
        <nav v-loading="loading">
            <el-form ref="form" :model="form" label-width="80px">
                <!-- <el-form-item label="员工昵称">
                    <el-input v-model="form.title" :disabled="type === '3'" style="width: 500px"></el-input>
                </el-form-item> -->
                <el-form-item label="员工账号">
                    <el-input v-model="form.account" :disabled="type === '3'"
                        :style="{width: deviceType === 'pc' ? '500px' : '95%'}">
                    </el-input>
                </el-form-item>
                <el-form-item label="员工密码">
                    <el-input v-model="form.password" :disabled="type === '3'"
                        :style="{width: deviceType === 'pc' ? '500px' : '95%'}">
                    </el-input>
                </el-form-item>
                <el-form-item label="员工权限">
                    <el-select
                        :disabled="type === '3'"
                        v-model="form.permission"
                        multiple placeholder="请选择"
                        :style="{width: deviceType === 'pc' ? '500px' : '95%'}"
                    >
                        <el-option
                        v-for="item in permissionMenu"
                        :key="item.id"
                        :label="item.label"
                        :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit" v-if="submitBtnText">{{submitBtnText}}</el-button>
                    <el-button @click="()=>{$router.go(-1)}">取消</el-button>
                </el-form-item>
            </el-form>
        </nav>
    </div>
</template>
  
<script>
import {getNoticetById, updataNotice, employeeAdd} from "@/http/api"
import permissionMenu from './permissionMenu.js'
import { mapState } from 'vuex'
export default {
    data(){
        return {
            id: '',
            type: '',
            loading: false,
            statusText: '',
            statuses: {
                '1': "新建",
                '2': "编辑",
                '3': "查看",
            },
            submitBtnText: '',
            submitBtns: {
                '1': "新建",
                '2': "修改",
                '3': "",
            },
            form: {
                title: '',
                account: '',
                password: '',
                permission: [],
            },
            permissionMenu,
        }
    },
    computed: {
        ...mapState(['deviceType'])
    },
    created(){
        this.id = this.$route.query.id
        this.type = this.$route.query.type
        this.statusText = this.statuses[this.type]
        this.submitBtnText = this.submitBtns[this.type]
        if(this.type !== '1') {
            const detail = JSON.parse(sessionStorage.getItem("employeeDetail"))
            console.log(detail)
            this.form.account = detail.username
            this.form.password = detail.password
            const permission = detail.authtype?.split(',')
            for(let i=0; i<permission.length; i++){
                permission[i] = parseInt(permission[i])
            }
            this.form.permission = permission
        }
    },
    methods: {
        handleCheckChange(data, checked, indeterminate) {
            console.log(data, checked, indeterminate);
        },
        async onSubmit(){
            this.loading = true
            let res = null
            
            const params = {
                username: this.form.account,
                password: this.form.password,
                authtype: this.form.permission.join(','),
            }
            
            if(this.$route.query.type === "1"){  // 新增
                res = await employeeAdd(params)
            }else{
                params.id = this.id*1
                res = await updataNotice(params)
            }
            const {code, msg} = res
            if(code === 0){
                this.$message.success('操作成功')
                this.$router.go(-1)
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
    }
}
</script>
  
<style lang="scss" scoped>
    header{
        justify-content: flex-start;
        i{
            margin-right: 20px;
            cursor: pointer;
        }
    }
    nav{
        margin-top: 50px;
        max-width: 100%;
    }
</style>
  