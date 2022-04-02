<template>
  <div class="sign_in_box" id="signup">
    <el-form
        ref="ruleFormRef"
        :label-position="position"
        :model="ruleForm"
        style="width: 300px;display: inline-block;margin-top: 10px"
        status-icon
        :rules="rulers"
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
      <el-form-item label="Password" prop="pass">
        <el-input v-model="ruleForm.pass" type="password" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Password again" prop="checkPass">
        <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Email" prop="email">
        <el-input v-model="ruleForm.email" type="text" auto-complete="on"/>
      </el-form-item>
      <el-form-item label="Verification" prop="code">
        <el-input v-model="ruleForm.code" type="text" style="width: 60%;" auto-complete="off"/>
        <el-button style="color: white;background-color: #3c8dbc;width: 40%;margin: 0" v-show="showTime"
                   @click="sendEmail(ruleForm.email)">发送验证码
        </el-button>
        <el-button style="color: white;background-color: #3E414E;width: 40%;margin: 0" v-show="!showTime">
          {{ sendTime }}秒
        </el-button>
      </el-form-item>
      <div style="margin-top: 30px;margin-bottom: 30px">
        <el-button type="primary" @click="submitForm(ruleFormRef)">注册
        </el-button>
        <el-button @click="this.resetForm(ruleFormRef)">清空</el-button>
      </div>
      <div>

        <router-link type="string" :underline="false" :to="{path:'/register'}">已有账号，点击登录</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
const {ref} = require("vue");
const {ElMessage} = require("element-plus");
const axios = require("axios");
const {useRouter} = require("vue-router");


let timeout;
export default {
  name: "RegisterView",
  data(){
    const router = useRouter()
    // setTimeout(()=>{
    //   router.push({name: 'login'})
    // },1000)

    return {
      position:ref('top'),
      ruleForm:{
        user_id: '',
        username: '',
        email: '',
        code: '',
        pass: '',
        checkPass: ''
      },
      rulers:{
        user_id: [{validator: this.validateId, trigger: 'blur'}],
        username: [{validator: this.validateUsername, trigger: 'blur'}],
        email: [{validator: this.validateEmail, trigger: 'blur'}],
        code: [{validator: this.validateCode, trigger: 'blur'}],
        pass: [{validator: this.validatePass, trigger: 'blur'}],
        checkPass: [{validator: this.validatePass2, trigger: 'blur'}],
      },
      NumberWord : "[0-9a-zA-Z]{5,16}",
      EmailRegex : "^[0-9a-zA-Z][0-9a-zA-Z.]+@[0-9a-zA-Z]+\\.[a-zA-Z]+$",
      showTime: true,
      sendTime: '',
      timer: null,
      router
    }
  },
  setup() {
    const ruleFormRef = ref()
    return {ruleFormRef}
  },
  methods: {
    // 解决按钮快速点击时出现的抖动现象
    debounce(fn, delay = 300) {   //默认300毫秒
      console.log(fn)
      if (timeout[fn]) clearTimeout(timeout[fn])
      timeout[fn] = setTimeout(() => {
        fn()
      }, delay)
    },
    validateId(rule, value, callback) {
      if (value === '') {
        callback(new Error('Please input userId'))
      } else if (!value.match(this.NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        axios.get("/api/account/check?user_id=" + this.ruleForm.user_id, {})
        .then(function (res){
          if (res.data.data === 0)
            callback()
          else
            callback(new Error('ID已存在'))
        })
        .catch(function (){
          callback(new Error('未知错误'))
        })
      }
    },
    validateUsername(rule, value, callback){
      if (value === '') {
        callback(new Error('Please input username'))
      } else if (!value.match(this.NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    },

    validateEmail(rule, value, callback){
      if (value === '') {
        callback(new Error('Please input email'))
      } else if (!value.match(this.EmailRegex)) {
        callback(new Error('Please input correct email address'))
      } else {
        axios.get("/api/account/check_email?email=" + this.ruleForm.email, {})
            .then(function (res){
              if (res.data.data === 0)
                callback()
              else
                callback(new Error('邮箱已注册'))
            })
            .catch(function (){
              callback(new Error('未知错误'))
            })
      }
    },

    validateCode (rule, value, callback){
      if (value === '') {
        callback(new Error('Please input validation code'))
      } else if (!value.match("[0-9]{6}")) {
        callback(new Error('Please input correct code (six digits)'))
      } else {
        callback()
      }
    },

    validatePass  (rule, value, callback) {
      if (value === '') {
        callback(new Error('Please input password'))
      } else if (!value.match(this.NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    },

    validatePass2  (rule, value, callback) {
      if (value === '') {
        callback(new Error('Please input the password again'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error("Two inputs don't match!"))
      } else {
        callback()
      }
    },

    sendEmail (email) {
      const TIME_COUNT = 60; //  更改倒计时时间
      if (!this.timer) {
        // 真实发送验证码逻辑
        console.log('sendCode to ', email)
        axios.get("/api/account/apply_code", {
          params: {
            user_id: this.ruleForm.user_id,
            email: this.ruleForm.email,
            operation: 'register'
          }
        }).then(function (res){
          if (res.data.code === 200) {
            ElMessage("验证码已发送至您的邮箱,验证码5分钟内有效")
            // 发送成功开始倒计时
            this.sendTime = TIME_COUNT;
            this.showTime = false;
            this.timer = setInterval(() => {
              if (this.sendTime > 0 && this.sendTime <= TIME_COUNT) {
                this.sendTime--;
              } else {
                this.showTime = true;
                clearInterval(this.timer); // 清除定时器
                this.timer = null;
              }
            }, 1000);
          }else {
            ElMessage(res.data.message)
          }
        }).catch(function () {
          ElMessage('未知错误')
        })
      }
    },

    submitForm (formEl) {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          console.log('submit!')
          const data = {
            user_id: this.ruleForm.user_id,
            username: this.ruleForm.username,
            email: this.ruleForm.email,
            password: this.ruleForm.pass,
            validation_code: this.ruleForm.code
          }
          let json = JSON.stringify(data)
          const config = {
            method: 'post',
            url: '/api/account/register',
            headers: {
              'Content-Type': 'application/json'
            },
            data: json
          };
          axios(config)
              .then((res) => {
                console.log(res)
                if (res.data.code === 200) {
                  // 跳转登录
                  this.router.push({name: 'login'})
                } else {
                  ElMessage(res.data.message)
                }
              })
              .catch((err) => {
                console.log(err)
                ElMessage('未知错误')
              })

        } else {
          // 表单校验失败
          console.log('error submit!')
          return false
        }
      })
    },

    resetForm (formEl) {
      if (!formEl) return
      formEl.resetFields()
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
  /*width: 30%;*/
  /*height: 500px;*/
  padding: 20px 40px;
  border-radius: 5px;
  /*margin-left: 15px;*/
  box-shadow: darkgrey 10px 10px 15px 5px
}
</style>