## 说明

基于安卓最新的 jetpack 技术栈实现的个人助理应用，主要用于客户端开发学习，开发的语言是 Java。

### 相关功能
| 功能点   | 功能描述        | 目前进展  |
|-------|-------------|-------|
| AI 对话 | 通用智能体、专业智能体 | MVP完成 |
| 看一看   | 短视频、长视频     | MVP完成 |
| 听一听   | 听歌、播客       | 待完成   |
| 探索    | 类似朋友圈的信息流   | 待完成   |

### 相关技术
+ MVVM
+ Material Design 3
+ LiveData
+ Room
+ Retrofit2
+ ExoPlayer

### 参考视觉稿

<img width="513" alt="image" src="https://github.com/user-attachments/assets/7be54d8b-03cc-48f9-917a-7eb1c5109e4b">

[完整地址](
https://www.figma.com/design/cvpC33GRSIFRU1dD6QrCp4/%E5%BE%AE%E4%BF%A18.0%EF%BC%8C%E7%94%A8%E6%88%B7%E7%95%8C%E9%9D%A2%E9%87%8D%E6%9E%84%EF%BD%9CMONK.REN-(Community)?node-id=337-19277&t=mTU36KK4pD6SG6bt-0)


### 准备工作

#### 关联应用
+ [chatbot 的 Java 应用](https://github.com/workdance/chatbot-core)
+ [chatbot 的 Python 应用](https://github.com/workdance/chatbot-ai)
+ [chatbot 的 React PC 应用](https://github.com/workdance/chatbot-web)

#### gradle 的安装源
+ 修改 `gradle/wrapper/gradle-wrapper.properties` 的地址为 https://mirrors.cloud.tencent.com/gradle/gradle-8.7-bin.zip


### 致谢

+ [wildfirechat提供聊天界面功能实现](https://github.com/wildfirechat/android-chat?tab=readme-ov-file)
+ [Jetpack-MVVM-Best-Practice](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice)
+ [XPopup](https://github.com/junixapp/XPopup)
+ [BigImageViewPager 提供图片查看功能](https://github.com/SherlockGougou/BigImageViewPager)


### License

项目基于 [MIT 协议](./LICENSE)，请自由地享受和参与开源。
