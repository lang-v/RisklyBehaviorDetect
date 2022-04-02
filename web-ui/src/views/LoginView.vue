<template>
    <div class="login_box" id="login">
      <el-form
          ref="ruleFormRef"
          :model="ruleForm"
          :label-position="position"
          style="width: 300px;display: inline-block;margin-top: 10px"
          status-icon
          :rules="rules"
          label-width="80px"
      >
        <div>
          <h3>登录</h3>
        </div>

        <el-form-item label="UserID" prop="user_id">
          <el-input v-model="ruleForm.user_id" type="text" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="Password" prop="pass">
          <el-input v-model="ruleForm.pass" type="password" autocomplete="off"/>
        </el-form-item>
        <div style="margin-top: 30px;margin-bottom: 30px">
          <el-button type="primary" @click="submitForm(ruleFormRef)"
          >登录
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

const {ref} = require("vue");

export default {
  name: "LoginView",
  props: {
    id: String
  },
  setup() {
    const ruleFormRef = ref()
    const NumberWord = "[0-9a-zA-Z]{5,16}"

    const validateId = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input username'))
      } else if (!value.match(NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    }

    const position = ref('top')
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input password'))
      } else if (!value.match(NumberWord)) {
        callback(new Error('Please input number or char'))
      } else {
        callback()
      }
    }
    // const validatePass2 = (rule, value, callback) => {
    //   if (value === '') {
    //     callback(new Error('Please input the password again'))
    //   } else if (value !== ruleForm.pass) {
    //     callback(new Error("Two inputs don't match!"))
    //   } else {
    //     callback()
    //   }
    // }

    const ruleForm = reactive({
      user_id: '',
      pass: '',
    })

    const rules = reactive({
      user_id: [{validator: validateId, trigger: 'blur'}],
      pass: [{validator: validatePass, trigger: 'blur'}],
    })

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
      ruleFormRef
    }
  }
}
</script>

<style scoped>
div.login_box {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  /*display: table-cell; !**!*/
  text-align: center;
  background: #42b983;
  width: 350px;
  height: 300px;
  padding: 20px 40px;
  border-radius: 5px;
  /*margin-left: 15px;*/
  box-shadow: darkgrey 10px 10px 15px 5px
}
</style>