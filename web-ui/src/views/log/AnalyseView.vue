<template>
  <div style="padding: 20px" v-loading="loading.on" :element-loading-text="loading.msg" >
  <div v-if="cardInfo.length === 0">
    <el-empty description="暂无数据"></el-empty>
  </div>
  <div v-else>
    <el-row :gutter="10" type="flex">
      <el-col :span="3.5" v-for="item in cardInfo" :key="item">
        <CountCard :item="item"/>
      </el-col>
    </el-row>
  </div>

</div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import CountCard from "@/components/CountCard"
export default {
  name: "AnalyseView",
  components:{
    CountCard
  },
  data() {
    return {
      loading: {
        on: true,
        msg: '加载中...'
      },
      data: {},
      cardInfo:[]
    }
  },
  methods:{
    splitData(arr) {
      let dataArr = []
      arr.map(mapItem => {
        if (mapItem.type !== 'Register')
        if (dataArr.length === 0) {
          dataArr.push({ type: mapItem.type, count:1 })
        } else {
          let res = dataArr.some(item=> {//判断相同日期，有就添加到当前项
            if (item.type === mapItem.type) {
              item.count += 1
              return true
            }
          })
          if (!res) {//如果没找相同类型添加一个新对象
            dataArr.push({ type: mapItem.type, count:1 })
          }
        }
      })
      return dataArr
    },
    init(){
      // 这里负责把拉到的日志进行初始化处理
      this.cardInfo = this.splitData(this.data)
    }
  },
  created() {
    // 创建完成后开始加载所有日志数据，然后生成分析报告
    const body = {
      token: this.$userinfo.token
    }
    const json = JSON.stringify(body)
    let config = {
      method: 'post',
      url: '/api/log/all',
      headers: {
        'Content-Type': 'application/json'
      },
      data: json
    }
    axios(config).then((res) => {
      this.loading.on = false
      if (res.data.code === 200) {
        this.data = res.data.data
        this.init()
      } else {
        console.log(res)
        ElMessage(res.data.message)
      }
    })
        .catch((err) => {
          this.loading.on = false
          console.log(err)
          ElMessage('未知错误')
        })
  }
}
</script>

<style scoped>

</style>