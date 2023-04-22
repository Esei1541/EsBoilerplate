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
    - [BaseFragment](#base-fragment)
    - [BaseFragmentForViewBinding](#base-fragment-for-view-binding)
    - [BaseViewModel](#base-view-model)
    - [PreferenceManager](#preference-manager)
    - [RecyclerViewBaseAdapter](#recycler-view-base-adapter)
    - [RecyclerViewParentController](#recycler-view-parent-controller)
    - [SimplePermissionChecker](#simple-permission-checker)
    - [ProgressDialog](#progress-dialog)
    - [Extension Functions](#extension-functions)
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

#### Values

>`val context: Context`
>- View 클래스의 context를 반환하도록 구현합니다.

>`val activity: AppCompatActivity`
>- Activity 객체를 반환하도록 구현합니다.

#### Functions

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


#### Values
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


#### Values

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

#### Values

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

<details>
<summary><h3 id="base-fragment">BaseFragment</h3></summary>

```kotlin

/**
 * @param B 해당 Fragment의 Databinding Class
 * @param layoutResId Layout xml의 resource ID
 */
public abstract class BaseFragment<B: ViewDataBinding>(private val layoutResId: Int): Fragment(), RecyclerViewParentController

```

DataBinding 환경에서 필요한 Fragment 기능을 정의합니다.</br>
해당 클래스는 Fragment Class를 완전하게 대체합니다.</br>
[RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.

#### 적용 예제

```kotlin
class ExampleFragment: BaseFragment<ActivityExampleBinding>(R.layout.fragment_example) {

    // binding 객체의 inflate 및 lifecycleOwner 설정을 입력할 필요가 없습니다.
    // binding 객체는 onDestroyView Lifecycle에서 null로 설정됩니다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onClickListItem(pos: Int, responseCode: Int) {
        // RecyclerView을 구현한 상황에서 ListItem을 클릭했을 때의 반응을 구현
        // 해당 Fragment에 RecyclerView가 없다면 빈 function으로 남겨두면 됩니다.
    }

    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {
        // RecyclerView을 구현한 상황에서 ListItem 내부의 특정 View 클릭했을 때의 반응을 구현
        // 해당 Fragment에 RecyclerView가 없다면 빈 function으로 남겨두면 됩니다.
    }

}

```

#### Values
> `protected open val TAG: String`
> - 클래스의 이름으로 문자열을 반환합니다.
> - override를 통해 문자열을 재정의할 수 있습니다.

> `protected val binding: B`
> - 현재 클래스에 연결된 DataBinding 객체를 반환합니다.

</details>

<details>
<summary><h3 id="base-fragment-for-view-binding">BaseFragmentForViewBinding</h3></summary>

```kotlin

/**
 * @param B 해당 Fragment의 Viewbinding Class
 * @param inflate ViewBinding을 inflate하는 함수 (FragmentXXX::inflate를 넘겨주면 됨)
 */
public abstract class BaseFragmentForViewBinding<B : ViewBinding>(private val inflate: FragmentInflater<B>) : Fragment(), RecyclerViewParentController

```

ViewBinding 환경에서 필요한 Fragment 기능을 정의합니다.</br>
해당 클래스는 Fragment Class를 완전하게 대체합니다.</br>
[RecyclerViewParentController](#recycler-view-parent-controller) Interface를 기본적으로 상속하고 있습니다.

#### 적용 예제

```kotlin
class ExampleFragment: BaseFragmentForViewBinding<FragmentExampleBinding>(FragmentExampleBinding::inflate) {

    // binding 객체의 inflate 및 lifecycleOwner 설정을 입력할 필요가 없습니다.
    // binding 객체는 onDestroyView Lifecycle에서 null로 설정됩니다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

}

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

```kotlin
class ExampleViewModel(override val navigator: ActivityNavigator) : BaseViewModel() {

    fun doSomething() {
        //...
    }

}

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
 * @param masterKey 암호화에 사용할 masterKey
 * @param fileName 디바이스에 저장될 SharedPreference 파일명. 입력하지 않을 시 packagename.preference로 저장된다.
 */
public class PreferenceManager(private val masterKey: String, private val fileName: String? = null)

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

```kotlin
/**
 * @param MODEL: List Item의 데이터가 정의된 Model Class Type
 * @param VB: Item Layout의 ViewBinding Class Type
 * @param VH: RecyclerView.ViewHolder를 상속하여 구현한 ViewHolder Class type
 */
public abstract class RecyclerViewBaseAdapter<MODEL, VB: ViewBinding, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>()
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
``` kotlin
class CustomGalleryAdapter(): RecyclerViewBaseAdapter<CustomGalleryImageModel, ItemCustomGalleryBinding, CustomGalleryAdapter.ViewHolder>() {

    companion object {
        // Adapter 외부에서 특정 View를 구분하기 위해 설정
        val VIEW_ID_IV_IMAGE = 0
    }

    private lateinit var context: Context

    // responseCode를 지정하지 않는 경우 -1로 설정
    // 주로 하나의 Adapter만을 사용할 경우 해당 생성자로 초기화합니다.
    constructor(controller: RecyclerViewParentController): this() {
        setController(controller, -1)
    }

    // 하나의 View Class에서 두 개 이상의 Adapter를 사용할 경우, responseCode를 지정하여 구분
    constructor(controller: RecyclerViewParentController, responseCode: Int): this() {
        setController(controller, responseCode)
    }
    
    // ViewHolder를 내부 클래스로 구현
    inner class ViewHolder(private val binding: ItemCustomGalleryBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CustomGalleryImageModel, position: Int) {
            binding.apply {
                // List Item의 View를 설정
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemCustomGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding, context)

        // List Item을 클릭했을 때의 동작을 설정
        binding.root.setOnClickListener {
            val pos = viewHolder.adapterPosition

            if (pos != RecyclerView.NO_POSITION) {
                // controller의 onClickListItem callback을 실행하도록 구현합니다.
                controller.onClickListItem(pos, responseCode)
            }
        }
        
        // List Item 내부의 특정 View를 클릭했을 때
        binding.ivImage.setOnClickListener { view ->
            val pos = viewHolder.adapterPosition

            if (pos != RecyclerView.NO_POSITION) {
                // 클릭한 View의 id를 onClickInnerItem Callback에 전달하여 어떤 view를 클릭했는지 구분합니다.
                controller.onClickInnerItem(pos, VIEW_ID_IV_IMAGE, responseCode)
            }
        }

        return viewHolder
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, position)
    }
    
}
```

- ##### Activity

```kotlin

class ExampleActivity: BaseActivity<ActivityExampleBinding, ExampleViewModel>(R.layout.activity_example) {

    companion object {
        private const val RECYCLER_VIEW_CUSTOM_GALLERY = 0
    }

    // ...

    private val adapter = CustomGalleryAdapter(this, RECYCLER_VIEW_CUSTOM_GALLERY)
    
    // Adapter에서 List Item을 클릭할 경우 실행되는 Callback Function
    override fun onClickListItem(pos: Int, responseCode: Int) {
        super.onClickListItem(pos, responseCode)

        // responseCode로 특정 Adapter를 구분
        when (responseCode) {
            RECYCLER_VIEW_CUSTOM_GALLERY -> {
                // CustomGalleryAdpater에서 Click Event가 발생했을 경우의 동작을 수행
            }
        }
    }
    
    // Adapter에서 List Item 내부의 특정 View를 클릭할 경우 실행되는 Callback Function
    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {
        super.onClickInnerItem(pos, id, responseCode)

        when (responseCode) {
            MAX_SELECTED_COUNT -> {
                if (id == CustomGalleryAdapter.VIEW_ID_IV_IMAGE) {
                    // List Item 내부의 특정 View가 클릭되었을 경우의 동작을 수행
                }
            }
        }
    }
}

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

> `public fun onClickListItem(pos: Int, responseCode: Int)`
> RecyclerView에서 특정 Item을 클릭했을 때 event를 정의하기 위한 함수입니다.
> RecyclerViewBaseAdapter에서 item click event 발생 시 호출하도록 설정하고, View Class에서 동작을 정의합니다.
> 여러 개의 Adapter를 동시에 사용중일 경우, Adapter Class에 설정한 responseCode를 통해 Callback을 호출한 Adapter를 구분할 수 있습니다.

> `public fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)`
> RecyclerView에서 Item 내부의 특정 View를 클릭했을 때 event를 정의하기 위한 함수입니다.
> RecyclerViewBaseAdapter에서 특정 view 클릭 시 호출하도록 설정하고, View Class에서 동작을 정의합니다.
> onClickListItem과 대체로 비슷한 동작을 수행하지만, 특정 id를 받아 클릭한 View를 구분합니다.

</details>

<details>
<summary><h3 id="simple-permission-checker">SimplePermissionChecker</h3></summary>

</details>

<details>
<summary><h3 id="progress-dialog">PrograssDialog</h3></summary>

</details>


<details>
<summary><h3 id="extension-functions">Extension Functions</h3></summary>

</details>
