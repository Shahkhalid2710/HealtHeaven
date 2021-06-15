package com.applocum.connecttomyhealth.ui.booksession.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.dateTimeUTCFormat
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.booksession.adapters.AvailableTimeClickAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import com.applocum.connecttomyhealth.ui.confirmbooking.activities.ConfirmBookingActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.activity_session_book.*
import kotlinx.android.synthetic.main.activity_session_book.calendarView
import kotlinx.android.synthetic.main.activity_session_book.ivBack
import kotlinx.android.synthetic.main.activity_session_book.rvAvailableTime
import kotlinx.android.synthetic.main.custom_small_progress.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SessionBookActivity : BaseActivity(), View.OnClickListener, BookSessionPresenter.View, OnDateSelectedListener {
    private var selectSession = ""
    private var sType = ""
    private var sSlot = ""
    private var seleteddate = ""
    private var sTime = ""
    lateinit var specialist: Specialist
    private lateinit var commonData: Common

    @Inject
    lateinit var presenter: BookSessionPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_session_book

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        specialist = intent.getSerializableExtra("specialist") as Specialist


        btnPhoneCall.setOnClickListener {
            sType = "phone_call_appointment"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btnPhoneCall.setBackgroundResource(R.drawable.custom_btn)
            btnVideoCall.setBackgroundResource(R.drawable.default_button)
            btnFaceToFace.setBackgroundResource(R.drawable.default_button)
            btnPhoneCall.setTextColor(Color.WHITE)
            btnVideoCall.setTextColor(Color.parseColor("#008976"))
            btnFaceToFace.setTextColor(Color.parseColor("#008976"))
        }

        btnVideoCall.setOnClickListener {
            sType = "online_appointment"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btnPhoneCall.setBackgroundResource(R.drawable.default_button)
            btnVideoCall.setBackgroundResource(R.drawable.custom_btn)
            btnFaceToFace.setBackgroundResource(R.drawable.default_button)
            btnPhoneCall.setTextColor(Color.parseColor("#008976"))
            btnVideoCall.setTextColor(Color.WHITE)
            btnFaceToFace.setTextColor(Color.parseColor("#008976"))
        }

        btnFaceToFace.setOnClickListener {
            sType = "offline_appointment"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btnPhoneCall.setBackgroundResource(R.drawable.default_button)
            btnVideoCall.setBackgroundResource(R.drawable.default_button)
            btnFaceToFace.setBackgroundResource(R.drawable.custom_btn)
            btnPhoneCall.setTextColor(Color.parseColor("#008976"))
            btnVideoCall.setTextColor(Color.parseColor("#008976"))
            btnFaceToFace.setTextColor(Color.WHITE)
        }

        btn10Mins.setOnClickListener {
            sSlot = "10"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btn10Mins.setBackgroundResource(R.drawable.custom_btn)
            btn20Mins.setBackgroundResource(R.drawable.default_button)
            btn30Mins.setBackgroundResource(R.drawable.default_button)
            btn10Mins.setTextColor(Color.WHITE)
            btn20Mins.setTextColor(Color.parseColor("#008976"))
            btn30Mins.setTextColor(Color.parseColor("#008976"))
        }

        btn20Mins.setOnClickListener {
            sSlot = "20"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btn10Mins.setBackgroundResource(R.drawable.default_button)
            btn20Mins.setBackgroundResource(R.drawable.custom_btn)
            btn30Mins.setBackgroundResource(R.drawable.default_button)
            btn10Mins.setTextColor(Color.parseColor("#008976"))
            btn20Mins.setTextColor(Color.WHITE)
            btn30Mins.setTextColor(Color.parseColor("#008976"))
        }

        btn30Mins.setOnClickListener {
            sSlot = "30"
            presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
            btn10Mins.setBackgroundResource(R.drawable.default_button)
            btn20Mins.setBackgroundResource(R.drawable.default_button)
            btn30Mins.setBackgroundResource(R.drawable.custom_btn)
            btn10Mins.setTextColor(Color.parseColor("#008976"))
            btn20Mins.setTextColor(Color.parseColor("#008976"))
            btn30Mins.setTextColor(Color.WHITE)
        }

        btn10Mins.performClick()

        switchMultiSessions.setOnClickListener(this)

        ivMinus.setOnClickListener { decreaseInteger() }
        ivPlus.setOnClickListener { increaseInteger() }

        cbDaily.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "1"
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbWeeklyonthursday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "2"
                cbDaily.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbMonthly1stThursday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "3"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbAnuallyonApril13th.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "4"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbEveryWeekday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "5"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbCustom.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession = "6"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
            }
        }

        RxView.clicks(btnContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                if (validateBookSession(seleteddate, sType, sSlot, sTime)) {
                    val intent = Intent(this, ConfirmBookingActivity::class.java)
                    val appointment = userHolder.getBookAppointmentData()
                    appointment.appointmentDateTime = dateTimeUTCFormat(sTime)
                    appointment.appointmentTime = sTime
                    appointment.appointmentType = sType
                    appointment.appointmentSlot = sSlot
                    appointment.appointmentDate = seleteddate
                    userHolder.saveBookAppointmentData(appointment)
                    intent.putExtra("commonData", commonData)
                    startActivity(intent)
                }
            }

        calendarView.setOnDateChangedListener(this)
        calendarView.addDecorator(PrimeDayDisableDecorator())
    }

    override fun onClick(v: View?) {
        if (switchMultiSessions.isChecked) {
            llMultiSessions.visibility = View.VISIBLE
        } else {
            llMultiSessions.visibility = View.GONE
        }
    }

    private fun increaseInteger() {
        display(etSessions.text.toString().toInt() + 1)
    }

    private fun decreaseInteger() {
        display(etSessions.text.toString().toInt() - 1)
    }

    private fun display(number: Int) {
        etSessions.setText("$number")
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

    @SuppressLint("SimpleDateFormat")
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

        val formateDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        seleteddate = formateDate
        presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
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

    private fun validateBookSession(date: String, sessionType: String, slot: String, time: String): Boolean {
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
        return true
    }
}