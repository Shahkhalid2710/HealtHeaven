package com.applocum.connecttomyhealth.ui.booksession

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.adapters.AvailableTimeClickAdapter
import com.applocum.connecttomyhealth.ui.booksession.adapters.SessionTypeAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import com.applocum.connecttomyhealth.ui.confirmbooking.ConfirmBookingActivity
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.activity_session_book.*
import kotlinx.android.synthetic.main.activity_session_book.cbAnuallyonApril13th
import kotlinx.android.synthetic.main.activity_session_book.cbCustom
import kotlinx.android.synthetic.main.activity_session_book.cbDaily
import kotlinx.android.synthetic.main.activity_session_book.cbEveryWeekday
import kotlinx.android.synthetic.main.activity_session_book.cbMonthly1stThursday
import kotlinx.android.synthetic.main.activity_session_book.cbWeeklyonthursday
import kotlinx.android.synthetic.main.activity_session_book.rvAvailableTime
import kotlinx.android.synthetic.main.activity_session_book.rvSelectSlot
import kotlinx.android.synthetic.main.activity_session_book.rvSessionType
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SessionBookActivity : BaseActivity(), View.OnClickListener,BookSessionPresenter.View,
    OnDateSelectedListener {
    private val mListSessionType: ArrayList<SessionType> = ArrayList()
    private val mListSelectSlot: ArrayList<SessionType> = ArrayList()
    private var selectSession = ""

    private var sType=""
    private var sSlot=""
    private var seleteddate = ""

    @Inject
    lateinit var presenter: BookSessionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        val sessionType1 = SessionType("Phone")
        val sessionType2 = SessionType("Video")
        val sessionType3 = SessionType("Face to Face")
        mListSessionType.add(sessionType1)
        mListSessionType.add(sessionType2)
        mListSessionType.add(sessionType3)

        rvSessionType.layoutManager = GridLayoutManager(this,3)
        rvSessionType.adapter = SessionTypeAdapter(this, mListSessionType,object :SessionTypeAdapter.ItemClickListner{
            override fun onItemClick(sessionType: SessionType, position: Int) {
                when(position)
                {
                    0-> sType="phone_call_appointment"
                    1-> sType="online_appointment"
                    2-> sType="offline_appointment"
                }
                presenter.getTimeSlots(3260,seleteddate,sType,sSlot)
            }
        })

        val sessionType4 = SessionType("10 min")
        val sessionType5 = SessionType("20 min")
        val sessionType17 = SessionType("30 min")

        mListSelectSlot.add(sessionType4)
        mListSelectSlot.add(sessionType5)
        mListSelectSlot.add(sessionType17)
        rvSelectSlot.layoutManager =GridLayoutManager(this,4)
        rvSelectSlot.adapter = SessionTypeAdapter(this, mListSelectSlot,object :SessionTypeAdapter.ItemClickListner{
            override fun onItemClick(sessionType: SessionType, position: Int) {
                when(position)
                {
                    0-> sSlot="10"
                    1-> sSlot="20"
                    2-> sSlot="30"
                }
                presenter.getTimeSlots(3260,seleteddate,sType,sSlot)
            }
        })

        switchMultiSessions.setOnClickListener(this)
        
        ivMinus.setOnClickListener { decreaseInteger() }
        ivPlus.setOnClickListener { increaseInteger() }

        cbDaily.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="1"
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbWeeklyonthursday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="2"
                cbDaily.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbMonthly1stThursday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="3"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbAnuallyonApril13th.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="4"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbEveryWeekday.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbEveryWeekday.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="5"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbCustom.isChecked = false
            }
        }

        cbCustom.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectSession="6"
                cbDaily.isChecked = false
                cbWeeklyonthursday.isChecked = false
                cbMonthly1stThursday.isChecked = false
                cbAnuallyonApril13th.isChecked = false
                cbEveryWeekday.isChecked = false
            }
        }

        btnContinue.setOnClickListener {
            startActivity(Intent(this, ConfirmBookingActivity::class.java))
        }

        calendarView.setOnDateChangedListener(this)
        calendarView.addDecorator(PrimeDayDisableDecorator())

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_session_book

    override fun onClick(v: View?) {
        if (switchMultiSessions.isChecked()) {
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
        rvAvailableTime.layoutManager = GridLayoutManager(this, 4)
        rvAvailableTime.adapter = AvailableTimeClickAdapter(this, list,object :AvailableTimeClickAdapter.ItemClickListner{
            override fun onItemClick(time: Time, position: Int) {
            }
        })
    }

    override fun displaymessage(message: String) {
        val snackbar = Snackbar.make(llSessionbook, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        snackbar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date1: CalendarDay,
        selected: Boolean
    ) {
        var date=date1.date
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val temp:String =date.date.toString()
        try {
            date = formatter.parse(temp)
            Log.e("dateeeeeee", date.toString() + "")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val formateDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        seleteddate=formateDate
        presenter.getTimeSlots(3260,seleteddate,sType,sSlot)
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
}