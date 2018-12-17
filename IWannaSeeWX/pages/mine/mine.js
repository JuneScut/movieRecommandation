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
    // 查看是否授权
    var that = this;
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            success: function (res) {
              //从数据库获取用户信息
              // that.queryUsreInfo();
              // 用户已经授权过
              let objectData = JSON.parse(res.rawData)
              that.setData({
                showGetInfoButton: false,
                userInfo: objectData
              })
            }
          });
        }
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
    } else if (e.currentTarget.dataset.type === '1'){
      this.setData({
        currentType: 1
      }) 
    } else {
      this.setData({
        currentType: 2
      }) 
    }
  }
})