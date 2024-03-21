# EsBoilerplate
[![](https://jitpack.io/v/Esei1541/EsBoilerplate.svg)](https://jitpack.io/#Esei1541/EsBoilerplate)

EsBoilerplate는 안드로이드 개발 환경에서 중복적으로 사용되는 코드를 사전에 정의하여 더욱 빠르고 편리한 개발을 도와줍니다.

# Contents
1. [Setup](#setup)
 - [Gradle SDK 레포지토리 설정](#gradlesdkrepository)
 - [Dependency 설정](#setdependency)
2. [Documentation](#documentation)
 - [ActivityBundleEventModel](#activitybundleeventmodel)
 - [ActivityResultEventModel](#activityresulteventmodel)
 - [BaseActivity](#baseactivity)
 - [BaseActivityForViewBinding](#baseactivityforviewbinding)
 - [BaseFragment](#basefragment)
 - [BaseFragmentForViewBinding](#basefragmentforviewbinding)
 - [BaseViewModel](#baseviewmodel)
 - [PreferenceManager](#preferencemanager)
 - ~~[RecyclerViewBaseAdapter](#recyclerviewbaseadapter)~~
 - [RecyclerViewParentController](#recyclerviewparentcontroller)
 - ~~[SimplePermissionChecker](#simplepermissionchecker)~~
 - [Extension Functions](#extensionfunctions)


# <a id="setup"></a>⚙Setup

### <a id="gradlesdkrepository"></a>Gradle SDK 레포지토리 설정

#### A) bulid.gradle (Android Studio의 버전이 Artic Fox 미만일 경우)
라이브러리를 적용할 프로젝트의 build.gradle(Project)에 다음과 같이 설정합니다.

```groovy 
allprojects {
   repositories {
      maven { url 'https://jitpack.io' }
   } 
}
``` 

#### B) settings.gradle (Android Studio의 버전이 Artic Fox 이상일 경우)
라이브러리를 적용할 프로젝트의 settings.gradle(Project)에 다음과 같이 설정합니다.

```groovy
dependencyResolutionManagement { 
   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
   repositories {
      google() 
      mavenCentral() 
      maven { url 'https://jitpack.io' } 
   } 
} 
``` 

### <a id="setdependency"></a>Dependency 설정
프로젝트의 build.gradle(Module)에 의존성을 추가합니다.

```groovy 
dependencies {
   implementation 'com.github.Esei1541:EsBoilerplate:1.1.0-alpha' 
} 
``` 
<br>

# <a id="documentation"></a> 📔Documentation

## ActivityBundleEventModel

[BaseViewModel](#baseviewmodel) 또는 [BaseActivity](#baseactivity")/[BaseFragment](#basefragment)에서 다른 [BaseAcitivy](#baseactivity)를 실행할 때 데이터를 전달하기 위한 Model Class입니다.

내부에 HashMap를 상속한 [EventDataBundle](#eventdatabundle) 객체를 포함하고 있으며, 이를 통해 원하는 데이터를 전달합니다.

일반적인 경우, 내부 구현이 되어있는 [BaseViewModel](#baseviewmodel)의 `handleStartActivityEvent` 함수를 통한 접근을 권장합니다.

### Public Constructors

```kotlin 
class ActivityBundleEventModel(val activityClass: Class<*>, val launcherKey: String? = null) 
``` 

`val activityClass: Class<*>`
> 실행할 Activity Class를 정의합니다.
>
> Class 객체를 SomeActivity::class.java 형태로 전달해야 합니다.

`val launcherKey: String?`
>전달된 Key가 [BaseActivity](#baseactivity) 또는 [BaseFragment](#basefragment)의 `activityResultLaunchers` List에 존재할 경우 ActivityResultLauncher를 통해 Activity를 실행합니다.
>
> null일 경우 ActivityResultLauncher가 아닌 startActivity(intent: Intent)를 통해 Activity를 실행합니다.
>
> 기본값은 null입니다.

### Public / Protected Variables

`val bundle: EventDataBundle`

> 다음 Activity로 전달할 데이터를 담고 있는 [EventDataBundle](#eventdatabundle) 객체입니다.

`fun put(key: String, value: String)`
`fun put(key: String, value: Int)`
`fun put(key: String, value: Boolean)`
`fun put(key: String, value: Long)`
`fun put(key: String, value: Double)`
`fun put(key: String, value: Float)`
`fun put(key: String, value: Any)`

> 해당하는 타입의 데이터를 `bundle: EventDataBundle`에 저장합니다.
>
> 데이터는 Any 타입으로 변환하여 저장됩니다.

## ActivityResultEventModel

[BaseViewModel](#baseviewmodel)에서 `finishWithResult` Event로 [BaseActivity](#baseactivity)를 종료할 때, Result Callbak에 결과를 전달하기 위한 Model Class입니다.

### Public Constructors

```kotlin 
data class ActivityResultEventModel(val resultCode: Int, val data: Bundle? = null) 
``` 

`val resultCode: Int`

> Activity 실행 결과를 구분하기 위한 결과 코드입니다.
> 
> Activity Class에 정의된 `RESULT_OK`, `RESULT_CANCELED` 등 상수 사용을 권장합니다.

`val data: Bundle?`

> resultCode 외의 추가 데이터를 전달하기 위한 Bundle 객체입니다. [관련 문서 보기](https://developer.android.com/reference/android/os/Bundle)
>
> 기본값은 null입니다.

## BaseActivity

DataBinding 기반 MVVM 환경에서 Activity의 공통 사항을 정의하는 추상 클래스입니다.

이 클래스는 [BaseViewModel](#baseviewmodel)과의 사용을 전제로 구성되어 있습니다. 이 클래스는 AppCompatActivity를 완전히 대체합니다.

아래에 설명되지 않은 부분은 부모 Class인 AppCompatActivity의 구조를 공유합니다. [관련 문서 보기](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity)

### 적용 예시

```kotlin 
class ExampleActivity: BaseActivity<ActivityExampleBinding, ExampleViewModel> (R.layout.activity_example) { 
   override val activityViewModel: ExampleViewModel by viewModel()
   
   override fun onCreate(savedInstanceState: Bundle?) { 
      super.onCreate(savedInstanceState) 
      // Binding variable에 BaseActivity의 activityViewModel을 지정
      binding.viewModel = this.activityViewModel 
      
      // ... 
   } 
} 
``` 

### Public Constructors

```kotlin 
abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int) : AppCompatActivity(), RecyclerViewParentController 
``` 

`B : ViewDataBinding`

> 구현할 BaseActivity와 연결된 DataBinding Class의 Type을 전달합니다.
>
> 해당 Binding Class는 내부 binding 객체로 초기화되어 접근할 수 있습니다.

`VM : BaseViewModel`

> 구현할 BaseActivity에서 사용할 [BaseViewModel](#baseviewmodel) 구현체 Class Type을 전달합니다.
>
> 해당 ViewModel은 내부 activityViewModel 객체를 통해 접근할 수 있습니다.

`private val layoutResId: Int`

> XML Layout의 Resource ID를 전달합니다.
>
> 전달된 ID는 BaseActivity 내부에서 binding 객체를 초기화하는데 사용됩니다.

### Public / Protected Variables

`protected open val TAG: String`

> Class의 TAG 문자열을 정의합니다.
>
> 기본값은 `javaClass.simpleName`을 반환합니다.

`protected lateinit var binding: B`

> 생성자에서 ViewDataBinding 타입을 전달받아 초기화하는 DataBinding 객체입니다.
>
> 이 객체를 통해 XML Layout에 정의된 View로 접근할 수 있습니다.

`protected abstract val activityViewModel: VM`

> 생성자에서 전달 받은 타입의 [BaseViewModel](#baseviewmodel) 객체입니다.
>
> 추상화 객체이기 때문에 자식 Class에서 해당 ViewModel 객체를 구현해주어야 합니다.
>
> Koin 등 DI Library를 통한 의존성 주입을 추천합니다.

`val activityResultLaunchers: Map<String, ActivityResultLauncher<Intent>>`

> 다른 Activity를 실행하며 특정한 Callback을 실행하기 위한 ActivityResultLauncher로 구성된 Map 객체입니다.
>
> [BaseViewModel](#baseviewmodel)에서 Callback Function을 정의하고, Activity의 onCreate Lifecycle에서 해당 Callback을 실행할 ActivityResultLauncher를 초기화합니다.
>
> Launcher의 초기화 및 접근 과정은 다음과 같습니다.
>
> 1. 이 Class와 연결된 [BaseViewModel](#baseviewmodel) Class에서 `activityResultCallbacks`를 override합니다. `activityResultCallbacks`는 String Type Key와 (ActivityResult) → Unit Type의 Value로 구성됩니다. (`activityResultCallbacks`는 반드시 [BaseViewModel](#baseviewmodel)의 초기화 과정에서 정의되어야 합니다.)
>
> 2. BaseActivity가 onCreate 상태로 진입하였을 때, 연결된 [BaseViewModel](#baseviewmodel)의 `activityResultCallbacks`를 참조하여 ActivityResultLauncher를 초기화합니다. 선언된 ActivityResultLauncher는 Callback과 동일한 Key로 `activityResultLaunchers` Map에 저장됩니다.
>
> 3. 필요한 시점에 [BaseViewModel](#baseviewmodel) 또는 BaseActivity에서 `activityResultLaunchers`의 Key를 통해 생성된 ActivityResultLauncher로 접근할 수 있습니다.

```kotlin
// BaseViewModel에서 BaseActivity의 activityResultLaunchers를 통해 ActivityResultLauncher를 정의하는 과정 
class ExampleViewModel(dispatcher: CoroutineDispatcher? = null): BaseViewModel(dispatcher) { 
   
   companion object { 
      const val LAUNCHER_KEY_LOGIN = "LAUNCHER_KEY_LOGIN" 
   }
   
   //...

   // 초기화 단계에서 activityResultCallbacks를 override하여 Callback 정의 
   override val activityResultCallbacks: Map<String, (ActivityResult) -> Unit> = mapOf(
      LAUNCHER_KEY_LOGIN to { _ ->
      // Do Something...
   } 
   )
   
   fun startResultActivity() {
      // Activity 실행 이벤트를 호출하여 SecondActivity 실행 
      // SecondActivity가 종료되면 LAUNCHER_KEY_LOGIN으로 지정된 Result Callback이 호출됩니다. 
      handleStartActivityEvent(SecondActivity::class.java, LAUNCHER_KEY_LOGIN) 
   } 
} 
```
 
`protected val bundle: EventDataBundle?` 
 
> 이전 Activity에서 전달 받은 데이터를 담고 있는 [EventDataBundle](#eventdatabundle) 객체입니다. 
> 
> StartActivityWithBundle Event를 통해 Activity가 실행되었을 경우 전달 받은 [EventDataBundle](#eventdatabundle)에 접근할 수 있습니다. 

### Public / Protected Functions 
 
`override fun onCreate(savedInstanceState: Bundle?)` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - `binding: B` 객체의 초기화 (setContentView / lifecycleOwner 설정) 
> 
> - `observeEvent()` 를 호출하여 Event Observer 초기화
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `activityResultCallbacks`를 참조하여 `activityResultLaunchers`에 ActivityResultLauncher 초기화 
> 
> - `bundle: EventDataBundle?` 객체를 연결된 [BaseViewModel](#baseviewmodel)의 `bundle`객체로 전달 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onActivityCreate()` 호출 
 
`override fun onStart()` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewStart()` 호출 
 
`override fun onResume()` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewResume()` 호출 
 
`override fun onPause()` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewPause()` 호출 
 
`override fun onStop()` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewStop()` 호출 
 
`override fun onDestroy()` 
 
> Activity의 Lifecycle Function입니다. 
> 
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다. 
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewDestroy()` 호출 
 
`override fun onClickListItem(pos: Int, responseCode: Int)` 
 
> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다. 
> 
> 클릭한 Item의 position과 [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다. 
> 
> 한 BaseActivity에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다. 
 
`override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)` 
 
> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다. 
> 
> Item 전체가 아닌 내부 특정 View의 Click Event를 정의할 때 사용됩니다. 
> 
> 클릭한 Item의 position과 View의 id, [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다. 
> 
> 한 BaseActivity에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다. 
 
`open fun observeEvent()` 
 
> [BaseViewModel](#baseviewmodel)에서 EventWrapper LiveData를 통해 전달 받은 Event를 처리할 수 있도록 [EventObserver](#eventobserver)를 정의합니다. 
> 
> 기본적으로 [BaseViewModel](#baseviewmodel)에 정의된 Event에 대한 Callback을 정의하고 있습니다. 
> 
> [BaseViewModel](#baseviewmodel)의 구현 Class에서 추가 Event를 정의할 경우 override하여 [EventObserver](#eventobserver)를 구현해야 합니다. 
 
```kotlin
class ExampleViewModel(dispatcher: CoroutineDispatcher? = null): BaseViewModel(dispatcher) {
   // ViewModel에서 추가 Event를 정의
   private val _exampleEvent: MutableLiveEvent<String> = MutableLiveData() 
   val exampleEvent: LiveEvent<String> get() = _exampleEvent 
   
   // 정의해둔 exampleEvent를 호출 
   fun handleExampleEvent() { 
      _exampleEvent = Event("Hello") 
   } 
} 

class ExampleActivity: BaseActivity<ActivityExampleBinding, ExampleViewModel>(R.layout.activity_example) { 
   override fun observeEvent() { 
      super.observeEvent()
      // Event에 대한 Observer 정의
      activityViewModel.exampleEvent.observeEvent(this) { string ->
          // exampleEvent 호출 시 실행될 Callback
          Log.d(TAG, "Event -> ${string}")  // Hello
      }
   } 
}
``` 

## BaseActivityForViewBinding

ViewModel을 사용하지 않는 ViewBinding 기반의 Activity를 정의합니다.

주로 View 이외의 로직이 들어가지 않거나 간단한 로직만으로 이루어진 Activity에 사용합니다.

해당 클래스는 AppCompatActivity를 완전히 대체합니다.

AppCompatActivity를 상속하여 구현되어있기 때문에 아래에 기술되지 않은 부분은 부모 Class의 구조를 공유합니다. [관련 문서 보기](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity "https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity")

### **적용 예시**

```kotlin
class ExampleActivity: BaseActivityForViewBinding<ActivityExampleBinding>(ActivityExampleBinding::inflate) { 
   override fun onCreate(savedInstanceState: Bundle?) { 
      super.onCreate(savedInstanceState) 
      // Do Something... 
   }
}
```

### Public Constructors

```kotlin
abstract class BaseActivityForViewBinding<B : ViewBinding>(private val inflate: ActivityInflater<B>) : AppCompatActivity(), RecyclerViewParentController
```

`B : ViewBinding`

> 구현할 BaseActivityForViewBinding과 연결된 ViewBinding Class의 Type을 전달합니다.
>
> 전달받은 Binding Class는 내부 binding 객체로 초기화되어 접근할 수 있습니다.

`private val inflate: ActivityInflater<B>`

> binding 객체를 초기화하기 위해 필요한 LayoutInflater를 전달합니다.
> 
> ActivityInflater<B>는 (LayoutInflater) -> B 형태의 함수를 뜻합니다.
>
> 해당하는 ViewBinding Class의 Inflater를 `B::inflate` 형태로 전달합니다.

### Public / Protected Variables

`protected open val TAG: String`

> Class의 TAG 문자열을 정의합니다.
>
> 기본값은 `javaClass.simpleName`을 반환합니다.

`protected lateinit var binding: B`

> 생성자에서 전달 받은 타입으로 초기화 된 ViewBinding 객체입니다.
>
> 이 객체를 통해 XML Layout에 정의된 View로 접근할 수 있습니다.

### Public / Protected Functions

`override fun onCreate(savedInstanceState: Bundle?)`

> Activity의 Lifecycle Function입니다.
>
> AppCompatActivity의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - `binding: B` 객체의 초기화 (setContentView 설정)

`override fun onClickListItem(pos: Int, responseCode: Int)`

> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다.
>
> 클릭한 Item의 position과 [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다.
>
> 한 BaseActivity에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다.

`override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)`

> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다.
>
> Item 전체가 아닌 내부 특정 View의 Click Event를 정의할 때 사용됩니다.
>
> 클릭한 Item의 position과 View의 id, [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다.
>
> 한 BaseActivity에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다.

## BaseFragment

DataBinding 기반 MVVM 환경에서 Fragment의 공통 사항을 정의하는 추상 클래스입니다.

이 클래스는 [BaseViewModel](#baseviewmodel)과의 사용을 전제로 구성되어 있습니다. 이 클래스는 Fragment를 완전히 대체합니다.

아래에 설명되지 않은 부분은 부모 Class인 Fragment의 구조를 공유합니다. [관련 문서 보기](https://developer.android.com/reference/android/app/Fragment "https://developer.android.com/reference/android/app/Fragment")

### **적용 예시**

```kotlin
class ExampleFragment: BaseFragment<FragmentExampleBinding, ExampleViewModel>(R.layout.fragment_example) { 
   override lateinit var fragmentViewModel: ExampleViewModel
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { 
      super.onCreateView(inflater, container, savedInstanceState) 
      fragmentViewModel = getViewModel() // Koin Library를 통한 의존성 주입 
      binding.viewModel = this.fragmentViewModel // Binding 객체에 ViewModel 연결 
      return binding.root 
   } 
}
```

### Public Constructor

```kotlin
abstract class BaseFragment<B: ViewDataBinding, VM: BaseViewModel>(private val layoutResId: Int): Fragment(), RecyclerViewParentController
```

`B : ViewDataBinding`

> 구현할 BaseFragment와 연결된 DataBinding Class의 Type을 전달합니다.
>
> 전달받은 Binding Class는 내부 binding 객체로 초기화되어 접근할 수 있습니다.

`VM : BaseViewModel`

> 구현할 BaseFragment에서 사용할 [BaseViewModel](#baseviewmodel) 구현체 Class Type을 전달합니다.
>
> 전달받은 ViewModel은 내부 fragmentViewModel 객체를 통해 접근할 수 있습니다.

`private val layoutResId: Int`

> XML Layout의 Resource ID를 전달합니다.
>
> 전달받은 ID는 BaseFragment 내부에서 binding 객체를 초기화하는데 사용됩니다.

### Public / Protected Variables

`protected open val TAG: String`

> Class의 TAG 문자열을 정의합니다.
>
> 기본값은 Class의 이름으로 설정되어 있습니다.

`protected lateinit var binding: B`

> 생성자에서 전달 받은 타입으로 초기화 된 DataBinding 객체입니다.
>
> 해당 객체를 통해 XML Layout에 정의된 View로 접근할 수 있습니다.

`protected abstract val fragmentViewModel: VM`

> 생성자에서 전달 받은 타입의 [BaseViewModel](#baseviewmodel) 객체입니다.
>
> 추상화 객체이기 때문에 자식 Class에서 해당 ViewModel 객체를 구현해주어야 합니다.
>
> Koin 등 DI Library의 의존성 주입을 통해 사용하는 것을 추천합니다.
>
> 상위 Activity에서 Replace가 일어나는 Fragment의 경우 ViewModel의 Lifecycle을 신중히 고려해야 합니다. (Koin Library의 경우 by viewModel() 대신 getViewModel()을 통한 의존성 주입을 권장)

`val activityResultLaunchers: Map<String, ActivityResultLauncher<Intent>>`

> 다른 Activity를 실행하며 특정한 Callback을 실행하기 위한 ActivityResultLauncher로 구성된 Map 객체입니다.
>
> [BaseViewModel](#baseviewmodel)에서 Callback Function을 정의하고, Fragment의 onViewCreated Lifecycle에서 해당 Callback을 실행할 ActivityResultLauncher를 초기화합니다.
>
> Launcher의 초기화 및 접근 과정은 다음과 같습니다.
>
> 1. 이 Class와 연결된 [BaseViewModel](#baseviewmodel) Class에서 `activityResultCallbacks`를 override합니다. `activityResultCallbacks`는 String Type Key와 (ActivityResult) → Unit Type의 Value로 구성됩니다. (`activityResultCallbacks`는 반드시 [BaseViewModel](#baseviewmodel)의 초기화 과정에서 정의되어야 합니다.)
>
> 2. BaseFragment가 onViewCreated 상태로 진입하였을 때, 연결된 [BaseViewModel](#baseviewmodel)의 `activityResultCallbacks`를 참조하여 ActivityResultLauncher를 초기화합니다. 선언된 ActivityResultLauncher는 Callback과 동일한 Key로 `activityResultLaunchers` Map에 저장됩니다.
>
> 3. 필요한 시점에 [BaseViewModel](#baseviewmodel) 또는 BaseFragment에서 `activityResultLaunchers`의 Key를 통해 생성된 ActivityResultLauncher로 접근할 수 있습니다.

### Public / Protected Functions

`override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - `binding: B` 객체의 초기화 (inflate / lifecycleOwner 설정)

`override fun onViewCreated(view: View, savedInstanceState: Bundle?)`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onFragmentCreated()` 호출
> 
> - `observeEvent()` 를 호출하여 Event Observer 초기화
> 
> - 연결된 [BaseViewModel](#baseviewmodel)의 `activityResultCallbacks`를 참조하여 `activityResultLaunchers`에 ActivityResultLauncher 초기화

`override fun onStart()`

> Fragment의 Lifecycle Function입니다.
>   
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>  
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewStart()` 호출

`override fun onResume()`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewResume()` 호출

`override fun onPause()`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewPause()` 호출

`override fun onStop()`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewStop()` 호출

`override fun onDestroyView()`

> Fragment의 Lifecycle Function입니다.
>
> Fragment의 기본 동작에 추가로 다음과 같은 동작을 수행합니다.
>
> - 연결된 [BaseViewModel](#baseviewmodel)의 `onViewDestroy()` 호출
>
> - `binding: B` 객체의 리소스를 해제

`override fun onClickListItem(pos: Int, responseCode: Int)`

> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다.
>
> 클릭한 Item의 position과 [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다.
>
> 한 BaseFragment에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다.

`override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)`

> [BaseListAdapter](#baselistadapter)로 구현한 RecyclerView의 Item Click Event를 전달 받는 Callback입니다.
>
> Item 전체가 아닌 내부 특정 View의 Click Event를 정의할 때 사용됩니다.
>
> 클릭한 Item의 position과 View의 id, [BaseListAdapter](#baselistadapter)에 설정한 responseCode를 전달 받습니다.
>
> 한 BaseFragment에 여러 개의 RecyclerView가 존재할 경우, responseCode를 통해 특정 RecyclerView를 구분해야 합니다.

`open fun observeEvent()`

> [BaseViewModel](#baseviewmodel)에서 EventWrapper LiveData를 통해 전달 받은 Event를 처리할 수 있도록 [EventObserver](#eventobserver)를 정의합니다.
>
> 기본적으로 [BaseViewModel](#baseviewmodel)에 정의된 Event에 대한 Callback을 정의하고 있습니다.
>
> [BaseViewModel](#baseviewmodel)의 구현 Class에서 추가 Event를 정의할 경우 override하여 [EventObserver](#eventobserver)를 구현해야 합니다.

```kotlin
class ExampleViewModel(dispatcher: CoroutineDispatcher? = null): BaseViewModel(dispatcher) {
    // ViewModel에서 추가 Event를 정의 
    private val _exampleEvent: MutableLiveEvent<String> = MutableLiveData() 
    val exampleEvent: LiveEvent<String> get() = _exampleEvent 
    
    // 정의해둔 exampleEvent를 호출
    fun handleExampleEvent() { 
        _exampleEvent = Event("Hello") 
    } 
} 

class ExampleFragment: BaseFragment<FragmentExampleBinding, ExampleViewModel>(R.layout.fragment_example) {
    
    // ...
    
    override fun observeEvent() { 
        super.observeEvent() 
        // Event에 대한 Observer 정의 
        fragmentViewModel.exampleEvent.observeEvent(this) { string -> 
            // exampleEvent 호출 시 실행될 Callback 
            Log.d(TAG, "Event -> ${string}") // Hello 
        }
    }
}
```

<details> 
<summary><h3 id="base-fragment-for-view-binding">BaseFragmentForViewBinding</h3></summary> 

```kotlin 
 /** 
* @param B 해당 Fragment의 Viewbinding Class * @param inflate ViewBinding을 inflate하는 함수 (FragmentXXX::inflate를 넘겨주면 됨) 
 */public abstract class BaseFragmentForViewBinding<B : ViewBinding>(private val inflate: FragmentInflater<B>) : Fragment(), RecyclerViewParentController 
 
``` 
ViewBinding 환경에서 필요한 Fragment 기능을 정의합니다.</br> 
해당 클래스는 Fragment Class를 완전하게 대체합니다.</br> 
[RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.

#### 적용 예제

```kotlin class ExampleFragment: BaseFragmentForViewBinding<FragmentExampleBinding>(FragmentExampleBinding::inflate) { 
 
// binding 객체의 inflate 및 lifecycleOwner 설정을 입력할 필요가 없습니다. 
// binding 객체는 onDestroyView Lifecycle에서 null로 설정됩니다. 
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { super.onCreateView(inflater, container, savedInstanceState) return binding.root } } 
 
``` 
#### Values
> `protected open val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.
> - override를 통해 문자열을 재정의할 수 있습니다.

> `protected val binding: B`
> - 현재 클래스에 연결된 ViewBinding 객체를 반환합니다.

</details> 

<details> 
<summary><h3 id="base-view-model">BaseViewModel</h3></summary> 

```kotlin 
 public abstract class BaseViewModel: ViewModel() 
 
``` 
MVVM 개발 환경에서 필요한 ViewModel 기능을 정의합니다.</br> 
해당 클래스는 ViewModel Class를 대체합니다.</br> 
[ActivityNavigator](#activity-navigator) 객체를 포함하고 있어, 해당 interface를 상속한 객체를 받아 구현해야 합니다.

#### 적용 예제

```kotlin class ExampleViewModel(override val navigator: ActivityNavigator) : BaseViewModel() { 
 
fun doSomething() { //... } } 
 
``` 
#### Values
> `protected open val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.
> - override를 통해 문자열을 재정의할 수 있습니다.

> `protected abstract val navigator: ActivityNavigator`
> - Activity의 특정 기능에 접근하기 위한 interface입니다.
> - 일반적으로 Activity에서 해당 클래스를 상속해 구현한 뒤, BaseViewModel의 Parameter로 넘겨주어 사용합니다.
> - [BaseActivity](#base-activity)에는 기본적으로 해당 interface가 구현된 상태로, 본 클래스에서 BaseActivity 객체를 받아 초기화하여 사용합니다.

> `protected fun toast(res: Int)`</br> 
> `protected fun toast(string: String)`</br> 
> `protected fun startActivity(intent: Intent)`
> - ActivityNavigator의 특정 기능에 빠르게 접근하기 위한 shortcut function입니다.
> - ActivityNavigator에 구현된 동일명의 function을 실행합니다.

</details> 

<details> 
<summary><h3 id="preference-manager">PreferenceManager</h3></summary> 

```kotlin 
 /** 
* @param masterKey 암호화에 사용할 masterKey * @param fileName 디바이스에 저장될 SharedPreference 파일명. 입력하지 않을 시 packagename.preference로 저장된다. 
 */public class PreferenceManager(private val masterKey: String, private val fileName: String? = null) 
 
``` 
암호화된 SharedPreference 객체를 초기화하고 관리합니다.

#### Functions
> `public fun init(context: Context)`
> - SharedPreference 객체를 초기화합니다.
> - 객체 선언 후 가장 먼저 호출해야 합니다.

> `public fun put(key: String, value: String)`</br> 
> `public fun put(key: String, value: Int)`</br> 
> `public fun put(key: String, value: Boolean)`</br> 
> `public fun put(key: String, value: Long)`</br> 
> `public fun put(key: String, value: Float)`
> - 지정된 key값으로 데이터를 저장합니다.

> `public fun getString(key: String): String`
> - 특정 key값으로 저장된 String 값을 불러옵니다.
> - 값이 없을 경우 빈 문자열을 반환합니다.

> `public fun getInt(key: String): Int`
> - 특정 key값으로 저장된 Int 값을 불러옵니다.
> - 값이 없을 경우 -1을 반환합니다.

> `public fun getBoolean(key: String): Boolean`
> - 특정 key값으로 저장된 Boolean 값을 불러옵니다.
> - 값이 없을 경우 false를 반환합니다.

> `public fun getLong(key: String): Long`
> - 특정 key값으로 저장된 Long 값을 불러옵니다.
> - 값이 없을 경우 -1L을 반환합니다.

> `public fun getFloat(key: String): Float`
> - 특정 key값으로 저장된 Float 값을 불러옵니다.
> - 값이 없을 경우 -1f를 반환합니다.

> `public fun delete(key: String)`
> - 특정 key 값으로 저장된 데이터를 삭제합니다.

> `public fun clear()`
> - 저장되어있는 모든 값을 삭제합니다.

</details> 

<details> 
<summary><h3 id="recycler-view-base-adapter">RecyclerViewBaseAdapter</h3></summary> 

```kotlin /** 
 * @param MODEL: List Item의 데이터가 정의된 Model Class Type * @param VB: Item Layout의 ViewBinding Class Type * @param VH: RecyclerView.ViewHolder를 상속하여 구현한 ViewHolder Class type */public abstract class RecyclerViewBaseAdapter<MODEL, VB: ViewBinding, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() 
``` 
RecyclerView의 Adapter를 정의할 때 필수 사항 및 주로 사용되는 설정을 미리 정의하여 빠르게 구현할 수 있도록 도와줍니다.</br> 
Adapter를 사용하는 View Class에서 [RecyclerViewParentController](#recycler-view-parent-controller)를 상속해 구현해주어야 합니다.</br> 
</br> 
전체적인 구현 순서는 다음과 같습니다.

> 1. Adapter class에 RecyclerViewBaseAdapter를 RecyclerView.Adapter 대신 상속하여 필요한 부분을 구현
> 1. RecyclerView가 들어가는 Activity 또는 Fragment에 RecyclerViewParentController를 상속
> 1. 이 클래스 구현체의 생성자 또는 setController 함수를 통해 controller 변수에 RecyclerViewParentController의 구현체를 전달

#### 적용 예제

- ##### Adapter
``` kotlinclass CustomGalleryAdapter(): RecyclerViewBaseAdapter<CustomGalleryImageModel, ItemCustomGalleryBinding, CustomGalleryAdapter.ViewHolder>() { 
companion object { // Adapter 외부에서 특정 View를 구분하기 위해 설정 
 val VIEW_ID_IV_IMAGE = 0 } 
 private lateinit var context: Context 
 // responseCode를 지정하지 않는 경우 -1로 설정 
// 주로 하나의 Adapter만을 사용할 경우 해당 생성자로 초기화합니다. 
 constructor(controller: RecyclerViewParentController): this() { setController(controller, -1) } 
// 하나의 View Class에서 두 개 이상의 Adapter를 사용할 경우, responseCode를 지정하여 구분 
 constructor(controller: RecyclerViewParentController, responseCode: Int): this() { setController(controller, responseCode) } // ViewHolder를 내부 클래스로 구현 
 inner class ViewHolder(private val binding: ItemCustomGalleryBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) { fun bind(item: CustomGalleryImageModel, position: Int) { binding.apply { // List Item의 View를 설정 
 } } } override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { 
 context = parent.context val binding = ItemCustomGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false) val viewHolder = ViewHolder(binding, context) 
 // List Item을 클릭했을 때의 동작을 설정 
 binding.root.setOnClickListener { val pos = viewHolder.adapterPosition 
 if (pos != RecyclerView.NO_POSITION) { // controller의 onClickListItem callback을 실행하도록 구현합니다. 
controller.onClickListItem(pos, responseCode) } } // List Item 내부의 특정 View를 클릭했을 때 
 binding.ivImage.setOnClickListener { view -> val pos = viewHolder.adapterPosition 
if (pos != RecyclerView.NO_POSITION) { // 클릭한 View의 id를 onClickInnerItem Callback에 전달하여 어떤 view를 클릭했는지 구분합니다. 
 controller.onClickInnerItem(pos, VIEW_ID_IV_IMAGE, responseCode) } } 
 return viewHolder } override fun onBindViewHolder(holder: ViewHolder, position: Int) { 
val item = dataList[position] holder.bind(item, position) } } ``` 
- ##### Activity 
 
```kotlin 
 class ExampleActivity: BaseActivity<ActivityExampleBinding, ExampleViewModel>(R.layout.activity_example) { 
 
 companion object { private const val RECYCLER_VIEW_CUSTOM_GALLERY = 0 } 
 // ... 
 private val adapter = CustomGalleryAdapter(this, RECYCLER_VIEW_CUSTOM_GALLERY) // Adapter에서 List Item을 클릭할 경우 실행되는 Callback Function override fun onClickListItem(pos: Int, responseCode: Int) { super.onClickListItem(pos, responseCode) // responseCode로 특정 Adapter를 구분 
 when (responseCode) { RECYCLER_VIEW_CUSTOM_GALLERY -> { // CustomGalleryAdpater에서 Click Event가 발생했을 경우의 동작을 수행 
 } } } // Adapter에서 List Item 내부의 특정 View를 클릭할 경우 실행되는 Callback Function override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) { super.onClickInnerItem(pos, id, responseCode)when (responseCode) { MAX_SELECTED_COUNT -> { if (id == CustomGalleryAdapter.VIEW_ID_IV_IMAGE) { // List Item 내부의 특정 View가 클릭되었을 경우의 동작을 수행 
 } } } }} 
 
``` 
#### Values

> `public val dataList: List<MODEL>`
> - adapter에 설정된 item의 data list를 반환합니다.

> `public val responseCode: Int`
> - adapter에 설정된 responseCode를 반환합니다.

#### Functions

> `override fun getItemCount()`
> - adapter에 설정된 data list의 현재 size를 반환합니다.

> `public fun setController(controller: RecyclerViewParentController, responseCode: Int)`
> - controller 객체 및 responseCode를 받아 내부 변수에 할당합니다.
> - 생성자에서 호출하는 것을 권장합니다.

> `public open fun setDataList(list: ArrayList<MODEL>)`
> - 외부 Data Model의 ArrayList를 받아 dataList에 할당합니다.
> - 해당 function을 호출하지 않을 경우, 기본적으로 Adapter 내부의 List를 사용해 데이터를 관리합니다.

> `public open fun notifyAdapter()`
> - 기본적으로 notifyDataSetChanged()를 호출하도록 구현되어 있습니다.
> - notify와 함께 추가적인 동작이 필요할 경우 override하여 해당 로직을 구현할 수 있습니다.

> `public open fun addItems(list: List<MODEL>)`
> - list 내의 모든 데이터를 내부 dataList에 추가합니다.

> `public open fun addItem(item: MODEL)`
> - 데이터를 내부 dataList에 추가합니다.

> `public open fun removeItem(position: Int)`
> - 특정 position의 데이터를 제거합니다.

> `public open fun removeItem(item: MODEL)`
> - 특정 model과 일치하는 데이터를 제거합니다.

> `public open fun clearItem()`
> - dataList의 모든 데이터를 제거합니다.

> `public open fun getItem(position: Int) : MODEL`
> - dataList에서 특정 postion의 데이터를 반환합니다.

</details> 

<details> 
<summary><h3 id="recycler-view-parent-controller">RecyclerViewParentController</h3></summary> 

[RecyclerViewBaseAdapter](#recycler-view-base-adapter)의 Callback Function을 정의하고 관리하기 위한 interface입니다.</br> 
[BaseActivity](#base-activity), [BaseActivityForViewBinding](#base-activity-for-view-binding), [BaseFragment](#base-fragment), [BaseFragmentForViewBinding](#base-fragment-for-view-binding)에 기본적으로 상속되어 있으므로, 해당 Class를 사용하며 override를 통해 특정 동작을 구현할 것을 권장합니다.

#### Functions

> `public fun onClickListItem(pos: Int, responseCode: Int)` > RecyclerView에서 특정 Item을 클릭했을 때 event를 정의하기 위한 함수입니다. 
> RecyclerViewBaseAdapter에서 item click event 발생 시 호출하도록 설정하고, View Class에서 동작을 정의합니다. 
> 여러 개의 Adapter를 동시에 사용중일 경우, Adapter Class에 설정한 responseCode를 통해 Callback을 호출한 Adapter를 구분할 수 있습니다.

> `public fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)` > RecyclerView에서 Item 내부의 특정 View를 클릭했을 때 event를 정의하기 위한 함수입니다. 
> RecyclerViewBaseAdapter에서 특정 view 클릭 시 호출하도록 설정하고, View Class에서 동작을 정의합니다. 
> onClickListItem과 대체로 비슷한 동작을 수행하지만, 특정 id를 받아 클릭한 View를 구분합니다.

</details> 

<details> 
<summary><h3 id="simple-permission-checker">SimplePermissionChecker</h3></summary> 

```kotlin 
 /** 
 * @param parentActivity (필수) 권한 체크를 실행할 부모 activity * @param onAllPermissionsGranted (선택) 권한이 모두 승인되었을 경우 실행할 callback * @param onDenied (선택) 권한이 하나 이상 거부되었을 경우 실행할 callback / 기본적으로 showDialog()를 실행하도록 되어 있음 
 * @param showDialog (선택) 권한 거부 시 설정창으로 이동을 유도하는 dialog를 출력 / 기본값은 AlertDialog로 설정되어있지만, function을 통해 커스텀 가능 
 * @param onDialogPositive (선택) dialog에서 확인을 눌렀을 경우 실행할 callback / 기본적으로 설정 화면으로 이동하도록 되어 있음 
 * @param onDialogNegative (필수) 설정 이동 다이얼로그에서 취소를 눌렀을 경우 실행할 callback * @param permissions (필수) 권한 체크를 실행할 권한 목록 
 */public class SimplePermissionChecker( 
 private val parentActivity: AppCompatActivity, private var onAllPermissionsGranted: (() -> Unit)? = null, private var onDenied: (() -> Unit)? = null, private var showDialog: (() -> Unit)? = null, private var onDialogPositive: (() -> Unit)? = null, private var onDialogNegative: () -> Unit, private var permissions: Array<String>) 
 
``` 
일반적인 권한 체크 Flow를 자동화한 유틸리티 클래스입니다.</br> 
각 단계의 기본적인 동작이 사전에 정의되어있으며, param에 callback을 등록하여 커스텀이 가능합니다</br> 
해당 클래스는 내부적으로 registerForActivityResult를 사용하고 있으므로 반드시 Activity의 onCreate() lifecycle에 초기화되어야 합니다.</br> 
</br> 
커스텀하지 않은 기본적인 상태의 Flow는 다음과 같습니다.
> * 1. 권한 체크 (체크할 권한의 종류는 생성자에서 필수 항목으로 넘겨주어야 합니다.)
> * 2. 권한이 승인되었을 경우 onAllPermissionsGranted() callback 후 종료 / 권한 중 하나 이상이 거부되었을 경우 onDenied()를 통해 AlertDialog 출력
> * 3. 다이얼로그에서 확인을 누르면 설정 화면으로 이동, 설정 화면에서 돌아올 경우 1번으로 돌아가 체크 flow 실행
> * 4. 다이얼로그에서 취소를 눌렀을 경우 onDialogNegative() callback 후 종료

#### 적용 예제
```kotlin 
 override fun onCreate(savedInstanceState: Bundle?) { 
 super.onCreate(savedInstanceState) 
 permissionCheck()} 
 // 필수가 아닌 항목을 기본 Flow로 동작하기 위해서는 생성자에서 Param을 작성하지 않거나 null을 넘겨줍니다. private fun permissionCheck(onAllPermissionGranted: () -> Unit) { 
SimplePermissionChecker( parentActivity = this, onDialogNegative = { // 권한 거부 -> 설정 이동을 유도하는 AlertDialog에서 취소를 눌렀을 경우 
 toast("권한이 거부되어 기능을 이용할 수 없습니다. 화면을 종료합니다.") 
 finish() }, permissions = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA ) ).check()} 
 
``` 
#### Values

> `public val dialogTitle: String`
> - 설정화면 유도 AlertDialog에 출력될 Title String을 반환합니다.

> `public val dialogMessage: String`
> - 설정화면 유도 AlertDialog에 출력될 Message String을 반환합니다.

#### Functions

> `public fun check()`
> - 권한 체크를 실행합니다.
> - 체크할 권한 목록 및 callback 동작은 호출 시 설정된 값에 따릅니다.

> `public fun setDefaultDialogTitle(title: String)`
> - 설정화면 유도 AlertDialog에 출력될 Title String을 설정합니다.

> `public fun setDefaultDialogMessage(message: String)`
> - 설정화면 유도 AlertDialog에 출력될 Message String을 설정합니다.

> `public fun setOnAllPermissionsGrantedListener(listener: () -> Unit)`
> - 권한이 승인되었을 경우 실행될 `onAllPermissionsGranted()` callback function을 설정합니다.
> - 기본 상태에서는 아무 동작도 수행하지 않습니다.

> `public fun setOnDeniedListener(listener: () -> Unit)`
> - 권한이 하나 이상 거부되었을 경우 실행될 `onDenied()` callback function을 설정합니다.
> - 기본 상태에서는 `showDialog()`를 실행합니다

> `public fun setOnShowDialogListener(listener: () -> Unit)`
> - 설정화면으로 유도하는 AlertDialog를 출력하는 `showDialog()` callback function을 설정합니다.
> - `onDenied()`가 기본 상태일 경우 실행됩니다.

> `public fun setPermissions(permissions: Array<String>)`
> - 체크할 권한 목록을 설정합니다.

</details> 

<details> 
<summary><h3 id="extension-functions">Extension Functions</h3></summary> 

개발에 주로 사용되는 유틸성 확장 함수를 정의합니다.

#### Functions

> `public fun Context.toast(@StringRes res: Int)`
> - String Resource ID를 받아 toast를 출력합니다.
> - duration은 Toast.LENGTH_SHORT로 설정되어 있습니다.

> `public fun Context.toast(string: String)`
> - String 문자열을 받아 toast를 출력합니다.
> - duration은 Toast.LENGTH_SHORT로 설정되어 있습니다.

> `public fun Context.dpToPx(dp: Int): Int`
> - Dp를 Px 단위로 변경합니다.

> `public fun AppCompatActivity.setStatusBarColor(color: Int, lightMode: Boolean)`
> - 화면 상단 status bar의 색상 및 `controllerCompat.isAppearanceLightStatusBars`를 변경합니다.

> `public fun AppCompatActivity.getStatusBarHeight(): Int`
> - 화면 상단 status bar의 높이를 px size로 반환합니다.

> `public fun AppCompatActivity.getNavigationBarHeight(): Int`
> - 화면 하단 navigation bar의 높이를 px size로 반환합니다.

> `public fun AppCompatActivity.isSoftNavigationBar(): Boolean`
> - 현재 디바이스의 navigation bar가 소프트 키라면 true를, 물리 키라면 false를 반환합니다.

> `public fun Context.getScreenMaxWidth(): Int`
> - 디바이스 스크린의 최대 넓이를 반환합니다.

> `public fun AppCompatActivity.addTransparentStatusBarFlag()`
> - Activity의 window 객체에 status bar를 투명화하기 위한 Flag를 추가합니다.

> `public fun AppCompatActivity.removeStatusBar(rootLayout: ViewGroup)`
> - 상단 status bar 영역을 제거합니다.

> `public fun AppCompatActivity.setLayoutInDisplayCutout()`
> - Activity 레이아웃을 컷아웃 영역으로 배치합니다.

> `public fun AppCompatActivity.activityResultLauncher(onResultActivity: (ActivityResult) -> Unit): ActivityResultLauncher<Intent>`
> - `registerForActivityResult`를 간소화한 shortcut function입니다.
> - 특정 Activity를 실행시키고, 해당 Activity가 종료되었을 때 등록한 callback을 실행합니다.

> `public fun Uri.getRealPath(context: Context): String`
> - Uri에서 실제 파일 경로를 반환합니다.

> `public fun Long.toLocalDate(): LocalDate`
> - MilliSecond를 LocalDate로 변환합니다.

> `public fun Long.toLocalDateTime(): LocalDateTime`
> - MilliSecond를 LocalDateTime으로 변환합니다.

> `public fun LocalDate.toMilliSecond(): Long`
> - LocalDate 객체에 지정된 시간값을 MilliSecond로 변환합니다.

> `public fun LocalDateTime.toMilliSecond(): Long`
> - LocalDateTime 객체에 지정된 시간값을 MilliSecond로 변환합니다.

> `public fun String.emailValidation(): Boolean`
> - 문자열을 검사하여 email 양식일 경우 true를 반환합니다.

> `public fun String.toNoBreakString(): String`
> - String의 공백문자를 줄바꿈되지 않는 특수문자(\u00A0)로 변환하여 반환합니다.

> `public inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object : TypeToken<T>() {}.type)`
> - Gson() library 사용 시, TypeToken 객체를 통해 fromJson()을 호출합니다.
> - List 등 TypeToken을 정의하지 않으면 Exception이 발생하는 타입을 변환할 때 사용하면 편리합니다.

</details>