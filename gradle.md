# Gradle 备忘录

## 5个基础知识点
- Gradle是一个通用的构建工具，与要构建的项目无关，具体的项目通过自定义插件来提供功能
- Gradle的核心模型基于任务，Gradle把构建工作抽象成一个由任务构成的DAG。Gradle在配置阶段所做的工作就是配置一系列任务并基于他们之间的依赖构建一副DAG。
  - 每个任务由多个动作，输入，输出组成
- Gradle有固定的构建步骤
  - 初始化：为项目生成Project对象，决定哪些项目参与构建
  - 配置：根据构建脚本配置Project对象，构建并且配置任务图
  - 执行：执行任务
- Gradle支持多种扩展方式
  - 自定义任务
  - 自定义动作
  - 可以给项目和任务添加额外的属性
  - 自定义规范：
  - 自定义模型：比如源集
- 通过API的方式配置构建过程
  - 构建脚本用来描述构建过程有哪些步骤，至于每一步应该怎么做是任务应该关心的。
  - Gradle的强大与灵活性来自于构建脚本是可执行的代码

## 参考资料
- [What is Gradle?](https://docs.gradle.org/current/userguide/what_is_gradle.html#the_core_model_is_based_on_tasks)
