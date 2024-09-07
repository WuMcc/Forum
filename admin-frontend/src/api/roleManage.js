import request from '@/utils/request'

export default{
  // getRoleList(searchModel){
  //   return request({
  //     url: '/role/list',
  //     method: 'get',
  //     params:{
  //       pageNo: searchModel.pageNo,
  //       pageSize: searchModel.pageSize,
  //       roleName: searchModel.roleName
  //     }
  //   });
  // },
  // addRole(role){
  //   return request({
  //     url: '/role',
  //     method: 'post',
  //     data: role
  //   });
  // },
  // updateRole(role){
  //   return request({
  //     url: '/role',
  //     method: 'put',
  //     data: role
  //   });
  // },
  // saveRole(role){
  //   if(role.roleId == null && role.roleId == undefined){
  //     return this.addRole(role);
  //   }
  //   return this.updateRole(role);
  // },
  // getRoleById(id){
  //   return request({
  //     url: `/role/${id}`,
  //     method: 'get'
  //   });
  // },
  // deleteRoleById(id){
  //   return request({
  //     url: `/role/${id}`,
  //     method: 'delete'
  //   });
  // },
  getAllRoleList(){
    return request({
      url: '/account/role/list',
      method: 'get'
    });
  },
  getAllTypeList(){
    return request({
      url: 'http://localhost:8080/api/forum/types',
      method: 'get'
    });
  },
  getTopicList(searchModel){
    return request({
      url: '/topic/list',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        title: searchModel.title,
        type: searchModel.type,
        username: searchModel.username
      }
    });
  },
  getTop(id){
    return request({
      //url: '/user/' + id,
      url: `/topic/top`,
      method: 'get',
      params: {
        tid: id
      }
    });
  },
  saveTop(id, top){
    return request({
      url: `/topic/top`,
      method: 'post',
      params: {
        id: id,
        top: top
      }
    });
  },
  deleteTopic(id){
    return request({
      url: `/topic/delete`,
      method: 'post',
      params: {
        id: id
      }
    });
  },
  saveNotice(notice){
    return request({
      url: `/topic/notice`,
      method: 'post',
      data:{
        notice: notice
      }
    });
  },
  getCommentList(searchModel){
    return request({
      url: `/topicComment/list`,
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        title: searchModel.title
      }
    })
  },
  deleteComment(id){
    return request({
      url: `/topicComment/delete`,
      method: 'post',
      params: {
        id: id
      }
    });
  },
}
