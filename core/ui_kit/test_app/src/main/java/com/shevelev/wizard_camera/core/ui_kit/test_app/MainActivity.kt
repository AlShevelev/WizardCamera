package com.shevelev.wizard_camera.core.ui_kit.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenu
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flowerMenu = findViewById<FlowerMenu>(R.id.flowerMenu)
        val items = listOf(
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 1"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "text 2"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 3"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 4"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 5"),
            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 6")
        )
        flowerMenu.init(items)

        findViewById<Button>(R.id.showButton).setOnClickListener {
            flowerMenu.show()
        }

        findViewById<Button>(R.id.hideButton).setOnClickListener {
            flowerMenu.hide()
        }

        flowerMenu.setOnItemClickListener { flowerMenu.hide() }
    }
}