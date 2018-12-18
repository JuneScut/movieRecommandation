
// export const baseURL = 'http://localhost:8080'; // 本地测试
export const baseURL = 'http://120.79.178.50:8080'; // 阿里云服务器
export const GET = 'GET';
export const POST = 'POST';
export const DELETE = 'DELETE';
export const PUT = 'PUT';
// export const PATCH = 'PATCH'; // wx.request不支持patch方法

// wx支持promise的方式
function promiseRequest(options = {}) {
  const {
    url,
    data,
    header,
    method,
    dataType,
    responseType,
    success,
    fail,
    complete
  } = options;

  return new Promise((result, reject) => {
    wx.request({
      url,
      data,
      header,
      method,
      dataType,
      responseType,
      success(r) {
        result(r);
      },
      fail(err) {
        reject(err);
      },
      complete
    });
  });
}

// ***************** API *********************

/*
wx openid 和 unionid
- unionid: 微信用户的唯一标识，即[对于一个微信用户，unionid是唯一的]，在微信平台下公众号、订阅号、小程序上使用
- openid: 对于同一微信用户，在微信平台下公众号、订阅号、小程序三者[各自]持有一个openid，即至少有3个openid...
*/

// miniprogram login
// 小程序登录，使用小程序openid标识用户唯一性
export const mplogin = () => {
  wx.login({
    success(res) {
      if (res.code) {
        promiseRequest({
          url: baseURL + '/users/wx-mp-login',
          method: POST,
          data: {
            'code': res.code
          }
        }).then((result) => {
          // TODO: 保存user_id，openid应该不用保存
          console.log(result)
          console.log(result.data)
          wx.setStorage({
            key: 'userId',
            data: result.data.user_id
          })
          var data = result.data.data
          console.log(data)

        }).catch((err) => {
          console.log(err);
        });
      } else {
        console.log('登录失败! ' + res.errMsg);
      }
    }
  })
};

// 微信全平台登录，使用unionid标识用户身份
export const unionlogin = () => {
  var requestParams = {
    'code': '',
    'userInfoData': null
  };

  wx.getSetting({
    success(res) {
      if (res.authSetting['scope.userInfo']) {
        // 用户已授权过[用户信息]
        wx.getUserInfo({
          success(res) {
            requestParams.userInfoData = res;

            console.log(requestParams)

            wx.login({
              success(res) {
                if (res.code) {
                  requestParams.code = res.code;

                  promiseRequest({
                    url: baseURL + '/users/wx-login',
                    method: POST,
                    data: requestParams
                  }).then((result) => {
                    // TODO: 保存user_id等信息
                    console.log(result)
                    console.log(result.data)
                    var data = result.data.data
                    console.log(data)
                  }).catch((err) => {
                    console.log(err);
                  });
                } else {
                  console.log('登录失败! ' + res.errMsg);
                }
              }
            })
          }
        })
      }
    }
  })
};

// 用户收藏一部电影
export const addMovieToCollection = (user_id, movid_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/collections',
    method: POST,
    data: {
      'movie_id': movid_id
    }
  });
};

// 用户取消收藏一部电影
export const removeMovieFromCollection = (user_id, movid_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/collections?movie_id=' + movid_id,
    method: DELETE
  })
};

// wx.request的delete方法会把数据转换成ResponseBody?? 
// 不是想要的效果，所以采用上面那种拼接URL的方式
// export const removeMovieFromCollection = (user_id, movid_id) => {
//   return promiseRequest({
//     url: baseURL + '/users/' + user_id + '/collections',
//     method: DELETE,
//     data: {
//       'movie_id': movid_id
//     }
//   })
// };

// 获取用户收藏列表（movie_id的列表）
export const getUserCollections = (user_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/collections',
    method: GET
  })
};

// 获取用户收藏[电影]
// 默认参数（可省略）：page = 1, per_page = 10
export const getCollectedMovies = (user_id, page, per_page) => {
  page = page ? page : 1;
  per_page = per_page ? per_page : 10;
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/collections/movies',
    method: GET,
    data: {
      'page': page,
      'per_page': per_page
    }
  })
}

// 用户查询是否已收藏一部电影
export const checkACollectfedMovie = (user_id, movie_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/collections/' + movie_id,
    method: GET,
  })
}

// 用户添加喜好的电影类别
// favor_category_ids: 包含 category_id 的list
export const addFavorCategories = (user_id, favor_category_ids) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/favor-categories',
    method: POST,
    data: {
      'favor_category_ids': favor_category_ids
    }
  })
}

