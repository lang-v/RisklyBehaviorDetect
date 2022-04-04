<template>
  <div v-loading="loading.on" :element-loading-text="loading.msg" element-loading-background="rgba(0, 0, 0, 0.8)" style="width: 100%;height: 100%">
    <div align="center">
      <el-form
          ref="formRef"
          align="center"
          :model="form"
          label-width="120px"
          style="width: 500px">
        <div style="margin-top: 30px;margin-bottom: 30px">
          <h3>新建项目</h3>
        </div>
        <el-form-item required="true" prop="projectName" label="项目名称">
          <el-input v-model="form.projectName"/>
        </el-form-item>
        <el-form-item required="true" prop="url" label="链接地址">
          <el-input v-model="form.url"/>
        </el-form-item>
        <el-form-item required="true" prop="type" label="类型">
          <el-select placeholder="选择一项" v-model="form.type">
            <el-option label="本地文件" value="LocalFile"/>
            <el-option label="监控摄像头" value="Camera"/>
          </el-select>
        </el-form-item>
        <div align="center" style="margin-top: 30px;margin-bottom: 30px">
          <el-button type="primary" @click="this.submitForm(formRef)">创建
          </el-button>
          <el-button @click="this.resetForm(formRef)">重置</el-button>
        </div>
      </el-form>
    </div>

  </div>
</template>

<script>
import { ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";

export default {
  name: "CreateView",
  data() {
    return {
      form: {
        projectName: '',
        type: '',
        url: '',
      },
      loading: {
        on: false,
        msg: '上传中...'
      }
    }
  },
  setup(){
    const formRef = ref()
    return {formRef}
  },
  methods: {
    submitForm(formEl) {
      if (!formEl)return
      formEl.validate((valid)=>{
        if (valid) {
          this.loading.on = true
          let data = {
            projectName: this.form.projectName,
            type:this.form.type,
            url:this.form.url,
            token:this.$userinfo.token
          }
          let json = JSON.stringify(data)
          const config = {
            method: 'post',
            url: '/api/projects/create',
            headers: {
              'Content-Type': 'application/json'
            },
            data: json
          };
          axios(config).then((res)=>{
            this.loading.on = false
            if (res.data.code === 200) {
              ElMessage('创建成功')
            }else {
              ElMessage(res.data.message)
            }
          }).catch((err)=>{
            this.loading.on = false
            ElMessage('未知错误')
            console.log(err)
          })
        }
      })
    },
    resetForm(formEl) {
      if (!formEl) return
      formEl.resetFields()
    }
  }
}
</script>

<style scoped>

</style>