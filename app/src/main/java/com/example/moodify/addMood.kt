package com.example.moodify

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.moodify.databinding.FragmentAddMoodBinding
import com.example.moodify.databinding.FragmentMeditateBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addMood.newInstance] factory method to
 * create an instance of this fragment.
 */
class addMood : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAddMoodBinding
    lateinit var moodsDB: MoodsDB
    lateinit var mAuth: FirebaseAuth
    lateinit var auth: FirebaseAuth

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
        binding = FragmentAddMoodBinding.inflate(inflater, container, false)

        with(binding) {
            anxious.setOnClickListener {
                saveMood("Anxious")
            }

            sad.setOnClickListener {
                saveMood("Sad")
            }

            soso.setOnClickListener {
                saveMood("So so")
            }

            good.setOnClickListener {
                saveMood("Good")
            }

            happy.setOnClickListener {
                saveMood("Happy")
            }
        }

        // get mood database instance
        moodsDB = MoodsDB.getInstance(this.requireActivity())

        // firebase
        mAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addMood.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addMood().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun saveMood(moodDescription: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date = LocalDateTime.now().format(formatter)
        GlobalScope.launch(Dispatchers.IO) {
            if(moodsDB.moodDAO().getMood(auth.currentUser!!.email!!, date) != null) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Only one mood per day!", Snackbar.LENGTH_SHORT
                ).show()
            } else {
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