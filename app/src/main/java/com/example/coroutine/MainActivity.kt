package com.example.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "zzzzzz"
    private lateinit var btncallcorutinewithretutnval: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btncallcorutinewithretutnval = findViewById(R.id.btncallcorutinewithretutnval)
        btncallcorutinewithretutnval.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
               val returnValue =  callcoroutinewithvalue()
                Log.d(TAG, "GOT RETURN VALUE = "+ returnValue)
            }

        }
    }

    suspend fun callcoroutinewithvalue() : List<String>{
        for (i in 1..10) {
            Log.d(TAG, "callcoroutinewithvalue:  waiting for 10 iterations!!!!")

        }
        delay(1000)
        val mList = listOf("abc:","def")
        return mList

    }


    private suspend fun getDatafromApiOne(): String {
        Log.d(TAG, "getDatafromApiOne called: 1111111111111111111111111111 ")
        for (i in 1..1000) {
            Log.e(TAG, "getDatafromApiOne:  VALUE OF I $i")
        }
        delay(1000)
        Log.d(TAG, "getDatafromApiOne: dalay one complete")
        return "ApiData_One"
    }

    private suspend fun getDatafromApiTwo(): String {
        Log.d(TAG, "getDatafromApiTwo called: 2222222222222222222222222222222")
        for (i in 1..1000) {
            Log.d(TAG, "getDatafromApiTwo:  VALUE OF I $i")
        }
        delay(1000)
        Log.d(TAG, "getDatafromApiTwo: dalay Two complete")
        return "ApiData_Two"
    }

    fun callSimpleCorutineexample(view: View) {

        CoroutineScope(Dispatchers.IO).launch {
            val data = getDatafromApiOne()
            /*CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }*/
            lifecycleScope.launch {
                Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun callparalleeCorutineexample(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val valueOne = async { getDatafromApiOne() }
            val valueTwo = async { getDatafromApiTwo() }

            val fromApiOne = valueOne.await()
            val fromApiTwo = valueTwo.await()
            displayToUi(fromApiOne + fromApiTwo)
        }
    }

    private fun displayToUi(s: String) {
        lifecycleScope.launch {
            Toast.makeText(this@MainActivity, s, Toast.LENGTH_SHORT).show()
        }
    }

    fun callseriesCorutineexample(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val valueOneSeries = withContext(Dispatchers.IO) { getDatafromApiOne() }
            val valueTwoSeries = withContext(Dispatchers.IO) { getDatafromApiTwo() }
            val result = valueOneSeries + valueTwoSeries
            displayToUi(result)
        }

    }

    fun calljobjoin(view: View) {
        val job = GlobalScope.launch(Dispatchers.Default)
        {
            repeat(5) {
                Log.d(TAG, "Coroutine is still working: ")
                delay(100)
            }
        }
        runBlocking {

            job.join() //below executes only after completion of the job coroutine above.
            Log.d(TAG, "Main Thread is running: ")
        }

    }

    fun calljobcancel(view: View) {
        val job = GlobalScope.launch(Dispatchers.Default)
        {
            repeat(5)
            {
                Log.d(TAG, "calljobcancel:  still working")
                delay(1000);
            }
        }
        runBlocking {
            delay(2000)
            job.cancel();
            Log.d(TAG, "calljobcancel MAIN THREAD RUNNING!!!! ")
        }

    }

    fun calljobisactive(view: View) {
        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting the long calculation...")
            for (i in 30..40) {

                // if(isActive) // IF ENABLED THAN AFTER CANCEL VERY FEW LOGS(EXECUTION STOPS)
                Log.d(TAG, "Result for i =$i : ${fib(i)}")
            }
            Log.d(TAG, "Ending the long calculation...")
        }

        runBlocking {
            delay(2000)
            job.cancel()
            Log.d(TAG, "Main Thread is Running")
        }

    }

    fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib(n - 2)
    }

    fun calljobiwithtimeout(view: View) {

        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting the long calculation...")


            withTimeout(3000L) //COROUTINE WORKS ONLY FOR 3 SECS !!!!!!!
            {
                for (i in 30..40) {
                    if (isActive)
                        Log.d(TAG, "Result for i =$i : ${fib(i)}")
                }
            }
            Log.d(TAG, "Ending the long calculation...")
        }
    }
}