package com.zhuinden.simplestacktutorials.steps.step_9

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestacktutorials.R
import com.zhuinden.simplestacktutorials.steps.step_9.core.navigation.FragmentStateChanger
import com.zhuinden.simplestacktutorials.steps.step_9.features.login.LoginKey
import com.zhuinden.simplestacktutorials.steps.step_9.features.profile.ProfileKey
import kotlinx.android.synthetic.main.activity_step9.*

class Step9Activity : AppCompatActivity(), SimpleStateChanger.NavigationHandler {
    private lateinit var fragmentStateChanger: FragmentStateChanger
    private lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step9)

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.step9Root)
        appContext = applicationContext

        Navigator.configure()
            .setStateChanger(SimpleStateChanger(this))
            .setScopedServices(ServiceProvider())
            .setGlobalServices(
                GlobalServices.builder()
                    .addService("appContext", appContext)
                    .build()
            )
            .install(
                this, step9Root, History.of(
                    when {
                        AuthenticationManager.isAuthenticated(appContext) -> ProfileKey()
                        else -> LoginKey()
                    }
                )
            )
    }

    override fun onBackPressed() {
        if (!Navigator.onBackPressed(this)) {
            super.onBackPressed()
        }
    }

    override fun onNavigationEvent(stateChange: StateChange) {
        fragmentStateChanger.handleStateChange(stateChange)
    }

    override fun onDestroy() {
        if (isFinishing) {
            AuthenticationManager.clearRegistration(appContext) // just for sample repeat sake
        }

        super.onDestroy()
    }
}