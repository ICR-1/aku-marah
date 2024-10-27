package com.example.smartcontrol

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private val database = FirebaseDatabase.getInstance()
    private val devicesRef = database.getReference("devices")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Navigat    ion Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_about -> {
                    // Show About Dialog
                    showAboutDialog()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        // Setup Control Buttons
        findViewById<Button>(R.id.btnLightOn).setOnClickListener {
            updateDeviceState("lamp", true)
        }

        findViewById<Button>(R.id.btnLightOff).setOnClickListener {
            updateDeviceState("lamp", false)
        }

        findViewById<Button>(R.id.btnDoorOpen).setOnClickListener {
            updateDeviceState("door", true)
        }

        findViewById<Button>(R.id.btnDoorClose).setOnClickListener {
            updateDeviceState("door", false)
        }
    }

    private fun updateDeviceState(device: String, state: Boolean) {
        devicesRef.child(device).setValue(state)
            .addOnSuccessListener {
                Toast.makeText(this, "Command sent successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send command", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAboutDialog() {
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setTitle("About")
        dialogBuilder.setMessage("Smart Home Control App\nVersion 1.0\n\nDeveloped by: Your Name\n\nThis app controls smart home devices using Firebase Realtime Database.")
        dialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        dialogBuilder.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}