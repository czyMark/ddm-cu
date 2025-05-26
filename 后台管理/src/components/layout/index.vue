<template>
    <div class="layout">
        <div class="header">
            <Header></Header>
        </div>

        <div :class="['nav flex']">
            <div class="menu">
                <Menu></Menu>
            </div>
            <div class="pageContainer" v-if="deviceType === 'pc'">
                <!-- <div class="pageTab scrollbar-x">
                    <PageTab></PageTab>
                </div> -->
                <div class="content">
                    <router-view :key="key"></router-view>
                </div>
            </div>
        </div>
    </div>
    
</template>

<script>
import Menu from './menu.vue'
import Header from './header.vue'
import PageTab from './pageTab.vue'
import { mapState } from 'vuex'

export default {
    components:{Header, Menu, PageTab},
    data(){
        return {
            data: [],
            defaultProps: {
            children: 'children',
            label: 'label'
            }
      };
    },
    computed: {
        ...mapState(['menuSwitch', 'deviceType']),
        key() {
            return this.$route.fullPath;
        },
    },
    methods: {
        handleNodeClick(data) {
            console.log(data);
        }
    }
}
</script>

<style lang="scss" scoped>
.nav{
    width: 100vw;
}
.menu-ani-close1{
    animation: muneClose;
    animation-duration: 0.5s;
    width: calc(100vw - 64px);
}
.menu-ani-open1{
    animation: muneOpen;
    animation-duration: 0.5s;
    width: calc(100vw - 250px);
}
@keyframes muneClose {
    
    0%{
        width: calc(100vw - 250px);
    }
    100%{
        width: calc(100vw - 64px);
    }
}
@keyframes muneOpen {
    0%{
        width: calc(100vw - 64px);
    }
    100%{
        width: calc(100vw - 250px);
    }
}
.pageContainer{
    margin: 16px 16px 0 16px;
    overflow: auto;
    height: calc(100vh - 120px);
    width: calc(100vw - 250px);
}
.pageContainer::-webkit-scrollbar{
    width: 6px;
}
.pageContainer::-webkit-scrollbar-thumb{
    width: 6px;
    background: #C6D0DE;
    border-radius: 2px;
}

.pageTab{
    flex-shrink: 0;
    height: 50px;
    box-sizing: border-box;
    width: 100%;
    overflow-x: auto;
    
}
.content{
    background: #FFF;
    min-height: calc(100vh - 112px);
    padding: 20px 14px 50px;
}
</style>
  