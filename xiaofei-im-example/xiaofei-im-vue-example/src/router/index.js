import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
// 共三个页面： 聊天页面，好友页面，个人简历分别对应一下路由
    routes: [
        {
            path: '/',
            component: () => import('@/views/chat/chat')
        }
    ],
    linkActiveClass: 'active'
})
export default router