// 用户查询喜好的电影类别
export const getFavorCategories = (user_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/favor-categories',
    method: GET
  })
};

// 用户删除喜好的电影类别
export const removeFavorCategory = (user_id, category_id) => {
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/favor-categories?category_id=' + category_id,
    method: DELETE
  })
};

// 批量获取电影简要信息
export const getMoviesInfo = (page, per_page) => {
  page = page ? page : 1;
  per_page = per_page ? per_page : 10;
  return promiseRequest({
    url: baseURL + '/movies/info',
    method: GET,
    data: {
      'page': page,
      'per_page': per_page
    }
  })
};

// 获取一部电影详细信息
export const getAMovie = (movie_id) => {
  return promiseRequest({
    url: baseURL + '/movies/' + movie_id,
    method: GET
  })
};

// 获取电影豆瓣短评论
export const getShortComments = (movid_id) => {
  return promiseRequest({
    url: baseURL + '/movies/' + movid_id + '/short-comments',
    method: GET
  })
};

// 获取所有电影的数量
export const getMoviesCount = () => {
  return promiseRequest({
    url: baseURL + '/movies/count',
    method: GET
  })
};

// 获取所有电影类别
export const getAllCategories = () => {
  return promiseRequest({
    url: baseURL + '/movies/categories',
    method: GET
  })
};

// 获取该类别下K部的电影
// 默认参数（可选）: K = 5, orderByRank = true
// orderByRank = true 电影按douban_rank排序
// orderByRank = false 随机返回K部电影
export const getKMoviesUnderCategory = (category_id, K, orderByRank) => {
  K = K ? K : 5
  orderByRank = orderByRank ? orderByRank : false
  return promiseRequest({
    url: baseURL + '/movies/categories/' + category_id + '/some-movies',
    method: GET,
    data: {
      'K': K,
      'orderByRank': orderByRank
    }
  })
};

// 获取某一类别下的电影
// 默认参数（可省略）：page = 1, per_page = 10
export const getMoivesUnderCategory = (category_id, page, per_page) => {
  page = page ? page : 1
  per_page = per_page ? per_page : 10
  return promiseRequest({
    url: baseURL + '/movies/categories/' + category_id + '/movies',
    method: GET,
    data: {
      'page': page,
      'per_page': per_page
    }
  })
};


/* 
rating = {
  'user_id': 1,
  'movie_id': 2,
  'rating': 4 // 
  'comment': 'cool!'
}
*/
export const addARating = (rating) => {
  return promiseRequest({
    url: baseURL + '/movies/' + rating.movie_id + '/ratings',
    method: POST,
    data: rating
  })
}

export const getRatingsOfMovie = (movie_id, page, per_page) => {
  page = page ? page : 1
  per_page = per_page ? per_page : 10
  return promiseRequest({
    url: baseURL + '/movies/' + movie_id + '/ratings',
    method: GET,
    data: {
      'page': page,
      'per_page': per_page
    }
  })
}

export const getRatingsOfUser = (user_id, page, per_page) => {
  page = page ? page : 1
  per_page = per_page ? per_page : 10
  return promiseRequest({
    url: baseURL + '/users/' + user_id + '/ratings',
    method: GET,
    data: {
      'page': page,
      'per_page': per_page
    }
  })
}

export const removeARating = (user_id, movie_id) => {
  return promiseRequest({
    url: baseURL + '/movies/' + movie_id + '/ratings?user_id=' + user_id,
    method: DELETE
  })
}

export const updateARating = (rating) => {
  return promiseRequest({
    url: baseURL + '/movies/' + rating.movie_id + '/ratings',
    method: PUT,
    data: rating
  })
}

// 
// export const getAllCategories = () => {
//   return promiseRequest({
//     url: baseURL + '/movies/categories',
//     method: GET
//   })
// };

// ------------- debug -----------
export const testAPI = () => {
  mplogin()

  // var rating = {
  //   'user_id': 1,
  //   'movie_id': 100,
  //   'score': 5,
  //   'comment': 'hope'
  // }

  //updateARating(rating)
  //getRatingsOfMovie(1)
  //getRatingsOfUser(1)
  //addARating(rating)
  // removeARating(1, 100)
  //   .then((result) => {
  //     console.log(result);
  //     console.log(result.data);
  //     var data = result.data.data;
  //     console.log(data);
  //   }).catch((err) => {
  //     console.log(err)
  //   })
};

