import service from "./request";

// -----------------------------------------------

// 查询轮播图列表
export function getSwiperList(data) {
    return service({
        url: "/image/selectAll",
        method: "POST",
        data,
    });
}

// 根据id查询轮播图
export function getSwiperById(data) {
    return service({
        url: "/image/selectByid",
        method: "POST",
        data,
    });
}

// 根据轮播图id修改轮播图
export function updataSwiper(data) {
    return service({
        url: "/image/updata",
        method: "POST",
        data,
    });
}

// 新增轮播图
export function addSwiper(data) {
    return service({
        url: "/image/add",
        method: "POST",
        data,
    });
}

// 根据轮播图id删除轮播图
export function deleteSwiper(data) {
    return service({
        url: "/image/deleteByid",
        method: "POST",
        data,
    });
}


// -----------------------------------------------

// 查询公告列表
export function getNoticetList(data) {
    return service({
        url: "/notice/selectList",
        method: "POST",
        data,
    });
}

// 根据id查询公告
export function getNoticetById(data) {
    return service({
        url: "/notice/selectByid",
        method: "POST",
        data,
    });
}

// 根据公告id修改标题和内容
export function updataNotice(data) {
    return service({
        url: "/notice/updataNotice",
        method: "POST",
        data,
    });
}

// 新增公告
export function addNotice(data) {
    return service({
        url: "/notice/addNotice",
        method: "POST",
        data,
    });
}

// 根据公告id删除公告
export function deleteNotice(data) {
    return service({
        url: "/notice/deleteNotice",
        method: "POST",
        data,
    });
}


// --------------------------------------------



// 查询常见问题列表
export function getQuestionList(data) {
    return service({
        url: "/question/selectList",
        method: "POST",
        data,
    });
}

// 根据id查询常见问题
export function getQuestionById(data) {
    return service({
        url: "/question/selectByid",
        method: "POST",
        data,
    });
}

// 根据常见问题id修改标题和内容
export function updataQuestion(data) {
    return service({
        url: "/question/updataQuestion",
        method: "POST",
        data,
    });
}

// 新增常见问题
export function addQuestion(data) {
    return service({
        url: "/question/addQuestion",
        method: "POST",
        data,
    });
}

// 根据常见问题id删除常见问题
export function deleteQuestion(data) {
    return service({
        url: "/question/deleteQuestion",
        method: "POST",
        data,
    });
}

// --------------------------------------------

// 查询帮助设置
export function querySetting(data) {
    return service({
        url: "/setting/select",
        method: "POST",
        data,
    });
}

// 修改帮助设置
export function updataSetting(data) {
    return service({
        url: "/setting/updataSetting",
        method: "POST",
        data,
    });
}



// 分页查询用户列表
export function queryUserList(data) {
    return service({
        url: "/user/selectAlluser",
        method: "POST",
        data,
    });
}


// 查询中日汇率
export function queryRate(data) {
    return service({
        url: "/rate/select",
        method: "POST",
        data,
    });
}
// 更新中日汇率
export function updataRate(data) {
    return service({
        url: "/rate/updata",
        method: "POST",
        data,
    });
}

// 查询cookie
export function queryCookie(data) {
    return service({
        url: "/cookie/select",
        method: "POST",
        data,
    });
}
// 修改cookie
export function updataCookie(data) {
    return service({
        url: "/cookie/updata",
        method: "POST",
        data,
    });
}




// 根据订单自增主id删除订单（煤炉）
export function deleteMercariOrderList(data) {
    return service({
        url: "/MerOrder/deleteByid",
        method: "POST",
        data,
    });
}
// 分页查询所有订单（煤炉）
export function queryMercariOrderList(data) {
    return service({
        url: "/MerOrder/SelectAll",
        method: "POST",
        data,
    });
}
// 根据用户id和type状态修改订单状态（煤炉）
export function updataMercariOrderStatus(data) {
    return service({
        url: "/MerOrder/updataTypeByid",
        method: "POST",
        data,
    });
}
// 管理员计算费用接口（煤炉）
export function countCost(data) {
    return service({
        url: "/MerOrder/updataFy",
        method: "POST",
        data,
    });
}
// 管理员发货（煤炉）
export function sendGood(data) {
    return service({
        url: "/MerOrder/sendGood",
        method: "POST",
        data,
    });
}
// 订单详情（煤炉）
export function queryMercariOrderDetail(data) {
    return service({
        url: "/MerOrder/SelectByid",
        method: "POST",
        data,
    });
}

