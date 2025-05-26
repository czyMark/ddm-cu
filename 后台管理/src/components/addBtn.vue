<template>
    <div class="addBtn center"
        @touchstart.stop="dragDown"
        @touchmove.stop="dragMove"
        @touchend.stop="dragUp"
    >
        <i class="el-icon-plus" @click="add"></i>
    </div>
</template>
  
<script>
  
export default {
    data(){
        return {

        }
    },
    methods: {
        add(){
            this.$emit("add")
        },
      dragDown(e){
        if(this.listType === 1){
          const node = document.getElementById("myFileComp")
          node.style.overflow = "hidden"
        }else if(this.listType === 2){
          const node = document.getElementById("teamFileComp")
          node.style.overflow = "hidden"
        }else if(this.listType === 3){
          const node = document.getElementById("listPageContent")
          node.style.overflow = "hidden"
        }
        const element = e.targetTouches[0]// 记录初始 client 位置，用于计算移动距离
        const client = {
          x: element.clientX,
          y: element.clientY
        }
        const offset = {
          x: e.target.offsetLeft,
          y: e.target.offsetTop
        }

        this.reduce = {
          x: client.x - offset.x,
          y: client.y - offset.y,
        }

      },
      dragMove(e){
        const element = e.targetTouches[0]
        const clientY =  element.clientY
        const clientX =  element.clientX
        const node =  e.target

        const TOP = clientY - this.reduce.y
        const LEFT = clientX - this.reduce.x
        // var T = clientY - TOP  // 限制出界添加的代码
        // var L = clientX - LEFT // 限制出界添加的代码
        // var Height = document.documentElement.clientHeight || document.body.clientHeight  // 限制出界添加的代码
        // var Width = document.documentElement.clientWidth || document.body.clientWidth     // 限制出界添加的代码
        // if(T <= 0) T = 0; // 限制出界添加的代码
        // if(T >= Height - node.clientHeight) T = Height - node.clientHeight// 限制出界添加的代码
        // if(L <= 0) L = 0;// 限制出界添加的代码
        // if(L >= Width - node.clientWidth) L = Width - node.clientWidth// 限制出界添加的代码
        node.style.top = TOP + "px"
        node.style.left = LEFT + "px"
      },
      dragUp(e){
      },
    }
}
</script>
  
<style lang="scss" scoped>
    ::v-deep .el-icon-plus{
        font-size: 7vw;
        color: #FFF;
        position: fixed;
        z-index: 999;
        bottom: 20vh;
        right: 10vw;
        width: 12vw;
        height: 12vw;
        background: #417ff9;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
  