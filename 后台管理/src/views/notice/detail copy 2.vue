<template>
  <div class="editor">
    <quill-editor
      class="ql-editor"
      v-model="content"
      ref="myQuillEditor"
      :options="editorOption"
      @blur="onEditorBlur($event)"
      @focus="onEditorFocus($event)"
      @change="onEditorChange($event)">
    </quill-editor>
  </div>
</template>
 
<script>
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import { quillEditor, Quill } from 'vue-quill-editor'
 
 
export default {
  name: "customEditor",
  components: {
    quillEditor
  },
  data(){
    return{
      content:'',
      // 富文本功能配置
      editorOption:{
        modules:{
          // 如果想增加事件控制，比如图片的事件，参考我上面vue3的配置，是一样的用法
          toolbar:[
            ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
            ['blockquote', 'code-block'],     //引用，代码块
            [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
            [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
            [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
            [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
            [{ 'direction': 'rtl' }],             // 文本方向
            [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
            [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题
            [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
            [{ 'font': [] }],     //字体
            [{ 'align': [] }],    //对齐方式
            ['clean'],    //清除字体样式
            ['image','video']    //上传图片、上传视频
          ]
        },
       
      },
    }
  },
  methods:{
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
      this.content = html
    },
 
  },
  mounted() {
    this.content = '<h1>我是个默认测试数据</h1>'
  }
}
</script>
 
<style scoped lang="scss">
.editor{
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
 
</style>