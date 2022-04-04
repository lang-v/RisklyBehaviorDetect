<template>
  <div>
    <el-menu
        class="el-menu-demo"
        default-active="/home"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        :ref="headerRef"
        @select="changePage">
      <div style="margin-top: 15px;color: white">
        <span style="font-family: 华文行楷,system-ui;font-size: xx-large">高空危险行为预警系统</span>
      </div>
      <el-menu-item style="margin-left: 20px" index="/home">首页</el-menu-item>
      <el-menu-item index="/project">项目管理</el-menu-item>
      <el-menu-item index="/log">系统日志</el-menu-item>
      <el-menu-item align="right" index="/account">账户管理</el-menu-item>
      <div class="hp">
        <el-dropdown :trigger="this.$userinfo.hasLogin ? 'hover' : 'contextmenu'" >
          <el-avatar >{{ this.$userinfo.username }}</el-avatar>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-menu>
  </div>
</template>

<script>
import {getCurrentInstance, ref} from "vue";
import {ElMessage} from "element-plus";
import ck from "@/cookies/utils";

export default {
  name: "HeadMenu",
  setup(){
    const globalProperties = getCurrentInstance().appContext.config.globalProperties; // 获取全局挂载
    return {globalProperties}
  },
  data() {
    return {
      defaultActive: 'home',
      headerRef: ref(),
    }
  },
  methods: {
    changePage(index) {
      let hasLogin = this.$userinfo.hasLogin
      let to = index
      // 登录后才解锁访问权限
      if (!hasLogin) {
        if (index !== '/home') {
          to = '/login'
          this.$router.push(to)
          ElMessage("请先登录")

        }else {
          this.$router.push(to)
        }
      }else {
        this.$router.push(to)
      }
    },
    logout(){
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

.el-menu-demo {
  position: relative;

  .hp {
    position: absolute;
    right: 1rem;
    bottom: 0.5rem;
  }
}
</style>