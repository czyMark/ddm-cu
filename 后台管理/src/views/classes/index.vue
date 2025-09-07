<template>
    <div class="page">
        
        <div class="tabs">
            <el-radio-group v-model="currentPlant" style="margin-bottom: 30px;" @change="onCurrentPlantClick">
                <el-radio-button label="yahoo">雅虎</el-radio-button>
                <el-radio-button label="mercari">煤炉</el-radio-button>

            </el-radio-group>

            <el-row>
                <el-col :span="3">
                    <el-switch
                        v-model="showYahoo"
                        active-text="展示雅虎"
                        inactive-text="隐藏雅虎">
                    </el-switch>
                </el-col>

                <el-col :span="3">
                    <el-switch
                        v-model="showSearch"
                        active-text="展示搜索"
                        inactive-text="隐藏搜索">
                    </el-switch>
                </el-col>

                <el-col :span="3">
                    <el-switch
                        v-model="showWallet"
                        active-text="展示钱包"
                        inactive-text="隐藏钱包">
                    </el-switch>
                </el-col>

                <el-col :span="5">
                    <span style="margin-right: 10px">汇率增加</span>
                    <el-input v-model="rateFlow" type="number" style="width: 150px"></el-input>
                </el-col>

            </el-row>

        </div>
        <el-row>
            <div class="nav flex" v-loading="loading">

                <template>
                    <div class="tree">
                        <div class="addRoot center">
                            <el-button type="primary" size="mini" icon="el-icon-add" @click="addRoot">添加一级分类</el-button>
                        </div>
                        
                        <div class="treeBox scrollbar">
                            <el-tree
                                draggable
                                :accordion="true"
                                :data="currentPlant === 'yahoo' ? treeData_Yahoo : treeData_Mercari"
                                :props="defaultProps"
                                @node-click="handleNodeClick"
                                :expand-on-click-node="false"
                                node-key="id"
                                :render-content="renderContent"
                            >
                            </el-tree>
                        </div>
                    </div>
                    <div class="setImg" v-if="showForm">
                        <el-form ref="form" :model="form" label-width="150px" style="margin-top: 20px">
                            <el-form-item label="分类Id" label-width="150px">
                                <el-input v-model="form.classId" style="width: 300px" @input="onInput2"></el-input>
                            </el-form-item>
                            <el-form-item label="分类名" label-width="150px">
                                <el-input v-model="form.className" style="width: 300px" @input="onInput"></el-input>
                            </el-form-item>
                            <el-form-item label="分类图片" label-width="150px">
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
                            <el-form-item label="是否显示在首页" v-if="currentLevel === 1 || currentLevel === 2">
                                <el-switch
                                    v-model="currentNode.showInHome"
                                    active-text="显示"
                                    inactive-text="隐藏"
                                >
                                </el-switch>
                            </el-form-item>
                            <el-form-item label="Id转关键词搜索">
                                <el-switch
                                    v-model="currentNode.onKeySearch"
                                    active-text="是"
                                    inactive-text="否"
                                >
                                </el-switch>
                            </el-form-item>
                            <el-form-item label="关键词" label-width="150px" v-if="currentNode.onKeySearch">
                                <el-input v-model="form.keyword" style="width: 300px" @input="onInput3"></el-input>
                            </el-form-item>

                            
                            <el-form-item label="" label-width="200px">
                                <el-button type="primary" @click="onSubmit">确定</el-button>
                                <el-button @click="onCancel">取消</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </template>

            </div>
        </el-row>
    </div>
</template>
  
<script>
import mercariClass from './mercariClass'
import yahooClass from './yahooClass'
import newMercariClass from './newMercariClass'


