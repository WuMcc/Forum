<script setup>
import ToolCard from "@/components/ToolCard.vue";
const hotList = {
  "秘塔写作猫":['https://xiezuocat.com/','https://www.tboxn.com/wp-content/uploads/2024/08/1722585029-be53080dac6d1f24d8a877c4d1af0147.jpeg','AI写作辅助神器',['AI','写作猫']],
  "AI灵感PPT": ['https://xiezuocat.com/','https://www.tboxn.com/wp-content/uploads/2024/03/微信图片_20240306175603.png','一键PPT生成，全套AI办公工具',['AI','写作猫']],
  "Zlibrary": ["https://z-lib.yt/?ref=www.tboxn.com",'https://www.tboxn.com/wp-content/uploads/2022/02/图书-1.png','全球最大的数字图书馆，电子书下载！',['Zlibrary','zhelper search']],
  "Kimi智能助手": ["https://kimi.moonshot.cn",'https://www.tboxn.com/wp-content/uploads/2024/05/1715867605-logo.png','超强AI写作助手，一键总结20w字长文，支持批量文档上传，多端同步内容不怕丢失。论文综述、文档速读、脚本小说创作，统统交给Kimi！实时联网搜索，给你最智能清晰的解答。',['AI写作','AI阅读','Kimi']],
  "有梗蛙": ["https://www.yougengwa.com/",'https://www.tboxn.com/wp-content/uploads/2023/09/yougengwa-log.png','十万个梗百科知识库，网络流行用语',['有梗蛙','梗百科','网络流行用语']],
  "超级书签": ["https://www.maxiaobang.com/superbookmark",'https://www.tboxn.com/wp-content/uploads/2022/01/icon_c_%E4%B9%A6%E7%AD%BE_%E7%BB%BF.png','浏览器书签也能变超级工具',['商品历史价格查询','网页自由编辑']]}

const useList = {
  "人格测试":['https://www.16personalities.com/ch','https://www.tboxn.com/wp-content/uploads/2023/10/m1Pspaw0_400x400.jpg','免费的16型人格测试工具',['16型人格测试','e人']],
  "网速测试": ['https://www.speedtest.cn/','https://www.tboxn.com/wp-content/uploads/2022/07/%E6%B5%8B%E9%80%9F%E7%BD%91.png','网络速度测试网站',['测速网','网络测速']],
  "钛盘": ["https://tmp.link/",'https://www.tboxn.com/wp-content/uploads/2022/06/TMP.png','免费的在线临时网盘服务，无需注册登录',['临时网盘']],
  "朋友圈集赞生成器": ["https://ourboy.cn/dz",'https://www.tboxn.com/wp-content/uploads/2023/09/%E6%9C%8B%E5%8F%8B%E5%9C%88.png','朋友圈集赞生成器免费版',['微信','朋友圈','集赞']],
  "奈飞工厂": ["https://www.netflixgc.net/",'https://www.tboxn.com/wp-content/uploads/2024/04/1712770372-143870_netflix_icon.png','奈飞影视动漫在线免费播放平台',['Netflix','影视']],
  "i Love PDF": ["https://www.ilovepdf.com/zh-cn",'https://www.tboxn.com/wp-content/uploads/2022/05/202112161157422711.png','PDF文件在线处理工具',['PDF','在线处理']],
  }

