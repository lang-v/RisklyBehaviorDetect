<template>
  <div class="horizontal-div">

  <el-menu
      default-active="/log/retrieval"
      active-text-color="#ffd04b"
      background-color="#545c64"
      class="el-menu-vertical-demo"
      text-color="#fff"
      style="width: 10%;height: 100%;"
      :router="$router"
      @open="handleOpen"
      @close="handleClose"
  >
    <el-menu-item index="/log/retrieval">
      <el-icon>
        <svg t="1648972694193" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4241" width="200" height="200"><path d="M946.3 904.7L869 827.4c33.6-54 26.9-126-20-172.9-27.4-27.4-63.2-41-99-41s-71.7 13.7-99 41c-54.7 54.7-54.7 143.4 0 198.1 27.4 27.4 63.2 41 99 41 25.6 0 51.3-7 73.8-21l77.2 77.2c6.2 6.2 14.4 9.4 22.6 9.4s16.4-3.1 22.6-9.4c12.5-12.4 12.5-32.6 0.1-45.1z m-196.4-75c-20.3 0-39.4-7.9-53.8-22.3-29.7-29.7-29.7-77.9 0-107.6 14.4-14.4 33.5-22.3 53.8-22.3 20.3 0 39.4 7.9 53.8 22.3 29.7 29.7 29.7 77.9 0 107.6-14.3 14.4-33.4 22.3-53.8 22.3zM642.6 288.2c0-17.7-14.3-32-31.9-32H291.5c-17.6 0-31.9 14.3-31.9 32s14.3 32 31.9 32h319.1c17.7 0 32-14.3 32-32zM642.6 480.2c0-17.7-14.3-32-31.9-32H291.5c-17.6 0-31.9 14.3-31.9 32s14.3 32 31.9 32h319.1c17.7 0 32-14.3 32-32zM291.5 640.2c-17.6 0-31.9 14.3-31.9 32s14.3 32 31.9 32h193.6c17.6 0 31.9-14.3 31.9-32s-14.3-32-31.9-32H291.5z" p-id="4242"></path><path d="M805.9 546.5c-17.6 0-31.9-14.2-32-31.9l-1.4-354c-0.1-17.7-14.5-32-32.1-31.9l-545.8 2.5c-17.6 0.1-31.9 14.5-31.9 32.1l0.2 63.2c0.1 17.7-14.2 32-31.9 32.1-17.7 0.1-32-14.2-32.1-31.9l-0.2-63.2c-0.2-53 42.7-96.2 95.6-96.4l545.8-2.5c52.9-0.2 96.1 42.7 96.3 95.7l1.4 354c0.1 17.8-14.2 32.2-31.9 32.2zM580.8 866.1c0 17.6-14.2 31.9-31.8 32l-351.1 1.6c-52.9 0.2-96.1-42.7-96.3-95.7l-0.3-65.1c-0.1-17.7 14.2-32 31.9-32.1 17.7-0.1 32 14.2 32.1 31.9l0.3 65.1c0.1 17.7 14.5 32 32.1 31.9l351-1.6c17.6-0.1 32.1 14.2 32.1 32zM133.6 579.8c-17.7 0.1-32-14.2-32.1-31.8l-0.7-128.2c-0.1-17.7 14.2-32.1 31.9-32.2 17.7-0.1 32 14.2 32.1 31.8l0.7 128.2c0 17.7-14.2 32.1-31.9 32.2zM164.5 355.5l-64 0.3c-17.7 0.1-32-14.2-32.1-31.9v-0.1c-0.1-17.7 14.2-32 31.9-32.1l64-0.3c17.7-0.1 32 14.2 32.1 31.9v0.1c0 17.7-14.3 32-31.9 32.1z" p-id="4243"></path><path d="M166 675.7l-64 0.3c-17.7 0.1-32-14.2-32.1-31.9v-0.1c-0.1-17.7 14.2-32 31.9-32.1l64-0.3c17.7-0.1 32 14.2 32.1 31.9v0.1c0 17.7-14.2 32.1-31.9 32.1z" p-id="4244"></path></svg>
      </el-icon>
      <span>日志检索</span>
    </el-menu-item>
    <el-menu-item index="/log/analyse">
      <el-icon>
        <svg t="1648972647148" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3411" width="200" height="200"><path d="M819.2 256h-102.4v460.8h102.4z m-256-102.4H460.8v563.2h102.4zM307.2 358.4H204.8v358.4h102.4z m-204.8 409.6v102.4h819.2v-102.4z" fill="" p-id="3412"></path></svg>

      </el-icon>
      <span>日志分析</span>
    </el-menu-item>
  </el-menu>
  <router-view style="float: right;width: 84%;height: 100%;padding: 20px"/>
  </div>

</template>

<script>

export default {
  name: "EventLogView",
  components:{
  },
  created() {
    // 默认选中检索也
    this.$router.push('/log/retrieval')
  },
  setup() {
    const handleOpen = (key, keyPath) => {
      console.log(key, keyPath)
    }
    const handleClose = (key, keyPath) => {
      console.log(key, keyPath)
    }
    return {handleOpen,handleClose}
  }
}
</script>

<style scoped>
div.horizontal-div {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  height: 100%;
}
</style>