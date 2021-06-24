package com.ani.e_canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.ani.e_canteen.auth.LoginActivity

import com.ani.e_canteen.ui.cart.CartFragment
import com.ani.e_canteen.ui.dashboard.DashboardFragment
import com.ani.e_canteen.ui.rincian.RincianFragment
import com.ani.e_canteen.ui.setting.SettingFragment
import com.google.android.material.navigationrail.NavigationRailView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {
    companion object {
        lateinit var navigation: NavigationRailView
    }

    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sessionManager = SessionManager(this)
        navigation = find(R.id.navigation_rail)
        navigation_rail.setOnItemSelectedListener { menuitem ->
            when (menuitem.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        DashboardFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }

                R.id.cart -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        CartFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }

                R.id.chat -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        com.ani.e_canteen.ui.chat.ChatFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }

                R.id.setting -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        SettingFragment()
                    ).commit()
                    navigation_rail.visibility = View.GONE
                    return@setOnItemSelectedListener true
                }

                R.id.riwayat -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        RincianFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        DashboardFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }
            }
        }

        moveToFragment(DashboardFragment())

    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.frame, fragment)
        fragmentTrans.commit()
    }

}