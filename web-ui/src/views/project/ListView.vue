<template>
  <div v-loading="loading.on" :element-loading-text="loading.msg" >
    <div v-if="projectInfoList.length === 0">
      <el-empty description="暂无数据"></el-empty>
    </div>
    <div v-else>
      <el-row :gutter="10" type="flex">
        <el-col :span="3.5" v-for="item in projectInfoList" :key="item">
          <ProjectInfo :info="item"/>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
import ProjectInfo from '../../components/ProjectInfo'
import axios from "axios";
import {ElMessage} from "element-plus";


export default {
  name: "ProjectList",
  components:{
    ProjectInfo
  },
  data() {
    return {
      loading: {
        on: false,
        msg: '加载中...'
      },
      info:{
        resourceId:1,
        name:'项目1',
        type:'LOCALFILE',
        createTime:1649059712000,
        status:'Ready',
        url:'movie.mp4'
      },
      sourceList:[],
      projectInfoList:[]
    }
  },
  methods:{
    loadProjects() {
      let data = {
        token: this.$userinfo.token
      }
      const json = JSON.stringify(data)
      const config = {
        method: 'post',
        url: '/api/projects/query_projects',
        headers: {
          'Content-Type': 'application/json'
        },
        data: json
      }
      axios(config)
          .then((res) => {
            this.loading.on = false
            if (res.data.code === 200) {
              this.sourceList = res.data.data
              this.resolveData()
            } else {
              console.log(res.data)
              ElMessage('加载失败')
            }
          })
          .catch((err) => {
            this.loading.on = false
            console.log(err)
            ElMessage('未知错误')
          })
    },
    resolveData() {
      // 拉到服务器数据后就在这里进行处理，便于后面的列表展示
      let arr = this.sourceList.map(((value) => {
        return {
          resourceId:value.resourceId,
          name: value.name,
          type: value.type,
          createTime: value.createTime,
          status: 'Ready',
          url: value.url
        }
      })).sort((a,b)=>{
        return a.createTime - b.createTime
      })
      this.projectInfoList = arr
    },
    refresh() {
      this.loading.on = true
      this.loadProjects()
    }
  },
  created() {
    this.refresh()
  }
}
</script>

<style scoped>

</style>