import {fenleiupdata, fenleiselect} from "@/http/api"
let id = 10000
export default {
    data(){
        return {
            treeData_Yahoo: [],
            treeData_Mercari: [],
            defaultProps: {
                children: 'children',
                label: 'cname'
            },
            currentNode: {},
            currentLevel: null,
            
            form: {
                classId: '',
                className: '',
                img: '',
                isShow: true,
                keyword: '',
            },
            loading: false,
            showForm: false,

            newArr: [],

            currentPlant: 'yahoo',
            showYahoo: false,
            showSearch: false,
            showWallet: false,
            showMercari: false,
            isSend: false,

            rateFlow: 0,

        }
    },
    created(){
        // this.setChildren(mercariClass)
        // console.log('mercariClass', mercariClass)
        // localStorage.setItem('mercariClass', JSON.stringify(mercariClass))


        
        // const classes = JSON.parse(localStorage.getItem('content'))
        // this.treeData_Yahoo = classes.yahooClass
        // this.treeData_Mercari = classes.mercariClass

        console.log('this.isSend', this.isSend)
        this.getClasses()
    },
    methods: {
        renderContent(h, { node, data, store }) {
            return (
                <span class="custom-tree-node">
                    <span>{node.label}</span>
                    <span>
                    <el-button size="mini" type="text" on-click={ () => this.append(data) }>添加</el-button>
                    <el-button size="mini" type="text" on-click={ () => this.remove(node, data) }>删除</el-button>
                    </span>
                </span>
            )
        },
        addRoot() {
            
            if(this.currentPlant === 'yahoo'){
                this.treeData_Yahoo.push({
                    children: [],
                    id: ++id,
                    cname: "新增节点"+id,
                    img: "",
                    isShow: false,
                    showInHome: false,
                })
            }else{
                this.treeData_Mercari.push({
                    children: [],
                    id: ++id,
                    cname: "新增节点"+id,
                    img: "",
                    isShow: false,
                    showInHome: false,
                })
            }
        },
        append(data) {
            const newChild = { 
                id: ++id,
                cname: "新增节点"+id,
                children: [],
                img: "",
                isShow: false,
                showInHome: false,
             };
            if (!data.children) {
            this.$set(data, 'children', []);
            }
            data.children.push(newChild);
        },
        remove(node, data) {
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            children.splice(index, 1);
        },
        onCurrentPlantClick(val){
            this.showForm = false
            this.currentNode = {}
            this.currentLevel = null

            // this.getClasses()

            if(val === 'yahoo'){

            }else if(val === 'mercari'){

            }
        },
        setId(data){
            data?.forEach(item=>{
                this.newArr.forEach(it=>{
                    if(item.id === it.id){
                        item.path = it.path
                    }
                })

                if(item.children && item.children.length > 0){
                    this.setId(item.children)
                }
            })
        },
        bianpinghua(data, newArr){
            data?.forEach(item=>{
                newArr.push({
                    id: item.id,
                    path: item.path
                })

                if(item.children && item.children.length > 0){
                    this.bianpinghua(item.children, newArr)
                }
            })
        },

        // 加字段
        setChildren(data){
            data?.forEach(item=>{

                // 煤炉
                // delete item.cid
                // delete item.name
                // delete item.name_en
                // delete item.parentid

                // item.isShow = true
                // item.showInHome = false
                // item.img = item.img || ''

                item.onKeySearch = false

                if(item.children && item.children.length > 0){
                    this.setChildren(item.children)
                }
            })
        },
        setChildrenImg(data){
            data?.forEach(item=>{

                item.img = 'https://ddm-cu.com/upload/2025/08/04/c507e420-71a0-4b59-bab0-ea4744bb6099.png'

                if(item.children && item.children.length > 0){
                    this.setChildrenImg(item.children)
                }
            })
        },
        async getClasses(){
            this.loading = true
            const res = await fenleiselect()
            const {code, data, msg} = res
            if(code === 0){
                const classes = JSON.parse(data.content)
                this.treeData_Yahoo = classes.yahooClass
                this.treeData_Mercari = classes.mercariClass

                this.showYahoo = classes.showYahoo
                this.showSearch = classes.showSearch
                this.showWallet = classes.showWallet
                this.rateFlow = classes.rateFlow
                
                
            }else{
                this.$message.error(msg)
            }

            // this.treeData_Yahoo = JSON.parse(localStorage.getItem('yahooClass'))
            // this.treeData_Mercari = JSON.parse(localStorage.getItem('mercariClass'))
            
            this.loading = false
        },
        handleNodeClick(data, node, comp) {
            console.log(data, node, comp)
            // if(node.level === 2){
                this.showForm = true
                this.currentNode = data
                this.currentLevel = node.level
                this.form.classId = data.id || ''
                this.form.className = data.cname || ''
                this.form.img = data.img || ''
                this.form.isShow = data.isShow || false
                this.form.keyword = data.keyword || ''
                
            // }else{
            //     this.showForm = false
            // }
        },
        onInput(val){
            this.currentNode.cname = val
        },
        onInput2(val){
            this.currentNode.id = val
        },
        onInput3(val){
            this.currentNode.keyword = val
        },
        async handleAvatarSuccess(res){
            this.form.img = res.data
            this.currentNode.img = res.data
        },

        async onSubmit(){
            this.loading = true
            const params = {
                content: JSON.stringify({
                    mercariClass: this.treeData_Mercari,
                    yahooClass: this.treeData_Yahoo,
                    showYahoo: this.showYahoo,
                    showSearch: this.showSearch,
                    showWallet: this.showWallet,
                    rateFlow: this.rateFlow,
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
                classId: '',
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
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
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
.nav{
    margin-top: 20px
}
.treeBox{
    height: calc(100vh - 300px);
    overflow-x: hidden;
    overflow-y: auto;
}
</style>
  