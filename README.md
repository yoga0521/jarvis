# J.A.R.V.I.S

### 本地运行
在jarvis目录下执行下面脚本，在本地仓库install jarvis-dependencies-bom的模块，然后再导入整个项目的依赖

``` shell
mvn clean install -Dmaven.test.skip=true -pl jarvis-dependencies-bom -am
```

PowerShell 窗口下，执行带参数的需要’单引号’包起来，可以执行下面脚本
``` shell
mvn clean install package '-Dmaven.test.skip=true' -pl jarvis-dependencies-bom -am
```

提示：等后续上传到maven仓库就可以不需要这一步了