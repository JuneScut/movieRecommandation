// pages/recommandation/recommandation.js
import * as RestAPI from '../../apis/RestAPI.js'
import {getUserId} from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: 11,
    recommandByTags: [],
    recommandByKNN: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(getUserId()){
      this.setData({
        userId: getUserId()
      })
    } else {
      wx.navigateTo({
        url: '/pages/login/login',
      })
    }
    console.log(this.data.userId)
    this.getRecommandByTags()
    this.getRecommandByKNN()
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

  getRecommandByTags(){
    // user_id, k_movies
    let that = this
    RestAPI.recommend_category_based(this.data.userId, 3).then((res) => {
      let arr = res.data.data.recommended_movie_ids
      arr.forEach((item, index) => {
        if (index < 3) {
          wx.request({
            url: 'http://120.79.178.50:8080/movies/' + item,
            success(res) {
              // console.log(res.data.data)
              let temp = "recommandByTags[" + index + "]";
              that.setData({
                [temp]: res.data.data
              })
            }
          })
        }
      })
    })
  },
  getRecommandByKNN() {
    let that = this
    RestAPI.recommend_user_knn(this.data.userId, 3).then((res) => {
      let arr = res.data.data.recommended_movie_ids
      arr.forEach((item, index) => {
        if(index < 3){
          wx.request({
            url: 'http://120.79.178.50:8080/movies/' + item,
            success(res) {
              // console.log(res.data.data)
              let temp = "recommandByKNN[" + index + "]";
              that.setData({
                [temp]: res.data.data
              })
            }
          })
        }
      })
    })
  },
  jump2Detail(e){
    console.log(e.currentTarget.dataset.id)
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/detail/index?id=' + id,
    })
  }
})