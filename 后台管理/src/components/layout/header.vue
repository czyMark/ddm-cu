<template>
    <div>
        <div class="header between" v-if="deviceType === 'pc'">
            <div class="btns">
                代代米日淘后台管理
                <!-- <i class="el-icon-s-fold" @click="handleMenuChange" v-if="menuSwitch"></i>
                <i class="el-icon-s-unfold" @click="handleMenuChange" v-if="!menuSwitch"></i> -->
            </div>
            <div class="user">
                <el-dropdown trigger="click">
                    <!-- <img src="" alt=""/> -->
                    <span class="el-dropdown-link">
                        <i class="el-icon-user"></i>
                            {{userInfo.username || ''}}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <div class="header_mobile" v-else>
            <van-nav-bar
                :title="$route.meta.title"
                :left-text="canGoBack ? '返回' : ''"
                :left-arrow="canGoBack"
                @click-left="onClickLeft"
                fixed
                placeholder
            >
                <template #right>
                    <van-icon name="apps-o" size="20" @click="showMenu"/>
                </template>
            </van-nav-bar>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
    data(){
        return {
            canGoBack: false
        }
    },
    watch: {
        $route: {
            deep: true,
            immediate: true,
            handler(val){
                let isHave = false
                this.menuList?.forEach(item=>{
                    if(item.path === val.path) isHave =  true
                })
                this.canGoBack = !isHave
            }
        }
    },
    computed: {
        ...mapState(['menuSwitch', 'userInfo', 'deviceType', 'menuList']),
    },
    methods: {
        showMenu(){
            this.$store.commit('change_menuSwitch', true)
        },
        onClickLeft(){
            this.$router.go(-1)
        },
        logout(){
            this.$router.push({
                path: '/login'
            })
        },
        handleMenuChange(){
            this.$store.commit('change_menuSwitch', !this.menuSwitch)
        }
    }
}
</script>

<style lang="scss" scoped>
.header{
    width: 100vw;
    background: #fff;
    height: 70px;
    box-sizing: border-box;
    padding: 30px 16px;
    background: #97C9FF;
    margin-bottom: 20px;
    .btns{
        font-size: 26px;
        color: #fff;
        i{
            cursor: pointer;
        }
    }
    .user{
        cursor: pointer;
    }
}
.header_mobile{
    width: 100vw;
}
.el-dropdown-link{
    color: #fff;
}
</style>
