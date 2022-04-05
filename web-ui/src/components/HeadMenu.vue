<template>
  <el-row style="height: 100%;width: 100%" align="middle" justify="end" :gutter='10'>
    <el-col :span="30" :pull="5">
      <div style="color: white">
        <span style="font-family: 华文行楷,system-ui;font-size: xx-large">高空危险行为预警系统</span>
      </div>
    </el-col>
      <el-col v-show="this.$userinfo.hasLogin === 'false'" :span="3">
        <el-row align="middle" justify="center" :gutter="20">
          <el-col :span="1">
            <el-button type="primary" @click="toLogin">登录</el-button>
          </el-col>
          <el-col :span="1" :offset="10" :pull="3" @click="toRegister">
            <el-button>注册</el-button>
          </el-col>
        </el-row>
      </el-col>

      <el-col v-show="this.$userinfo.hasLogin === 'true'" :span="1" :offset="1">
        <div>
          <el-dropdown trigger="hover">
            <el-avatar>{{ this.$userinfo.username }}</el-avatar>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-col>

  </el-row>

</template>

<script>
import {getCurrentInstance} from "vue";
// import {ElMessage} from "element-plus";
import ck from "@/cookies/utils";

export default {
  name: "HeadMenu",
  setup() {
    const globalProperties = getCurrentInstance().appContext.config.globalProperties; // 获取全局挂载
    return {globalProperties}
  },
  data() {
    return {}
  },
  methods: {
    toLogin() {
      this.$router.push('login')
    },
    toRegister() {
      this.$router.push('register')
    },
    logout() {
      console.log(this.$userinfo)
      // 清除cookie
      ck.clearCookies()
      ck.refreshCookies(this.globalProperties)
      // 退出直接切到主页
      this.$router.push('/home')
    }
  }
}
</script>

<style lang="scss">
* {
  padding: 0;
  margin: 0;
}

body {
  margin: 0;
}


</style>