const studyList = {
  "慕课网":['https://www.icourse163.org/','https://ts1.cn.mm.bing.net/th/id/R-C.416bf571bd617590599a8de0b542a7ef?rik=oajI7rMwiBZMMw&riu=http%3a%2f%2fpic.downyi.com%2fupload%2f2017-9%2f2017921843188034.png&ehk=e1zqnv4sw%2fFh3deA%2bU7SLrWGqcr9osRVApWVYG9aMps%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1','中国大学MOOC(慕课) 是爱课程网携手网易云课堂打造的在线学习平台，每一个有提升愿望的人，都可以在这里学习中国优质的大学课程，学完还能获得认证证书',['慕课','学习']],
  "哔哩哔哩|学习": ['https://www.bilibili.com/','https://www.tboxn.com/wp-content/uploads/2022/02/icon_bilibili-circle-1.png','B站大学！！！',['大学','B站']],
  "菜鸟教程": ["https://www.runoob.com/",'https://www.kxxue.com/wp-content/uploads/2022/04/9003c-www.runoob.com.png','菜鸟教程(www.runoob.com)提供了编程的基础技术教程, 介绍了HTML、CSS、Javascript、Python，Java，Ruby，C，PHP , MySQL等各种编程语言的基础知识。 同时本站中也提供了大量的在线实例，通过实例，您可以更好的学习编程。..',['编程','菜鸟教程']],
  "框框大学": ["https://www.kkdaxue.com/",'https://www.tboxn.com/wp-content/uploads/2022/11/%E6%A1%86%E6%A1%86%E5%A4%A7%E5%AD%A6.png','来自上千所大学前辈们的学习建议',['框框大学','好好学习']],
  "白马程序员": ["https://www.itbaima.cn/",'https://www.itbaima.cn/image/webapp/icon-512x.webp','0基础,月薪过万，就来白马程序员',['Java','编程']],
  "打字打字": ["https://dazidazi.com/",'https://ts1.cn.mm.bing.net/th?id=ODLS.018b31b4-1b1b-4820-8022-652ef5311328&w=32&h=32&qlt=90&pcl=fffffa&o=6&pid=1.2','在线打字练习平台',['打字','在线打字']],
}

const aiList = {
  "ChatGPT镜像网址":['https://chat18.aichatos.xyz/#/chat/','https://www.tboxn.com/wp-content/uploads/2024/05/1716468182-ic_chatgpt_active-copy.png','OpenAI研发的大语言模型国内镜像',['AI','ChatGPT','镜像']],
  "讯飞星火": ['https://xinghuo.xfyun.cn/','https://www.tboxn.com/wp-content/uploads/2023/09/%E6%98%9F%E7%81%AB.png','讯飞星火大语言模型，支持聊天对话、写文章、AI绘画',['AI','讯飞星火']],
  "灵境矩阵": ["https://agents.baidu.com/",'https://www.tboxn.com/wp-content/uploads/2024/01/%E7%81%B5%E5%A2%83%E7%9F%A9%E9%98%B5.png','基于文心大模型的智能体（Agent）平台，0代码开发专属自己的智能应用',['GPTs','灵境矩阵']],
  "AutoDraw": ["https://www.autodraw.com/",'https://www.autodraw.com/assets/images/logo.gif','探索Google AI绘画工具，快速素描艺术创作。',['AI','绘画']],
  "博查AI搜索": ["https://bochaai.com/",'https://bochaai.com/_next/image?url=%2Flogo-blue.png&w=256&q=75','AI搜索引擎，兼容多种模型，高效智能检索。',['搜索引擎','AI']],
  "通义法睿": ["https://tongyi.aliyun.com/farui",'https://img.alicdn.com/imgextra/i1/O1CN01u53mAi1dfVJtHpYov_!!6000000003763-55-tps-251-59.svg','阿里推出AI法律顾问助手，免费提供法律咨询。',['法律助手','AI']],
}

const gameList = {
  "桌游合集":['https://game.hullqin.cn/','https://www.tboxn.com/wp-content/uploads/2024/05/1715192089-%E6%A1%8C%E6%B8%B8%E5%90%88%E9%9B%86.png','线下聚会多人在线联机游戏平台',['休闲娱乐','聚会']],
  "人生重开模拟器": ['https://liferestart.syaro.io/public/index.html','https://www.tboxn.com/wp-content/uploads/2022/09/%E7%A7%91%E5%AD%A6%E6%80%AA%E4%BA%BA.png','人生能够不断重开的模拟器',['休闲娱乐','人生']],
  "沙雕新闻": ["https://shadiao.plus",'https://www.tboxn.com/wp-content/uploads/2022/04/1055019_news_article_information_newspaper_periodical_icon.png','专注于搬运搞笑奇葩新闻的网站',['搞笑网站','搬运']],
  "Poki": ["https://poki.cn/",'https://www.tboxn.com/wp-content/uploads/2022/03/fav-256.png','两万个免费的网页小游戏',['poki','在线游戏']],
  "柠檬精": ["http://lemonjing.com",'https://www.tboxn.com/wp-content/uploads/2022/08/iconfont.png','趣味游戏集合网站',['模拟器','休闲娱乐']],
  "4399": ["https://www.4399.com/",'https://ts1.cn.mm.bing.net/th?id=ODLS.ab9e6dd1-032a-4735-adcf-a75a778f77da&w=32&h=32&qlt=90&pcl=fffffa&o=6&pid=1.2','00后steam！！！',['游戏','娱乐','4399']],
}
</script>


