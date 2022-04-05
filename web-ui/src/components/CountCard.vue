<template>
  <el-row class="count-card common">
    <el-col align="left">
      <el-icon>
        <svg t="1648988027785" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
             p-id="11152" width="200" height="200">
          <path
              d="M904.386185 323.911419a34.133333 34.133333 0 0 1-24.135911 58.312362l-82.400002-0.048272-126.761807 126.761808L624.409612 835.592742a34.133333 34.133333 0 0 1-57.926187 19.308729L349.260221 637.678268 132.037018 854.901471 59.629284 879.037383l24.135911-72.407734L300.988398 589.406445 83.765195 372.183242a34.133333 34.133333 0 0 1 19.308729-57.926187l326.655426-46.678853L556.491157 140.816395l-0.048272-82.400002A34.133333 34.133333 0 0 1 614.755247 34.280482l289.630938 289.630937z"
              fill="#02B980" p-id="11153"></path>
        </svg>
      </el-icon>
      <b style="margin-left: 20px;">{{ item.type }}</b>
    </el-col>
    <el-col align="center">
      <b style="font-family: monospace;font-size: xxx-large;color: #3c8dbc">{{ item.count }}</b>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CountCard",
  props: {
    item: Object
  }
}
</script>

<style scoped>
.count-card {
  border-radius: 5px;
  background-color: #b6e1ee;
  padding: 20px 10px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px 0 #00000033
}

.common {
  min-width: 380px;
  transition: all .5s;
}

/*.common:hover {*/
/*  margin-top: -5px;*/
/*}*/

</style>