<template>
  <div>
    <img alt="Vue logo" src="../assets/logo.png">
    <el-table :data="[]"></el-table>
    <el-button type="success">测试按钮</el-button>
  </div>

</template>

<script>
export default {
  name: "LoginView",
  props: {
    id: String
  }
}
</script>

<style scoped>

</style>