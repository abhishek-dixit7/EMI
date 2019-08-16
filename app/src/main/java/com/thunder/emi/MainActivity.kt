package com.thunder.emi


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var date: String = LocalDateTime.now().toString()
            var today: String = date.substring(0..9)
            dod.setText(today)
        }
        else{
            var current = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            var date: String = formatter.format(current)
            var today: String = date.substring(0..9)
            dod.setText(today)
        }



        days.setText("30")



        calculate.setOnClickListener {


            var principal: String = amount.text.toString()
            var interest: String = rate.text.toString()
            var duration: String = tenure.text.toString()
            var day: String = days.text.toString()
            var expression: Double
            var additional: Int
            var final: String

            if (TextUtils.isEmpty(principal)) {
                amount.error = "Enter Loan Amount"
                amount.requestFocus()
                return@setOnClickListener
                //Toast.makeText( this,"Amount is Empty!!!", Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(interest)) {
                rate.error = "Enter Interest Rate"
                rate.requestFocus()
                return@setOnClickListener
                //Toast.makeText( this,"Rate is Empty!!!", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(duration)) {
                tenure.error = "Enter Tenure of Loan"
                tenure.requestFocus()
                return@setOnClickListener
                //Toast.makeText( this,"Tenure is Empty!!!", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(day)) {
                days.error = "Enter Days to 1st EMI"
                days.requestFocus()
                return@setOnClickListener
                //Toast.makeText( this,"Days is Empty!!!", Toast.LENGTH_LONG).show()
            } else {

                interest = (interest.toDouble() / (100 * 12)).toString()
                expression = ((principal.toDouble() * interest.toDouble() * Math.pow(
                    (1 + interest.toDouble()),
                    duration.toDouble()
                )) / (Math.pow((1 + interest.toDouble()), duration.toDouble()) - 1))



                additional = ((interest.toDouble() / (30.0)) * (day.toDouble() - 30.0) * expression).toInt()



                expression = expression + additional.toDouble()
                expression = round(expression)
                if(expression.toInt() == 0){
                    expression = 1.0
                }
                final = "EMI - " + expression.toString()

                emi_amount.text = final
                Log.i("MainActivity", additional.toString())
                Log.i("MainActivity", expression.toString())
            }

            //Toast.makeText( this,principal+"\n"+interest+"\n"+duration+"\n"+day, Toast.LENGTH_LONG).show()
        }

        set.setOnClickListener {


            var disbursment: String = dod.text.toString()
            val format = SimpleDateFormat("yyyy-MM-dd")
            format.isLenient = false

            if (TextUtils.isEmpty(disbursment)) {
                dod.error = "Enter Date of Disbursment"
                dod.requestFocus()
                return@setOnClickListener

            } else {
                try {
                    format.parse(disbursment)
                    val patternMonth = "\\d+\\-"
                    if (disbursment.substring(5..6) == patternMonth.toRegex().find(disbursment.substring(5..6))?.value) {
                        dod.error = "Enter month as mm i.e. 02"
                        dod.requestFocus()
                        return@setOnClickListener

                    } else if (disbursment.length < 10) {
                        dod.error = "Date isn't in right order i.e. 2000-02-02"
                        dod.requestFocus()
                        return@setOnClickListener

                    } else {
                        var d: String = disbursment.substring(8..9)
                        var m: String = disbursment.substring(5..6)
                        var y: String = disbursment.substring(0..3)
                        var newdate = "0000-00-00"

                        if (d.toInt() in 1..10 && (m != "12" && m != "09")) {
                            d = "05"
                            m = "0" + (m.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 1..10 && m == "09") {
                            d = "05"
                            m = (m.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 11..15 && (m != "12" && m != "09")) {
                            d = "10"
                            m = "0" + (m.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 11..15 && m != "12") {
                            d = "10"
                            m = "0" + (m.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 16..31 && (m == "03" || m == "05" || m == "10")) {
                            d = "05"
                            m = "0" + (m.toInt() + 2).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 16..31 && m == "08") {
                            d = "05"
                            m = (m.toInt() + 2).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        }
                        //for m=01 january
                        else if (d.toInt() in 16..31 && (m == "01")) {
                            if (y.toInt() % 4 == 0) {
                                //leap year feb = 29 days
                                d = "05"
                                m = "0" + (m.toInt() + 2).toString()
                                newdate = y + "-" + m + "-" + d
                            } else if (y.toInt() % 4 != 0) {
                                //not leap year feb =28 days
                                d = "05"
                                m = "0" + (m.toInt() + 2).toString()
                                newdate = y + "-" + m + "-" + "0" + (d.toInt() + 1).toString()
                            }
                        } else if (d.toInt() in 16..31 && (m == "02" || m == "06" || m == "11" || m == "07")) {
                            d = "05"
                            m = "0" + (m.toInt() + 2).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 2).toString()
                        } else if (d.toInt() in 16..31 && m == "09") {
                            d = "05"
                            m = (m.toInt() + 2).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 2).toString()
                        }
                        //for m=12 december
                        else if (d.toInt() in 16..31 && m == "12") {
                            d = "05"
                            m = "00"
                            m = "0" + (m.toInt() + 2).toString()
                            y = (y.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 2).toString()
                        } else if (d.toInt() in 1..10 && m == "12") {
                            d = "05"
                            m = "00"
                            m = "0" + (m.toInt() + 1).toString()
                            y = (y.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        } else if (d.toInt() in 11..15 && m == "12") {
                            d = "10"
                            m = "00"
                            m = "0" + (m.toInt() + 1).toString()
                            y = (y.toInt() + 1).toString()
                            newdate = y + "-" + m + "-" + "0" + (d.toInt() - 1).toString()
                        }
                        Log.i("MainActivity", newdate)


                        var d1: Int = disbursment.substring(8..9).toInt()
                        var m1: Int = disbursment.substring(5..6).toInt()
                        var y1: Int = disbursment.substring(0..3).toInt()
                        var d2: Int = newdate.substring(8..9).toInt()
                        var m2: Int = newdate.substring(5..6).toInt()
                        var y2: Int = newdate.substring(0..3).toInt()

                        var newFormat = SimpleDateFormat("yyyy-MM-dd")


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            var date1 = LocalDate.of(y1,m1,d1)
                            var date2 = LocalDate.of(y2,m2,d2)
                            var diff: String = ChronoUnit.DAYS.between(date1, date2).toString()
                            days.setText(diff)
                        } else {
                            var date1 = newFormat.parse("$y1-$m1-$d1")
                            var date2 = newFormat.parse("$y2-$m2-$d2")
                            var diff:String = ((date2.time - date1.time) / (24 * 60 * 60 * 1000)).toString()
                            days.setText(diff)
                        }


                        var finalDate: String
                        newdate = y + "-" + m + "-" + d
                        finalDate = "Date of 1st EMI - " + newdate
                        first_emi_date.text = finalDate


                    }
                } catch (e: Exception) {
                    when (e) {
                        is ParseException, is DateTimeException -> {
                            dod.error = "Date is not valid"
                            dod.requestFocus()
                            return@setOnClickListener

                        }
                        else -> throw e
                    }


                }
            }


            //Toast.makeText( this,diff, Toast.LENGTH_SHORT).show()


        }

    }
}
