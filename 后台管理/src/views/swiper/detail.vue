<template>
    <div class="page">,
        <div v-if="deviceType === 'pc'">
            <header class="center">
                <i class="el-icon-back" @click="()=>{$router.go(-1)}"></i>
                <h4>{{statusText}}轮播图</h4>
            </header>
            <nav v-loading="loading">
                <el-form ref="form" :model="form" label-width="120px">
                    <el-form-item label="轮播图图片">
                        <el-upload
                            :disabled="type === '3'"
                            class="avatar-uploader"
                            :action="type === '2' ? '/image/updata' : '/image/add'"
                            :show-file-list="false"
                            :data="type === '2' ? {id} : {}"
                            :on-success="handleAvatarSuccess">
                            <img v-if="imageUrl" :src="imageUrl" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                        <p>推荐上传 375px * 175px 的图片</p>
                    </el-form-item>
                    <el-form-item>
                        <!-- <el-button type="primary" @click="onSubmit" v-if="submitBtnText">{{submitBtnText}}</el-button> -->
                        <el-button @click="()=>{$router.go(-1)}">取消</el-button>
                    </el-form-item>
                </el-form>
            </nav>
        </div>
        <div v-else class="page_m column">
            <el-upload
                style="border: 1px dashed rgb(183 179 179)"
                :disabled="type === '3'"
                class="avatar-uploader"
                :action="type === '2' ? '/image/updata' : '/image/add'"
                :show-file-list="false"
                :data="type === '2' ? {id} : {}"
                :on-success="handleAvatarSuccess">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <p>推荐上传 375px * 175px 的图片</p>
            <el-button @click="()=>{$router.go(-1)}">取消</el-button>
        </div>
    </div>
</template>
  
<script>
import {getSwiperById, updataSwiper, addSwiper} from "@/http/api"
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
                '1': "发布",
                '2': "修改",
                '3': "",
            },
            form: {
                imageUrl: '',
            },
            imageUrl: '',
            editorConfig: {
                height: '400px'
            },
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
        if(this.type !== '1') this.getSwiperById()
    },
    methods: {
        handleAvatarSuccess(res, file) {
            this.$router.go(-1)
        },
        async getSwiperById(){
            this.loading = true
            const params = {
                id: this.id*1
            }
            const res = await getSwiperById(params)
            const {code, data, msg} = res
            if(code === 0){
                this.imageUrl = data.url
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
<style scoped lang="scss">
    ::v-deep .avatar-uploader .el-upload {
        border: 1px dashed #e5e5e5;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    ::v-deep .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    ::v-deep .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 375px;
        height: 175px;
        line-height: 175px;
        text-align: center;
    }
    .avatar {
        width: 375px;
        height: 175px;
        display: block;
    }
    .page_m{
        background: #FFF; width: 95vw; margin: 0 auto;
        p{
            font-size: 12px;
            margin: 20px auto;
            text-align: center;
        }
    }

</style>
  