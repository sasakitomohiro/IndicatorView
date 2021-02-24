# IndicatorView

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Maven Central](https://img.shields.io/maven-central/v/com.github.sasakitomohiro/indicatorview)

<img src="indicator.gif" />

## Usage

### dependency

```build.gradle
dependencies {
    implementation 'com.github.sasakitomohiro:indicatorview:latest'
}
```

### sample

```xml
<com.github.sasakitomohiro.indicatorview.IndicatorView
            android:id="@+id/indicator"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
```

```kt
binding.root.doOnLayout {
    binding.indicator.count = 5
    binding.indicator.selectedIndex = 0
}
```
