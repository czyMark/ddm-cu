import request from './request.js'

const api = {}

// 登录
api.select = params => request('POST', '/goods/select', params)


export default api
