<template>
  <div id="app">
    <Header :current-page-info="currentPageInfo" :userinfo="userinfo" :logout="logout"
            :on-page-changed="onPageChanged"/>
    <router-view/>
  </div>
</template>

<script>
import Header from "@/components/HeadMenu";
import {reactive} from "vue";
import {useRouter} from "vue-router";

export default {
  components: {
    Header
  },
  data() {

    let router = useRouter()

    let currentPageInfo = reactive({
      index: "home"
    })

    let userinfo = reactive({
      username: "",
      userId: "",
      email: "",
      token: "",
      login:false
    })

    // 退出登录
    const logout = () => {
      if (!userinfo.login) {
        console.log("发起退出登录，但是并未登录")
        return
      }
      userinfo.username = "None"
      userinfo.email = ""
      userinfo.userId = ""
      userinfo.token = ""
      console.log("退出登录")
    }

    // menu 选中回调
    const onPageChanged = (index) => {
      if (currentPageInfo.index !== index) {
        console.log(index)
        if (index === 'account' && !userinfo.login) {
          router.push("login")
        }

        router.push(index)
      }
    }

    return {
      userinfo,
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
