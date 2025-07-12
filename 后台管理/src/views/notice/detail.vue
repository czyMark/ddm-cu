<template>
    <div class="page">
        <header class="center">
            <i class="el-icon-back" @click="()=>{$router.go(-1)}"></i>
            <h4>{{statusText}}公告</h4>
        </header>
        <nav v-loading="loading">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="公告标题">
                    <el-input v-model="form.title" :disabled="type === '3'"></el-input>
                </el-form-item>
                <el-form-item label="公告内容">
                    <!-- <ckeditor ref="ckeditor" v-model="form.ckeditorData" class="ckeditor" :config="editorConfig" :disabled="type === '3'"/> -->
                    <div class="ckeditor">
                        <quill-editor
                            class="ql-editor"
                            v-model="form.ckeditorData"
                            ref="myQuillEditor"
                            :options="editorOption"
                            @blur="onEditorBlur($event)"
                            @focus="onEditorFocus($event)"
                            @change="onEditorChange($event)"
                            :disabled="type === '3'"
                        >
                        </quill-editor>
                    </div>

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
import {getNoticetById, updataNotice, addNotice} from "@/http/api"
import { quillEditor, Quill } from 'vue-quill-editor'

export default {
    components: {
        quillEditor
    },
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
                height: '400px'
            },

            content:'',
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
        this.id = this.$route.query.id
        this.type = this.$route.query.type
        this.statusText = this.statuses[this.type]
        this.submitBtnText = this.submitBtns[this.type]
        if(this.type !== '1') this.getNoticetById()
    },
    mounted(){
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
        async onSubmit(){
            this.loading = true
            let res = null
            
            const params = {
                title: this.form.title,
                content: this.form.ckeditorData,
            }
            
            if(this.$route.query.type === "1"){  // 新增
                res = await addNotice(params)
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
        async getNoticetById(){
            this.loading = true
            const params = {
                id: this.id*1
            }
            const res = await getNoticetById(params)
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
    .editor{
        width: 100%;
        :deep(.ql-editor) {
            min-height: 400px;
        }
    }

    ::v-deep .ql-container{
        min-height: 400px;
    }
</style>
  