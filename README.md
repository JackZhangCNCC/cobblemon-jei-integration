# Cobblemon JEI Integration

为 Cobblemon 模组提供 JEI 和 EMI 物品管理器集成支持的附属模组。

## 🎯 功能特性

### 🥇 核心功能

**宝可梦掉落物信息显示**
- ✅ **JEI 集成**: 在 JEI 中显示宝可梦掉落物配方
- ✅ **EMI 集成**: 在 EMI 中显示宝可梦掉落物配方
- ✅ **双向查询**: 支持通过宝可梦查看掉落物，或通过物品查看来源宝可梦
- ✅ **掉落概率**: 显示每个物品的具体掉落概率百分比
- ✅ **本地化支持**: 支持中文和英文界面

## 🏗️ 项目结构

```
cobblemon-jei-integration/
└── neoforge/                  # NeoForge 平台代码
    └── src/main/java/com/cwg/cobblemon/jei/
        ├── CobblemonJEIIntegration.java      # JEI 插件主类
        ├── CobblemonJEILogger.java           # 日志工具
        ├── CobblemonJEIConstants.java        # 常量定义
        ├── ingredient/                        # JEI 自定义成分
        │   ├── PokemonIngredient.java
        │   ├── PokemonIngredientHelper.java
        │   └── PokemonIngredientRenderer.java
        ├── recipe/                            # JEI 配方数据
        │   └── PokemonDropsRecipe.java
        ├── category/                          # JEI 配方类别
        │   └── PokemonDropsRecipeCategory.java
        ├── integration/emi/                   # EMI 集成
        │   ├── CobblemonEmiPlugin.java
        │   └── PokemonDropsEmiRecipe.java
        └── neoforge/                          # 平台入口
            └── CobblemonJEINeoForge.java
```

## 🔧 技术实现

### JEI 集成
- **自定义成分系统**: `PokemonIngredient` 用于在 JEI 中显示宝可梦
- **专用渲染器**: `PokemonIngredientRenderer` 渲染宝可梦图片和名称
- **配方类别**: `PokemonDropsRecipeCategory` 定义掉落物配方显示
- **自动注册**: 通过 `@JeiPlugin` 注解自动加载
<img width="867" height="857" alt="cji-jei" src="https://github.com/user-attachments/assets/a5d1ba20-6c4d-4d2f-8237-535eea685d99" />


### EMI 集成
- **EMI 插件**: `CobblemonEmiPlugin` 实现 EMI 插件接口
- **EMI 配方**: `PokemonDropsEmiRecipe` 适配 EMI 配方系统
- **自动发现**: 通过 `@EmiEntrypoint` 注解自动加载
- **界面优化**: 支持概率显示和文本居中对齐
<img width="814" height="819" alt="cji-emi" src="https://github.com/user-attachments/assets/df10fc09-c5a9-4772-a14f-3b04f634940b" />


## 📋 依赖要求

### 必需依赖
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.172+
- **Cobblemon**: 1.6.0+

### 可选依赖（至少需要其中一个）
- **JEI**: 19.21.1.304+ （推荐）
- **EMI**: 1.1.0+

## 🚀 开发状态

### ✅ 已完成
- [x] JEI 完整集成
  - [x] 自定义宝可梦成分系统
  - [x] 宝可梦掉落物配方显示
  - [x] 掉落概率信息显示
  - [x] 双向查询支持
- [x] EMI 完整集成
  - [x] EMI 插件实现
  - [x] 宝可梦掉落物配方显示
  - [x] 掉落概率信息显示
  - [x] 界面优化和文本居中
- [x] 多语言支持（中文/英文）
- [x] 构建配置和项目结构

## 🛠️ 构建说明

```bash
# 构建模组
./gradlew build

# 运行开发环境
./gradlew :neoforge:runClient

# 清理构建文件
./gradlew clean
```

## 🎮 使用方法

### JEI 环境下
1. 安装 JEI 和本模组
2. 在 JEI 界面中搜索任意宝可梦名称
3. 点击宝可梦图标查看掉落物信息
4. 或者搜索物品，查看哪些宝可梦会掉落该物品

### EMI 环境下
1. 安装 EMI 和本模组
2. 在 EMI 界面中找到"宝可梦掉落物"类别
3. 浏览所有宝可梦的掉落物信息
4. 支持双向查询和概率显示

## 🔧 兼容性

| 环境 | 状态 | 功能 |
|------|------|------|
| 仅 JEI | ✅ 完全支持 | 宝可梦掉落物配方显示 |
| 仅 EMI | ✅ 完全支持 | 宝可梦掉落物配方显示 |
| JEI + EMI | ✅ 兼容 | 两个集成并存工作 |
| 无物品管理器 | ⚠️ 基础功能 | 模组可加载但无集成功能 |

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 创建 Pull Request

## 📄 许可证

MIT License - 详见 LICENSE 文件

## 🙏 致谢

- **Cobblemon** - 优秀的宝可梦模组
- **JEI** - 强大的物品查询系统
- **EMI** - 现代化的物品管理器
- **NeoForge** - 优秀的模组开发平台

## 📞 支持

如有问题或建议，请在 GitHub Issues 中提出。
