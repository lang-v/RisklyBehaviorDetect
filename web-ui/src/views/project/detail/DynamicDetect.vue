<template>
  <div v-loading="" :element-loading-text="loading.msg">
    <div>
      <span>选择项目</span>
      <el-select-v2
          v-model="selectedProject"
          :options="projectList"
          placeholder="选择输入源/检测项目"
          style="width: 200px"
          multiple
      />
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import axios from "axios";
// import {ElMessage} from "element-plus";

export default {
  name: "DynamicDetect",
  props:{
    project:Object
  },
  data() {
    return {
      loading: {
        on: true,
        msg: '加载中...'
      },
      playOptions: {},
      projectList: [{label:'test',value:'hhh'}],
      selectedProject: [],
      // 里面存了项目信息，成员列表，项目上报记录(报警)
      data: ref(),
      // 项目的检测信息
      detectInfo: {},
      currentProject: {}
    }
  },
  methods: {
    loadProjectInfo(path) {
      axios({
        method: 'get',
        url: '/api2/detect',
        params: {
          source: path
        }
      }).then((res) => {
        if (res.status === 200) {
          // 等待视频加载就绪才开始展示
          this.currentProject = res.data
        }
      })
    }
  }
}
</script>

<style scoped>

</style>