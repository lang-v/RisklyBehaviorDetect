<template>
  <div v-loading="loading.on" :element-loading-text="loading.msg" style="padding:20px">

    <el-row>
      <el-col>
        <el-select style="float: left; margin-bottom: 20px" v-model="selectedProjectIndex" placeholder="选择一个项目">
          <el-option v-for="(item,i) in projects" :key="item.resourceId" :label="item.name" :value="i"/>
        </el-select>
      </el-col>
    </el-row>
    <el-tabs type="border-card" style="height: 100%">
      <el-tab-pane label="成员列表">
        <ol>
          <li v-for="(item,i) in members" :key="i">
            {{ item.userid }}
          </li>
        </ol>
      </el-tab-pane>

      <el-tab-pane label="管理成员">
        <el-transfer
            v-model="notMembers"
            :data="allUsers"
            :props="{key:'userid',label:'userid'}"
            :button-texts="['添加成员','移除成员']"
            :titles="['已有成员名单','其他人员名单']"
        />
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script>
import {ElMessage} from "element-plus";
import {ref} from "vue";
export default {
  name: "MemberManager",
  data() {
    return {
      loading: {
        on: false,
        msg: '加载中...'
      },
      projects: [],
      resourceId:ref(-1),
      selectedProjectIndex: ref(),
      allUsers: [],
      // transfer 左侧值
      members: [],
      // transfer 右侧值
      notMembers:[],
    }
  },
  watch:{
    selectedProjectIndex:{
      handler(nv) {
        // debugger
        this.members = this.projects[nv].members.map(v=>{
          return {userid:v.user_id}
        })
        // 下面这一步就是用allUsers 减去 members
        const arr = this.allUsers.filter(value => !this.members.includes(value))

        // 此时arr 还是对象数组然而 transfer 只需要 key arr 即字符串数组
        this.notMembers = arr.map(v=>v.userid)

        // 得到非成员 key 列表
        // this.notMembers = arr
        console.log('not members',this.notMembers)
      }
    }
  },
  methods: {
    loadAllProject() {
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
      this.$axios(config)
          .then((res) => {
            this.loading.on = false
            if (res.data.code === 200) {
              this.projects = res.data.data.sort((a, b) => {
                return a.createTime - b.createTime
              })
              if (this.resourceId !== -1) {
                this.projects.forEach((v,i) => {
                  if (v.resourceId + "" === this.resourceId + "") {
                    this.selectedProjectIndex = i
                    return 0
                  }
                })
              }
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
    loadAllUser() {
      this.loading.on = true
      this.$axios({
        method: 'post',
        url: "/api/account/query_all",
        data: {
          token: this.$userinfo.token,
          user_id: this.$userinfo.userid
        }
      }).then((res) => {
        this.loading.on = false
        if (res.status === 200) {
          this.loadAllProject()
          this.allUsers = res.data.data.map(v=>{
            return {userid:v.first}
          })
        } else {
          ElMessage('发生错误')
          console.log(res)
        }
      }).catch((err) => {
        this.loading.on = false
        ElMessage('未知错误')
        console.log(err)
      })
    }
  },
  created() {
    this.resourceId = this.$route.query.resourceId
    this.loadAllUser()
  }
}
</script>

<style scoped>

</style>