//index.js
//获取应用实例
const app = getApp()
import * as RestAPI from '../../apis/RestAPI.js'
import {getUserId} from '../../utils/util.js'
Page({
  data: {
    categoryMovies: [],
    swipers: [
      {id: 2, url: '/images/duye.jpg'},
      {id: 3, url: '/images/longmao.jpg'},
      {id: 4, url: '/images/haiwang.jpg'}
    ],
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    recommandByKNN: [],
    newMovies: [{
      imgUrl: '/images/drawning.jpg',
      title: '云上日出',
      introduction: '1941年11月4日...'
    }, {
        imgUrl: '/images/Totoro.jpg',
        title: '龙猫',
        introduction: '小月的母亲...'
      }, {
        imgUrl: '/images/einstein.jpg',
        title: '狗十三',
        introduction: '13岁的少女李玩...'
      }],
    showLogin: false
  },
  onLoad: function () {
    let userId = getUserId()
    console.log(userId)
    if(userId){
      this.setData({
        userId: userId
      })
    }
    if(!userId){
      wx.navigateTo({
        url: '/pages/login/login',
      })
    }
    this.getRecommandByKNN()
    this.getAllCategories()
    this.getNewMovies()
  },
  navigateToMore(event){
    let id = event.currentTarget.dataset.categoryId
    let title = event.currentTarget.dataset.categoryTitle
    wx.navigateTo({
      url: '/pages/movieList/index?id=' + id + '&title=' + title + '&load_hot_movies=' + 0
    })
  },
  jump2Detail(e){
    console.log(e.currentTarget.dataset.id)
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/detail/index?id=' + id,
    })
  },
  // 获取所有类别
  getAllCategories(){ 
    let self = this;
    wx.request({
      url: 'http://120.79.178.50:8080/movies/categories',
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        self.setData({
          categoryMovies: []
        })
        for(let item of res.data.data.list){
          let temp = {}
          temp.title = item.title
          temp.id = item.id
          wx.request({
            url: 'http://120.79.178.50:8080/movies/categories/'+item.id+'/some-movies?K=3&orderByRank=true',
            success(res){
              temp.movies = res.data.data.list
              let catogryList = self.data.categoryMovies
              catogryList.push(temp)
              self.setData({
                categoryMovies: catogryList
              })
            }
          })
        }
      }
    })
  },
  // 这周影院有啥看
  getNewMovies(){
    let self = this;
    // wx.request({
    //   url: 'https://www.easy-mock.com/mock/5c14fc797aeb86217625d848/projectManage/getNewHotMovie#!method=get',
    //   header: {
    //     'content-type': 'application/json'
    //   },
    //   success(res) {
    //     self.setData({
    //       newMovies: res.data.data.list
    //     })
    //   }
    // })

    RestAPI.getHotMovies(1, 3)
    .then((result)=>{
      console.log(result)
      console.log(result.data)
      console.log(result.data.data)

      if (result.data.status === 200)
      {
        self.setData({
          newMovies: result.data.data.list
        })
      }
    }).catch((err)=>{
      console.log(err)
    })
  },
  navigateToMoreHotMovies(event) {
    wx.navigateTo({
      url: '/pages/movieList/index?load_hot_movies=' + 1 + '&title=Hot Movies'
    })
  },
  getRecommandByKNN() {
    let that = this
    RestAPI.recommend_user_knn(this.data.userId, 3).then((res) => {
      let arr = res.data.data.recommended_movie_ids
      arr.forEach((item, index) => {
        if (index < 3) {
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
  }
})
