package com.applocum.connecttomyhealth.ui.booksession.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.convertCurrentDate
import com.applocum.connecttomyhealth.ui.booksession.presenters.BookSessionPresenter
import com.applocum.connecttomyhealth.ui.booksession.adapters.AvailableTimeAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.Common
import com.applocum.connecttomyhealth.ui.booksession.models.Time
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.activity_session_book.*
import kotlinx.android.synthetic.main.custom_small_progress.view.*
import kotlinx.android.synthetic.main.fragment_availability.*
import kotlinx.android.synthetic.main.fragment_availability.calendarView
import kotlinx.android.synthetic.main.fragment_availability.rvAvailableTime
import kotlinx.android.synthetic.main.fragment_availability.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AvailabilityFragment : Fragment(), OnDateSelectedListener, BookSessionPresenter.View {
    private var sType = ""
    private var sSlot = ""
    private var seleteddate = ""
    lateinit var specialist: Specialist
    lateinit var v: View

    @Inject
    lateinit var presenter: BookSessionPresenter

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_availability, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectview(this)

        specialist = arguments?.getSerializable("specialist") as Specialist

        RxView.clicks(v.btnPhoneCall).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "phone_call_appointment"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btnPhoneCall.setBackgroundResource(R.drawable.custom_btn)
                v.btnVideoCall.setBackgroundResource(R.drawable.default_button)
                v.btnFaceToFace.setBackgroundResource(R.drawable.default_button)
                v.btnPhoneCall.setTextColor(Color.WHITE)
                v.btnVideoCall.setTextColor(Color.parseColor("#008976"))
                v.btnFaceToFace.setTextColor(Color.parseColor("#008976"))
            }

        RxView.clicks(v.btnVideoCall).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "online_appointment"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btnPhoneCall.setBackgroundResource(R.drawable.default_button)
                v.btnVideoCall.setBackgroundResource(R.drawable.custom_btn)
                v.btnFaceToFace.setBackgroundResource(R.drawable.default_button)
                v.btnPhoneCall.setTextColor(Color.parseColor("#008976"))
                v.btnVideoCall.setTextColor(Color.WHITE)
                v.btnFaceToFace.setTextColor(Color.parseColor("#008976"))
            }

        RxView.clicks(v.btnFaceToFace).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sType = "offline_appointment"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btnPhoneCall.setBackgroundResource(R.drawable.default_button)
                v.btnVideoCall.setBackgroundResource(R.drawable.default_button)
                v.btnFaceToFace.setBackgroundResource(R.drawable.custom_btn)
                v.btnPhoneCall.setTextColor(Color.parseColor("#008976"))
                v.btnVideoCall.setTextColor(Color.parseColor("#008976"))
                v.btnFaceToFace.setTextColor(Color.WHITE)
            }

        RxView.clicks(v.btn10Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "10"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btn10Mins.setBackgroundResource(R.drawable.custom_btn)
                v.btn20Mins.setBackgroundResource(R.drawable.default_button)
                v.btn30Mins.setBackgroundResource(R.drawable.default_button)
                v.btn10Mins.setTextColor(Color.WHITE)
                v.btn20Mins.setTextColor(Color.parseColor("#008976"))
                v.btn30Mins.setTextColor(Color.parseColor("#008976"))
            }

        RxView.clicks(v.btn20Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "20"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btn10Mins.setBackgroundResource(R.drawable.default_button)
                v.btn20Mins.setBackgroundResource(R.drawable.custom_btn)
                v.btn30Mins.setBackgroundResource(R.drawable.default_button)
                v.btn10Mins.setTextColor(Color.parseColor("#008976"))
                v.btn20Mins.setTextColor(Color.WHITE)
                v.btn30Mins.setTextColor(Color.parseColor("#008976"))
            }

        RxView.clicks(v.btn30Mins).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                sSlot = "30"
                presenter.getTimeSlots(specialist.id, seleteddate, sType, sSlot)
                v.btn10Mins.setBackgroundResource(R.drawable.default_button)
                v.btn20Mins.setBackgroundResource(R.drawable.default_button)
                v.btn30Mins.setBackgroundResource(R.drawable.custom_btn)
                v.btn10Mins.setTextColor(Color.parseColor("#008976"))
                v.btn20Mins.setTextColor(Color.parseColor("#008976"))
                v.btn30Mins.setTextColor(Color.WHITE)
            }

        v.calendarView.setOnDateChangedListener(this)
        v.calendarView.addDecorator(PrimeDayDisableDecorator())
        v.calendarView.selectedDate = CalendarDay.today()

        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        val date = day.toString() + "/" + (month + 1) + "/" + year

        seleteddate = convertCurrentDate(date)
        v.btn10Mins.performClick()

        return v
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

    override fun getTimeSlot(list: ArrayList<Time>) {
        if (list.isEmpty()) {
            v.rvAvailableTime.visibility = View.GONE
            v.NotAvailabeTime.visibility = View.VISIBLE
        } else {
            val availableTimeAdapter = AvailableTimeAdapter(requireActivity(), list)
            v.NotAvailabeTime.visibility = View.GONE
            v.rvAvailableTime.visibility = View.VISIBLE
            rvAvailableTime.layoutManager = GridLayoutManager(requireActivity(), 4)
            rvAvailableTime.adapter = availableTimeAdapter
            availableTimeAdapter.notifyDataSetChanged()
        }
    }

    override fun displaymessage(message: String) {
        val snackBar = Snackbar.make(llAvailability, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackBar.show()
    }

    override fun viewProgress(isShow: Boolean) {
        v.progressSmall.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun getPrice(common: Common) {}

    override fun noInternet(isConnect: Boolean) {

        if(!isConnect)
        {
            val snackBar = Snackbar.make(llAvailability,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }
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

    fun newInstance(specialist: Specialist): AvailabilityFragment {
        val args = Bundle()
        args.putSerializable("specialist", specialist)
        val fragment = AvailabilityFragment()
        fragment.arguments = args
        return fragment
    }
}

