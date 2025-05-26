const permissionMenu = [

    {
        id: 1,
        label: '系统运营',
        path: '/employee'
    },

    {
        id: 2,
        label: '订单管理',
        path: '/employee',
        children: [
            {
                id: 3,
                label: '预约出价',
                path: '/orderList/yahoo?yahooType=100'
            },
            {
                id: 4,
                label: '竞拍中',
                path: '/orderList/yahoo?yahooType=1'
            },
            {
                id: 5,
                label: '待支付',
                path: '/orderList/yahoo?yahooType=2'
            },
            {
                id: 6,
                label: '已支付',
                path: '/orderList/yahoo?yahooType=2'
            },
            {
                id: 7,
                label: '待入库',
                path: '/orderList/yahoo?yahooType=4'
            },
            {
                id: 8,
                label: '已入库',
                path: '/orderList/yahoo?yahooType=5'
            },
            {
                id: 9,
                label: '申请发货',
                path: '/orderList/yahoo?yahooType=6'
            },
            {
                id: 10,
                label: '待付尾款',
                path: '/orderList/yahoo?yahooType=7'
            },
            {
                id: 11,
                label: '待发货',
                path: '/orderList/yahoo?yahooType=8'
            },
            {
                id: 12,
                label: '已发货',
                path: '/orderList/yahoo?yahooType=9'
            },
            {
                id: 13,
                label: '待签收',
                path: '/orderList/yahoo?yahooType=999'
            },

        ]
    },

    {
        id: 14,
        label: '仓库管理',
        path: '/employee',
        
        children: [
            {
                id: 15,
                label: '已入库',
                path: '/orderList/yahoo?yahooType=9999'
            },
            {
                id: 16,
                label: '已出库',
                path: '/orderList/yahoo?yahooType=99999'
            },

        ]
    },

    
    {
        id: 17,
        label: '用户管理',
        path: '/userList',
        children: [
            {
                id: 18,
                label: '用户列表',
                path: '/userList'
            },
        ]
    },
    

    
		
    {
        id: 19,
        label: '财务管理',
        path: '/employee',
        children: [
            {
                id: 20,
                label: '用户提现申请',
                path: '/drawRequest'
            },
            {
                id: 21,
                label: '押金解冻列表',
                path: '/releaseDeposit'
            },
        ]
    },

    {
        
        id: 22,
        label: '站点设置',
        path: '/employee',
    },

    {
        id: 23,
        label: '美术设计',
        path: '/employee',
        children: [
            {
                id: 24,
                label: '首页轮播图',
                path: '/swiper'
            },
            {
                id: 25,
                label: '公告',
                path: '/notice'
            },
            {
                id: 26,
                label: '常见问题',
                path: '/question'
            },
            {
                id: 27,
                label: '规则说明',
                path: '/setting'
            },
        ]
    },


    {
        
        id: 28,
        label: '系统管理',
        path: '/employee',
        children: [
            {
                id: 29,
                label: '员工列表',
                path: '/employee'
            },
            {
                id: 30,
                label: '规则设置',
                path: '/setting'
            },
            {
                id: 31,
                label: '基础设置',
                path: '/cookie'
            },
        ]

    },
    



    // {
    //     id: 5,
    //     label: '设置管理员',
    //     path: '/setAdmin'
    // },
    // {
    //     id: 6,
    //     label: '雅虎订单列表',
    //     path: '/orderList/yahoo'
    // },
    // {
    //     id: 7,
    //     label: '煤炉订单列表',
    //     path: '/orderList/mercari'
    // },
    // {
    //     id: 13,
    //     label: '汇率设置',
    //     path: '/rate'
    // },
]





export default permissionMenu







