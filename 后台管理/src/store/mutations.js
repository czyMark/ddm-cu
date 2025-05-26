export default {
    change_menuSwitch(state, val){
        state.menuSwitch = val
    },
    add_historyRecord(state, val){
        let isRepeat = false
        state.historyRecord?.forEach(item=>{
            if(item.path === val.path) isRepeat = true
            if(val.path.indexOf('/orderDetail') !== -1 && item.path.indexOf('/orderDetail') !== -1) isRepeat = true
        })
        !isRepeat &&  state.historyRecord.push(val)
    },
    reduce_historyRecord(state, index){
        console.log(state.historyRecord)
        state.historyRecord.splice(index, 1)
    },
    clear_historyRecord(state, index){
        state.historyRecord = []
    },
    change_userInfo(state, val){
        state.userInfo = val
    },
    change_menuList(state, val){
        state.menuList = val
    },
    change_deviceType(state, val){
        state.deviceType = val
    },
}