# Dependency Injection
**Dependency Injection, DI (의존성 주입)** 는 객체지향 프로그래밍에서 구성요소 (객체) 사이의 의존 관계가   
소스코드 상이 아닌 외부의 설정파일 (Bean Context..) 등을 통해 정의하고 참조시키는 디자인 패턴

--------------------------------------------------

## ButterKnife
**Field and Method binding for Android Views**

![](http://dl.dropbox.com/s/l4kgx6nos24n0zr/ButterKnife.png)  

## Gradle Download

```java
compile 'com.jakewharton:butterknife:8.5.1'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
```

--------------------------------------------------

## Annotations

#### View Binding
```@BindView``` 및 ButterKnife 의 View ID로 Field에 Annotation 을 달아   
Layout,xml 에서 해당 View 를 찾아 자동 캐스팅.

```java
class ExampleActivity extends Activity {
  @BindView(R.id.title) TextView title;
  @BindView(R.id.subtitle) TextView subtitle;
  @BindView(R.id.footer) TextView footer;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.simple_activity);
      ButterKnife.bind(this);
      // TODO Use fields...
  }
}
```

--------------------------------------------------

#### ```ButterKnife.bind(this);```
Reflection 대신 ButterKnife 는 코드를 생성하여 VIew 조회를 수행.
ButterKnife 는 다음과 같은 코드를 생성

```java
public void bind(ExampleActivity activity) {
  activity.subtitle = (android.widget.TextView) activity.findViewById(2130968578);
  activity.footer = (android.widget.TextView) activity.findViewById(2130968579);
  activity.title = (android.widget.TextView) activity.findViewById(2130968577);
}
```

--------------------------------------------------

#### Resource Binding
R.bool ID (또는 지정된 유형)의 사전 정의된 리소스를 해당 Field에 바인드   
```@BindBool, @BindColor, @BindDimen, @BindDrawable, @BindInt, @BindString```

```java
class ExampleActivity extends Activity {
  @BindString(R.string.title) String title;
  @BindDrawable(R.drawable.graphic) Drawable graphic;
  @BindColor(R.color.red) int red; // int or ColorStateList field
  @BindDimen(R.dimen.spacer) Float spacer; // int (for pixel size) or float (for exact value) field
  // ...
}
```

--------------------------------------------------

#### Non-Activity Binding
Activity에 세팅된 Layout 만이 아니라 View 루트를 제공하여
임의의 View 객체에 바인딩을 수행 할 수도 있다.

```java
public class FancyFragment extends Fragment {
  @BindView(R.id.button1) Button button1;
  @BindView(R.id.button2) Button button2;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fancy_fragment, container, false);

    // Inflation으로 메모리에 객체화 한 View를 ButterKnife에 Bind
    ButterKnife.bind(this, view);
    // TODO Use fields...
    return view;
  }
}
```

또 다른 용도는 ListAdapter 내부의 ViewHolder 패턴을 단순화.

```java
public class MyAdapter extends BaseAdapter {
  @Override public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = inflater.inflate(R.layout.whatever, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    holder.name.setText("John Doe");
    // etc...

    return view;
  }

  static class ViewHolder {
    @BindView(R.id.title) TextView name;
    @BindView(R.id.job_title) TextView jobTitle;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
```

--------------------------------------------------

#### 다른 바인딩 API
**```ButterKnife.bind()```를 호출하면 ```findViewById()``` 호출을 선언 할 수있는 곳이면 어디서든
호출 가능.**

* Activity를 View 루트로 사용하여 임의의 객체를 바인드.   
  MVC와 같은 패턴을 사용하면 ```ButterKnife.bind(this)```를 사용하여 컨트롤러를 바인딩 할 수 있다.  
* ```ButterKnife.bind(this)```를 사용하여 View의 자식을 Field에 바인딩.   
  Layout.xml에서 ```<merge>``` 태그를 사용하고 CustomView Constructor에서 inflate로 ​​즉시 호출 가능.   
  또는 XML에서 확장 된 CustomView Type은 ```onFinishInflate()``` Callback Method에서 사용할 수 있다.

--------------------------------------------------

#### View List
여러 View를 ```List<E>``` 또는 배열로 그룹화 할 수 있다.

```java
@BindViews({ R.id.first_name, R.id.middle_name, R.id.last_name })
List<EditText> nameViews;
```

```apply()``` Method를 사용하면 List의 모든 View를 한꺼번에 처리 할 수 ​​있다.

```java
ButterKnife.apply(nameViews, DISABLE);
ButterKnife.apply(nameViews, ENABLED, false);
```

Action 및 Setter 인터페이스를 사용하면 간단한 동작을 지정할 수 있다.

```java
static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
  @Override public void apply(View view, int index) {
    view.setEnabled(false);
  }
};
static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
  @Override public void set(View view, Boolean value, int index) {
    view.setEnabled(value);
  }
};
```

Android Property는 ```apply()``` method로 지정 가능

```java
ButterKnife.apply(nameViews, View.ALPHA, 0.0f);
```

--------------------------------------------------

#### Listener Binding
Listener는 Method에 자동으로 구성할 수 있다.

```java
@OnClick(R.id.submit)
public void submit(View view) {
  // TODO submit data to server...
}
```

Listener Method에 대한 모든 Parameter는 선택적이다.

```java
@OnClick(R.id.submit)
public void submit() {
  // TODO submit data to server...
}
```

Parameter의 Type을 정의하면 자동으로 형변환.

```java
@OnClick(R.id.submit)
public void sayHi(Button button) {
  button.setText("Hello!");
}
```

공통 Event 처리를 위해 단일 바인딩에서 여러 ID를 지정.

```java
@OnClick({ R.id.door1, R.id.door2, R.id.door3 })
public void pickDoor(DoorView door) {
  if (door.hasPrizeBehind()) {
    Toast.makeText(this, "You win!", LENGTH_SHORT).show();
  } else {
    Toast.makeText(this, "Try again", LENGTH_SHORT).show();
  }
}
```

CustomView는 ID를 지정하지 않고 자체 Method에 바인딩 할 수 있다.

```java
public class FancyButton extends Button {
  @OnClick
  public void onClick() {
    // TODO do something!
  }
}
```

--------------------------------------------------

#### Binding Reset
Fragment은 Activity와 수명주기가 다르다.   
**```onCreateView()```에서 Fraqgment을 바인딩 할 때 ```onDestroyView()```에서 View를 null로 설정.**   
**Butter Knife는 바인드를 호출 할 때 Unbinder 인스턴스를 반환한다.**   
해당 수명주기 콜백에서 ```unbind()``` Method를 호출해야 한다.

```java
public class FancyFragment extends Fragment {
  @BindView(R.id.button1) Button button1;
  @BindView(R.id.button2) Button button2;
  private Unbinder unbinder;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fancy_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    // TODO Use fields...
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    // Must Be Called!
    unbinder.unbind();
  }
}
```

--------------------------------------------------

#### Optional Bindings
기본적으로 ```@Bind``` 및 Listener 바인딩이 필요하고 Target View가 발견되지 않는 경우는
Exception이 던져진다.  
Exception을 차단하고 선택적 바인딩을 만들려면   
```@Nullable``` Annotation을 Field에 추가하거나 ```@Optional``` Annotation을 Method에 추가.  

참고 : ```@Nullable```이라는 Annotation은 Field에 사용할 수 있다.   
Android의 "support-annotations"라이브러리에 ```@Nullable``` Annotation을 사용하는 것이 좋다.

```java
@Nullable @BindView(R.id.might_not_be_there) TextView mightNotBeThere;

@Optional @OnClick(R.id.maybe_missing) void onMaybeMissingClicked() {
  // TODO ...
}
```

--------------------------------------------------

#### Multi-Method Listeners
Listener가 복수의 Callback을 가지는 Method Annotation으로, 어느 Method에도 바인드 할 수 있다.     
각 Annotation에는 바인딩 할 기본 Callback이 있다. Callback Parameter를 사용하여 대체를 지정.

```java
@OnItemSelected(R.id.list_view)
void onItemSelected(int position) {
  // TODO ...
}

@OnItemSelected(value = R.id.maybe_missing, callback = NOTHING_SELECTED)
void onNothingSelected() {
  // TODO ...
}
```

--------------------------------------------------

#### Bonus
View, Activity 또는 Dialog에서 View를 찾아야하는 코드를 단순화하는 findById() Method가 있다.     
제네릭을 사용하여 return Type을 추론하고 자동으로 캐스팅를 수행.

```java
View view = LayoutInflater.from(context).inflate(R.layout.thing, null);
TextView firstName = ButterKnife.findById(view, R.id.first_name);
TextView lastName = ButterKnife.findById(view, R.id.last_name);
ImageView photo = ButterKnife.findById(view, R.id.photo);

// Add a static import for ButterKnife.findById and enjoy even more fun.
```

--------------------------------------------------

## Lisense

Copyright 2013 Jake Wharton  

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  

```html
http://www.apache.org/licenses/LICENSE-2.0
```

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.
