# EsBoilerplate
[![](https://jitpack.io/v/Esei1541/EsBoilerplate.svg)](https://jitpack.io/#Esei1541/EsBoilerplate)

EsBoilerplate는 안드로이드 개발 환경에서 중복적으로 사용되는 코드를 사전에 정의하여 더욱 빠르고 편리한 개발을 도와줍니다.

## Contents
1. [Setup](#setup)
    - [Gradle SDK 레포지토리 설정](#gradle-sdk-repository)
    - [Dependency 설정](#set-dependency)
1. [Documentation](#documentation)
    - [BaseActivity](#base-activity)
    - [ActivityNavigator](#activity-navigator)
</br>

<h2 id="setup">⚙Setup</h2>

<h3 id="gradle-sdk-repository">Gradle SDK 레포지토리 설정</h3>

#### A) bulid.gradle (Android Studio의 버전이 Artic Fox 미만일 경우)
라이브러리를 적용할 프로젝트의 build.gradle(Project)에 다음과 같이 설정합니다.
```gradle

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

```

#### B) settings.gradle (Android Studio의 버전이 Artic Fox 이상일 경우)
라이브러리를 적용할 프로젝트의 settings.gradle(Project)에 다음과 같이 설정합니다.
```gradle

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

```

<h3 id="set-dependency">Dependency 설정</h3>
프로젝트의 build.gradle(Module)에 의존성을 추가합니다.

```gradle

dependencies {
    implementation 'com.github.Esei1541:EsBoilerplate:1.0.0-alpha-2'
}

```
</br>

<h2 id="documentation">📔Documentation</h2>

<details>
<summary><h3 id="base-activity">BaseActivity</h3></summary>

```kotlin

/**
 * @param B 해당 Activity의 DataBinding Class
 * @param VM ViewModel Class
 * @param layoutResId Layout xml의 resource ID
 */
abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int) : AppCompatActivity(), ActivityNavigator, RecyclerViewParentController 

```

DataBinding 기반 MVVM 환경에서 필요한 Activity 기능을 정의합니다.</br>
해당 클래스는 AppCompatActivity Class를 완전하게 대체합니다.</br>
본 라이브러리의 [ActivityNavigator](#activity-navigator), [RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.

#### 적용 예제

```kotlin
class ExampleActivity: BaseActivity<ActivityExampleBinding, ExampleViewModel>(R.layout.activity_example) {

    // viewModel에 의존성 주입
    // 사용자의 개발 환경에 따라 Dagger2, Koin 등의 라이브러리 사용을 추천합니다.
    override val viewModel: ExampleViewModel by viewModel { parametersOf(this) }

    // binding 객체의 setContentView 및 lifecycleOwner 설정을 입력할 필요가 없습니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventListener()
    }

    private fun initEventListener() {
        binding.apply {
            btnExample.setOnClickListener {
                viewModel.doExample()
            }
        }
    }

}

```


#### Parameters
> `protected open val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.

> `protected lateinit var binding: B`
> - 현재 클래스에 연결된 ViewDataBinding 객체를 반환합니다.

</details>
<details>
<summary><h3 id="activity-navigator">ActivityNavigator</h3></summary>

</details>



