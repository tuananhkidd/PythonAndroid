from android.os import Bundle
from androidx.appcompat.app import AppCompatActivity
from com.beetech.python.ar import R
from java import jvoid, Override, static_proxy


class MainActivity(static_proxy(AppCompatActivity)):

    @Override(jvoid, [Bundle])
    def onCreate(self, state):
        AppCompatActivity.onCreate(self, state)
        self.setContentView(R.layout.activity_main)