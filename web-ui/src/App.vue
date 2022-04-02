<template>
  <div id="app">
    <Header :current-page-info="currentPageInfo" :userinfo="global.$userinfo" :logout="logout"
            :on-page-changed="onPageChanged"/>
    <router-view/>
  </div>
</template>

<script>
import Header from "@/components/HeadMenu";
import {useRouter} from "vue-router";
import {getCurrentInstance, reactive} from 'vue'
import {ElMessage} from "element-plus";

export default {
  components: {
    Header
  },
  setup() {
    const internalInstance = getCurrentInstance()
    //  全局变量
    let global=internalInstance.appContext.config.globalProperties;
    let router = useRouter()

    let currentPageInfo = reactive({
      index: "/"
    })

    // 退出登录
    const logout = () => {
      if (!global.$userinfo.login) {
        console.log("发起退出登录，但是并未登录")
        return
      }
      ElMessage('退出登录')
      global.$userinfo.username = "None"
      global.$userinfo.email = ""
      global.$userinfo.userId = ""
      global.$userinfo.token = ""
      console.log("退出登录")
    }

    // 头部菜单 选中回调
    const onPageChanged = (index) => {
      if (currentPageInfo.index !== index) {
        console.log(index)
        // console.log(!global.$userinfo.login)
        if (index !== 'home' && !global.$userinfo.login) {
          // 未登录状态下除了首页其他地方点击后都跳进登录页
          ElMessage('请先登录')
          router.push("login")
          return
        }
        router.push(index)
      }
    }

    return {
      global,
      currentPageInfo,
      logout,
      onPageChanged
    }
  }
}
</script>

<style lang="scss">
#app {

  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

</style>
