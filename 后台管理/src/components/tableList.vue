<template>
    <div class="tableList">
        <div class="item" v-for="(item, index) in list" :key="index">
            <div class="index between">

                <!-- 序号 -->
                <div v-if="['yahooList', 'mercariList'].indexOf(type) === -1">序号：{{index+1}}</div>
                <div v-if="type === 'yahooList'" class="flex">
                    <!-- <div>（序号：{{index+1}}）</div> -->
                    <a :href="`https://page.auctions.yahoo.co.jp/jp/auction/${item.goodCode}`" target="_blank" style="color: blue">商品id：{{item.goodCode}}</a>
                </div>
                <div v-if="type === 'mercariList'" class="flex">
                    <!-- <div>（序号：{{index+1}}）</div> -->
                    <a :href="`https://jp.mercari.com/item/${item.goodCode}`" target="_blank" style="color: blue">商品id：{{item.goodCode}}</a>
                </div>


                <!-- 操作按钮 -->

                <div class="options" v-if="type === 'drawRequest'">
                    <i v-if="item.type === 1" class="el-icon-check" @click="updataDrawRecord(item.id, item.userid, 2)"></i>
                    <i v-if="item.type === 1" class="el-icon-close" style="color: red" @click="updataDrawRecord(item.id, item.userid, 3)"></i>
                </div>
                <div class="options" v-else-if="type === 'releaseDeposit'">
                    <i v-if="item.type === 1" class="el-icon-check" @click="checkUserReleaseDeposit(item, 1)"></i>
                    <i v-if="item.type === 1" class="el-icon-close" style="color: red" @click="checkUserReleaseDeposit(item, 0)"></i>
                </div>
                <div class="options" v-else-if="type === 'userList'">
                    <i class="el-icon-phone-outline" @click="showContactUser(item)"></i>
                </div>
                <div class="options" v-else-if="type === 'mercariList'">
                    <el-button type="text" size="small" @click="orderRefund(item)" v-if="item.type === 1">退款</el-button>
                    <el-button type="text" size="small" @click="showPostFee(item)" v-if="item.type === 1">已入库</el-button>
                    <el-button type="text" size="small" @click="toDetail(item)" v-if="item.type === 3">计算尾款</el-button>
                    <el-button type="text" size="small" @click="changeCount(item)" v-if="item.type === 4">更正尾款金额</el-button>
                    <el-button type="text" size="small" @click="sendPackage(item)" v-if="item.type === 5">发货</el-button>
                    <el-button type="text" size="small" @click="review(item)">查看</el-button>
                    <el-button type="text" size="small" @click="deleteItem(item)" style="color: red">删除</el-button>
                </div>
                <div class="options" v-else-if="type === 'yahooList'">
                    <el-button type="text" size="small" @click="changeStatus(item.id, 2, item.userid)" v-if="item.type === 1">竞拍成功</el-button>
                    <el-button type="text" size="small" @click="changeStatus(item.id, 3, item.userid)" v-if="item.type === 1">竞拍失败</el-button>
                    <el-button type="text" size="small" @click="showPostFee(item)" v-if="item.type === 4">已入库</el-button>
                    <el-button type="text" size="small" @click="toDetail(item)" v-if="item.type === 6">计算尾款</el-button>
                    <el-button type="text" size="small" @click="changeCount(item)" v-if="item.type === 7">更正尾款金额</el-button>
                    <el-button type="text" size="small" @click="sendPackage(item)" v-if="item.type === 8">发货</el-button>
                    <el-button type="text" size="small" @click="review(item)">查看</el-button>
                    <el-button type="text" size="small" @click="deleteItem(item)" style="color: red">删除</el-button>
                </div>
                <div class="options" v-else>
                    <i class="el-icon-div" @click="divItem(item)"></i>
                    <i class="el-icon-edit-outline" @click="editItem(item)"></i>
                    <i class="el-icon-delete" style="color: red" @click="deleteItem(item)"></i>
                </div>
                
            </div>
            <div class="between" v-if="type !== 'yahooList' && type !== 'mercariList'">
                <div class="lable lable2 flex" style="align-items: center"
                    v-if="['drawRequest', 'userList', 'releaseDeposit'].indexOf(type) !== -1">
                    <div>用户头像：</div>
                    <div>
                        <img v-if="item.avatarUrl" :src="item.avatarUrl" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                        <img v-else src="@/assets/defaultAvatar.png" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: auto">
                    </div>
                </div>
                <div class="lable" v-for="(it, i) in column.filter(item=>item.prop !== 'index')" :key="i">
                    {{it.label}}: {{item[it.prop]}}
                </div>
                <div class="swiper" v-if="type === 'swiper'">
                    <img :src="item.url" alt="">
                </div>
                <div class="notice" v-if="['notice', 'question'].indexOf(type) !== -1">
                    标题：{{item.title}}
                </div>
            </div>

            <div class="container" v-if="type === 'yahooList'">
                <div class="itemHeader between">
                    <div :class="['no', item.isCurrentDay ? 'isCurrentDay' : '']">订单号：{{item.orderid}}</div>
                </div>
                <div class="itemNav flex">
                    <img :src="item.imageurl"/>
                    <div class="info column">
                        <div class="title">{{item.title}}</div>
                        <div class="bottom between">
                            <div class="price">价格总计<span>{{item.priceTotal}}</span> 円</div>
                        </div>
                        <div class="bottom between">
                            <div class="price">商品价格<span>{{item.price}}</span> 円</div>
                        </div>
                    </div>
                </div>
                <div class="status between">
                    <div>用户昵称：{{item.user.nickName}}</div>
                    <div>用户id：{{item.userid}}</div>
                </div>
                <div class="status between">
                    <div>下单时间：{{item.createtime}}</div>
                    <div>订单状态：<span>{{yahooTypeMap[item.type]}}</span></div>
                </div>
                <div class="showChildren" v-if="item.children?.length > 0 && !item.showChildren" @click="showChildren(item)">显示合并订单</div>
            </div>


            <template v-if="type === 'yahooList' && item.showChildren">
                <div class="container" v-for="(it, i) in item.children" :key="i">
                    <div class="itemNav flex">
                        <img :src="it.imageurl"/>
                        <div class="info column">
                            <div class="title">{{it.title}}</div>
                            <div class="bottom between">
                                <div class="price">商品价格<span>{{it.price}}</span> 円</div>
                            </div>
                        </div>
                    </div>
                </div>
            </template>

            <div class="container" v-if="type === 'mercariList'">
                <div class="itemHeader between">
                    <div :class="['no', item.isCurrentDay ? 'isCurrentDay' : '']">订单号：{{item.orderid}}</div>
                </div>
                <div class="itemNav flex">
                    <img :src="item.imageurl"/>
                    <div class="info column">
                        <div class="title">{{item.title}}</div>
                        <div class="bottom between">
                            <div class="price">价格总计<span>{{item.priceTotal}}</span> 元</div>
                        </div>
                        <div class="bottom between">
                            <div class="price">商品价格<span>{{item.JPprice}}</span> 円</div>
                        </div>
                    </div>
                </div>
                <div class="status between">
                    <div>用户昵称：{{item.user.nickName}}</div>
                    <div>用户id：{{item.userid}}</div>
                </div>
                <div class="status between">
                    <div>下单时间：{{item.time}}</div>
                    <div>订单状态：<span>{{mercariTypeMap[item.type]}}</span></div>
                </div>
                <div class="showChildren" v-if="item.children?.length > 0 && !item.showChildren" @click="showChildren(item)">显示合并订单</div>
            </div>

            <template v-if="type === 'mercariList' && item.showChildren">
                <div class="container" v-for="(it, i) in item.children" :key="i">
                    <div class="itemNav flex">
                        <img :src="it.imageurl"/>
                        <div class="info column">
                            <div class="title">{{it.title}}</div>
                            <div class="bottom between">
                                <div class="price">商品价格<span>{{it.JPprice}}</span> 円</div>
                            </div>
                        </div>
                    </div>
                </div>
            </template>



        </div>
        <div class="nodata column" v-if="list.length === 0">
            <img src="@/assets/images/noSearchResult.png" alt=""/>
            <span>暂无数据</span>
        </div>

        <div class="pagination center" v-if="showPagination">
            <el-pagination
                style="width: 100vw"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNumber"
                :page-sizes="[5, 10, 30, 50]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
            >
            </el-pagination>
        </div>
    </div>
