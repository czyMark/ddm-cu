import Vue from 'vue'
import VueRouter from 'vue-router'

const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function (location) {
  return routerPush.call(this, location).catch(err => {})
};


Vue.use(VueRouter)

/* Layout */
import Layout from './../components/layout'

export const constantRoutes = [
  {
    path: '/',
    redirect: '/userList',
    hidden: true,
  },
  {
    path: '/login',
    name: 'login',
    hidden: true,
    meta: { title: 'login', icon: 'login', affix: false },
    component: () => import('../views/login.vue')
  },
  {
    id: 1,
    path: '/employee',
    component: Layout,
    alwaysShow: true,
    meta: { title: '员工列表', icon: 'el-icon-tickets'},
    component: () => import('../views/employee')
  },
  {
    path: '/employee/detail',
    component: Layout,
    alwaysShow: false,
    meta: { title: '员工详情', icon: 'el-icon-tickets'},
    component: () => import('../views/employee/detail.vue')
  },
  {
    id: 2,
    path: '/userList',
    component: Layout,
    alwaysShow: true,
    meta: { title: '用户列表', icon: 'el-icon-tickets'},
    component: () => import('../views/userList')
  },
  {
    id: 3,
    path: '/drawRequest',
    component: Layout,
    alwaysShow: true,
    meta: { title: '用户提现申请', icon: 'el-icon-tickets'},
    component: () => import('../views/drawRequest')
  },
  {
    id: 4,
    path: '/releaseDeposit',
    component: Layout,
    alwaysShow: true,
    meta: { title: '押金解冻列表', icon: 'el-icon-tickets'},
    component: () => import('../views/releaseDeposit')
  },
  {
    id: 5,
    path: '/setAdmin',
    component: Layout,
    alwaysShow: true,
    meta: { title: '设置管理员', icon: 'el-icon-tickets'},
    component: () => import('../views/setAdmin')
  },
  {
    id: 6,
    path: '/orderList/yahoo',
    alwaysShow: true,
    component: Layout,
    meta: { title: '雅虎订单列表', icon: 'el-icon-tickets'},
    component: () => import('../views/orderList/yahooList.vue'),
  },
  {
    id: 7,
    path: '/orderList/mercari',
    component: Layout,
    alwaysShow: true,
    meta: { title: '煤炉订单列表', icon: 'el-icon-tickets'},
    component: () => import('../views/orderList/mercariList.vue'),
  },
  {
    id: 77,
    path: '/orderList/allOrderList',
    component: Layout,
    alwaysShow: true,
    meta: { title: '订单列表', icon: 'el-icon-tickets'},
    component: () => import('../views/orderList/allOrderList.vue'),
  },
  
  {
    path: '/orderList/orderDetail',
    component: Layout,
    alwaysShow: false,
    meta: { title: '订单详情', icon: 'el-icon-tickets'},
    component: () => import('../views/orderList/orderDetail.vue'),
  },
  {
    id: 8,
    path: '/swiper',
    component: Layout,
    alwaysShow: true,
    meta: { title: '轮播图管理', icon: 'el-icon-tickets'},
    component: () => import('../views/swiper'),
  },
  {
    path: '/swiper/detail',
    component: Layout,
    alwaysShow: false,
    meta: { title: '轮播图管理', icon: 'el-icon-tickets'},
    component: () => import('../views/swiper/detail.vue'),
  },
  {
    id: 9,
    path: '/notice',
    component: Layout,
    alwaysShow: true,
    meta: { title: '公告管理', icon: 'el-icon-tickets'},
    component: () => import('../views/notice'),
  },
  {
    path: '/notice/detail',
    component: Layout,
    alwaysShow: false,
    meta: { title: '公告管理', icon: 'el-icon-tickets'},
    component: () => import('../views/notice/detail.vue'),
  },
  {
    id: 10,
    path: '/question',
    component: Layout,
    alwaysShow: true,
    meta: { title: '常见问题', icon: 'el-icon-tickets'},
    component: () => import('../views/question')
  },
  {
    path: '/question/detail',
    component: Layout,
    alwaysShow: false,
    meta: { title: '问题详情', icon: 'el-icon-tickets'},
    component: () => import('../views/question/detail.vue'),
  },
  {
    id: 11,
    path: '/setting',
    component: Layout,
    alwaysShow: true,
    meta: { title: '基础设置', icon: 'el-icon-tickets'},
    component: () => import('../views/setting')
  },
  {
    id: 12,
    path: '/cookie',
    component: Layout,
    alwaysShow: true,
    meta: { title: '账户设置', icon: 'el-icon-tickets'},
    component: () => import('../views/cookie')
  },
  {
    id: 13,
    path: '/rate',
    component: Layout,
    alwaysShow: true,
    meta: { title: '汇率设置', icon: 'el-icon-tickets'},
    component: () => import('../views/rate')
  },
  {
    id: 14,
    path: '/classes',
    component: Layout,
    alwaysShow: true,
    meta: { title: '汇率设置', icon: 'el-icon-tickets'},
    component: () => import('../views/classes')
  },
]

const router = new VueRouter({
  mode: 'hash', // require service support
  // base: '',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

// router.beforeEach((to, from, next) => {
//   next()
// })

export default router
