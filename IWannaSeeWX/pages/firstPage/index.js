// pages/firstPage/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tags: ["动漫","文艺","超级英雄","科幻","悬疑","喜剧"],
    tagStatus: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let temp = []
    let len = this.data.tags.length;
    for(let i=0;i<len;i++)
      temp[i] = false;
    this.setData({
      tagStatus: temp
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
  chooseTag(e){
    let id = e.currentTarget.dataset.id
    let temp = this.data.tagStatus
    temp[id] = !temp[id]
    this.setData({
      tagStatus: temp
    })
  }
})