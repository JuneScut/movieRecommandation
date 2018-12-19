// pages/comment/index.js
import * as RestAPI from '../../apis/RestAPI';
import {getUserId} from '../../utils/util.js'
import { $wuxToast } from '../../components/dist/index'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    movieId: 1,
    title: '大话西游',
    userId: 11,
    pic_url: 'https://img1.doubanio.com/view/photo/m/public/p2454008807.webp',
    detail: {
      ellipsis: true, /* 文字是否收起，默认收起*/
      title: "肖申克的救赎",
      douban_score: "9.6",
      douban_votes: "1226754",
      douban_quote: "希望让人自由。",
      pic_url: "https://img1.doubanio.com/view/photo/m/public/p2454008807.webp"
    },
    starFlag: 5,
    score: 0,
    commentContent: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (query) {
    this.setData({
      userId: getUserId()
    })
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
  getContent(e) {
    //console.log(e)
    this.setData({
      commentContent: e.detail.value
    })
  },
  commitComment(){
    console.log(parseInt(this.selectComponent('#self-rate').data.rate))
    console.log(this.data.commentContent)
    /* 
    rating = {
      'user_id': 1,
      'movie_id': 2,
      'rating': 4 // 
      'comment': 'cool!'
    }
    */
    let self = this
    let rating = {}
    rating.user_id = this.data.userId
    rating.movie_id = parseInt(this.data.movieId)
    rating.score = parseInt(this.selectComponent('#self-rate').data.rate)
    rating.comment = this.data.commentContent
    console.log(rating)
    RestAPI.addARating(rating).then(res => {
      console.log(res)
      self.showToast()
      setTimeout(wx.navigateTo({
        url: '/pages/detail/index?id='+movieId,
      }), 3000)
    })
  },
  showToast() {
    $wuxToast().show({
      type: '成功',
      duration: 1500,
      color: '#fff',
      text: '你的评论已经添加成功，赶紧去看看吧',
      success: () => console.log('已完成')
    })
  },
})