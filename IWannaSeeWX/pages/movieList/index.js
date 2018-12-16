// pages/movieList/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0,
    title: '',
    movies: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (query) {
    query.id = parseInt(query.id)
    // console.log(query)
    this.setData({
      id: query.id,
      title: query.title
    })
    this.getAllMovies()
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
  getAllMovies() {
    let id = this.data.id
    let self = this
    wx.request({
      url: 'http://120.79.178.50:8080/movies/categories/' + id + '/some-movies?K=200&orderByRank=true',
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        // console.log(res.data.data.list)
        self.setData({
          movies: res.data.data.list
        })
      }
    })
  },
  junp2detail(e){
    console.log(e.currentTarget.dataset.id)
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/detail/index?id=' + id,
    })
  }
})