</template>
  
<script>
  
export default {
    props: {
        showPagination: {
            type: Boolean,
            default: false
        },
        type: {
            type: String,
            default: ""
        },
        list: {
            type: Array,
            default: ()=>[]
        },
        yahooTypeMap: {
            type: Object,
            default: ()=>{}
        },
        mercariTypeMap: {
            type: Object,
            default: ()=>{}
        },
        column: {
            type: Array,
            default: ()=>[]
        },
        pageNumber: {
            type: Number,
            default: 1
        },
        pageSize: {
            type: Number,
            default: 30
        },
        total: {
            type: Number,
            default: 0
        },
    },
    data(){
        return {

        }
    },
    methods: {
        divItem(item){
            this.$emit('divItem', item)
        },
        editItem(item){
            this.$emit('editItem', item)
        },
        deleteItem(item){
            this.$emit('deleteItem', item)
        },
        changeStatus(id, status, userid){
            this.$emit('changeStatus', id, status, userid)
        },
        orderRefund(item){
            this.$emit('orderRefund', item)
        },
        showPostFee(item){
            this.$emit('showPostFee', item)
        },
        toDetail(item){
            this.$emit('toDetail', item)
        },
        changeCount(item){
            this.$emit('changeCount', item)
        },
        sendPackage(item){
            this.$emit('sendPackage', item)
        },
        review(item){
            this.$emit('review', item)
        },
        showContactUser(item){
            this.$emit('showContactUser', item)
        },
        updataDrawRecord(id, userid, status){
            this.$emit('updataDrawRecord', id, userid, status)
        },
        checkUserReleaseDeposit(item, status){
            this.$emit('checkUserReleaseDeposit', item, status)
        },
        handleSizeChange(val){
            this.$emit('handleSizeChange', val)
        },
        handleCurrentChange(val){
            this.$emit('handleCurrentChange', val)
        },
        showChildren(item){
            this.$set(item, "showChildren", true)
        }
        
    }
}
</script>
  
