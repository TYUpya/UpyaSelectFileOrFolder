# 自述
`UpyaSelectFileOrFolder`是一个用于挑选文件或文件夹，获取路径信息的库，可单选、多选、混选。😍

# 截图
| 单选文件 | 单选文件夹 | 单选文件和文件夹 |
| ------------ | ------------ | ------------ |
| <image src="./images/1.jpg"/> | <image src="./images/3.jpg"/> | <image src="./images/5.jpg"/> |

| 多选文件 | 多选文件夹 | 多选文件和文件夹 |
| ------------ | ------------ | ------------ |
| <image src="./images/2.jpg"/> | <image src="./images/4.jpg"/> | <image src="./images/6.jpg"/> |

# 依赖
### Gradle：

* 步骤1：将以下代码添加到工程的build.gradle中：
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

* 步骤2：添加依赖关系
```groovy
dependencies {
	...
	implementation 'com.github.TYUpya:UpyaSelectFileOrFolder:1.0.0'
}
```

### Maven:

* 步骤1：
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

* 步骤2：
```xml
<dependency>
	<groupId>com.github.TYUpya</groupId>
	<artifactId>UpyaSelectFileOrFolder</artifactId>
	<version>1.0.0</version>
</dependency>
```

# 使用介绍
* 调用示例之 单选文件
```java
public void onClick01(View view) {
	new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FILE,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
			}
		}).show();
}
```

* 调用示例之 多选文件
```java
public void onClick02(View view) {
	new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FILE,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：\n");
				for (File file : selectedFileList) {
					mainText.append(file.getAbsolutePath());
					mainText.append("\n");
				}
			}
		}).show();
}
```

* 调用示例之 单选文件夹
```java
public void onClick03(View view) {
	new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FOLDER,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
			}
		}).show();
}
```

* 调用示例之 多选文件夹
```java
public void onClick04(View view) {
	new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FOLDER,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：\n");
				for (File file : selectedFileList) {
					mainText.append(file.getAbsolutePath());
					mainText.append("\n");
				}
			}
		}).show();
}
```

* 调用示例之 单选文件和文件夹
```java
public void onClick05(View view) {
	new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_UNLIMITED,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
			}
		}).show();
}
```

* 调用示例之 多选文件和文件夹
```java
public void onClick06(View view) {
	new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_UNLIMITED,
		new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
			@Override
			public void onSelectFileOrFolder(List<File> selectedFileList) {
				mainText.setText("选择的文件：\n");
				for (File file : selectedFileList) {
					mainText.append(file.getAbsolutePath());
					mainText.append("\n");
				}
			}
		}).show();
}
```

# 混淆规则
```txt
-dontwarn vip.upya.lib.sfof.**
-keep class vip.upya.lib.sfof.**{*;}
```

# 感谢
😂nobody🤣

# License
```text
Copyright 2020 TYUpya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
