# Previously on Building A Note Taking App

In the [**previous tutorial**](https://androidpirate.github.io/NoteAppCardView/ "**previous tutorial**"), we finished added a **CardView** for each list item in the list and implemented a **click listener** to display a **Toast message** whenever a list item is clicked.

Without further due…



## NoteApp Intents – Tutorial 3

Start by cloning/forking the app from the link below:

[**NoteApp Intents - Tutorial 3**](https://github.com/androidpirate/NoteAppIntents "**NoteApp Intents - Tutorial 3**")



### Goal of This Tutorial

The goal of this tutorial is to add a new **DetailActivity class** that will display the details of a **Note** whenever the user clicks on one of the notes in the list, use an **Intent** to navigate between activities and implement **Up Navigation**.

An **Intent** is a messaging object to request an action from another application component. They are separated into two types; **Explicit and Implicit Intents**. (Refer to the following document on
  [**Intents**](https://developer.android.com/guide/components/intents-filters.html "**Intents**")).


### What’s in Starter Module?

Starter module already has a fully functional **RecyclerView** which displays static notes provided by **FakeDataUtils class** as cards and responds to user clicks with a **Toast message**.

You can follow the steps below and give it a shot yourself, and if you stuck at some point, check out the **solution module** or the rest of the tutorial.



### Steps to Build

1. Add a new activity: **DetailActivity.java**
2. Design layout file: **activity_detail.xml**
3. Implement **onClick() method** in **MainActivity class** to start **DetailActivity**
4. Define **MainActivit**y as the parent activity for **DetailActivity** in **Android Manifest file**
5. Implement **Up button** to return back to **MainActivity** from **DetailActivity**



### Adding Another Activity

In order to display the details of the note on a dedicated screen, we are going to add a new activity. Add a **new blank activity** and name it as **DetailActivity** and also make sure to create a layout file for **DetailActivity**. Open up the layout file **activity_detail.xml** and add **TextView** elements to display title and description:


```xml
<?xml version="1.0" encoding="utf-8"?>

<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/tv_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@android:color/black"
            android:textSize="22sp"
            android:textStyle="bold"
            android:layout_marginLeft="16dp"
            android:layout_marginRight="16dp"
            android:layout_marginTop="4dp"
            android:layout_marginBottom="4dp"
            tools:text="Note Title"/>

        <TextView
            android:id="@+id/tv_description"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@android:color/black"
            android:textSize="18sp"
            android:layout_below="@id/tv_title"
            android:layout_marginLeft="16dp"
            android:layout_marginRight="16dp"
            android:layout_marginTop="4dp"
            android:layout_marginBottom="4dp"
            tools:text="@string/lore_ipsum_text"/>

    </RelativeLayout>

</ScrollView>
```


This time though, make sure to wrap **RelativeLayout** into a **ScrollView**. What it does is, making sure of the content on detail page is scrollable if its size exceeds the page limit.

Since we have the layout ready, we can go ahead and get the references for the **TextView** elements in **DetailActivity**:


```java
public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Get a reference for TextViews
        title = findViewById(R.id.tv_title);
        title.setText("Title");
        description = findViewById(R.id.tv_description);
        description.setText("Description");
    }
}
```


Now that we have the references we need some data. We know the user will select a note from the list to display its details, so the data related to it should be delivered to us. But how the data is delivered between activities? With **Intents!**



### What is an Intent?

As the documentation indicates, an **Intent** is an abstract description of an operation to be performed. (Check out [**this document**](https://developer.android.com/training/basics/firstapp/starting-activity.html "**this document**") on starting another activity.)

Do you remember the paper planes that we used to deliver notes with? **(Millenials, just Google it...)** That is what an **Intent** is... It is just more accurate.

We can definitely use an intent to start **DetailActivity** from **MainActivity** whenever the user clicks on a note. Switch to **MainActivity**' s
**onClick()** method and instead of displaying a **Toast message**, start **DetailActivity**:


```java
public class MainActivity extends AppCompatActivity
    implements NoteAdapter.NoteClickListener {
    // Fields and callbacks are excluded for simplicity

    @Override
    public void onClick(Note note) {
        // Create an intent to start DetailActivity
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
}
```

You already know that an **Explicit Intent** gets a **Context** and a target class name as parameters. We passed **MainActivity** as context and specified **DetailActivity** as the target above.

Run your app! When you click on a note, the **DetailActivity** will be started... But there is a problem... Your **DetailActivity** always showing the same details, no matter which note you click on the list!



### Intent Extras

What is an **Intent Extra**? An intent extra is a bundle that carries any additional information. The bundle itself is made out of key-value pairs. (If you are still confused about **Intents**, check out [**this tutorial**](https://guides.codepath.com/android/Using-Intents-to-Create-Flows "**this tutorial**"))

So we can add the note itself as an **Intent Extra**! or do we ???

In Android framework, we can not simply pass objects using **Intents**. Though we can use either one of **Serializable** or **Parcelable** interfaces to pass a POJO object, such as our **Note**, to do the job. (Awesome post on [**Serializable vs Parcelable**](https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable "**Serializable vs Parcelable**"))

Fortunately, our model class (**Note.java**), has been implemented using **Parcelable** interface. (**You are welcome!**). All we need to do is put the note as an **Intent extra**:


```java
public class MainActivity extends AppCompatActivity
    implements NoteAdapter.NoteClickListener {
    // Fields and callbacks are excluded for simplicity

    @Override
    public void onClick(Note note) {
        // Create an intent to start DetailActivity
        Intent intent = new Intent(this, DetailActivity.class);
        // Add note as an intent extra
        intent.putExtra(DetailActivity.EXTRA_NOTE, note);
        startActivity(intent);
    }
}
```


At this point, Android Studio might give you a warning about **DetailActivity.EXTRA_NOTE** key, don't worry, we will fix it next.



### Got Intent?

It is time to get note details in **DetailActivity** and display it. Since an **Intent Extra** is made out of key-value pairs, we need to define a key:

```java
public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra-note";
    // Fields and callbacks are excluded for simplicity
}
```

Rest is getting the extra using the key and use it to display note details:


```java
public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra-note";
    private Note note;
    // Fields are excluded for simplicity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Get the note data from intent extra
        if(getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
        }
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        if(note.getTitle() != null && note.getDescription() != null) {
            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }
    }
}
```

Run the app one more time to see how the details of each note are displayed!



### Getting Back to Where We Started

Good job on displaying the details of notes. But how about going back to our list of notes? The back button usually works with going back and forth between activities but there is a significant difference between using the **Back Navigation** and **Up Navigation**! ([**What's Up?**](https://developer.android.com/training/design-navigation/ancestral-temporal "**What's Up**"))


To enable up navigation, first we need to specify **MainActivity** as the parent activity of **DetailActivity**. Open **AndroidManifest.xml** file from project pane and specify a parent activity for **DetailActivity**:


```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.example.android.noteappintents">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name="com.example.android.noteappintents.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
        <activity android:name="com.example.android.noteappintents.DetailActivity"
            android:parentActivityName="com.example.android.noteappintents.MainActivity">
        </activity>
    </application>

</manifest>
```

And then enable up navigation within **Action Bar** in **DetailActivity**:


```java
public class DetailActivity extends AppCompatActivity {
    // Fields are excluded for simplicity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Enable up button in ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        .
        .
    }
}
```


Implement **Home Button** in **onOptionsItemSelected()** callback, to navigate back to **MainActivity**:


```java
public class DetailActivity extends AppCompatActivity {
    // Fields and callbacks are excluded for simplicity

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Returns back to MainActivity
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
```


That's it, now we can go back to **MainActivity** properly! Run the app and test it yourself.



### What's in Next Tutorial

In our [**next tutorial**](https://androidpirate.github.io/NoteAppSqliteQuery/ "**next tutorial**"), we are going to implement a **SQLite database** to store and manage our notes locally. Stay tuned!



### Resources
1. [Android Developer Guides](https://developer.android.com/guide/ "Android Developer Guides") by Google
2. [Android Cliffnotes](https://guides.codepath.com/android "Android Cliffnotes") by Codepath
