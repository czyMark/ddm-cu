<template>
    <div>

        <div :class="['menu', menuSwitch ? 'menu-ani-open' : 'menu-ani-close']" v-if="deviceType === 'pc'">
            <img src="./../../assets/img/logo.png" alt="" class="logo" v-if="menuSwitch"/>
            <el-menu
                mode="vertical"
                :collapse="!menuSwitch"
                :default-active="defalutcActive"
                unique-opened
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                background-color="#fff"
                text-color="#8C8C8C"
                active-text-color="#0074FF">

                <div v-for="(item, index) in menuList" :key="index">

                    <template  v-if="item.children">
                        <el-submenu :index="`${index+1}-1`">
                            <template slot="title">
                                <!-- <i class="el-icon-location"></i> -->
                                <span slot="title">{{item.label}}</span>
                            </template>

                            <el-menu-item-group>
                                <el-menu-item :index="`${index+1}-${i+1}-1`" v-for="(it, i) in item.children" :key="i"  @click="toPath(it)">
                                    <!-- <i class="el-icon-location"></i> -->
                                    <span slot="title">{{it.label}}</span>
                                </el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>

                    </template>

                    <template  v-else>
                        <el-menu-item :index="`${index+1}-1`" :disabled="item.disabled">
                            <!-- <i class="el-icon-menu"></i> -->
                            <span slot="title">{{item.label}}</span>
                        </el-menu-item>
                    </template>




                </div>
               

            </el-menu>
        </div>



        <div v-else>
            <!-- <div class="showMenu center" @click="showMenu">
                <van-icon name="arrow" />
            </div> -->
            <van-popup
                v-model="menuSwitch2"
                position="left"
                :style="style"
                @click-overlay="closeMenu">
                <div class="menuList">
                    <div :class="['item']"  @click="toPath(item)"
                        v-for="(item, index) in menuList" :key="index">
                        {{item.meta.title}}
                    </div>
                    <div class="item logout" @click="logout">退出登录</div>
                </div>
            </van-popup>
        </div>
    </div>

</template>
  
<script>
import { mapState } from 'vuex'
import router from './../../router'
export default {
    data(){
        return {
            // menu: router.options.routes.filter(item=>item.alwaysShow),
            defalutcActive: '1-1',
            style: {
                height: '100vh',
                width: '30vw'
            },
            menuSwitch2: false,
        }
    },
    watch: {
        $route: {
            handler(val){
                this.menuList?.forEach((item, index)=>{
                    if(val.path === item.path){
                        this.defalutcActive = `${index + 1}-1`
                    }
                })
            },
            immediate: true,
            deep: true
        },
        menuSwitch(val){
            this.menuSwitch2 = val
        }
    },
    computed: {
        ...mapState(['menuSwitch', 'menuList', 'deviceType']),
    },
    created(){
        console.log('menu', this.menuList)
    },
    methods: {
        handleOpen(){},
        handleClose(){},
        toPath(item){
            this.$router.push({
                path: item.path
            })
            // this.$store.commit("add_historyRecord", item)
            // if(this.deviceType !== 'pc') this.$store.commit('change_menuSwitch', false)
        },
        showMenu(){
            this.$store.commit('change_menuSwitch', true)
        },
        closeMenu(){
            this.$store.commit('change_menuSwitch', false)
        },
        logout(){
            this.$store.commit('change_menuSwitch', false)
            this.$router.push({
                path: '/login'
            })
        }
    }
}
</script>
  
<style lang="scss" scoped>
.menu{
    width: 250px;
    height: 100vh;
    background: #fff;
    padding: 20px 0 0 0;
    border-radius: 20px;
    overflow: auto;
}
.menu::-webkit-scrollbar{
    width: 6px;
}
.menu::-webkit-scrollbar-thumb{
    width: 6px;
    background: #C6D0DE;
    border-radius: 2px;
}
.menu-ani-close{
    animation: muneClose;
    animation-duration: 0.5s;
    width: 64px;
}
.menu-ani-open{
    animation: muneOpen;
    animation-duration: 0.5s;
    width: 250px;
}
@keyframes muneClose {
    0%{
        width: 250px;
    }
    100%{
        width: 64px;
    }
}
@keyframes muneOpen {
    0%{
        width: 64px;
    }
    100%{
        width: 250px;
    }
}
.logo{
    width: 206px;
    height: 62px;
    margin: 0 auto 20px;
    cursor: pointer;
}
::v-deep .el-menu-item:hover{
    background: linear-gradient(to right, rgb(243, 247, 254), rgb(193, 225, 255)) !important;
}
::v-deep .el-menu .is-active{
    background: linear-gradient(to right, rgb(243, 247, 254), rgb(193, 225, 255)) !important;
}
::v-deep .el-submenu__title .is-active{
    background: linear-gradient(to right, rgb(243, 247, 254), rgb(193, 225, 255)) !important;
}
::v-deep .el-submenu__title:hover{
    background: linear-gradient(to right, rgb(243, 247, 254), rgb(193, 225, 255)) !important;
}


::v-deep .el-menu{
    border: none;
}

::v-deep .el-menu .is-disabled{
    opacity: 1; 
    background: #fff !important;
    color: #000 !important;
    font-size: 18px;
    font-weight: 600;
}

.showMenu{
    position: fixed;
    top: 40vh;
    width: 5vw;
    height: 10vh;
    border: 1px solid #b6b3b3;
    border-left: none;
    background: #FFF;
    border-radius: 0 10px 10px 0;
}
.menuList{
    width: 30vw;
    height: 100vh;
    font-size: 14px;
    position: relative;
    .item{
        padding: 10px 10px;
        border-bottom: 1px solid #e7e7e7;
    }
    .logout{
        position: absolute;
        bottom: 0;
        left: 10px;
        font-weight: 600;
        color: red;
    }
}

@media screen and (max-width: 750px) {

}

</style>
  