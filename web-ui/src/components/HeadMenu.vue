<template>
  <div>
    <el-menu
        :default-active="defaultActive"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        :ref="headerRef"
        active-text-color="#ffd04b"
        @select="changePage">
      <el-menu-item style="margin-left: 100px" index="home">首页</el-menu-item>
      <el-menu-item index="project">项目管理</el-menu-item>
      <el-menu-item index="log">系统日志</el-menu-item>
      <el-menu-item index="account|login|register">账户管理</el-menu-item>
      <div class="hp">
        <el-dropdown trigger="contextmenu">
          <el-avatar alt="None">{{ this.$userinfo.username }}</el-avatar>
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
import {ref} from "vue";
import {ElMessage} from "element-plus";

export default {
  name: "HeadMenu",

  data() {
    return {
      defaultActive: 'home',
      headerRef: ref()
    }
  },
  methods: {
    changePage(index) {
      let hasLogin = this.$userinfo.login
      let to = index
      // 登录后才解锁访问权限
      if (!hasLogin) {
        if (index !== 'home') {
          to = 'login'
        }
        this.$router.push(to)
        ElMessage("请先登录")
      }else {
        this.$router.push(to)
      }
    },
    logout(){
      // 清除cookie
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