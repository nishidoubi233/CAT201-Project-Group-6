# CAT201-Project

## Git 配置教程

### 1. 安装 Git
1. 访问 [Git官网](https://git-scm.com/downloads) 下载适合你操作系统的 Git 版本
2. 运行安装程序，全部选择默认选项即可

### 2. 配置 Git
打开命令行（就是终端，比如vscode里面的终端），输入以下命令：

```bash
git config --global user.name "你的名字"
git config --global user.email "你的邮箱"
```

### 3. 克隆项目
1. 创建一个用于存放项目的文件夹
2. 在该文件夹中右键选择 "Git Bash Here"（或在命令行中导航到该文件夹）
3. 输入以下命令克隆项目：

```bash
git clone https://github.com/nishidoubi233/CAT201-Project-Group-6.git
```

### 4. 日常使用 Git 的工作流程

#### 开始工作前：

bash
切换到项目目录
cd CAT201-Project-Group-6
获取最新代码
git pull origin main

#### 提交你的修改：

```bash
git add .

git commit -m "你的修改描述"


git push origin main
```

### 5. 常见问题解决

#### 如果出现冲突：
1. 先执行 `git pull origin main`
2. 解决冲突文件中的冲突标记
3. 重新添加和提交修改
4. 推送到 GitHub

#### 如果想撤销修改：
- 撤销未暂存的修改：`git checkout -- 文件名`
- 撤销已暂存的修改：`git reset HEAD 文件名`

### 6. 重要提示
- 每次开始工作前先 pull
- 经常 commit 和 push
- commit 信息要清晰明了
- 如有任何问题，及时与团队沟通
测试111