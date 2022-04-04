<template>
  <div class="info-group" style="width: 200px;">
    <el-row gutter="20" align="left" @click="$router.push({name:'detect',query:{project:JSON.stringify(info)}})">
      <el-col align="left">
        <span style="font-size: larger">{{ info.name }}</span>
        <el-icon style="float: right">
          <svg t="1649056893010" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="3327" width="200" height="200">
            <path
                d="M956 686.5l-0.1-0.1-0.1-0.1C911.7 593 843.4 545 752.5 545s-159.2 48.1-203.4 141.3v0.1c-5.4 11.5-5.4 24.9 0 36.4C593.3 816 661.6 864 752.5 864s159.2-48.1 203.4-141.3c5.4-11.5 5.4-24.8 0.1-36.2zM752.5 800c-62.1 0-107.4-30-141.1-95.5C645 639 690.4 609 752.5 609c62.1 0 107.4 30 141.1 95.5C860 770 814.6 800 752.5 800z"
                p-id="3328"></path>
            <path
                d="M697 705a56 56 0 1 0 112 0 56 56 0 1 0-112 0zM136 232h704v253h72V192c0-17.7-14.3-32-32-32H96c-17.7 0-32 14.3-32 32v520c0 17.7 14.3 32 32 32h352v-72H136V232z"
                p-id="3329"></path>
            <path
                d="M724.9 338.1l-36.8-36.8c-3.1-3.1-8.2-3.1-11.3 0L493 485.3l-86.1-86.2c-3.1-3.1-8.2-3.1-11.3 0L251.3 543.4c-3.1 3.1-3.1 8.2 0 11.3l36.8 36.8c3.1 3.1 8.2 3.1 11.3 0l101.8-101.8 86.1 86.2c3.1 3.1 8.2 3.1 11.3 0l226.3-226.5c3.2-3.1 3.2-8.2 0-11.3z"
                p-id="3330"></path>
          </svg>
        </el-icon>
      </el-col>

      <el-col style="margin-top: 20px" align="left">
        <span class="second-text">类型：{{ info.type }}</span>
      </el-col>

      <el-col align="left">
        <span class="second-text">创建时间：{{ (new Date(info.createTime)).toLocaleString() }}</span>
      </el-col>

      <el-col align="left" style="margin-top: 20px">
        <span class="ring"></span>
        <span class="second-text">{{ info.status }}</span>
        <span class="second-text hidden-out"> {{ info.url }} </span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "ProjectInfo",
  props: {
    info: Object
  }
}
</script>

<style scoped>
div.info-group {
  height: auto;
  cursor: pointer;
  margin-bottom: 20px;
  padding: 16px 17px 20px 12px;
  border-top: 8px solid #373d41;
  box-shadow: 0 1px 3px 0 #00000033;
}

div.info-group:hover {
  border-top: 8px solid #3c8dbc;
}

span.second-text {
  font-size: smaller;
  color: darkgrey;
  vertical-align: top;
}

span.hidden-out {
  float: right;
  white-space:nowrap;/*强制单行显示*/
  text-overflow:ellipsis;/*超出部分省略号表示*/
  overflow:hidden;/*超出部分隐藏*/
  width: 60px;/*设置显示的最大宽度*/
  display:inline-block;
}

span.ring {
  width: 10px;
  height: 10px;
  display: inline-block;
  border-radius: 50%;
  margin-right: 10px;
  vertical-align: top;
  /*background-color: #42b983;*/
  background: #6FCE7F
}

</style>