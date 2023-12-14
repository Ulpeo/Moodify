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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var binding: FragmentNewEntryBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewEntryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        // get or instantiate DiaryDB
        val diaryDB = DiaryDB.getInstance(this.requireActivity())
        val moodsDB = MoodsDB.getInstance(this.requireActivity())

        // firebase
        var mAuth = FirebaseAuth.getInstance()
        var auth = Firebase.auth

        with(binding) {
            btnSaveEntry.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val date = (entryDate.year).toString() + "/" + (entryDate.month + 1).toString() + "/" + entryDate.dayOfMonth
                    if(diaryDB.diaryEntryDAO().getEntry(auth.currentUser!!.email!!, date) != null) {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "Only one entry per day!", Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        val entry = DiaryEntry(
                            0,
                            auth.currentUser!!.email!!,
                            date,
                            dailyGratitude.text.toString(),
                            freeExpression.text.toString()
                        )

                        diaryDB.diaryEntryDAO().insert(entry)
                    }
                }
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Entry saved!", Snackbar.LENGTH_SHORT
                ).show()
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
}