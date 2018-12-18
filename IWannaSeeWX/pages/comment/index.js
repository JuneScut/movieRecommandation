// pages/comment/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    movieId: 1,
    title: '大话西游',
    pic_url: 'https://img1.doubanio.com/view/photo/m/public/p2454008807.webp',
    detail: {
      ellipsis: true, /* 文字是否收起，默认收起*/
      title: "肖申克的救赎",
      douban_score: "9.6",
      douban_votes: "1226754",
      douban_quote: "希望让人自由。",
      pic_url: "https://img1.doubanio.com/view/photo/m/public/p2454008807.webp"
    },
    starFlag: 5
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (query) {
    if (query) {
      this.setData({
        movieId: query.id,
        title: query.title,
        pic_url: query.image
      })
    }
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
  changeOne: function () {
    var that = this;
    that.setData({
      starFlag: 1
    });
  },
  changeTwo: function () {
    var that = this;
    that.setData({
      starFlag: 2
    });
  },
  changeThree: function () {
    var that = this;
    that.setData({
      starFlag: 3
    });
  },
  changeFour: function () {
    var that = this;
    that.setData({
      starFlag: 4
    });
  },
  changeFive: function () {
    var that = this;
    that.setData({
      starFlag: 5
    });
  }
})