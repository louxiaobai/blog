<template>
  <el-card>
    <h1 class="me-author-name">Blog</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;河南&商丘</span>
      <span><i class="me-icon-job"></i> &nbsp;java技术开发</span>
    </div>
    <div class="me-author-tool">
      <i @click="showTool(weixin)" :title="weixin.title" class="iconfont icon-weixin"></i>
      <i @click="showTool(github)" :title="github.title" class="iconfont icon-github-fill"></i>
    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {
        weixin: {title: '微信', message: 'louquanwei111'},
        github: {
          title: 'GitHub',
          message: '<a target="_blank" href="https://github.com/louxiaobai">https://github.com/louxiaobai</a>'
        }
      }
    },
    methods: {
      showTool(tool) {
        this.$message({
          duration: 0,
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: '<strong>' + tool.message + '</strong>'
        });
      }
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
</style>
