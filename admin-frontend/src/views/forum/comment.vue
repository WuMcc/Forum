<template>
  <div>
    <!-- 搜索栏 -->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input
            v-model="searchModel.username"
            placeholder="用户名"
            clearable
          ></el-input>
          <el-input
            v-model="searchModel.title"
            placeholder="帖子标题"
            clearable
          ></el-input>
          <el-button
            @click="getCommentList"
            type="primary"
            round
            icon="el-icon-search"
          >查询</el-button
          >
        </el-col>
      </el-row>
    </el-card>

    <!-- 结果列表 -->
    <el-card>
      <el-table :data="commentList" stripe style="width: 100%">
        <el-table-column label="#">
          <template slot-scope="scope">
            <!-- (pageNo-1) * pageSize + index + 1 -->
            {{
              (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="帖子标题">
        </el-table-column>
        <el-table-column prop="username" label="评论人">
        </el-table-column>
        <el-table-column prop="content" label="评论内容">
        </el-table-column>
        <el-table-column prop="time" label="评论时间">
          <template slot-scope="scope">
            <div>{{new Date(scope.row.time).toLocaleString()}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="deleteComment(scope.row)" type="danger" icon="el-icon-delete" size="mini" circle></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页组件 -->
    <el-pagination
      lang="zh-CN"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="searchModel.pageNo"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import roleApi from "@/api/roleManage";
import axios from "axios";
import item from "@/layout/components/Sidebar/Item.vue";
import {white} from "mockjs/src/mock/random/color_dict";
import TopicTag from "@/components/TopicTag.vue";
export default {
  components: {TopicTag},
  computed: {
    white() {
      return white
    },
    item() {
      return item
    }
  },
  data() {
    return {
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10,
      },
      commentList: [],
    };
  },
  methods: {
    axios,
    deleteComment(comment){
      this.$confirm(`您确认删除评论" ${comment.content} "?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        roleApi.deleteComment(comment.id).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          });
          this.getCommentList();
        });

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    saveTop(id, top) {
      // 提交请求给后台
      roleApi.saveTop(id, top).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        });
        // 关闭对话框
        this.dialogFormVisible = false;
        // 刷新表格
        this.getCommentList();
      });
    },
    saveNotice(notice) {
      console.log(notice)
      // 提交请求给后台
      roleApi.saveNotice(notice).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        });
        // 关闭对话框
        this.dialogForm = false;
        // 刷新表格
      });
    },
    openEditUI(tid) {
      if(tid === null){
        this.title = "发布公告";
        this.dialogForm = true;
      }
      else{
        this.title = "帖子置顶";
        this.id=tid;
        // 根据id查询用户数据
        roleApi.getTop(tid).then(response => {
          this.top = response.data === null ? 0 : response.data;
        });
        this.dialogFormVisible = true;
      }
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize;
      this.getCommentList();
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo;
      this.getCommentList();
    },
    getCommentList() {
      roleApi.getCommentList(this.searchModel).then((response) => {
        this.commentList = response.data.rows;
        this.total = response.data.count;
        console.log(this.total)
      });
    },
  },
  created() {
    this.getCommentList();
  },
};
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 10px;
}
.el-dialog .el-input {
  width: 85%;
}
</style>
