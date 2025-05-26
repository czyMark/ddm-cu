<template>
    <div class="pageTab flex scrollbar-x">
        <div :class="['item', $route.path === item.path ? 'active' : '', $route.path.indexOf('/orderDetail') !== -1 && item.path.indexOf('/orderDetail') !== -1 ? 'active' : '']"
            @click="toRoute(item)" v-for="(item, index) in historyRecord" :key="index">
            <span>{{ item.meta.title }}</span>
            <i class="el-icon-close" @click.stop="closeTab(item, index)"></i>
        </div>
    </div>
</template>
  
<script>
import { mapState } from 'vuex';

export default {
    data(){
        return {
        }
    },
    computed: {
        ...mapState(['historyRecord']),
    },
    created(){
    },
    methods: {
        closeTab(item, index){
            // 只有一个路由，判断是不是默认页，不是跳转，是不做更改
            if(this.historyRecord.length === 1){
                return false
            }

            // 判断关闭的tab是不是当前路由
            if(this.$route.path === item.path || (this.$route.path.indexOf('/orderDetail') !== -1 && item.path.indexOf('/orderDetail') !== -1)){
                // 是当前路由，需要判断index 是不是 0,
                if(index === 0){
                    this.$router.push(this.historyRecord[index + 1].path)
                }else{
                    this.$router.push(this.historyRecord[index - 1].path)
                }
            }
            this.$store.commit('reduce_historyRecord', index)
        },
        toRoute(item){
            this.$router.push(item.path)
        },
    }
}
</script>

<style lang="scss" scoped>
.item{
    margin-right: 10px;
    padding: 10px 13px;
    background: #FAFBFC;
    border-radius: 6px 6px 0px 0px;
    color: #909399;
    cursor: pointer;
    flex-shrink: 0;
    span{
        margin-right: 10px;
    }
}
.active{
    color: #1890FF;
    background: #FFF;
}
</style>
  