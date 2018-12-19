// pages/firstPage/index.js
import * as RestAPI from '../../apis/RestAPI';
import {getUserId} from '../../utils/util.js'
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
    
    RestAPI.addFavorCategories(11,tags).then(res => {
        wx.setStorage({
          key: 'noFirstTime',
          data: true
        })
    })
  },
})