<style lang="scss" scoped>
.tableList{
    width: 95vw;
    margin: 20px auto;
    .item{
        width: 100%;
        background: #FFFFFF;
        border-radius: 10px;
        opacity: 1;
        border: 1px solid #E1E1E6;
        margin-bottom: 10px;
        padding: 15px;
        padding-bottom: 5px;
        font-size: 12px;
        .index{
            border-bottom: 1px solid #E1E1E6;
            padding-bottom: 5px;
            .options{
                color: #1989fa;
                i{
                    margin: 0 10px;
                    font-size: 16px;
                }
            }
        }
        .between{
            flex-wrap: wrap;
            .lable2{
                display: flex;
            }
        }
        .lable{
            margin: 5px 0;
            width: 49%;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;     //  限制行数  
            overflow: hidden;
            text-overflow : ellipsis; 
        }
    }
}
.nodata{
    img{
        width: 50vw;
    }
    span{
        margin-top: 20px;
        opacity: 0.5;
    }
}
.pagination{
    box-sizing: border-box;
    padding: 0 10px;
    width: 100vw; overflow: auto;
    position: fixed;
    bottom: 0;
    left: 0;
    background: #FFF;
    border-top: 1px solid #E1E1E6;
    height: 15vw;
}
.swiper img{
    width: 90vw;
}
.notice{
    padding: 10px 0;
}

.item{
    box-shadow: 0px 2px 5px 3px rgba(0,0,0,0.07);
    border-radius: 20px;
    padding: 20px;
    background: #FFF;
    margin-bottom: 30px;
    // .item{
    //     width: 650rpx;
    //     .container{
    //         width: 650rpx;
    //     }
    //     image{
    //         height: 190rpx;
    //         width: 190rpx;
    //     }
    // }
    .container{
        width: 85vw;
        .status{
            padding: 3px 0;
            span{
                color: red;
            }
        }
        .showChildren{
            border-top: 1px dashed #e1e1e6;
            text-align: center;
            padding: 5px;
            color: blue;
        }
    }
    img{
        height: 20vw;
        width: 20vw;
    }
    .itemHeader{
        width: 100%;
        margin: 10px 0;
    }
    .itemNav{
        width: 100%;
        margin-top: 10px;
    }
    .info{
        margin-left: 2vw;
        width: 65vw;
        justify-content: space-around;
        align-items: flex-start;
        .title{
            width: 100%;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3;
            overflow: hidden;
            text-overflow : ellipsis; 
        }
        .bottom{
            width: 100%;
        }
        .price span{
            color: red;
            padding: 0 10px;
        }
        .status{
            width: 100%;
            padding: 3px 0;
        }
    }
}

.isCurrentDay{
    color: red;
    font-weight: 600;
}

</style>
  