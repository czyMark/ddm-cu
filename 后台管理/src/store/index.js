import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
import state from './state'
import mutations from './mutations'
import createPersistedstate from 'vuex-persistedstate'

export default new Vuex.Store({
    state,
    mutations,
    plugins: [
        createPersistedstate({
            key: "project",
            storage: window.sessionStorage,
            reducer(val)  {
                return {
                    menuSwitch: val.menuSwitch,
                    historyRecord: val.historyRecord,
                    userInfo: val.userInfo,
                    menuList: val.menuList,
                }
            }
        })
    ]
})
