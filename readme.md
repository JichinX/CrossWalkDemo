## CrossWalkSdk
说明：
1. 该sdk使用crosswalk内核，集成集体庞大，仅为Android5.0以下系统适配使用，
2. Android5.0以上系统的自带Webview控件使用chrome内核基本可以满足要求，后续会进行适配
### 与Native交互协议
说明：在本SDK内，可自定义多种协议，以及专属的解析方式，如无特殊要求，则使用默认的协议 -- codvision
#### 一、协议组织方式
协议是按照一定规范组织行程的字符串
本SDK采用协议形式为Uri形式
> 协议组织格式：
>`[scheme:][//host:port][path][?query][#fragment]`
>1. **[scheme]** 为协议解析的入口，可以自定义添加，本SDK默认协议为"codvision",即使用该协议中方法的方式是：
>`codvision://`
>2. **[host:port][path]** 为Authority部分，在不需要复杂解析的情况下，可以使用【authority】代替，比如在本SDK默认协议**codvision**中，就是`codvision://sayHello`,其中**sayHello**,就是对应的方法Key.
> 3. **[?query]** 部分会被解析为方法参数，均为String格式，具体解析成的数据类型，根据方法所需来转换，但顺序**必须一致**
> 4. **[fragment]** 在不需要复杂解析的协议内不需要进行处理，再具体协议内，会有特殊说

注意：
#### 二、协议使用方式
说明：根据webview的特点，使用三种方式实现，分别对应三种不同环境下的使用
1. 使用Webview原生注解形式 --@JavascriptInterface，java层对应的是含有注解类对应的key,和该类的实例：
```java
//java
public class DefaultJsExpose {
    public static final String ExposeName = "DefaultInterface";

    @JavascriptInterface
    public String sayHello() {
        return "Hello from Native";
    }
}
 //设置默认的js入口
mWebView.addJavascriptInterface(new DefaultJsExpose(), DefaultJsExpose.ExposeName);
```
javaScript中使用给出的对象名称即可
```javascript
 function simpleToast() {
      var result = DefaultInterface.sayHello();
      ...
      ...
    }
```
使用场景：适用于能立刻获得结果的同步性请求
2. 拦截Url,将符合协议要求的请求进行拦截，而进行自处理，在Java层面是将网页的链接请求进行拦截后，进行解析处理
```java
   @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        LogTool.d("shouldOverrideUrlLoading:" + url);
        Uri uri = Uri.parse(url);
        //自定义协议
        if (schemesMap.containsKey(uri.getScheme())) {
            schemesMap.get(uri.getScheme()).analysis(uri);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
```
js层面，只要封装好符合协议格式的url,进行请求即可
```js
document.location = "codvision://sayHello?name=yuhang&arg2=23"
```
使用场景：纯粹的单方面通知类型、或传递数据类型逻辑。注意是js->java 单方面
3. 原理同2，只不过是通过拦截js中prompt请求，比2更好用的地方，在于可以实现数据异步回调
java 层面变化不大
```java
    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult
            result) {
        LogTool.d("shouldOverrideUrlLoading:" + url);
        Uri uri = Uri.parse(message);
        //自定义协议
        if (schemesMap.containsKey(uri.getScheme())) {
            schemesMap.get(uri.getScheme()).analysis(uri, result);
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
```
js层面也只是从ur请求，转换为prompt请求
```js
    var result = prompt("codvision://sayHello?name=xujichang&arg2=23");
     ...
     ...
```
#### 三、协议方法介绍
##### 1. 默认协议（DefaultInterface）不算是协议
```js
DefaultInterface.sayHello();
```
说明：
DefaultInterface为注入js的对象，js对象
sayHello为方法名，可以携带参数

*待根据需求进行补充*
##### 2. codvision协议
2.1 sayHello测试：
```js 
codvision://sayHello?name=xujichang
```
说明：
>codvision 为解析类型    
>sayHello 为对应方法标志   
>name=xujichang 表示sayHello对应的方法的参数为name，值为xujichang

2.2 **obtainFile**,获取手机内文件
```html
codvision://obtainFile?type=image&[mime_type=?]
```
说明：   
>obtainFile 方法标志   
type为获取文件的类型，其值可以为image、video、audio、txt，其中 image已实现     
mime_type 可选，表示所需文件的具体类型，若与type冲突，以type为准

返回数据：   
正确
```json
{
  "code": 200,
  "data": {
    "locPath": "/storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg",
    "status": 1,
    "timeStamp": 1528962416382
  },
  "msg": "成功"
}
```
错误
```json
{
  "code": 300,
  "data": {
    "status": 3,
    "timeStamp": 1528962497792
  },
  "msg": "对象或集合为空"
}
```
2.3 **obtainLocation**获取手机位置信息
```
codvision://obtainLocation?type=1
```
说明：  
>obtainLocation 方法标志  
>type 请求方式   
>0 立即请求-表示 取手机中缓存的上次获取的位置信息    
>1 重新请求-表示 需要重新调用位置获取方法，需要一段时间。

返回数据：   
正确
```json
{
  "code": 200,
  "data": {
    "lat": 120.12313456464,
    "lng": 30.156464
  },
  "msg": "成功"
}
```
错误
```json
{
  "code": 100,
  "msg": "请求的操作被取消"
}
```
2.4 显示文件信息
```
codvision://[img|]/...
```
说明：     
因机制原因，并不能根据类似于"/storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg"的路径直接显示文件信息，
所以采取根据协议去拦截的方法
>uri字符串中的Authority部分为需要显示的文件类型，与2.2获取文件相同可省略，值可以为img、video、audio、txt.

注意：以下两种协议字符串解析出的结果是不同的
```html
//1
codvision://img//storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg
//2
codvision://storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg
```
初看 2 是 1 的省略Authority部分之后的字符串，但解析的结果不一样     
1 解析后 Authority部分是img, path 是 /storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg       
2 解析后 Authority部分是storage，path 是/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg     
因为path 是不同的，所以 这样的'省略'Authority部分是不正确的。正确应该是：       
```
codvision:///storage/emulated/0/DCIM/Camera/IMG_20180515_111539.jpg
```
*待根据需求进行补充*


