# Homeward Cookings


<p align="center">
<img src = "https://user-images.githubusercontent.com/39553613/145566584-494920e6-bdac-4c23-985d-9d00187c90e1.jpg">
</p>

<h4 align="center">一个可以通过尝试配方来制作出不同菜品的厨艺插件 ？</h4>
<p align="center">
<a href="https://www.codefactor.io/repository/github/caishangqi/homeward-webstorebridge/overview/plugin-webstore-bridge"><img src="https://www.codefactor.io/repository/github/caishangqi/homeward-webstorebridge/badge/plugin-webstore-bridge" alt="CodeFactor" /></a>
<img alt="Lines of code" src="https://img.shields.io/tokei/lines/github/Caishangqi/homeward-plugin-cooking">
<img alt="Lines of code" src="https://img.shields.io/badge/paper-1.18.2-green">
<img alt="GitHub branch checks state" src="https://img.shields.io/github/checks-status/Caishangqi/homeward-plugin-cooking/master?label=build">
<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/Caishangqi/homeward-plugin-cooking">
</p>

## 介绍 Introduction
使用 **4格配方** 任意搭配任何食材进行烹饪，指定的搭配会在制作完成后解锁新的配方并记录在食谱中，如果你的配方没有与配置的配方符合你将获得默认菜系或者失败的菜系。你的制作不一定是要百分之百与配方核对，配方是动态的，有些配方只需要你满足若干个原料相同即可做出相同菜系！

<p align="center">
  <img alt="cookingpot" src="https://user-images.githubusercontent.com/39553613/180586916-6fa340a1-f8c6-4fc4-ad34-d56a5e707ee8.gif">
</p>

## 特性

- 最多可以支持4个原材料
- 可以自定义每种配方的烹饪时间或者基于每种材料的烹饪时间进行计算
- 可以配置无限的副产物或者指令等等
- 产出不仅仅是物品，也可以是指令
- 支持单个GUI界面或者储存容器的形式还原容器制作！
- 完全脱离数据库，数据储存在世界文件中
- 将玩家解锁的特殊配方储存到数据库
- 支持多个插件的物品 ItemsAdder MMOItems Oraxen 等

## 指令
`/hwc` 为插件主指令

`/hwc give <玩家> <数量>` 给予指定玩家若干个烹饪锅

`/hwc checkPool` 检查当前GUI池里的GUI数量

`/hwc removePool` 移除当前GUI池里的所有缓存GUI

`/hwc recipes` 查看当前成功加载的配方

`/hwc dictionary` 查看当前成功加载的词典

`/hwc onprocess` 查看当前正在烹饪的配方

`/hwc reload <dictionary | recipe>` 重载插件，重载插件词典或者重载插件配方

`/hwc clearredundant` 清除由于插件错误导致的多于数据

## 简单上手

- 将插件丢入 `plugin` 文件夹下
- 输入指令 `/hwc give <玩家> <数量>` 获取烹饪锅
- 将烹饪锅放置到地面，打开它放入配方
- 点击 `开始烹饪` !

## 未来计划 TODO
- 提供完成烹饪，开始烹饪，烹饪中的API
- 新增燃料设置，可以在配置文件中取消
- 烹饪锅的升级 和 玩家烹饪属性与mcmmo和mmocore专业等级兼容
- 原版模式下拥有美观的物品悬浮效果

## 协调系列 Homeward Species

| [Homeward Cooking 协调烹饪](https://github.com/Caishangqi/homeward-plugin-cooking) | Caishangqi | 1.18.2 |
|-----------------------|------------|--------|
| **[Homeward Brewing 协调酿造](https://github.com/Ba1oretto/Brewing)** |  **Ba1oretto**          | **1.18.2** |
| **[Homeward Libs 协调核心](https://github.com/Caishangqi/homeward-plugin-lib)**    | **Caishangqi** | **1.18.2** |
| **[Homeward InfoBar 协调浮窗](https://github.com/Caishangqi/homeward-plugin-infobar)** | **Caishangqi** | **1.18.2** |

## 特别说明 Special Information

这是一个专门给服务器使用的插件，主要需求会依照服务器需求进行配置，您的需求可能会被延后，感谢谅解。

_This is a plugin specially used for the server. The main requirements will be configured according to the server
requirements. Your requirements may be delayed. Thank you for your understanding._

## 特别鸣谢 Special Thanks

[Baioretto](https://github.com/Ba1oretto) 的各项高级JVM特性支持，在 [这里](https://github.com/Ba1oretto/Brewing) 可以查看与这个插件类型类似的出色作品。
