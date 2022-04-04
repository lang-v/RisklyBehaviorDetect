<template>
  <div v-loading="" :element-loading-text="loading.msg" element-loading-background="rgba(0,0,0,0.8)"
       style="padding: 20px;width: 100%;">
    <el-col style="width: 100%;">
      <div style="width:80%;height: auto;margin-top: 30px">
        <el-row style="margin-bottom: -3rem">
          <el-col style="height:1.5rem;padding-left: 10px;padding-right: 10px">
            <span style="color: white;float: left;"> FPS:{{ currentProject.info.fps }}</span>
            <span style="color: white;alignment: center">STATE:{{ currentProject.state }}</span>
            <span style="color: white;float: right" id="datetime"/>
          </el-col>
        </el-row>
        <el-col>
          <video id="detect-video" :src="videoRealUrl" controls
                 style="max-width: 1280px;max-height: 720px;width: 100%;height: auto" muted loop/>
        </el-col>
      </div>


      <div style="padding: 0px;alignment: center;width: 20%">
        <!--          展示截图区域，若发生危险事件，将截图放到这-->
          <el-col>
            <svg t="1649090056959" :color="alarmColor" fill="currentColor" class="icon" viewBox="0 0 1026 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2205" width="200" height="200">
              <path
                  d="M1004.657 801.716 602.263 91.599c-49.213-86.817-129.646-86.817-178.866 0L21.004 801.716c-49.207 86.906-8.949 157.798 89.388 157.798l804.877 0C1013.606 959.514 1053.825 888.622 1004.657 801.716zM544.635 832.216l-63.649 0 0-63.649 63.649 0L544.635 832.216zM544.635 641.27l-63.649 0L480.986 259.377l63.649 0L544.635 641.27z"
                  p-id="2206"></path>
            </svg>
          </el-col>
      </div>

    </el-col>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import actions from "../../../data/actions.json"

export default {
  name: "DynamicDetect",
  props: {
    project: Object
  },
  data() {
    return {
      loading: {
        on: true,
        msg: '加载中...'
      },
      // 项目的检测信息
      detectInfo: {
        fps: 1,
        height: 1,
        width: 1,
        preds_intime: []
      },
      currentProject: {
        info: {
          fps: 12,
        },
        state: 'READY',
        data: {
          video_path: '',
          video_preds: ''
        }
      },
      videoRealUrl: '',
      preIndex: 0,
      actionsArr: JSON.parse(JSON.stringify(actions)),
      alarmColor: 'rgba(0,0,0,233)',
      colorTimer: undefined
    }
  },
  methods: {
    loadProjectInfo() {
      // 加载视频的信息，fps 宽高 源文件名 检测信息路径
      // path = path.replace("http://localhost:8082", "/api2")
      axios({
        method: 'get',
        url: "/bpi/detect",
        params: {
          source: this.project.url
        }
      }).then((res) => {
        if (res.status === 200) {
          this.currentProject = res.data
          this.getVideoUrl()
          this.loadDetectInfo()
        } else {
          ElMessage('发生错误')
          console.log(res)
        }
      }).catch((err) => {
        ElMessage('未知错误')
        console.log(err)
      })
    },
    getVideoUrl() {
      this.videoRealUrl = "http://localhost:8082/video?source=" + this.currentProject.data.video_path;
    },
    loadDetectInfo() {
      // path = path.replace("http://localhost:8082", "/api2")
      axios({
        method: 'get',
        url: '/bpi/video?source=' + this.currentProject.data.video_preds,
      }).then((res) => {
        if (res.status === 200) {
          this.detectInfo = res.data
          const v = document.getElementById('detect-video')
          v.ontimeupdate = ev => {
            this.onProgress(ev)
          }
          // this.resolveDetectInfo(res.data)
        } else {
          ElMessage('发生错误')
          console.log(res)
        }
      }).catch((err) => {
        ElMessage('未知错误')
        console.log(err)
      })
    },
    resolveDetectInfo(data) {
      // 将获取的检测信息组装成时间段信息
      // 废弃，感觉不需要转
      console.log(data)
    },
    report(actionCodes) {
      console.log('report riskily behavior', actionCodes)
      // todo
    },
    alarm(level, ac) {
      // debugger
      console.log('alarm', level, ac)
      this.report(ac)
      if (this.colorTimer !== undefined) clearTimeout(this.colorTimer)
      switch (level) {
        case 1: {
          this.alarmColor = 'rgba(255,0,0)'
          break
        }
        case 2: {
          this.alarmColor = 'rgb(241,237,17)'
          break
        }
        default: {
          // 行为检测出来没有风险，啥也不做
          break
        }
      }
      if (level !== 0) {
        // 3秒后消失
        this.colorTimer = setTimeout(() => {
          this.alarmColor = 'rgba(0,0,0,0)'
        }, 3000 * level)
      }
      console.log('行为:', ac, 'behavior level:', level)
    },
    onProgress(event) {
      // 刚开始以为这个方法可以完美获取到视频的播放进度
      // debugger
      if (this.detectInfo.preds_intime.length === 0) return
      // 获取播放进度
      let index = this.findCurrentIndex(this.preIndex, event.timeStamp / 1000)
      if (index === -1) return;
      let ac = this.detectInfo.preds_intime[index][1]
      let level = this.getBehaviorLevel(ac)
      if (level >= 1)
        this.alarm(level, ac)
    },
    findCurrentIndex(preIndex, time) {
      // let index = 0
      if (time === 0)
        return 0
      let arr = this.detectInfo.preds_intime
      for (let i = preIndex; i < arr.length; i++) {
        let item = arr[i]
        if (time <= item[0]) {
          return i - 1;
        }
      }
      // 一般来说进度都是往前的，所以大概率在前面的循环就能找到,没找到的话就把前面的重新找一遍
      for (let i = 1; i < preIndex; i++) {
        let item = arr[i]
        if (time <= item[0]) {
          return i - 1;
        }
      }
      // 没找到，说明结束了
      return -1
    },

    // 返回 0-2；数值越高越危险
    // actionsCodes : [
    //     [1,93],
    //     [2,67]
    // ]
    getBehaviorLevel(actionCodes) {
      console.log('actionCodes', actionCodes)
      const levelTwo = [4/*摔倒*/, 6/*跳跃*/]
      const levelThree = [51/*射击*/, 57/*扔东西*/, 63 /*打架*/]
      let level = 0
      for (let i = 0; i < actionCodes.length; i++) {
        let item = actionCodes[i][0]
        console.log('code:', item)
        if (levelTwo.includes(item)) {
          level = 1
        } else if (levelThree.includes(item)) {
          return 2
        }
      }
      return level
    }
  },
  setup() {
  },
  created() {
    this.loadProjectInfo()
    console.log(this.videoRealUrl)
    setInterval("document.getElementById('datetime').innerHTML=new Date().toLocaleString();", 1000);
  }
}

// eslint-disable-next-line no-unused-vars
// function onProgressProxy(event) {
//   this.onProgress(event)
// }
</script>

<style scoped>

</style>