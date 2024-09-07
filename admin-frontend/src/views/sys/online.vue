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
            v-model="searchModel.browser"
            placeholder="用户浏览器"
            clearable
          ></el-input>
          <el-button
            @click="getOnlineList"
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
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="#" width="80">
          <template slot-scope="scope">
            <!-- (pageNo-1) * pageSize + index + 1 -->
            {{
              (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="tokenId" label="会话编号" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="name" label="用户名">
        </el-table-column>
        <el-table-column prop="ipAddr" label="IP">
        </el-table-column>
        <el-table-column prop="browser" label="浏览器">
        </el-table-column>
        <el-table-column prop="os" label="系统架构">
        </el-table-column>
        <el-table-column prop="osName" label="操作系统">
        </el-table-column>
        <el-table-column prop="online" label="状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.online === 0" type="danger">离线</el-tag>
            <el-tag v-else type="success">在线</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="最近登陆时间" width="160">
          <template slot-scope="scope">
            <div v-if="scope.row.logintime === null">暂未登录</div>
            <div v-else>{{new Date(scope.row.logintime).toLocaleString()}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="最近退出时间" width="160">
          <template slot-scope="scope">
            <div v-if="scope.row.logouttime === null">暂未登录</div>
            <div v-else>{{new Date(scope.row.logouttime).toLocaleString()}}</div>
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
import userApi from "@/api/userManage";
import axios from "axios";
export default {
  data() {

    return {
      checked: true,
      roleList: [],
      formLabelWidth: "130px",
      userForm: {
        role: ""
      },
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10,
      },
      userList: [],
    };
  },
  methods: {
    axios,
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize;
      this.getOnlineList();
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo;
      this.getOnlineList();
    },
    getOnlineList() {
      userApi.getOnlineList(this.searchModel).then((response) => {
        this.userList = response.data.rows;
        this.total = response.data.count;
      });
    },
  },
  created() {
    this.getOnlineList();
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
