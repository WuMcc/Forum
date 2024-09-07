<script setup>
import {useRoute} from "vue-router";
import {get, post, unauthorized} from "@/net";
import {computed, reactive, ref} from "vue";
import {
  ArrowLeft,
  ChatLineRound,
  ChatSquare,
  CircleCheck,
  Delete,
  EditPen,
  Female,
  Male,
  Plus,
  Star
} from "@element-plus/icons-vue";
import { QuillDeltaToHtmlConverter } from 'quill-delta-to-html';
import Card from "@/components/Card.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import InteractButton from "@/components/InteractButton.vue";
import {ElMessage} from "element-plus";
import {useStore} from "@/store";
import TopicEditor from "@/components/TopicEditor.vue";
import TopicCommentEditor from "@/components/TopicCommentEditor.vue";

const route = useRoute()
const store = useStore()
const isUnauthorized = unauthorized()
const tid = route.params.tid
const types = ref(null)
const topic = reactive({
    data: null,
    like: false,
    collect: false,
    comments: null,
    words: null,
    page: 1
})
const centerDialogVisible = ref(false)
const words = () => get('/api/forum/words', data => topic.words = data)
const edit = ref(false)
const comment = reactive({
    show: false,
    text: '',
    quote: null
})

const init = () => get(`api/forum/topic?tid=${tid}`, data => {
    topic.data = data
    topic.like = data.interact.like
    topic.collect = data.interact.collect
    loadComments(1)
}, () => {ElMessage.info("帖子已删除！")})
init()
words()


function convertToHtml(content) {
    const ops = JSON.parse(content).ops
    const converter = new QuillDeltaToHtmlConverter(ops, { inlineStyles: true });
    return converter.convert();
}

function handleClick(active){
  if(isUnauthorized){
    centerDialogVisible.value = true
  }else{
    if(active === 'comment'){
      comment.show = true;
      comment.quote = null;
    }
    if(active === 'like'){
      interact('like', '点赞')
    }
    if (active === 'collect')
      interact('collect', '收藏')
  }
}

function interact(type, message) {
    get(`/api/forum/interact?tid=${tid}&type=${type}&state=${!topic[type]}`, () => {
        topic[type] = !topic[type]
        if(topic[type])
            ElMessage.success(`${message}成功！`)
        else
            ElMessage.success(`已取消${message}！`)
    })
}
function updateTopic(editor) {
    post('/api/forum/update-topic', {
        id: tid,
        type: editor.type.id,
        title: editor.title,
        content: editor.text
    }, () => {
        ElMessage.success('帖子内容更新成功！')
        edit.value = false
        init()
    })
}

function loadComments(page) {
    topic.comments = null
    topic.page = page
    get(`/api/forum/comments?tid=${tid}&page=${page - 1}`, data => topic.comments = data)
}

function onCommentAdd() {
    comment.show = false
    loadComments(Math.floor(++topic.data.comments / 10) + 1)
}

function deleteComment(id) {
    get(`/api/forum/delete-comment?id=${id}`, () => {
        ElMessage.success('删除评论成功！')
        loadComments(topic.page)
    })
}
</script>

