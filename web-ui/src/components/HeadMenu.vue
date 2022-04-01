<template>
  <div>
    <el-menu
        :default-active="currentPageInfo.index"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="onPageChanged">
      <el-menu-item style="margin-left: 100px" index="/">首页</el-menu-item>
      <el-menu-item index="project">项目管理</el-menu-item>
      <el-menu-item index="log">系统日志</el-menu-item>
      <el-menu-item index="account">账户管理</el-menu-item>
      <div class="hp">
        <el-dropdown trigger="contextmenu">
        <el-avatar alt="None">{{ userinfo.username }}</el-avatar>
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
// import {useRouter} from "vue-router";

export default {
  name: "HeadMenu",
  props:{
    currentPageInfo:Object,
    userinfo:Object,
    logout:Function,
    onPageChanged:Function
  },

  setup() {

    // const menuIndexes = ['home', 'project', 'event_log', 'account'];
    // console.log(menuIndexes)
    // let router = useRouter()
    // console.log(router)
    // console.log(this.router)
    // var currentPath = this.$router.path

    // const activeIndex2 = ref(this.props.path())
    // const handleSelect = (key) => {
    //   // 页面选中
    //   this.props.onPageChanged(key)
    //   console.log("选中菜单：" + key)
    // }
    // return {handleSelect}
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