<template>
  <div class="sign_in_box">
    <h4>注册账户</h4>
    <input class="input-common" placeholder="输入用户ID (16为数字或字母):">
    <input class="input-common" placeholder="输入用户名(字母):">
    <input class="input-common" placeholder="输入密码:">
    <input class="input-common" placeholder="输入邮箱:">

    <input class="validation-code" pattern="[0-9]{6}" placeholder="输入验证码">
    <button class="send-code" style="color: white">发送验证码</button>
    <p/>
    <span style="font-size: small;font-family: -apple-system">Tips:登录解锁全部功能</span>
  </div>


</template>

<script>
export default {
  name: "SignUp"
}
</script>

<style scoped>
div.sign_in_box {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: #42b983;
  width: 30%;
  height: 500px;
  padding: 20px 20px;
  border-radius: 5px;
  margin-left: 15px;
  box-shadow: darkgrey 10px 10px 15px 5px
}

input.input-common {
  margin-top: 20px;
  box-sizing: border-box;
  border-radius: 4px;
  border-bottom-style: none;
  border-color: transparent;
  width: 70%;
  height: 30px;
  outline: none;
}

input.validation-code {
  vertical-align:middle;
  box-sizing: border-box;
  border-bottom-left-radius: 4px;
  border-top-left-radius: 4px;
  border-bottom-style: none;
  border-color: transparent;
  width: 45%;
  padding: 0;
  margin: 20px 0 0;
  height: 30px;
  outline: none;
}

button.send-code {
  padding: 0;
  font-family: Apple,sans-serif;
  font-style: normal;
  margin: 20px 0 0;
  vertical-align:middle;
  box-sizing: content-box;
  background-color: deepskyblue;
  border-bottom-right-radius: 4px;
  border-top-right-radius: 4px;
  border: none transparent;
  width: 25%;
  line-height: 100%;
  height: 30px;
  outline: none;
}

</style>