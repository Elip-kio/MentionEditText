# MentionEditText

一个可定制性优良支持@的输入框组件。支持定制响应字符，自定义高亮Span，输入与数据及时同步。

## 预览

![preview.gif](./images/preview.gif)

## 开始使用

### 引入依赖

暂时没上传

### 使用MentionEditText控件

```xml
    <studio.kio.mentionlibrary.MentionEditText
        android:id="@+id/et_sample"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```

```kotlin
    val myMentionEditText = MentionEditText(context)
```

### 自定义一个可被关联的数据类

```kotlin
    data class User(
        val Id: Int,
        val name: String
    )
```

### 启用@功能

```kotlin
        MentionUtil.withType(User::class)//返回MentionHandlerBuilder以支持mention事件及定制
            .attach(et_sample)//返回MentionEditTextHandler以支持插入及获取已关联的mentionItems
```

### 定制

```kotlin
    mentionHandlerBuilder.tag('@')//设置响应的字符
    .onMention(object : OnMentionInsertedListener<User> {
         override fun onMentionInserted(position: Int) {
             //用户输入了@时这里会被回调
         }
     })
    .decorate(object : MentionHandlerBuilder.MentionDecorator {
        override fun getSpan(): Any {
            //自定义高亮样式
            return ForegroundColorSpan((0xff000000 or (Math.random() * Int.MAX_VALUE).toLong()).toInt())
        }
    })
```

### 插入

```kotlin
    mentionHandler.insert(user, label, position)
```

### 获取关联的MentionItem

```kotlin
    mentionHandler.getMentionItems()//List<MentionItem>
```

##完整Demo

```kotlin
this.mentionHandler = MentionUtil.withType(User::class)
            .onMention(object : OnMentionInsertedListener<User> {
                override fun onMentionInserted(position: Int) {
                    mentionHandler.insert(user, user.name, position)
                }
            })
            .decorate(object : MentionHandlerBuilder.MentionDecorator {
                override fun getSpan(): Any {
                    return ForegroundColorSpan((0xff000000 or (Math.random() * Int.MAX_VALUE).toLong()).toInt())
                }
            })
            .tag('@')
            .attach(et_sample)

//触发@
mentionHandler.automaticallyAppend()

//获取关联的用户
mentionHandler.getMentionItems()

```