// pages/mine/mine.js
import * as RestAPI from '../../apis/RestAPI';
import { $wuxDialog, $wuxSelect } from '../../components/dist/index'
import { getUserId, getUserInfo } from '../../utils/util.js'

Page({
  data: {
    userId: 11,
    showGetInfoButton: false,
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
    currentType: 0,
    userTags: [],
    favMovies: [],
    myComments: [],
    newTags: [],
    allTags: [],
    selectedTags: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(getUserId())
    if(getUserId()){
      this.setData({
        userId: getUserId(),
        userInfo: getUserInfo()
      })
    } else {
      wx.navigateTo({
        url: '/pages/login/login',
      })
    }
    this.getCollections()
    this.getComments()
    this.getUserTags()
    this.getTags()
    // 查看是否授权
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
    // RestAPI.mplogin()
    // this.setData({
    //   showGetInfoButton: false
    // })
  },
  changeType(e){
    console.log(e.currentTarget.dataset.type)
    if (e.currentTarget.dataset.type === '0') {
      this.getComments()
      this.setData({
        currentType: 0
      })
    } else if (e.currentTarget.dataset.type === '1'){
      this.getUserTags()
      this.setData({
        currentType: 1
      }) 
    } else {
      this.getCollections() 
      this.setData({
        currentType: 2
      })
    }
  },
  getUserTags(){
    let self = this
    console.log("getUserTags begin")
    wx.request({
      url: 'http://120.79.178.50:8080/users/'+ self.data.userId + '/favor-categories',
      success(res){
        console.log(res.data.data.list)
        self.setData({
          userTags: res.data.data.list
        })
      },
      fail(res){
        console.log(res)
      }
    })
  },

  getCollections(){
    let self = this;
    wx.request({
      url: 'http://120.79.178.50:8080/users/'+ self.data.userId + '/collections',
      success(res){
        let list = res.data.data.list
        let tempFavMovies = []
        for (let item of list){
          wx.request({
            url: 'http://120.79.178.50:8080/movies/'+item,
            success(res){
              let tempMovie = res.data.data;
              tempFavMovies.push(tempMovie)
              self.setData({
                favMovies: tempFavMovies
              })
            }
          })
        }
      }
    })
  },
  jump2Detail(e){
    console.log(e.currentTarget.dataset.id)
    let movieId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/detail/index?id=' + movieId,
    })
  },

  testAPI(e){
    RestAPI.testAPI()
  },
  deleteTag(e){
    let that = this
    console.log(e.currentTarget.dataset.id)
    let tagId = e.currentTarget.dataset.id
    $wuxDialog().confirm({
      resetOnClose: true,
      closable: true,
      title: '删除标签',
      content: '你确定要删除该标签吗？',
      onConfirm(e) {
        RestAPI.removeFavorCategory(that.data.userId, tagId).then(res => {
          that.getUserTags()
        })
      },
      onCancel(e) {
        
      }
    })
  },
  getComments(){
    // getRatingsOfUser = (user_id, page, per_page)
    // {pic_url, score, rating}
    let that = this
    RestAPI.getRatingsOfUser(this.data.userId, 1, 10).then(res => {
      console.log(res.data.data.list)
      that.setData({
        myComments: res.data.data.list
      })
      let arr = res.data.data.list
      for(let i=0; i<arr.length; i++){
        wx.request({
          url: 'http://120.79.178.50:8080/movies/' + arr[i].movie_id,
          success(res){
            let tempMovieUrl = res.data.data.pic_url;
            var temp = "myComments[" + i + "].pic_url";
            that.setData({
              [temp]: tempMovieUrl
            })
            // console.log(that.data.myComments)
          }
        })
      }
    })
  },
  deleteComment(e){
    console.log(e.currentTarget.dataset.id)
    let movieId = e.currentTarget.dataset.id
    let self = this;
    console.log(this)
    $wuxDialog().confirm({
      resetOnClose: true,
      closable: true,
      title: '删除评论',
      content: '你确定要删除该评论吗？',
      onConfirm(e) {
        RestAPI.removeARating(self.data.userId, movieId).then(res => {
          console.log(res)
          self.getComments()
        })
      },
      onCancel(e) {

      }
    })
    // $wuxDialog().confirm({
    //   resetOnClose: true,
    //   closable: true,
    //   title: '定制冰激凌',
    //   content: '你确定要吃我的冰淇淋吗？',
    //   onConfirm(e) {
    //     console.log('凭什么吃我的冰淇淋！')
    //   },
    //   onCancel(e) {
    //     console.log('谢谢你不吃之恩！')
    //   },
    // })

  },
  showAddTagPopup(){
    this.setData({
      popupVisible: true
    })
  },
  closeAddTagPopup(){
    this.setData({
      popupVisible: false
    })
  },
  getTags () {
    let self = this;
    wx.request({
      url: 'http://120.79.178.50:8080/movies/categories',
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        // console.log(res.data.data.list)
        self.setData({
          allTags: res.data.data.list
        })
        let arr = self.data.allTags
        for(let i=0; i<arr.length; i++){
          let temp = {}
          temp.title = arr[i].title
          temp.value = parseInt(arr[i].id)
          let tempAttrName = "allTags[" + i +"]"
          self.setData({
            [tempAttrName]: temp
          })
        }
      }
    })
  },
  onClick3() {
    let allSelections = this.data.allTags;
    let self = this
    $wuxSelect('#wux-select').open({
      value: this.data.newTags,
      multiple: true,
      toolbar: {
        title: '选择属于您的个性标签',
        confirmText: 'ok',
      },
      options: allSelections,
      onChange: (value, index, options) => {
        console.log('onChange', value)
        // this.setData({
        //   value3: value,
        //   title3: index.map((n) => options[n].title),
        // })
      },
      onConfirm: (value, index, options) => {
        console.log('onConfirm', value)
        self.setData({
          selectedTags: value
        })
        self.sendTags()
      },
    })
  },
  sendTags(){
    let tags = this.data.selectedTags;
    for (let i=0; i<tags.length; i++) {
      tags[i] = parseInt(tags[i])
    }
    let self = this
    console.log(tags)
    RestAPI.addFavorCategories(this.data.userId, tags).then(res => {
      self.getUserTags()
    })
  }
})