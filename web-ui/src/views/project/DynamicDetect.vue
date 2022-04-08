<template>
  <div v-loading="loading.on" :element-loading-text="loading.msg" style="padding: 20px;">
    <el-container>
      <el-header>
        <el-row>
          <el-col>
            <el-select style="float: left; margin-bottom: 20px" v-model="selected" placeholder="选择一个项目">
              <el-option v-for="item in projects" :key="item.resourceId" :label="item.name" :value="item.url"/>
            </el-select>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-row style="margin-top: 20px" >
              <el-row style="width: 100%;" justify="start" :gutter="20" align="middle">
                <el-col :span="6">
                  <span style="color: black;"> FPS:{{ currentVideo.info.fps }}</span>
                </el-col>
                <el-col :span="6">
                  <span style="color: black;">STATE:{{ currentVideo.state }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: black;" id="datetime"/>
                </el-col>
              </el-row>

              <el-col>
                <video id="detect-video" autoplay ref="videoRef" crossorigin="anonymous" :src="videoRealUrl" controls
                       style="width: 100%;height: auto;max-height: 600px;min-height: 600px"
                       muted loop/>
              </el-col>
            </el-row>
          </el-col>

          <el-col :span="8">
            <div style="width: 100%;height: 100%" v-show="alarmShow">
              <!--          展示截图区域，若发生危险事件，将截图放到这-->
              <el-row align="middle" justify="center" :gutter="20" >
                <el-button @click="cancelAlert" type="primary">解除警报</el-button>
                <svg t="1649090056959" :color="alarmColor" fill="currentColor" class="icon" viewBox="0 0 1026 1024"
                     version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2205" width="200" height="200">
                  <path
                      d="M1004.657 801.716 602.263 91.599c-49.213-86.817-129.646-86.817-178.866 0L21.004 801.716c-49.207 86.906-8.949 157.798 89.388 157.798l804.877 0C1013.606 959.514 1053.825 888.622 1004.657 801.716zM544.635 832.216l-63.649 0 0-63.649 63.649 0L544.635 832.216zM544.635 641.27l-63.649 0L480.986 259.377l63.649 0L544.635 641.27z"
                      p-id="2206"></path>
                </svg>

                <img crossorigin="anonymous" alt="loading" style="width: 100%;height: 100%" id="detect-frame"/>

                <span>疑似检测到危险行为请及时处理:<br/></span>
                <span v-for="item in detectActionCodes" :key="item">
              {{ actionsArr[item[0]] + ":" + item[1] + "%" }}<br/>
            </span>
              </el-row>
            </div>

          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import actions from "../../data/actions.json"
import {ref} from "vue";

export default {
  name: "DynamicDetect",
  props: {},
  data() {
    return {
      resourceId: ref(-1),
      projects: [],
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
      currentVideo: {
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
      videoRef: ref(),
      preIndex: 0,
      actionsArr: JSON.parse(JSON.stringify(actions)),
      alarmColor: 'rgba(0,0,0,0)',
      alarmShow: false,
      colorTimer: undefined,
      detectActionCodes: [],
      hadSendEmail: false,
      selected: ref(),
    }
  },
  watch: {
    selected: {
      handler(nv) {
        this.projects.forEach(project => {
          if (project.url+"" === nv+"") {
            this.resourceId = project.resourceId
          }
        })
        console.log('resourceId',this.resourceId)
        console.log('load Video',nv)
        this.loadVideoInfo()
      }
    },
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
      axios(config)
          .then((res) => {
            this.loading.on = false
            if (res.data.code === 200) {
              this.projects = res.data.data.sort((a, b) => {
                return a.createTime - b.createTime
              })
              if (this.resourceId !== -1) {
                // this.selector
                this.projects.map(v => {
                  if (v.resourceId + "" === this.resourceId + "") {
                    this.selected = v.url
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
    loadVideoInfo() {
      // 加载视频的信息，fps 宽高 源文件名 检测信息路径
      // path = path.replace("http://localhost:8082", "/api2")
      if (this.selected === '') {
        return
      }
      this.loading.on = true
      axios({
        method: 'get',
        url: "/bpi/detect",
        params: {
          source: this.selected
        }
      }).then((res) => {
        this.loading.on = false
        if (res.status === 200) {
          this.currentVideo = res.data
          this.getVideoUrl()
          this.loadDetectInfo()
        } else {
          ElMessage('发生错误')
          console.log(res)
        }
      }).catch((err) => {
        this.loading.on = false
        ElMessage('未知错误')
        console.log(err)
      })
    },
    getVideoUrl() {
      this.videoRealUrl = "/bpi/video?source=" + this.currentVideo.data.video_path;
    },
    loadDetectInfo() {
      // path = path.replace("http://localhost:8082", "/api2")
      axios({
        method: 'get',
        url: '/bpi/video?source=' + this.currentVideo.data.video_preds,
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
    report(ac) {
      // 上报每次都要上报，但是一个小时只能发送一次邮件
      if (this.resourceId === -1) return
      const data = {
        token: this.$userinfo.token,
        notifyTime: (new Date()).getTime(),
        resourceId: this.resourceId,
        actionCodes: ac.map((item) => {
          return item[0]
        }),
        needEmail: !this.hadSendEmail,
        url: "http://localhost:8080/project/detect?resourceId="+this.resourceId
      }
      const json = JSON.stringify(data)
      const config = {
        method: 'post',
        url: '/api/projects/report',
        headers: {
          'Content-Type': 'application/json'
        },
        data: json
      }
      console.log(config)
      axios(config)
          .then((res) => {
            if (res.data.code === 200) {
              console.log('成功上报')
              if (data.needEmail) {
                this.hadSendEmail = true
                setTimeout(() => {
                  // 一个小时只发送一次邮件
                  this.hadSendEmail = false
                }, 60 * 1000 * 16)
              }
            } else {
              console.log(res)
              ElMessage(res.data.message)
            }
          })
          .catch((err) => {
            console.log(err)
            ElMessage('上报失败，请及时联系管理员')
          })
    },
    alarm(level, ac, lastNotifyTime) {
      if (this.colorTimer !== undefined) return
      switch (level) {
        case 2: {
          this.captureVideoFrame()
          this.alarmShow = true
          this.alarmColor = 'rgba(255,0,0,255)'
          this.detectActionCodes = ac
          break
        }
        case 1: {
          this.captureVideoFrame()
          this.alarmShow = true
          this.alarmColor = 'rgb(241,237,17)'
          this.detectActionCodes = ac
          break
        }
        default: {
          // 行为检测出来没有风险，啥也不做
          break
        }
      }
      this.report(ac)
      // 一段时间内警报是重复的，不需要多次提交
      this.colorTimer = setTimeout(() => {
      }, lastNotifyTime)
      console.log('行为:', ac, 'behavior level:', level)
    },
    cancelAlert() {
      // 解除警报
      this.alarmShow = false
      this.alarmColor = 'rgba(0,0,0,0)'
      this.detectActionCodes = []
    },
    captureVideoFrame() {
      const video = document.getElementById('detect-video')
      video.setAttribute('crossorigin', 'anonymous')
      let canvas = document.createElement('canvas');
      canvas.width = video.videoWidth
      canvas.height = video.videoHeight
      canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height)
      const img = document.getElementById('detect-frame')
      img.setAttribute('crossorigin', 'anonymous'); // 注意设置图片跨域应该在图片加载之前
      img.src = canvas.toDataURL('image/jpeg', 1)
    },
    onProgress() {
      // 刚开始以为这个方法可以完美获取到视频的播放进度
      if (this.detectInfo.preds_intime.length === 0) return
      // 获取播放进度
      let index = this.findCurrentIndex(this.preIndex, document.getElementById('detect-video').currentTime)
      if (index === -1) return;
      let ac = this.detectInfo.preds_intime[index][1]
      let level = this.getBehaviorLevel(ac)
      // 两秒进行一次异常检测
      if (level >= 1)
        this.alarm(level, ac, 2)
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
      const levelTwo = [4/*摔倒*/, 6/*跳跃*/]
      const levelThree = [51/*射击*/, 57/*扔东西*/, 63 /*打架*/]
      let level = 0
      for (let i = 0; i < actionCodes.length; i++) {
        let item = actionCodes[i][0]
        if (levelTwo.includes(item)) {
          level = 1
        } else if (levelThree.includes(item)) {
          return 2
        }
      }
      return level
    }
  },
  created() {
    this.resourceId = this.$route.query.resourceId
    this.loadAllProject()
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