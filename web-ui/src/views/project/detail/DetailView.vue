<template>
  <div>
    <div v-if="this.$route.query.project === undefined">
      This page access requires the parameter 'project'
    </div>
    <div v-else>
      {{project}}
    </div>
  </div>
</template>

<script>
export default {
  name: "DetailView",
  data(){
    let json = this.$route.query.project
    console.log(json)
    if (json === undefined) {
      json = "{}"
    }
    return {
      project: JSON.parse(json)
    }
  },
  setup() {
    // console.log(this.$route.query.project)
    // const project = JSON.parse(this.$route.query.project)
    // console.log(this.project)
    // return {project}
  }
}
</script>

<style scoped>

</style>