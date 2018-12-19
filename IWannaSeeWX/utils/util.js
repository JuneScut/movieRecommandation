import { mplogin } from '../apis/RestAPI.js'
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

const getUserId = () => {
  return wx.getStorageSync('userId')
}
const getUserInfo = () => {
  return wx.getStorageSync('WxUserInfo')
}


module.exports = {
  formatTime: formatTime,
  getUserId: getUserId,
  getUserInfo: getUserInfo
}
