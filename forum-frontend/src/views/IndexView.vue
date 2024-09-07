<script setup>
import {get, logout, unauthorized} from '@/net'
import router from "@/router";
import {useStore} from "@/store";
import {reactive, ref} from "vue";
import {
  Back,
  Bell,
  ChatDotSquare, Check, ChromeFilled, Collection, DataLine,
  Document, Files, GoldMedal, Key,
  Location, Lock, Message, Monitor, Moon,
  Notification, Operation,
  Position,
  School, Search, Sunny, SwitchFilled,
  Umbrella, User
} from "@element-plus/icons-vue";
import LightCard from "@/components/LightCard.vue";
import {useDark} from "@vueuse/core";

const dark = ref(useDark())

const store = useStore()
const loading = ref(true)
const isUnauthorized = unauthorized()

if(!isUnauthorized){
  get('/api/user/info', (data) => {
    store.user = data
    loading.value = false
  })
}
if(isUnauthorized){
  setTimeout(() => {
    loading.value = false;
  }, 500); // 0.5秒后改变loading的值
}
const searchInput = reactive({
    type: '1',
    text: ''
})
const notification = ref([])

if(isUnauthorized){
  store.$reset()
}
const loadNotification =
    () => get('/api/notification/list', data => notification.value = data)

if(!isUnauthorized){
  loadNotification()
}

function userLogout() {
    logout(() => router.push("/welcome"))
}

function confirmNotification(id, url) {
    get(`/api/notification/delete?id=${id}`, () => {
        loadNotification()
        if(url == null || url === '')
          router.push("/index")
      else window.open(url)
    })
}
function deleteAllNotification() {
  get(`/api/notification/delete-all`, loadNotification)
}

function scrollToSection(sectionId){
  const element = document.getElementById(sectionId)
  if(element){
    element.scrollIntoView({behavior: "smooth", block: "start"})
  }
}
</script>

