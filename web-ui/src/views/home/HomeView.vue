<template>
  <div class="background" style="width: 100%;height: 100%;">
    <!--    <div class="float-notice">-->
    <!--      <el-row justify="center" style="margin-top: 50px">-->

    <!--        <svg t="1649090056959" color="red" fill="currentColor" class="icon" viewBox="0 0 1026 1024"-->
    <!--             version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2205" width="200" height="200">-->
    <!--          <path-->
    <!--              d="M1004.657 801.716 602.263 91.599c-49.213-86.817-129.646-86.817-178.866 0L21.004 801.716c-49.207 86.906-8.949 157.798 89.388 157.798l804.877 0C1013.606 959.514 1053.825 888.622 1004.657 801.716zM544.635 832.216l-63.649 0 0-63.649 63.649 0L544.635 832.216zM544.635 641.27l-63.649 0L480.986 259.377l63.649 0L544.635 641.27z"-->
    <!--              p-id="2206"></path>-->
    <!--        </svg>-->

    <!--        <span style="margin-top: 50px;color: white">疑似检测到危险行为请及时处理<br/></span>-->
    <!--      </el-row>-->
    <!--    </div>-->
    <el-row>

      <el-col :span="14">

        <img src="./screen.png" style="margin-top: 100px;width: 60%;height: auto;" alt=""/>
      </el-col>
      <el-col :span="10">
        <div class='Img'>
          <el-row style="margin-bottom: 100px;margin-top: 50px" align="bottom">
            <el-col>
              <span style="font-size: xxx-large;font-family: 微软雅黑,sans-serif;color: black">高空危险行为预警系统</span>
            </el-col>
          </el-row>
          <el-row style="padding: 0" align="middle" justify="start">
            <el-col style="width: 50%">
              <span style="font-size: unset;color: black">本系统基于深度学习实现多人行为识别并对危险行为进行警报；</span>
              <p/>
              <span style="font-size: unset;color: black">采用SlowFast算法，基于AVA数据集训练可识别80种行为。</span>
              <p/>
              <span style="font-size: unset;color: black">快快注册体验吧！</span>
            </el-col>
          </el-row>
          <el-row v-show="this.$userinfo.hasLogin === 'false'" style="margin-top: 100px;height: 30%;" align="middle"
                  justify="center">
            <el-col :span="8">
              <button class="ring" @click="$router.push('/login')">登录</button>
            </el-col>
            <el-col :span="8">
              <button class="ring-primary" @click="$router.push('/register')">注册</button>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <div style="float: bottom;bottom: 2rem;position: absolute;left: 50%">
      <span style="font-size: small">版权所有 | 高空危险行为预警系统</span>
    </div>
  </div>
</template>

<script>

export default {
  name: 'HomeView',
}
</script>

<style>
div.background {
  width: 100%;
  height: 100%;
  background-position: center center;
  background-size: cover;
  overflow: hidden;
  background-color: white;
  /*background-image: url('bg.jpg');*/
}

.Img {
  margin-top: 100px;
  margin-right: 50px;
  padding-bottom: 100px;
  background-position: center center;
  background-size: cover;
  overflow: hidden;
  border-radius: 5px;
  background-color: #00000044;
  /*background-image: url('bg.jpg');*/
}

button.ring {
  background-color: white;
  font-size: large;
  color: #000;
  border: none;
  height: 40px;
  width: 200px;
  border-radius: 20px;
}

button.ring:hover {
  background-color: lightgray;
  color: black;
}

button.ring-primary {
  background-color: deepskyblue;
  border: none;
  color: black;
  font-size: large;
  height: 40px;
  width: 200px;
  border-radius: 20px;
}

button.ring-primary:hover {
  background-color: dodgerblue;
  color: black;
}

div.float-notice {
  height: 400px;
  width: 400px;
  position: absolute;
  right: 4rem;
  top: 20rem;
  /*background-color: #00000067;*/
}

</style>