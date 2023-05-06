package com.github.deviants.wisewallet.presentation.adding

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.deviants.wisewallet.R
import com.github.deviants.wisewallet.data.transaction.Transaction
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.EXPENSE
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.INCOME
import com.github.deviants.wisewallet.databinding.FragmentAddTransactionBinding
import com.github.deviants.wisewallet.presentation.*
import com.github.deviants.wisewallet.presentation.details.ItemDetailFragmentArgs
import com.github.deviants.wisewallet.presentation.list.ExpensesViewModelFactory
import com.github.deviants.wisewallet.presentation.list.TransactionsViewModel
import java.util.*


class AddTransactionFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var firstCategory: String

    private val viewModel: TransactionsViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    lateinit var transaction: Transaction
    private var date: Long? = null
    private var transactionType = EXPENSE
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId

        initViews()


        val expenseCategories = resources.getStringArray(R.array.expense_categories)
        val incomeCategories = resources.getStringArray(R.array.income_categories)

        var prevView: View? = null
        val adapter = CategoryAdapter(object : CategoryActionListener {
            override fun onClick(newCategory: String, v: View?) {
                category = newCategory
                //TODO(FIX selection)
                if (prevView != null) {
                    prevView?.elevation = 4F
                    prevView?.alpha = 1F
                    prevView?.isClickable = true
                }
                prevView = v
                prevView?.elevation = 0F
                prevView?.alpha = 0.5F
                prevView?.isClickable = false
            }
        })
        val layoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        adapter.categories = expenseCategories.toList()
        firstCategory = adapter.categories.first()
        binding.categoriesRv.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radiobutton_expense -> {
                    adapter.categories = expenseCategories.toList()
                    firstCategory = adapter.categories.first()
                    category = firstCategory
                    binding.categoriesRv.adapter = adapter
                    binding.itemIsCompulsory.visibility = View.VISIBLE
                    transactionType = EXPENSE
                }
                R.id.radiobutton_income -> {
                    adapter.categories = incomeCategories.toList()
                    firstCategory = adapter.categories.first()
                    category = firstCategory
                    binding.categoriesRv.adapter = adapter
                    binding.itemIsCompulsory.visibility = View.GONE
                    transactionType = INCOME
                }
            }
        }

        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                transaction = selectedItem
                bind(transaction)
                binding.radioGroup.check(
                    if (transaction.transactionType == EXPENSE) R.id.radiobutton_expense
                    else R.id.radiobutton_income
                )
            }

        } else {
            binding.radioGroup.check(R.id.radiobutton_expense)
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun initViews() {
        binding.itemIsCompulsory.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> binding.itemIsCompulsory.text = getString(R.string.compulsory_expense)
                false -> binding.itemIsCompulsory.text = getString(R.string.incompulsory_expense)
            }
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            date = calendar.timeInMillis
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun bind(transaction: Transaction) {
        binding.apply {
            category = transaction.transactionCategory
            itemPrice.setText(transaction.transactionPrice.toString())
            itemIsCompulsory.isChecked = transaction.isCompulsory
            calendarView.date = transaction.date
            saveAction.setOnClickListener { updateItem() }
            binding.itemDescription.setText(transaction.transactionDescription)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            category ?: firstCategory,
            binding.itemPrice.text.toString(),
            if (date != null) date.toString() else binding.calendarView.date.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                transactionType,
                category ?: firstCategory,
                binding.itemPrice.text.toString(),
                binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString(),
                binding.itemDescription.text.toString()
            )
            goBack()
        } else {
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                transactionType,
                category ?: firstCategory,
                this.binding.itemPrice.text.toString(),
                this.binding.itemIsCompulsory.isChecked.toString(),
                if (date != null) date.toString() else binding.calendarView.date.toString(),
                binding.itemDescription.text.toString()
            )
            goBack()
        } else {
            binding.itemPrice.error = getString(R.string.InputError)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.adding_fragment_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                binding.saveAction.callOnClick()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}
