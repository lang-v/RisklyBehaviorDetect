<template>
  <div v-loading="loading.on" :element-loading-text="loading.msg">
    <div class="horizontal-div">
      <div>
        <span> 类型：</span>
        <el-select-v2
            v-model="filters"
            :options="options"
            placeholder="选择日志类型"
            style="width: 200px"
            multiple
        />
      </div>
      <div style="margin-left: 30px">
        <span>时间范围:</span>
        <el-date-picker
            v-model="timeRange"
            arrow-control
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
        />
      </div>
      <div style="margin-left: 100px">
        <el-button type="primary" @click="onRetrieval">查询日志</el-button>
        <el-button @click="onReset">重置</el-button>
      </div>
    </div>
    <el-divider/>

    <!--    下面两块用来展示数据-->
    <div v-if="useTimeLine" style="width: 100%">

    </div>
    <div v-else style="width: 100%;padding: 20px;">
      <el-table :data="data.slice((currentPage-1)*pageSize,currentPage*pageSize)" width="100%">
        <el-table-column
            label="序号"
            type="index"
            width="80"
            align="center"/>
        <el-table-column prop="logId" label="ID" width="80"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column prop="resourceId" label="关联项目" width="100"/>
        <el-table-column prop="content" label="内容" width="450"/>
        <el-table-column prop="createTime" label="触发时间"/>
      </el-table>
      <el-pagination style="margin-left: 45%;margin-top: 100px"
                     page-size="10"
                     :current-page="currentPage.value"
                     :total="data.length"
                     :hide-on-single-page="true"
                     @current-change="handlerPageChange"
                     background layout="prev, pager, next" />
    </div>
    <div>
      <!--    <el-empty v-show="logIsEmpty" :description="emptyMessage"/>-->
    </div>

  </div>
</template>

<script>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";

export default {
  name: "RetrievalView",
  data() {
    return {
      loading: {
        on: false,
        msg: '加载中...'
      },
      logIsEmpty: true,
      emptyMessage: "暂无数据",
      useTimeLine: false,
      filters: [],
      options: [
        {label: 'login', value: 'Login'},
        {label: 'register', value: 'Register'},
        {label: 'reset', value: 'Reset'},
        {label: 'update', value: 'Update'},
        {label: 'projectcreate', value: 'ProjectCreate'},
        {label: 'addnumber', value: 'ProjectAddMember'},
        {label: 'removenumber', value: 'ProjectRemoveMember'},
        {label: 'alarm', value: 'Alarm'}],
      // 时间范围
      timeRange: ref(),
      // 最终查询到的数据
      data: [],
      currentPage: ref(1),
      pageSize: 10
    }
  },

  methods: {
    onRetrieval() {
      // 查询日志
      console.log(this.filters, this.timeRange)
      this.loading.on = true
      ElMessage('查询中')

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
        } else {
          ElMessage(res.data.message)
        }
      })
          .catch((err) => {
            this.loading.on = false
            console.log(err)
            ElMessage('未知错误')
          })

    },
    onReset() {
      delete this.timeRange[0]
      delete this.timeRange[1]
      this.filters = []
      this.data = []
    },
    handlerPageChange(currentPage) {
      this.currentPage = currentPage
    }
  }
}
</script>

<style scoped>
div.horizontal-div {
  display: flex;
  justify-content: flex-start;
}
</style>