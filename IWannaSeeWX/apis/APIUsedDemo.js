import * as RestAPI from "../apis/RestAPI.js"

var pageObject = {
  testAPI: function (e) {
    // promise的调用方式
    /* 
    RestAPI.foo(a, b).then((result)={
      console.log(result)
      console.log(result.data)
      var data = result.data.data
      console.log(data)
    }).catch((err) => {
      console.log(err)
    })
    */

    // 以下，密集恐惧症患者请自动忽略
    
    // RestAPI.removeFavorCategory(1, 2)
    // RestAPI.removeMovieFromCollection(1, 200)
    //RestAPI.addMovieToCollection(1, 200)
    //RestAPI.getMoviesInfo()
    // RestAPI.getUserCollections(1)
    // RestAPI.getCollectedMovies(1)
    //RestAPI.checkACollectedMovie(1, 128)
    //RestAPI.addFavorCategories(1, [20, 1, 4])
    //RestAPI.removeFavorCategory(1, 20)
    // RestAPI.getFavorCategories(1)
    // RestAPI.getAMovie(100)
    // RestAPI.getShortComments(160)
    // RestAPI.getMoviesCount()
    //RestAPI.getAllCategories()
    // RestAPI.getKMoviesUnderCategory(10, 10)
    RestAPI.getMoivesUnderCategory(10)
      .then((result) => {
        console.log(result);
        console.log(result.data);
        var data = result.data.data;
        console.log(data);
      }).catch((err) => {
        console.log(err);
      });
  }
}

