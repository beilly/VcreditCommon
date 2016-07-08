# VcreditCommon
VcreditCommon

[![](https://jitpack.io/v/shibenli/VcreditCommon.svg)](https://jitpack.io/#shibenli/VcreditCommon)

### How to use###

#### gradle
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

Step 2. Add the dependency
```
	dependencies {
	        compile 'com.github.shibenli:VcreditCommon:v0.1.2'
	}
```

Step 3. Add BaseApp or it's child class to AndroidManifest.xml

  may be like this:

```
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:name="com.benli.common.base.BaseApp"
        android:theme="@style/AppTheme">

    </application>
  ```
