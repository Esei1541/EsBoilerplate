# EsBoilerplate
[![](https://jitpack.io/v/Esei1541/EsBoilerplate.svg)](https://jitpack.io/#Esei1541/EsBoilerplate)

EsBoilerplate는 안드로이드 개발 환경에서 중복적으로 사용되는 코드를 사전에 정의하여 더욱 빠르고 편리한 개발을 도와줍니다.

## Contents
1. [Setup](#setup)
    - [Gradle SDK 레포지토리 설정](#gradle-sdk-repository)
    - [Dependency 설정](#set-dependency)
1. [Documentation](#documentation)
    - [ActivityNavigator](#activity-navigator)
    - [BaseActivity](#base-activity)
    - [BaseActivityForViewBinding](#base-activity-for-view-binding)
    - [BaseApplication](#base-application)
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
<summary><h3 id="activity-navigator">ActivityNavigator</h3></summary>

ViewModel에서 View 클래스의 의존성을 방지하면서 Activity 기능에 접근하기 위한 interface입니다.</br>
일반적으로 Activity 등 View 클래스에 상속하여 구현합니다.</br>
라이브러리 내 [BaseActivity](#base-activity)에 기본적으로 구현되어 있습니다.

>`val context: Context`
>- View 클래스의 context를 반환하도록 구현합니다.

>`val activity: AppCompatActivity`
>- Activity 객체를 반환하도록 구현합니다.

>`fun onBackPressed()`</br>
>`fun clearFocus()`</br>
>`fun finish()`</br>
>`fun finishAffinity()`</br>
>`fun toast(res: Int)`</br>
>`fun toast(string: String)`</br>
>`fun startActivity(intent: Intent)`</br>
>`fun startActivity(intent: Intent?, options: Bundle?)`</br>
>- Activity의 특정 기능에 빠르게 접근하기 위한 shortcut function입니다. 해당 function을 호출하도록 구현합니다.

</details>

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
[ActivityNavigator](#activity-navigator), [RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.

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
> - override를 통해 문자열을 재정의할 수 있습니다.

> `protected lateinit var binding: B`
> - 현재 클래스에 연결된 DataBinding 객체를 반환합니다.

</details>

<details>
<summary><h3 id="base-activity-for-view-binding">BaseActivityForViewBinding</h3></summary>

``` kotlin

/**
 * @param B 해당 Activity의 ViewBinding Class
 * @param inflate ViewBinding을 inflate하는 함수 (ActivityXXX::inflate를 넘겨주면 됨)
 */
public abstract class BaseActivityForViewBinding<B : ViewBinding>(private val inflate: ActivityInflater<B>) : AppCompatActivity(),
    RecyclerViewParentController

```

ViewBinding 기반 개발 환경에서 필요한 Activity 기능을 정의합니다.</br>
해당 클래스는 AppCompatActivity Class를 완전하게 대체합니다.</br>
[RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.</br>

#### 적용 예제
```kotlin

class ExampleActivity : BaseActivityForViewBinding<ActivityExampleBinding>(ActivityExampleBinding::inflate) {

    // binding 객체 및 setContentView를 설정할 필요가 없습니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}

```


#### Parameters

> `protected open val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.
> - override를 통해 문자열을 재정의할 수 있습니다.

> `protected val binding: B`
> - 현재 클래스에 연결된 ViewBinding 객체를 반환합니다.

</details>

<details>
<summary><h3 id="base-application">BaseApplication</h3></summary>

```kotlin

/**
 * @param preferenceMasterKey preference 암호화를 위한 마스터 키
 * @param preferenceName preference 파일명. 지정하지 않을 경우 packageName이 들어간다.
 * @param progressTintColor 로딩 다이얼로그의 색상
 */
public class BaseApplication(
    private val preferenceMasterKey: String,
    private val preferenceName: String? = null,
    private val progressTintColor: Int = R.color.esboiler_primary
): Application()

```

전역 Application 클래스에서 주로 사용하는 기능을 정의합니다.</br>
SharedPreperence에 접근 가능한 Manager 클래스를 관리하고 기본적인 ProgressDialog의 출력을 설정할 수 있습니다.</br>

#### Parameters

> `protected val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.


#### Functions

> `public fun getPreferenceManager(): PreferenceManager`
> - [PreferenceManager](#preference-manager)객체를 반환합니다.
> - 객체가 존재하지 않을 경우 초기화합니다.

> `public fun showProgressDialog(fragmentManager: FragmentManager)`
> - 화면에 로딩 다이얼로그를 보여주고 유저의 조작을 일시적으로 차단합니다.
> - 작업이 끝나면 반드시 `dismissProgressDialog()`를 호출하여 다이얼로그를 종료해야 합니다.
> - activity의 fragmentManager를 param으로 받습니다.

> ` public fun showProgressDialog(fragmentManager: FragmentManager, statusBarColor: Int, isLightStatusBar: Boolean)`
> - 화면에 로딩 다이얼로그를 보여주고 유저의 조작을 일시적으로 차단합니다.
> - 작업이 끝나면 반드시 `dismissProgressDialog()`를 호출하여 다이얼로그를 종료해야 합니다.
> - activity의 fragmentManager를 param으로 받습니다.
> - 다이얼로그 출력 중 statusBar의 색상 및 light theme 여부를 지정할 수 있습니다.

> `public fun dismissProgressDialog()`
> - 현재 출력되고 있는 로딩 다이얼로그를 종료하고 유저의 조작을 허용합니다.

</details>


