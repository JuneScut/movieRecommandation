
Page({
  data: {
    ellipsis: true, /* 文字是否收起，默认收起*/
      title: "肖申克的救赎",
      country:"美国",
      year:"1994",
      diretors:" 弗兰克·德拉邦特 Frank Darabont",
      stars:" 蒂姆·罗宾斯 / 摩根·弗里曼 / 鲍勃·冈顿 / 威廉姆·赛德勒 / 克兰西·布朗 ",
      release_date: " 1994-09-10(多伦多电影节) / 1994-10-14(美国)",
      running_time:"142分钟",
      short_intro: "20世纪40年代末，小有成就的青年银行家安迪（蒂姆·罗宾斯 Tim Robbins 饰）因涉嫌杀害妻子及她的情人而锒铛入狱。在这座名为肖申克的监狱内，希望似乎虚无缥缈，终身监禁的惩罚无疑注定了安迪接下来灰暗绝望的人生。未过多久，安迪尝试接近囚犯中颇有声望的瑞德（摩根·弗里曼 Morgan Freeman 饰），请求对方帮自己搞来小锤子。以此为契机，二人逐渐熟稔，安迪也仿佛在鱼龙混杂、罪恶横生、黑白混淆的牢狱中找到属于自己的求生之道。他利用自身的专业知识，帮助监狱管理层逃税、洗黑钱，同时凭借与瑞德的交往在犯人中间也渐渐受到礼遇。表面看来，他已如瑞德那样对那堵高墙从憎恨转变为处之泰然，但是对自由的渴望仍促使他朝着心中的希望和目标前进。而关于其罪行的真相，似乎更使这一切朝前推进了一步…… ",
      type:"动作，冒险，热血",
      douban_score:"9.6",
      douban_votes:"1226754",
      douban_quote:"希望让人自由。",
    pic_url:"https://img3.doubanio.com//view//movie_poster_cover//spst//public//p2378133884.jpg",
      assess: [
       {
        name:"amy",
        content:"不需要女主角的好电影"},
       {
          name: "tom",
          content: "有种鸟是关不住的."
        },
        {
          name: "deny",
          content: "超级喜欢超级喜欢,不看的话人生不圆满."
        }
         ],
    screen: {
      minHeight: 'auto'
    }
  },
  ellipsis: function () {
    var value = !this.data.ellipsis;
    this.setData({
      ellipsis: value
    })
  },
  onLoad: function (query) {
    var self = this;
    // 设置页面高度，避免底部出现白色区域。
    wx.getSystemInfo({
      success: function (info) {
        self.setData({
          'screen.minHeight': info.windowHeight + 'px'
        });
      }
    })
  
  },
   });
