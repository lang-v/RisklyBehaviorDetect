<template>
  <div class="sign_in_box" id="signup">
    <el-form
        ref="ruleFormRef"
        :label-position="position"
        :model="ruleForm"
        style="width: 300px;display: inline-block;margin-top: 10px"
        status-icon
        :rules="rules"
        label-width="80px"
    >

      <div>
        <h3>注册</h3>
      </div>

      <el-form-item label="UserID" prop="user_id">
        <el-input v-model="ruleForm.user_id" type="text" auto-complete="off"/>
      </el-form-item>
      <el-form-item label="Username" prop="username">
        <el-input v-model="ruleForm.username" type="text" auto-complete="off"/>
      </el-form-item>
      <el-form-item label="Email" prop="email">
        <el-input v-model="ruleForm.email" type="text" auto-complete="on"/>
      </el-form-item>
      <el-form-item label="Verification" prop="code">
        <el-input v-model="ruleForm.code" type="text" style="width: 60%;" auto-complete="off"/>
        <el-button style="color: white;background-color: #3c8dbc;width: 40%;margin: 0"  v-show="CodeTimer.showTime" @click="sendEmail(ruleForm.email)">发送验证码</el-button>
        <el-button style="color: white;background-color: #3c8dbc;width: 40%;margin: 0"  v-show="!CodeTimer.showTime" >{{CodeTimer.sendTime}}秒</el-button>
      </el-form-item>
      <el-form-item label="Password" prop="pass">
        <el-input v-model="ruleForm.pass" type="password" autocomplete="off"/>
      </el-form-item>
      <div style="margin-top: 30px;margin-bottom: 30px">
        <el-button type="primary" @click="submitForm(ruleFormRef)">注册
        </el-button>
        <el-button @click="resetForm(ruleFormRef)">清空</el-button>
      </div>
      <div>

        <router-link type="string" :underline="false" :to="{path:'/register'}">没有账号？点击注册</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
// import axios from "axios";
// import {useRouter} from "vue-router";

const {ref} = require("vue");


export default {
  name: "RegisterView",
  setup() {
    const ruleFormRef = ref()
    const NumberWord = "[0-9a-zA-Z]{5,16}"
    const EmailRegex = "^[0-9a-zA-Z][0-9a-zA-Z.]+@[0-9a-zA-Z]+\\.[a-zA-Z]+$"
    const position = ref('top')

    const validateId = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input userId'))
      } else if (!value.match(NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    }

    const validateUsername = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input username'))
      } else if (!value.match(NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    }

    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input email'))
      } else if (!value.match(EmailRegex)) {
        callback(new Error('Please input correct email address'))
      } else {
        callback()
      }
    }

    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input validation code'))
      } else if (!value.match("[0-9]{6}")) {
        callback(new Error('Please input correct code (six digits)'))
      } else {
        callback()
      }
    }

    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input password'))
      } else if (!value.match(NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    }

    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'))
      } else if (value !== ruleForm.pass) {
        callback(new Error("Two inputs don't match!"))
      } else {
        callback()
      }
    }

    const ruleForm = reactive({
      user_id: '',
      username: '',
      email: '',
      code: '',
      pass: '',
      checkPass: ''
    })

    const rules = reactive({
      user_id: [{validator: validateId, trigger: 'blur'}],
      username: [{validator: validateUsername, trigger: 'blur'}],
      email: [{validator: validateEmail, trigger: 'blur'}],
      code: [{validator: validateCode, trigger: 'blur'}],
      pass: [{validator: validatePass, trigger: 'blur'}],
      checkPass: [{validator: validatePass2, trigger: 'blur'}],
    })

    const CodeTimer = reactive({
      showTime:true,
      sendTime:'',
      timer:null
    })

    const sendEmail = (email)=>{
      const TIME_COUNT = 300; //  更改倒计时时间
      if (!CodeTimer.timer) {
        // 真实发送验证码逻辑
        console.log('sendCode to ',email)
        ElMessage("验证码已发送至您的邮箱")
        CodeTimer.sendTime = TIME_COUNT;
        CodeTimer.showTime = false;
        CodeTimer.timer = setInterval(() => {
          if (CodeTimer.sendTime > 0 && CodeTimer.sendTime <= TIME_COUNT) {
            CodeTimer.sendTime--;
          } else {
            CodeTimer.showTime = true;
            clearInterval(CodeTimer.timer); // 清除定时器
            CodeTimer.timer = null;
          }
        }, 1000);
      }
    }


    const submitForm = (formEl) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          console.log('submit!')
        } else {
          console.log('error submit!')
          return false
        }
      })
    }

    const resetForm = (formEl) => {
      if (!formEl) return
      formEl.resetFields()
    }
    return {
      position,
      resetForm,
      submitForm,
      rules,
      ruleForm,
      ruleFormRef,
      // 验证码逻辑
      CodeTimer,
      sendEmail
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
  /*height: 500px;*/
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