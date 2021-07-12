package com.applocum.connecttomyhealth.ui.booksession.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.applocum.connecttomyhealth.*
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.adapters.AvailableTimeClickAdapter
import com.applocum.connecttomyhealth.ui.booksession.adapters.CustomDateAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.booksession.models.DateModel
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.bottomnavigationview.activities.BottomNavigationViewActivity
import com.applocum.connecttomyhealth.ui.confirmbooking.activities.ConfirmBookingActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.activity_session_book.*
import kotlinx.android.synthetic.main.activity_session_book.tvCancel
import kotlinx.android.synthetic.main.custom_date_dialog.view.*
import kotlinx.android.synthetic.main.custom_small_progress.*
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SessionBookActivity : BaseActivity(), BookSessionPresenter.View,
    OnDateSelectedListener {
    private var sType = ""
    private var sSlot = ""
    private var seleteddate = ""
    private var sTime = ""
    private var specialistId = 0
    private lateinit var commonData: Common
    private var mListDate:ArrayList<DateModel> = ArrayList()
    private var mListDay:ArrayList<DateModel> = ArrayList()
    private lateinit var dateModel:DateModel
    private lateinit var customDateAdapter:CustomDateAdapter
    private lateinit var customDateAdapterDay:CustomDateAdapter
    private var multiDate:String=""
    private var multiDateWeek:String=""
    private var confirmDate=""
    private var weekDay=""
    private var weekDate=""

    val date = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                                     "21","22","23","24","25","26","27","28","29","30","31")

    private var isRecurringOrNot = false
    private var recurringType=""

    @Inject
    lateinit var presenter: BookSessionPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_session_book

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult", "SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        dateModel = DateModel()

         setDate()
         setDay()

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvCancel).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val intent = (Intent(this, BottomNavigationViewActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(0, 0)
            }

        specialistId = intent.getIntExtra("specialistId", 0)

        RxView.clicks(btnPhoneCall).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "phone_call_appointment"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btnPhoneCall.setBackgroundResource(R.drawable.custom_btn)
                btnVideoCall.setBackgroundResource(R.drawable.default_button)
                btnFaceToFace.setBackgroundResource(R.drawable.default_button)
                btnPhoneCall.setTextColor(Color.WHITE)
                btnVideoCall.setTextColor(Color.parseColor("#008976"))
                btnFaceToFace.setTextColor(Color.parseColor("#008976"))

                sTime = ""
            }

        RxView.clicks(btnVideoCall).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "online_appointment"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btnPhoneCall.setBackgroundResource(R.drawable.default_button)
                btnVideoCall.setBackgroundResource(R.drawable.custom_btn)
                btnFaceToFace.setBackgroundResource(R.drawable.default_button)
                btnPhoneCall.setTextColor(Color.parseColor("#008976"))
                btnVideoCall.setTextColor(Color.WHITE)
                btnFaceToFace.setTextColor(Color.parseColor("#008976"))

                sTime = ""
            }

        RxView.clicks(btnFaceToFace).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "offline_appointment"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btnPhoneCall.setBackgroundResource(R.drawable.default_button)
                btnVideoCall.setBackgroundResource(R.drawable.default_button)
                btnFaceToFace.setBackgroundResource(R.drawable.custom_btn)
                btnPhoneCall.setTextColor(Color.parseColor("#008976"))
                btnVideoCall.setTextColor(Color.parseColor("#008976"))
                btnFaceToFace.setTextColor(Color.WHITE)

                sTime = ""
            }

        RxView.clicks(btn10Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "10"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btn10Mins.setBackgroundResource(R.drawable.custom_btn)
                btn20Mins.setBackgroundResource(R.drawable.default_button)
                btn30Mins.setBackgroundResource(R.drawable.default_button)
                btn10Mins.setTextColor(Color.WHITE)
                btn20Mins.setTextColor(Color.parseColor("#008976"))
                btn30Mins.setTextColor(Color.parseColor("#008976"))

                sTime = ""
            }

        RxView.clicks(btn20Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "20"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btn10Mins.setBackgroundResource(R.drawable.default_button)
                btn20Mins.setBackgroundResource(R.drawable.custom_btn)
                btn30Mins.setBackgroundResource(R.drawable.default_button)
                btn10Mins.setTextColor(Color.parseColor("#008976"))
                btn20Mins.setTextColor(Color.WHITE)
                btn30Mins.setTextColor(Color.parseColor("#008976"))

                sTime = ""
            }

        RxView.clicks(btn30Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "30"
                presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)
                btn10Mins.setBackgroundResource(R.drawable.default_button)
                btn20Mins.setBackgroundResource(R.drawable.default_button)
                btn30Mins.setBackgroundResource(R.drawable.custom_btn)
                btn10Mins.setTextColor(Color.parseColor("#008976"))
                btn20Mins.setTextColor(Color.parseColor("#008976"))
                btn30Mins.setTextColor(Color.WHITE)

                sTime = ""
            }


        //ivMinus.setOnClickListener { decreaseInteger() }
        //ivPlus.setOnClickListener { increaseInteger() }

        cbDaily.setOnCheckedChangeListener { _, b ->
            if (b) {
                recurringType="daily"
                confirmDate="Daily from"
                cbWeekly.isChecked = false
                cbMonthly.isChecked = false
                cbCustom.isChecked = false
                tvDates.visibility=View.GONE
                tvDates.text=""
            }
        }

        cbWeekly.setOnCheckedChangeListener { _, b ->
            if (b) {
                recurringType="weekly"
                confirmDate= cbWeekly.text.toString()
                cbDaily.isChecked = false
                cbMonthly.isChecked = false
                cbCustom.isChecked = false
                tvDates.visibility=View.GONE
                tvDates.text=""
            }
        }

        cbMonthly.setOnCheckedChangeListener { _, b ->
            if (b) {
                recurringType="monthly"
                confirmDate=cbMonthly.text.toString()
                cbDaily.isChecked = false
                cbWeekly.isChecked = false
                cbCustom.isChecked = false
                tvDates.visibility=View.GONE
                tvDates.text=""
            }
        }

        cbCustom.setOnCheckedChangeListener { _, b ->
            if (b) {
                val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_date_dialog, null, false)
                val dialog = AlertDialog.Builder(this).create()
                dialog.setView(showDialogView)

                showDialogView.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }

                showDialogView.rbDateOfMonth.isChecked = true
                recurringType="monthly"
                confirmDate=tvDates.text.toString()
                showDialogView.rvDates.layoutManager = GridLayoutManager(this, 7)
                showDialogView.rvDates.adapter = customDateAdapter

                showDialogView.rbDateOfMonth.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                    {
                        recurringType="monthly"
                        confirmDate=tvDates.text.toString()
                        showDialogView.rvDates.layoutManager = GridLayoutManager(this, 7)
                        showDialogView.rvDates.adapter = customDateAdapter
                    }
                }

                showDialogView.rbDayOfWeek.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                    {
                        recurringType="weekly"
                        confirmDate=tvDates.text.toString()
                        showDialogView.rvDates.layoutManager = GridLayoutManager(this, 7)
                        showDialogView.rvDates.adapter = customDateAdapterDay
                    }
                }

                showDialogView.btnDone.setOnClickListener {

                    if (showDialogView.rbDateOfMonth.isChecked)
                    {
                    val selectedItems:ArrayList<String> = ArrayList()
                        for (i in customDateAdapter.selectedItem().indices) {
                            selectedItems.add(customDateAdapter.selectedItem()[i].date)
                        }

                        if (selectedItems.joinToString().isEmpty())
                        {
                            Toast.makeText(this,"Please select at least one option",Toast.LENGTH_SHORT).show()
                        }else
                        {
                            multiDate = selectedItems.joinToString()
                            dialog.dismiss()
                            tvDates.visibility=View.VISIBLE
                        }

                        tvDates.text = "Monthly on $multiDate"
                        confirmDate=tvDates.text.toString()

                    }

                    if (showDialogView.rbDayOfWeek.isChecked)
                    {
                        val selectedItems:ArrayList<String> = ArrayList()
                        for (i in customDateAdapterDay.selectedItem().indices) {
                            selectedItems.add(convertWeekDay(customDateAdapterDay.selectedItem()[i].date))
                        }

                        if (selectedItems.joinToString().isEmpty())
                        {
                            Toast.makeText(this,"Please select at least one option",Toast.LENGTH_SHORT).show()
                        }else
                        {
                            multiDateWeek = selectedItems.joinToString()
                            dialog.dismiss()
                            tvDates.visibility=View.VISIBLE
                        }
                        tvDates.text = "Weekly on $multiDateWeek"
                        confirmDate=tvDates.text.toString()
                    }

                }

                dialog.show()

                cbDaily.isChecked = false
                cbWeekly.isChecked = false
                cbMonthly.isChecked = false
            }
        }


        switchMultiSessions.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                llMultiSessions.visibility = View.VISIBLE
                isRecurringOrNot = true
            }else
            {
                llMultiSessions.visibility = View.GONE
                etSessions.setText("")
                recurringType=""
                confirmDate=""
                isRecurringOrNot = false
            }
        }

        RxView.clicks(btnContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {

                if (validateBookSession(seleteddate, sType, sSlot, sTime,isRecurringOrNot,etSessions.text.toString(),recurringType)) {
                    val intent = Intent(this,ConfirmBookingActivity::class.java)
                    val appointment = userHolder.getBookAppointmentData()
                    appointment.appointmentDateTime = dateTimeUTCFormat(sTime)
                    appointment.appointmentTime = sTime
                    appointment.appointmentType = sType
                    appointment.appointmentSlot = sSlot
                    appointment.confirmDate = confirmDate
                    appointment.appointmentDate = seleteddate
                    appointment.therapistId = specialistId
                    appointment.isRecurring = isRecurringOrNot
                    appointment.recurringSessionCount=etSessions.text.toString()
                    appointment.recurringType=recurringType

                    if(cbCustom.isChecked)
                    {
                        appointment.recurringWeekDays = multiDateWeek.toLowerCase(Locale.ROOT)
                        appointment.recurringMonthDate = multiDate
                    }else {
                        appointment.recurringWeekDays = weekDay.toLowerCase(Locale.ROOT)
                        appointment.recurringMonthDate = weekDate
                    }
                    userHolder.saveBookAppointmentData(appointment)
                    intent.putExtra("commonData", commonData)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }

        calendarView.setOnDateChangedListener(this)
        calendarView.addDecorator(PrimeDayDisableDecorator())
        calendarView.selectedDate = CalendarDay.today()


        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        val date = day.toString() + "/" + (month + 1) + "/" + year

        val wDay=c.get(Calendar.DAY_OF_WEEK)
        weekDay = DateFormatSymbols().weekdays[wDay]

        weekDate= convertWeekDate(date)
        seleteddate = convertCurrentDate(date)
        btn10Mins.performClick()

        cbMonthly.text ="Monthly on"+" "+weekDate+"th"
        cbWeekly.text = "Weekly on $weekDay"

    }

    override fun getTimeSlot(list: ArrayList<Time>) {
        if (list.isEmpty()) {
            rvAvailableTime.visibility = View.GONE
            NotAvailabeTime.visibility = View.VISIBLE
        } else {
            NotAvailabeTime.visibility = View.GONE
            rvAvailableTime.visibility = View.VISIBLE
            rvAvailableTime.layoutManager = GridLayoutManager(this, 4)
            val availableTimeClickAdapter = AvailableTimeClickAdapter(this, list,
                object : AvailableTimeClickAdapter.ItemClickListner {
                    override fun onItemClick(time: Time, position: Int) {
                        sTime = time.start_time
                    }
                })
            rvAvailableTime.adapter = availableTimeClickAdapter
            availableTimeClickAdapter.notifyDataSetChanged()
        }
    }

    override fun displaymessage(message: String) {
        val snackbar = Snackbar.make(llSessionbook, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        progressSmall.visibility = if (isShow) View.VISIBLE else View.GONE
        rvAvailableTime.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun getPrice(common: Common) {
        commonData = common
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            val snackbar = Snackbar.make(llSessionbook, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date1: CalendarDay,
        selected: Boolean
    ) {
        var date = date1.date

        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val temp: String = date.date.toString()
        try {
            date = formatter.parse(temp)
            Log.e("dateeeeeee", date.toString() + "")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        sTime = ""

        val formateDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        weekDate=SimpleDateFormat("d").format(date)

        val day=date1.calendar.get(Calendar.DAY_OF_WEEK)
        weekDay = DateFormatSymbols().weekdays[day]
        seleteddate = formateDate
        presenter.getTimeSlots(specialistId, seleteddate, sType, sSlot)

        cbMonthly.text = "Monthly on"+" "+weekDate+"th"
        cbWeekly.text = "Weekly on $weekDay"
    }

    private class PrimeDayDisableDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val date = CalendarDay.today()
            return day.isBefore(date)
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true)
        }
    }

    private fun validateBookSession(
        date: String,
        sessionType: String,
        slot: String,
        time: String,
        isRecurring:Boolean,
        sessionCount:String,
        recurringType:String
    ): Boolean {
        if (date.isEmpty()) {
            val snackbar = Snackbar.make(llSessionbook, "Please select date", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }
        if (sessionType.isEmpty()) {
            val snackbar = Snackbar.make(llSessionbook, "Please select session type", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }
        if (slot.isEmpty()) {
            val snackbar = Snackbar.make(llSessionbook, "Please select slot", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }
        if (time.isEmpty()) {
            val snackbar = Snackbar.make(llSessionbook, "Please select proper time slot", Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
            return false
        }

        if(isRecurring)
        {
            if (sessionCount.isEmpty()) {
                val snackbar = Snackbar.make(llSessionbook, "Session count must be greater than 1", Snackbar.LENGTH_LONG)
                snackbar.changeFont()
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snackbar.show()
                return false
            }
            if (sessionCount.toInt() < 2) {
                val snackbar = Snackbar.make(llSessionbook, "Session count must be greater than 1", Snackbar.LENGTH_LONG)
                snackbar.changeFont()
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snackbar.show()
                return false
            }

            if (recurringType.isEmpty()) {
                val snackbar = Snackbar.make(llSessionbook, "Please select recurring session interval", Snackbar.LENGTH_LONG)
                snackbar.changeFont()
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snackbar.show()
                return false
            }

            return true
        }
        return true
    }


    private fun setDate()
    {
        for (i in date.indices)
        {
            val dateModel=DateModel(date[i])
            mListDate.add(dateModel)
        }
        customDateAdapter = CustomDateAdapter(this, mListDate)

    }

    private fun setDay()
    {
        val day= arrayOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")

        for (i in day.indices)
        {
            val dateModel=DateModel(day[i])
            mListDay.add(dateModel)
        }
        customDateAdapterDay = CustomDateAdapter(this, mListDay)
    }
}