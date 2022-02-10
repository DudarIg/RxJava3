package ru.dudar_ig.rxjava3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.dudar_ig.rxjava3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var disposable : Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.subscribeButton.setOnClickListener {
           disposable = observable
               .subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeBy (
                onNext = { updateResult(it) },
                onError = { onErrors(it)},
                onComplete = { onComplete()}

            )
        }
        binding.disposableButton.setOnClickListener {
            disposable?.dispose()

        }
    }
    private val observable = Observable.create<String> { emitter ->
        try {
            Thread.sleep(1_000)
            emitter.onNext("1 строка")
            Thread.sleep(1_000)
            emitter.onNext("2 строка")
            Thread.sleep(1_000)
            emitter.onNext("3 строка")
            Thread.sleep(1_000)
            emitter.onNext("4 строка")
            Thread.sleep(1_000)
            emitter.onComplete()
        }catch (ie: InterruptedException) {
            runOnUiThread {
                Toast.makeText(this, "Interrupted Exception", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun onErrors(it: Throwable) {
        binding.resultTextView.text = it.message
    }

    private fun onComplete() {
        binding.resultTextView.text = "COMPLETE"

    }
    private fun updateResult(text: String) {
        val oldText = binding.resultTextView.text.toString()
        binding.resultTextView.text = oldText + "\n" + text
    }


}