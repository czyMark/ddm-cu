import request from './request.js'

const api = {}

// 登录
api.login = params => request('POST', '/user/login', params)

// 查询中日汇率
api.getRate = params => request('POST', '/rate/select', params)

// 查询帮助设置
api.querySetting = params => request('POST', '/setting/select', params)


// 根据分类类别查询商品列表 雅虎
api.selectcates = params => request('POST', '/good/selectcates', params)

// 查询商品详情 雅虎
api.getYahooGoodDetail = params => request('POST', '/good/selectGoodDetail', params)

// 条件筛选查询商品 雅虎
api.getYahooGoodList = params => request('POST', '/good/selectGood', params)

// 条件筛选查询商品 雅虎
api.yahooOrder = params => request('POST', '/yahooOrder/pay', params)

// 条件筛选查询商品 雅虎
api.queryYahooOrderList = params => request('POST', '/yahooOrder/SelectOrderByType', params)

// 补差价 雅虎
api.fixYahooPrice = params => request('POST', '/yahooOrder/payCjPrice', params)

// 竞拍成功（补差价）微信支付
api.fixYahooPrice2 = params => request('POST', '/yahooOrder/payJpPrice', params)

// 合并订单 用户申请发货 雅虎
api.createYahoomergeOrder = params => request('POST', '/yahooOrder/createmergeOrder', params)

// 删除订单 煤炉
api.deleteMercariOrder = params => request('POST', '/MerOrder/deleteByid', params)

// 删除订单 雅虎
api.deleteYahooOrder = params => request('POST', '/yahooOrder/deleteByid', params)

// 根据订单自增主id查询订单详情 物流信息 地址 用户 雅虎
api.queryYahooOrderDetail = params => request('POST', '/yahooOrder/SelectByid', params)

// 用户付尾款 雅虎
api.yahoopayFinalPrice = params => request('POST', '/yahooOrder/payFinalPrice', params)













// 根据分类类别查询商品列表 煤炉
api.getMercariSelectcates = params => request('POST', '/Mercarigood/selectcates', params)

// 查询商品详情 煤炉
api.getMercariGoodDetail = params => request('POST', '/Mercarigood/selectdetial', params)


// 新增购物车
api.addCart = params => request('POST', '/MrShppingCar/add', params)

// 根据用户id清空购物车
api.clearCart = params => request('POST', '/MrShppingCar/deleteAll', params)

// 根据购物车商品id和用户id删除购物车商品
api.deleteCart = params => request('POST', '/MrShppingCar/deleteByid', params)

// 根据用户id查询购物车列表
api.queryCart = params => request('POST', '/MrShppingCar/selectbyid', params)




// 查询轮播图列表
api.getSwiperList = params => request('POST', '/image/selectAll', params)



// 查询公告列表
api.getNoticetList = params => request('POST', '/notice/selectList', params)

// 根据id查询公告
api.getNoticetById = params => request('POST', '/notice/selectByid', params)

// 查询常见问题列表
api.getQuestionList = params => request('POST', '/question/selectList', params)

// 根据id查询常见问题
api.getQuestionById = params => request('POST', '/question/selectByid', params)

// 新增收藏
api.addCollect = params => request('POST', '/collect/add', params)

// 根据商品编码删除收藏
api.collectDeleteByCode = params => request('POST', '/collect/deleteByCode', params)

// 查询个人收藏列表
api.collectByUserId = params => request('POST', '/collect/selectbyid', params)

// 新增浏览足迹
api.addViewLog = params => request('POST', '/liulan/add', params)

// 查询浏览足迹
api.queryViewLog = params => request('POST', '/liulan/select', params)



// 查询地址信息列表
api.selectByAduserid = params => request('POST', '/user/selectByAduserid', params)

// 新增地址信息
api.addAddress = params => request('POST', '/user/address', params)

// 根据地址id更新地址信息
api.updataByAdid = params => request('POST', '/user/updataByAdid', params)

// 根据地址id删除地址信息
api.deleteByAdid = params => request('POST', '/user/deleteByAdid', params)

// 根据地址id设为默认地址
api.defaultType = params => request('POST', '/user/defaultType', params)




// 查询个人资料
api.selectzl = params => request('POST', '/user/selectzl', params)

// 修改个人资料
api.updatazl = params => request('POST', '/user/updatazl', params)





// 新增收藏
api.addCollect = params => request('POST', '/collect/add', params)

// 删除收藏
api.deleteCollect = params => request('POST', '/collect/deleteByCode', params)

// 查询收藏列表
api.queryCollect = params => request('POST', '/collect/selectbyid', params)

// 根据用户id和商品编码查询商品是否收藏
api.queryCollectById = params => request('POST', '/collect/selectGoodCollect', params)




// 用户充值模块
api.payBalance = params => request('POST', '/account/payBalance', params)

// 查询用户余额
api.selectUserBalance = params => request('POST', '/account/selectUserBalance', params)

// 根据用户id和变更状态查询变更记录
api.selectUserBlUpdata = params => request('POST', '/account/selectUserBlUpdata', params)

// 根据用户id解冻押金
api.jdyj = params => request('POST', '/account/jdyj', params)

// 用户充值押金模块
api.payYjBalance = params => request('POST', '/account/payYjBalance', params)

// 根据用户id查询押金列表
api.queryUserYJLog = params => request('POST', '/account/yjList', params)




// 用户申请提现
api.createTx = params => request('POST', '/txRecord/createTx', params)

// 根据用户id和提现状态查询提现记录
api.usertx = params => request('POST', '/txRecord/usertx', params)








// 煤炉单商品直接下单借口
api.mercariOrder = params => request('POST', '/MerOrder/pay', params)

// 煤炉购物车下单
api.mercariCartOrder = params => request('POST', '/MerOrder/payCart', params)

// 根据用户id和状态查询订单 0全部订单,1待入库,2已入库，3申请发货（合并订单中）
api.queryMercariOrderList = params => request('POST', '/MerOrder/SelectOrderByType', params)

// 合并订单 用户申请发货
api.createmergeOrder = params => request('POST', '/MerOrder/createmergeOrder', params)

// 用户付尾款
api.payFinalPrice = params => request('POST', '/MerOrder/payFinalPrice', params)

// 根据订单自增主id查询订单详情 物流信息 地址 用户
api.queryOrderDetail = params => request('POST', '/MerOrder/SelectByid', params)

// 上传图片
api.uploadImg = params => request('POST', '/upload', params)

// 根据用户id修改用户已读
api.userRead = params => request('POST', '/contact/userRead', params)

// 根据卖家code查询商品列表
api.queryGoodsBySalerCode = params => request('POST', '/good/selectSellerGoods', params)












export default api
