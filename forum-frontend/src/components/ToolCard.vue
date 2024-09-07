<script setup>
import {ArrowRightBold} from "@element-plus/icons-vue";

defineProps({
  name: String,
  url: String,
  desc: String,
  img: String,
  tags: Array
})
function handle(url){
  window.open(url, '_blank')
}
</script>

<template>
  <div class="instance-card" style="margin-top: 20px; position: relative; padding-bottom: 50px;">
<!--    <a :href="url" target="_blank"/>-->
    <div style="display: flex">
      <el-image :src="img" style="height: 30%; width: 30%"/>
      <div style="margin-left: 20px">
        <strong>{{name}}</strong>
        <div class="text-container">
          <span class="ellipsis-text" :title="desc">
            {{desc}}
          </span>
        </div>
      </div>
    </div>
    <div class="bottom-container" style="position: absolute; bottom: 10px; width: 90%; display: flex; justify-content: space-between;">
      <div>
        <el-tag style="margin-right: 8px" v-for="item in tags" :key="item" size="small">{{item}}</el-tag>
      </div>
      <div class="icon-wrapper">
        <el-tooltip content="点击访问" placement="top">
          <el-icon @click="handle(url)"><ArrowRightBold /></el-icon>
        </el-tooltip>
      </div>
    </div>
  </div>

</template>

<style scoped>
.text-container {
  width: 200px; /* 设置容器的宽度 */
  overflow: hidden;
  white-space: nowrap; /* 禁止文本换行 */
  text-overflow: ellipsis; /* 当文本溢出时使用省略号展示 */
}

.ellipsis-text:hover {
  white-space: normal; /* 鼠标悬浮时允许文本换行 */
  overflow: visible;
  position: absolute;
  background-color: #efefef;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  color: #232323;
  z-index: 1;
}

</style>