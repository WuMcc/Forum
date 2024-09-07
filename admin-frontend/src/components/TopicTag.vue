<template>
  <div class="topic-type"
       :style="{
         color: color + 'EE',
         'border-color': color + '77',
         'background': color + '33'
       }">
    {{ type }}
  </div>
</template>

<script>

export default {
  props: {
    type: String,
    color: String
  },
  computed: {
  }
};
</script>

<style scoped>
.topic-type {
  display: flex;        /* 启用 Flex 布局 */
  align-items: center;  /* 垂直居中文本 */
  justify-content: center; /* 水平居中文本（可选） */
  border: solid 0.5px grey;
  border-radius: 3px;
  font-size: 12px;
  padding: 0 5px;
  height: 18px;
  width: fit-content;
}
</style>
