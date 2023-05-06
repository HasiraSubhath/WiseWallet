package com.github.deviants.wisewallet.presentation.list

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.deviants.wisewallet.R
import com.github.deviants.wisewallet.databinding.FragmentTransactionListBinding
import com.github.deviants.wisewallet.data.transaction.Transaction
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.ALL_TRANSACTIONS
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.EXPENSE
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.INCOME
import com.github.deviants.wisewallet.presentation.ExpensesApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter

    private val viewModel: TransactionsViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as ExpensesApplication).database.itemDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRV()
    }

    private fun initRV() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.floatingActionButton.isShown) binding.floatingActionButton.hide()
                if (dy < 0 && !binding.floatingActionButton.isShown) binding.floatingActionButton.show()
            }

            /*override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) binding.floatingActionButton.show()
                super.onScrollStateChanged(recyclerView, newState)
            }*/
        })
        initRVAdapter()
    }


    private fun initViews() {
        binding.floatingActionButton.setOnClickListener {
            val action = TransactionListFragmentDirections.actionNavigationListToAddItemFragment(
                    -1,
                    getString(R.string.add_fragment_title)
                )
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun initRVAdapter() {
        adapter = TransactionAdapter(object : TransactionAdapter.Listener {
            override fun onDetailInfo(itemId: Int) {
                val action = TransactionListFragmentDirections.actionNavigationListToItemDetailFragment(
                        itemId
                    )
                findNavController().navigate(action)
            }
        })
        binding.recyclerView.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = activity?.applicationContext?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.transaction_types,
                R.layout.item_toolbar_spinner
            )
        }
        adapter?.setDropDownViewResource(R.layout.item_toolbar_spinner)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                lifecycleScope.launchWhenStarted {
                    when (position) {
                        0 -> {
                            submitList(viewModel.replaceList(ALL_TRANSACTIONS))
                        }
                        1 -> {
                            submitList(viewModel.replaceList(EXPENSE))
                        }
                        2 -> {
                            submitList(viewModel.replaceList(INCOME))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                submitList(viewModel.replaceList(ALL_TRANSACTIONS))
            }
        }
    }

    private fun correctDataTitle(data: List<Transaction>?): MutableList<Transaction>? {
        return viewModel.correctDataTitle(data)
    }

    private fun submitList(newList: LiveData<List<Transaction>>) {
        newList.observe(viewLifecycleOwner) {
            adapter.submitList(correctDataTitle(newList.value))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.clear_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteAll()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
