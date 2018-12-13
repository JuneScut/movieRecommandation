// pages/mine/mine.js
Page({
  data: {
    showGetInfoButton: true,
    userInfo: {
      nickName: '',
      gender: 0,
      city: '',
      province: '',
      avatarUrl: ''
    },
    statusType: [
      { name: "评论", page: 0 },
      { name: "收藏", page: 0 }],
    currentType: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let self = this;
    try {
      const res = wx.getStorageInfoSync()
      if (res.keys.indexOf("userInfo") !== -1 ){
        self.setData({
          showGetInfoButton: false
        })
      }
    } catch (e) {
      // Do something when catch error
    }
    wx.getStorage({
      key: 'userInfo',
      success(res) {
        self.setData({
          userInfo: res.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  onGotUserInfo(e) {
    if (e.detail.userInfo) {
      this.setData({
        showGetInfoButton: false,
        userInfo: e.detail.userInfo
      })
      wx.setStorage({
        key: 'userInfo',
        data: e.detail.userInfo
      })
    }
    try {
      const res = wx.getStorageInfoSync()
      console.log(res.keys)
      console.log(res.currentSize)
      console.log(res.limitSize)
    } catch (e) {
      // Do something when catch error
    }
   
  },
  changeType(e){
    console.log(e.currentTarget.dataset.type)
    if (e.currentTarget.dataset.type === '0') {
      this.setData({
        currentType: 0
      })
    } else {
      this.setData({
        currentType: 1
      })
    }
  }
})