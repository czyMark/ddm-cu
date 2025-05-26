<template>
    <div id="app" :class="deviceType === 'pc' ? '' : 'app2'">
        <layout v-if="$route.path.indexOf('/login') === -1"/>
        <router-view></router-view>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import layout from './components/layout/index.vue'
export default {
    name: 'App',
    components: {
        layout
    },
    created(){
        this.pcOrh5()
        if(this.deviceType !== 'pc'){
            this.$store.commit("change_menuSwitch", false)
        }   
    },
    computed: {
        ...mapState(['deviceType'])
    },
    methods: {
        pcOrh5(){
            if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))){
                this.$store.commit("change_deviceType", 'mobile')
            }else{
                this.$store.commit("change_deviceType", 'pc')
            }
        },
    }
}
</script>

<style lang="scss" scoped>
@media screen and (max-width: 750px) {
    #app{
       width: 100vw;
       height: 100vh;
       overflow-y: hidden;
       overflow-x: auto;
    }
    .app2{
       overflow-y: auto !important;
       overflow-x: hidden !important;
    }
}

#app{
    background: #D8EAFA;
}

</style>
