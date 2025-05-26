import permissionMenu from "./../views/employee/permissionMenu"
export default {
    menuSwitch: true,
    historyRecord: [
        {
            path: '/userList',
            alwaysShow: true,
            meta: { title: '用户列表', icon: 'el-icon-tickets'},
            component: () => import('../views/userList')
        },
    ],
    userInfo: {},
    // menuList: [],
    menuList: permissionMenu,
    
    deviceType: '',
}