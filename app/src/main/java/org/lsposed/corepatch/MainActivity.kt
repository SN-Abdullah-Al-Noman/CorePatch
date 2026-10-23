package org.lsposed.corepatch

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import org.lsposed.corepatch.App.Companion.mService
import org.lsposed.corepatch.App.Companion.reloadListener
import org.lsposed.corepatch.adapter.MultiTypeListAdapter
import org.lsposed.corepatch.data.SwitchData

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reloadListener = { runOnUiThread { showContent() } }
        showContent()
    }

    private fun showContent() {
        val service = mService
        if (service == null || "system" !in service.scope) {
            setContentView(LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                fitsSystemWindows = true
                addView(TextView(this@MainActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    setText(R.string.xposed_service_unavailable)
                    setTextAppearance(android.R.style.TextAppearance_Medium)
                    gravity = Gravity.CENTER
                })
                addView(TextView(this@MainActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    setText(R.string.xposed_service_unavailable_summary)
                    gravity = Gravity.CENTER
                })
            })
            return
        }
        val dataSet = arrayListOf(
            SwitchData(getString(R.string.bypass_verification), getString(R.string.bypass_verification_summary), Config.BYPASS_VERIFICATION)
        )
        val listView = ListView(this).apply {
            adapter = MultiTypeListAdapter(dataSet)
            fitsSystemWindows = true
        }
        setContentView(listView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.string.hide_launcher_icon, Menu.NONE, R.string.hide_launcher_icon).apply {
            isCheckable = true
            setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val component = ComponentName(this, "$packageName.LauncherAlias")
        menu.findItem(R.string.hide_launcher_icon).isChecked =
            packageManager.getComponentEnabledSetting(component) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.string.hide_launcher_icon) {
            val component = ComponentName(this, "$packageName.LauncherAlias")
            val hidden = packageManager.getComponentEnabledSetting(component) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            packageManager.setComponentEnabledSetting(
                component,
                if (hidden) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            item.isChecked = !hidden
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        reloadListener = {}
    }
}
