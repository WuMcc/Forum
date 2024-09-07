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
          <el-select placeholder="帖子类别" v-model="searchModel.type">
            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
              :disabled="item.disabled"
              ></el-option>
          </el-select>
          <el-button
            @click="getUserList"
            type="primary"
            round
            icon="el-icon-search"
          >查询</el-button
          >
        </el-col>
        <el-col :span="4" align="right">
          <el-button
            @click="openEditUI(null)"
            type="primary"
            icon="el-icon-plus"
            circle
          ></el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 结果列表 -->
    <el-card>
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="#">
          <template slot-scope="scope">
            <!-- (pageNo-1) * pageSize + index + 1 -->
            {{
              (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="uid" label="用户ID">
        </el-table-column>
        <el-table-column prop="username" label="帖子作者">
        </el-table-column>
        <el-table-column prop="avatar" label="头像">
          <template slot-scope="scope">
            <el-avatar v-if="scope.row.avatar !== null && scope.row.avatar !== ''" :src="`http://localhost:8080/images${scope.row.avatar}`"/>
            <el-avatar v-else :src="`https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png`"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="帖子标题">
        </el-table-column>
        <el-table-column prop="type" label="帖子类别">
          <template slot-scope="scope">
            <topic-tag :type="typeById(scope.row.type)" :color="colorById(scope.row.type)"></topic-tag>
          </template>
        </el-table-column>
        <el-table-column prop="text" label="简介"> </el-table-column>
        <el-table-column prop="time" label="发帖时间">
          <template slot-scope="scope">
            <div>{{new Date(scope.row.time).toLocaleString()}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="openEditUI(scope.row.tid)" type="primary" icon="el-icon-thumb" size="mini" circle></el-button>
            <el-button @click="deleteUser(scope.row)" type="danger" icon="el-icon-delete" size="mini" circle></el-button>
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

    <!-- 用户信息编辑对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
    >
      <el-form :model="top">
        <el-form-item label="是否置顶该帖子" :label-width="formLabelWidth">
          <el-radio-group v-model="top">
            <el-radio :label="1">置顶</el-radio>
            <el-radio :label="0">不置顶</el-radio>
          </el-radio-group>
          <div style="font-size: 12px; color: #666; margin-top: 1px;">默认不置顶</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveTop(id, top)">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      @close="notice=''"
      :title="title"
      :visible.sync="dialogForm"
    >
      <el-form v-model="notice">
        <el-form-item label="公告内容" :label-width="formLabelWidth" prop="notice">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入内容"
            v-model="notice">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogForm = false">取 消</el-button>
        <el-button type="primary" @click="saveNotice(notice)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userApi from "@/api/userManage";
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
      id: 0,
      notice: "",
      checked: true,
      typeList: [],
      formLabelWidth: "130px",
      top: 0,
      dialogFormVisible: false,
      dialogForm: false,
      title: "",
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10,
        type: 0
      },
      userList: [],
    };
  },
  methods: {
    axios,
    typeById(id) {
      const item = this.typeList.find(item => item.id === id);
      return item ? item.name : null;
    },
    colorById(id) {
      const item = this.typeList.find(item => item.id === id);
      return item ? item.color : null;
    },
    getAllRoleList(){
      roleApi.getAllTypeList().then(response => {
        this.typeList = response.data;
        this.typeList.splice(0, 0, {id: 0, name: "全部类别", desc: "", color: ""});
        console.log(this.typeList);
      });
    },
    deleteUser(topic){
      this.$confirm(`您确认删除帖子 ${topic.title} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        roleApi.deleteTopic(topic.tid).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          });
          this.getUserList();
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
        this.getUserList();
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
      this.getUserList();
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo;
      this.getUserList();
    },
    getUserList() {
      roleApi.getTopicList(this.searchModel).then((response) => {
        this.userList = response.data.rows;
        this.total = response.data.count;
        console.log(this.total)
      });
    },
  },
  created() {
    this.getUserList();
    this.getAllRoleList();
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
