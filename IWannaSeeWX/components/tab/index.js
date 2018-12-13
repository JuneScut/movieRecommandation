//组件的对外属性，是属性名到属性设置的映射表，属性设置中可包含三个字段， type 表示属性类型、 value 表示属性初始值、 observer 表示属性值被更改时的响应函数
Component({
  properties: {
    //标题列表
    tList: {
      type: Array,
      value: []
    },
    //当前tab index
    currentTab: {
      type: Number,
      value: 0,
      observer: function (newVal, oldVal) {
        this.setData({
          currentTab: newVal
        })
      }
    }
  },
  //组件的方法，包括事件响应函数和任意的自定义方法，关于事件响应函数的使用
  methods: {
    // 内部方法建议以下划线开头
    _swichNav: function (e) {
      //自定义组件触发事件时，需要使用 triggerEvent 方法，指定事件名、detail对象和事件选项
      this.triggerEvent('changeCurrent', {
        currentNum: e.currentTarget.dataset.current
      })
    }
  }
})