<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">

      <div class="title-container">
        <h1 class="title">代代米日淘后台管理系统</h1>
      </div>

      <el-form-item prop="username">
        <i class="el-icon-user-solid" style="color: #ccc;margin-left: 8px" />
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="账号"
          name="username"
          type="text"
        />
      </el-form-item>

      <el-form-item prop="password">
        <i class="el-icon-unlock" style="color: #ccc;margin-left: 8px" />
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
        />
        <i class="el-icon-view" style="color: #ccc" @click="showPwd" />
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
    </el-form>
    <div class="changePassword" @click="showChangePassword">修改密码</div>

    <el-dialog
      title="修改密码"
      :visible.sync="changePasswordFlag"
      width="600px"
      :before-close="handleClose">
      
      <el-form ref="passwordForm" :model="passwordForm" :rules="passwordFormRules" class="passwordForm" label-width="120px">
        <el-form-item label="账号" prop="account">
          <el-input v-model="passwordForm.account"></el-input>
        </el-form-item><el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="checkNewPassword">
          <el-input v-model="passwordForm.checkNewPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="submitChange">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <a href="https://beian.miit.gov.cn/" target="_blank" class="ba">京ICP备2025129620号-1</a>
  </div>
</template>

<script>
import {login, updatapassword} from "@/http/api"
import permissionMenu from '@/views/employee/permissionMenu.js'
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!value.length) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordFormRules: {
        account: [{ required: true, trigger: 'blur', message: '请输入账号' }],
        oldPassword: [{ required: true, trigger: 'blur', message: '请输入原密码' }],
        newPassword: [{ required: true, trigger: 'blur', message: '请输入新密码' }],
        checkNewPassword: [{ required: true, trigger: 'blur', message: '请确认新密码' }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      permissionMap: {},
      permissionLabel: {},

      changePasswordFlag: false,
      passwordForm: {
        account: '',
        oldPassword: '',
        newPassword: '',
        checkNewPassword: '',
      }
    }
  },
  created() {
    permissionMenu.forEach(item=>{
            this.permissionMap[item.id] = item.path
            this.permissionLabel[item.id] = item.label
        })
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  methods: {
    submitChange(){
      this.$refs.passwordForm.validate(async(valid) => {
        console.log("valid", valid)
        if (valid){
          if(this.passwordForm.newPassword !== this.passwordForm.checkNewPassword){
            return this.$message.error('两次新密码不相同')
          }
          const params = {
            username: this.passwordForm.account,
            oldpassword: this.passwordForm.oldPassword,
            newpassword: this.passwordForm.newPassword,
          }
          const res = await updatapassword(params)
          const {data, code, msg} = res
          if(code === 0){
            this.$message.success('密码修改成功')
            this.handleClose()
          }else{
            this.$message.error(msg)
          }

        }else{
          return false;
        }
      })
    },
    handleClose(){
      this.changePasswordFlag = false
      this.passwordForm = {
        account: '',
        oldPassword: '',
        newPassword: '',
        checkNewPassword: '',
      }
    },
    showChangePassword(){
      this.changePasswordFlag = true
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    async handleLogin() {
      this.$refs.loginForm.validate(async(valid) => {
        if (valid) {
          this.loading = true
          const params = {
            username: this.loginForm.username,
            password: this.loginForm.password,
          }
          const res = await login(params)
          const {code, data, msg} = res
          console.log('data', data)
          if(code === 0){
            this.$store.commit('clear_historyRecord')
            this.$store.commit('change_userInfo', data)
            
            const userMenu = data.authtype?.split(',') || []
            const menuID = userMenu?.[0] || 1
            const path = this.permissionMap[menuID]
            const label = this.permissionLabel[menuID]

            // 筛选权限路由
            const routes = []
            this.$router.options.routes?.forEach(item=>{
              if(userMenu.indexOf((item.id) + '') !== -1){
                routes.push(item)
              }
            })
            // this.$store.commit('change_menuList', routes)
            localStorage.setItem('token', data.token)
            

            console.log('permissionMenu', permissionMenu[1].children[0].path)
            // 跳转第一个菜单
            const record = {
              path: permissionMenu[1].children[0].path,
              meta: {
                title: label
              }
            }
            this.$store.commit('add_historyRecord', record)
            this.$router.push({
              path: permissionMenu[1].children[0].path
            })
          }else{
            this.$message.error(msg)
          }
          this.loading = false

        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
$bg:#283443;
$light_gray:#fff;
$cursor: #fff;
.login-container{
  position: relative;
  .ba{
    color: #fff;
    position: absolute;
    bottom: 50px;
    left: 0;
    right: 0;
    margin: auto;
    text-align: center;
  }
}

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container .login-form {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
.passwordForm input{
  color: #000 !important;
}

</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  height: 100vh;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
.changePassword{
  margin: 0 auto;
  box-sizing: border-box;
  padding: 0 35px;
  width: 520px;
  text-align: right;
  color: #a9a4a4;
  cursor: pointer;
}
</style>
