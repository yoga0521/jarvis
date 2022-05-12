# universe

### 本地运行
在universe目录下执行下面脚本，在本地仓库install universe-dependencies-bom的模块，然后再导入整个项目的依赖

``` shell
mvn clean install -Dmaven.test.skip=true -pl universe-dependencies-bom -am
```

提示：等后续上传到maven仓库就可以不需要这一步了