<template>
    <div class="main-content" v-loading="loading" element-loading-text="正在进入，请稍后...">
        <el-container style="height: 100%" v-if="!loading">
            <el-header class="main-content-header">
                <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
                <div style="flex: 1;padding: 0 20px;text-align: center">
                    <el-input v-model="searchInput.text" style="width: 100%;max-width: 500px"
                              placeholder="搜索论坛相关内容...">
                        <template #prefix>
                            <el-icon>
                                <Search/>
                            </el-icon>
                        </template>
                        <template #append>
                            <el-select style="width: 120px" v-model="searchInput.type">
                                <el-option value="1" label="帖子广场"/>
                                <el-option value="2" label="校园活动"/>
                                <el-option value="3" label="表白墙"/>
                                <el-option value="4" label="教务通知"/>
                            </el-select>
                        </template>
                    </el-input>
                </div>
              <div>
                <el-switch style="margin: 0 20px"
                           v-model="dark" active-color="#424242"
                           :active-action-icon="Moon"
                           :inactive-action-icon="Sunny"/>
              </div>
                <div class="user-info">
                    <el-popover placement="bottom" :width="350" trigger="click">
                        <template #reference>
                            <el-badge style="margin-right: 15px" is-dot :hidden="!notification.length">
                                <div class="notification" :hidden="isUnauthorized">
                                    <el-icon><Bell/></el-icon>
                                    <div style="font-size: 10px">消息</div>
                                </div>
                            </el-badge>
                        </template>
                        <el-empty :image-size="80" description="暂时没有未读消息哦~" v-if="!notification.length"/>
                        <el-scrollbar :max-height="500" v-else>
                            <light-card v-for="item in notification" class="notification-item"
                                        @click="confirmNotification(item.id, item.url)">
                                <div>
                                    <el-tag size="small" :type="item.type">消息</el-tag>&nbsp;
                                    <span style="font-weight: bold">{{item.title}}</span>
                                </div>
                                <el-divider style="margin: 7px 0 3px 0"/>
                                <div style="font-size: 13px;color: grey">
                                    {{item.content}}
                                </div>
                            </light-card>
                        </el-scrollbar>
                        <div style="margin-top: 10px">
                            <el-button size="small" type="info" :icon="Check" @click="deleteAllNotification"
                                       style="width: 100%" plain>清除全部未读消息</el-button>
                        </div>
                    </el-popover>
                    <div class="profile">
                        <div>{{ store.user.username }}</div>
                        <div>{{ store.user.email }}</div>
                    </div>
                    <el-dropdown>
                        <el-avatar :src="store.avatarUrl"/>
                        <template #dropdown>
                            <el-dropdown-item @click="router.push('/index/user-setting')" v-if="!isUnauthorized">
                                <el-icon>
                                    <Operation/>
                                </el-icon>
                                个人设置
                            </el-dropdown-item>
                            <el-dropdown-item @click="router.push('/index/privacy-setting')" v-if="!isUnauthorized">
                                <el-icon>
                                    <Message/>
                                </el-icon>
                                消息列表
                            </el-dropdown-item>
                            <el-dropdown-item @click="userLogout" divided v-if="!isUnauthorized">
                                <el-icon>
                                    <Back/>
                                </el-icon>
                                退出登录
                            </el-dropdown-item>
                            <el-dropdown-item @click="router.push('/welcome')" v-if="isUnauthorized">
                                <el-icon>
                                  <Back/>
                                </el-icon>
                                前往登录
                            </el-dropdown-item>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="230px">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <el-menu
                                router
                                :default-active="$route.path"
                                :default-openeds="['1', '2', '3']"
                                style="min-height: calc(100vh - 55px)">
                            <el-sub-menu index="1">
                                <template #title>
                                    <el-icon>
                                        <Location/>
                                    </el-icon>
                                    <span><b>校园论坛</b></span>
                                </template>
                                <el-menu-item index="/index">
                                    <template #title>
                                        <el-icon>
                                            <ChatDotSquare/>
                                        </el-icon>
                                        帖子广场
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Bell/>
                                        </el-icon>
                                        失物招领
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Notification/>
                                        </el-icon>
                                        校园活动
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Umbrella/>
                                        </el-icon>
                                        表白墙
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <School/>
                                        </el-icon>
                                        海文考研
                                        <el-tag style="margin-left: 10px" size="small">合作机构</el-tag>
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                          <el-sub-menu index="2">
                            <template #title>
                              <el-icon>
                                <Position/>
                              </el-icon>
                              <span><b>工具箱</b></span>
                            </template>
                            <el-menu-item index="/index/tools/item-1" @click="scrollToSection('item-1')">
                              <template #title>
                                <el-icon><GoldMedal /></el-icon>
                                热门工具
                              </template>
                            </el-menu-item>
                            <el-menu-item index="/index/tools/item-2" @click="scrollToSection('item-2')">
                              <template #title>
                                <el-icon><Key /></el-icon>
                                实用工具
                              </template>
                            </el-menu-item>
                            <el-menu-item index="/index/tools/item-3" @click="scrollToSection('item-3')">
                              <template #title>
                                <el-icon><Collection /></el-icon>
                                好好学习
                              </template>
                            </el-menu-item>
                            <el-menu-item index="/index/tools/item-4" @click="scrollToSection('item-4')">
                              <template #title>
                                <el-icon><ChromeFilled /></el-icon>
                                AI工具
                              </template>
                            </el-menu-item>
                            <el-menu-item index="/index/tools/item-5" @click="scrollToSection('item-5')">
                              <template #title>
                                <el-icon><SwitchFilled /></el-icon>
                                休闲娱乐
                              </template>
                            </el-menu-item>
                          </el-sub-menu>
                            <el-sub-menu index="3" v-if="!isUnauthorized">
                                <template #title>
                                    <el-icon>
                                        <Operation/>
                                    </el-icon>
                                    <span><b>个人设置</b></span>
                                </template>
                                <el-menu-item index="/index/user-setting">
                                    <template #title>
                                        <el-icon>
                                            <User/>
                                        </el-icon>
                                        个人信息设置
                                    </template>
                                </el-menu-item>
                                <el-menu-item index="/index/privacy-setting">
                                    <template #title>
                                        <el-icon>
                                            <Lock/>
                                        </el-icon>
                                        账号安全设置
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                        </el-menu>
                    </el-scrollbar>
                </el-aside>
                <el-main class="main-content-page">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <router-view v-slot="{ Component }">
                            <transition name="el-fade-in-linear" mode="out-in">
                                <component :is="Component" style="height: 100%"/>
                            </transition>
                        </router-view>
                    </el-scrollbar>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style lang="less" scoped>
html {
  scroll-behavior: smooth;
}

.menu-link {
  color: inherit;
  text-decoration: none;
}

div[id] {
  scroll-margin-top: 50px; /* 假设你的导航栏高度为50px */
}

.notification-item {
    transition: .3s;
    &:hover {
        cursor: pointer;
        opacity: 0.7;
    }
}

.notification {
    font-size: 22px;
    line-height: 14px;
    text-align: center;
    transition: color .3s;

    &:hover {
        color: grey;
        cursor: pointer;
    }
}

.main-content-page {
    padding: 0;
    background-color: #f7f8fa;
}

.dark .main-content-page {
    background-color: #212225;
}

.main-content {
    height: 100vh;
    width: 100vw;
}

.main-content-header {
    border-bottom: solid 1px var(--el-border-color);
    height: 55px;
    display: flex;
    align-items: center;
    box-sizing: border-box;

    .logo {
        height: 32px;
    }

    .user-info {
        display: flex;
        justify-content: flex-end;
        align-items: center;

        .el-avatar:hover {
            cursor: pointer;
        }

        .profile {
            text-align: right;
            margin-right: 20px;

            :first-child {
                font-size: 18px;
                font-weight: bold;
                line-height: 20px;
            }

            :last-child {
                font-size: 10px;
                color: grey;
            }
        }
    }
}
</style>
