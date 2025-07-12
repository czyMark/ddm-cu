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

        <!-- <ckeditor v-model="editorValue" class="ckeditor" :config="editorConfig"/> -->

        <div class="ckeditor">
            <quill-editor
                class="ql-editor"
                v-model="editorValue"
                ref="myQuillEditor"
                :options="editorOption"
                @blur="onEditorBlur($event)"
                @focus="onEditorFocus($event)"
                @change="onEditorChange($event)"
                :disabled="type === '3'"
            >
            </quill-editor>
        </div>



        <div class="search center">
            <el-button type="primary" @click="submit">确定</el-button>
            <el-button  @click="querySetting">取消</el-button>
        </div>
    </div>
</template>
  
<script>
import {querySetting, updataSetting } from "@/http/api"
import { quillEditor, Quill } from 'vue-quill-editor'

export default {
    components: {
        quillEditor
    },
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
            
            // 富文本功能配置
            editorOption:{
                modules:{
                    // 如果想增加事件控制，比如图片的事件，参考我上面vue3的配置，是一样的用法
                    toolbar:[
                        ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
                        // ['blockquote', 'code-block'],     //引用，代码块
                        // [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
                        [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
                        // [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
                        // [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
                        // [{ 'direction': 'rtl' }],             // 文本方向
                        [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
                        // [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题
                        [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
                        [{ 'font': [] }],     //字体
                        [{ 'align': [] }],    //对齐方式
                        // ['clean'],    //清除字体样式
                        ['image','video']    //上传图片、上传视频
                    ]
                },
            },
        }
    },
    created(){
        this.querySetting()
    },
    methods: {
        // 失去焦点事件
        onEditorBlur(e){
            console.log(e, '失去焦点事件');
        },
        // 获得焦点事件
        onEditorFocus(e){
            console.log(e, '获得焦点事件');
        },
        // 内容改变事件
        // html是带标签，text是纯文本
        onEditorChange({ quill, html, text }){
            this.form.ckeditorData = html
        },
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
    ::v-deep .ql-container{
        min-height: 400px;
    }
</style>
  