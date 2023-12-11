package com.example.moodify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moodify.databinding.FragmentNewEntryBinding
import com.google.android.material.snackbar.Snackbar
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

        // get or instantiate DiaryEntriesDB
        val diaryEntriesDB = DiaryEntriesDB.getInstance(this.requireActivity())

        with(binding) {
            btnSaveEntry.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val entry = DiaryEntry(
                        (entryDate.year).toString() + "/" + (entryDate.month + 1).toString() + "/" + entryDate.dayOfMonth,
                        dailyGratitude.text.toString(),
                        freeExpression.text.toString()
                    )

                    diaryEntriesDB.diaryEntryDAO().insert(entry)
                }
                val mySnackbar = Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Entry saved!", Snackbar.LENGTH_SHORT
                )
                mySnackbar.show()
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