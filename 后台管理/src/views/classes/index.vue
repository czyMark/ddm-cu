<template>
    <div class="page">
        <div class="nav flex" v-loading="loading">
            <div class="tree">
                <el-tree
                    :data="treeData"
                    :props="defaultProps"
                    @node-click="handleNodeClick"
                    :expand-on-click-node="false"
                >
                </el-tree>
            </div>
            <div class="setImg" v-if="showForm">
                <el-form ref="form" :model="form" label-width="150px" style="margin-top: 20px">
                    <el-form-item label="分类名" label-width="150px">
                        <el-input v-model="form.className" style="width: 300px" @input="onInput"></el-input>
                    </el-form-item>
                    <el-form-item label="分类图片" label-width="150px" v-if="currentLevel < 3">
                        <el-upload
                            class="avatar-uploader"
                            action="/upload"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload"
                            name="image"
                        >
                            <img v-if="form.img" :src="form.img" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="显示/隐藏">
                        <el-switch
                            v-model="currentNode.isShow"
                            active-text="显示"
                            inactive-text="隐藏"
                        >
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="是否显示在首页" v-if="currentLevel === 1">
                        <el-switch
                            v-model="currentNode.showInHome"
                            active-text="显示"
                            inactive-text="隐藏"
                        >
                        </el-switch>
                    </el-form-item>

                    
                    <el-form-item label="" label-width="200px">
                        <el-button type="primary" @click="onSubmit">确定</el-button>
                        <el-button @click="onCancel">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>
  
<script>
import mercariClass from './mercariClass'
import yahooClass from './yahooClass'
import newMercariClass from './newMercariClass'


import {fenleiupdata, fenleiselect} from "@/http/api"

export default {
    data(){
        return {
            treeData: [],
            defaultProps: {
                children: 'children',
                label: 'cname'
            },
            currentNode: {},
            
            form: {
                className: '',
                img: '',
                isShow: true,
            },
            loading: false,
            showForm: false,
            currentLevel: null,
        }
    },
    created(){
        // localStorage.setItem('newMercariClass', JSON.stringify(newMercariClass))
        // this.treeData = JSON.parse(localStorage.getItem('newMercariClass'))
        // this.setChildren(this.treeData)



        this.getClasses()
    },
    methods: {
        setChildren(data){
            data?.forEach(item=>{
                delete item.cid
                delete item.name
                delete item.name_en
                delete item.path

                item.isShow = true

                if(item.children && item.children.length > 0){
                    this.setChildren(item.children)
                }
            })
        },
        async getClasses(){
            this.loading = true
            const res = await fenleiselect()
            const {code, data, msg} = res
            if(code === 0){
                const classes = JSON.parse(data.content)
                classes.mercariClass.forEach(item=>{
                    item.showInHome = false
                })

                // this.setChildren(classes.mercariClass)
                this.treeData = classes.mercariClass
                console.log('this.treeData', this.treeData)

            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        handleNodeClick(data, node, comp) {
            console.log(data, node, comp)
            // if(node.level === 2){
                this.showForm = true
                this.currentNode = data
                this.currentLevel = node.level
                this.form.className = data.cname || ''
                this.form.img = data.img || ''
                this.form.isShow = data.isShow || false
                
            // }else{
            //     this.showForm = false
            // }
        },
        onInput(val){
            this.currentNode.cname = val
        },
        async handleAvatarSuccess(res){
            this.form.img = res.data
            this.currentNode.img = res.data
        },

        async onSubmit(){
            this.loading = true
            const params = {
                content: JSON.stringify({
                    mercariClass: this.treeData,
                    yahooClass: []
                })
            }
            const fenlei_res = await fenleiupdata(params)
            const {code, data, msg} = fenlei_res
            if(code === 0){
                this.$message.success('操作成功')
            }else{
                this.$message.error(msg)
            }
            this.loading = false
        },
        onCancel(){
            this.showForm = false
            this.form = {
                className: '',
                img: '',
                isShow: true,
            }
            this.getClasses()
        },

        beforeAvatarUpload(file) {
            console.log(file.type)
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }
    }
}
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
  
<style lang="scss" scoped>
.tree{
    width: 300px;
}
</style>
  