// 用户退款
export function orderRefund(data) {
    return service({
        url: "/MerOrder/refundsOder",
        method: "POST",
        data,
    });
}




// 根据订单自增主id删除订单（雅虎）
export function deleteYahooOrderList(data) {
    return service({
        url: "/yahooOrder/deleteByid",
        method: "POST",
        data,
    });
}
// 分页查询所有订单（雅虎）
export function queryYahooOrderList(data) {
    return service({
        url: "/yahooOrder/SelectAll",
        method: "POST",
        data,
    });
}
// 根据用户id和type状态修改订单状态（雅虎）
export function updataYahooOrderStatus(data) {
    return service({
        url: "/yahooOrder/updataTypeByid",
        method: "POST",
        data,
    });
}
// 管理员计算费用接口（雅虎）
export function countYahooCost(data) {
    return service({
        url: "/yahooOrder/updataFy",
        method: "POST",
        data,
    });
}
// 管理员发货（雅虎）
export function sendYahooGood(data) {
    return service({
        url: "/yahooOrder/sendGood",
        method: "POST",
        data,
    });
}
// 竞拍成功 补差价（雅虎）
export function fixYahooPrice(data) {
    return service({
        url: "/yahooOrder/payCjPrice",
        method: "POST",
        data,
    });
}
// 订单详情（雅虎）
export function queryYahooGoodDetail(data) {
    return service({
        url: "/yahooOrder/SelectByid",
        method: "POST",
        data,
    });
}
// 导出雅虎订单（雅虎）
export function exportYahooOrder(data) {
    return service({
        url: "/yahooOrder/exportOrder",
        method: "POST",
        data,
        headers: {
            'Content-Type': 'application/json; application/octet-stream',
        },
        responseType: "blob"
    });
}



// 导出煤炉订单（煤炉）
export function exportMercariOrder(data) {
    return service({
        url: "/MerOrder/exportOrder",
        method: "POST",
        data,
        headers: {
            'Content-Type': 'application/json; application/octet-stream',
        },
        responseType: "blob"
    });
}



// 根据用户id和提现状态查询提现记录
export function queryDrawRecord(data) {
    return service({
        url: "/txRecord/usertx",
        method: "POST",
        data,
    });
}
// 修改提现状态
export function updataDrawRecord(data) {
    return service({
        url: "/txRecord/updataType",
        method: "POST",
        data,
    });
}
// 根据用户id解冻押金
export function releaseDeposit(data) {
    return service({
        url: "/account/jdyj",
        method: "POST",
        data,
    });
}
// 根据用户id查询押金列表
export function queryReleaseDepositList(data) {
    return service({
        url: "/account/yjList",
        method: "POST",
        data,
    });
}
// 后台押金审批
export function checkUserReleaseDeposit(data) {
    return service({
        url: "/account/yjShenpi",
        method: "POST",
        data,
    });
}




// 更改用户管理员状态 1管理员 0 不是管理员
export function updataAdmin(data) {
    return service({
        url: "/user/updataAdmin",
        method: "POST",
        data,
    });
}

// 上传图片
export function uploadImg(data) {
    return service({
        url: "/upload",
        method: "POST",
        data,
    });
}

// 后台管理联系用户
export function contactUser(data) {
    return service({
        url: "/contact/contactUser",
        method: "POST",
        data,
    });
}


// 登录
export function login(data) {
    return service({
        url: "/admin/login",
        method: "POST",
        data,
    });
}
// 新增员工账号
export function employeeAdd(data) {
    return service({
        url: "/admin/add",
        method: "POST",
        data,
    });
}
// 删除员工账号
export function employeeDelete(data) {
    return service({
        url: "/admin/deleteByname",
        method: "POST",
        data,
    });
}
// 查询所有员工账号
export function employeeQuery(data) {
    return service({
        url: "/admin/selectall",
        method: "POST",
        data,
    });
}
// 修改登录密码
export function updatapassword(data) {
    return service({
        url: "/admin/updatapassword",
        method: "POST",
        data,
    });
}

























