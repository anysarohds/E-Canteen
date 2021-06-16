package com.ani.e_canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ani.e_canteen.ui.ChatFragment
import com.ani.e_canteen.ui.cart.CartFragment
import com.ani.e_canteen.ui.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

         navigation_rail.setOnItemSelectedListener {menuitem ->
            when(menuitem.itemId){
                R.id.home ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        DashboardFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }

                R.id.cart ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        CartFragment()
                    ).commit()
                    return@setOnItemSelectedListener true
                }
                else ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        ChatFragment()
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