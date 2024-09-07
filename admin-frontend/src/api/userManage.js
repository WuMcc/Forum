import request from '@/utils/request'

export default{
  getUserList(searchModel){
    return request({
      url: '/account/list',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        email: searchModel.email
      }
    });
  },
  getOnlineList(searchModel) {
    return request({
      url: '/account/online',
      method: 'get',
      params:{
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        browser: searchModel.browser
      }
    });
  },
  addUser(user){
    return request({
      url: '/account/add',
      method: 'post',
      data: user
    });
  },
  updateUser(user){
    return request({
      url: '/account/update',
      method: 'post',
      data: user
    });
  },
  saveUser(user){
    if(user.id == null && user.id === undefined){
      return this.addUser(user);
    }
    return this.updateUser(user);
  },
  getUserById(id){
    return request({
      //url: '/user/' + id,
      url: `/account/one`,
      method: 'get',
      params: {
        id: id
      }
    });
  },
  deleteUserById(uid){
    return request({
      url: `/account/delete`,
      method: 'get',
      params: {
        id : uid
      }
    });
  },
  logoutUserById(name){
    return request({
      url: `/topic/logout`,
      method: 'get',
      params:{
        'name': name
      }
    });
  }
}
