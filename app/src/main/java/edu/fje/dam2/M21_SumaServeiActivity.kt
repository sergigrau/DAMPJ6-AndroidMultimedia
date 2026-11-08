package edu.fje.dam2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import edu.fje.dam2.databinding.M21ActivitySumaServeiBinding

/**
 * Activity que utilitza Jetpack WorkManager per a calcular la suma de dos nombres.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M21_SumaServeiActivity : AppCompatActivity() {

    private lateinit var binding: M21ActivitySumaServeiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = M21ActivitySumaServeiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.botoSuma.setOnClickListener {
            val num1 = binding.numero1.text.toString().toIntOrNull() ?: 0
            val num2 = binding.numero2.text.toString().toIntOrNull() ?: 0

            val workRequest = OneTimeWorkRequestBuilder<M21_SumaWorker>()
                .setInputData(workDataOf("n1" to num1, "n2" to num2))
                .build()

            val workManager = WorkManager.getInstance(this)
            workManager.enqueue(workRequest)

            workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val resultatSuma = workInfo.outputData.getInt("suma", 0)
                    binding.resultat.text = resultatSuma.toString()
                }
            }
        }
    }
}
