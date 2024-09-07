import { createRouter, createWebHistory } from 'vue-router'
import { unauthorized } from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
          path: '/',
          redirect: '/index'
        },
        {
            path: '/welcome',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '/welcome',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                }, {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                }, {
                    path: 'forget',
                    name: 'welcome-forget',
                    component: () => import('@/views/welcome/ForgetPage.vue')
                }
            ]
        }, {
            path: '/index',
            name: 'index',
            component: () => import('@/views/IndexView.vue'),
            children: [
                {
                    path: '',
                    name: 'topics',
                    component: () => import('@/views/forum/Forum.vue'),
                    children: [
                        {
                            path: '',
                            name: 'topic-list',
                            component: () => import('@/views/forum/TopicList.vue')
                        },{
                            path: 'topic-detail/:tid',
                            name: 'topic-detail',
                            component: () => import('@/views/forum/TopicDetail.vue')
                        }
                    ]
                }, {
                    path: 'user-setting',
                    name: 'user-setting',
                    component: () => import('@/views/settings/UserSetting.vue')
                }, {
                    path: 'privacy-setting',
                    name: 'privacy-setting',
                    component: () => import('@/views/settings/PrivacySetting.vue')
                },
                {
                    path: 'tools',
                    name: 'tools',
                    component: () => import('@/views/forum/Tools.vue'),
                },{
                    path: 'tools/item-1',
                    name: 'tools/item-1',
                    component: () => import('@/views/forum/Tools.vue'),
                },{
                    path: 'tools/item-2',
                    name: 'tools/item-2',
                    component: () => import('@/views/forum/Tools.vue'),
                },{
                    path: 'tools/item-3',
                    name: 'tools/item-3',
                    component: () => import('@/views/forum/Tools.vue'),
                },{
                    path: 'tools/item-4',
                    name: 'tools/item-4',
                    component: () => import('@/views/forum/Tools.vue'),
                },{
                    path: 'tools/item-5',
                    name: 'tools/item-5',
                    component: () => import('@/views/forum/Tools.vue'),
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if (to.name.startsWith('welcome') && !isUnauthorized) {
        // 用户已登录且访问了 welcome 页面，重定向到 /index 页面
        next('/index');
    } else {
        // 其他情况正常进行导航
        next();
    }
})

export default router
