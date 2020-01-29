package dev.ugurcan.githubrepos

import android.app.Application
import com.ww.roxie.Roxie
import dev.ugurcan.githubrepos.di.reposModule
import dev.ugurcan.githubrepos.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class GitHubReposApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Roxie
        Roxie.enableLogging(object : Roxie.Logger {
            override fun log(msg: String) {
                Timber.tag("Roxie").d(msg)
            }
        })

        // Koin
        startKoin {
            androidLogger()
            // Android context
            androidContext(this@GitHubReposApp)
            // modules
            modules(listOf(vmModule, reposModule))
        }
    }
}