<template>
  <div style="margin: auto;max-width: 80%">
    <div id="item-1" style="margin-top: 20px">
      <el-button size="large" type="danger" round disabled>热门工具</el-button>
      <div style="display: flex;gap: 20px;flex-wrap: wrap;justify-content: flex-start">
        <tool-card
            v-for="(value, key) in hotList"
            :key="key"
            :name="key"
            :url=value[0]
            :img=value[1]
            :desc=value[2]
            :tags=value[3]
        />
      </div>
    </div>
    <div id="item-2" style="margin-top: 20px">
      <el-button type="primary" size="large" round disabled>实用工具</el-button>
      <div style="display: flex;gap: 20px;flex-wrap: wrap;justify-content: flex-start">
        <tool-card
            v-for="(value, key) in useList"
            :key="key"
            :name="key"
            :url=value[0]
            :img=value[1]
            :desc=value[2]
            :tags=value[3]
        />
      </div>
    </div>
    <div id="item-3" style="margin-top: 20px">
      <el-button size="large" type="success" round disabled>好好学习</el-button>
      <div style="display: flex;gap: 20px;flex-wrap: wrap;justify-content: flex-start">
        <tool-card
            v-for="(value, key) in studyList"
            :key="key"
            :name="key"
            :url=value[0]
            :img=value[1]
            :desc=value[2]
            :tags=value[3]
        />
      </div>
    </div>
    <div id="item-4" style="margin-top: 20px">
      <el-button size="large" type="warning" round disabled>AI工具</el-button>
      <div style="display: flex;gap: 20px;flex-wrap: wrap;justify-content: flex-start">
        <tool-card
            v-for="(value, key) in aiList"
            :key="key"
            :name="key"
            :url=value[0]
            :img=value[1]
            :desc=value[2]
            :tags=value[3]
        />
      </div>
    </div>
    <div id="item-5" style="margin-top: 20px">
      <el-button size="large" type="info" round disabled>休闲娱乐</el-button>
      <div style="display: flex;gap: 20px;flex-wrap: wrap;justify-content: flex-start">
        <tool-card
            v-for="(value, key) in gameList"
            :key="key"
            :name="key"
            :url=value[0]
            :img=value[1]
            :desc=value[2]
            :tags=value[3]
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.dark .instance-card {
  color: #d9d9d9;
}
.interact-item {
  transition: .3s;

  &:hover {
    cursor: pointer;
    scale: 1.1;
    opacity: 0.8;
  }
}
.instance-card {
  width: 320px;
  padding: 15px;
  background-color: var(--el-bg-color);
  border-radius: 5px;
  box-sizing: border-box;
  color: #606060;
  transition: .3s;

  &:hover {
    cursor: pointer;
    scale: 1.02;
  }

  .name {
    font-size: 15px;
    font-weight: bold;
  }

  .status {
    font-size: 14px;
  }

  .os {
    font-size: 13px;
    color: grey;
  }

  .network {
    font-size: 13px;
  }

  .hardware {
    margin-top: 5px;
    font-size: 13px;
  }

  .progress {
    margin-top: 10px;
    font-size: 12px;
  }

  .network-flow {
    margin-top: 10px;
    font-size: 12px;
    display: flex;
    justify-content: space-between;
  }
  .cpu {
    font-size: 13px;

  }
}
</style>