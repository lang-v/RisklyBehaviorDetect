<template>
  <div class="sign_in_box" id="signup">
    <h4>注册账户</h4>
    <form style="margin-top: 20px" onsubmit="return false">

      <div style="width: 70%;">
      <span v-show="nameAvailable.shown"
            :class="nameAvailable.error?'notAvailable':'available'">{{ nameAvailable.msg }}<p/></span>
      </div>
      <input class="input-common" style="margin-top: 0" pattern="[0-9a-zA-Z]{5,16}" placeholder="输入用户ID (5-16位数字或字母):"
             v-model="userId.id" required="required">

      <input class="input-common" pattern="[a-zA-Z]+" required="required" placeholder="输入用户名(字母):"
             v-model="signUpForm.username">
      <input type="password" pattern="[0-9a-zA-Z.,'/;`~@#$%^&*()_+-=]{8,16}" required="required" class="input-common"
             placeholder="输入密码（8-16位数字、字符或字母）:" v-model="signUpForm.password">
      <input class="input-common" pattern="^[0-9a-zA-Z][0-9a-zA-Z.]+@[0-9a-zA-Z]+\.[a-zA-Z]+$" required="required"
             placeholder="输入邮箱:"
             v-model="signUpForm.email">

      <div>
        <input class="validation-code" pattern="[0-9]{6}" title="格式不对" placeholder="输入验证码" required="required"
               v-model="signUpForm.code">
        <button class="send-code hover-active" type="button" :disabled="!sendButtonText.clickAble" @click="sendCode">
          {{ sendButtonText.msg }}
        </button>
      </div>

      <button class="sign-up hover-active" type="submit" @click="register">立即注册</button>
    </form>

    <p></p>
    <span style="font-size: xx-small">Tips:登录解锁全部功能</span>
  </div>
</template>

<script>
import {reactive, watch} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";


export default {
  name: "SignUpView",
  setup() {

    const userId = reactive({
      id: ""
    })
    const router = useRouter()
    const sendButtonText = reactive({
      msg: '发送验证码',
      clickAble: true
    })

    const signUpForm = reactive({
      // user_id: "",
      username: "",
      password: "",
      email: "",
      code: ""
    })

    const nameAvailable = reactive({
      msg: "",
      shown: false,
      error: false,
    })

    let timeout = {};

    // 避免抖动
    function debounce(fn, delay = 300) {   //默认300毫秒
      console.log(fn)
      if (timeout[fn]) clearTimeout(timeout[fn])
      timeout[fn] = setTimeout(() => {
        fn()
      }, delay)
    }

    function checkId() {
      axios.get("/api/account/check?user_id=" + userId.id, {}).then(function (res) {
        console.log(res)
        // res.data
        nameAvailable.shown = true
        nameAvailable.msg = res.data.message
        nameAvailable.error = (res.data.data === 1)
        console.log("data=" + res.data.data + "error=" + nameAvailable.error)
      }).catch(function (err) {
        console.log(err)
        nameAvailable.msg = "未知错误"
        nameAvailable.shown = true
        nameAvailable.error = true
      })
    }

    watch([userId], () => {
      console.log("watch")
      debounce(checkId, 300)
    })

    const sendCode = () => {
      console.log("sendCode")
      const apply = () => {
        axios.get("/api/account/apply_code", {
          params: {
            user_id: userId.id,
            email: signUpForm.email,
            operation: 'register'
          }
        }).then((res) => {
          sendButtonText.msg = res.data.message
          sendButtonText.clickAble = false
          setTimeout(() => {
            sendButtonText.clickAble = true
          }, 5 * 60 * 1000)  // 请求太频繁
        }).catch((err) => {
          sendButtonText.msg = err.data.message
          sendButtonText.clickAble = false
          setTimeout(() => {
            sendButtonText.clickAble = true
          }, 5 * 60 * 1000)  // 请求太频繁
        })
      }
      debounce(apply)
    }

    const register = () => {
      const data = {
        user_id: userId.id,
        username: signUpForm.username,
        email: signUpForm.email,
        password: signUpForm.password,
        validation_code: signUpForm.code
      }
      for (let dataKey in data) {
        if (data[dataKey].isEmpty) {
          return
        }
      }
      let json = JSON.stringify(data)
      var config = {
        method: 'post',
        url: '/api/account/register',
        headers: {
          'Content-Type': 'application/json'
        },
        data: json
      };
      const apply = () => {
        axios(config)
            .then((res) => {
              console.log(res)
              if (res.data.code === 200) {
                // 跳转登录
                router.push({name: '/login', query: {id: userId.id}})
              } else {
                alert(res.data.message)
              }
            })
            .catch((err) => {
              console.log(err)
              alert(err)
            })
      }
      debounce(apply, 300)
    }

    return {
      userId,
      signUpForm,
      sendButtonText,
      nameAvailable,
      debounce,
      checkIdWithDebounce: checkId,
      sendCode,
      register
    }
  }
}
</script>

<style scoped>
div.sign_in_box {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: table-cell; /**/
  text-align: center;
  background: #42b983;
  width: 30%;
  height: 450px;
  padding: 20px 20px;
  border-radius: 5px;
  /*margin-left: 15px;*/
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
  vertical-align: middle;
  margin-right: 0;
  box-sizing: border-box;
  border-bottom-left-radius: 4px;
  border-top-left-radius: 4px;
  border-bottom-style: none;
  border-color: transparent;
  width: 45%;
  padding: 0;
  margin-top: 20px;
  height: 30px;
  outline: none;
}

button.send-code {
  padding: 0;
  font-family: Apple, sans-serif;
  font-style: normal;
  margin-top: 20px;
  margin-left: 0;
  vertical-align: middle;
  box-sizing: content-box;
  background-color: deepskyblue;
  color: white;
  border-bottom-right-radius: 4px;
  border-top-right-radius: 4px;
  border: none transparent;
  width: 25%;
  line-height: 100%;
  height: 30px;
  outline: none;
}

button.sign-up {
  margin-top: 40px;
  border-width: 0;
  background-color: deepskyblue;
  width: 40%;
  height: 40px;
  border-radius: 6px;
  color: white;
  font-size: large;
}

button.hover-active:hover {
  background-color: cornflowerblue;
  color: lightgray;
}

button.hover-active:active {
  background-color: blue;
  color: lightgray;
}

button.hover-active:disabled {
  color: white;
  background-color: darkgrey;
}

.notAvailable {
  width: 70%;
  font-size: smaller;
  color: orangered;
}

.available {
  width: 70%;
  font-size: smaller;
  color: mediumspringgreen;
}
</style>