<template>
    <div class="topic-page" v-if="topic.data">
        <div class="topic-main" style="position: sticky;top: 0;z-index: 10">
            <card style="display: flex;width: 100%;">
                <el-button :icon="ArrowLeft" type="info" size="small"
                           plain round @click="router.push('/index')">返回列表</el-button>
                <div style="text-align: center;flex: 1">
                    <topic-tag :type="topic.data.type"/>
                    <span style="font-weight: bold;margin-left: 5px">{{topic.data.title}}</span>
                </div>
            </card>
        </div>
        <div class="topic-main">
          <el-dialog
              v-model="centerDialogVisible"
              title="提示"
              width="380"
              align-center
              style="border-radius: 5px"
          >
            <div><b>{{ types }}</b><span>功能需登陆后才能访问，是否前往登录页面？...🚀</span></div>
            <template #footer>
              <div class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="router.push('/welcome')">
                  前往
                </el-button>
              </div>
            </template>
          </el-dialog>
            <div class="topic-main-left">
                <el-avatar :src="store.avatarUserUrl(topic.data.user.avatar)" :size="60"/>
                <div>
                    <div style="font-size: 18px;font-weight: bold">
                        {{topic.data.user.username}}
                        <span style="color: hotpink" v-if="topic.data.user.gender === 1">
                            <el-icon><Female/></el-icon>
                        </span>
                        <span style="color: dodgerblue" v-if="topic.data.user.gender === 0">
                            <el-icon><Male/></el-icon>
                        </span>
                    </div>
                    <div class="desc">{{topic.data.user.email}}</div>
                </div>
                <el-divider style="margin: 10px 0"/>
                <div style="text-align: left;margin: 0 5px">
                    <div class="desc">微信号: {{topic.data.user.wx || '已隐藏或未填写'}}</div>
                    <div class="desc">QQ号: {{topic.data.user.qq || '已隐藏或未填写'}}</div>
                    <div class="desc">手机号: {{topic.data.user.phone || '已隐藏或未填写'}}</div>
                </div>
                <el-divider style="margin: 10px 0"/>
                <div class="desc" style="margin: 0 5px">{{topic.data.user.desc}}</div>
            </div>
            <div class="topic-main-right">
                <div class="topic-content" v-html="convertToHtml(topic.data.content)"></div>
                <el-divider/>
                <div style="font-size: 13px;color: grey;text-align: center">
                    <div>发帖时间: {{new Date(topic.data.time).toLocaleString()}}</div>
                </div>
              <div style="font-size: 13px;color: grey;text-align: center;margin-top: 5px" v-if="topic.data.updateTime">
                <div>更新时间: {{new Date(topic.data.updateTime).toLocaleString()}}</div>
              </div>
                <div class="button" style="text-align: right;margin-top: 30px;">
                  <InteractButton name="编辑帖子" color="dodgerblue" :check="false"
                                  @check="edit = true" v-if="store.user.id === topic.data.user.id">
                    <el-icon><EditPen/></el-icon>
                  </InteractButton>
                  <Interact style="justify-content: center" name="点个赞吧" check-name="已点赞" color="red" :check="topic.like"
                                     @click="handleClick('like');types='点赞'">
                    </Interact>
                    <InteractButton name="收藏本帖" check-name="已收藏" color="orange" :check="topic.collect"
                                     style="justify-content: right"
                                    @click="handleClick('collect');types='收藏'">
                      <el-icon><Star/></el-icon>
                    </InteractButton>
                </div>
            </div>
        </div>
      <div class="topic-main" style="position: relative;">
        <!-- 评论标题和数量 -->
        <div style="display: flex; align-items: center; height: 28px; margin: 2px 5px;">
          <div style="display: flex; align-items: center;">
            <h3>评论</h3>
          </div>
          <div style="margin: 0 5px 0 6px; font-size: 16px; font-weight: 400; color: #9499A0;">{{ topic.data.comments }}</div>
        </div>

        <!-- 头像部分 -->
        <el-avatar :src="store.avatarUserUrl(store.user.avatar)" :size="48" style="position: absolute; top: 50%; transform: translateY(-50%); left: 80px;"></el-avatar>

        <!-- 按钮部分 -->
        <div style="position: absolute; top: 50%; left: 47%; transform: translate(-37%, -50%); width: 84%; text-align: center;">
          <el-button style="width: 100%;font-size: 13px;color: #9499A0" type="default" plain @click="types='评论';handleClick('comment');">{{topic.words}}</el-button>
        </div>
      </div>
      <div style="width: 100%;margin-top: 20px;font-size: 13px;color: #9499A0;text-align: center;user-select: none" v-if="topic.comments.length <= 0">占领无人区吧！！！</div>
        <transition name="el-fade-in-linear" mode="out-in">
            <div v-if="topic.comments">
                <div class="topic-main" style="margin-top: 10px" v-for="item in topic.comments">
                    <div class="topic-main-left">
                        <el-avatar :src="store.avatarUserUrl(item.user.avatar)" :size="60"/>
                        <div>
                            <div style="font-size: 18px;font-weight: bold">
                                {{item.user.username}}
                                <span style="color: hotpink" v-if="item.user.gender === 1">
                            <el-icon><Female/></el-icon>
                        </span>
                                <span style="color: dodgerblue" v-if="item.user.gender === 0">
                            <el-icon><Male/></el-icon>
                        </span>
                            </div>
                            <div class="desc">{{item.user.email}}</div>
                        </div>
                        <el-divider style="margin: 10px 0"/>
                        <div style="text-align: left;margin: 0 5px">
                            <div class="desc">微信号: {{item.user.wx || '已隐藏或未填写'}}</div>
                            <div class="desc">QQ号: {{item.user.qq || '已隐藏或未填写'}}</div>
                            <div class="desc">手机号: {{item.user.phone || '已隐藏或未填写'}}</div>
                        </div>
                    </div>
                    <div class="topic-main-right">
                        <div style="font-size: 13px;color: grey">
                            <div>评论时间: {{new Date(item.time).toLocaleString()}}</div>
                        </div>
                        <div v-if="item.quote" class="comment-quote">
                            回复: {{item.quote}}
                        </div>
                        <div class="topic-content" v-html="convertToHtml(item.content)"></div>
                        <div style="text-align: right" v-if="!isUnauthorized">
                            <el-link :icon="ChatSquare" @click="comment.show = true;comment.quote = item"
                                     type="info">&nbsp;回复评论</el-link>
                            <el-link :icon="Delete" type="danger" v-if="item.user.id === store.user.id"
                                     style="margin-left: 20px" @click="deleteComment(item.id)">&nbsp;删除评论</el-link>
                        </div>
                    </div>
                </div>
              <div style="width: 100%;margin-top: 20px;font-size: 13px;color: #9499A0;text-align: center;user-select: none" v-if="topic.comments.length > 0">我也是有底线的~</div>
                <div style="width: fit-content;margin: 20px auto">
                    <el-pagination background layout="prev, pager, next"
                                   v-model:current-page="topic.page" @current-change="loadComments"
                                   :total="topic.data.comments" :page-size="10"
                                    hide-on-single-page/>
                </div>
            </div>
        </transition>

        <topic-editor :show="edit" @close="edit = false" v-if="topic.data && store.forum.types"
                      :default-type="topic.data.type" :default-text="topic.data.content"
                      :default-title="topic.data.title" submit-button="更新帖子内容" :submit="updateTopic"/>
        <topic-comment-editor :show="comment.show" @close="comment.show = false" :tid="tid"
                              :quote="comment.quote" @comment="onCommentAdd"/>
        <div class="add-comment" @click="comment.show = true;comment.quote = null" v-if="!isUnauthorized">
          <el-icon><ChatLineRound /></el-icon>
        </div>
    </div>
</template>

<style lang="less" scoped>
.comment-quote {
    font-size: 13px;
    color: grey;
    background-color: rgba(94, 94, 94, 0.1);
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
}

.add-comment {
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    font-size: 18px;
    color: var(--el-color-primary);
    text-align: center;
    line-height: 45px;
    background: var(--el-bg-color-overlay);
    box-shadow: var(--el-box-shadow-lighter);

    &:hover {
        background: var(--el-border-color-extra-light);
        cursor: pointer;
    }
}

.topic-page {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 10px 0;
}

.topic-main {
    display: flex;
    border-radius: 7px;
    margin: 0 auto;
    background-color: var(--el-bg-color);
    width: 800px;

    .topic-main-left {
        width: 200px;
        padding: 10px;
        text-align: center;
        border-right: solid 1px var(--el-border-color);

        .desc {
            font-size: 12px;
            color: grey;
        }
    }

    .topic-main-right {
        width: 600px;
        padding: 10px 20px;
        display: flex;
        flex-direction: column;

        .topic-content {
            font-size: 14px;
            line-height: 22px;
            opacity: 0.8;
            flex: 1;
        }
    }
  .button {
    display: flex;
    justify-content: space-between;
  }
}
</style>
