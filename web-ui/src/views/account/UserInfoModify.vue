<template>
  <el-tabs v-loading="loading.on" v-model="activeName" :element-loading-text="loading.msg" style="padding: 20px;">
    <el-tab-pane label="查看详细信息" name="info">
      <el-descriptions title="用户信息">
        <el-descriptions-item label="用户名">
          {{ this.$userinfo.username }}
          <el-icon class="icon-btn" @click="()=>{this.activeName='modify-username'}">
            <svg t="1649318427650" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="2227" width="200" height="200">
              <path
                  d="M862.709333 116.042667a32 32 0 1 1 45.248 45.248L455.445333 613.813333a32 32 0 1 1-45.258666-45.258666L862.709333 116.053333zM853.333333 448a32 32 0 0 1 64 0v352c0 64.8-52.533333 117.333333-117.333333 117.333333H224c-64.8 0-117.333333-52.533333-117.333333-117.333333V224c0-64.8 52.533333-117.333333 117.333333-117.333333h341.333333a32 32 0 0 1 0 64H224a53.333333 53.333333 0 0 0-53.333333 53.333333v576a53.333333 53.333333 0 0 0 53.333333 53.333333h576a53.333333 53.333333 0 0 0 53.333333-53.333333V448z"
                  p-id="2228"></path>
            </svg>
          </el-icon>
        </el-descriptions-item>
        <el-descriptions-item label="账户ID">{{ this.$userinfo.userid }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ this.$userinfo.email }}</el-descriptions-item>
        <el-descriptions-item label="用户凭据">{{ this.$userinfo.token }}</el-descriptions-item>
      </el-descriptions>
    </el-tab-pane>
    <el-tab-pane label="修改用户名" name="modify-username">
      <el-row justify="center" align="middle">
        <el-col :span="8">
          <el-form
              ref="userFormRef"
              :model="usernameForm"
              :rules="rulers"
              label-position="top">
            <el-form-item label="您的用户名：">
              <span> {{ this.$userinfo.username }}</span>
            </el-form-item>
            <el-form-item label="新的用户名：" prop="username">
              <el-input placeholder="输入字符和数字组合：" v-model="usernameForm.username" type="text" auto-complete="off"/>
            </el-form-item>
            <div>
              <el-button type="primary" @click="updateUsername(userFormRef)">修改</el-button>
              <el-button @click="this.resetForm(userFormRef)">重置</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>

    </el-tab-pane>
    <el-tab-pane label="修改密码" name="modify-password">
      <el-row justify="center" align="middle">
        <el-col :span="8">
          <el-form
              ref="passwordFormRef"
              :model="passwordForm"
              :rules="rulers"
              label-position="top">
            <el-form-item label="原密码：" prop="password">
              <el-input placeholder="输入字符和数字组合：" v-model="passwordForm.password" type="password" auto-complete="off"/>
            </el-form-item>
            <el-form-item label="新密码：" prop="newPassword">
              <el-input placeholder="输入字符和数字组合：" v-model="passwordForm.newPassword" type="password" auto-complete="off"/>
            </el-form-item>
            <div>
              <el-button type="primary" @click="updatePassword(passwordFormRef)">修改</el-button>
              <el-button @click="this.resetForm(passwordFormRef)">重置</el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {getCurrentInstance} from "vue";

export default {
  name: "UserInfoModify",
  data() {
    return {
      loading: {
        on: false,
        msg: '加载中...'
      },
      activeName: 'info',
      usernameForm: {
        username: ''
      },
      NumberWord : "[0-9a-zA-Z]{5,16}",
      rulers:{
        username: [{validator: this.validateUsername, trigger: 'blur'}],
        password: [{validator: this.validatePass, trigger: 'blur'}],
        newPassword: [{validator: this.validateNewPass, trigger: 'blur'}]
      },
      passwordForm:{
        password:'',
        newPassword:''
      }
    }
  },
  setup(){
    const userFormRef = ref()
    const passwordFormRef = ref()
    const globalProperties = getCurrentInstance().appContext.config.globalProperties; // 获取全局挂载
    return {userFormRef,passwordFormRef,globalProperties}

  },
  methods: {
    validateUsername(rule, value, callback){
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else if (!value.match(this.NumberWord)) {
        callback(new Error('请输入5-16为字符或数字'))
      } else {
        callback()
      }
    },
    validatePass  (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入5-16为字符或数字'))
      } else if (!value.match(this.NumberWord)) {
        callback(new Error('请输入5-16为字符或数字'))
      } else {
        callback()
      }
    },
    validateNewPass  (rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入5-16为字符或数字'))
      } else if (value === this.passwordForm.password) {
        callback(new Error("新密码不能与旧密码相同"))
      } else {
        callback()
      }
    },
    updateUsername(formEl) {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          this.loading.on = true
          const data = {
            username: this.$userinfo.username,
            new_username: this.usernameForm.username,
            token: this.$userinfo.token
          }
          let json = JSON.stringify(data)
          const config = {
            method: 'post',
            url: '/api/account/update_username',
            headers: {
              'Content-Type': 'application/json'
            },
            data: json
          };
          this.$axios(config)
              .then((res) => {
                console.log(res)
                this.loading.on = false
                if (res.data.code === 200) {
                  this.$userinfo.username = this.usernameForm.username
                  formEl.resetFields()
                  this.activeName = 'info'
                }
                ElMessage(res.data.message)
              })
              .catch((err) => {
                console.log(err)
                this.loading.on = false
                ElMessage('未知错误')
              })

        } else {
          // 表单校验失败
          return false
        }
      })
    },
    updatePassword(formEl) {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          this.loading.on = true
          const data = {
            password: this.passwordForm.password,
            new_password: this.passwordForm.newPassword,
            user_id:this.$userinfo.userid,
            token: this.$userinfo.token
          }
          let json = JSON.stringify(data)
          const config = {
            method: 'post',
            url: '/api/account/update',
            headers: {
              'Content-Type': 'application/json'
            },
            data: json
          };
          this.$axios(config)
              .then((res) => {
                console.log(res)
                this.loading.on = false
                if (res.data.code === 200) {
                  // 退出登录
                  this.logout()
                }
                ElMessage(res.data.message)
              })
              .catch((err) => {
                console.log(err)
                this.loading.on = false
                ElMessage('未知错误')
              })

        } else {
          // 表单校验失败
          return false
        }
      })

    },
    resetForm(formEl) {
      if (!formEl) return
      formEl.resetFields()
    },
    logout() {
      console.log(this.$userinfo)
      // 清除cookie
      this.$ck.clearCookies()
      this.$ck.refreshCookies(this.globalProperties)
      // 退出直接切到主页
      this.$router.push('/home')
    }

  }
}
</script>

<style scoped>

.icon-btn:hover {
  background-color: rgba(213, 216, 231, 0.6);
}

</style>