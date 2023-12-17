package com.example.moodify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.moodify.databinding.FragmentNewEntryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date
import android.util.Log
import android.widget.TextView
import android.os.Looper
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var binding: FragmentNewEntryBinding
lateinit var diaryDB: DiaryDB
lateinit var moodsDB: MoodsDB
lateinit var mAuth: FirebaseAuth
lateinit var auth: FirebaseAuth
var entry: DiaryEntry? = null

/**
 * A simple [Fragment] subclass.
 * Use the [NewEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewEntryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val date: String
        get() = requireArguments().getString("DATE")
            ?: throw IllegalArgumentException("Argument date required")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentNewEntryBinding.inflate(inflater, container, false)

        diaryDB = DiaryDB.getInstance(this.requireActivity())
        moodsDB = MoodsDB.getInstance(this.requireActivity())

        // firebase
        mAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        // Initialize datePicker
        if(date != null) {
            var dateSplit = date.split("/")
            binding.entryDate.updateDate(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())
            Log.d("Test", dateSplit.toString())
        }

        // display the entry if one already exists for that user and date
        displayEntry(date, true)

        with(binding) {
            btnSaveEntry.setOnClickListener {
                if(btnSaveEntry.text == "Save entry") {
                    GlobalScope.launch(Dispatchers.IO) {
                        val dateConverted = (entryDate.year).toString() + "/" + (entryDate.month + 1).toString() + "/" + entryDate.dayOfMonth
                        var existingEntry = diaryDB.diaryEntryDAO().getEntry(auth.currentUser!!.email!!, dateConverted)
                        if(existingEntry != null) {
                            existingEntry.dailyGratitude = dailyGratitude.text.toString()
                            existingEntry.freeExpression = freeExpression.text.toString()
                            diaryDB.diaryEntryDAO().update(existingEntry)
                            Snackbar.make(
                                requireActivity().findViewById(android.R.id.content),
                                "Entry updated!", Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            val entry = DiaryEntry(
                                0,
                                auth.currentUser!!.email!!,
                                dateConverted,
                                dailyGratitude.text.toString(),
                                freeExpression.text.toString()
                            )
                            diaryDB.diaryEntryDAO().insert(entry)
                            Snackbar.make(
                                requireActivity().findViewById(android.R.id.content),
                                "Entry saved!", Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    with(binding) {
                        dailyGratitude.setEnabled(true)
                        freeExpression.setEnabled(true)
                        btnSaveEntry.text = "Save entry"
                    }
                }
            }

            btnSaveMood.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val date = (entryDate.year).toString() + "/" + (entryDate.month + 1).toString() + "/" + entryDate.dayOfMonth
                    if(moodsDB.moodDAO().getMood(auth.currentUser!!.email!!, date) != null) {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Only one mood per day!", Snackbar.LENGTH_SHORT
                        ).show()
                    } else {

                        val checkedMood = moodSelect.checkedRadioButtonId
                        val selectedRadioButton = requireActivity().findViewById(checkedMood) as RadioButton
                        val moodDescription = selectedRadioButton.text.toString()

                        val mood = Mood(
                            0,
                            auth.currentUser!!.email!!,
                            date,
                            moodDescription
                        )

                        moodsDB.moodDAO().insert(mood)
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Entry saved!", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            entryDate.setOnDateChangedListener { view, year, month, dayOfMonth ->
                displayEntry(year.toString() + "/" + month.toString() + "/" + dayOfMonth.toString(), false)
            }
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewEntryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewEntryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun displayEntry(queryDate: String, createLooper: Boolean) {
        // Display existing entry if applicable
        GlobalScope.async(Dispatchers.Main) {
            val job: Job = launch(context = Dispatchers.Default) {
                var dateSplit = queryDate.split("/")
                var dateConverted = dateSplit[0] + "/" + (dateSplit[1].toInt() + 1).toString() + "/" + dateSplit[2]
                entry = diaryDB.diaryEntryDAO().getEntry(auth.currentUser!!.email!!, dateConverted)
            }
            job.join()
            if(entry != null) {
                with(binding) {
                    dailyGratitude.setEnabled(false)
                    dailyGratitude.setText(entry?.dailyGratitude ?: "")
                    freeExpression.setEnabled(false)
                    freeExpression.setText(entry?.freeExpression ?: "")
                    btnSaveEntry.text = "Edit entry"
                }
            } else {
                with(binding) {
                    dailyGratitude.setEnabled(true)
                    dailyGratitude.setText("")
                    freeExpression.setEnabled(true)
                    freeExpression.setText("")
                }

            }
        }


    }
}