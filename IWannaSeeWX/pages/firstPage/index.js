// pages/firstPage/index.js
import * as RestAPI from '../../apis/RestAPI';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tags: [{
      id: 11, 
      title: "冒险"
    }],
    tagStatus: [],
    choosedTags: [],
    showLogin: false
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 查看是否授权
    var that = this;
    // 查看是否授权
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            success: function (res) {
              //从数据库获取用户信息
              // that.queryUsreInfo();
              // 用户已经授权过
              that.setData({
                showLogin: false
              })
              // 判断是否选择过标签 其实这个字段存放再数据库应该比较合理
              try {
                const res = wx.getStorageInfoSync()
                console.log(res.keys)
                if (res.keys.indexOf('noFirstTime') !== -1) {
                  // 已经选择过了
                  wx.reLaunch({
                    url: '/pages/home/index'
                  })
                }
              } catch (e) {
                // Do something when catch error
              }

            }
          });
        } else {
          that.setData({
            showLogin: true
          })
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.getTags()
    let temp = []
    let len = this.data.tags.length;
    for (let i = 0; i < len; i++)
      temp[i] = false;
    this.setData({
      tagStatus: temp
    })
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
  chooseTag(e){
    let id = e.currentTarget.dataset.id
    let temp = this.data.tagStatus
    temp[id] = !temp[id]
    this.setData({
      tagStatus: temp
    })
    let temp2 = this.data.choosedTags;
    if (temp[id]){
      temp2.push(this.data.tags[id].id)
    } else {
      let index = temp2.indexOf(id)
      temp2.splice(index, 1)
    }
    this.setData({
      choosedTags: temp2
    })
    console.log(this.data.choosedTags)
  },
  getTags(){
    let self = this;
    wx.request({
      url: 'http://120.79.178.50:8080/movies/categories', 
      header: {
        'content-type': 'application/json' 
      },
      success(res) {
        // console.log(res.data.data.list)
        self.setData({
          tags: res.data.data.list
        })
      }
    })
  },
  sendTags(){
    let self = self;
    let tags = this.data.choosedTags;
    
    RestAPI.addFavorCategories(4,tags).then(res => {
        wx.setStorage({
          key: 'noFirstTime',
          data: true
        })
    })
  },
  bindGetUserInfo: function (e) {
        if (e.detail.userInfo) {
            //用户按了允许授权按钮
            var that = this;
            //插入登录的用户的相关信息到数据库
            // wx.request({
            //     url: getApp().globalData.urlPath + 'hstc_interface/insert_user',
            //     data: {
            //         openid: getApp().globalData.openid,
            //         nickName: e.detail.userInfo.nickName,
            //         avatarUrl: e.detail.userInfo.avatarUrl,
            //         province:e.detail.userInfo.province,
            //         city: e.detail.userInfo.city
            //     },
            //     header: {
            //         'content-type': 'application/json'
            //     },
            //     success: function (res) {
            //         //从数据库获取用户信息
            //         that.queryUsreInfo();
            //         console.log("插入小程序登录用户信息成功！");
            //     }
            // });
          // 授权成功后，跳转进入小程序标签选择
          // wx.reLaunch({
          //   url: '/pages/home/index'
          // })
          this.setData({
            showLogin: false
          })
        } else {
            //用户按了拒绝按钮
            wx.showModal({
                title:'警告',
                content:'您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
                showCancel:false,
                confirmText:'返回授权',
                success:function(res){
                    if (res.confirm) {
                        console.log('用户点击了“返回授权”')
                    } 
                }
            })
        }
    }
    
})