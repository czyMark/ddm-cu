<template>
  <div class="page">
      <header class="center">
          <i class="el-icon-back" @click="()=>{$router.go(-1)}"></i>
          <h4>{{statusText}}帮助</h4>
      </header>
      <nav v-loading="loading">
          <el-form ref="form" :model="form" label-width="80px">
              <el-form-item label="帮助标题">
                  <el-input v-model="form.title" :disabled="type === '3'"></el-input>
              </el-form-item>
              <el-form-item label="帮助内容">
                  <ckeditor ref="ckeditor" v-model="form.ckeditorData" class="ckeditor" :config="editorConfig" :disabled="type === '3'"/>
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
import {getQuestionById, updataQuestion, addQuestion} from "@/http/api"

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
              '1': "发布",
              '2': "修改",
              '3': "",
          },
          form: {
              title: '',
              ckeditorData: ''
          },
          editorConfig: {
              height: '400px',
              colorButton_enableMore: true,
              colorButton_colors: 'CF5D4E,454545,FFF,DDD,CCEAEE,66AB16'
          },
      }
  },
  created(){
      this.id = this.$route.query.id
      this.type = this.$route.query.type
      this.statusText = this.statuses[this.type]
      this.submitBtnText = this.submitBtns[this.type]
      if(this.type !== '1') this.getQuestionById()
  },
  methods: {
      async onSubmit(){
          this.loading = true
          let res = null
          
          const params = {
              title: this.form.title,
              content: this.form.ckeditorData,
          }
          
          if(this.$route.query.type === "1"){  // 新增
              res = await addQuestion(params)
          }else{
              params.id = this.id*1
              res = await updataQuestion(params)
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
      async getQuestionById(){
          this.loading = true
          const params = {
              id: this.id * 1
          }
          const res = await getQuestionById(params)
          const {code, data, msg} = res
          if(code === 0){
              this.form.title = data.title
              this.form.ckeditorData = data.content
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
