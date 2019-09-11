# sunny-spring-boot-starter

这个项目的主要功能是开发spring boot的启动器的

### starter开发进度

- [x] oss-spring-boot-starter


### gradle 依赖规则

  * compileOnly - 对于编译生产代码所必需的依赖项，但不应该是运行时类路径的一部分
  
  * implementation（取代compile） - 用于编译和运行时
  
  * runtimeOnly（取代runtime） - 仅在运行时使用，不用于编译
  
  * testCompileOnly- compileOnly除了测试之外
  
  * testImplementation - 测试相当于 implementation
  
  * testRuntimeOnly - 测试相当于 runtimeOnly