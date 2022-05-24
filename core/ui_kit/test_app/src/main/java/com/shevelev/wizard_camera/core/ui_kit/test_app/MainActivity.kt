package com.shevelev.wizard_camera.core.ui_kit.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenu
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flowerMenu = findViewById<FlowerMenu>(R.id.flowerMenu)
        val items = listOf(
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Some text")
        )
        flowerMenu.init(items)
    }
}